package view;

import javax.swing.*;

import control.ClientControl;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HeaderPanel extends JPanel {
    
    private static final Color HEADER_BACKGROUND = new Color(70, 130, 180);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final int LOGO_HEIGHT = 150;
    private static final int HEADER_HEIGHT = 100;
    private static final int SEARCH_FIELD_WIDTH = 300;
    private static final int SEARCH_FIELD_HEIGHT = 35;
    
    private JTextField searchField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel usernameLabel;
    private JPopupMenu userMenu;
    private LoginWindow loginWindow;
    private RegisterWindow registerWindow;
    private JPanel authPanel;
    private ClientControl control;
    private boolean isLoggedIn = false;

    
    public HeaderPanel(ClientControl control) {
        this.control = control;
        initializeComponents();
        initializePanel();
    }

    private void initializeComponents() {
        searchField = createSearchField();
        loginButton = createLoginButton();
        registerButton = createRegisterButton();
        usernameLabel = createUsernameLabel();
        userMenu = createUserMenu();
    }

    private void initializePanel() {
        setupPanelProperties();
        addLogoPanel();
        addSearchPanel();
        addAuthPanel();
    }

    private void setupPanelProperties() {
        setBackground(HEADER_BACKGROUND);
        setPreferredSize(new Dimension(1200, HEADER_HEIGHT));
        setLayout(new BorderLayout());
    }

    private void addLogoPanel() {
        add(createLogoPanel(), BorderLayout.WEST);
    }

    private void addSearchPanel() {
        add(createSearchPanel(), BorderLayout.CENTER);
    }

    private void addAuthPanel() {
        authPanel = createAuthPanel();
        add(authPanel, BorderLayout.EAST);
    }

    private JPanel createLogoPanel() {
        JPanel logoPanel = createBaseLogoPanel();
        try {
            logoPanel.add(createLogoLabel());
        } catch (Exception e) {
            handleLogoError(e);
        }
        return logoPanel;
    }

    private JPanel createBaseLogoPanel() {
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setOpaque(false);
        return logoPanel;
    }

    private JLabel createLogoLabel() {
        ImageIcon scaledIcon = loadAndScaleLogo();
        JLabel logoLabel = new JLabel(scaledIcon);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(-30, 20, 0, 0));
        return logoLabel;
    }

    private ImageIcon loadAndScaleLogo() {
        String path = "resources/CarHubLogo.png";
        ImageIcon originalIcon = new ImageIcon(path);
        Image scaledImage = originalIcon.getImage().getScaledInstance(
            -1, 
            LOGO_HEIGHT,
            Image.SCALE_SMOOTH
        );
        return new ImageIcon(scaledImage);
    }

    private void handleLogoError(Exception e) {
        System.err.println("Error loading logo: " + e.getMessage());
        e.printStackTrace();
    }

    private JTextField createSearchField() {
        JTextField field = new JTextField(25);
        field.setPreferredSize(new Dimension(SEARCH_FIELD_WIDTH, SEARCH_FIELD_HEIGHT));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        return field;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        configureSearchPanel(searchPanel);
        searchPanel.add(searchField);
        searchPanel.add(createSearchButton());
        return searchPanel;
    }

    private void configureSearchPanel(JPanel searchPanel) {
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(
            (HEADER_HEIGHT - SEARCH_FIELD_HEIGHT) / 2, 0, 0, 0
        ));
    }

    private JButton createSearchButton() {
        JButton searchButton = new JButton("Search");
        configureSearchButton(searchButton);
        searchButton.addActionListener(e -> handleSearch());
        return searchButton;
    }

    private void configureSearchButton(JButton button) {
        button.setPreferredSize(new Dimension(100, SEARCH_FIELD_HEIGHT));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private void handleSearch() {
        if (!getSearchText().isBlank()) {
            control.search(getSearchText());
        } else {
            showValidationError("Ingresa algo para buscar");
        }
    }

    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "Error de Validación", 
            JOptionPane.ERROR_MESSAGE);
    }

    private JButton createLoginButton() {
        JButton button = new JButton("Iniciar Sesión");
        configureAuthButton(button);
        button.addActionListener(e -> showLoginWindow());
        return button;
    }

    private JButton createRegisterButton() {
        JButton button = new JButton("Registrarse");
        configureAuthButton(button);
        button.addActionListener(e -> showRegisterWindow());
        return button;
    }

    private void configureAuthButton(JButton button) {
        button.setPreferredSize(new Dimension(120, SEARCH_FIELD_HEIGHT));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private JPopupMenu createUserMenu() {
        JPopupMenu menu = new JPopupMenu();
        addMenuItems(menu);
        return menu;
    }

    private void addMenuItems(JPopupMenu menu) {
        menu.add(createMenuItem("Mis Publicaciones", e -> control.getMyListings()));
        menu.addSeparator();
        menu.add(createMenuItem("Eliminar Usuario", e -> handleDeleteAccount()));
        menu.addSeparator();
        menu.add(createMenuItem("Cerrar Sesión", e -> setLoggedIn(false, null)));
    }

    private JMenuItem createMenuItem(String text, java.awt.event.ActionListener action) {
        JMenuItem item = new JMenuItem(text);
        item.addActionListener(action);
        return item;
    }

    private void handleDeleteAccount() {
        if (confirmDeleteAccount()) {
            control.deleteAccount();
            setLoggedIn(false, null);
        }
    }

    private boolean confirmDeleteAccount() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Estas seguro que deseas eliminar tu cuenta? perderas toda tu informacion",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }

    private JLabel createUsernameLabel() {
        JLabel label = new JLabel("User: Guest");
        configureUsernameLabel(label);
        addUsernameLabelListener(label);
        return label;
    }

    private void configureUsernameLabel(JLabel label) {
        label.setForeground(TEXT_COLOR);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void addUsernameLabelListener(JLabel label) {
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isLoggedIn) {
                    userMenu.show(label, 0, label.getHeight());
                }
            }
        });
    }

    private JPanel createAuthPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        configureAuthPanel(panel);
        addAuthButtons(panel);
        return panel;
    }

    private void configureAuthPanel(JPanel panel) {
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(
            (HEADER_HEIGHT - SEARCH_FIELD_HEIGHT) / 2, 0, 0, 20
        ));
    }

    private void addAuthButtons(JPanel panel) {
        panel.add(loginButton);
        panel.add(registerButton);
    }

    private void showLoginWindow() {
        if (loginWindow == null) {
            loginWindow = new LoginWindow(SwingUtilities.getWindowAncestor(this), control);
        }
        loginWindow.setVisible(true);
    }

    private void showRegisterWindow() {
        if (registerWindow == null) {
            registerWindow = new RegisterWindow(SwingUtilities.getWindowAncestor(this), control);
        }
        registerWindow.setVisible(true);
    }

    public void setLoggedIn(boolean loggedIn, String username) {
        isLoggedIn = loggedIn;
        updateAuthPanel(loggedIn, username);
    }

    private void updateAuthPanel(boolean loggedIn, String username) {
        authPanel.removeAll();
        if (loggedIn) {
            usernameLabel.setText("User: " + username);
            authPanel.add(usernameLabel);
        } else {
            addAuthButtons(authPanel);
        }
        authPanel.revalidate();
        authPanel.repaint();
    }

    public String getSearchText() {
        return searchField.getText();
    }

    public void addMyPostsListener(java.awt.event.ActionListener listener) {
        addMenuItemListener("Mis Publicaciones", listener);
    }

    public void addDeleteAccountListener(java.awt.event.ActionListener listener) {
        addMenuItemListener("Eliminar Usuario", listener);
    }

    public void addLogoutListener(java.awt.event.ActionListener listener) {
        addMenuItemListener("Cerrar Sesión", listener);
    }

    private void addMenuItemListener(String menuItemText, java.awt.event.ActionListener listener) {
        Component[] components = userMenu.getComponents();
        for (Component component : components) {
            if (component instanceof JMenuItem && ((JMenuItem) component).getText().equals(menuItemText)) {
                ((JMenuItem) component).addActionListener(listener);
            }
        }
    }

    public LoginWindow getLoginWindow() {
        return loginWindow;
    }

    public RegisterWindow getRegisterWindow() {
        return registerWindow;
    }
} 