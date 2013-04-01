package org.umontreal.ift3913.piroser;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 * Piroser
 * UML Diagram Parser.
 * @version 0.0.1
 * @author Truong Pham
 *
 */
public class Piroser extends JFrame {
	private JButton btn_load_file;
	private JButton btn_parse;
	private JTextField text_file_path;
	private JTextArea textarea_details;
	private JList list_classes;
	private JList list_subclasses;
	private JList list_assoc_aggre;
	private JList list_methods;
	private JList list_attributes;
	private JPanel panel_header;
	private JPanel panel_classes;
	private JPanel panel_components;
	private JPanel panel_components_container;
	private JPanel panel_raw_details;
	private JFileChooser file_chooser;
	private File file;
	private Lexer lexer;
	private Parser parser;
	private Model model;
	
	
	public Piroser() {
		this.init();
	}
	
	private void init() {
		// Window Properties
		setTitle("Piroser 0.0.1");
		setSize(900,700);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// Header panel
		panel_header = new JPanel();
		
		// Create Load File Button
		btn_load_file = new JButton("Load file");
		// Listen to button being pressed
		btn_load_file.addActionListener(new LoadFileButtonHandler());
		panel_header.add(btn_load_file);
		
		panel_header.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createEmptyBorder(2,2,2,2),
						BorderFactory.createEtchedBorder()));
		
		// Create File Path Text Field
		text_file_path = new JTextField();
		text_file_path.setPreferredSize(new Dimension(300, 22));
		text_file_path.setEditable(false);
		panel_header.add(text_file_path);
		
		// Create Parse Button
		btn_parse = new JButton("Parse");
		btn_parse.addActionListener(new ParseFileButtonHandler());
		panel_header.add(btn_parse);
		
		// Create File Chooser
		file_chooser = new JFileChooser(".");
		// Show only files with .ucd extension
		file_chooser.addChoosableFileFilter(new FileNameExtensionFilter("UML Diagrams", "ucd"));
		
		
		
		getContentPane().add(panel_header, BorderLayout.NORTH);
		
		// Create classes panel
		panel_classes = new JPanel();
		panel_classes.setBorder(BorderFactory.createTitledBorder("Classes"));
		panel_classes.setPreferredSize(new Dimension(230,0));
		
		// Create Classes List
		list_classes = new JList();
		list_classes.addListSelectionListener(new ClassSelectionHandler());
		list_classes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// Set the Classes' List to fill the panel and scroll if needed
		panel_classes.setLayout(new BorderLayout());
		panel_classes.add(new JScrollPane(list_classes), BorderLayout.CENTER);
		
		// Add Class Panel to the Frame
		getContentPane().add(panel_classes, BorderLayout.WEST);
		
		// Create 1x2 panel for the class' components
		panel_components = new JPanel(new GridLayout(2,1));
		
		// Create 2x2 container for the attributes, methods, subclasses and associations/aggregations of a class
		panel_components_container = new JPanel(new GridLayout(2,2));
		
		// Create a panel for the raw details
		panel_raw_details = new JPanel();
		panel_raw_details.setSize(new Dimension(100, 100));
		panel_raw_details.setBorder(BorderFactory.createTitledBorder("Details"));
		// Create Text Area
		textarea_details = new JTextArea();
		textarea_details.setEditable(false);
		// Set Text Area to fill the panel and adding scroll.
		panel_raw_details.setLayout(new BorderLayout());
		panel_raw_details.add(new JScrollPane(textarea_details), BorderLayout.CENTER);
		
		// Adding square panels to the 2x2 layout
		list_attributes = new JList();
		list_methods = new JList();
		list_subclasses = new JList();
		list_assoc_aggre = new JList();
		panel_components_container.add(new SquarePanel("Attributes", list_attributes));
		panel_components_container.add(new SquarePanel("Methods", list_methods));
		panel_components_container.add(new SquarePanel("Sub-Classes", list_subclasses));
		panel_components_container.add(new SquarePanel("Associations/aggregations", list_assoc_aggre));
		
		// Adding Handlers to these new lists
		list_attributes.addListSelectionListener(new ComponentsSelectionHandler());
		list_methods.addListSelectionListener(new ComponentsSelectionHandler());
		list_subclasses.addListSelectionListener(new ComponentsSelectionHandler());
		list_assoc_aggre.addListSelectionListener(new AssocAggreSelectionHandler());
		
		
		
		
		// Adding panels to the main container
		panel_components.add(panel_components_container);
		panel_components.add(panel_raw_details);
		
		// Adding the main components container to the JFrame
		getContentPane().add(panel_components);
		
	}
	
	/**
	 * JPanel for the attributes, methods, sub-classes, associations/aggregations
	 * @author Truong Pham
	 *
	 */
	class SquarePanel extends JPanel {
		public SquarePanel(String title, JList list) {
			// Adding a title
			setBorder(BorderFactory.createTitledBorder(title));
			
			// Single select mode only.
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			// Set the list to fill the panel area and scroll if needed
			setLayout(new BorderLayout());
			add(new JScrollPane(list), BorderLayout.CENTER);
		}
	}
	
	/**
	 * Handler for the "Load File" button
	 * @author Truong Pham
	 */
	class LoadFileButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int return_value = file_chooser.showOpenDialog(null);
			
			if(return_value == file_chooser.APPROVE_OPTION) {
				file = file_chooser.getSelectedFile();
				text_file_path.setText(file.getAbsolutePath());
			}
		}
	}
	
	/**
	 * Event Handler for the parse button
	 * @author Truong Pham
	 *
	 */
	class ParseFileButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String file_path = text_file_path.getText();
			
			// Load the file and tokenize it
			file = new File(file_path);
			lexer = new Lexer(file);
			try {
				// Parse the file and load the root model
				parser = new Parser(lexer.get_tokens());
				model = parser.get_model();
				
				// Update the classes JList with all the classes
				list_classes.setListData(model.get_classes().toArray());
			} 
			catch (InvalidUMLException ex) {
				JOptionPane.showMessageDialog(Piroser.this, ex.getMessage(), "Parsing error", JOptionPane.ERROR_MESSAGE );
			}
			
		}
	}
	
	/**
	 * Selection handler on the Classes JList
	 * @author Truong Pham
	 *
	 */
	class ClassSelectionHandler implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent e) {
			// Select the index of the selected class
			int index = list_classes.getSelectedIndex();
			// Clear the details
			textarea_details.setText("");
			
			// Get all the classes
			Classe selected_classe = model.get_classes().get(index);
			ArrayList<String> subclasses = fetch_subclasses(selected_classe);
			DefaultListModel assoc_aggre_list_model = fetch_assoc_aggre(selected_classe);
			
			// Update the attributes list with the selected class
			list_attributes.setListData(selected_classe.get_attributes().toArray());
			list_methods.setListData(selected_classe.get_operations().toArray());
			list_subclasses.setListData(subclasses.toArray());
			list_assoc_aggre.setModel(assoc_aggre_list_model);
			
			
		}
		
		/**
		 * This function loops through the list of generalizations and finds the one with it's name matching
		 * the classe's name and fetches the subclasses.
		 * @param c The class to look for subclass
		 * @return List of the subclasses
		 */
		public ArrayList<String> fetch_subclasses(Classe c) {
			ArrayList<String> subclasses = new ArrayList<String>();
			ArrayList<Generalization> generalizations = model.get_generalizations();
			String class_name = c.get_name();
			
			for(Generalization gen : generalizations) {
				if(gen.get_name().endsWith(class_name)) {
					return gen.get_subclasses();
				}
			}
			return subclasses;
		}
		
		/**
		 * This function loops through the list of associations
		 * and aggregations to find the class' and build's the model
		 * @param c The class to look for subclass
		 * @return List model of the associations and aggregations
		 */
		public DefaultListModel fetch_assoc_aggre(Classe c) {
			ArrayList<Aggregation> aggregations = model.get_aggregations();
			ArrayList<Association> associations = model.get_associations();
			String class_name = c.get_name();
			DefaultListModel list_model = new DefaultListModel();
			
			// Loop through the list of aggregation to find a container matching
			// the class' name
			for(Aggregation agg : aggregations) {
				
				// If found construct a string with the parts
				if(agg.get_container().get_name().equals(class_name)) {
					for(Role role : agg.get_parts()) {
						list_model.addElement("(A) [P] "+role.get_name());
					}
				}
				
				// Loops through all the parts of the aggregations to 
				// check if the class is a part.
				for(Role role : agg.get_parts()) {
					if(role.get_name().equals(class_name)) {
						list_model.addElement("(A) [C] "+agg.get_container().get_name());
					}
				}
			}
			
			// Loop through the associations to find the class
			for(Association ass : associations) {
				// First role, relation is normal
				if(ass.get_first_role().get_name().equals(class_name)) {
					list_model.addElement("(R) [N] "+ass.get_name());
				}
				
				// Second role, relation is inversed
				if(ass.get_second_role().get_name().equals(class_name)) {
					list_model.addElement("(R) [I] "+ass.get_name());
				}
			}
			
			return list_model;
		}
		
	}
	
	class ComponentsSelectionHandler implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent e) {
			clear_selection_except((JList) e.getSource());
			textarea_details.setText("");
		}	
	}
	
	class AssocAggreSelectionHandler implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent e) {
			JList list = (JList) e.getSource();
			ListModel list_model = list.getModel();
			String selected_string, property_type, details;
			clear_selection_except((JList) e.getSource());
			
			// Do nothing if nothing is selected
			if(list.getSelectedIndex() == -1) return;
			
			// Get the selected string.
			// I cast String because I know the list_model contains only strings.
			selected_string = (String) list_model.getElementAt(list.getSelectedIndex());
			
			// Since the template is always "("A|R") ["P|C|N|I"]" IDENTIFIER
			// I can pull out the first "parameter" and pass the string to the proper
			// function to build the detail string
			property_type = selected_string.substring(1,2);
			if(property_type.equals("A")) {
				details = build_aggregation_details(selected_string);
			}
			else {
				details = build_association_details(selected_string);
			}
			
			textarea_details.setText(details);
		}		
		
		/**
		 * This function detects if the selected aggregation is a
		 * container or a part and gives control to the proper 
		 * function to build the details.
		 * @param selected_string
		 * @return The aggregation details
		 */
		private String build_aggregation_details(String selected_string) {
			String type, identifier;
			
			// Select whether it is a container or a part
			type = selected_string.substring(5,6);
			
			// Get the identifier
			identifier = selected_string.substring(8);
			
			// If the type is C then it's a container else it's a part
			if(type.equals("C")) return build_as_container(identifier);
			else return build_as_part(identifier);
			
		}
		
		/**
		 * This function builds the details of an aggregation
		 * where the selected identifier is a container.
		 * This assume the identifier is valid.
		 * @param identifier
		 * @return The aggregation details
		 */
		private String build_as_container(String identifier) {
			ArrayList<Aggregation> aggregations = model.get_aggregations();
			StringBuilder details = new StringBuilder();
			Aggregation aggregation = null;
			
			// Find the proper aggregation
			for(Aggregation agg : aggregations) {
				if(agg.get_container().get_name().equals(identifier)) {
					aggregation = agg;
					break;
				}
			}
			
			details.append("AGGREGATION\n");
			details.append("CONTAINER\n");
			details.append("   CLASS "+aggregation.get_container().get_name()+" "+aggregation.get_container().get_multiplicity()+"\n");
			details.append("PARTS\n");
			
			for(Role role : aggregation.get_parts()) {
				details.append("   CLASS "+role.get_name()+" "+role.get_multiplicity()+"\n");
			}
			
			return details.toString();
		}
		
		/**
		 * This function builds the details of an aggregation where the
		 * selected identifier is a part
		 * @param identifier
		 * @return
		 */
		private String build_as_part(String identifier) {
			ArrayList<Aggregation> aggregations = model.get_aggregations();
			StringBuilder details = new StringBuilder();
			Aggregation aggregation = null;
			Role role = null;
			
			// Loop through the list of aggregation to loop through the parts
			for(Aggregation agg : aggregations) {
				
				// Loops through all the parts of the aggregations to find the identifier
				for(Role r : agg.get_parts()) {
					if(r.get_name().equals(identifier)) {
						role = r;
						break;
					}
				}
				
				// If the role is not null it means we found the identifier 
				// so this aggregation must be the one containing it.
				if(role != null) {
					aggregation = agg;
					break;
				}
			}
			
			details.append("AGGREGATION\n");
			details.append("CONTAINER\n");
			details.append("   CLASS "+aggregation.get_container().get_name()+" "+aggregation.get_container().get_multiplicity()+"\n");
			details.append("PARTS\n");
			
			for(Role r : aggregation.get_parts()) {
				details.append("   CLASS "+r.get_name()+" "+r.get_multiplicity()+"\n");
			}
			
			return details.toString();
		}
		
		/**
		 * This function finds the name of the relation and display its details;
		 * @param selected_string The selected string
		 * @return The association details
		 */
		private String build_association_details(String selected_string) {
			String identifier;
			ArrayList<Association> associations = model.get_associations();
			Association association = null;
			StringBuilder details = new StringBuilder();
			
			// Get the identifier
			identifier = selected_string.substring(8);
			
			// Find the association name 
			for(Association ass : associations) {
				if(ass.get_name().equals(identifier)) {
					association = ass;
					break;
				}
				
				
			}
			
			// Build the string
			details.append("RELATION ");
			details.append(association.get_name());
			details.append("\n");
			details.append("   ROLES\n");
			details.append("   CLASS "+association.get_first_role().get_name()+" ");
			details.append(association.get_first_role().get_multiplicity()+"\n");
			details.append("   CLASS "+association.get_second_role().get_name()+" ");
			details.append(association.get_second_role().get_multiplicity()+"\n");
			
			return details.toString();
		}
	}
	
	/**
	 * Method to clear the selection of the class properties lists except the one passed as parameter.
	 * @param exception_list The exception
	 */
	private void clear_selection_except(JList exception_list) {
		if (! list_attributes.equals(exception_list)) list_attributes.clearSelection();
		if (! list_methods.equals(exception_list)) list_methods.clearSelection();
		if (! list_assoc_aggre.equals(exception_list)) list_assoc_aggre.clearSelection();
		if (! list_subclasses.equals(exception_list)) list_subclasses.clearSelection();
	}
	
	/**
	 * Main executable
	 * @param args Program args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		 SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                Piroser GUI = new Piroser();
	                GUI.setVisible(true);
	            }
	        });
	}
	

}



