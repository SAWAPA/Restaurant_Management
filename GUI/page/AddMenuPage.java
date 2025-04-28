package GUI.page;

import GUI.attribute.Label;
import GUI.SqlConnect;
import GUI.attribute.Button;
import GUI.attribute.TextField;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddMenuPage extends JPanel {
    private JTable menuTable;

    private DefaultTableModel model;

    private JComboBox comboBox1;

    private Label nameMenuLabel;
    private Label priceLabel;
    private Label categoryLabel;
    private Label idMenuLabel;
    private Label selectColumLabel;
    private Label idMenuSelect;

    private TextField nameField;
    private TextField priceField;
    private TextField categoryField;

    private Button insertButton;
    private Button deleteButton;


    public AddMenuPage() {
        this.setLayout(null);
        this.setBackground(Color.WHITE);

        deleteButton = new Button("Delete", 1100, 250, 100, 30);
        this.add(deleteButton);
        
        initializeUI();
    }

    protected void initializeUI(){
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
        idMenuSelect.setVisible(false);
        selectColumLabel.setVisible(false);
    }

    private void setTextFields(){
        nameField = new TextField(900, 150, 200, 30);
        priceField = new TextField(900, 210, 200, 30);
        categoryField = new TextField(900, 270, 200, 30);

        this.add(nameField);
        this.add(priceField);
        this.add(categoryField);

        setButtonInsert(nameField, priceField, categoryField);
    }

    private void setLabel(){
        nameMenuLabel = new Label("Name", 16, 800, 150, 200, 30);
        priceLabel = new Label("Price", 16, 800, 210, 200, 30);
        categoryLabel = new Label("Category", 16, 800, 270, 200, 30);
        idMenuLabel = new Label("ID Select: ", 16, 800, 200, 200, 30);
        selectColumLabel = new Label("Please select colum ID to delete.", 16, 800, 150, 500, 30);
        idMenuSelect = new Label("........", 16, 900, 200, 500, 30);

        this.add(nameMenuLabel);
        this.add(priceLabel);
        this.add(categoryLabel);    
        this.add(idMenuLabel);
        this.add(selectColumLabel);
        this.add(idMenuSelect);

        selectTextField(idMenuSelect);
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
                idMenuSelect.setVisible(false);
                deleteButton.setVisible(false);
                selectColumLabel.setVisible(false);
            }
            else if(selected.equals("Delete")){
                idMenuLabel.setVisible(true);
                idMenuSelect.setVisible(true);
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
                idMenuSelect.setVisible(false);
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

    private void selectTextField(Label id) {
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
                id.setText(result);
    
                System.out.println("Array: " + arr);

                buttonDelete(arr, id);
            }
        });
    }

    private void buttonDelete(ArrayList<Integer> arr, Label id) {
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
    

    private void setButtonInsert(TextField menuName, TextField price, TextField category){
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
                javax.swing.JOptionPane.showMessageDialog(null, "Please enter the price as numbers only.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
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