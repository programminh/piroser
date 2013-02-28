package org.umontreal.ift3913.piroser;

import java.util.ArrayList;
/**
 * Parser.
 * This is where the magic happens. 
 * Unicorn exists, chicken can fly, domestic animals are replaced by Pokemons,
 * I'm the only male in my computer science classes (I secretly wish this one was true), 
 * vampires don't shine, the Internet is still troll-free and cat-free. But, how is my
 * parser magic? Well, it can tell whether YOU, yes YOU, the one who created that diagram,
 * if you have done a great job or a poor job. If you did good it'll show you a fancy 
 * GUI'ed representation of your badass UML-D. If you did wrong, oh boy, it'll spit out
 * some nasty error message and look down upon you, until you correct your mistakes.
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
		if(!tokens.get(current_index).equals("MODEL")) throw new InvalidUMLException("UML Diagram must start with `MODEL`");
		
		// Read the next token.
		read_token();
		
		// Throw error if the diagram constains only Model
		if(this.current_token == END) throw new InvalidUMLException("UML Diagram must have a MODEL name");
		
		// Create new Model
		model = new Model(this.current_token);
		
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
		if(this.current_token.equals(END)) return; 
		
		if(this.current_token.equals("CLASS")) {
			parse_class();
		}
		else if(this.current_token.equals("RELATION")) {
			parse_association();
		}
		else if(this.current_token.equals("AGGREGATION")) {
			parse_aggregation();
		}
		else if(this.current_token.equals("GENERALIZATION")) {
			parse_generalization();
		}
		else {
			System.out.println(this.current_token);
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
		
		if(! valid_identifier(this.current_token)) throw new InvalidUMLException("Invalid CLASS IDENTIFIER");
		
		// Create a new CLASS
		Classe classe = new Classe(this.current_token);
		
		// Parsing ATTRIBUTES
		read_token();
		
		if(! this.current_token.equals("ATTRIBUTES")) throw new InvalidUMLException("Missing ATTRIBUTES in CLASS");
		classe = parse_attributes(classe);
		
		// Parsing OPERATIONS
		read_token();
		if(! this.current_token.equals("OPERATIONS")) throw new InvalidUMLException("Missing OPERATIONS in CLASS");
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
		
		if(valid_identifier(this.current_token) && ! classe.get_attributes().isEmpty()) {
			// Throw an exception if the current token is a valid identifier and the operations array is not empty.
			// It means that we are missing a comma.
			throw new InvalidUMLException("Missing `,` between multiple ATTRIBUTES");
		}
		
		// Read the next token if it is a comma.
		if(this.current_token.equals(",")) read_token();

		// Validate the identifier
		if (! valid_identifier(this.current_token)) throw new InvalidUMLException("Invalid ATTRIBUTES IDENTIFIER");
		// Set the name of the attribute
		name = this.current_token;
	
		read_token();
		if (!this.current_token.equals(":")) throw new InvalidUMLException("Expecting `:` after ATTRIBUTE name");
		
		read_token();
		if(! valid_identifier(this.current_token)) throw new InvalidUMLException("Invalid TYPE IDENTIFIER");
		type = this.current_token;
		
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
		if(this.current_token.equals(";")) {
			return classe;
		}
		else if(valid_identifier(this.current_token) && !classe.get_operations().isEmpty()) {
			// Throw an exception if the current token is a valid identifier and the operations array is not empty.
			// It means that we are missing a comma.
			throw new InvalidUMLException("Missing `,` between multiple OPERATIONS");
		}
		else if(this.current_token.equals(",")) {
			// Read the comma
			read_token();
		}
		
		if(! valid_identifier(this.current_token)) throw new InvalidUMLException("Invalid OPERATIONS IDENTIFIER");
		name = this.current_token;
		
		read_token();
		if(! this.current_token.equals("(")) throw new InvalidUMLException("Expecting `(`");
		
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
		
		if(! this.current_token.equals(":")) throw new InvalidUMLException("Expecting `:`");
		
		
		// Validate the type
		read_token();
		if(! valid_identifier(this.current_token)) throw new InvalidUMLException("Invalid OPERATION type IDENTIFIER");
		type = this.current_token;
		
		
		
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
			if(! valid_identifier(this.current_token)) throw new InvalidUMLException("Invalid ARGUMENT name IDENTIFIER");
			name = this.current_token;
			
			read_token();
			if(! this.current_token.equals(":")) throw new InvalidUMLException("Expecting `:`");
			
			read_token();
			if(! valid_identifier(this.current_token)) throw new InvalidUMLException("Invalid ARGUMENT type IDENTIFIER");
			type = this.current_token;
			
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
				throw new InvalidUMLException("Missing `,` between multiple ARGUMENTS");
			}
		}
		
		return arguments;
	}
	
	/**
	 * Association parser.
	 * Association syntax:
	 * "RELATION" IDENTIFIER "ROLES" role, role; 
	 * @throws InvalidUMLException 
	 */
	private void parse_association() throws InvalidUMLException {
		String relation_name, role_name, role_multiplicity;
		Role first_role, second_role;
		Association association;

		read_token();
		if(! valid_identifier(this.current_token)) throw new InvalidUMLException("Invalid ASSOCIATION IDENTIFIER");
		relation_name = this.current_token;
		
		read_token();
		if(! this.current_token.equals("ROLES")) throw new InvalidUMLException("Expecting `ROLES` after ASSOCIATION IDENTIFIER");
		
		// Create the first role.
		first_role = parse_role();
		
		// Check for comma
		read_token();
		if(! this.current_token.equals(",")) throw new InvalidUMLException("Missing `,` between ROLES");
		
		// Create second role
		second_role = parse_role();
		
		// Check for closing semicolon
		read_token();
		if(! this.current_token.equals(";")) throw new InvalidUMLException("Expecting closing `;` after ASSOCIATION");
		
		association = new Association(relation_name, first_role, second_role);
		model.add_association(association);
		
		parse_declarations();
	}
	
	/**
	 * Role parser.
	 * Role syntax is always:
	 * "CLASS" IDENTIFIER MULTIPLICITY
	 * @return
	 * @throws InvalidUMLException 
	 */
	private Role parse_role() throws InvalidUMLException {
		String name, multiplicity;
		
		// Check for CLASS keyword
		read_token();
		if(! this.current_token.equals("CLASS")) throw new InvalidUMLException("Expecting `CLASS` before ROLE IDENTIFIER");
		
		// Validate IDENTIFIER
		read_token();
		if(! valid_identifier(this.current_token)) throw new InvalidUMLException("Invalid ROLE IDENTIFIER");
		name = this.current_token;
		
		// Validate MULTIPLICITY
		read_token();
		if(! Role.multiplicity_is_valid(this.current_token)) throw new InvalidUMLException("Invalid MULTIPLICITY");
		multiplicity = this.current_token;		
		
		return new Role(name, multiplicity);
	}
	
	/**
	 * Aggregation parser.
	 * Aggregation syntax: 
	 * "AGGREGATION" "CONTAINER" role "PARTS" roles;
	 * @throws InvalidUMLException
	 */
	private void parse_aggregation() throws InvalidUMLException {
		Aggregation aggregation;
		Role container, part;
		ArrayList<Role> parts = new ArrayList<Role>();
		
		read_token();
		if(! this.current_token.equals("CONTAINER")) throw new InvalidUMLException("Expecting `CONTAINER` after `AGGREGATION`");
		
		// Create container
		container = parse_role();
		
		read_token();
		if(! this.current_token.equals("PARTS")) throw new InvalidUMLException("Expecting `PARTS` after container");
		
		// Loop to construct the parts
		while(true) {
			part = parse_role();
			parts.add(part);	
			
			read_token();
			if(current_token.equals(";")) {
				// Read the closing semicolon and break from the loop.
				break;
			}
			else if(current_token.equals("CLASS")) {
				// If the token is CLASS they have forgotten a comma
				throw new InvalidUMLException("Expecting `,` between part");
			}
			else if(current_token.equals(",")) {
				// Loop again
				continue;
			}
			else {
				// If it is not a semicolon, not comma nor CLASS it means there is an error
				throw new InvalidUMLException("Expecting closing `;` after aggregation");
			}
		}
		
		aggregation = new Aggregation(container, parts);
		model.add_aggregation(aggregation);
		
		parse_declarations();
	}
	
	/**
	 * Generalization parser.
	 * Generalization syntax:
	 * "GENERALIZATION" IDENTIFIER "SUBCLASSES" subclasses;
	 * @throws InvalidUMLException
	 */
	private void parse_generalization() throws InvalidUMLException {
		Generalization generalization;
		String name;
		ArrayList<String> subclasses = new ArrayList<String>();
		
		// Validate name
		read_token();
		if(! valid_identifier(this.current_token)) throw new InvalidUMLException("Invalid GENERALIZATION IDENTIFIER");
		name = this.current_token;
		
		// Check for `SUBCLASS` keyword
		read_token();
		if(! this.current_token.equals("SUBCLASSES")) throw new InvalidUMLException("Expecting `SUBCLASSES` after IDENTIFIER");
		
		// Loop to check for subclasses
		while(true) {
			// Check for valid identifier
			read_token();
			if(! valid_identifier(this.current_token)) throw new InvalidUMLException("Invalid subclass IDENTIFIER");
			subclasses.add(this.current_token);
			
			
			read_token();
			if(this.current_token.equals(";")) {
				// Break from loop because of closing semicolon
				break;
			}
			else if(valid_identifier(this.current_token)) {
				// Missing a comma
				throw new InvalidUMLException("Expecting `,` between subclasses");
			}
			else if(this.current_token.equals(",")){
				// Error
				continue;
			}
			else {
				throw new InvalidUMLException("Expecting closing `;` after generalization");
			}	
		}
		
		generalization = new Generalization(name, subclasses);
		this.model.add_generalization(generalization);
		
		parse_declarations();
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
			this.current_token = END;
		}
		else {
			this.current_token = tokens.get(++current_index);
		}
	}

}

