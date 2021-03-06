package org.umontreal.ift3913.piroser;

import java.util.ArrayList;
import java.util.Iterator;
/**
 * This computes the metrics of a class.
 * @author Truong Pham
 *
 */
public class Metric {
	private Classe classe;
	private Model model;
	public static final String[] DEFINITIONS = {
		"Nombre moyen d'arguments des méthodes locales de la classe sélectionée.",
		"Nombre de méthodes locales/héritées de la classe sélectionée. Dans le cas où une méthode héritée est redéfinie localement, elle ne compte qu'une fois.",
		"Nombre d'attributs locaux/héritées de la classe sélectionée.",
		"Nombre de fois où d'autres classes du diagramme apparaissent comme types des arguments des méthodes de la classe sélectionée.",
		"Nombre de fois où la classe sélectionnée apparaît comme type des arguments dans les méthodes des autres classes du diagramme.",
		"Nombre d'associations (incluant les agrégations) locales/héritées auxquelles participe la classe sélectionée.",
		"Taille du chemin le plus long reliant la classe sélectionée à une classe racine dans le graphe d'héritage.",
		"Taille du chemin le plus long reliant la classe sélectionée à une classe feuille dans le graphe d'héritage.",
		"Nombre de sous-classes directes.",
		"Nombre de sous-classes directes et indirectes."
	};
	
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
		
		if(methods_count == 0) return 0;
		
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
	
	/**
	 * Get the number of arguments
	 * @return Number of arguments
	 */
	public int get_noa() {
		return get_noa(classe);
	}
	
	/**
	 * Recursive search for the number of arguments
	 * @param c The class to search
	 * @return Number of argument
	 */
	private int get_noa(Classe c) {
		// Loop through the list of generalization to find if the class has a parent
		for(Generalization gen : model.get_generalizations()) {
			if(gen.get_subclasses().contains(c.get_name())) {
				return c.get_attributes().size() + get_noa(find_classe(gen.get_name()));
			}
		}
		
		return c.get_attributes().size();
	}
	
	/**
	 * Get the internal coupling
	 * @return Number of internal coupling
	 */
	public int get_itc() {
		int itc_count = 0;
		
		for(Operation op : classe.get_operations()) {
			
			for(Argument arg : op.get_arguments()) {
				if(find_classe(arg.get_type()) != null) {
					itc_count++;
				}
			}
		}
		
		return itc_count;
	}
	
	/**
	 * Get the external coupling
	 * @return Number of external coupling
	 */
	public int get_etc() {
		int etc_count = 0;
		
		for(Classe c : model.get_classes()) {
			// Skip if it's the same class
			if(c.get_name().equals(classe.get_name())) continue;
			
			for(Operation op : c.get_operations()) {
				
				for(Argument arg : op.get_arguments()) {
					if(arg.get_type().equals(classe.get_name())) {
						etc_count++;
					}
				}
			}			
		}
		
		return etc_count;
	}
	
	/**
	 * Get the CAC
	 * @return CAC count
	 */
	public int get_cac() {
		return get_cac(classe);
	}
	
	/**
	 * Recursively get the CAC
	 * @param c Class to count the CAC
	 * @return CAC count
	 */
	private int get_cac(Classe c) {
		int cac_count = 0;
		
		// Search Aggregation
		for(Aggregation agg : model.get_aggregations()) {
			
			// Increment the count if it matches the container
			if(agg.get_container().get_name().equals(c.get_name())) {
				cac_count++;
			}
			else {
				
				// Else search in the parts
				for(Role r : agg.get_parts()) {
					
					// If found in the parts increment the count and break the loop
					// to save some time
					if(r.get_name().equals(c.get_name())) {
						cac_count++;
						break;
					}
				}
			}
		}
		
		
		// Search in the associations
		for(Association ass : model.get_associations()) {
			// If the first or the second role matches increment the count
			if(ass.get_first_role().get_name().equals(c.get_name())
					|| ass.get_second_role().get_name().equals(c.get_name())) {
				cac_count++;
			}
		}
		
		// Search for a parent
		for(Generalization gen : model.get_generalizations()) {
			if(gen.get_subclasses().contains(c.get_name())) {
				return cac_count + get_cac(find_classe(gen.get_name()));
			}
		}
		
		return cac_count;
	}
	
	/**
	 * Count the Depth in Tree
	 * @return DIT Count
	 */
	public int get_dit() {
		return get_dit(classe);
	}
	
	/**
	 * Recursively get the Depth in Tree
	 * @param c
	 * @return DIT Count
	 */
	private int get_dit(Classe c) {
		int max = 0;
		ArrayList<Classe> parents = find_parents(c);
		
		if(parents.size() == 0) return 0;
		
		// Loop through all the parents to get the maximum DiT
		for(Classe cl : parents) {
			if (max < get_dit(cl)) {
				max = get_dit(cl);
			}
		}
		
		return 1 + max;
		
	}
	
	/**
	 * Get a list of the parents
	 * @param c Class to the find the parents
	 * @return A list with all the class' parent
	 */
	private ArrayList<Classe> find_parents(Classe c) {
		ArrayList<Classe> classes = new ArrayList<Classe>();
		
		// Loop through the generalization to find the parent
		for(Generalization gen : model.get_generalizations()) {
			if(gen.get_subclasses().contains(c.get_name())) {
				// If a parent is found add it to the list of classes
				classes.add(find_classe(gen.get_name()));
			}
		}
		
		return classes;
	}
	
	/**
	 * Get the number of child
	 * @return Child count
	 */
	public int get_noc() {
		return get_noc(classe);
	}
	
	/**
	 * Get the number of child of the specified class
	 * @param c The class
	 * @return Child count
	 */
	private int get_noc(Classe c) {
		int count = 0;
		
		for(Generalization gen : model.get_generalizations()) {
			if(gen.get_name().equals(c.get_name())) {
				count += gen.get_subclasses().size();
			}
		}
		
		return count;
	}
	
	/**
	 * Get the number of direct and indirect child
	 * @return Child count
	 */
	public int get_nod() {
		return get_nod(classe);
	}
	
	/**
	 * Recursively get the number of direct and indirect child of the specified class
	 * @param c The class to count
	 * @return Child count
	 */
	private int get_nod(Classe c) {
		ArrayList<Classe> childs = find_childs(c);
		int count = 0;
		
		if(childs.size() == 0) return 0;
		
		count += childs.size();
		
		for(Classe cl : childs) {
			count += get_nod(cl);
		}
		
		return count;
		
		
		
	}
	
	/**
	 * Get a list of the childs
	 * @param c Class to the find childs
	 * @return A list with all the class' child
	 */
	private ArrayList<Classe> find_childs(Classe c) {
		ArrayList<Classe> classes = new ArrayList<Classe>();
		
		// Loop through the generalization to find the parent
		for(Generalization gen : model.get_generalizations()) {
			if(gen.get_name().equals(c.get_name())) {
				// If we have found the parent loop through the sub-classes and
				// add them to the list of child
				for(String name : gen.get_subclasses()) {
					classes.add(find_classe(name));
				}
				
			}
		}
		
		return classes;
	}
	
	/**
	 * Class-to-leaf Depth
	 * @return CLD Count
	 */
	public int get_cld() {
		return get_cld(classe);
	}
	
	/**
	 * Class-to-leaf Depth of the specified class
	 * @param c Class to count
	 * @return CLD Count
	 */
	private int get_cld(Classe c) {
		ArrayList<Classe> childs = find_childs(c);
		int max = 0;
		
		if(childs.size() == 0) return 0;
		
		for(Classe cl : childs) {
			if(max < get_cld(cl)) {
				max = get_cld(cl);
			}
		}
		
		return 1 + max;
	}
}
/***********************************************************************************
 * A flower to thank you for not making me move up to school to give you my report.*
 *                          Oh! And for the :) as well.                            *
 ***********************************************************************************
 
                   "M,        .mM"
                     IMIm    ,mIM"
                     ,MI:"IM,mIMm
          "IMmm,    ,IM::::IM::IM,          ,m"
             "IMMIMMIMm::IM:::::IM""==mm ,mIM"
    __      ,mIM::::::MIM:::::::IM::::mIMIM"
 ,mMIMIMIIMIMM::::::::mM::::::::IMIMIMIMMM"
IMM:::::::::IMM::::::M::::::::IIM:::::::MM,
 "IMM::::::::::MM:::M:::::::IM:::::::::::IM,
    "IMm::::::::IMMM:::::::IM:::::::::::::IM,
      "Mm:::::::::IM::::::MM::::::::::::::::IM,
       IM:::::::::IM::::::MM::::::::::::::::::IM,
        MM::::::::IM:::::::IM::::::::::::::::::IM
        "IM::::::::IM:::::::IM:::::::::::::::::IM;.
         "IM::::::::MM::::::::IM::::::::::mmmIMMMMMMMm,.
           IM::::::::IM:::::::IM::::mIMIMM"""". .. "IMMMM
           "IM::::::::IM::::::mIMIMM"". . . . . .,mM"   "M
            IMm:::::::IM::::IIMM" . . . . . ..,mMM"
            "IMMIMIMMIMM::IMM" . . . ._.,mMMMMM"
             ,IM". . ."IMIM". . . .,mMMMMMMMM"
           ,IM . . . .,IMM". . . ,mMMMMMMMMM"
          IM. . . .,mIIMM,. . ..mMMMMMMMMMM"
         ,M"..,mIMMIMMIMMIMmmmMMMMMMMMMMMM"
         IM.,IMI"""        ""IIMMMMMMMMMMM
        ;IMIM"                  ""IMMMMMMM
        ""                         "IMMMMM
                                     "IMMM
                                      "IMM,
                                       "IMM
                                        "MM,
                                         IMM,              ______   __
                        ______           "IMM__        .mIMMIMMIMMIMMIMM,
                   .,mIMMIMMIMM, ,mIMM,   IMM"""     ,mIM". . . . "IM,..M,
                 ,IMMM' . . . "IMM.\ "M,  IMM      ,IM". . . .  / :;IM \ M,
               .mIM' . . .  / .:"IM.\ MM  "MM,    ,M". . .  / .;mIMIMIM,\ M
              ,IM'. . .  / . .:;,IMIMIMMM  IMM   ,M". .  / .:mIM"'   "IM,:M
             ,IM'. . . / . .:;,mIM"  `"IMM IMM   IM. .  / .mM"         "IMI
            ,IM . .  / . .:;,mIM"      "IMMMMM   MM,.  / ,mM            "M'
            IM'. .  / . .;,mIM"          "IIMMM ,IMIM,.,IM"
            IM . . / . .,mIM"              IMMMMMMM' """
            `IM,.  / ;,mIM"                 IIMMM
             "IMI, /,mIM"                 __IMMM
               "IMMMM"                   """IMM
                 ""                         IMM
                                            IMM__
                                            IMM"""
                                            IMM
                                            IMM
                                          __IMM
                                         """IMM
                                            IMM
                                            IMM
                                            IMM__
                                            IMM"""
                                            IMM
                                   Normand  IMM  Veilleux
                                   
                     Ouain... sorry je ne suis pas l'auteur original de ce ASCII.
**/
