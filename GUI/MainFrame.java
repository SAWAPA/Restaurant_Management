package GUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.*;

import GUI.attribute.Button;
import GUI.attribute.Label;
import GUI.attribute.TextField;

import java.awt.event.MouseEvent;

// คลาส MyFrame ดูแลเรื่องหน้าต่างและ layout
public class MainFrame extends JFrame {
    private static String textFromField1;
    private static String textFromField2; 

    public MainFrame() {
        this.setTitle("BP's Restaurant");
        this.setSize(1920, 1080);
        this.setLayout(null); // ปิด layout manager
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        setLabel();
        setTextField();
    }

    private void setTextField(){
        TextField textField1 = new TextField(760, 300, 300, 40);
        TextField textField2 = new TextField(760, 400, 300, 40);
        this.add(textField1);
        this.add(textField2);

        setButton(textField1, textField2);
    }

    private void setLabel(){
        Label label1 = new Label("Welcome to BP's Restaurant", 40, 630, 40, 1000, 50);
        Label label2 = new Label("Login", 40,850, 220, 1000, 50);

        Label linkToRegisterPage = new Label("register", 18, 900, 510, 100, 30);
        linkToRegisterPage.setForeground(Color.BLUE); // ทำให้เหมือนลิงก์
        linkToRegisterPage.setCursor(new Cursor(Cursor.HAND_CURSOR)); // เปลี่ยนเมาส์เป็นรูปมือ

        // เพิ่ม MouseListener
        linkToRegisterPage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // เปิดหน้าใหม่
                new RegisterFrame(); // หรือชื่อหน้า (Frame) ที่ต้องการ
                dispose(); // ปิดหน้าปัจจุบันถ้าต้องการ
            }
        });

        this.add(label1);
        this.add(label2);
        this.add(linkToRegisterPage);
    }

    private void setButton(TextField user, TextField pass){
        Button buttonLogin = new Button("Login", 1000, 510, 100, 30);

        this.add(buttonLogin);

        buttonLogin.addActionListener(e -> {
            // ดึงข้อมูลจาก TextField เมื่อปุ่ม Login ถูกคลิก
            textFromField1  = user.getText();  // ดึงค่าจาก TextField
            textFromField2 = pass.getText();
            System.out.println("Text from TextField1: " + textFromField1 + "\nText from TextField2: " + textFromField2);  // แสดงผลข้อมูลจาก TextField
            login(textFromField1, textFromField2);
        });
    }

    public static void login(String username, String pass){
        SqlConnect connect = new SqlConnect();
        Register user = new Register(username, pass);
        
        String loginToSql = "SELECT * FROM restaurant.users WHERE username = ? AND password = ?";
        try(Connection connection = DriverManager.getConnection(connect.getUrlD(), connect.getUserSqlD(), connect.getPassSqlD());
            PreparedStatement ps = connection.prepareStatement(loginToSql)){
            ps.setString(1, user.getUsername());
            ps.setString(2,user.getPassword());

            var rowsInserted = ps.executeQuery();

            if (rowsInserted.next()) {
                System.out.println("-------------------------------");
                System.out.println("Login successfully! Welcome, " + user.getUsername());

                return; // out from loginPage
            }else{
                System.out.println("Password or Username is incorected.");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}