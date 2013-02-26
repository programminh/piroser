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
		return name;
	}

	public ArrayList<Classe> get_classes() {
		return classes;
	}
	
	public void add_classe(Classe c) {
		classes.add(c);
	}

	public ArrayList<Association> get_associations() {
		return associations;
	}
}
