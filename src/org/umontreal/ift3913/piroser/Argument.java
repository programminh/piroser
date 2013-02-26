package org.umontreal.ift3913.piroser;

public class Argument {
	private String name;
	private String type;
	
	public Argument(String name, String type) {
		this.name = name;
		this.type = type;
	}
	
	public String get_name() {
		return this.name;
	}
	
	public String get_type() {
		return this.type;
	}
}
