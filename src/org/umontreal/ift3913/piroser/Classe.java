package org.umontreal.ift3913.piroser;

import java.util.ArrayList;
/**
 * Classe Class
 * The reason why this class has such a confusing name is because `Class` 
 * is a reserved keyword in Java. I wanted to name it Boobie since we can
 * name them, they have attributes and we can perform operations with them
 * too. Imagine how much fun we could get by invoking `new Boobie()`. Unfortunately
 * it is not really suited for an University project thought. I guess I'd 
 * reconsider the name for a personal project. 
 * (Hopefully nobody reads this)
 * @author Truong Pham
 *
 */
public class Classe {
	private String name;
	private ArrayList<Attribute> attributes;
	private ArrayList<Operation> operations;
	
	/**
	 * Constructor
	 * @param name The classe's name
	 */
	public Classe(String name) {
		this.name = name;
		attributes = new ArrayList<Attribute>();
		operations = new ArrayList<Operation>();
	}

	/**
	 * Name getter
	 * @return The name
	 */
	public String get_name() {
		return this.name;
	}

	/**
	 * Add an attribute to the class
	 * @param attribute The attribute to be added
	 */
	public void add_attribute(Attribute attribute) {
		this.attributes.add(attribute);
	}
	
	/**
	 * Attributes getter
	 * @return The attributes
	 */
	public ArrayList<Attribute> get_attributes() {
		return this.attributes;
	}
	
	/**
	 * Add an operation to the class
	 * @param o The operation to be added
	 */
	public void add_operation(Operation o) {
		this.operations.add(o);
	}
	
	/**
	 * Operations getter
	 * @return The operations
	 */
	public ArrayList<Operation> get_operations() {
		return this.operations;
	}
	
	/**
	 * Stringify for JList
	 * @return The name
	 */
	public String toString() {
		return this.name;
	}
}
