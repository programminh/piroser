package org.umontreal.ift3913.piroser;

import static org.junit.Assert.*;
import java.io.*;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * Testing the Lexer class
 * @author Truong Pham
 *
 */
public class LexerTest {

	private File file;
	private ArrayList<String> expected_tokens;

	@Before
	public void setUp() {
		file = new File("test/file.txt");
		
		// Expected tokens
		expected_tokens = new ArrayList<String>();
		expected_tokens.add("Bonjour");
		expected_tokens.add(",");
		expected_tokens.add("ceci");
		expected_tokens.add("est");
		expected_tokens.add(";");
		expected_tokens.add("un");
		expected_tokens.add("test");
		expected_tokens.add(";");
	}
	
	@Test
	public void test_tokenize() {
		Lexer lexer = new Lexer(file);
		assertEquals(expected_tokens, lexer.get_tokens());
	}
}
