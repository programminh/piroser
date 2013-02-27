package org.umontreal.ift3913.piroser;

/**
 * Attribute Class
 * @author Truong Pham
 *
 */
public class Attribute {
	private String name;
	private String type;
	
	/**
	 * Constructor
	 * @param name The attribute's name
	 * @param type The attribute's type
	 */
	public Attribute(String name, String type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * Name getter
	 * @return The attribute's name
	 */
	public String get_name() {
		return this.name;	
	}
	
	/**
	 * Type getter
	 * @return The attribute's type
	 */
	public String get_type() {
		return this.type;
	}
	
	/**
	 * Stringify
	 * @return The human readable string
	 */
	public String toString() {
		return this.type+" "+this.name;
	}
}
