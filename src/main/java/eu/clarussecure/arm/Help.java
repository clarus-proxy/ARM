/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.clarussecure.arm;

/**
 *
 * @author diegorivera
 */

import java.io.InputStream;
import java.io.IOException;

import java.util.Set;

public class Help extends Command{
	//TODO - Put other data from the command as fields of the object
	private int policyID;
	
	public Help(String[] args) throws CommandParserException{
		parseCommandArgs(args);
	}

	public CommandReturn execute() throws CommandExecutionException{
		// Prepare the output
		String data = "";

		try{
			// Get the embedded resource from the jar file
			InputStream f = Help.class.getClassLoader().getResource("help.txt").openStream();
			byte[] buf = new byte[(int) f.available()];
			
			// Read the content
			f.read(buf);
			data = new String(buf, "UTF-8");
		} catch (IOException e){
			data = "help not found";
		}

		CommandReturn cr = new CommandReturn(0, data);
		return cr;
	}

	public boolean parseCommandArgs(String[] args) throws CommandParserException{
		// Default case, nothing to do here

		return true;
	}
}
