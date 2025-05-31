package view;

import javax.swing.*;

import control.ClientControl;

import java.awt.*;

public class LoginWindow extends JWindow {
    private static final int WINDOW_WIDTH = 300;
    private static final int WINDOW_HEIGHT = 150;
    private static final int BORDER_PADDING = 20;
    private static final int GRID_ROWS = 2;
    private static final int GRID_COLS = 2;
    private static final int GRID_HGAP = 5;
    private static final int GRID_VGAP = 10;
    private static final int BUTTON_GAP = 10;

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private ClientControl control;

    public LoginWindow(Window owner, ClientControl control) {
        super(owner);
        this.control = control;
        initializeWindow();
    }

    private void initializeWindow() {
        setupWindowProperties();
        JPanel mainPanel = createMainPanel();
        add(mainPanel);
    }

    private void setupWindowProperties() {
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(getOwner());
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(
            BORDER_PADDING, BORDER_PADDING, BORDER_PADDING, BORDER_PADDING
        ));
        
        mainPanel.add(createFormPanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        return mainPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(GRID_ROWS, GRID_COLS, GRID_HGAP, GRID_VGAP));
        addFormFields(formPanel);
        return formPanel;
    }

    private void addFormFields(JPanel formPanel) {
        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);
        
        formPanel.add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, BUTTON_GAP, 0));
        addButtons(buttonPanel);
        return buttonPanel;
    }

    private void addButtons(JPanel buttonPanel) {
        loginButton = createLoginButton();
        cancelButton = createCancelButton();
        
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
    }

    private JButton createLoginButton() {
        JButton button = new JButton("Iniciar Sesión");
        addLoginListener(button);
        return button;
    }

    private JButton createCancelButton() {
        JButton button = new JButton("Cancelar");
        button.addActionListener(e -> dispose());
        return button;
    }

    private void addLoginListener(JButton button) {
        button.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        if (validateFields()) {
            performLogin();
        } else {
            showValidationError("Por favor, rellena los campos");
        }
    }

    private void performLogin() {
        control.login(getEmail(), getPassword());
    }

    public void showValidationError(String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "Error de Validación", 
            JOptionPane.ERROR_MESSAGE);
    }

    private boolean validateFields() {
        return !getEmail().isBlank() && !getPassword().isBlank();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }
} 