package org.umontreal.ift3913.piroser;

/**
 * Role Class
 * @author Truong Pham
 *
 */
public class Role {
	private String name;
	private String multiplicity;
	private static final String[] VALID_MULTIPLICITY = {
			"ONE",
			"MANY",
			"ONE_OR_MANY",
			"OPTIONALLY_ONE",
			"UNDEFINED",
	};
	
	/**
	 * Validate the multiplicity and creates the object
	 * @param name
	 * @param mul
	 * @throws InvalidUMLException
	 */
	public Role(String name, String mul) throws InvalidUMLException{
		// Validate multiplicity
		this.name = name;
		this.multiplicity = mul;
	}
	
	/**
	 * Check whether the multiplicity is a valid one
	 * @param mul
	 * @return true if valid
	 */
	public static boolean multiplicity_is_valid(String mul) {
		for(String s : VALID_MULTIPLICITY) {
			if(s.equals(mul)) return true;
		}
		
		return false;
	}
	
	/**
	 * Name getter
	 * @return The name
	 */
	public String get_name() {
		return this.name;
	}
	
	/**
	 * Multiplicity getter
	 * @return The multiplicity
	 */
	public String get_multiplicity() {
		return this.multiplicity;
	}
	
}
