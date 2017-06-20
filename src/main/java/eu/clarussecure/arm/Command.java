package eu.clarussecure.arm;

import eu.clarussecure.proxy.access.SimpleMongoUserAccess;

public abstract class Command{
	// All the commands of the Security Administrator use the login, Password and/or Identify Files
	protected String loginID = "";
	protected String password = "";
	protected String identityFilePath = "";

	public abstract CommandReturn execute() throws CommandExecutionException;

	public abstract boolean parseCommandArgs(String[] args) throws CommandParserException;
	
	public boolean verifyRights(String profile) throws CommandExecutionException{
        // This method will return true iff the user exists, is correctly authenticated AND has the given profile.
        // TODO - Implement "... has the given profile OR BETTER"
		SimpleMongoUserAccess auth = SimpleMongoUserAccess.getInstance();;
        try{
            // Check if the user exists
            if(!auth.identify(this.loginID))
                throw new CommandExecutionException("The user '" + this.loginID + "' was not found as a registered user.");

            // Authenticate the user
            if(!auth.authenticate(this.loginID, this.password))
                throw new CommandExecutionException("The authentication of the user '" + this.loginID + "' failed.");

            // Check is the user is authroized to execute this command
            if(!auth.userProfile(this.loginID).equals(profile))
                throw new CommandExecutionException("The user '" + this.loginID + "' is not authorized to execute this command.");
        } finally {
            auth.deleteInstance();
        }
		return true;
	}

	public boolean parseCredentials(String[] args){
		// Parse other params of the command
		try{
			for (int i=1; i<args.length; i++){
				// Parse the options:
				switch(args[i]){
					case "-l":
					case "--login":
						// The next argument will be interpreted as the login name
						this.loginID = args[++i];
						break;
					case "-p":
					case "--password":
						// The next argument will be interpreted as the password
						this.password = args[++i];
						break;
					case "-i":
						// The next argument will be interpreted as the login name
						this.identityFilePath = args[++i];
						break;
					default:
						break;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e){
			// This Exception is not fatal. Missing information will be asked directly to the user while executing the command
		}

		// Check if there's any missing information
		while(this.loginID.equals("")){
			// Ask the user for the loginID
			System.out.print("loginID?");
			this.loginID = System.console().readLine();
		}

		// The next conditions implies that blank passwords are not allowed!!!
		if(this.password.equals("") && this.identityFilePath.equals("")){
			// Ask the user for the password
			System.out.print("Password?");
			this.password = new String(System.console().readPassword());
		}

		// Pathological case: no password provided. Ask for the path of the identity file
		while(this.password.equals("") && this.identityFilePath.equals("")){
			System.out.print("Identity File path:");
			this.identityFilePath = System.console().readLine();
		}

		return true;
	}
}
