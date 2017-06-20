package eu.clarussecure.arm;

import eu.clarussecure.arm.dao.CLARUSARMDAO;

public class Set extends Command {

    private String username;
    private String dataspace;
    private String rights;

    public Set(String[] args) throws CommandParserException {
        parseCommandArgs(args);
    }

    @Override
    public CommandReturn execute() throws CommandExecutionException {
        this.verifyRights("admin");

        // Get the DAO instanc and set the rights on the DB
        CLARUSARMDAO dao = CLARUSARMDAO.getInstance();
        boolean success = dao.setRights(this.username, this.dataspace, this.rights);
        dao.deleteInstance();

        int retValue = 0;
        String retMessage = "Rights '" + this.rights + "' on the dataspace " + this.dataspace
                + " were correctly assigned to the user '" + this.username + "'";

        if (!success) {
            retValue = 3;
            retMessage = "The User " + this.username + " could not be found.";
        }

        CommandReturn cr = new CommandReturn(retValue, retMessage);
        return cr;
    }

    @Override
    public boolean parseCommandArgs(String[] args) throws CommandParserException {
        // First, sanity check
        if (!args[0].toLowerCase().equals("set"))
            throw new CommandParserException("Why a non-'set' command ended up in the 'set' part of the parser?");

        try {
            this.username = args[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new CommandParserException("The field 'username' was not given and it is required.");
        }

        try {
            this.dataspace = args[2];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new CommandParserException("The field 'dataspace' was not given and it is required.");
        }

        try {
            switch (args[3].toLowerCase()) {
            case "r":
            case "rw":
            case "none":
                this.rights = args[3].toLowerCase();
                break;
            case "wr":
                this.rights = "rw";
                break;
            default:
                throw new CommandParserException(
                        "The 'rights' value provided could not be interpreted. Supported values are 'r', 'rw' and 'none'");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new CommandParserException("The field 'rights' was not given and it is required.");
        }
        return true;
    }
}
