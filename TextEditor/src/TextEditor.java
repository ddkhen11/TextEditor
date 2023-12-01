import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;

public class TextEditor extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	JTextArea textArea;
	JScrollPane scrollPane;
	JLabel fontLabel;
	JSpinner fontSizeSpinner;
	JButton textColorButton;
	JComboBox fontBox;
	UndoManager undoManager;
	
	JMenuBar menuBar;
	JMenu fileMenu;
	JMenuItem openItem;
	JMenuItem saveItem;
	JMenuItem exitItem;
	JMenu editMenu;
	JMenuItem undo;
	JMenuItem redo;
	
	TextEditor() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Text Editor");
		this.setSize(500, 500);
		this.setLayout(new FlowLayout());
		this.setLocationRelativeTo(null);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setFont(new Font("Arial", Font.PLAIN, 14));
		
		scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(450, 450));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		fontLabel = new JLabel("Font: ");
		
		fontSizeSpinner = new JSpinner();
		fontSizeSpinner.setPreferredSize(new Dimension(50, 25));
		fontSizeSpinner.setValue(20);
		fontSizeSpinner.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				textArea.setFont(new Font(textArea.getFont().getFamily(), Font.PLAIN, (int) fontSizeSpinner.getValue()));
				
			}
			
		});
		
		textColorButton = new JButton("Color");
		textColorButton.addActionListener(this);
		
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		fontBox = new JComboBox(fonts);
		fontBox.addActionListener(this);
		fontBox.setSelectedItem("Arial");
		
		undoManager = new UndoManager();
		textArea.getDocument().addUndoableEditListener(new UndoableEditListener() {

			@Override
			public void undoableEditHappened(UndoableEditEvent e) {
				undoManager.addEdit(e.getEdit());
				undo.setEnabled(undoManager.canUndo());
				redo.setEnabled(undoManager.canRedo());
			}
		});
		
		// menubar
		
		menuBar = new JMenuBar();
		
		fileMenu = new JMenu("File");
		openItem = new JMenuItem("Open");
		saveItem = new JMenuItem("Save");
		exitItem = new JMenuItem("Exit");
		
		openItem.addActionListener(this);
		saveItem.addActionListener(this);
		exitItem.addActionListener(this);
		
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.add(exitItem);
		menuBar.add(fileMenu);
		
		editMenu = new JMenu("Edit");
		undo = new JMenuItem("Undo");
		redo = new JMenuItem("Redo");
		
		undo.addActionListener(this);
		redo.addActionListener(this);
		
		editMenu.add(undo);
		editMenu.add(redo);
		menuBar.add(editMenu);
		
		undo.setEnabled(false);
	    redo.setEnabled(false);
		
		// end menubar
		
		this.setJMenuBar(menuBar);
		this.add(fontLabel);
		this.add(fontSizeSpinner);
		this.add(textColorButton);
		this.add(fontBox);
		this.add(scrollPane);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == textColorButton) {
			Color color = JColorChooser.showDialog(null, "Choose a color", Color.black);
			if (color != null) {
	            textArea.setForeground(color);
	            textColorButton.setForeground(color);
	        }
		}
		
		if (e.getSource() == fontBox) {
			textArea.setFont(new Font((String) fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize()));
		}
		
		if (e.getSource() == openItem) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + File.separator + "Downloads"));
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
			fileChooser.setFileFilter(filter);
			
			int response = fileChooser.showOpenDialog(null);
			
			if (response == JFileChooser.APPROVE_OPTION) {
				File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
				Scanner fileIn = null;
				
				try {
					fileIn = new Scanner(file);
					if (file.isFile()) {
						while (fileIn.hasNextLine()) {
							String line = fileIn.nextLine() + "\n";
							textArea.append(line);
						}
					}
				} 
				catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
				finally {
					if (fileIn != null) {
						fileIn.close();
					}
				}
			}
		}
		
		if (e.getSource() == saveItem) {
			save();
		}
		
		if (e.getSource() == exitItem) {
			int option = JOptionPane.showConfirmDialog(this,"Save before exiting?");  
			if (option == JOptionPane.CANCEL_OPTION) {
				return;
			}
			else if (option == JOptionPane.YES_OPTION) {
				save();
			}
			System.exit(0);
		}
		
		if (e.getSource() == undo) {
			if (undoManager.canUndo()) {
				undoManager.undo();
			}
		}
		
		if (e.getSource() == redo) {
			if (undoManager.canRedo()) {
				undoManager.redo();
			}
		}
		
		if (undoManager != null) {
			undo.setEnabled(undoManager.canUndo());
			redo.setEnabled(undoManager.canRedo());
		}
	}
	
	public void save() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + File.separator + "Downloads"));
		
		int response = fileChooser.showSaveDialog(null);
		
		if (response == JFileChooser.APPROVE_OPTION) {
			File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
			PrintWriter fileOut = null;
			
			try {
				fileOut = new PrintWriter(file);
				fileOut.println(textArea.getText());
			} 
			catch (FileNotFoundException e1){
				e1.printStackTrace();
			}
			finally {
				fileOut.close();
			}
		}
	}

}
