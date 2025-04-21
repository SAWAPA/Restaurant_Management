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
                               "1 : Show Menu\n" +
                               "2 : Add new Menu\n" +
                               "3 : Delete Menu\n" +
                               "4 : Order Food\n" +
                               "5 : Show Order\n" +
                               "6 : Log Out\n" +
                               "-------------------------------");
            System.out.print("choose your choice : ");
    
            int num = sc.nextInt();
            sc.nextLine();
    
            switch (num) {
                case 0:
                    System.out.println("Exit Program...");
                    System.exit(0);
                case 1:
                    showData(connection);
                    break;
                case 2:
                    addMenu(connection, sc);
                    break;
                case 3:
                    deleteMenu(connection, sc);
                    break;
                case 4:
                    orderFood(connection, sc);
                    break;
                case 5:
                    showOrder(connection, sc);
                    break;
                case 6:
                    logOut(connection);
                    return;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }

    public static void showOrder(Connection connection, Scanner sc) {
        String showOrder = "SELECT * FROM restaurant.orders";
        String clearOrder = "DELETE FROM restaurant.orders";
        String orderSum = "SELECT SUM(total_price) FROM restaurant.orders";
    
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(showOrder)) {
            boolean hasData = false;
            System.out.println("-------------------------------");
            System.out.println("*--- All ordered food ---*");
            while (resultSet.next()) {
                hasData = true;
                System.out.print("[ID: " + resultSet.getInt("id") + " | ");
                System.out.print("Menu ID: " + resultSet.getString("menu_id") + " | ");
                System.out.print("Name: " + resultSet.getString("name") + " | ");
                System.out.print("Quantity: " + resultSet.getInt("quantity") + " | ");
                System.out.print("Price: " + resultSet.getDouble("total_price") + " | ");
                System.out.println("Order Date: " + resultSet.getString("order_date") + " ]");
            }
            System.out.println("-------------------------------");
    
            if (!hasData) {
                System.out.println("--Order is Empty--\n-------------------------------");
                return;
            }

            // แสดงยอดรวมของราคาทั้งหมด
            try (Statement sumStatement = connection.createStatement();
                ResultSet sumResult = sumStatement.executeQuery(orderSum)) {

                if (sumResult.next()) {
                double total = sumResult.getDouble(1);
                System.out.println("Total price of all orders: " + total);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        System.out.println("Enter 1 to clear all orders\nEnter 2 to cancel.");
        int num = sc.nextInt();
    
        switch (num) {
            case 1:
                try (Statement statement = connection.createStatement()) {
                    int rowsDeleted = statement.executeUpdate(clearOrder);
                    System.out.println("All orders cleared (" + rowsDeleted + " rows deleted).\n-------------------------------");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

            case 2:
                break;
            default:
                System.out.println("Invaild choice");
                break;
        }
    }
    

    public static void orderFood(Connection connection, Scanner sc) {
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
            
            if(!hasData){
                System.out.println("--Menu is Empty--");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.print("Food ID : ");
        int foodId = sc.nextInt();
        System.out.print("Quantity : ");
        int quantity = sc.nextInt();

        // ดึงราคาจาก menu ที่ตรงกับ foodId
        String getPriceAndNameSql = "SELECT price, name FROM restaurant.menu WHERE id = ?";
        int pricePerUnit = 0;
        String foodName = "";

        try (PreparedStatement ps = connection.prepareStatement(getPriceAndNameSql)) {
            ps.setInt(1, foodId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                pricePerUnit = rs.getInt("price");
                foodName = rs.getString("name");
            } else {
                System.out.println("Food ID not found.");
                return; // ถ้าไม่เจอเมนู ให้จบ method เลย
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // สร้างออเดอร์ใหม่
        Order order = new Order(foodId, quantity, pricePerUnit);

        // แสดงรายละเอียดการสั่งซื้อ
        System.out.println("Order Details: " + order.toString());

        // Insert ข้อมูลลงใน restaurant.orders พร้อมชื่อ
        String insertOrderSql = "INSERT INTO restaurant.orders (menu_id, name, quantity, total_price) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(insertOrderSql)) {
            ps.setInt(1, order.getMenuId());
            ps.setString(2, foodName); // name from menu
            ps.setInt(3, order.getQuantity());
            ps.setInt(4, order.getTotalPrice());

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Order placed successfully!");
            } else {
                System.out.println("Failed to place order.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteMenu(Connection connection, Scanner sc) {
        System.out.print("Food ID : ");
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
    
    public static void addMenu(Connection connection, Scanner sc) {
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
