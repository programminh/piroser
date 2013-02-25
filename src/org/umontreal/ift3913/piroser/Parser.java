package org.umontreal.ift3913.piroser;

import java.util.ArrayList;
/**
 * Parser
 * @author Truong Pham
 *
 */
public class Parser {
	private ArrayList<String> tokens;
	private Model model;
	private int current_index;
	private String current_token;
	
	public Parser(ArrayList<String> tokens) throws InvalidUMLException {
		this.tokens = tokens;
		current_index = 0;
		start();
	}
	
	public Model get_model() {
		return model;
	}

	
	private void start() throws InvalidUMLException {
		if(tokens.get(current_index) != "MODEL") throw new InvalidUMLException("UML Diagram must start with `MODEL`");
		read_token();
		
		model = new Model(current_token);
		parse_declarations();
		if(tokens.get(tokens.size() - 1) != ";") throw new InvalidUMLException("Missing `;`");
	}
	
	private boolean expect(String s) {
		return tokens.get(current_index + 1) == s;
	}
	
	public void parse_declarations() throws InvalidUMLException {		
		read_token();
		
		if(current_token == "CLASS") {
			parse_class();
		}
		else if(current_token == "RELATION") {
			parse_association();
		}
		else if(current_token == "AGGREGATION") {
			parse_aggregation();
		}
		else if(current_token == "GENERALIZATION") {
			parse_generalization();
		}
		else {
			throw new InvalidUMLException("Invalid declaration");
		}
	}
	
	public void parse_class() {
		read_token();
	}
	
	public void parse_association() {
		
	}
	
	public void parse_aggregation() {
		
	}
	
	public void parse_generalization() {
		
	}
	
	private void read_token() {
		current_token = tokens.get(++current_index);
	}

}

