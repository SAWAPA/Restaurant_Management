package GUI.page;


import GUI.attribute.*;

import java.awt.Color;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import GUI.SqlConnect;

public class OrderFoodPage extends JPanel{
    private JTable menuTable;
    private JTable orderTable;

    private Connection connection;

    private Button deleteOrderButton;

    private DefaultTableModel menuModel;
    private DefaultTableModel orderModel;
    
    public OrderFoodPage(){
        this.setLayout(null);
        this.setBackground(Color.WHITE);

        initializeUI();
    }

    protected void initializeUI(){
        setMenuTable();
        setOrderTable();
        setLabel();
        setTextFields();
        createDeleteButton();
        selectTableField();
        
        this.setVisible(true);
    }

    private void setLabel(){
        Label orderLable = new Label("Menu", "bold", 20, 100, 30, 200, 50);  
        Label foodId = new Label("Menu ID", "bold", 20, 700, 30, 200, 50);
        Label foodQuantity = new Label("Quantity", "bold", 20, 870, 30, 200, 50);

        this.add(foodId);
        this.add(orderLable);
        this.add(foodQuantity);
    }

    private void setTextFields(){
        TextField orderTextField = new TextField(18, 800, 40, 50, 30);
        TextField quantityTextField = new TextField(18, 980, 40, 50, 30);

        this.add(orderTextField);
        this.add(quantityTextField);

        addMenuButton(orderTextField, quantityTextField);
    }

    private void createDeleteButton() {
        
        deleteOrderButton = new Button("Delete", 1200, 40, 90, 30);
        deleteOrderButton.setEnabled(false);
        this.add(deleteOrderButton);
    
        deleteOrderButton.addActionListener(ev -> {
            int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete?", "Confirm Delete", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                SqlConnect connect = new SqlConnect();
                String delete = "DELETE FROM restaurant.orders WHERE id = ?";
                try (Connection connection = DriverManager.getConnection(connect.getUrlD(), connect.getUserSqlD(), connect.getPassSqlD());
                    PreparedStatement ps = connection.prepareStatement(delete)) {
                    
                    for (int i = 0; i < selectedIds.size(); i++) {
                        ps.setInt(1, selectedIds.get(i));
                        ps.executeUpdate();
                        System.out.println("Delete id = " + selectedIds.get(i));
                    }
                    
                    orderModel.setRowCount(0);
                    showOrderMenu(orderTable, orderModel);

                    JOptionPane.showMessageDialog(this, "Delete success!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return;
            }
        });
    }
    
    private ArrayList<Integer> selectedIds = new ArrayList<>();
    
    private void selectTableField(){
        orderTable.setCellSelectionEnabled(true);
        orderTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    
        orderTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int[] selectedRows = orderTable.getSelectedRows();
                selectedIds.clear();
                for (int row : selectedRows) {
                    Object value = orderTable.getValueAt(row, 0);
                    if (value != null) {
                        try {
                            int intValue = Integer.parseInt(value.toString());
                            selectedIds.add(intValue);
                        } catch (NumberFormatException ex) {
                            System.out.println("Not number: " + value);
                        }
                    }
                }
                deleteOrderButton.setEnabled(!selectedIds.isEmpty());
            }
        });
    }

    private void addMenuButton(TextField orderId, TextField quantity){
        try {
            SqlConnect connect = new SqlConnect();
            final String URL = connect.getUrlD() + "?useUnicode=true&characterEncoding=UTF-8";
            final String USER = connect.getUserSqlD();
            final String PASS = connect.getPassSqlD();
        
            connection = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException ev) {
            ev.printStackTrace();
            return;
        }

        Button addButton = new Button("Add", 1100, 40, 90, 30);

        String getPriceAndNameSql = "SELECT price, name FROM restaurant.menu WHERE id = ?";
        String insertOrder = "INSERT INTO restaurant.orders (menu_id, name, quantity, total_price) VALUES (?, ?, ?, ?)";
        String showOrder = "SELECT * FROM restaurant.orders";

        this.add(addButton);

        addButton.addActionListener(e ->{
            String idText = orderId.getText().trim();
            String qText = quantity.getText().trim();
            
            int id;
            int q;

            // check null
            if (idText.isEmpty() || qText.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(null, "Please Insert text.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                id = Integer.parseInt(idText);
                q = Integer.parseInt(qText);
            } catch (NumberFormatException ex) {
                javax.swing.JOptionPane.showMessageDialog(null, "Please enter the price as numbers only.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            int pricePerUnit = 0;
            String foodName = "";

            try (PreparedStatement ps = connection.prepareStatement(getPriceAndNameSql)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
    
                if (rs.next()) {
                    pricePerUnit = rs.getInt("price");
                    foodName = rs.getString("name");
                } else {
                    System.out.println("Food ID not found.");
                    return;
                }
            } catch (SQLException ev) {
                ev.printStackTrace();
            }

            Order order = new Order(id, q, pricePerUnit);

            try (PreparedStatement ps = connection.prepareStatement(insertOrder)) {
                ps.setInt(1, order.getMenuId());
                ps.setString(2, foodName);
                ps.setInt(3, order.getQuantity());
                ps.setInt(4, order.getTotalPrice());
    
                int rowsInserted = ps.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Order placed successfully!");
                } else {
                    System.out.println("Failed to place order.");
                }
            } catch (SQLException ev) {
                ev.printStackTrace();
            }

            try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(showOrder)){

                orderModel.setRowCount(0);
                while (resultSet.next()) {
                    id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    q = resultSet.getInt("quantity");
                    int price = resultSet.getInt("total_price");
                    String date = resultSet.getString("order_date");
        
                    orderModel.addRow(new Object[]{id, name, q, price, date});
                }
                
            } catch (Exception ev) {
                ev.printStackTrace();
            }
            
        });
    }

    private void setMenuTable(){
        menuTable = new JTable();
        menuModel = new DefaultTableModel();
        menuTable.setModel(menuModel);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        menuTable.setDefaultEditor(Object.class, null);
        center.setHorizontalAlignment(SwingConstants.CENTER);
        
        Font thaiFont = new Font("Tahoma", Font.PLAIN, 16);
        menuTable.setFont(thaiFont);
        menuTable.setRowHeight(30);
        menuTable.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 16));

        menuModel.addColumn("ID");
        menuModel.addColumn("Name");
        menuModel.addColumn("Price");
        menuModel.addColumn("Category");

        menuTable.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        menuTable.getColumnModel().getColumn(0).setCellRenderer(center);

        menuTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Name

        menuTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Price
        menuTable.getColumnModel().getColumn(2).setCellRenderer(center);
        
        menuTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Category

        menuTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int tableWidth = menuTable.getColumnModel().getColumn(0).getPreferredWidth() +
                        menuTable.getColumnModel().getColumn(1).getPreferredWidth() +
                        menuTable.getColumnModel().getColumn(2).getPreferredWidth() +
                        menuTable.getColumnModel().getColumn(3).getPreferredWidth() + 4;

        JScrollPane scrollPane = new JScrollPane(menuTable);
        scrollPane.setBounds(100, 100, tableWidth, 600);
        this.add(scrollPane);

        showTableMenu(menuTable, menuModel);

        menuTable.getTableHeader().repaint();
    }

    private void showTableMenu(JTable menu, DefaultTableModel model) {
        try {
            // connect to database
            SqlConnect connect = new SqlConnect();
            final String URL = connect.getUrlD() + "?useUnicode=true&characterEncoding=UTF-8"; // Support UTF-8
            final String USER = connect.getUserSqlD();
            final String PASS = connect.getPassSqlD();
    
            String query = "SELECT * FROM restaurant.menu";
    
            Connection connection = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            //show data
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int price = rs.getInt("price");
                String category = rs.getString("category");
    
                model.addRow(new Object[]{id, name, price, category});
            }

            rs.close();
            ps.close();
            connection.close();
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setOrderTable(){
        orderTable = new JTable();
        orderModel = new DefaultTableModel();
        orderTable.setModel(orderModel);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        orderTable.setDefaultEditor(Object.class, null);
        center.setHorizontalAlignment(SwingConstants.CENTER);

        Font thaiFont = new Font("Tahoma", Font.PLAIN, 16);
        orderTable.setFont(thaiFont);
        orderTable.setRowHeight(30);
        orderTable.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 16));

        orderModel.addColumn("ID");
        orderModel.addColumn("Name");
        orderModel.addColumn("Quantity");
        orderModel.addColumn("Price");
        orderModel.addColumn("Order Date");

        orderTable.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        orderTable.getColumnModel().getColumn(0).setCellRenderer(center);

        orderTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Name

        orderTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Price
        orderTable.getColumnModel().getColumn(2).setCellRenderer(center);
        
        orderTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Category

        orderTable.getColumnModel().getColumn(4).setPreferredWidth(200);

        orderTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int tableWidth = orderTable.getColumnModel().getColumn(0).getPreferredWidth() +
                            orderTable.getColumnModel().getColumn(1).getPreferredWidth() +
                            orderTable.getColumnModel().getColumn(2).getPreferredWidth() +
                            orderTable.getColumnModel().getColumn(3).getPreferredWidth() + 
                            orderTable.getColumnModel().getColumn(4).getPreferredWidth() + 4;

        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setBounds(800, 100, tableWidth, 600);
        this.add(scrollPane);

        showOrderMenu(orderTable, menuModel);
        orderTable.getTableHeader().repaint();
    }

    private void showOrderMenu(JTable order, DefaultTableModel model) {
        try {
            // connect to database
            SqlConnect connect = new SqlConnect();
            final String URL = connect.getUrlD() + "?useUnicode=true&characterEncoding=UTF-8"; // Support UTF-8
            final String USER = connect.getUserSqlD();
            final String PASS = connect.getPassSqlD();
    
            String query = "SELECT * FROM restaurant.orders";
    
            Connection connection = DriverManager.getConnection(URL, USER, PASS);
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            //show data
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int quantity = rs.getInt("quantity");
                int total_price = rs.getInt("total_price");
                String dateTime = rs.getString("order_date");
    
                orderModel.addRow(new Object[]{id, name, quantity, total_price, dateTime});
            }

            rs.close();
            ps.close();
            connection.close();
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
