package org.umontreal.ift3913.piroser.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.umontreal.ift3913.piroser.*;

public class MetricTest {

	@Test
	public void test_ana() {
		float expected_ana;
		Model model = new Model("TestModel");
		Classe classe = new Classe("TestClass");
		Metric metric;
		Operation op; 
		ArrayList<Argument> args = new ArrayList<Argument>();
		
		// First method has 3 arguments
		args.add(new Argument("first_arg", "test"));
		args.add(new Argument("second_arg", "test"));
		args.add(new Argument("third_arg", "test"));
		
		op = new Operation("first_method", "", args);
		classe.add_operation(op);
		
		// Second method has no arugments
		args = new ArrayList<Argument>();
		op = new Operation("second_method", "", args);
		classe.add_operation(op);
		
		model.add_classe(classe);
		
		metric = new Metric(model, "TestClass");
		
		expected_ana = (float) 3 / 2;

		assertEquals(expected_ana, metric.get_ana(), 0.01);
	}

	
	@Test
	public void test_nom_without_inheritance() {
		Model model = new Model("TestModel");
		Classe classe = new Classe("TestClasse");
		Metric metric;
		
		classe.add_operation(new Operation("op1", "test", new ArrayList<Argument>()));
		classe.add_operation(new Operation("op2", "test", new ArrayList<Argument>()));
		classe.add_operation(new Operation("op3", "test", new ArrayList<Argument>()));
		
		model.add_classe(classe);
		metric = new Metric(model, "TestClasse");
		
		assertEquals(3, metric.get_nom());
	}
	
	@Test
	public void test_nom_with_single_inheritance() {
		Model model = new Model("TestModel");
		Classe parent_classe = new Classe("ParentClass");
		Classe child_classe = new Classe("ChildClass");
		ArrayList<String> sub = new ArrayList<String>();
		Generalization gen;
		Metric metric;
		
		// Three method to the parent class 
		parent_classe.add_operation(new Operation("p_op1", "test", new ArrayList<Argument>()));
		parent_classe.add_operation(new Operation("p_op2", "test", new ArrayList<Argument>()));
		parent_classe.add_operation(new Operation("p_op3", "test", new ArrayList<Argument>()));
		model.add_classe(parent_classe);
		
		// One method to child class
		child_classe.add_operation(new Operation("c_op1", "test", new ArrayList<Argument>()));
		model.add_classe(child_classe);
		
		
		sub.add("ChildClass");
		gen = new Generalization("ParentClass", sub);
		model.add_generalization(gen);
		
		metric = new Metric(model, "ChildClass");
		
		assertEquals(4, metric.get_nom());
	}
	
	@Test
	public void test_nom_with_single_inheritance_and_same_redifinition() {
		Model model = new Model("TestModel");
		Classe parent_classe = new Classe("ParentClass");
		Classe child_classe = new Classe("ChildClass");
		ArrayList<String> sub = new ArrayList<String>();
		Generalization gen;
		Metric metric;
		
		// Three method to the parent class 
		parent_classe.add_operation(new Operation("p_op1", "test", new ArrayList<Argument>()));
		parent_classe.add_operation(new Operation("p_op2", "test", new ArrayList<Argument>()));
		parent_classe.add_operation(new Operation("p_op3", "test", new ArrayList<Argument>()));
		model.add_classe(parent_classe);
		
		// One method to child class and one same redefinition
		child_classe.add_operation(new Operation("c_op1", "test", new ArrayList<Argument>()));
		child_classe.add_operation(new Operation("p_op1", "test", new ArrayList<Argument>()));
		model.add_classe(child_classe);
		
		sub.add("ChildClass");
		gen = new Generalization("ParentClass", sub);
		model.add_generalization(gen);
		
		metric = new Metric(model, "ChildClass");
		
		assertEquals(4, metric.get_nom());
	}
	
	@Test
	public void test_nom_with_single_inheritance_and_different_redifinition() {
		Model model = new Model("TestModel");
		Classe parent_classe = new Classe("ParentClass");
		Classe child_classe = new Classe("ChildClass");
		ArrayList<Argument> args = new ArrayList<Argument>();
		ArrayList<String> sub = new ArrayList<String>();
		Generalization gen;
		Metric metric;
		
		// Three method to the parent class 
		parent_classe.add_operation(new Operation("p_op1", "test", new ArrayList<Argument>()));
		parent_classe.add_operation(new Operation("p_op2", "test", new ArrayList<Argument>()));
		parent_classe.add_operation(new Operation("p_op3", "test", new ArrayList<Argument>()));
		model.add_classe(parent_classe);
		
		// Redifinition different return type
		child_classe.add_operation(new Operation("p_op1", "test_diff", new ArrayList<Argument>()));
		// Redifinition different arguments
		args.add(new Argument("arg1", "test"));
		child_classe.add_operation(new Operation("p_op2", "test", args));
		// New method
		child_classe.add_operation(new Operation("c_op", "test", new ArrayList<Argument>()));
		model.add_classe(child_classe);
		
		sub.add("ChildClass");
		gen = new Generalization("ParentClass", sub);
		model.add_generalization(gen);
		
		metric = new Metric(model, "ChildClass");
		
		assertEquals(6, metric.get_nom());
	}
	
	@Test
	public void test_nom_with_double_inheritance() {
		Model model = new Model("TestModel");
		Classe parent_parent_classe = new Classe("ParentParentClass");
		Classe parent_classe = new Classe("ParentClass");
		Classe child_classe = new Classe("ChildClass");
		ArrayList<String> sub = new ArrayList<String>();
		Generalization gen;
		Metric metric;
		
		// One method to the parent parent class 
		parent_parent_classe.add_operation(new Operation("pp_op1", "test", new ArrayList<Argument>()));
		model.add_classe(parent_parent_classe);
	
		// Three method to the parent class 
		parent_classe.add_operation(new Operation("p_op1", "test", new ArrayList<Argument>()));
		parent_classe.add_operation(new Operation("p_op2", "test", new ArrayList<Argument>()));
		parent_classe.add_operation(new Operation("p_op3", "test", new ArrayList<Argument>()));
		model.add_classe(parent_classe);
		
		// One method to child class
		child_classe.add_operation(new Operation("c_op1", "test", new ArrayList<Argument>()));
		model.add_classe(child_classe);
		
		// Adding first inheritance
		sub.add("ChildClass");
		gen = new Generalization("ParentClass", sub);
		model.add_generalization(gen);
		
		// Adding second inheritnace
		sub = new ArrayList<String>();
		sub.add("ParentClass");
		gen = new Generalization("ParentParentClass", sub);
		model.add_generalization(gen);
		
		metric = new Metric(model, "ChildClass");
		
		assertEquals(5, metric.get_nom());
	}
	
	@Test
	public void test_nom_with_double_inheritance_with_redefinition() {
		Model model = new Model("TestModel");
		Classe parent_parent_classe = new Classe("ParentParentClass");
		Classe parent_classe = new Classe("ParentClass");
		Classe child_classe = new Classe("ChildClass");
		ArrayList<String> sub = new ArrayList<String>();
		Generalization gen;
		Metric metric;
		
		// One method to the parent parent class 
		parent_parent_classe.add_operation(new Operation("pp_op1", "test", new ArrayList<Argument>()));
		parent_parent_classe.add_operation(new Operation("pp_op2", "test", new ArrayList<Argument>()));
		model.add_classe(parent_parent_classe);
	
		// Three method to the parent class 
		parent_classe.add_operation(new Operation("p_op1", "test", new ArrayList<Argument>()));
		// Redifinition of first method of parent
		parent_classe.add_operation(new Operation("pp_op1", "test_diff", new ArrayList<Argument>()));
		// Same definition of second parent method
		parent_classe.add_operation(new Operation("pp_op2", "test", new ArrayList<Argument>()));
		model.add_classe(parent_classe);
		
		// One method to child class
		child_classe.add_operation(new Operation("c_op1", "test", new ArrayList<Argument>()));
		model.add_classe(child_classe);
		
		// Adding first inheritance
		sub.add("ChildClass");
		gen = new Generalization("ParentClass", sub);
		model.add_generalization(gen);
		
		// Adding second inheritnace
		sub = new ArrayList<String>();
		sub.add("ParentClass");
		gen = new Generalization("ParentParentClass", sub);
		model.add_generalization(gen);
		
		metric = new Metric(model, "ChildClass");
		
		assertEquals(5, metric.get_nom());
	}
	
	@Test
	public void test_noa_without_inheritance() {
		Model model = new Model("TestModel");
		Classe c = new Classe("Classe");
		Metric metric;

		c.add_attribute(new Attribute("attr1", "test"));
		c.add_attribute(new Attribute("attr2", "test"));
		model.add_classe(c);
		
		metric = new Metric(model, "Classe");
		
		assertEquals(2, metric.get_noa());	
	}
	
	@Test
	public void test_noa_with_inheritance() {
		Model model = new Model("TestModel");
		Classe c = new Classe("Classe");
		Classe p_c = new Classe("ParentClasse");
		ArrayList<String> subs = new ArrayList<String>();
		Metric metric;

		p_c.add_attribute(new Attribute("pattr1", "test"));
		p_c.add_attribute(new Attribute("pattr2", "test"));
		model.add_classe(p_c);
		
		c.add_attribute(new Attribute("attr1", "test"));
		c.add_attribute(new Attribute("attr2", "test"));
		model.add_classe(c);
		
		subs.add("Classe");
		model.add_generalization(new Generalization("ParentClasse", subs));
		
		
		metric = new Metric(model, "Classe");
		
		assertEquals(4, metric.get_noa());	
	}
	
	@Test
	public void test_noa_double_inheritance() {
		Model model = new Model("TestModel");
		Classe c = new Classe("Classe");
		Classe p_c = new Classe("ParentClasse");
		Classe p_p_c = new Classe("ParentParentClasse");
		ArrayList<String> subs = new ArrayList<String>();
		Metric metric;
		
		p_p_c.add_attribute(new Attribute("ppattr1", "test"));
		p_p_c.add_attribute(new Attribute("ppattr2", "test"));
		model.add_classe(p_p_c);

		p_c.add_attribute(new Attribute("pattr1", "test"));
		p_c.add_attribute(new Attribute("pattr2", "test"));
		model.add_classe(p_c);
		
		c.add_attribute(new Attribute("attr1", "test"));
		c.add_attribute(new Attribute("attr2", "test"));
		model.add_classe(c);
		
		subs.add("Classe");
		model.add_generalization(new Generalization("ParentClasse", subs));
		
		
		subs = new ArrayList<String>();
		subs.add("ParentClasse");
		model.add_generalization(new Generalization("ParentParentClasse", subs));
		
		metric = new Metric(model, "Classe");
		
		assertEquals(6, metric.get_noa());	
	}
	
	@Test
	public void test_itc() {
		Metric metric;
		Model model = new Model("TestModel");
		Classe cl_1 = new Classe("Class1");
		Classe cl_2 = new Classe("Class2");
		Classe cl_3 = new Classe("Class3");
		ArrayList<Argument> args = new ArrayList<Argument>();
		
		// First method with two internal class
		args.add(new Argument("arg1", "Class2"));
		args.add(new Argument("arg2", "Test"));
		args.add(new Argument("arg3", "Class3"));
		cl_1.add_operation(new Operation("ops1", "void", args));
		
		// Second method no internal class
		args = new ArrayList<Argument>();
		args.add(new Argument("arg1", "Test"));
		cl_1.add_operation(new Operation("ops2", "void", args));
		
		// Third method one internal argument
		args = new ArrayList<Argument>();
		args.add(new Argument("arg1", "Class2"));
		cl_1.add_operation(new Operation("ops3", "void", args));
		
		model.add_classe(cl_1);
		model.add_classe(cl_2);
		model.add_classe(cl_3);
		
		metric = new Metric(model, "Class1");
		
		assertEquals(3, metric.get_itc());
	}
	
	@Test
	public void test_etc() {
		Metric metric;
		Model model = new Model("TestModel");
		Classe cl_1 = new Classe("Class1");
		Classe cl_2 = new Classe("Class2");
		Classe cl_3 = new Classe("Class3");
		ArrayList<Argument> args = new ArrayList<Argument>();
		
		// Other class #1
		// First method has 2 times Class1
		args.add(new Argument("arg1", "Class1"));
		args.add(new Argument("arg2", "Class1"));
		cl_2.add_operation(new Operation("op1", "void", args));
		args.add(new Argument("arg3", "Test"));
		// Second method has 2 times Class1 and 1 random argument
		cl_2.add_operation(new Operation("op2", "void", args));
		// Third method has no Class1 as argument
		cl_2.add_operation(new Operation("op3", "void", new ArrayList<Argument>()));
		
		// Other class #2
		// First method has 2 times Class1 and 1 random argument
		cl_3.add_operation(new Operation("op1", "void", args));
		// Second method has Class1 as arugment
		cl_3.add_operation(new Operation("op2", "void", new ArrayList<Argument>()));
		
		model.add_classe(cl_1);
		model.add_classe(cl_2);
		model.add_classe(cl_3);
		
		metric = new Metric(model, "Class1");
		
		assertEquals(6, metric.get_etc());
	}
	
	@Test 
	public void test_cac_no_inheritance() {
		Metric metric;
		Model model = new Model("TestModel");
		Classe ville = new Classe("Ville");
		Classe equipe = new Classe("Equipe");
		Classe joueur = new Classe("Joueur");
		Classe proprio = new Classe("Proprio");
		Aggregation agg;
		Association ass;
		ArrayList<Role> roles = new ArrayList<Role>();
		
		// Equipe is a container
		roles.add(new Role("Joueur", "ONE_OR_MANY"));
		agg = new Aggregation(new Role("Equipe", "ONE"), roles);
		model.add_aggregation(agg);
		
		// Equipe is a part
		roles = new ArrayList<Role>();
		roles.add(new Role("Equipe", "ONE_OR_MANY"));
		agg = new Aggregation(new Role("Ville", "ONE"), roles);
		model.add_aggregation(agg);
		
		// Equipe is first role
		ass = new Association("est_situe_a", new Role("Equipe", "MANY"), new Role("Ville", "ONE"));
		model.add_association(ass);
		
		// Equipe is second role
		ass = new Association("est_proprietaire_de", new Role("Proprio", "ONE"), new Role("Equipe", "MANY"));
		model.add_association(ass);
		
		model.add_classe(proprio);
		model.add_classe(joueur);
		model.add_classe(equipe);
		model.add_classe(ville);
		
		
		metric = new Metric(model, "Equipe");
		
		assertEquals(4, metric.get_cac());
	}
	
	@Test 
	public void test_cac_no_inheritance_with_inheritance() {
		Metric metric;
		Model model = new Model("TestModel");
		Classe continent = new Classe("Continent");
		Classe pays = new Classe("Pays");
		Classe ville = new Classe("Ville");
		Classe equipe = new Classe("Equipe");
		Classe joueur = new Classe("Joueur");
		Classe proprio = new Classe("Proprio");
		
		Aggregation agg;
		Association ass;
		ArrayList<String> subs = new ArrayList<String>();
		ArrayList<Role> roles = new ArrayList<Role>();
		
		// Equipe is a container
		roles.add(new Role("Joueur", "ONE_OR_MANY"));
		agg = new Aggregation(new Role("Equipe", "ONE"), roles);
		model.add_aggregation(agg);
		
		// Equipe is a part
		roles = new ArrayList<Role>();
		roles.add(new Role("Equipe", "ONE_OR_MANY"));
		agg = new Aggregation(new Role("Ville", "ONE"), roles);
		model.add_aggregation(agg);
		
		// Equipe is first role
		ass = new Association("est_situe_a", new Role("Equipe", "MANY"), new Role("Ville", "ONE"));
		model.add_association(ass);
		
		// Equipe is second role
		ass = new Association("est_proprietaire_de", new Role("Proprio", "ONE"), new Role("Equipe", "MANY"));
		model.add_association(ass);
		
		// Pays container
		roles = new ArrayList<Role>();
		roles.add(new Role("Ville", "MANY"));
		agg = new Aggregation(new Role("Pays", "ONE"), roles);
		model.add_aggregation(agg);
		
		// Pays part
		roles.add(new Role("Pays", "MANY"));
		agg = new Aggregation(new Role("Continent", "ONE"), roles);
		model.add_aggregation(agg);
		
		// Pays first role
		ass = new Association("est_situe_a", new Role("Pays", "MANY"), new Role("Continent", "ONE"));
		model.add_association(ass);
		
		// Pays second role
		ass = new Association("controle", new Role("Proprio", "ONE_OR_MANY"), new Role("Pays", "ONE_OR_MANY"));
		model.add_association(ass);
		
		subs.add("Equipe");
		// Add the inheritance
		model.add_generalization(new Generalization("Pays", subs));
		
		model.add_classe(continent);
		model.add_classe(pays);
		model.add_classe(proprio);
		model.add_classe(joueur);
		model.add_classe(equipe);
		model.add_classe(ville);
		
		metric = new Metric(model, "Equipe");
		
		assertEquals(8, metric.get_cac());
	}
	
	@Test
	public void test_dit_single_inheritance() {
		Model model = new Model("TestModel");
		Classe child = new Classe("Child"),
				parent = new Classe("Parent"), 
				p_parent = new Classe("ParentParent"),
				p_p_parent = new Classe("ParentParentParent");
		ArrayList<String> subs = new ArrayList<String>();
		Metric metric;
		
		subs.add("Child");
		model.add_generalization(new Generalization("Parent", subs));
		
		subs = new ArrayList<String>();
		subs.add("Parent");
		model.add_generalization(new Generalization("ParentParent", subs));
		
		subs = new ArrayList<String>();
		subs.add("ParentParent");
		model.add_generalization(new Generalization("ParentParentParent", subs));
		
		model.add_classe(child);
		model.add_classe(parent);
		model.add_classe(p_parent);
		model.add_classe(p_p_parent);
		
		metric = new Metric(model, "Child");
		assertEquals(3, metric.get_dit());
		
		metric = new Metric(model, "Parent");
		assertEquals(2, metric.get_dit());
		
		metric = new Metric(model, "ParentParent");
		assertEquals(1, metric.get_dit());
		
		metric = new Metric(model, "ParentParentParent");
		assertEquals(0, metric.get_dit());
	}
	
	@Test
	public void test_dit_multiple_inheritance() {
		Model model = new Model("TestModel");
		Classe child = new Classe("Child"),
				parent = new Classe("Parent"), 
				p_parent = new Classe("ParentParent"),
				parent_ = new Classe("Parent_"),
				p_parent_ = new Classe("ParentParent_"),
				p_p_parent_ = new Classe ("ParentParentParent_");
		ArrayList<String> subs = new ArrayList<String>();
		Metric metric;
		
		subs.add("Child");
		model.add_generalization(new Generalization("Parent", subs));
		model.add_generalization(new Generalization("Parent_", subs));
		
		subs = new ArrayList<String>();
		subs.add("Parent");
		model.add_generalization(new Generalization("ParentParent", subs));
		
		subs = new ArrayList<String>();
		subs.add("Parent_");
		model.add_generalization(new Generalization("ParentParent_", subs));
		
		subs = new ArrayList<String>();
		subs.add("ParentParent_");
		model.add_generalization(new Generalization("ParentParentParent_", subs));
		
		model.add_classe(child);
		model.add_classe(parent);
		model.add_classe(p_parent);
		model.add_classe(parent_);
		model.add_classe(p_parent_);
		model.add_classe(p_p_parent_);
		
		metric = new Metric(model, "Child");
		assertEquals(3, metric.get_dit());
	}
	
	@Test
	public void test_noc() {
		Model model = new Model("TestModel");
		Classe parent = new Classe("Parent"),
				child1 = new Classe("Child1"),
				child2 = new Classe("Child2"),
				child3 = new Classe("Child3");
		ArrayList<String> subs = new ArrayList<String>();
		Metric metric;
		
		
		model.add_classe(parent);
		model.add_classe(child1);
		model.add_classe(child2);
		model.add_classe(child3);
		
		subs.add("Child1");
		subs.add("Child2");
		model.add_generalization(new Generalization("Parent", subs));
		
		metric = new Metric(model, "Parent");
		assertEquals(2, metric.get_noc());
		
		subs = new ArrayList<String>();
		subs.add("Child3");
		model.add_generalization(new Generalization("Parent", subs));
		
		assertEquals(3, metric.get_noc());
	}
	
	@Test
	public void test_nod() {
		Model model = new Model("TestModel");
		Classe parent = new Classe("Parent"),
				child1 = new Classe("Child1"),
				childchild1_1 = new Classe("ChildChild1_1"),
				childchildchild1_1 = new Classe("ChildChildChild1_1"),
				child2 = new Classe("Child2"),
				childchild2_1 = new Classe("ChildChild2_1"),
				childchild2_2 = new Classe("ChildChild2_2"),
				child3 = new Classe("Child3");
		ArrayList<String> subs = new ArrayList<String>();
		Metric metric;
		model.add_classe(parent);
		model.add_classe(child1);
		model.add_classe(childchild1_1);
		model.add_classe(childchildchild1_1);
		model.add_classe(child2);
		model.add_classe(childchild2_1);
		model.add_classe(childchild2_2);
		model.add_classe(child3);
		
		subs.add("Child1");
		subs.add("Child2");
		subs.add("Child3");
		model.add_generalization(new Generalization("Parent", subs));
		
		subs = new ArrayList<String>();
		subs.add("ChildChild1_1");
		model.add_generalization(new Generalization("Child1", subs));
		
		subs = new ArrayList<String>();
		subs.add("ChildChildChild1_1");
		model.add_generalization(new Generalization("ChildChild1_1", subs));
		
		subs = new ArrayList<String>();
		subs.add("ChildChild2_1");
		subs.add("ChildChild2_2");
		model.add_generalization(new Generalization("Child2", subs));
		
		metric = new Metric(model, "Parent");
		
		assertEquals(7, metric.get_nod());
	}
}
