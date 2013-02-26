package org.umontreal.ift3913.piroser;

import java.util.ArrayList;

public class Model {
	private String name;
	private ArrayList<Classe> classes;
	private ArrayList<Generalization> generalizations;
	private ArrayList<Association> associations;
	private ArrayList<Aggregation> aggregations;
	
	public Model(String n) {
		name = n;
		classes = new ArrayList<Classe>();
		generalizations = new ArrayList<Generalization>();
		associations = new ArrayList<Association>();
		aggregations = new ArrayList<Aggregation>();
	}
	
	public String get_name() {
		return this.name;
	}

	public ArrayList<Classe> get_classes() {
		return this.classes;
	}
	
	public void add_classe(Classe c) {
		this.classes.add(c);
	}

	public ArrayList<Association> get_associations() {
		return this.associations;
	}

	public void add_association(Association a) {
		this.associations.add(a);
	}

	public ArrayList<Aggregation> get_aggregations() {
		return aggregations;
	}
	
	public void add_aggregation(Aggregation a) {
		this.aggregations.add(a);
	}
	
	public void add_generalization(Generalization g) {
		this.generalizations.add(g);
	}
	
	public ArrayList<Generalization> get_generalizations() {
		return this.generalizations;
	}
	
}
