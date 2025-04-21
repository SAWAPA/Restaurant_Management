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

public class MainFrame extends JFrame {
    private static String textFromField1;
    private static String textFromField2; 
    private Label label3;

    public MainFrame() {
        this.setTitle("BP's Restaurant");
        this.setSize(1920, 1080);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        label3 = new Label("Error", 14, 850, 550, 500, 40);
        label3.setForeground(Color.RED);
        label3.setVisible(false);
        this.add(label3);

        setLabel();
        setTextField();

        this.setVisible(true);
    }

    private void setTextField(){
        TextField textField1 = new TextField(760, 300, 300, 40);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 26));
        passwordField.setBounds(760, 400, 300, 40);

        this.add(textField1);
        this.add(passwordField);

        setButton(textField1, passwordField);
    }

    private void setLabel(){
        Label label1 = new Label("Welcome to BP's Restaurant", 40, 630, 40, 1000, 50);
        Label label2 = new Label("Login", 40,850, 220, 1000, 50);
        Label labal4 = new Label("Username", 20, 630, 295, 1000, 50);
        Label label5 = new Label("Password", 20, 630, 395, 1000, 50);

        Label linkToRegisterPage = new Label("register", 18, 900, 510, 100, 30);
        linkToRegisterPage.setForeground(Color.BLUE);
        linkToRegisterPage.setCursor(new Cursor(Cursor.HAND_CURSOR));

        linkToRegisterPage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RegisterFrame();
                dispose();
            }
        });

        this.add(label1);
        this.add(label2);
        this.add(linkToRegisterPage);
        this.add(labal4);
        this.add(label5);
    }

    private void setButton(TextField user, JPasswordField pass){
        Button buttonLogin = new Button("Login", 1000, 510, 100, 30);
        this.add(buttonLogin);

        buttonLogin.addActionListener(e -> {
            textFromField1 = user.getText();
            textFromField2 = new String(pass.getPassword());

            System.out.println("Text from TextField1: " + textFromField1);
            System.out.println("Text from PasswordField: " + textFromField2);

            login(textFromField1, textFromField2);
        });
    }

    private void login(String username, String pass){
        SqlConnect connect = new SqlConnect();
        Register user = new Register(username, pass);

        String errorText = "";
        String loginToSql = "SELECT * FROM restaurant.users WHERE username = ? AND password = ?";

        boolean hasError = false;

        try(Connection connection = DriverManager.getConnection(connect.getUrlD(), connect.getUserSqlD(), connect.getPassSqlD());
            PreparedStatement ps = connection.prepareStatement(loginToSql)){
            ps.setString(1, user.getUsername());
            ps.setString(2,user.getPassword());

            var rowsInserted = ps.executeQuery();

            if (hasError) {
                label3.setText(errorText);
                label3.setVisible(true);
                label3.repaint();
                this.repaint();
                return;
            }

            if (rowsInserted.next()) {
                System.out.println("-------------------------------");
                System.out.println("Login successfully! Welcome, " + user.getUsername());

                new MenuFrame();
                dispose();
            }else{
                System.out.println("Password or Username is incorected.");
                errorText = "Password or Username is incorected.";
                hasError = true;
            }

            

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}