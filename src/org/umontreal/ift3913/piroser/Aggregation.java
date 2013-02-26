package org.umontreal.ift3913.piroser;

import java.util.ArrayList;

public class Aggregation {
	private Role container;
	private ArrayList<Role> parts;
	
	public Aggregation(Role c, ArrayList<Role> p) {
		this.container = c;
		this.parts = p;
	}
	
	public Role get_container() {
		return this.container;
	}
	
	public ArrayList<Role> get_parts() {
		return this.parts;
	}
}
