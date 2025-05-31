package view;

import javax.swing.*;

import control.ClientControl;

import java.awt.*;

public class LoginWindow extends JWindow {
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
        setSize(300, 150);
        setLocationRelativeTo(getOwner());
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 5, 10));
        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);
        formPanel.add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        loginButton = new JButton("Iniciar Sesión");
        addLoginListener();
        cancelButton = new JButton("Cancelar");
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        cancelButton.addActionListener(e -> dispose());
    }

    public void addLoginListener() {
        loginButton.addActionListener(e -> {
            if (validateFields()) {
                control.login(
                    getEmail(),
                    getPassword()
                );
            } else {
                showValidationError("Por favor, rellena los campos");
            }
        });
    }

    public void showValidationError(String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "Error de Validación", 
            JOptionPane.ERROR_MESSAGE);
    }

    private boolean validateFields(){
        if (getEmail().isBlank() || getPassword().isBlank()) {
            return false;
        } else {
            return true;
        }
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }
} 