/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.clarussecure.arm;

import eu.clarussecure.arm.dao.CLARUSARMDAO;
import eu.clarussecure.proxy.access.SimpleMongoUserAccess;

/**
 *
 * @author diegorivera
 */
public class ListUsers extends Command{
	
	public ListUsers(String[] args) throws CommandParserException{
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
        // Only the admin can run this command?
        if(!auth.userProfile(this.loginID).equals("admin")){
            throw new CommandExecutionException("The user '" + this.loginID + "' is not authorized to execute this command.");
        }
        
		// Get the DAO instanc and set the rights on the DB
		CLARUSARMDAO dao = CLARUSARMDAO.getInstance();
		String users = dao.listUsers();
		dao.deleteInstance();

		CommandReturn cr = new CommandReturn(0, users);
		return cr;
	}

	public boolean parseCommandArgs(String[] args) throws CommandParserException {
		// First, sanity check
		if (!args[0].toLowerCase().equals("list_users"))
			throw new CommandParserException("Why a non-'list_users' command ended up in the 'list_users' part of the parser?");
		
		// Parse the credentials of the command
		this.parseCredentials(args);
		
		return true;
	}
}
