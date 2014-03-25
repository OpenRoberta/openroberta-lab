package de.fraunhofer.roberta;

import java.io.File;
import java.io.IOException;

public class CommandLine
{

	public CommandLine()
	{
		//
	}
	
	public void hideLeJOS() throws IOException, InterruptedException
	{
		ProcessBuilder shellCommand = new ProcessBuilder("sh", "-c", "echo s > menufifo");
		shellCommand.directory(new File("/home/root/lejos/bin/utils"));
		Process p = shellCommand.start();
		p.waitFor();
	}
	
	public void showLeJOS() throws IOException, InterruptedException
	{
		ProcessBuilder shellCommand = new ProcessBuilder("sh", "-c", "echo r > menufifo");
		shellCommand.directory(new File("/home/root/lejos/bin/utils"));
		Process p = shellCommand.start();
		p.waitFor();
	}
	
	public void runProgram(String programName) throws IOException, InterruptedException
	{
		ProcessBuilder shellCommand = new ProcessBuilder("sh", "-c", "jrun -jar " + programName);
		shellCommand.directory(new File("/home/lejos/programs"));
		Process p = shellCommand.start();
		p.waitFor();
	}
}
