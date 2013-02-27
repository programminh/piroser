package org.umontreal.ift3913.piroser;

/**
 * Exception to be raised when an error occurs in the parsing process
 * @author Truong Pham
 *
 */
public class InvalidUMLException extends Exception {
	
	public InvalidUMLException(String message) {
		super(message);
	}
}
