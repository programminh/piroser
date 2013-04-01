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
}
