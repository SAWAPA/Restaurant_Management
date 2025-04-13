package GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public class TextField extends JTextField implements ActionListener{

    TextField(){
        this.setPreferredSize(new Dimension(400,40));
        this.addActionListener(this);
        setFontTextField();
    }


    private void setFontTextField(){
        this.setFont(new Font("Tahoma", Font.PLAIN, 24));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this) {
            System.out.println("TextField Enter pressed. Text = " + this.getText());
        }
    }
}
