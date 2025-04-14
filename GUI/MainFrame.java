package GUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.*;

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
        TextField textField1 = new TextField();
        textField1.setBounds(760, 300, 300, 40);
        TextField textField2 = new TextField();
        textField2.setBounds(760, 400, 300, 40);
        this.add(textField1);
        this.add(textField2);

        setButton(textField1, textField2);
    }

    private void setLabel(){
        Label label1 = new Label("Welcome to BP's Restaurant", 630, 40, 1000, 50);  // สร้าง label
        Label label2 = new Label("Login", 850, 220, 1000, 50);
        this.add(label1);
        this.add(label2);
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