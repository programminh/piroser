package org.umontreal.ift3913.piroser;

import java.util.ArrayList;

/**
 * Operation Class
 * @author Truong Pham
 *
 */
public class Operation {
	private String name;
	private String type;
	private ArrayList<Argument> arguments;
	
	/**
	 * Constructor
	 * @param name The operation's name
	 * @param type The operation's return type
	 * @param args A list of the operation's argument
	 */
	public Operation(String name, String type, ArrayList<Argument> args) {
		this.name = name;
		this.type = type;
		arguments = args;
	}
	
	/**
	 * Argument getter
	 * @return A list of arguments
	 */
	public ArrayList<Argument> get_arguments() {
		return this.arguments;
	}
	
	/**
	 * Name getter
	 * @return The operation's name
	 */
	public String get_name() {
		return this.name;
	}

	/**
	 * Type getter
	 * @return The operation's type
	 */
	public String get_type() {
		return this.type;
	}

	/**
	 * Stringify
	 * @return Human readable String
	 */
	public String toString() {
		StringBuilder args = new StringBuilder();
		String type;
		
		// Ignore type if it is void
		type = (this.type.equalsIgnoreCase("void")) ? "" : this.type+" ";
		
		// Constructs the argument string
		for(int i = 0;i < this.arguments.size(); i++) {
			args.append(arguments.get(i).get_type());
			
			// Skip the comma if it is the last item
			if(i != (this.arguments.size() - 1)) args.append(", ");
		}
		
		return type+this.name+"("+args.toString()+")"; 
	}
}
