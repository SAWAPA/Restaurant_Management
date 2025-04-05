import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SqlConnect connect = new SqlConnect();
        Connection connection = null;
    
        try {
            connection = DriverManager.getConnection(connect.getUrlD(), connect.getUserSqlD(), connect.getPassSqlD());
            Scanner sc = new Scanner(System.in);
    
            while (true) {
                System.out.println("-------------------------------");
                System.out.println("*- WELCOME TO BP'RESTAURANT -*");
                System.out.println("-------------------------------");
                System.out.println("0 : EXIT PROGRAM\n" +
                                   "1 : LOGIN\n" +
                                   "2 : REGISTER\n" +
                                   "-------------------------------");
    
                System.out.print("choose your choice: ");
                String num = sc.nextLine();
                switch (num) {
                    case "0":
                        System.out.println("Exit Program...");
                        connection.close();
                        sc.close();
                        return;
                    case "1":
                        loginPage(connection);
                        break;
                    case "2":
                        registerPage(connection);
                        break;
                    default:
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }    

    public static void registerPage(Connection connection){
        Scanner sc = new Scanner(System.in);
        String confirmPassword;
        System.out.print("Username: ");
        String username = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();
    
        while (true) {
            System.out.print("Confirm Password: ");
            confirmPassword = sc.nextLine();

            if (password.equals(confirmPassword)) {
                break;
            } else {
                System.out.println("Password is incorrected.");
            }
        }

        System.out.print("Role: ");
        String role = sc.nextLine();

        String insertToSql = "INSERT INTO restaurant.users (username, password, role) VALUES (?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(insertToSql)){
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Registered successfully!");
            } else {
                System.out.println("Registration failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loginPage(Connection connection){
        Scanner sc = new Scanner(System.in);
        System.out.print("Username: ");
        String username = sc.nextLine();
        
        System.out.print("Password: ");
        String password = sc.nextLine();

        Register user1 = new Register(username, password);
        
        String loginToSql = "SELECT * FROM restaurant.users WHERE username = ? AND password = ?";

        try (PreparedStatement ps = connection.prepareStatement(loginToSql)) {

            ps.setString(1, user1.getUsername());
            ps.setString(2, user1.getPassword());

            var rowsInserted = ps.executeQuery();
            if (rowsInserted.next()) {
                System.out.println("Login successfully! Welcome, " + user1.getUsername());

                MenuMain.mainPage(connection); // link to Menumain method mainPage
                return; // ออกจาก loginPage
            }else{
                System.out.println("Password or Username is incorected.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
