package lejos.remote.ev3;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class RemoteRequestMenu implements Menu, Serializable {
	private static final long serialVersionUID = -2002269631228847368L;
	private ObjectInputStream is;
	private ObjectOutputStream os;
	private Socket socket;
	
	private static final int PORT = 8002;
	
	public RemoteRequestMenu(String host) throws IOException {
		socket = new Socket(host,PORT);
		is = new ObjectInputStream(socket.getInputStream());
		os = new ObjectOutputStream(socket.getOutputStream());
	}
	
	@Override
	public void runProgram(String programName) {
		MenuRequest req = new MenuRequest();
		req.request = MenuRequest.Request.RUN_PROGRAM;
		req.name = programName;
		try {
			os.writeObject(req);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void runSample(String programName) {
		MenuRequest req = new MenuRequest();
		req.request = MenuRequest.Request.RUN_SAMPLE;
		req.name = programName;
		try {
			os.writeObject(req);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void debugProgram(String programName) {
		MenuRequest req = new MenuRequest();
		req.request = MenuRequest.Request.DEBUG_PROGRAM;
		req.name = programName;
		try {
			os.writeObject(req);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public boolean deleteFile(String fileName) {
		MenuRequest req = new MenuRequest();
		req.request = MenuRequest.Request.DELETE_FILE;
		req.name = fileName;
		req.replyRequired = true;
		try {
			os.writeObject(req);
			MenuReply reply = (MenuReply) is.readObject();
			return reply.result;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
	}

	@Override
	public long getFileSize(String filename) {
		MenuRequest req = new MenuRequest();
		req.request = MenuRequest.Request.GET_FILE_SIZE;
		req.name = filename;
		req.replyRequired = true;
		try {
			os.writeObject(req);
			MenuReply reply = (MenuReply) is.readObject();
			return reply.reply;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public String[] getProgramNames() {
		MenuRequest req = new MenuRequest();
		req.request = MenuRequest.Request.GET_PROGRAM_NAMES;
		try {
			os.writeObject(req);
			MenuReply reply = (MenuReply) is.readObject();
			return reply.names;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String[] getSampleNames() {
		MenuRequest req = new MenuRequest();
		req.request = MenuRequest.Request.GET_SAMPLE_NAMES;
		try {
			os.writeObject(req);
			MenuReply reply = (MenuReply) is.readObject();
			return reply.names;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean uploadFile(String fileName, byte[] contents) {
		MenuRequest req = new MenuRequest();
		req.request = MenuRequest.Request.UPLOAD_FILE;
		req.name = fileName;
		req.contents = contents;
		req.replyRequired = true;
		try {
			os.writeObject(req);
			MenuReply reply = (MenuReply) is.readObject();
			return reply.result;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public byte[] fetchFile(String fileName) {
		MenuRequest req = new MenuRequest();
		req.request = MenuRequest.Request.FETCH_FILE;
		req.name = fileName;
		req.replyRequired = true;
		try {
			os.writeObject(req);
			MenuReply reply = (MenuReply) is.readObject();
			return reply.contents;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getSetting(String setting) {
		MenuRequest req = new MenuRequest();
		req.request = MenuRequest.Request.GET_SETTING;
		req.name = setting;
		req.replyRequired = true;
		try {
			os.writeObject(req);
			MenuReply reply = (MenuReply) is.readObject();
			return reply.value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void setSetting(String setting, String value) {
		MenuRequest req = new MenuRequest();
		req.request = MenuRequest.Request.SET_SETTING;
		req.name = setting;
		req.value = value;
		try {
			os.writeObject(req);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void deleteAllPrograms() {
		MenuRequest req = new MenuRequest();
		req.request = MenuRequest.Request.DELETE_ALL_PROGRAMS;
		try {
			os.writeObject(req);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getVersion() {
		MenuRequest req = new MenuRequest();
		req.request = MenuRequest.Request.GET_VERSION;
		req.replyRequired = true;
		try {
			os.writeObject(req);
			MenuReply reply = (MenuReply) is.readObject();
			return reply.value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getMenuVersion() {
		MenuRequest req = new MenuRequest();
		req.request = MenuRequest.Request.GET_MENU_VERSION;
		req.replyRequired = true;
		try {
			os.writeObject(req);
			MenuReply reply = (MenuReply) is.readObject();
			return reply.value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getName() {
		MenuRequest req = new MenuRequest();
		req.request = MenuRequest.Request.GET_NAME;
		req.replyRequired = true;
		try {
			os.writeObject(req);
			MenuReply reply = (MenuReply) is.readObject();
			return reply.value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void setName(String name) {
		MenuRequest req = new MenuRequest();
		req.request = MenuRequest.Request.RUN_PROGRAM;
		req.name = name;
		try {
			os.writeObject(req);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void stopProgram() {
		MenuRequest req = new MenuRequest();
		req.request = MenuRequest.Request.STOP_PROGRAM;
		try {
			os.writeObject(req);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public String getExecutingProgramName() {
		MenuRequest req = new MenuRequest();
		req.request = MenuRequest.Request.GET_EXECUTING_PROGRAM_NAME;
		req.replyRequired = true;
		try {
			os.writeObject(req);
			MenuReply reply = (MenuReply) is.readObject();
			return reply.value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void shutdown() {
		MenuRequest req = new MenuRequest();
		req.request = MenuRequest.Request.SHUT_DOWN;
		try {
			os.writeObject(req);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
