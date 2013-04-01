package org.umontreal.ift3913.piroser;

import java.util.ArrayList;
import java.util.Iterator;

public class Metric {
	private Classe classe;
	private Model model;
	
	/**
	 * Constructor
	 * @param The model
	 * @param The class' name
	 */
	public Metric(Model m, String c) {
		
		// Loop through the models to get the class
		for(Classe cl : m.get_classes()) {
			if(cl.get_name().equals(c)) {
				this.classe = cl;
			}
		}
		
		this.model = m;
	}
	
	/**
	 * Get the average number of arguments
	 * @return The average
	 */
	public float get_ana() {
		int methods_count, arguments_count = 0;
		
		methods_count = classe.get_operations().size();
		
		for(Operation op : classe.get_operations()) {
			arguments_count += op.get_arguments().size();
		}
		
		return (float) arguments_count / methods_count;
	}

	/**
	 * Get the number of methods
	 * @return Method count
	 */
	public int get_nom() {
		return get_nom(classe, classe.get_operations());
	}
	
	/**
	 * Get the number of method of the specified class
	 * @param classe The classe to count
	 * @param ops The list of methods
	 * @return Method count
	 */
	private int get_nom(Classe classe, ArrayList<Operation> ops) {
		Classe parent;
		
		// Loop through the generalization to find if the class 
		// inherits from another
		for(Generalization gen : model.get_generalizations()) {
			
			// If the classe has a parent
			if(gen.get_subclasses().contains(classe.get_name())) {
				parent = find_classe(gen.get_name());
				
				// Recursively compute number of class of the parent
				// Merge methods list of the parent and child leaving out
				// duplicates
				return get_nom(parent, merge_operations(ops, parent.get_operations()));
			}
		}
		
		return ops.size();
		
	}
	
	/**
	 * This function takes two array of operations and compares them together removing 
	 * all the methods with the same definitions and return a new array.
	 * I know this function might not be optimal since it's complexity is at least O(n^2)
	 * where n is the size of the longest array but there's no time!
	 * @param ops1 First list of operations
	 * @param ops2 Second list of operations
	 * @return New list of different operations
	 */
	private ArrayList<Operation> merge_operations(ArrayList<Operation> ops1, ArrayList<Operation> ops2) {
		ArrayList<Operation> new_ops = new ArrayList<Operation>();
		Operation op;
		Iterator<Operation> itr; 
		for(Operation op1 : ops1) {
			itr = ops2.iterator();
			// Compare the operation with all the operations in 
			// the second list.
			while(itr.hasNext()) {
				op = itr.next();
				
				// If the name matches and the return type matches remove the operation from the second list
				if(op1.get_name().equals(op.get_name()) 
						&& op1.get_type().equals(op.get_type()) 
						&& compare_arguments(op1.get_arguments(), op.get_arguments())) {
					itr.remove();
				}
			}
			new_ops.add(op1);
		}
		
		new_ops.addAll(ops2);
		
		return new_ops;
	}
	
	/**
	 * Compare two list of arguments
	 * @param args1
	 * @param args2
	 * @return True|False
	 */
	private boolean compare_arguments(ArrayList<Argument> args1, ArrayList<Argument> args2) {
		Argument arg1, arg2;
		
		if(args1.size() != args2.size()) return false;
		
		for(int i = 0; i < args1.size(); i++) {
			arg1 = args1.get(i);
			arg2 = args2.get(i);
			
			// If the name of the argument or the return type does not match return false
			if (! arg1.get_name().equals(arg2.get_name()) || ! arg1.get_type().equals(arg2.get_type())) return false;
		}
		
		return true;
	}
	
	/**
	 * Find the Class in the list of classes
	 * @param class_name The class' name
	 * @return The found class
	 */
	private Classe find_classe(String class_name) {
		for(Classe c : model.get_classes()) {
			if(c.get_name().equals(class_name)) {
				return c;
			}
		}
		
		return null;
	}
}
