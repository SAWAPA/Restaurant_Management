package GUI.attribute;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class Button extends JButton implements ActionListener{
    public Button(String text, int x, int y, int width, int height) {
        this.setText(text);
        this.setBounds(x, y, width, height);
        this.setFocusable(false);
        this.setBackground(Color.WHITE);
        this.addActionListener(this);  // ให้ปุ่มฟัง event ของตัวเอง

        setFontButton();
    }

    private void setFontButton(){
        this.setFont(new Font("Tahoma", Font.PLAIN, 18));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this) {
            System.out.println("Button '" + this.getText() + "' was clicked.");
        }
    }
}
