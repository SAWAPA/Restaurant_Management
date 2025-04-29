package GUI.attribute;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public class TextField extends JTextField implements ActionListener{

    public TextField(int fontSize, int x, int y, int w, int h){
        this.setBounds(x,y,w,h);
        this.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
        this.addActionListener(this);
        setFontTextField();
    }


    private void setFontTextField(){
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }
}
