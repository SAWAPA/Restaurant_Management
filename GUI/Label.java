package GUI;

import java.awt.Font;
import javax.swing.JLabel;

public class Label extends JLabel{

    public Label(String text, int x, int y, int w, int h){
        this.setText(text);
        this.setBounds(x,y,w,h);
        setFontLabel();
    }

    private void setFontLabel(){
        setFont(new Font("Tahoma", Font.BOLD, 40));
    }
}
