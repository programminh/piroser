package org.umontreal.ift3913.piroser;
/**
 * Association Class
 * @author Truong Pham
 *
 */
public class Association {
	private String name;
	private Role first_role;
	private Role second_role;
	
	/**
	 * Constructor
	 * @param name Association name
	 * @param first First Role
	 * @param second Second Role
	 */
	public Association(String name, Role first, Role second) {
		this.name = name;
		this.first_role = first;
		this.second_role = second;
	}
	
	/**
	 * First Role getter
	 * @return First Role
	 */
	public Role get_first_role() {
		return this.first_role;
	}
	
	/**
	 * Second Role getter
	 * @return Second Role
	 */
	public Role get_second_role() {
		return this.second_role;
	}
	
	/**
	 * Name getter
	 * @return Association's name
	 */
	public String get_name() {
		return this.name;
	}
}
