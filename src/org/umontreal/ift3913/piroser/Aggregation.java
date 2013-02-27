package org.umontreal.ift3913.piroser;

import java.util.ArrayList;
/**
 * Aggregation Class
 * @author Truong Pham
 *
 */
public class Aggregation {
	private Role container;
	private ArrayList<Role> parts;
	
	/**
	 * Constructor
	 * @param c The container role
	 * @param p List of part's role
	 */
	public Aggregation(Role c, ArrayList<Role> p) {
		this.container = c;
		this.parts = p;
	}
	
	/**
	 * Container role getter
	 * @return The container role
	 */
	public Role get_container() {
		return this.container;
	}
	
	/**
	 * Parts role getter
	 * @return The parts role
	 */
	public ArrayList<Role> get_parts() {
		return this.parts;
	}
}
