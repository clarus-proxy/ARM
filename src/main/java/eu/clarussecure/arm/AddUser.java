/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.clarussecure.arm;

import eu.clarussecure.proxy.access.CLARUSUserOperations;
import eu.clarussecure.proxy.access.SimpleMongoUserAccess;

/**
 *
 * @author diegorivera
 */
public class AddUser extends Command{
	
	private String username;
	private String password;
	
	public AddUser(String[] args) throws CommandParserException{
		parseCommandArgs(args);
	}

	public CommandReturn execute() throws CommandExecutionException {
        // Authenticate the user
        SimpleMongoUserAccess auth = SimpleMongoUserAccess.getInstance();
        if(!auth.identify(this.loginID)){
            throw new CommandExecutionException("The user '" + this.loginID + "' was not found as a registered user.");
        }
        
        if(!auth.authenticate(this.loginID, this.password)){
            throw new CommandExecutionException("The authentication of the user '" + this.loginID + "' failed.");
        }
        
        // Check is the user is authroized to execute this command
        if(!auth.userProfile(this.loginID).equals("admin")){
            throw new CommandExecutionException("The user '" + this.loginID + "' is not authorized to execute this command.");
        }
        
		// Get the DAO instanc and set the rights on the DB
		CLARUSUserOperations dao = CLARUSUserOperations.getInstance();
		boolean success = dao.addUser(this.username, this.password);
		dao.deleteInstance();

		int retValue = 0;
		String retMessage = "The User '" + this.username + "' was added.";

		if (!success){
			retValue = 3;
			retMessage = "The User " + this.username + " could not be added.";
		}

		CommandReturn cr = new CommandReturn(retValue, retMessage);
		return cr;
	}

	public boolean parseCommandArgs(String[] args) throws CommandParserException {
		// First, sanity check
		if (!args[0].toLowerCase().equals("add_user"))
			throw new CommandParserException("Why a non-'add_user' command ended up in the 'add_user' part of the parser?");
		
		try{
			this.username = args[1];
		} catch (ArrayIndexOutOfBoundsException e){
			throw new CommandParserException("The field 'username' was not given and it is required.");
		}
				
		try{
			this.password = args[2];
		} catch (ArrayIndexOutOfBoundsException e){
			throw new CommandParserException("The field 'password' was not given and it is required.");
		}
		
		// Only an admin can add a user
		this.parseCredentials(args);
		
		return true;
	}
}
