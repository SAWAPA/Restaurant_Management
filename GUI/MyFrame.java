package GUI;

import javax.swing.*;

// คลาส MyFrame ดูแลเรื่องหน้าต่างและ layout
public class MyFrame extends JFrame {
    public MyFrame() {
        this.setTitle("BP's Restaurant");
        this.setSize(1920, 1080);
        this.setLayout(null); // ปิด layout manager
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
