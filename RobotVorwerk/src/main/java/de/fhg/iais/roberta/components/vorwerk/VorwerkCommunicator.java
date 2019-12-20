package de.fhg.iais.roberta.components.vorwerk;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import de.fhg.iais.roberta.util.dbc.DbcException;

public class VorwerkCommunicator {
    public final String pathToCompilerResourcesDir;
    private String ip;
    private String username;
    private String password;
    //ports on the robot
    int sshPort = 22;

    public VorwerkCommunicator(String pathToCompilerResourcesDir) {
        this.pathToCompilerResourcesDir = pathToCompilerResourcesDir;
    }

    public void setCredentials(String ip, String username, String password) {
        this.ip = ip;
        this.username = username;
        this.password = password;
    }

    /**
     * upload a file. If not successful, throw an exception
     */
    public void uploadFile(String path, String programName) throws Exception {
        List<String> fileNames = new ArrayList<>();
        fileNames.add("__init__.py");
        fileNames.add("blockly_methods.py");
        fileNames.add("hal.py");

        Session session = null;
        try {
            session = createSession(this.username, this.ip, 22, this.password);
            sshCommand("rm " + programName);
            sshCommand("rm -r roberta");
            sshCommand("mkdir roberta");
            for ( String fname : fileNames ) {
                copyLocalToRemote(session, this.pathToCompilerResourcesDir + "roberta", "roberta", fname);
            }
            copyLocalToRemote(session, path, ".", programName);
            sshCommand("python " + programName);
            sshCommand("rm " + programName);
            sshCommand("rm -r roberta");
        } finally {
            try {
                if ( session != null ) {
                    session.disconnect();
                }
            } catch ( Exception e ) {
                // OK
            }
        }
    }

    public String getIp() {
        return this.ip;
    }

    /**
     * create an ssh command. If not successful, throw an exception
     */
    private void sshCommand(String command) throws Exception {
        Session session = null;
        Channel channel = null;
        try {
            session = createSession(this.username, this.ip, 22, this.password);
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);
            InputStream in = channel.getInputStream();
            channel.connect();

            //show messages on the ssh channel
            byte[] tmp = new byte[1024];
            while ( true ) {
                while ( in.available() > 0 ) {
                    int i = in.read(tmp, 0, 1024);
                    if ( i < 0 ) {
                        break;
                    }
                }
                if ( channel.isClosed() ) {
                    if ( in.available() > 0 ) {
                        continue;
                    }
                    break;
                }
                Thread.sleep(1000);
            }
        } finally {
            try {
                if ( channel != null ) {
                    channel.disconnect();
                }
            } catch ( Exception e ) {
                // OK
            }
            try {
                if ( session != null ) {
                    session.disconnect();
                }
            } catch ( Exception e ) {
                // OK
            }
        }
    }

    /**
     * create a session. If not successful, throw an exception
     *
     * @return the session; throw exception if creation is not successful
     */
    private static Session createSession(String user, String host, int port, String password) throws Exception {
        JSch jsch = new JSch();

        Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");

        Session session = jsch.getSession(user, host, port);
        session.setConfig(config);
        session.setPassword(password);
        session.connect(5000);

        return session;
    }

    /**
     * copy local file to remote. If not successful, throw an exception
     */
    private static void copyLocalToRemote(Session session, String from, String to, String fileName) throws Exception {
        Channel channel = null;
        InputStream fis = null;
        try { //NOSONAR : proposes to use try-resource, but Channel doesn't implement Closeable
            boolean ptimestamp = true;
            from = from + "/" + fileName;

            // exec 'scp -t rfile' remotely
            String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + to; //NOSONAR : ptimestamp used to enhance readability, even if it is constant
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            // get I/O streams for remote scp
            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();

            channel.connect();
            checkAck(in);

            File _lfile = new File(from);

            if ( ptimestamp ) {
                command = "T" + _lfile.lastModified() / 1000 + " 0";
                // The access time should be sent here,
                // but it is not accessible with JavaAPI ;-<
                command += " " + _lfile.lastModified() / 1000 + " 0\n";
                out.write(command.getBytes());
                out.flush();
                checkAck(in);
            }

            // send "C0644 filesize filename", where filename should not include '/'
            long filesize = _lfile.length();
            command = "C0644 " + filesize + " ";
            if ( from.lastIndexOf('/') > 0 ) {
                command += from.substring(from.lastIndexOf('/') + 1);
            } else {
                command += from;
            }
            command += "\n";
            out.write(command.getBytes());
            out.flush();
            checkAck(in);

            // send content of lfile
            fis = new FileInputStream(from);
            byte[] buf = new byte[1024];
            while ( true ) {
                int len = fis.read(buf, 0, buf.length);
                if ( len <= 0 ) {
                    break;
                }
                out.write(buf, 0, len); //out.flush();
            }

            // send '\0'
            buf[0] = 0;
            out.write(buf, 0, 1);
            out.flush();
            checkAck(in);
        } finally {
            try {
                if ( channel != null ) {
                    channel.disconnect();
                }
            } catch ( Exception e ) {
                // OK
            }
            try {
                if ( fis != null ) {
                    fis.close();
                }
            } catch ( Exception e ) {
                // OK
            }
        }
    }

    /**
     * check the response from a channel. Return if ok; if an error occurred, throw an exception
     *
     * @throws Exception
     */
    private static void checkAck(InputStream in) throws Exception {
        int b = in.read();
        // b may be 0 for success, 1 for error, 2 for fatal error, -1 for success (???)
        if ( b == 0 || b == -1 ) {
            return;
        }
        StringBuffer sb = new StringBuffer();
        int c;
        do {
            c = in.read();
            sb.append((char) c);
        } while ( c != '\n' );
        throw new DbcException("Error. code: " + b + " msg: " + sb.toString());
    }
}
