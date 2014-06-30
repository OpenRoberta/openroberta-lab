package lejos.remote.rcx;

/**
 * A Packet handler that guarantees reliable delivery using checksums,
 * acks, and a single bit sequence number.
 * 
 **/ 
public class LLCReliableHandler extends PacketHandler {
  private byte [] inPacket = new byte[3];
  private byte [] outPacket = new byte[3];
  private byte [] inAck = new byte[2];
  private byte [] outAck = new byte [2];
  private int inPacketLength = 0;

  private boolean sequence = false, receiveSequence = false;

  public LLCReliableHandler(PacketHandler handler) {
    super(handler);
  }

  /**
   * Reset sequence numbers.
   **/
  public void reset() {
    sequence = false;
    receiveSequence = false;
  }

  /** Send a packet.
   * @param packet the bytes to send
   * @param len the number of bytes to send
   * @return true if send successful, else false
   */
  public boolean sendPacket(byte [] packet, int len) {
    byte b = (byte) (0xf0 + len); // Send byte
    if (sequence) b |= 8;
    outPacket[0] = b;
    int sum = b;
    for(int i=0; i<len; i++) {
      sum += packet[i];
      outPacket[i+1] = packet[i];
    }
    outPacket[len+1] = (byte) sum;

    for(int j=0;;j++) {
      int sendTime = (int)System.currentTimeMillis();
      lowerHandler.sendPacket(outPacket,len+2);
      do {
        Thread.yield();
      } while (!(lowerHandler.isAckAvailable()) && 
               (int)System.currentTimeMillis() < sendTime+500);
      if (lowerHandler.isAckAvailable()) {
        int retLen = lowerHandler.receiveAck(inAck);
        if (retLen == 2 && 
            inAck[0] == (byte) (b & 0xf8) &&
            inAck[0] == inAck[1]) {
          sequence = !sequence;
          return true; 
        }
      }
    }
  }

  /** Receive a packet.
   * @param buffer the buffer to receive the packet into
   * @return the number of bytes received
   */
  public int receivePacket(byte [] buffer) {
    int temp = inPacketLength-2;
    for(int i=0;i<temp;i++) buffer[i] = inPacket[i+1];
    inPacketLength = 0;
    return temp;
  }

  /** Check if a packet is available.
   * @return true if a packet is available, else false
   */
  public boolean isPacketAvailable() {
    if (inPacketLength > 0) return true;
    while (lowerHandler.isPacketAvailable()) {
      int len = lowerHandler.receivePacket(inPacket);
      int sum = 0;
      for(int i=0; i<len-1; i++) sum += inPacket[i];
      if ((byte) sum == inPacket[len-1]) {
        outAck[0] = (byte) (inPacket[0] & 0xf8);
        outAck[1] = outAck[0];
        lowerHandler.sendPacket(outAck, 2);
      } else {
        continue;
      }

      // Check the sequence
      if ((inPacket[0] & 0x8) == (receiveSequence ? 0x8 : 0x0)) {
        inPacketLength = len;
        receiveSequence = !receiveSequence;
        return true;
      }
    }
    return false;
  }
}


