package GUI.attribute;

import java.awt.Font;
import javax.swing.JTextField;

public class TextField extends JTextField{

    public TextField(int fontSize, int x, int y, int w, int h){
        this.setBounds(x,y,w,h);
        this.setFont(new Font("Tahoma", Font.PLAIN, fontSize));
    }
}
