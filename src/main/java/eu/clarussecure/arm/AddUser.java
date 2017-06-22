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
public class AddUser extends Command {

    private String username;
    private String password;
    private String role;

    public AddUser(String[] args) throws CommandParserException {
        parseCommandArgs(args);
    }

    @Override
    public CommandReturn execute() throws CommandExecutionException {
        this.verifyRights("admin");

        // Get the DAO instance and set the rights on the DB
        CLARUSUserOperations dao = CLARUSUserOperations.getInstance();
        boolean success = dao.addUser(this.username, this.password, this.role);
        dao.deleteInstance();

        int retValue = 0;
        String retMessage = "The User '" + this.username + "' was added.";

        if (!success) {
            retValue = 3;
            retMessage = "The User " + this.username + " could not be added.";
        }

        CommandReturn cr = new CommandReturn(retValue, retMessage);
        return cr;
    }

    @Override
    public boolean parseCommandArgs(String[] args) throws CommandParserException {
        // First, sanity check
        if (!args[0].toLowerCase().equals("add_user"))
            throw new CommandParserException(
                    "Why a non-'add_user' command ended up in the 'add_user' part of the parser?");

        try {
            this.username = args[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new CommandParserException("The field 'username' was not given and it is required.");
        }

        try {
            this.password = args[2];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new CommandParserException("The field 'password' was not given and it is required.");
        }

        try {
            this.role = args[3];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new CommandParserException("The field 'role' was not given and it is required.");
        }

        // Only an admin can add a user
        this.parseCredentials(args);

        return true;
    }
}
