package GUI.attribute;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public class TextField extends JTextField implements ActionListener{

    public TextField(int x, int y, int w, int h){
        this.setBounds(x,y,w,h);
        this.addActionListener(this);
        setFontTextField();
    }


    private void setFontTextField(){
        this.setFont(new Font("Tahoma", Font.PLAIN, 24));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }
}
