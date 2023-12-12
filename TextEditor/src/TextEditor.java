import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
	JMenuItem findAndReplace;
	JMenu formatMenu;
	JMenuItem boldItem;
	JMenuItem italicsItem;
	
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
		fontSizeSpinner.setValue(14);
		fontSizeSpinner.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				Font oldFont = textArea.getFont();
				Font newFont = new Font(textArea.getFont().getFamily(), textArea.getFont().getStyle(), (int) fontSizeSpinner.getValue());
			
				undoManager.addEdit(new FontStyleEdit(TextEditor.this, oldFont, newFont));;
				textArea.setFont(newFont);
				
				if (undoManager != null) {
					undo.setEnabled(undoManager.canUndo());
					redo.setEnabled(undoManager.canRedo());
				}
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
		
		// File bar
		
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
		
		// Edit bar
		
		editMenu = new JMenu("Edit");
		undo = new JMenuItem("Undo");
		redo = new JMenuItem("Redo");
		findAndReplace = new JMenuItem("Find and Replace");
		
		undo.addActionListener(this);
		redo.addActionListener(this);
		findAndReplace.addActionListener(this);
		
		editMenu.add(undo);
		editMenu.add(redo);
		editMenu.add(findAndReplace);
		menuBar.add(editMenu);
		
		undo.setEnabled(false);
	    redo.setEnabled(false);
	    
	    // Format bar
	    
	    formatMenu = new JMenu("Format");
	    boldItem = new JMenuItem("Bold");
		italicsItem = new JMenuItem("Italic");
		
		boldItem.addActionListener(this);
		italicsItem.addActionListener(this);
		
		
		formatMenu.add(boldItem);
		formatMenu.add(italicsItem);;
		menuBar.add(formatMenu);
		
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
			Color oldColor = textArea.getForeground();
			Color newColor = JColorChooser.showDialog(null, "Choose a color", Color.black);
			
			if (newColor != null && !oldColor.equals(newColor)) {
				undoManager.addEdit(new TextColorEdit(textArea, textColorButton, oldColor, newColor));
	            textArea.setForeground(newColor);
	            textColorButton.setForeground(newColor);
	        }
		}
		
		if (e.getSource() == fontBox) {
			Font oldFont = textArea.getFont();
		    Font newFont = new Font((String) fontBox.getSelectedItem(), oldFont.getStyle(), oldFont.getSize());
		    
		    if (!oldFont.equals(newFont)) {
		    	undoManager.addEdit(new FontStyleEdit(this, oldFont, newFont));
		    	textArea.setFont(newFont);
		    }
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
		
		if (e.getSource() == findAndReplace) {
			FindReplaceDialog findReplaceDialog = new FindReplaceDialog(this, textArea);
			findReplaceDialog.setVisible(true);
			
		}
		
		if (e.getSource() == boldItem) {
			Font currentFont = textArea.getFont();
		    int currentStyle = currentFont.getStyle();
		    boolean isBold = (currentStyle & Font.BOLD) == Font.BOLD;

		    if (isBold) {
		        currentStyle &= ~Font.BOLD;
		    } 
		    else {
		        currentStyle |= Font.BOLD;
		    }

		    Font newFont = new Font(currentFont.getFamily(), currentStyle, currentFont.getSize());

		    undoManager.addEdit(new FontStyleEdit(this, currentFont, newFont));
		    textArea.setFont(newFont);	
		}
		
		if (e.getSource() == italicsItem) {
		    Font currentFont = textArea.getFont();
		    int currentStyle = currentFont.getStyle();
		    boolean isItalic = (currentStyle & Font.ITALIC) == Font.ITALIC;

		    if (isItalic) {
		        currentStyle &= ~Font.ITALIC;
		    } 
		    else {
		        currentStyle |= Font.ITALIC;
		    }

		    Font newFont = new Font(currentFont.getFamily(), currentStyle, currentFont.getSize());

		    undoManager.addEdit(new FontStyleEdit(this, currentFont, newFont));
		    textArea.setFont(newFont);  
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
