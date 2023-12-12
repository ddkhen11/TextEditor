import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FindReplaceDialog extends JDialog implements ActionListener {
	
    private static final long serialVersionUID = 1L;
    
	private final JTextArea textArea;
    private final JTextField findField;
    private final JTextField replaceField;
    private final JButton findButton;
    private final JButton replaceButton;

    private int lastIndex = -1;

    public FindReplaceDialog(JFrame owner, JTextArea textArea) {
        super(owner, "Find and Replace", false);
        this.textArea = textArea;

        findField = new JTextField(10);
        replaceField = new JTextField(10);
        findButton = new JButton("Find");
        replaceButton = new JButton("Replace");

        findButton.addActionListener(this);
        replaceButton.addActionListener(this);

        setLayout(new FlowLayout());
        add(new JLabel("Find:"));
        add(findField);
        add(new JLabel("Replace:"));
        add(replaceField);
        add(findButton);
        add(replaceButton);

        pack();
        setLocationRelativeTo(owner);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == findButton) {
            String searchText = findField.getText();
            String text = textArea.getText();
            lastIndex = text.indexOf(searchText, lastIndex + 1);
            if (lastIndex != -1) {
                textArea.setSelectionStart(lastIndex);
                textArea.setSelectionEnd(lastIndex + searchText.length());
            } 
            else {
                JOptionPane.showMessageDialog(this, "Text not found");
                lastIndex = -1;
            }
        } 
        else if (e.getSource() == replaceButton) {
            String searchText = findField.getText();
            String replaceText = replaceField.getText();
            if (lastIndex != -1 && lastIndex < textArea.getText().length()) {
                textArea.replaceRange(replaceText, lastIndex, lastIndex + searchText.length());
                lastIndex = -1;
            }
        }
    }
}