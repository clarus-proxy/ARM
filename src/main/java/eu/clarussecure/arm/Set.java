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
public class Set extends Command{
	
	private String username;
	private String dataspace;
	private String rights;
	
	public Set(String[] args) throws CommandParserException{
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
		CLARUSARMDAO dao = CLARUSARMDAO.getInstance();
		boolean success = dao.setRights(this.username, this.dataspace, this.rights);
		dao.deleteInstance();

		int retValue = 0;
		String retMessage = "Rights '" + this.rights + "' on the dataspace " + this.dataspace + " were correctly assigned to the user '" + this.username + "'";

		if (!success){
			retValue = 3;
			retMessage = "The User " + this.username + " could not be found.";
		}

		CommandReturn cr = new CommandReturn(retValue, retMessage);
		return cr;
	}

	public boolean parseCommandArgs(String[] args) throws CommandParserException {
		// First, sanity check
		if (!args[0].toLowerCase().equals("set"))
			throw new CommandParserException("Why a non-'set' command ended up in the 'set' part of the parser?");
		
		try{
			this.username = args[1];
		} catch (ArrayIndexOutOfBoundsException e){
			throw new CommandParserException("The field 'username' was not given and it is required.");
		}
				
		try{
			this.dataspace = args[2];
		} catch (ArrayIndexOutOfBoundsException e){
			throw new CommandParserException("The field 'dataspace' was not given and it is required.");
		}
				
		try{
			switch(args[3].toLowerCase()){
				case "r":
				case "rw":
				case "none":
					this.rights = args[3].toLowerCase();
					break;
				case "wr":
					this.rights = "rw";
					break;
				default:
					throw new CommandParserException("The 'rights' value provided could not be interpreted. Supported values are 'r', 'rw' and 'none'");
			}
		} catch (ArrayIndexOutOfBoundsException e){
			throw new CommandParserException("The field 'rights' was not given and it is required.");
		}
		return true;
	}
}
