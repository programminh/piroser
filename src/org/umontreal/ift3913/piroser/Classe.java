package org.umontreal.ift3913.piroser;

import java.util.ArrayList;

public class Classe {
	private String name;
	private ArrayList<Attribute> attributes;
	private ArrayList<Operation> operations;
	
	public Classe(String name) {
		this.name = name;
		attributes = new ArrayList<Attribute>();
		operations = new ArrayList<Operation>();
	}

	public String get_name() {
		return this.name;
	}

	public void add_attribute(Attribute attribute) {
		this.attributes.add(attribute);
	}
	
	public ArrayList<Attribute> get_attributes() {
		return this.attributes;
	}
	
	public void add_operation(Operation o) {
		this.operations.add(o);
	}
	
	public ArrayList<Operation> get_operations() {
		return this.operations;
	}
}
