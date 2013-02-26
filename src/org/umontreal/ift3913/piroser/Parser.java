package org.umontreal.ift3913.piroser;

import java.util.ArrayList;
/**
 * Parser
 * @author Truong Pham
 *
 */
public class Parser {
	private static final String END = "!!END!!";
	private static final String[] RESERVED_KEYWORDS = {
		"CLASS",
		"MODEL",
		"ATTRIBUTES",
		"OPERATIONS",
		"GENERALIZATION",
		"SUBCLASSES",
		"RELATION",
		"AGGREGATION",
		"CONTAINER",
		"PARTS",
	};
	private ArrayList<String> tokens;
	private Model model;
	private int current_index;
	private String current_token;
	
	/**
	 * Constructor
	 * @param tokens ArrayList of tokens
	 * @throws InvalidUMLException
	 */
	public Parser(ArrayList<String> tokens) throws InvalidUMLException {
		this.tokens = tokens;
		current_index = 0;
		start();
	}
	
	/**
	 * Model getter
	 * @return The Model
	 */
	public Model get_model() {
		return model;
	}

	
	/**
	 * Starts the parsing process by parsing the MODEL name
	 * @throws InvalidUMLException
	 */
	private void start() throws InvalidUMLException {
		// Throw error if the diagram does not start with MODEL
		if(tokens.get(current_index) != "MODEL") throw new InvalidUMLException("UML Diagram must start with `MODEL`");
		
		// Read the next token.
		read_token();
		
		// Throw error if the diagram constains only Model
		if(current_token == END) throw new InvalidUMLException("UML Diagram must have a MODEL name");
		
		// Create new Model
		model = new Model(current_token);
		
		// Parse the declaration
		parse_declarations();
	}
	
	/**
	 * Returns whether the identifier is valid or not.
	 * Identifier must contain only accent-free alphanumeric characters, hyphen or underscore.
	 * Identifier must not match a reserved keyword.
	 * @param identifier
	 * @return
	 */
	private boolean valid_identifier(String identifier) {
		return identifier.matches("^[a-zA-Z0-9_-]+") && keyword_not_reserved(identifier);
	}
	
	/**
	 * Check if the word is a reserved keyword
	 * @param word The string to check
	 * @return True if the word is not reserved
	 */
	private boolean keyword_not_reserved(String word) {
		for(String reserved : RESERVED_KEYWORDS) {
			if(word.equalsIgnoreCase(reserved)) return false;
		}
		
		return true;
	}
	
	/**
	 * Declaration parser.
	 * This parser detects which part needs to be parsed and gives control to the appropriate
	 * function.
	 * @throws InvalidUMLException
	 */
	private void parse_declarations() throws InvalidUMLException {		
		read_token();
		
		// End the parsing when there is no more token
		if(current_token == END) return; 
		
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
	
	/**
	 * CLASS parser.
	 * It starts with parsing the attributes and then the operations
	 * before giving the control back to the declaration parser.
	 * @throws InvalidUMLException
	 */
	private void parse_class() throws InvalidUMLException {
		read_token();
		
		if(current_token == END) throw new InvalidUMLException("Missing CLASS IDENTIFIER");
		if(! valid_identifier(current_token)) throw new InvalidUMLException("Invalid CLASS IDENTIFIER");
		
		// Create a new CLASS
		Classe classe = new Classe(current_token);
		
		// Parsing ATTRIBUTES
		read_token();
		if(current_token != "ATTRIBUTES") throw new InvalidUMLException("Missing ATTRIBUTES in CLASS");
		classe = parse_attributes(classe);
		
		// Parsing OPERATIONS
		read_token();
		if(current_token != "OPERATIONS") throw new InvalidUMLException("Missing OPERATIONS in CLASS");
		classe = parse_operations(classe);

		// Add the class to the model
		model.add_classe(classe);
		
		parse_declarations();
	}
	
	/**
	 * Attribute parser.
	 * Parse the attribute of a CLASS and recursively adds them to the CLASS in input 
	 * before sending back the control to the class parser.
	 * @param c The Classe
	 * @return The Classe with the attributes 
	 * @throws InvalidUMLException
	 */
	private Classe parse_attributes(Classe c) throws InvalidUMLException {
		Classe classe = c;
		Attribute attribute;
		String name, type;
		
		// Return the classe, we are done with attributes
		if(peek_token().equals("OPERATIONS")) return classe;
		
		read_token();
		
		if(valid_identifier(current_token) && ! classe.get_attributes().isEmpty()) {
			// Throw an exception if the current token is a valid identifier and the operations array is not empty.
			// It means that we are missing a comma.
			throw new InvalidUMLException("Missing `,` between multiple ATTRIBUTES");
		}
		
		// Read the next token if it is a comma.
		if(current_token.equals(",")) read_token();

		// Validate the identifier
		if (! valid_identifier(current_token)) throw new InvalidUMLException("Invalid ATTRIBUTES IDENTIFIER");
		// Set the name of the attribute
		name = current_token;
	
		read_token();
		if (!current_token.equals(":")) throw new InvalidUMLException("Expecting `:` after ATTRIBUTE name");
		
		read_token();
		if(! valid_identifier(current_token)) throw new InvalidUMLException("Invalid TYPE IDENTIFIER");
		type = current_token;
		
		// Create attribute
		attribute = new Attribute(name, type);
		classe.add_attribute(attribute);
		
		return parse_attributes(classe);
	}
	
	/**
	 * Operation parser.
	 * Parse the Operation of a CLASS and recursively adds it to the inputed CLASS
	 * before giving back control to the Classe parser.
	 * @param c The Classe
	 * @return The Classe with the operations
	 * @throws InvalidUMLException
	 */
	private Classe parse_operations(Classe c) throws InvalidUMLException {
		Classe classe = c;
		Operation operation;
		ArrayList<Argument> arguments;
		String name, type;
	
		read_token();	
		// Terminate the operations parsing when there is a semicolon;
		if(current_token.equals(";")) {
			return classe;
		}
		else if(valid_identifier(current_token) && !classe.get_operations().isEmpty()) {
			// Throw an exception if the current token is a valid identifier and the operations array is not empty.
			// It means that we are missing a comma.
			throw new InvalidUMLException("Missing `,` between multiple OPERATIONS");
		}
		else if(current_token.equals(",")) {
			// Read the comma
			read_token();
		}
		
		if(! valid_identifier(current_token)) throw new InvalidUMLException("Invalid OPERATIONS IDENTIFIER");
		name = current_token;
		
		read_token();
		if(! current_token.equals("(")) throw new InvalidUMLException("Expecting `(`");
		
		// If the parenthesis is not closed right after the opened one, we need to parse the arguments
		if(! peek_token().equals(")")) {
			arguments = parse_arguments();
		}
		else {
			arguments = new ArrayList<Argument>();
		}
		
		// Read the closing parenthesis
		read_token();
		
		// Read the colon
		read_token();
		
		if(! current_token.equals(":")) throw new InvalidUMLException("Expecting `:`");
		
		
		// Validate the type
		read_token();
		if(! valid_identifier(current_token)) throw new InvalidUMLException("Invalid OPERATION type IDENTIFIER");
		type = current_token;
		
		
		
		// Create the operation and add it to the class
		operation = new Operation(name, type, arguments);
		classe.add_operation(operation);
		
		return parse_operations(classe);
	}
	
	/**
	 * Argument parser.
	 * Parse the arguments of an operations
	 * @return ArrayList of arguments
	 * @throws InvalidUMLException
	 */
	private ArrayList<Argument> parse_arguments() throws InvalidUMLException {
		ArrayList<Argument> arguments = new ArrayList<Argument>();
		Argument argument;
		String name, type;
		
		// Loop to build an argument.
		// Each argument must be in this format: "name : type"
		while(true) {
			read_token();
			if(! valid_identifier(current_token)) throw new InvalidUMLException("Invalid ARGUMENT name IDENTIFIER");
			name = current_token;
			
			read_token();
			if(! current_token.equals(":")) throw new InvalidUMLException("Expecting `:`");
			
			read_token();
			if(! valid_identifier(current_token)) throw new InvalidUMLException("Invalid ARGUMENT type IDENTIFIER");
			type = current_token;
			
			argument = new Argument(name, type);
			arguments.add(argument);
			
			// Stop the loop if the next token is a closing parenthesis
			if(peek_token().equals(")")) {
				break;
			}
			else if(peek_token().equals(",")) {
				// If the next token is a comma, read it and parse the next argument
				read_token();
			}
			else {
				// Else throw an error
				throw new InvalidUMLException("Invalid ARGUMENTS");
			}
		}
		
		return arguments;
	}
	
	/**
	 * Association parser
	 */
	private void parse_association() {
		
	}
	
	private void parse_aggregation() {
		
	}
	
	private void parse_generalization() {
		
	}
	
	private String peek_token() {
		if(current_index == tokens.size() - 1) {
			return END;
		}
		else {
			return tokens.get(current_index + 1);
		}
	}
	
	private void read_token() {
		if(current_index == tokens.size() - 1) {
			current_token = END;
		}
		else {
			current_token = tokens.get(++current_index);
		}
	}

}

