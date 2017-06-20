package eu.clarussecure.arm;

import eu.clarussecure.arm.dao.CLARUSARMDAO;
import eu.clarussecure.datamodel.Policy;
import eu.clarussecure.proxy.access.SimpleMongoUserAccess;
import eu.clarussecure.secpolmgmt.dao.CLARUSPolicyDAO;

public class ListDataspaces extends Command{
	
	public ListDataspaces(String[] args) throws CommandParserException{
		parseCommandArgs(args);
	}

    @Override
	public CommandReturn execute() throws CommandExecutionException {
        this.verifyRights("admin");
        
		// Get the DAO instance and set the policies from the DB
        /*
		CLARUSPolicyDAO dao = CLARUSPolicyDAO.getInstance();
		java.util.Set<Policy> policies = dao.getPolicies();
		dao.deleteInstance();
        
		// Iterate over the policies, extracting the endpoints
		String list = "Registered Endpoints:\n";
		
		for(Policy p : policies){
			list += p.getEndpoint().getEndpointURL() + "\n";
		}
        */
        
        // Get the list of registered dataspaces
        CLARUSARMDAO dao = CLARUSARMDAO.getInstance();
        java.util.Set<String> dataspaces = dao.listDataspaces();
        dao.deleteInstance();

        // Form the return string
        String list = "";
        for(String dataspace : dataspaces){
            list += dataspace;
        }
        
		CommandReturn cr = new CommandReturn(0, list);
		return cr;
	}

    @Override
	public boolean parseCommandArgs(String[] args) throws CommandParserException {
		// First, sanity check
		if (!args[0].toLowerCase().equals("list_dataspaces"))
			throw new CommandParserException("Why a non-'list_dataspaces' command ended up in the 'list_datspaces' part of the parser?");
		
		// Parse the credentials of the command
		this.parseCredentials(args);
		
		return true;
	}
}
