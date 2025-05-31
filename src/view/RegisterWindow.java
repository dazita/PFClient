package view;

import javax.swing.*;

import control.ClientControl;

import java.awt.*;

public class RegisterWindow extends JDialog {
    private static final int FIELD_WIDTH = 20;
    private static final int PADDING = 20;
    private static final int FIELD_SPACING = 5;
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JTextField locationField;
    private JTextField phoneField;
    private JButton registerButton;
    private JButton cancelButton;
    private ClientControl control;

    public RegisterWindow(Window parent, ClientControl control) {
        super(parent, "Registro", ModalityType.APPLICATION_MODAL);
        this.control = control;
        initializeWindow();
        createComponents();
        layoutComponents();
        setupListeners();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initializeWindow() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private void createComponents() {
        createTextFields();
        createButtons();
    }

    private void createTextFields() {
        usernameField = new JTextField(FIELD_WIDTH);
        passwordField = new JPasswordField(FIELD_WIDTH);
        confirmPasswordField = new JPasswordField(FIELD_WIDTH);
        emailField = new JTextField(FIELD_WIDTH);
        locationField = new JTextField(FIELD_WIDTH);
        phoneField = new JTextField(FIELD_WIDTH);
    }

    private void createButtons() {
        registerButton = new JButton("Registrarse");
        cancelButton = new JButton("Cancelar");
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(FIELD_SPACING, FIELD_SPACING));
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(FIELD_SPACING, FIELD_SPACING));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        
        mainPanel.add(createFormPanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        return mainPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = createGridBagConstraints();
        
        addFormFields(formPanel, gbc);
        
        return formPanel;
    }

    private GridBagConstraints createGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(FIELD_SPACING, FIELD_SPACING, FIELD_SPACING, FIELD_SPACING);
        return gbc;
    }

    private void addFormFields(JPanel panel, GridBagConstraints gbc) {
        addFormField(panel, gbc, "Nombre de Usuario:", usernameField, 0);
        addFormField(panel, gbc, "Contraseña:", passwordField, 1);
        addFormField(panel, gbc, "Confirmar Contraseña:", confirmPasswordField, 2);
        addFormField(panel, gbc, "Correo Electrónico:", emailField, 3);
        addFormField(panel, gbc, "Ubicación:", locationField, 4);
        addFormField(panel, gbc, "Número de Teléfono:", phoneField, 5);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(labelText), gbc);
        
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, FIELD_SPACING, 0));
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        return buttonPanel;
    }

    private void setupListeners() {
        registerButton.addActionListener(e -> {
            if (validateFields()) {
                control.register(
                    getUsername(),
                    getPassword(),
                    getEmail(),
                    getUserLocation(),
                    getPhone()
                );
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
    }

    private boolean validateFields() {
        if (!validateRequiredFields()) return false;
        if (!validatePasswords()) return false;
        if (!validateEmail()) return false;
        return true;
    }

    private boolean validateRequiredFields() {
        if (isAnyFieldEmpty()) {
            showValidationError("Por favor complete todos los campos");
            return false;
        }
        return true;
    }

    private boolean isAnyFieldEmpty() {
        return usernameField.getText().trim().isEmpty() ||
               new String(passwordField.getPassword()).isEmpty() ||
               new String(confirmPasswordField.getPassword()).isEmpty() ||
               emailField.getText().trim().isEmpty() ||
               locationField.getText().trim().isEmpty() ||
               phoneField.getText().trim().isEmpty();
    }

    private boolean validatePasswords() {
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (!password.equals(confirmPassword)) {
            showValidationError("Las contraseñas no coinciden");
            return false;
        }
        return true;
    }

    private boolean validateEmail() {
        String email = emailField.getText().trim();
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            showValidationError("Por favor ingrese un correo electrónico válido");
            return false;
        }
        return true;
    }

    public void showValidationError(String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "Error de Validación", 
            JOptionPane.ERROR_MESSAGE);
    }

    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public String getEmail() {
        return emailField.getText().trim();
    }

    public String getUserLocation() {
        return locationField.getText().trim();
    }

    public String getPhone() {
        return phoneField.getText().trim();
    }
} 