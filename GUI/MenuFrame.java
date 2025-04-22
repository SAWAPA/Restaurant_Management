package GUI;

import GUI.attribute.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class MenuFrame extends JFrame{
    JComboBox comboBox1;

    MenuFrame(String username, String role){
        this.setTitle("Menu");
        this.setSize(1920, 1080);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Label label1 = new Label("Login successfully! Welcome, " + username + " Role: " + role, 20, 10, 10, 1000, 50);
        this.add(label1);

        setTableMenu();
        setComboBox();

        this.setVisible(true);
    }

    private void setComboBox(){
        Label nameMenuLabel = new Label("Name", 16, 700, 150, 200, 30);
        Label priceLabel = new Label("Price", 16, 700, 210, 200, 30);
        Label categoryLabel = new Label("Category", 16, 700, 270, 200, 30);

        TextField nameField = new TextField(790, 150, 200, 30);
        TextField priceField = new TextField(790, 210, 200, 30);
        TextField categoryField = new TextField(790, 270, 200, 30);

        String[] c = {" ", "Insert", "Delete"}; 

        comboBox1 = new JComboBox<>(c);
        comboBox1.setBounds(700, 100, 200, 30);
        comboBox1.setFont(new Font("Tahoma", Font.PLAIN, 16));
        comboBox1.setBackground(Color.WHITE);

        nameMenuLabel.setVisible(false);
        nameField.setVisible(false);
        priceLabel.setVisible(false);
        priceField.setVisible(false);
        categoryLabel.setVisible(false);
        categoryField.setVisible(false);

        this.add(comboBox1);
        this.add(nameMenuLabel);
        this.add(priceLabel);
        this.add(categoryLabel);
        this.add(nameField);
        this.add(priceField);
        this.add(categoryField);

        comboBox1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selected = (String) comboBox1.getSelectedItem();
                if (selected.equals("Insert")) {
                    nameMenuLabel.setVisible(true);
                    nameField.setVisible(true);
                    priceLabel.setVisible(true);
                    priceField.setVisible(true);
                    categoryLabel.setVisible(true);
                    categoryField.setVisible(true);
                }else {
                    nameMenuLabel.setVisible(false);
                    nameField.setVisible(false);
                    priceLabel.setVisible(false);
                    priceField.setVisible(false);
                    categoryLabel.setVisible(false);
                    categoryField.setVisible(false);
                }
            }
        });
    }

    private void setTableMenu(){
        JTable menuTable = new JTable();
        DefaultTableModel model = new DefaultTableModel();
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();

        menuTable.setModel(model);
        menuTable.setDefaultEditor(Object.class, null);
        center.setHorizontalAlignment(SwingConstants.CENTER);
        
        Font thaiFont = new Font("Tahoma", Font.PLAIN, 16);
        menuTable.setFont(thaiFont);
        menuTable.setRowHeight(30);
        menuTable.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 16));

        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Price");
        model.addColumn("Category");

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
                        menuTable.getColumnModel().getColumn(3).getPreferredWidth() + 3;

        JScrollPane scrollPane = new JScrollPane(menuTable);
        scrollPane.setBounds(10, 100, tableWidth, 600);
        this.add(scrollPane);

        showTableMenu(menuTable, model);

        menuTable.getTableHeader().repaint();
    }

    private void showTableMenu(JTable menu, DefaultTableModel model) {

        try {
            // connect to database
            SqlConnect connect = new SqlConnect();
            final String URL = connect.getUrlD() + "?useUnicode=true&characterEncoding=UTF-8"; // รองรับ UTF-8
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
    
}
