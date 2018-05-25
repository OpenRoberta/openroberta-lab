package de.fhg.iais.roberta.components.vorwerk;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import de.fhg.iais.roberta.util.Key;

public class VorwerkCommunicator {
    public final String pathToCompilerResourcesDir;
    private String ip;
    private String username;
    private String password;
    //ports on the robot
    int sshPort = 22;

    public VorwerkCommunicator(String ip, String username, String password, String pathToCompilerResourcesDir) {
        this.ip = ip;
        this.username = username;
        this.password = password;
        this.pathToCompilerResourcesDir = pathToCompilerResourcesDir;
    }

    public VorwerkCommunicator(String pathToCompilerResourcesDir) {
        this.pathToCompilerResourcesDir = pathToCompilerResourcesDir;
    }

    public String getIp() {
        return this.ip;
    }

    public Key uploadFile(String path, String programName) throws Exception {

        Key key = null;
        List<String> fileNames = new ArrayList<String>();
        fileNames.add("__init__.py");
        fileNames.add("blockly_methods.py");
        fileNames.add("hal.py");

        Session session = createSession(this.username, this.ip, 22, this.password);
        try {
            key = sshCommand("rm " + programName);
            key = sshCommand("rm -r roberta");
            key = sshCommand("mkdir roberta");
            if ( key == Key.VORWERK_PROGRAM_UPLOAD_SUCCESSFUL ) {
                for ( String fname : fileNames ) {
                    copyLocalToRemote(session, this.pathToCompilerResourcesDir + "roberta", "roberta", fname);
                }
                copyLocalToRemote(session, path, ".", programName);
                key = sshCommand("python " + programName);
                key = sshCommand("rm " + programName);
                key = sshCommand("rm -r roberta");
            }
            session.disconnect();
        } catch ( Exception e ) {
            session.disconnect();
            key = Key.VORWERK_PROGRAM_UPLOAD_ERROR_CONNECTION_NOT_ESTABLISHED;
        }
        return key;
    }

    private static Session createSession(String user, String host, int port, String password) {
        try {
            JSch jsch = new JSch();

            Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");

            Session session = jsch.getSession(user, host, port);
            session.setConfig(config);
            session.setPassword(password);
            session.connect(5000);

            return session;
        } catch ( Exception e ) {
            System.out.println(e);
            return null;
        }
    }

    private static void copyLocalToRemote(Session session, String from, String to, String fileName) throws JSchException, IOException {
        boolean ptimestamp = true;
        from = from + "/" + fileName;

        // exec 'scp -t rfile' remotely
        String command = "scp " + (ptimestamp ? "-p" : "") + " -t " + to;
        try {
            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);

            // get I/O streams for remote scp
            OutputStream out = channel.getOutputStream();
            InputStream in = channel.getInputStream();

            channel.connect();

            if ( checkAck(in) != 0 ) {
                System.exit(0);
            }

            File _lfile = new File(from);

            if ( ptimestamp ) {
                command = "T" + _lfile.lastModified() / 1000 + " 0";
                // The access time should be sent here,
                // but it is not accessible with JavaAPI ;-<
                command += " " + _lfile.lastModified() / 1000 + " 0\n";
                out.write(command.getBytes());
                out.flush();
                if ( checkAck(in) != 0 ) {
                    System.exit(0);
                }
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

            if ( checkAck(in) != 0 ) {
                System.exit(0);
            }

            // send a content of lfile

            InputStream fis = new FileInputStream(from);

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

            if ( checkAck(in) != 0 ) {
                System.exit(0);
            }
            out.close();

            try {
                if ( fis != null ) {
                    fis.close();
                }
            } catch ( Exception ex ) {
                System.out.println(ex);
            }

            channel.disconnect();
        } catch ( Exception e ) {
            System.out.println(e.getMessage());
        }
    }

    public static int checkAck(InputStream in) throws IOException {
        int b = in.read();
        // b may be 0 for success,
        //          1 for error,
        //          2 for fatal error,
        //         -1
        if ( b == 0 ) {
            return b;
        }
        if ( b == -1 ) {
            return b;
        }

        if ( b == 1 || b == 2 ) {
            StringBuffer sb = new StringBuffer();
            int c;
            do {
                c = in.read();
                sb.append((char) c);
            } while ( c != '\n' );
            if ( b == 1 ) { // error
                System.out.print(sb.toString());
            }
            if ( b == 2 ) { // fatal error
                System.out.print(sb.toString());
            }
        }
        return b;
    }

    private Key sshCommand(String command) {
        try {
            Session session = createSession(this.username, this.ip, 22, this.password);
            if ( session != null ) {
                //open channel and send command
                Channel channel = session.openChannel("exec");
                ((ChannelExec) channel).setCommand(command);

                //set streams
                channel.setInputStream(null);
                ((ChannelExec) channel).setErrStream(System.err);
                InputStream in = channel.getInputStream();

                //establish channel connection
                channel.connect();

                //show messages on the ssh channel
                byte[] tmp = new byte[1024];
                while ( true ) {
                    while ( in.available() > 0 ) {
                        int i = in.read(tmp, 0, 1024);
                        if ( i < 0 ) {
                            break;
                        }
                        //System.out.print(new String(tmp, 0, i));
                    }
                    if ( channel.isClosed() ) {
                        if ( in.available() > 0 ) {
                            continue;
                        }
                        // LOG.info("exit-status: " + channel.getExitStatus());
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch ( Exception ee ) {
                    }
                }

                //disconnect from channel and session
                channel.disconnect();
                session.disconnect();
                return Key.VORWERK_PROGRAM_UPLOAD_SUCCESSFUL;
            } else {
                return Key.VORWERK_PROGRAM_UPLOAD_ERROR_SSH_CONNECTION;
            }
        } catch ( Exception e ) {
            System.out.println(e);
            return Key.VORWERK_PROGRAM_UPLOAD_ERROR_SSH_CONNECTION;
        }
    }

    public void updateRobotInformation(String ip, String username, String password) {
        this.ip = ip;
        this.username = username;
        this.password = password;
    }
}
