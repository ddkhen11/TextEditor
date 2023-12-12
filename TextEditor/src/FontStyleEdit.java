import javax.swing.undo.AbstractUndoableEdit;
import java.awt.Font;

public class FontStyleEdit extends AbstractUndoableEdit {
	
    private static final long serialVersionUID = 1L;
    
	private final TextEditor textEditor;
    private final Font oldFont;
    private final Font newFont;

    public FontStyleEdit(TextEditor textEditor, Font oldFont, Font newFont) {
        this.textEditor = textEditor;
        this.oldFont = oldFont;
        this.newFont = newFont;
    }

    @Override
    public void undo() {
        super.undo();
        textEditor.textArea.setFont(oldFont);
        
        if (!oldFont.getFamily().equals(newFont.getFamily())) {
        	textEditor.fontBox.setSelectedItem(oldFont.getFamily());
        }
        
        if (oldFont.getSize() != newFont.getSize()) {
        	textEditor.fontSizeSpinner.setValue(oldFont.getSize());
        }
    }

    @Override
    public void redo() {
        super.redo();
        textEditor.textArea.setFont(newFont);
        
        if (!oldFont.getFamily().equals(newFont.getFamily())) {
        	textEditor.fontBox.setSelectedItem(newFont.getFamily());
        }
        
        if (oldFont.getSize() != newFont.getSize()) {
        	textEditor.fontSizeSpinner.setValue(newFont.getSize());
        }
    }
}