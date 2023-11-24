import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class TextEditor extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	JTextArea textArea;
	JScrollPane scrollPane;
	
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
		
		this.add(scrollPane);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
