import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // ข้อมูลการเชื่อมต่อฐานข้อมูล

        // final String url = "jdbc:mysql://localhost:3306/restaurant";
        // final String username = "root"; // หรือชื่อผู้ใช้ที่ตั้งไว้
        // final String password = "50473";
        // SqlConnect userSql = new SqlConnect(url, username, password);

        SqlConnect userSql1 = new SqlConnect();

        try {
            Connection connection = DriverManager.getConnection(userSql1.getUrlD(), userSql1.getUserSqlD(), userSql1.getPassSqlD());
            Scanner sc = new Scanner(System.in);
        
            System.out.println("connected MySQL success!");
            while (true){
                System.out.println(
                                "0 : Exit Program\n"+
                                "1 : Show data in Database\n" +
                                "2 : Add new Menu\n" +
                                "3 : Delete Menu");
                System.out.print("chose your choice : ");
            
                int num = sc.nextInt();
                switch (num) {
                    case 0:
                        System.out.println("Exit Program...");
                        connection.close();
                        sc.close();
                        return;
                    case 1:
                        showData(connection);
                        break;
                    case 2:
                        addMenu(connection);
                        break;
                    case 3:
                        deleteMenu(connection);
                        break;
                    default:
                        break;
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteMenu(Connection connection) {
        Scanner sc = new Scanner(System.in);
        System.out.print("food ID : ");
        int foodId = sc.nextInt();

        String deleteFood = "DELETE FROM restaurant.menu WHERE id = ?";
    
        try (PreparedStatement ps = connection.prepareStatement(deleteFood)) {
            ps.setInt(1, foodId);

            int rowsDeleted = ps.executeUpdate();
    
            if (rowsDeleted > 0) {
                System.out.println("Delete Success!");
            } else {
                System.out.println("Not Found ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void addMenu(Connection connection) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Food name: ");
        String productName = sc.nextLine();

        System.out.print("price: ");
        double productPrice = sc.nextDouble();
        sc.nextLine();

        System.out.print("category: ");
        String productCategory = sc.nextLine();

        String insertSql = "INSERT INTO restaurant.menu (name, price, category) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
            ps.setString(1, productName);
            ps.setDouble(2, productPrice);
            ps.setString(3, productCategory);

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("add new menu success!");
            } else {
                System.out.println("can't add new menu");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showData(Connection connection){
        String query = "SELECT * FROM restaurant.menu";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            boolean hasData = false;
            System.out.println("---Menu in restaurant---");
            while (resultSet.next()) {
                hasData = true;
                System.out.print("ID: [" + resultSet.getInt("id") + " | ");
                System.out.print("name: " + resultSet.getString("name") + " | ");
                System.out.print("price: " + resultSet.getDouble("price") + " | ");
                System.out.println("category: " + resultSet.getString("category")+ " ]");
            }
            System.out.println("─────────────────────────");

            if(!hasData){
                System.out.println("--Menu is Empty--");
            }

            System.out.println("─────────────────────────");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
