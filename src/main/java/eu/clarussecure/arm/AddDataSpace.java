package eu.clarussecure.arm;

import eu.clarussecure.arm.dao.CLARUSARMDAO;

public class AddDataSpace extends Command {
    
    private String dataspaceName;
    
    public AddDataSpace(String[] command) throws CommandParserException{
        parseCommandArgs(command);
    }

    @Override
    public CommandReturn execute() throws CommandExecutionException {
        this.verifyRights("admin");
        
        // Add the dataset to the list of registered ones
        CLARUSARMDAO dao = CLARUSARMDAO.getInstance();
        boolean success = dao.addDataspace(this.dataspaceName, this.loginID);
        dao.deleteInstance();
        
        CommandReturn cr = null;
        
        if(success)
            cr = new CommandReturn(0, "The dataspace '" + this.dataspaceName + "' was correctly registered.");
        else
            cr = new CommandReturn(3, "The dataspace '" + this.dataspaceName + "' was not correctly registered.");
        return cr;
    }

    @Override
    public boolean parseCommandArgs(String[] args) throws CommandParserException {
		// First, sanity check
		if (!args[0].toLowerCase().equals("add_dataspace"))
			throw new CommandParserException("Why a non-'add_dataspace' command ended up in the 'add_dataspace' part of the parser?");
		
		try{
			this.dataspaceName = args[1];
		} catch (ArrayIndexOutOfBoundsException e){
			throw new CommandParserException("The field 'dataspace' was not given and it is required.");
		}
        
        this.parseCredentials(args);
        
        return true;
    }
}
