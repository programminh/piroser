package org.umontreal.ift3913.piroser;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

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
	
	
	public Piroser() {
		this.init();
	}
	
	private void init() {
		// Window Properties
		setTitle("Piroser 0.0.1");
		setSize(700,700);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// Header panel
		panel_header = new JPanel();
		
		// Create Load File Button
		btn_load_file = new JButton("Load file");
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
		panel_header.add(btn_parse);
		
		// Create File Chooser
		file_chooser = new JFileChooser(".");
		// Show only files with .ucd extension
		file_chooser.addChoosableFileFilter(new FileNameExtensionFilter("UML Diagrams", "ucd"));
		
		// Listen to button being pressed
		btn_load_file.addActionListener(new LoadFileButtonHandler());
		
		getContentPane().add(panel_header, BorderLayout.NORTH);
		
		// Create classes panel
		panel_classes = new JPanel();
		panel_classes.setBorder(BorderFactory.createTitledBorder("Classes"));
		panel_classes.setPreferredSize(new Dimension(230,0));
		
		// Create Classes List
		list_classes = new JList();
		
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
		panel_components_container.add(new SquarePanel("Attributes", list_attributes));
		panel_components_container.add(new SquarePanel("Methods", list_methods));
		panel_components_container.add(new SquarePanel("Sub-Classes", list_subclasses));
		panel_components_container.add(new SquarePanel("Associations/aggregations", list_assoc_aggre));
		
		// Adding panels to the main container
		panel_components.add(panel_components_container);
		panel_components.add(panel_raw_details);
		
		// Adding the main components container to the JFrame
		getContentPane().add(panel_components);
		
	}
	
	/**
	 * JPanel for the attributes, methods, sub-classes, associations/aggregations
	 * @author TruongMacBook
	 *
	 */
	class SquarePanel extends JPanel {
		public SquarePanel(String title, JList list) {
			// Adding a title
			setBorder(BorderFactory.createTitledBorder(title));
			
			// Create the list
			list = new JList();
			
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
	
	public static void main(String[] args) throws IOException {
		 SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                Piroser GUI = new Piroser();
	                GUI.setVisible(true);
	            }
	        });
	}
	

}



