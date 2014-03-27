package lejos.internal.io;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import com.sun.jna.LastErrorException;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;

public class NativeSocket {
	
	public static final int AF_BLUETOOTH = 31;
	
	public static class SockAddr extends Structure implements Structure.ByReference  {  
        public short family = AF_BLUETOOTH;
        public byte[] bd_addr = new byte[6];
        public byte channel = 1;
        @Override
        protected List getFieldOrder()
        {
            // TODO Auto-generated method stub
            return Arrays.asList(new String[] {"family",
            "bd_addr",
            "channel"});
        }       
	}
	
    static class Linux_C_lib_DirectMapping {     

        native public int close(int fd) throws LastErrorException;

        native public int write(int fd, Buffer buffer, int count) throws LastErrorException;

        native public int read(int fd, Buffer buffer, int count) throws LastErrorException;
        
        native public int socket(int domain, int type, int protocol) throws LastErrorException;
        
        native public int connect(int sockfd, SockAddr sockaddr, int addrlen) throws LastErrorException;

        native public int bind(int sockfd, SockAddr sockaddr, int addrlen) throws LastErrorException;
        
        native public int accept(int sockfd, SockAddr rem_addr, IntByReference opt) throws LastErrorException;
        
        native public int listen(int sockfd, int channel) throws LastErrorException;

        static {
            try {
                Native.register("c");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    static Linux_C_lib_DirectMapping clib = new Linux_C_lib_DirectMapping();
    int socket;
    
    
    public NativeSocket(int domain, int type, int protocol) throws LastErrorException {
    	socket = clib.socket(domain, type, protocol);
    	System.out.println("Socket is " + socket);
    }
    
    public NativeSocket(int socket) {
    	this.socket = socket;
    }
    
    public void connect(SockAddr sockaddr, int addrlen) throws LastErrorException {
    	clib.connect(socket, sockaddr, addrlen);
    }
    
    /**
     * Attempt to read the requested number of bytes from the associated file.
     * @param buf location to store the read bytes
     * @param len number of bytes to attempt to read
     * @return number of bytes read or -1 if there is an error
     */
    public int read(byte[] buf, int len) throws LastErrorException {
    	return clib.read(socket,ByteBuffer.wrap(buf, 0, len), len);
    }
    
    /**
     * Attempt to write the requested number of bytes to the associated file.
     * @param buf location to store the read bytes
     * @param offset the offset within buf to take data from for the write
     * @param len number of bytes to attempt to read
     * @return number of bytes read or -1 if there is an error
     */
    public int write(byte[] buf, int offset, int len) throws LastErrorException {
        return clib.write(socket, ByteBuffer.wrap(buf, offset, len), len);
    }
    
    public void close() throws LastErrorException {
    	clib.close(socket);
    }
    
    public void bind(SockAddr sockaddr, int addrlen) throws LastErrorException {
    	clib.bind(socket, sockaddr, addrlen);
    }
    
    public void listen(int channel) throws LastErrorException {
    	clib.listen(socket, channel);
    } 
    
    public NativeSocket accept() throws LastErrorException {
    	SockAddr rem = new SockAddr();
    	int opt = rem.size();
    	int client = clib.accept(socket, rem, new IntByReference(opt) );
    	return new NativeSocket(client);
    } 
}
