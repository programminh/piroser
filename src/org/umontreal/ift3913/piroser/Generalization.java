package org.umontreal.ift3913.piroser;

import java.util.ArrayList;

public class Generalization {
	private String name;
	private ArrayList<String> subclasses;
	
	public Generalization(String n, ArrayList<String> s) {
		this.name = n;
		this.subclasses = s;
	}
	
	public String get_name() {
		return this.name;
	}
	
	public ArrayList<String> get_subclasses() {
		return this.subclasses;
	}

}
