/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.clarussecure.arm;

import eu.clarussecure.datamodel.Policy;
import eu.clarussecure.secpolmgmt.dao.CLARUSPolicyDAO;

/**
 *
 * @author diegorivera
 */
public class ListDataspaces extends Command{
	
	public ListDataspaces(String[] args) throws CommandParserException{
		parseCommandArgs(args);
	}

	public CommandReturn execute() throws CommandExecutionException {
		// Get the DAO instance and set the policies from the DB
		CLARUSPolicyDAO dao = CLARUSPolicyDAO.getInstance();
		java.util.Set<Policy> policies = dao.getPolicies();
		dao.deleteInstance();
		
		// Iterate over the policies, extracting the endpoints
		String list = "Registered Endpoints:\n";
		
		for(Policy p : policies){
			list += p.getEndpoint().getEndpointURL() + "\n";
		}

		CommandReturn cr = new CommandReturn(0, list);
		return cr;
	}

	public boolean parseCommandArgs(String[] args) throws CommandParserException {
		// First, sanity check
		if (!args[0].toLowerCase().equals("list_dataspaces"))
			throw new CommandParserException("Why a non-'list_dataspaces' command ended up in the 'list_datspaces' part of the parser?");
		
		// Parse the credentials of the command
		this.parseCredentials(args);
		
		return true;
	}
}
