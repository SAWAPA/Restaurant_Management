package GUI.attribute;

import java.awt.Font;
import javax.swing.JLabel;

public class Label extends JLabel{

    public Label(String text,int fontSize, int x, int y, int w, int h){
        this.setText(text);
        this.setBounds(x,y,w,h);
        this.setFont(new Font("Tahoma", Font.BOLD, fontSize));
        
    }

    public void setColor(){
        this.setColor();
    }
}
