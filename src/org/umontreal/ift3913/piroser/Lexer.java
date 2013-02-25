package org.umontreal.ift3913.piroser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Lexer Class
 * This class takes a file as an input and then build an array list 
 * of tokens.
 * @author Truong Pham
 *
 */
public class Lexer {
	private ArrayList<String> tokens;
	
	/**
	 * Constructor
	 * @param file File to tokenize
	 */
	public Lexer(File file) {
		try {
			tokenize(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tokens getter
	 * @return List of tokens
	 */
	public ArrayList<String> get_tokens() {
		return tokens;
	}
	
	/**
	 * Read a file and tokenize all the "words", removing white spaces.
	 * @param file File to tokenize
	 * @return An ArrayList of the tokens
	 * @throws FileNotFoundException
	 */
	private void tokenize(File file) throws FileNotFoundException {
		tokens = new ArrayList<String>();
		int r;
		char c;
		StringBuilder token = new StringBuilder();
		
		try {
			BufferedReader input = new BufferedReader(new FileReader(file));
			
			while((r = input.read()) != -1) {
				// Convert the integer representation of a char to an actual char
				c = (char) r;
				
				if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {	
					
					// Add token to array and reset the string builder if it is a white space
					if(!token.toString().isEmpty()) tokens.add(token.toString());
					token = new StringBuilder();
				}
				else if(c == '(' || c == ')' || c ==',' || c == ';' || c == ':'){
					// When we see a punctuation character, we have to treat it like a single token
					
					// Adding the token before the punctuation char
					if(!token.toString().isEmpty()) tokens.add(token.toString());
					
					// Adding the punctuation char
					tokens.add(String.valueOf(c));
					
					// Reset the String Builder
					token = new StringBuilder();
				}
				else {
					token.append(c);
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
