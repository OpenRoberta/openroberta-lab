package lejos.internal.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import com.sun.jna.LastErrorException;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;

public class NativeDomainSocket {
	
	public static final int AF_UNIX = 1;
	public static final int SOCK_STREAM = 1;
	
	public static final int SOL_SOCKET = 65535;
	
	private SocketInputStream is;
	private SocketOutputStream os;
	
	public static class SockAddr extends Structure implements Structure.ByReference  {  
        public short family = AF_UNIX;
        public byte[] addr = new byte[108];
       
        public SockAddr() {
        	//System.out.println("Socket address created");
        }
        
        public SockAddr(String name, boolean abs) {
        	//System.out.println("Socket address created with address = " + addr + ", abstract = " + abs);
        	System.arraycopy(name.getBytes(), 0, addr, 0, name.length());
        	//System.out.println("Array copied");
        }
	}
	
	public SockAddr sa = new SockAddr();

    static class Linux_C_lib_DirectMapping {
        native public int fcntl(int fd, int cmd, int arg) throws LastErrorException;

        native public int ioctl(int fd, int cmd, byte[] arg) throws LastErrorException;

        native public int ioctl(int fd, int cmd, Pointer p) throws LastErrorException;
        
        native public int open(String path, int flags) throws LastErrorException;

        native public int close(int fd) throws LastErrorException;

        native public int write(int fd, Buffer buffer, int count) throws LastErrorException;

        native public int read(int fd, Buffer buffer, int count) throws LastErrorException;
        
        native public int socket(int domain, int type, int protocol) throws LastErrorException;
        
        native public int connect(int sockfd, SockAddr sockaddr, int addrlen) throws LastErrorException;

        native public int bind(int sockfd, SockAddr sockaddr, int addrlen) throws LastErrorException;
        
        native public int accept(int sockfd, SockAddr rem_addr, Pointer opt) throws LastErrorException;
        
        native public int listen(int sockfd, int channel) throws LastErrorException;

        native public int getsockopt(int s, int level, int optname, byte[] optval, IntByReference optlen);
        
        native public int setsockopt(int s, int level, int optname, byte[] optval, int optlen);
        
        native public int recv(int s, Buffer buf, int len, int flags) throws LastErrorException;
        native public int recvfrom(int s, Buffer buf, int len, int flags, SockAddr from, IntByReference fromlen);

        native public int send(int s, Buffer msg, int len, int flags) throws LastErrorException;
        
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
    
    
    public NativeDomainSocket() throws LastErrorException {
    	//System.out.println("Domain Socket created");
    	socket = clib.socket(AF_UNIX, SOCK_STREAM, 0);
    	//System.out.println("Socket is " + socket);
    }
    
    public NativeDomainSocket(SockAddr addr, boolean ab) throws LastErrorException {
    	//System.out.println("Domain Socket created , abstract = " + false);
    	socket = clib.socket(AF_UNIX, SOCK_STREAM, 0);
    	//System.out.println("Socket is " + socket);
    }
    
    public NativeDomainSocket(SockAddr addr) throws LastErrorException {
    	this(addr,false);
    }
    
    public void connect(SockAddr addr) throws LastErrorException {
    	//System.out.println("Connect called with address " + addr);
    	clib.connect(socket, addr, addr.size());
    	is = new SocketInputStream(this);
    	os = new SocketOutputStream(this);
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
    
    public int send(byte[] buf, int len) {
    	return clib.send(socket,ByteBuffer.wrap(buf, 0, len), len, 0);	
    }
    
    public int recv(byte[] buf, int len) throws LastErrorException {
    	return clib.recv(socket,ByteBuffer.wrap(buf, 0, len), len, 0);
    }
    
    public void close() throws LastErrorException {
    	clib.close(socket);
    }
    
    public NativeDomainSocket accept() {
    	return this;
    }
    
    public void bind(SockAddr sockaddr) throws LastErrorException {
    	clib.bind(socket, sockaddr, sockaddr.size());
    }  
    
    public void sendCredentialByte(byte data) throws IOException
    {
    	//System.out.println("Send Credential byte " + data);
    	byte[] b = new byte[1];
    	send(b,1);
    	
    }
    
    public byte recvCredentialByte() throws IOException
    {
    	System.out.println("Read credential byte");
    	return 0;
    }
    
    public int getPeerUID()
    {
       //System.out.println("Get Peer UID");
       return 0;
    }
    
    public void setPassCred(boolean enable) throws IOException
    {
    	//System.out.println("set Pass Cred, enable = " + enable);
    }
    
    public void setBlocking(boolean enable)
    {
    	//System.out.println("Set Blocking");
    }
    
    public void setSoTimeout(int timeout)
    {
    	//System.out.println("Set timeout " + timeout);
    }
    
    public InputStream getInputStream()
    {
       return is;
    }
    
    public OutputStream getOutputStream()
    {
       return os;
    }
}
