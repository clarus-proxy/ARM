/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.clarussecure.arm;

import eu.clarussecure.arm.dao.CLARUSARMDAO;

/**
 *
 * @author diegorivera
 */
public class ShowRights extends Command{
	
	private String username;
	
	public ShowRights(String[] args) throws CommandParserException{
		parseCommandArgs(args);
	}

	public CommandReturn execute() throws CommandExecutionException {
		// Get the DAO instanc and set the rights on the DB
		CLARUSARMDAO dao = CLARUSARMDAO.getInstance();
		String rights = dao.showRights(this.username);
		dao.deleteInstance();

		CommandReturn cr = new CommandReturn(0, rights);
		return cr;
	}

	public boolean parseCommandArgs(String[] args) throws CommandParserException {
		// First, sanity check
		if (!args[0].toLowerCase().equals("show_rights"))
			throw new CommandParserException("Why a non-'show_rights' command ended up in the 'show_rights' part of the parser?");
		
		try{
			this.username = args[1];
		} catch (ArrayIndexOutOfBoundsException e){
			throw new CommandParserException("The field 'username' was not given and it is required.");
		}
		
		this.parseCredentials(args);
		
		return true;
	}
}
