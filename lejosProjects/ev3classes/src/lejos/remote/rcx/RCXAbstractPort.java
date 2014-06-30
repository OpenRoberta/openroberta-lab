package lejos.remote.rcx;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lejos.utility.Delay;

/** 
 * RCXAbstractPort provides an interface similar to java.net.Socket
 * Adapted from original code created by the LEGO3 Team at DTU-IAU
 * RCXAbstractPort implements input and output stream handling and input
 * buffering. It uses a packet handler for sending and receivng packets.
 * This version is abstract because it has no packet handler defined.
 * Specific versions of RCXAbstractPort override the constructor and
 * set up the packet handler to use a specific protocol stack. 
 * 
 * @author Brian Bagnall
 * @author Lawrie Griffiths
 * 
 */
public abstract class RCXAbstractPort {

   private boolean portOpen = true;
   private Listener listener;
   private int timeOut = 0;

   private RCXInputStream rcxin;
   private RCXOutputStream rcxout;
   protected PacketHandler packetHandler;

   /**
    * Constructor for the RCXAbstractPort.
    * Opens the port, and sets the protocol packet handler.
    * @param handler the packet handler
    */
   public RCXAbstractPort(PacketHandler handler) throws IOException {
      packetHandler = handler;
      rcxin = new RCXInputStream(this);
      rcxout = new RCXOutputStream(packetHandler);
      listener = new Listener();
      listener.setDaemon(true);
      listener.start();
   }

   /** Returns an input stream for this RCXPort.
    * @return an input stream for reading bytes from this RCXPort.
    */
   public InputStream getInputStream() {
      return (InputStream) rcxin;
   }

   /** Returns an output stream for this RCXPort.
    * @return an output stream for writing bytes to this RCXPort.
    */
   public OutputStream getOutputStream() {
      return (OutputStream) rcxout;
   }

   /**
    * Resets sequence numbers for this port 
    */
   public void reset() {
     packetHandler.reset();
   }

   /** Closes this RCXPort, stopping the Listener thread.
    */
   public void close() {
      portOpen = false;
   }

   /** Getter for property timeOut.
    * @return Value of property timeOut.
    */
   public int getTimeOut() {
      return timeOut;
   }

   /** Setter for property timeOut.
    * @param timeOut New value of property timeOut.
    */
   public void setTimeOut(int timeOut) {
      this.timeOut = timeOut;
   }

   private byte [] inPacket = new byte[2];

   /** Listener class runs a thread that reads and buffers bytes.
    * Allows a maximum of two bytes in a packet.
    */
   private class Listener extends Thread {
      public void run() {
         while (portOpen) {
            if (packetHandler.isPacketAvailable()) {
              int r = packetHandler.receivePacket(inPacket);
              for(int i=0;i<r;i++) rcxin.add(inPacket[i]);
            }
            Delay.msDelay(10);
         }
      }
   }

   /**
    * Hidden inner class extending InputStream. 
    */
   private class RCXInputStream extends InputStream {

      /** The default buffer size for the InputStream
       */
      public static final int bufferSize = 32;
      private byte[] buffer = new byte[bufferSize];
      private int current = 0, last = 0;
      private RCXAbstractPort dataPort;
      private IOException ioe = new IOException();

      /** Creates new RCXInputStream
      * @param port The RCXAbstractPort which should deliver data for to this InputStream
      */
      public RCXInputStream(RCXAbstractPort port) {
         dataPort = port;
      }

      /** Checks if there is any data avaliable on the InputStream
      * @throws IOException is never thrown
      * @return The number of bytes avaliable on the InputStream
      */
      public int available() throws IOException {
         if (last < current)
            return bufferSize-(current-last);
         else
            return last-current;
      }

      /** Read a single byte from the InputStream. Returns value as
      * an int value between 0 and 255.
      * @throws IOException is thrown when the read is timed out
      * @return A data byte from the stream
      */
      public synchronized int read() throws IOException {
         int time1 = (int)System.currentTimeMillis();
         int timeOut = dataPort.getTimeOut();
         while (available() == 0) {
            if (timeOut != 0 && ((int)System.currentTimeMillis()-time1 > timeOut)) {
                  throw ioe;
            }
            Delay.msDelay(10);
         }

         synchronized (buffer) {
            int b = buffer[current++];
            if (current == bufferSize)
               current = 0;

            if(b < 0) b = b + 256;
            return b;
         }
      }

      /** Add a data byte to the stream
      * This method should only be called by the RCXPort that
      * created the RCXInputStream
      * @param b The data byte
      */
      void add(byte b) {
         synchronized (buffer) {
            buffer[last++] = b;
            if (last == bufferSize)
               last = 0;
         }
      }
   }

   /** Hidden inner class extending OutputStream. 
    */
   private class RCXOutputStream extends OutputStream {

      private PacketHandler packetHandler;
      private IOException ioe = new IOException();

      /** Creates new RCXOutputStream
      * @param handler the packet handler used to send data
      */
      public RCXOutputStream(PacketHandler handler) {
         packetHandler = handler;
      }

      private byte [] bytePacket = new byte[1];

      /** Write a byte to the OutputStream.
      * @param b The byte.
      * @throws IOException if the byte could not be written to the stream
      */
      public synchronized void write(int b) throws IOException {
         bytePacket[0] = (byte) b;
         if (!packetHandler.sendPacket(bytePacket,1)) throw ioe;
      }
   }
}


