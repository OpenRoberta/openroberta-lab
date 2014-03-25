package lejos.remote.rcx;

import lejos.hardware.port.I2CPort;

import java.io.IOException;

/** 
 * RCXPort provides an interface similar to java.net.Socket
 * Adapted from original code created by the LEGO3 Team at DTU-IAU
 * Uses Reliable low-level comms for communication.
 * This is a two-layer comms stack consisting of LLCReliableHandler
 * and LLCHandler. It ensures that all packets get through.
 * Communication will stop when the IR tower is not in view or in range,
 * and will resume when it comes back into view.
 * RCXPort does not support addressing - it broadcasts messages to all devices.
 * 
 * @author Brian Bagnall
 * @author Lawrie Griffiths
 * 
 */
public class RCXPort extends RCXAbstractPort {
  public RCXPort(I2CPort port) throws IOException {
    super((PacketHandler) new LLCReliableHandler(
                       (PacketHandler) new LLCHandler(port)));
  }
}


