package de.fraunhofer.roberta;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import lejos.utility.Delay;

public class RobertaLauncher
{
	// predefined variables
	private final URL serverURL;
	private final String programDir = "/home/lejos/programs";
	private final String programName = "HelloWorld.jar";
	/////////////////////////////////////////////////////////
	
	private final RobertaGUI gui;
	private final CommandLine shellCommands;

	public RobertaLauncher() throws MalformedURLException
	{
		this.serverURL = new URL("http://10.0.1.10:8080/hello/txt");
		this.gui = new RobertaGUI();
		this.shellCommands = new CommandLine();
	}
	
	public void execute() throws ClassNotFoundException, IOException, InterruptedException
	{
		launchRoberta();
		//downloadProgram();
		startProgram(programName);
		Delay.msDelay(3000);
		shutdownRoberta();
	}
	
	private void launchRoberta() throws IOException, InterruptedException
	{
		// only required for command line start
		shellCommands.hideLeJOS();
		//////////////////////////
		gui.robertaActive();
		Delay.msDelay(3000);
		//keyMap();
	}
	
	private void keyMap()
	{
		Keyboard keyboard = new Keyboard();
		System.out.println(keyboard.getString());
	}
	
	private void downloadProgram() throws ClassNotFoundException, IOException
	{
		ServerConnector serverConnector = new ServerConnector();
		saveProgram(serverConnector.downloadProgram(serverURL));
	}
	
	private void saveProgram(byte[] program) throws IOException
	{
		File userProgram = new File(programDir, programName);
		FileOutputStream fis = new FileOutputStream(userProgram);
		try
		{
			fis.write(program);
		}
		finally
		{
			fis.close();
		}
	}
	
	private void startProgram(String programName) throws IOException, InterruptedException
	{
		gui.robertaPassive();
		shellCommands.runProgram(programName);
		gui.robertaActive();
	}
	
	private void shutdownRoberta() throws IOException, InterruptedException
	{
		gui.robertaPassive();
		shellCommands.showLeJOS();
		System.exit(0);
	}
}
