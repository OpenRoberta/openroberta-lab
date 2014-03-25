package lejos.remote.rcx;

import lejos.hardware.port.I2CPort;
import lejos.utility.Delay;

/**
 * Packet handler than implement the LLC packet protocol.
 * Deals with packets and acks. 
 * Supports independent streams of data in both directions.
 * 
 **/
public class LLCHandler extends PacketHandler {

  private byte op;
  private boolean gotAck = false;
  private boolean gotPacket = false;
  private byte [] inPacket = new byte [3];
  private byte [] ackPacket = new byte [2];
  private int inPacketLength;

  public LLCHandler(I2CPort port) {
    LLC.init(port);
  }

  /** Send a packet.
   * @param packet the bytes to send
   * @param len the number of bytes to send
   * @return true if the send was successful, else false
   */
  public boolean sendPacket(byte [] packet, int len) {
    synchronized (this) {
      boolean res = LLC.sendBytes(packet, len);
      Delay.msDelay(100);
      return res;
    }
  }  

  /** Receive a packet.
   * @param buffer the buffer to receive the packet into
   * @return the number of bytes received
   */
  public int receivePacket(byte [] buffer) {
    gotPacket = false;
    for(int i=0;i<inPacketLength;i++) buffer[i] = inPacket[i];
    return inPacketLength;
  }

  /** Receive an ack.
   * @param buffer the buffer to receive the ack into
   * @return the number of bytes received
   */
  public int receiveAck(byte [] buffer) {
    gotAck = false;
    for(int i=0;i<2;i++) buffer[i] = ackPacket[i];
    return 2;
  }

  /**
   * Search for the next packet or ack and read it into the relevant buffer
   * and set the relevant flag to say we've got it. 
   **/
  private void getOp() {
    for(;;) {
      int r = LLC.read();
      if (r < 0) return;
      op = (byte) r;
      if ((op & 0xf7) == 0xf1) {
        gotPacket = true;
        inPacket[0] = op;
        int extra = (op & 0x7) + 1; // Add 1 for the checksum
        for(int i=0;i<extra;i++) inPacket[i+1] = (byte) LLC.receive();
        inPacketLength = extra+1;
        return;
      }
      if ((op & 0xf7) == 0xf0) {
        gotAck = true;
        ackPacket[0] = op;
        ackPacket[1] = (byte) LLC.receive();
        return;
      }
    }
  }

  /**
   * Check if a packet is available
   * @return true if a Packet is available, else false
   */
  public boolean isPacketAvailable() {
    synchronized (this) {
      if (gotPacket) return true;
      getOp();
      return gotPacket;
    }
  }
 
  /**
   * Check if an Ack is available
   * @return true if ack is available, else false
   */
  public boolean isAckAvailable() {
    synchronized (this) {
      if (gotAck) return true;
      getOp();
      return gotAck;
    }
  }
}


