package org.umontreal.ift3913.piroser;

import java.util.ArrayList;

public class Operation {
	private String name;
	private String type;
	private ArrayList<Argument> arguments;
	
	public Operation(String name, String type, ArrayList<Argument> args) {
		this.name = name;
		this.type = type;
		arguments = args;
	}
	
	public ArrayList<Argument> get_arguments() {
		return this.arguments;
	}
	
	public String get_name() {
		return this.name;
	}

	public String get_type() {
		return this.type;
	}
	
	public void set_arguments(ArrayList<Argument> args) {
		this.arguments = args;
	}
	
	public void add_argument(Argument a) {
		this.arguments.add(a);
	}
}
