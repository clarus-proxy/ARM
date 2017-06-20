package eu.clarussecure.arm;

public class Main{
	static public void main(String[] args){
		// Get the instance of the Command Parser.
		// This object will create the correct instance of the Command and delegate its parsing correctly.
		CommandParser parser = CommandParser.getInstance();

		// Parse the command. This method will do all the checkings required to validate the command
		Command com = null;
		try{
			com = parser.parse(args);
		} catch (CommandParserException e){
			System.err.println(e.getMessage());
			System.exit(1);
		}

		// Start the delegation. This command will make the required modifications to the set of policies.
		CommandReturn cr = null;
		try{
			cr = com.execute();
		} catch (CommandExecutionException e){
			System.err.println(e.getMessage());
			System.exit(2);
		}

		// Print the return on the screen, and alert in case of an error
		if (cr.getReturnValue() == 0) {
			System.out.println(cr.getReturnInfo());
		} else {
			System.out.println("The command '" + args[0] + "' could not be completed:");
			System.out.println(cr.getReturnInfo());
		}

		//printPolicies(policies);

		// Return the value to the shell as well
		System.exit(cr.getReturnValue());
	}
}
