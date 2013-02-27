package org.umontreal.ift3913.piroser;

import java.util.ArrayList;

/**
 * Model Class
 * Root of the diagram
 * @author Truong Pham
 *
 */
public class Model {
	private String name;
	private ArrayList<Classe> classes;
	private ArrayList<Generalization> generalizations;
	private ArrayList<Association> associations;
	private ArrayList<Aggregation> aggregations;
	
	/**
	 * Constructor
	 * @param n The model's name
	 */
	public Model(String n) {
		name = n;
		
		// Initializting lists
		classes = new ArrayList<Classe>();
		generalizations = new ArrayList<Generalization>();
		associations = new ArrayList<Association>();
		aggregations = new ArrayList<Aggregation>();
	}
	
	/**
	 * Name getter
	 * @return The model's name
	 */
	public String get_name() {
		return this.name;
	}

	/**
	 * Classe getter
	 * @return List of classes
	 */
	public ArrayList<Classe> get_classes() {
		return this.classes;
	}
	
	/**
	 * Add a classe to the model
	 * @param c Classe to be added
	 */
	public void add_classe(Classe c) {
		this.classes.add(c);
	}

	/**
	 * Associations getter
	 * @return List of associations
	 */
	public ArrayList<Association> get_associations() {
		return this.associations;
	}

	
	/**
	 * Add an association to the model
	 * @param a The association to be added
	 */
	public void add_association(Association a) {
		this.associations.add(a);
	}

	/**
	 * Aggregations getter
	 * @return List of aggregations
	 */
	public ArrayList<Aggregation> get_aggregations() {
		return aggregations;
	}
	
	/**
	 * Add an aggregation
	 * @param a The aggregation to be added
	 */
	public void add_aggregation(Aggregation a) {
		this.aggregations.add(a);
	}
	
	/**
	 * Add a generalization
	 * @param g The generalization to be added
	 */
	public void add_generalization(Generalization g) {
		this.generalizations.add(g);
	}
	
	/**
	 * Generalizations getter
	 * @return A list of generalization
	 */
	public ArrayList<Generalization> get_generalizations() {
		return this.generalizations;
	}
	
}
