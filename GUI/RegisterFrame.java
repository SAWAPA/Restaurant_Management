package GUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;

import GUI.attribute.Button;
import GUI.attribute.Label;
import GUI.attribute.TextField;

import java.awt.event.MouseEvent;

public class RegisterFrame extends JFrame{
    private static String username;
    private static String password;
    private static String confirmPass;
    private static String role;

    RegisterFrame(){
        this.setTitle("Register");
        this.setSize(1920, 1080);
        this.setLayout(null); // ปิด layout manager
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        setLabel();
        setTextField();
    }

    private void setLabel(){
        Label label1 = new Label("Register", 40, 500, 170, 200, 100);
        Label label2 = new Label("Username : ",18 , 520, 290, 250, 40);
        Label label3 = new Label("Password : ", 18 , 520, 350, 250, 40);
        Label label4 = new Label("Confirm Password : ", 18 , 520, 410, 250, 40);
        Label label5 = new Label("Role : ", 18 , 520, 470, 250, 40);

        Label label6 = new Label("Cancel", 18, 900, 550, 100, 40);
        label6.setForeground(Color.BLUE);
        label6.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label6.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new MainFrame();
                dispose();
            }
        });

        this.add(label1);
        this.add(label2);
        this.add(label3);
        this.add(label4);
        this.add(label5);
        this.add(label6);
    }

    private void setTextField(){
        TextField textField1 = new TextField(740, 290, 250, 40);
        TextField textField2 = new TextField(740, 350, 250, 40);
        TextField textField3 = new TextField(740, 410, 250, 40);
        TextField textField4 = new TextField(740, 470, 250, 40);

        this.add(textField1);
        this.add(textField2);
        this.add(textField3);
        this.add(textField4);

        setButton(textField1, textField2, textField3, textField4);
    }

    private void setButton(TextField user, TextField pass, TextField conPass, TextField roles){
        Button button1 = new Button("Sign In", 1000, 550, 100, 40);

        this.add(button1);

        button1.addActionListener(e -> {
            username = user.getText();
            password = pass.getText();
            confirmPass = conPass.getText();
            role = roles.getText();

            System.out.println("Username: " + username + "\nPassword: " + password + "\nConfirm Password: " + confirmPass
                                + "\nRole: " + role);
            register(username, password, confirmPass, role);
        });
    }

    private void register(String username, String pass, String conPass, String role) {
        SqlConnect connect = new SqlConnect();
        Register user1 = new Register(username, pass, conPass, role);
    
        String insertToSql = "INSERT INTO restaurant.users (username, password, role) VALUES (?, ?, ?)";
        String checkUsername = "SELECT * FROM restaurant.users WHERE username = ?";
    
        try (Connection connection = DriverManager.getConnection(connect.getUrlD(), connect.getUserSqlD(), connect.getPassSqlD())) {
            
            // Check Username
            try (PreparedStatement checkStmt = connection.prepareStatement(checkUsername)) {
                checkStmt.setString(1, user1.getUsername());
                ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next()) {
                    System.out.println("Username already exists, please try again.");
                    return; // if have a same username return
                }
            }
    
            // Check Password
            if (pass.length() < 8) {
                System.out.println("Password must be at least 8 characters.");
                return;
            }
            if (!pass.matches(".*[A-Z].*")) {
                System.out.println("Password must contain at least one uppercase letter.");
                return;
            }
            if (!pass.matches(".*\\d.*")) {
                System.out.println("Password must contain at least one digit.");
                return;
            }
            if (!pass.matches(".*[!@#$%^&*()_+\\-={}\\[\\]:\";'<>?,./].*")) {
                System.out.println("Password must contain at least one special character.");
                return;
            }
            if (!pass.equals(conPass)) {
                System.out.println("Password and Confirm Password do not match.");
                return;
            }

            if (role == null || role.trim().isEmpty()) {
                System.out.println("Please put your role.");
                return;
            }
            
    
            // Insert to Sql
            try (PreparedStatement insertStmt = connection.prepareStatement(insertToSql)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, pass);
                insertStmt.setString(3, role);
    
                int rowsInserted = insertStmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Registered successfully!");
                } else {
                    System.out.println("Registration failed.");
                }
            }

            new MainFrame();
            dispose();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
