import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class RegisterMain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Username: ");
        String username = sc.nextLine();
        
        System.out.print("Password: ");
        String password = sc.nextLine();
        
        System.out.print("Role: ");
        String role = sc.nextLine();

        Register user1 = new Register(username, password, role);
        
        SqlConnect connect = new SqlConnect();
        
        String insertToSql = "INSERT INTO restaurant.users (username, password, role) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(connect.getUrlD(), connect.getUserSqlD(), connect.getPassSqlD());
             PreparedStatement ps = connection.prepareStatement(insertToSql)) {

            ps.setString(1, user1.getUsername());
            ps.setString(2, user1.getPassword());
            ps.setString(3, user1.getRole());

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("User registered successfully!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        sc.close(); // ปิด Scanner
    }
}
