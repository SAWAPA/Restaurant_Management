package GUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main{

    private static String textFromTextField;
    public static void main(String[] args) {
        MyFrame frame = new MyFrame();  // สร้างหน้าต่าง
        Label label = new Label("Welcome to BP's Restaurant", 630, 40, 1000, 50);  // สร้าง label
        Button buttonLogin = new Button("Login", 1200, 800, 150, 50);   // สร้างปุ่ม
        TextField textField = new TextField();
        textField.setBounds(600, 300, 250, 40);


        // เพิ่ม component ลงใน frame
        frame.add(label);
        frame.add(buttonLogin);
        frame.add(textField);

        buttonLogin.addActionListener(e -> {
            // ดึงข้อมูลจาก TextField เมื่อปุ่ม Login ถูกคลิก
            textFromTextField = textField.getText();  // ดึงค่าจาก TextField
            System.out.println("Text from TextField: " + textFromTextField);  // แสดงผลข้อมูลจาก TextField
            login(textFromTextField);
        });

        
    }

    public static void login(String text){
        SqlConnect connect = new SqlConnect();
        try{
            Connection connection = DriverManager.getConnection(connect.getUrlD(), connect.getUserSqlD(), connect.getPassSqlD());
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
}
