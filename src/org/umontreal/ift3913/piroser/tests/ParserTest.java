package org.umontreal.ift3913.piroser.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.umontreal.ift3913.piroser.Aggregation;
import org.umontreal.ift3913.piroser.Argument;
import org.umontreal.ift3913.piroser.Association;
import org.umontreal.ift3913.piroser.Attribute;
import org.umontreal.ift3913.piroser.Classe;
import org.umontreal.ift3913.piroser.Generalization;
import org.umontreal.ift3913.piroser.InvalidUMLException;
import org.umontreal.ift3913.piroser.Lexer;
import org.umontreal.ift3913.piroser.Model;
import org.umontreal.ift3913.piroser.Operation;
import org.umontreal.ift3913.piroser.Parser;


public class ParserTest {
	private ArrayList<String> tokens;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
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
	public void test_valid_class() throws InvalidUMLException {
		String[] toks = {"MODEL", "Ligue", 
				"CLASS", "Joueur", 
				"ATTRIBUTES", 
				"nom", ":", "String", ",",
				"nom_de_famille", ":", "String",
				"OPERATIONS", 
				"get_nom", "(", ")", ":", "String", ",",
				"set_nom", "(", "prenom", ":", "String", ",", "nom_de_famille", ":", "String", ")", ":", "void",
				";",
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
	
	@Test
	public void test_invalid_class_missing_comma_in_attributes() throws InvalidUMLException {
		thrown.expect(InvalidUMLException.class);
		thrown.expectMessage("Missing `,` between multiple ATTRIBUTES");
		
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
	
	@Test
	public void test_invalid_class_missing_comma_in_operations() throws InvalidUMLException {
		thrown.expect(InvalidUMLException.class);
		thrown.expectMessage("Missing `,` between multiple OPERATIONS");
		
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
	
	@Test
	public void test_invalid_class_missing_comma_in_arguments() throws InvalidUMLException {
		thrown.expect(InvalidUMLException.class);
		thrown.expectMessage("Missing `,` between multiple ARGUMENTS");
		
		String[] toks = {"MODEL", "Ligue", 
				"CLASS", "Joueur", 
				"ATTRIBUTES", 
				"nom", ":", "String", ",",
				"nom_de_famille", ":", "String",
				"OPERATIONS", 
				"get_nom", "(", ")", ":", "String", ",",
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
				"CLASS", "Equipe", "ONE", ";"};
		
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		Parser parser = new Parser(tokens);
		Model model = parser.get_model();
		
		assertEquals("est_localisee_a", model.get_associations().get(0).get_name());
		assertEquals("Entraineur", model.get_associations().get(0).get_first_role().get_name());
		assertEquals("ONE_OR_MANY", model.get_associations().get(0).get_first_role().get_multiplicity());
		assertEquals("Equipe", model.get_associations().get(0).get_second_role().get_name());
		assertEquals("ONE", model.get_associations().get(0).get_second_role().get_multiplicity());
	}
	
	
	@Test
	public void test_association_missing_closing_semicolon() throws InvalidUMLException {
		thrown.expect(InvalidUMLException.class);
		thrown.expectMessage("Expecting closing `;` after ASSOCIATION");
		
		String[] toks = {"MODEL", "Ligue", 
				"RELATION", "est_localisee_a",
				"ROLES",
				"CLASS", "Entraineur", "ONE_OR_MANY", ",",
				"CLASS", "Joueur", "ONE"};
		
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		Parser parser = new Parser(tokens);
	}
	
	@Test
	public void test_association_missing_comma_between_role() throws InvalidUMLException {
		thrown.expect(InvalidUMLException.class);
		thrown.expectMessage("Missing `,` between ROLES");
		
		String[] toks = {"MODEL", "Ligue", 
				"RELATION", "est_localisee_a",
				"ROLES",
				"CLASS", "Entraineur", "ONE_OR_MANY",
				"CLASS", "Equipe", "ONE"};
		
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		Parser parser = new Parser(tokens);
	}
	
	@Test
	public void test_association_invalid_multiplicty() throws InvalidUMLException {
		thrown.expect(InvalidUMLException.class);
		thrown.expectMessage("Invalid MULTIPLICITY");
		
		String[] toks = {"MODEL", "Ligue", 
				"RELATION", "est_localisee_a",
				"ROLES",
				"CLASS", "Entraineur", "Banane",
				"CLASS", "Equipe", "ONE"};
		
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		Parser parser = new Parser(tokens);
	}
	
	@Test
	public void test_association_missing_role_keyword() throws InvalidUMLException {
		thrown.expect(InvalidUMLException.class);
		thrown.expectMessage("Expecting `ROLES` after ASSOCIATION IDENTIFIER");
		
		String[] toks = {"MODEL", "Ligue", 
				"RELATION", "est_localisee_a",
				"CLASS", "Entraineur", "Banane",
				"CLASS", "Equipe", "ONE"};
		
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		Parser parser = new Parser(tokens);
	}
	
	@Test
	public void test_association_missing_class_keyword() throws InvalidUMLException {
		thrown.expect(InvalidUMLException.class);
		thrown.expectMessage("Expecting `CLASS` before ROLE IDENTIFIER");
		
		String[] toks = {"MODEL", "Ligue", 
				"RELATION", "est_localisee_a", "ROLES",
				"Entraineur", "Banane",
				"CLASS", "Equipe", "ONE"};
		
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		Parser parser = new Parser(tokens);
	}
	
	@Test
	public void test_valid_aggregation() throws InvalidUMLException {		
		String[] toks = {"MODEL", "Ligue", 
				"AGGREGATION", "CONTAINER",
				"CLASS", "Equipe", "ONE",
				"PARTS",
				"CLASS", "Joueur", "ONE_OR_MANY", ",",
				"CLASS", "Stade", "ONE", ";"};
		
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		Parser parser = new Parser(tokens);
		Model model = parser.get_model();
		// Get First Aggregation
		Aggregation aggregation = model.get_aggregations().get(0);
		
		assertEquals("Equipe", aggregation.get_container().get_name());
		assertEquals("ONE", aggregation.get_container().get_multiplicity());
		assertEquals("Joueur", aggregation.get_parts().get(0).get_name());
		assertEquals("ONE_OR_MANY", aggregation.get_parts().get(0).get_multiplicity());
		assertEquals("Stade", aggregation.get_parts().get(1).get_name());
		assertEquals("ONE", aggregation.get_parts().get(1).get_multiplicity());
		
	}
	
	@Test
	public void test_aggregation_missing_container_keyword() throws InvalidUMLException {	
		thrown.expect(InvalidUMLException.class);
		thrown.expectMessage("Expecting `CONTAINER` after `AGGREGATION`");
		
		String[] toks = {"MODEL", "Ligue", 
				"AGGREGATION",
				"CLASS", "Equipe", "ONE",
				"PARTS",
				"CLASS", "Joueur", "ONE_OR_MANY", ",",
				"CLASS", "Stade", "ONE", ";"};
		
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		Parser parser = new Parser(tokens);
	}
	
	@Test
	public void test_aggregation_missing_parts_keyword() throws InvalidUMLException {	
		thrown.expect(InvalidUMLException.class);
		thrown.expectMessage("Expecting `PARTS` after container");
		
		String[] toks = {"MODEL", "Ligue", 
				"AGGREGATION",
				"CONTAINER",
				"CLASS", "Equipe", "ONE",
				"CLASS", "Joueur", "ONE_OR_MANY", ",",
				"CLASS", "Stade", "ONE", ";"};
		
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		Parser parser = new Parser(tokens);
	}
	
	@Test
	public void test_aggregation_missing_container_role() throws InvalidUMLException {	
		thrown.expect(InvalidUMLException.class);
		thrown.expectMessage("Expecting `CLASS` before ROLE IDENTIFIER");
		
		String[] toks = {"MODEL", "Ligue", 
				"AGGREGATION",
				"CONTAINER",
				"PARTS",
				"CLASS", "Joueur", "ONE_OR_MANY", ",",
				"CLASS", "Stade", "ONE", ";"};
		
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		Parser parser = new Parser(tokens);
	}
	
	@Test
	public void test_aggregation_missing_comma_between_parts() throws InvalidUMLException {	
		thrown.expect(InvalidUMLException.class);
		thrown.expectMessage("Expecting `,` between part");
		
		String[] toks = {"MODEL", "Ligue", 
				"AGGREGATION",
				"CONTAINER",
				"CLASS", "Equipe", "ONE",
				"PARTS",
				"CLASS", "Joueur", "ONE_OR_MANY",
				"CLASS", "Stade", "ONE", ";"};
		
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		Parser parser = new Parser(tokens);
	}
	
	@Test
	public void test_aggregation_missing_closing_semicolon() throws InvalidUMLException {	
		thrown.expect(InvalidUMLException.class);
		thrown.expectMessage("Expecting closing `;` after aggregation");
		
		String[] toks = {"MODEL", "Ligue", 
				"AGGREGATION",
				"CONTAINER",
				"CLASS", "Equipe", "ONE",
				"PARTS",
				"CLASS", "Joueur", "ONE_OR_MANY", ",",
				"CLASS", "Stade", "ONE"};
		
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		Parser parser = new Parser(tokens);
	}
	
	@Test
	public void test_valid_generalization() throws InvalidUMLException {
		String[] toks = {"MODEL", "Ligue",
				"GENERALIZATION", "Participant",
				"SUBCLASSES", "Joueur", ",", "Entraineur", ";"};
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		Parser parser = new Parser(tokens);
		Model model = parser.get_model();
		Generalization generalization = model.get_generalizations().get(0);
		
		assertEquals("Participant", generalization.get_name());
		assertEquals("Joueur", generalization.get_subclasses().get(0));
		assertEquals("Entraineur", generalization.get_subclasses().get(1));
	}
	
	@Test
	public void test_generalization_missing_valid_generalization_identifier() throws InvalidUMLException {	
		thrown.expect(InvalidUMLException.class);
		thrown.expectMessage("Invalid GENERALIZATION IDENTIFIER");
		
		String[] toks = {"MODEL", "Ligue",
				"GENERALIZATION", "!",
				"SUBCLASSES", "Joueur", ",", "Entraineur", ";"};
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		Parser parser = new Parser(tokens);
	}
	
	@Test
	public void test_generalization_missing_valid_subclass_identifier() throws InvalidUMLException {	
		thrown.expect(InvalidUMLException.class);
		thrown.expectMessage("Invalid subclass IDENTIFIER");
		
		String[] toks = {"MODEL", "Ligue",
				"GENERALIZATION", "Participant",
				"SUBCLASSES", "Joueur", ",", "!", ";"};
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		Parser parser = new Parser(tokens);
	}
	
	@Test
	public void test_generalization_missing_comma_between_subclasses() throws InvalidUMLException {	
		thrown.expect(InvalidUMLException.class);
		thrown.expectMessage("Expecting `,` between subclasses");
		
		String[] toks = {"MODEL", "Ligue",
				"GENERALIZATION", "Participant",
				"SUBCLASSES", "Joueur", "Entraineur", ";"};
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		Parser parser = new Parser(tokens);
	}
	
	@Test
	public void test_generalization_missing_subclasses_keyword() throws InvalidUMLException {	
		thrown.expect(InvalidUMLException.class);
		thrown.expectMessage("Expecting `SUBCLASSES` after IDENTIFIER");
		
		String[] toks = {"MODEL", "Ligue",
				"GENERALIZATION", "Participant",
				"Joueur", ",", "Entraineur", ";"};
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		Parser parser = new Parser(tokens);
	}
	
	@Test
	public void test_generalization_missing_closing_semicolon() throws InvalidUMLException {	
		thrown.expect(InvalidUMLException.class);
		thrown.expectMessage("Expecting closing `;` after generalization");
		
		String[] toks = {"MODEL", "Ligue",
				"GENERALIZATION", "Participant",
				"SUBCLASSES", "Joueur", ",", "Entraineur"};
		tokens = new ArrayList<String>(Arrays.asList(toks));
		
		Parser parser = new Parser(tokens);
	}
	
	@Test
	public void test_ultimate() throws InvalidUMLException {
		Lexer lexer = new Lexer(new File("Ligue.ucd"));
		Parser parser = new Parser(lexer.get_tokens());
		
		Model model = parser.get_model();
		ArrayList<Classe> classes = model.get_classes();
		ArrayList<Aggregation> aggregations = model.get_aggregations();
		ArrayList<Association> associations = model.get_associations();
		ArrayList<Generalization> generalizations = model.get_generalizations();
		
		assertEquals("Ligue", model.get_name());
		assertEquals("Equipe", classes.get(0).get_name());
		assertEquals("nom_equipe", classes.get(0).get_attributes().get(0).get_name());
		assertEquals("String", classes.get(0).get_attributes().get(0).get_type());
		assertEquals("entraineur", classes.get(0).get_operations().get(1).get_name());
		assertEquals("String", classes.get(0).get_operations().get(1).get_type());
		assertEquals("add_joueur", classes.get(0).get_operations().get(2).get_name());
		assertEquals("element", classes.get(0).get_operations().get(2).get_arguments().get(0).get_name());
		assertEquals("Joueur", classes.get(0).get_operations().get(2).get_arguments().get(0).get_type());
		assertEquals("Joueur", classes.get(2).get_name());
		assertEquals("Participant", generalizations.get(0).get_name());
		assertEquals("Joueur", generalizations.get(0).get_subclasses().get(0));
		assertEquals("Entraineur", generalizations.get(0).get_subclasses().get(1));
		assertEquals("dirige", associations.get(1).get_name());
		assertEquals("Equipe", associations.get(1).get_second_role().get_name());
		assertEquals("ONE", associations.get(1).get_second_role().get_multiplicity());
		assertEquals("Equipe", aggregations.get(0).get_container().get_name());
		assertEquals("ONE", aggregations.get(0).get_container().get_multiplicity());
		assertEquals("Joueur", aggregations.get(0).get_parts().get(0).get_name());
		assertEquals("ONE_OR_MANY", aggregations.get(0).get_parts().get(0).get_multiplicity());
	}
}
