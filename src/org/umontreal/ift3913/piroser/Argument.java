package org.umontreal.ift3913.piroser;
/**
 * Argument Class
 * @author Truong Pham
 *
 */
public class Argument {
	private String name;
	private String type;
	
	/**
	 * Constructor
	 * @param name The argument name
	 * @param type The argument type
	 */
	public Argument(String name, String type) {
		this.name = name;
		this.type = type;
	}
	
	/**
	 * Name getter
	 * @return The argument's name
	 */
	public String get_name() {
		return this.name;
	}
	
	/**
	 * Type getter
	 * @return The argument's type
	 */
	public String get_type() {
		return this.type;
	}
}
