package eu.clarussecure.arm;

import eu.clarussecure.arm.dao.CLARUSARMDAO;
import eu.clarussecure.proxy.access.SimpleMongoUserAccess;

public class ListUsers extends Command {

    public ListUsers(String[] args) throws CommandParserException {
        parseCommandArgs(args);
    }

    @Override
    public CommandReturn execute() throws CommandExecutionException {
        this.verifyRights("admin");

        // Get the DAO instanc and set the rights on the DB
        CLARUSARMDAO dao = CLARUSARMDAO.getInstance();
        String users = dao.listUsers();
        dao.deleteInstance();

        CommandReturn cr = new CommandReturn(0, users);
        return cr;
    }

    @Override
    public boolean parseCommandArgs(String[] args) throws CommandParserException {
        // First, sanity check
        if (!args[0].toLowerCase().equals("list_users"))
            throw new CommandParserException(
                    "Why a non-'list_users' command ended up in the 'list_users' part of the parser?");

        // Parse the credentials of the command
        this.parseCredentials(args);

        return true;
    }
}
