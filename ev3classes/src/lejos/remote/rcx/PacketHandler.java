package lejos.remote.rcx;

/**
 * Abstract packet handler. 
 * Implementations must include sendPacket, receivePacket and
 * isPacketAvailable(). The other methods are optional.
 * 
 **/
public abstract class PacketHandler {
  protected PacketHandler lowerHandler;

  public PacketHandler() {
  }

  public PacketHandler(PacketHandler handler) {
    lowerHandler = handler;
  }
   
  /**
   * Set the source and destination for this connection.
   **/ 
  public void open(byte source, byte destination) {
  }

  /**
   * Reset sequence numbers for this handler
   **/
  public void reset() {
  }

  /**
   * Set or unset the listen flag to keep a PC serial tower alive
   * @param listen true to set listen mode, else false
   **/
  public void setListen(boolean listen) {
    lowerHandler.setListen(listen);
  }

  /** Send a packet.
   * @param packet the bytes to send
   * @param len the number of bytes to send
   * @return true if the send was successful, else false
   */
  public abstract boolean sendPacket(byte [] packet, int len);

  /** Receive a packet.
   * @param buffer the buffer to receive the packet into
   * @return the number of bytes received
   */
  public abstract int receivePacket(byte [] buffer);

  public int receiveAck(byte [] buffer) {
    return 0;
  }

  /**
   * Check if a packet is available
   * @return true if a Packet is available, else false
   */
  public abstract boolean isPacketAvailable();

  /**
   * Check if an ack is available
   * @return true if a ack is available, else false
   */
  public boolean isAckAvailable() {
    return false;
  }

  /**
   * Close this packet handler and all lower layers.
   */
  public void close() {
    lowerHandler.close();
  }

  /**
   * Get the last error.
   * @return the error number, or zero for success
   **/
  public int getError() {
    return lowerHandler.getError();
  }
}


