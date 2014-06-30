package lejos.internal.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;

import com.sun.jna.LastErrorException;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public class NativeWifi {
	
    static final int O_ACCMODE = 0003;
    static final int O_RDONLY = 00;
    static final int O_WRONLY = 01;
    static final int O_RDWR = 02;
    static final int O_CREAT = 0100;
    static final int O_EXCL = 0200;
    static final int O_NOCTTY = 0400;
    static final int O_TRUNC = 01000;
    static final int O_APPEND = 02000;
    static final int O_NONBLOCK = 04000;
    static final int O_NDELAY = O_NONBLOCK;
    static final int O_SYNC = 04010000;
    static final int O_FSYNC = O_SYNC;
    static final int O_ASYNC = 020000;
    static final int PROT_READ = 1;
    static final int PROT_WRITE = 2;
    static final int MAP_SHARED = 1;
    static final int MAP_PRIVATE = 2;
    static final int MAP_FILE = 0;
	
	public static final int AF_INET = 2;
	public static final int SOCK_DGRAM = 2;
	
	public static final int SIOCSIWESSID  = 0x8B1A;
	public static final int SIOCGIWESSID  = 0x8B1B;
	public static final int SIOCSIWNICKN  = 0x8B1C;
	public static final int SIOCGIWNICKN  = 0x8B1D;
	
	public static final int SIOCSIWAP     = 0x8B14;          /* set access point MAC addresses */
	public static final int SIOCGIWAP     = 0x8B15;          /* get access point MAC addresses */
	public static final int SIOCGIWAPLIST = 0x8B17;          /* Deprecated in favor of scanning */
	public static final int SIOCSIWSCAN   = 0x8B18;          /* trigger scanning (list cells) */
	public static final int SIOCGIWSCAN   = 0x8B19; 

	public static class SockAddr extends Structure {
		public short family;
        public byte[] bd_addr = new byte[14];
        @Override
        protected List getFieldOrder()
        {
            // TODO Auto-generated method stub
            return Arrays.asList(new String[] {"family",
            "bd_addr"});
        }
	}
	
	public static class Point extends Structure {
		public Pointer p;
        public short length;
        public short flags;
        @Override
        protected List getFieldOrder()
        {
            // TODO Auto-generated method stub
            return Arrays.asList(new String[] {"p",
            "length",
            "flags"});
        }
	}
	
	public static class WReqSocket extends Structure implements Structure.ByReference  {  
        public byte[] ifname = new byte[16];
        public SockAddr sockaddr = new SockAddr();
        @Override
        protected List getFieldOrder()
        {
            // TODO Auto-generated method stub
            return Arrays.asList(new String[] {"ifname",
            "sockaddr"});
        }       
	}
	
	public static class WReqPoint extends Structure implements Structure.ByReference  {  
        public byte[] ifname = new byte[16];
        public Point point = new Point();  
        public byte[] padding = new byte[8];
        @Override
        protected List getFieldOrder()
        {
            // TODO Auto-generated method stub
            return Arrays.asList(new String[] {"ifname",
            "point",  
            "padding"});
        }
	}
	
    static class Linux_C_lib_DirectMapping {
        native public int fcntl(int fd, int cmd, int arg) throws LastErrorException;

        native public int ioctl(int fd, int cmd, byte[] arg) throws LastErrorException;

        native public int ioctl(int fd, int cmd, Pointer p) throws LastErrorException;
        
        native public int ioctl(int fd, int cmd, WReqSocket w) throws LastErrorException;
        
        native public int ioctl(int fd, int cmd, WReqPoint p) throws LastErrorException;
        
        native public int open(String path, int flags) throws LastErrorException;

        native public int close(int fd) throws LastErrorException;

        native public int write(int fd, Buffer buffer, int count) throws LastErrorException;

        native public int read(int fd, Buffer buffer, int count) throws LastErrorException;
        
        native public Pointer mmap(Pointer addr, NativeLong len, int prot, int flags, int fd,
                NativeLong off) throws LastErrorException;
        
        native public int socket(int domain, int type, int protocol) throws LastErrorException;
        
        native public int connect(int sockfd, SockAddr sockaddr, int addrlen) throws LastErrorException;

        native public int bind(int sockfd, SockAddr sockaddr, int addrlen) throws LastErrorException;
        
        native public int accept(int sockfd, SockAddr rem_addr, Pointer opt) throws LastErrorException;
        
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
    int fd;
    File jfd;
    FileChannel fc;
    
    public NativeWifi() {
    	socket = clib.socket(AF_INET, SOCK_DGRAM, 0);
    	//System.out.println("Socket is " + socket);
    	fd = clib.open("/proc/net/wireless",O_RDONLY);
        jfd = new File("/proc/net/wireless", "r");
    }
    
    /**
     * Open the specified file/device 
     * for native access.
     * @param fname the name of the file to open
     * @param flags Linux style file access flags
     * @param mode Linux style file access mode
     * @throws FileNotFoundException
     */
    public void open(String fname, int flags, int mode) throws FileNotFoundException
    {
        fd = clib.open(fname, flags);
        if (fd < 0)
            throw new FileNotFoundException("File: " + fname + " errno " + Native.getLastError());
    }

    /**
     * Attempt to read the requested number of bytes from the associated file.
     * @param buf location to store the read bytes
     * @param len number of bytes to attempt to read
     * @return number of bytes read or -1 if there is an error
     */
    public int read(byte[] buf, int len)
    {
        try
        {
            return fc.read(ByteBuffer.wrap(buf, 0, len));
        } catch (IOException e)
        {
            return -1;
        }
    }
    
    /**
     * Attempt to write the requested number of bytes to the associated file.
     * @param buf location to store the read bytes
     * @param offset the offset within buf to take data from for the write
     * @param len number of bytes to attempt to read
     * @return number of bytes read or -1 if there is an error
     */
    public int write(byte[] buf, int offset, int len)
    {
        try
        {
            return fc.write(ByteBuffer.wrap(buf, offset, len));
        } catch (IOException e)
        {
            return -1;
        }
    }
    
    /**
     * Attempt to read the requested number of byte from the associated file.
     * @param buf location to store the read bytes
     * @param offset offset with buf to start storing the read bytes
     * @param len number of bytes to attempt to read
     * @return number of bytes read or -1 if there is an error
     */
    public int read(byte[] buf, int offset, int len)
    {
        try
        {
            return fc.read(ByteBuffer.wrap(buf, offset, len));
        } catch (IOException e)
        {
            return -1;
        }
    }
    
    /**
     * Attempt to write the requested number of bytes to the associated file.
     * @param buf location to store the read bytes
     * @param len number of bytes to attempt to read
     * @return number of bytes read or -1 if there is an error
     */
    public int write(byte[] buf, int len)
    {
        try
        {
            return fc.write(ByteBuffer.wrap(buf, 0, len));
        } catch (IOException e)
        {
            return -1;
        }
    }

    /**
     * Perform a Linux style ioctl operation on the associated file.
     * @param req ioctl operation to be performed
     * @param buf byte array containing the ioctl parameters if any
     * @return Linux style ioctl return
     */
    public int ioctl(int req, byte[] buf)
    {
        return clib.ioctl(socket, req, buf);
    }
    
    /**
     * Perform a Linux style ioctl operation on the associated file.
     * @param req ioctl operation to be performed
     * @param buf pointer to ioctl parameters
     * @return Linux style ioctl return
     */
    public int ioctl(int req, WReqSocket data)
    {
        return clib.ioctl(socket, req, data);
    }
    
    /**
     * Perform a Linux style ioctl operation on the associated file.
     * @param req ioctl operation to be performed
     * @param buf pointer to ioctl parameters
     * @return Linux style ioctl return
     */
    public int ioctl(int req, WReqPoint data)
    {
        return clib.ioctl(socket, req, data);
    }

    /**
     * Close the associated file
     * @return Linux style return
     */
    public int close()
    {
        try
        {
            fc.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return clib.close(fd);
    }

}
