package GUI.attribute;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;

public class Label extends JLabel {
    public Label(String text, String fontStyle, int fontSize, int x, int y, int w, int h) {
        this.setText(text);
        this.setBounds(x, y, w, h);
        this.setFont(new Font("Tahoma", getFontStyle(fontStyle), fontSize));
    }

    private int getFontStyle(String fontStyle) {
        switch (fontStyle.toLowerCase()) {
            case "bold":
                return Font.BOLD;
            case "italic":
                return Font.ITALIC;
            case "bolditalic":
            case "bold_italic":
                return Font.BOLD | Font.ITALIC;
            case "plain":
                return Font.PLAIN;
            default:
                return Font.PLAIN;
        }
    }

    // Set foreground color
    public void setColor(Color color) {
        this.setForeground(color);
    }
}
