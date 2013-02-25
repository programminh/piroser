package org.umontreal.ift3913.piroser;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ParserTest {
	private ArrayList<String> valid_tokens;
	private Parser parser;
	
	@BeforeClass
	public void setUp() {
		Lexer lexer = new Lexer(new File("Ligue.ucd"));
		valid_tokens = lexer.get_tokens();
		parser = new Parser(valid_tokens);
	}
	
	
	@Test
	public void test_valid_MODEL() throws InvalidUMLException {
		assertEquals("Ligue", parser.get_model().get_name());
	}
	
	@Test
	public void 

}
