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
public class CommandParser{
	// Singleton implementation.
	private static CommandParser instance = null;

	private CommandParser(){
	}

	public static CommandParser getInstance(){
		if(CommandParser.instance == null)
			CommandParser.instance = new CommandParser();

		return CommandParser.instance;
	}

	public Command parse(String[] command) throws CommandParserException{
		Command com = null;

		// This is where the delegation occurs.
		// Depending on the command provided, the parser will create the correct instance of the Command object
		// Extend this switch with new cases to support new commands
		switch(command[0].toLowerCase()){
			case "list_users":
				com = new ListUsers(command);
				break;
			case "list_dataspaces":
				com = new ListDataspaces(command);
				break;
			case "show_rights":
				com = new ShowRights(command);
				break;
			case "set":
				com = new eu.clarussecure.arm.Set(command);
				break;
			// Non defaults commands
			case "add_user":
				com = new AddUser(command);
				break;
			// Commands related to the authorization of the users
			case "list_non_authorized":
			case "add_authroization":
			case "list_authorized":
			case "remove_authorization":
			default:
				throw new CommandParserException("Unrecognized command '" + command[0] + "'");
		}
		return com;
	}
}
