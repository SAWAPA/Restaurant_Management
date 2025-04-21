package GUI;

import GUI.attribute.Label;

import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class MenuFrame extends JFrame{
    MenuFrame(){
        this.setTitle("Menu");
        this.setSize(1920, 1080);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setTableMenu();

        this.setVisible(true);
    }

    private void setLabel(){
        
    }

    private void setTableMenu(){
        JTable menuTable = new JTable();
        DefaultTableModel model = new DefaultTableModel();
        menuTable.setModel(model);

        menuTable.setDefaultEditor(Object.class, null);
        
        Font thaiFont = new Font("Tahoma", Font.PLAIN, 16);
        menuTable.setFont(thaiFont);
        menuTable.setRowHeight(30);
        menuTable.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 16));

        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Price");
        model.addColumn("Category");

        menuTable.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        menuTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Name
        menuTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Price
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
            String url = connect.getUrlD() + "?useUnicode=true&characterEncoding=UTF-8"; // รองรับ UTF-8
            String user = connect.getUserSqlD();
            String pass = connect.getPassSqlD();
    
            String query = "SELECT * FROM restaurant.menu";
    
            Connection connection = DriverManager.getConnection(url, user, pass);
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
