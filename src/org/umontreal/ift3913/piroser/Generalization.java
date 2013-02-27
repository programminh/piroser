package org.umontreal.ift3913.piroser;

import java.util.ArrayList;

/**
 * Generalization Class
 * @author Truong Pham
 *
 */
public class Generalization {
	private String name;
	private ArrayList<String> subclasses;
	
	/**
	 * Constructor
	 * @param n The generalization's name
	 * @param s A list of subclasses
	 */
	public Generalization(String n, ArrayList<String> s) {
		this.name = n;
		this.subclasses = s;
	}
	
	/** 
	 * Name getter
	 * @return The generalization's name
	 */
	public String get_name() {
		return this.name;
	}
	
	/**
	 * Subclasses getter
	 * @return The generalization's subclasses
	 */
	public ArrayList<String> get_subclasses() {
		return this.subclasses;
	}

}
