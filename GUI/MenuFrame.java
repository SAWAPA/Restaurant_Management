package GUI;

import GUI.attribute.*;
import GUI.page.*;

import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MenuFrame extends JFrame{
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel addMenuPage;
    private JPanel orderFoodPage;
    private JPanel menu3Page;
    private JPanel sidePanel;
    private boolean isMenuVisible = true;

    MenuFrame(String username, String role) {
        this.setTitle("Menu");
        this.setSize(1920, 1080);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addMenuPage = new AddMenuPage();
        addMenuPage.setLayout(null);
        addMenuPage.setBackground(Color.WHITE);

        orderFoodPage = new OrderFoodPage();
        orderFoodPage.setLayout(null);
        orderFoodPage.setBackground(Color.WHITE);

        menu3Page = new JPanel();
        menu3Page.setLayout(null);
        menu3Page.setBackground(Color.WHITE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBounds(200, 0, 1720, 1080);
        mainPanel.add(addMenuPage, "AddMenuPage");
        mainPanel.add(orderFoodPage, "OrderFoodPage");
        mainPanel.add(menu3Page, "Menu3Page");

        this.add(mainPanel);

        initializeUI();

        Label label1 = new Label("Login successfully! Welcome, " + username + " Role: " + role, "bold", 20, 100, 30, 1000, 50);
        addMenuPage.add(label1);

        isMenuVisible = true;
        sidePanel.setVisible(true);
    }


    protected void initializeUI(){
        setMenuBars();

        this.setVisible(true);
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
        sidePanel.setBackground(Color.GRAY);
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

        // click to change menu
        menuItem1.addActionListener(e -> {
            cardLayout.show(mainPanel, "AddMenuPage");
        });

        menuItem2.addActionListener(e -> {
            cardLayout.show(mainPanel, "OrderFoodPage");
        });

        menuItem3.addActionListener(e -> {
            cardLayout.show(mainPanel, "Menu3Page");
        });
    }
}
