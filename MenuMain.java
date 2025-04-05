import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MenuMain {
    public static void mainPage(Connection connection) {
        Scanner sc = new Scanner(System.in);
        System.out.println("-------------------------------");
        while (true) {
            System.out.println("0 : Exit Program\n" +
                               "1 : Show data in Database\n" +
                               "2 : Add new Menu\n" +
                               "3 : Delete Menu\n" +
                               "4 : Log Out\n" +
                               "-------------------------------");
            System.out.print("choose your choice : ");
    
            int num = sc.nextInt();
            sc.nextLine(); // เผื่อใช้ nextLine หลัง nextInt
    
            switch (num) {
                case 0:
                    System.out.println("Exit Program...");
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
                case 4:
                    logOut(connection);
                    return;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
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
                System.out.println("-------------------------------\n" +
                                    "Delete Success!\n" +
                                    "-------------------------------");
            } else {
                System.out.println("-------------------------------\n"
                                    +"Not Found ID\n"+
                                    "-------------------------------");
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
                System.out.println("-------------------------------\n"
                                    + "add new menu success!\n" +
                                    "-------------------------------");
            } else {
                System.out.println("-------------------------------\n"
                                    +"can't add new menu\n" +
                                    "-------------------------------");
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
            System.out.println("-------------------------------");
            System.out.println("*--- Menu in restaurant ---*");
            while (resultSet.next()) {
                hasData = true;
                System.out.print("[ID: " + resultSet.getInt("id") + " | ");
                System.out.print("name: " + resultSet.getString("name") + " | ");
                System.out.print("price: " + resultSet.getDouble("price") + " | ");
                System.out.println("category: " + resultSet.getString("category")+ " ]");
            }
            System.out.println("-------------------------------");
            // System.out.println("─────────────────────────");
            
            if(!hasData){
                System.out.println("--Menu is Empty--");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void logOut(Connection connection) {
        try {
            connection.close();  // close connection
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Logged out.");
        
        // call Main after logout
        Main.main(null);  // go bact to Main
    }
    

}
