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
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class MenuFrame extends JFrame{
    JComboBox comboBox1;

    JPanel sidePanel;
    boolean isMenuVisible = true;

    Label nameMenuLabel;
    Label priceLabel;
    Label categoryLabel;
    Label idMenuLabel;
    Label selectColumLabel;

    TextField idMenuTextField;
    TextField nameField;
    TextField priceField;
    TextField categoryField;

    Button insertButton;
    Button deleteButton;

    JTable menuTable;

    DefaultTableModel model;

    MenuFrame(String username, String role){
        this.setTitle("Menu");
        this.setSize(1920, 1080);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        deleteButton = new Button("Delete", 1100, 200, 100, 30);
        this.add(deleteButton);

        initializeUI();

        Label label1 = new Label("Login successfully! Welcome, " + username + " Role: " + role, 20, 250, 10, 1000, 50);
        this.add(label1);
    }

    protected void initializeUI(){
        setMenuBars();
        setTableMenu();
        setComboBox();
        setLabel();
        setTextFields();
        objectVisible();

        this.setVisible(true);
    }

    private void objectVisible(){
        nameMenuLabel.setVisible(false);
        nameField.setVisible(false);
        priceLabel.setVisible(false);
        priceField.setVisible(false);
        categoryLabel.setVisible(false);
        categoryField.setVisible(false);
        insertButton.setVisible(false);
        deleteButton.setVisible(false);
        idMenuLabel.setVisible(false);
        idMenuTextField.setVisible(false);
        selectColumLabel.setVisible(false);
    }

    private void setMenuBars() {
        JButton menuButton = new JButton("☰");
        menuButton.setBounds(0, 0, 200, 30);
        menuButton.setHorizontalAlignment(SwingConstants.LEFT);
        menuButton.setBackground(Color.BLACK);
        menuButton.setFocusPainted(false);
        menuButton.setContentAreaFilled(true);
        menuButton.setBorderPainted(false);
        menuButton.setForeground(Color.WHITE);
        menuButton.setFocusable(false);
    
        this.add(menuButton);
    
        // Side Menu Panel
        sidePanel = new JPanel();
        sidePanel.setBackground(Color.WHITE);
        sidePanel.setBounds(0, 0, 200, 1080);
        sidePanel.setLayout(null);

        JButton menuItem1 = new JButton("Add Menu");
        menuItem1.setBounds(0, 30, 200, 40);
        menuItem1.setBackground(Color.WHITE);
        menuItem1.setFocusPainted(false);
        menuItem1.setFocusable(false);
        menuItem1.setBorderPainted(false);
        sidePanel.add(menuItem1);

        JButton menuItem2 = new JButton("Order Food");
        menuItem2.setBounds(0, 70, 200, 40);
        menuItem2.setBackground(Color.WHITE);
        menuItem2.setFocusPainted(false);
        menuItem2.setFocusable(false);
        menuItem2.setBorderPainted(false);
        sidePanel.add(menuItem2);

        JButton menuItem3 = new JButton("Menu 3");
        menuItem3.setBounds(0, 110, 200, 40);
        menuItem3.setBackground(Color.WHITE);
        menuItem3.setFocusPainted(false);
        menuItem3.setFocusable(false);
        menuItem3.setBorderPainted(false);
        sidePanel.add(menuItem3);

        sidePanel.setVisible(true);
        this.add(sidePanel);

        // Event ☰
        menuButton.addActionListener(e -> {
            isMenuVisible = !isMenuVisible;
            sidePanel.setVisible(isMenuVisible);
        });

        menuItem2.addActionListener(e ->{
            new OrderFood();
            dispose();
        });
    }

    private void setTextFields(){
        nameField = new TextField(900, 150, 200, 30);
        priceField = new TextField(900, 210, 200, 30);
        categoryField = new TextField(900, 270, 200, 30);
        idMenuTextField = new TextField(900, 150, 500, 30);

        this.add(nameField);
        this.add(priceField);
        this.add(categoryField);
        this.add(idMenuTextField);

        setButtonInsert(nameField, priceField, categoryField, idMenuTextField);
        selectTextField(idMenuTextField);
    }

    private void setLabel(){
        nameMenuLabel = new Label("Name", 16, 800, 150, 200, 30);
        priceLabel = new Label("Price", 16, 800, 210, 200, 30);
        categoryLabel = new Label("Category", 16, 800, 270, 200, 30);
        idMenuLabel = new Label("ID Select", 16, 800, 150, 200, 30);
        selectColumLabel = new Label("Please select colum ID to delete.", 16, 800, 50, 500, 30);

        this.add(nameMenuLabel);
        this.add(priceLabel);
        this.add(categoryLabel);    
        this.add(idMenuLabel);
        this.add(selectColumLabel);
    }

    private void setComboBox(){
        String[] c = {" ", "Insert", "Delete"}; 

        comboBox1 = new JComboBox<>(c);
        comboBox1.setBounds(800, 100, 200, 30);
        comboBox1.setFont(new Font("Tahoma", Font.PLAIN, 16));
        comboBox1.setBackground(Color.WHITE);

        this.add(comboBox1);

        comboBox1.addActionListener(e -> {
            String selected = (String) comboBox1.getSelectedItem();
            if (selected.equals("Insert")) {
                nameMenuLabel.setVisible(true);
                nameField.setVisible(true);
                priceLabel.setVisible(true);
                priceField.setVisible(true);
                categoryLabel.setVisible(true);
                categoryField.setVisible(true);
                insertButton.setVisible(true);

                idMenuLabel.setVisible(false);
                idMenuTextField.setVisible(false);
                deleteButton.setVisible(false);
                selectColumLabel.setVisible(false);
            }
            else if(selected.equals("Delete")){
                idMenuLabel.setVisible(true);
                idMenuTextField.setVisible(true);
                deleteButton.setVisible(true);
                selectColumLabel.setVisible(true);

                nameMenuLabel.setVisible(false);
                nameField.setVisible(false);
                priceLabel.setVisible(false);
                priceField.setVisible(false);
                categoryLabel.setVisible(false);
                categoryField.setVisible(false);
                insertButton.setVisible(false);
            }
            else{
                idMenuLabel.setVisible(false);
                idMenuTextField.setVisible(false);
                deleteButton.setVisible(false);
                selectColumLabel.setVisible(false);
                nameMenuLabel.setVisible(false);
                nameField.setVisible(false);
                priceLabel.setVisible(false);
                priceField.setVisible(false);
                categoryLabel.setVisible(false);
                categoryField.setVisible(false);
                insertButton.setVisible(false);
            }
        });
    }

    private void setTableMenu(){
        menuTable = new JTable();
        model = new DefaultTableModel();
        menuTable.setModel(model);
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();

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
                        menuTable.getColumnModel().getColumn(3).getPreferredWidth() + 4;

        JScrollPane scrollPane = new JScrollPane(menuTable);
        scrollPane.setBounds(250, 100, tableWidth, 600);
        this.add(scrollPane);

        showTableMenu(menuTable, model);

        menuTable.getTableHeader().repaint();
    }

    private void selectTextField(TextField id) {
        ArrayList<Integer> arr = new ArrayList<>();
    
        menuTable.setCellSelectionEnabled(true);
        menuTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        menuTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int[] selectedRows = menuTable.getSelectedRows();
                StringBuilder selectedText = new StringBuilder();
                arr.clear();
    
                for (int row : selectedRows) {
                    Object value = menuTable.getValueAt(row, 0);
                    if (value != null) {
                        try {
                            int intValue = Integer.parseInt(value.toString());
                            arr.add(intValue);
                            selectedText.append(intValue).append(" ");
                        } catch (NumberFormatException ex) {
                            System.out.println("Not number: " + value);
                        }
                    }
                }
    
                String result = selectedText.toString().replaceAll(", $", "");
                id.setText("Selected: " + result);
    
                System.out.println("Array: " + arr);

                buttonDelete(arr, id);
            }
        });
    }

    private void buttonDelete(ArrayList<Integer> arr, TextField id) {
        SqlConnect connect = new SqlConnect();
        for (ActionListener al : deleteButton.getActionListeners()) {
            deleteButton.removeActionListener(al);
        }
    
        deleteButton.addActionListener(ev -> {
            String delete = "DELETE FROM restaurant.menu WHERE id = ?";
            
            try (Connection connection = DriverManager.getConnection(connect.getUrlD(), connect.getUserSqlD(), connect.getPassSqlD());
                 PreparedStatement ps = connection.prepareStatement(delete)) {
                
                for (int i = 0; i < arr.size(); i++) {
                    ps.setInt(1, arr.get(i));
                    ps.executeUpdate();
                    System.out.println("Delete id = " + arr.get(i));
                }
                
                model.setRowCount(0); // clear old table
                showTableMenu(menuTable, model); // refresh table
                
                JOptionPane.showMessageDialog(this, "Delete success!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    

    private void setButtonInsert(TextField menuName, TextField price, TextField category, TextField id){
        SqlConnect connect = new SqlConnect();

        insertButton = new Button("Insert", 1100, 310, 80, 30);

        this.add(insertButton);

        String insertSql = "INSERT INTO restaurant.menu (name, price, category) VALUES (?, ?, ?)";
        insertButton.addActionListener(e ->{
            String nameText = menuName.getText().trim();
            String priceText = price.getText().trim();
            String categoryText = category.getText().trim();

            // check null
            if (nameText.isEmpty() || priceText.isEmpty() || categoryText.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(null, "Please Insert text.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            int priceValue;
            try {
                priceValue = Integer.parseInt(priceText);
            } catch (NumberFormatException ex) {
                javax.swing.JOptionPane.showMessageDialog(null, "กรุณากรอกราคาเป็นตัวเลขเท่านั้น", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection connection = DriverManager.getConnection(connect.getUrlD(), connect.getUserSqlD(), connect.getPassSqlD());
                PreparedStatement ps = connection.prepareStatement(insertSql)){
                ps.setString(1, menuName.getText());
                ps.setInt(2, priceValue);
                ps.setString(3, category.getText());

                int rowsInserted = ps.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("-------------------------------\n"
                                        + "add new menu success!\n" +
                                            "-------------------------------");
                    model.setRowCount(0); // clear old table
                    showTableMenu(menuTable, model); // refresh table
                } else {
                    System.out.println("-------------------------------\n"
                                        +"can't add new menu\n" +
                                        "-------------------------------");
                }
            } catch (Exception a) {
                a.printStackTrace();
            }
        });
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
    
}
