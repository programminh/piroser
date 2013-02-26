package org.umontreal.ift3913.piroser.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.umontreal.ift3913.piroser.Argument;
import org.umontreal.ift3913.piroser.Attribute;
import org.umontreal.ift3913.piroser.Classe;
import org.umontreal.ift3913.piroser.InvalidUMLException;
import org.umontreal.ift3913.piroser.Model;
import org.umontreal.ift3913.piroser.Operation;
import org.umontreal.ift3913.piroser.Parser;


public class ParserTest {
	private ArrayList<String> tokens;
	
	@Test
	public void test_valid_model() throws InvalidUMLException {
		String[] toks = {"MODEL", "Ligue"};
		tokens = new ArrayList<String>(Arrays.asList(toks));
		Parser parser = new Parser(tokens);
		
		assertEquals("Ligue", parser.get_model().get_name());
	}
	
	@Test(expected=InvalidUMLException.class)
	public void test_nameless_model() throws InvalidUMLException {
		String[] toks = {"MODEL"};
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		Parser parser = new Parser(tokens);
	}
	
	@Test(expected=InvalidUMLException.class)
	public void test_invalid_beginning_of_tokens() throws InvalidUMLException {
		String[] toks = {"MooDEL"};
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		Parser parser = new Parser(tokens);
	}
	
	@Test
	public void test_lol() throws InvalidUMLException {
		String[] strings = {"ALLO", "11allo", "AllO", "a_lo", "a-lo", "A__LO"};
		for(String s : strings) {
			assertTrue(s.matches("^[1-9a-zA-Z-_]+"));
		}
	}
	
	@Test
	public void test_lo1l() throws InvalidUMLException {
		String[] strings = {"ALLO+", "+", ";"};
		for(String s : strings) {
			assertFalse(s.matches("^[1-9a-zA-Z-_]+"));
		}
	}
	
	@Test
	public void test_valid_class() throws InvalidUMLException {
		String[] toks = {"MODEL", "Ligue", 
				"CLASS", "Joueur", 
				"ATTRIBUTES", 
				"nom", ":", "String", ",",
				"nom_de_famille", ":", "String",
				"OPERATIONS", 
				"get_nom", "(", ")", ":", "String", ",",
				"set_nom", "(", "prenom", ":", "String", ",", "nom_de_famille", ":", "String", ")", ":", "void",
				";"};
		
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		// Parse tokens
		Parser parser = new Parser(tokens);

		// Fetch model
		Model model = parser.get_model();
		
		// Fetch the only class
		Classe classe = model.get_classes().get(0);
		
		// Fetch the attributes
		ArrayList<Attribute> attributes = classe.get_attributes();
		
		// Fetch the operations
		ArrayList<Operation> operations = classe.get_operations();
		
		// Fetch the arguments of the second operations only since the first operation doesn't have any
		ArrayList<Argument> arguments = operations.get(1).get_arguments();
		
		assertEquals("Ligue", model.get_name());
		assertEquals("Joueur", classe.get_name());
		assertEquals("nom", attributes.get(0).get_name());
		assertEquals("String", attributes.get(0).get_type());
		assertEquals("get_nom", operations.get(0).get_name());
		assertEquals("String", operations.get(0).get_type());
		assertEquals("set_nom", operations.get(1).get_name());
		assertEquals("prenom", arguments.get(0).get_name());
		assertEquals("String", arguments.get(0).get_type());
		assertEquals("nom_de_famille", arguments.get(1).get_name());
		assertEquals("String", arguments.get(1).get_type());
		assertEquals("void", operations.get(1).get_type());
	}
	
	@Test(expected=InvalidUMLException.class)
	public void test_invalid_class_missing_comma_in_attributes() throws InvalidUMLException {
		String[] toks = {"MODEL", "Ligue", 
				"CLASS", "Joueur", 
				"ATTRIBUTES", 
				"nom", ":", "String",
				"nom_de_famille", ":", "String",
				"OPERATIONS", 
				"get_nom", "(", ")", ":", "String", ",",
				"set_nom", "(", "prenom", ":", "String", ",", "nom_de_famille", ":", "String", ")", ":", "void",
				";"};
		
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		// Parse tokens
		Parser parser = new Parser(tokens);
	}
	
	@Test(expected=InvalidUMLException.class)
	public void test_invalid_class_missing_comma_in_operations() throws InvalidUMLException {
		String[] toks = {"MODEL", "Ligue", 
				"CLASS", "Joueur", 
				"ATTRIBUTES", 
				"nom", ":", "String", ",",
				"nom_de_famille", ":", "String",
				"OPERATIONS", 
				"get_nom", "(", ")", ":", "String",
				"set_nom", "(", "prenom", ":", "String", ",", "nom_de_famille", ":", "String", ")", ":", "void",
				";"};
		
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		// Parse tokens
		Parser parser = new Parser(tokens);
	}
	
	@Test(expected=InvalidUMLException.class)
	public void test_invalid_class_missing_comma_in_arguments() throws InvalidUMLException {
		String[] toks = {"MODEL", "Ligue", 
				"CLASS", "Joueur", 
				"ATTRIBUTES", 
				"nom", ":", "String", ",",
				"nom_de_famille", ":", "String",
				"OPERATIONS", 
				"get_nom", "(", ")", ":", "String",
				"set_nom", "(", "prenom", ":", "String", "nom_de_famille", ":", "String", ")", ":", "void",
				";"};
		
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		// Parse tokens
		Parser parser = new Parser(tokens);
	}
	
	@Test
	public void test_valid_association() throws InvalidUMLException {
		String[] toks = {"MODEL", "Ligue", 
				"RELATION", "est_localisee_a",
				"ROLES",
				"CLASS", "Entraineur", "ONE_OR_MANY", ",",
				"CLASS", "Equipe", "ONE"};
		
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		Parser parser = new Parser(tokens);
		Model model = parser.get_model();
		
		assertEquals("est_localise_a", model.get_associations().get(0).get_name());
		assertEquals("Entraineur", model.get_associations().get(0).get_first_role().get_name());
		assertEquals("ONE_OR_MANY", model.get_associations().get(0).get_first_role().get_multiplicity());
		assertEquals("Equipe", model.get_associations().get(0).get_second_role().get_name());
		assertEquals("ONE", model.get_associations().get(0).get_second_role().get_multiplicity());
	}
	
	@Test(expected=InvalidUMLException.class)
	public void test_association_missing_one_role() throws InvalidUMLException {
		String[] toks = {"MODEL", "Ligue", 
				"RELATION", "est_localisee_a",
				"ROLES",
				"CLASS", "Entraineur", "ONE_OR_MANY"};
		
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		Parser parser = new Parser(tokens);
	}
	
	@Test(expected=InvalidUMLException.class)
	public void test_association_missing_comma_between_role() throws InvalidUMLException {
		String[] toks = {"MODEL", "Ligue", 
				"RELATION", "est_localisee_a",
				"ROLES",
				"CLASS", "Entraineur", "ONE_OR_MANY",
				"CLASS", "Equipe", "ONE"};
		
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		Parser parser = new Parser(tokens);
	}
}
