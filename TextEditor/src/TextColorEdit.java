import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.Color;

public class TextColorEdit extends AbstractUndoableEdit {
	
    private static final long serialVersionUID = 1L;
    
	private final JTextArea textArea;
	private final JButton textColorButton;
    private final Color oldColor;
    private final Color newColor;

    public TextColorEdit(JTextArea textArea, JButton textColorButton, Color oldColor, Color newColor) {
        this.textArea = textArea;
        this.textColorButton = textColorButton;
        this.oldColor = oldColor;
        this.newColor = newColor;
    }

    @Override
    public void undo() {
        super.undo();
        textArea.setForeground(oldColor);
        textColorButton.setForeground(oldColor);
    }

    @Override
    public void redo() {
        super.redo();
        textArea.setForeground(newColor);
        textColorButton.setForeground(newColor);
    }
}
