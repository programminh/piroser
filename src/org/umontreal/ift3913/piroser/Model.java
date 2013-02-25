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
	}
	
	public String get_name() {
		return name;
	}
}
