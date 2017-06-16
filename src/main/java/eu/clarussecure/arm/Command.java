/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.clarussecure.arm;

import eu.clarussecure.proxy.access.CLARUSAccess;
import eu.clarussecure.proxy.access.SimpleMongoUserAccess;

/**
 *
 * @author diegorivera
 */
public abstract class Command{
	// All the commands of the Security Administrator use the login, Password and/or Identify Files
	protected String loginID = "";
	protected String password = "";
	protected String identityFilePath = "";

	public abstract CommandReturn execute() throws CommandExecutionException;

	public abstract boolean parseCommandArgs(String[] args) throws CommandParserException;
	
	public boolean verifyRights(){
		CLARUSAccess auth = SimpleMongoUserAccess.getInstance();
		
		if(!auth.identify(this.loginID))
			return false;
		
		return auth.authenticate(this.loginID, this.password);
	}

	public boolean parseCredentials(String[] args){
		// Parse other params of the command
		try{
			for (int i=1; i<args.length; i++){
				// Parse the options:
				switch(args[i]){
					case "-l":
					case "--login":
						// The next argument will be interpreted as the login name
						this.loginID = args[++i];
						break;
					case "-p":
					case "--password":
						// The next argument will be interpreted as the password
						this.password = args[++i];
						break;
					case "-i":
						// The next argument will be interpreted as the login name
						this.identityFilePath = args[++i];
						break;
					default:
						break;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e){
			// This Exception is not fatal. Missing information will be asked directly to the user while executing the command
		}

		// Check if there's any missing information
		while(this.loginID.equals("")){
			// Ask the user for the loginID
			System.out.print("loginID?");
			this.loginID = System.console().readLine();
		}

		// The next conditions implies that blank passwords are not allowed!!!
		if(this.password.equals("") && this.identityFilePath.equals("")){
			// Ask the user for the password
			System.out.print("Password?");
			this.password = new String(System.console().readPassword());
		}

		// Pathological case: no password provided. Ask for the path of the identity file
		while(this.password.equals("") && this.identityFilePath.equals("")){
			System.out.print("Identity File path:");
			this.identityFilePath = System.console().readLine();
		}

		return true;
	}
}
