package GUI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JPasswordField;

import GUI.attribute.Button;
import GUI.attribute.Label;
import GUI.attribute.TextField;

import java.awt.event.MouseEvent;

public class RegisterFrame extends JFrame{
    private static String username;
    private static String password;
    private static String confirmPass;
    private static String role;
    private Label label7;

    RegisterFrame(){
        this.setTitle("Register");
        this.setSize(1920, 1080);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.repaint();

        label7 = new Label("", 14, 800, 600, 500, 40);
        label7.setForeground(Color.RED);
        label7.setVisible(false);
        this.add(label7);

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

        label6.setForeground(Color.RED);
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

        JPasswordField textField2 = new JPasswordField();
        textField2.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 26));
        textField2.setBounds(740, 350, 250, 40);

        JPasswordField textField3 = new JPasswordField();
        textField3.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 26));
        textField3.setBounds(740, 410, 250, 40);

        TextField textField4 = new TextField(740, 470, 250, 40);

        this.add(textField1);
        this.add(textField2);
        this.add(textField3);
        this.add(textField4);

        setButton(textField1, textField2, textField3, textField4);
    }

    private void setButton(TextField user, JPasswordField pass, JPasswordField conPass, TextField roles){
        Button buttonSignIn = new Button("Sign In", 1000, 550, 100, 40);

        this.add(buttonSignIn);

        buttonSignIn.addActionListener(e -> {
            username = user.getText();
            password = new String(pass.getPassword());
            confirmPass = new String(conPass.getPassword());
            role = roles.getText();

            System.out.println("Username: " + username + "\nPassword: " + password + "\nConfirm Password: " + confirmPass
                                + "\nRole: " + role);
            register(username, password, confirmPass, role);
        });
    }

    private void register(String username, String pass, String conPass, String role) {
        SqlConnect connect = new SqlConnect();
        Register user1 = new Register(username, pass, conPass, role);

        String errorText = "";
        String insertToSql = "INSERT INTO restaurant.users (username, password, role) VALUES (?, ?, ?)";
        String checkUsername = "SELECT * FROM restaurant.users WHERE username = ?";
        boolean hasError = false;
    
        try (Connection connection = DriverManager.getConnection(connect.getUrlD(), connect.getUserSqlD(), connect.getPassSqlD())) {
            
            // Check Username
            try (PreparedStatement checkStmt = connection.prepareStatement(checkUsername)) {
                checkStmt.setString(1, user1.getUsername());
                ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next()) {
                    System.out.println("Username already exists, please try again.");
                    errorText = "Username already exists, please try again.";
                    hasError = true;
                }

                if (hasError) {
                    label7.setText(errorText);
                    label7.setVisible(true);
                    label7.repaint();
                    this.repaint();
                    return;
                }
            }
    
            // Check Password
            if (pass.length() < 8) {
                errorText = "Password must be at least 8 characters.";
                hasError = true;
            }
            else if (!pass.matches(".*[A-Z].*")) {
                errorText = "Password must contain at least one uppercase letter.";
                hasError = true;
            }
            else if (!pass.matches(".*\\d.*")) {
                errorText = "Password must contain at least one digit.";
                hasError = true;
            }
            else if (!pass.matches(".*[!@#$%^&*()_+\\-={}\\[\\]:\";'<>?,./].*")) {
                errorText = "Password must contain at least one special character.";
                hasError = true;
            }
            else if (!pass.equals(conPass)) {
                errorText = "Password and Confirm Password do not match.";
                hasError = true;
            }
            else if (role == null || role.trim().isEmpty()) {
                errorText = "Please put your role.";
                hasError = true;
            }

            if (hasError) {
                label7.setText(errorText);
                label7.setVisible(true);
                label7.repaint();
                this.repaint();
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
