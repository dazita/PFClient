package view;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import control.ClientControl;

import java.awt.*;
import java.io.File;
import java.util.List;

public class AddListingDialog extends JDialog {
    private static final String[] VEHICLE_TYPES = {"Carro", "Moto", "Camioneta", "Camión", "Bus", "Otro"};
    private static final Color BUTTON_COLOR = new Color(46, 139, 87);
    
    private JComboBox<String> brandField;
    private JComboBox<String> lineField;
    private JComboBox<String> modelField;
    private JTextField mileageField;
    private JTextArea descriptionArea;
    private JTextField priceField;
    private JTextField locationField;
    private JComboBox<String> vehicleTypeCombo;
    private JLabel imageLabel;
    private File selectedImage;
    private ClientControl control;

    public AddListingDialog(Frame parent, ClientControl control) {
        super(parent, "Nueva Publicación", true);
        this.control = control;
        initializeFields();
        initializeDialog();
    }

    private void initializeFields(){
        initializeCombos();
        mileageField = new JTextField(20);
        descriptionArea = new JTextArea(3, 20);
        priceField = new JTextField(20);
        locationField = new JTextField(20);
        imageLabel = new JLabel("No se ha seleccionado imagen");
    }

    private void initializeCombos(){
        brandField = new JComboBox<>();
        lineField = new JComboBox<>();
        modelField = new JComboBox<>();
        loadBrands();
        addBrandFieldListener();
        addLineFieldListener();
        vehicleTypeCombo = new JComboBox<>(VEHICLE_TYPES);
    }

    private void addBrandFieldListener(){
        brandField.addActionListener(e -> {
            String selectedBrand = (String) brandField.getSelectedItem();
            if (selectedBrand != null) {
                loadLines(selectedBrand);
            }
        });
    }

    private void addLineFieldListener(){
        lineField.addActionListener(e -> {
            String selectedBrand = (String) brandField.getSelectedItem();
            String selectedLine = (String) lineField.getSelectedItem();
            if (selectedLine != null) {
                loadModels(selectedBrand, selectedLine);
            }
        });
    }

    private void loadBrands() {
        brandField.removeAllItems();
        List<String> brands = control.getBrands();
        for (String brand : brands) {
            brandField.addItem(brand);
        }
    }

    private void loadLines(String brand) {
        lineField.removeAllItems();
        List<String> lines = control.getLines(brand);
        for (String line : lines) {
            lineField.addItem(line);
        }
        modelField.removeAllItems();
    }

    private void loadModels(String brand, String line) {
        modelField.removeAllItems();
        List<String> models = control.getModels(brand, line);
        for (String model : models) {
            modelField.addItem(model);
        }
    }

    private void initializeDialog() {
        setLayout(new BorderLayout());
        setSize(500, 600);
        setLocationRelativeTo(getParent());
        JPanel formPanel = createFormPanel();
        JPanel buttonPanel = createButtonPanel();
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel panel = createBaseFormPanel();
        addVehicleFields(panel);
        addImageSection(panel);
        return panel;
    }

    private JPanel createBaseFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return panel;
    }

    private void addVehicleFields(JPanel panel) {
        addFormField(panel, "Marca:", brandField);
        addFormField(panel, "Línea:", lineField);
        addFormField(panel, "Modelo:", modelField);
        addFormField(panel, "Kilometraje:", mileageField);
        addFormField(panel, "Descripción:", new JScrollPane(descriptionArea));
        addFormField(panel, "Precio:", priceField);
        addFormField(panel, "Ubicación:", locationField);
        addFormField(panel, "Tipo de Vehículo:", vehicleTypeCombo);
    }

    private void addImageSection(JPanel panel) {
        JButton selectImageButton = createImageButton();
        panel.add(Box.createVerticalStrut(10));
        panel.add(selectImageButton);
        panel.add(Box.createVerticalStrut(5));
        panel.add(imageLabel);
        panel.add(Box.createVerticalStrut(20));
    }

    private JButton createImageButton() {
        JButton button = createStyledButton("Seleccionar Imagen");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> selectImage());
        return button;
    }

    private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedImage = fileChooser.getSelectedFile();
            imageLabel.setText(selectedImage.getName());
        }
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.add(createCancelButton());
        panel.add(createSaveButton());
        return panel;
    }

    private JButton createCancelButton() {
        JButton button = new JButton("Cancelar");
        button.addActionListener(e -> dispose());
        return button;
    }

    private JButton createSaveButton() {
        JButton button = new JButton("Guardar");
        styleSaveButton(button);
        button.addActionListener(e -> saveListing());
        return button;
    }

    private void styleSaveButton(JButton button) {
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void addFormField(JPanel panel, String label, JComponent field) {
        JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fieldPanel.setBackground(Color.WHITE);
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setPreferredSize(new Dimension(100, 25));
        
        fieldPanel.add(labelComponent);
        fieldPanel.add(field);
        
        panel.add(fieldPanel);
        panel.add(Box.createVerticalStrut(10));
    }

    private void saveListing() {
        if (validateFields()) {
            control.createListing(
                (String) brandField.getSelectedItem(),
                (String) lineField.getSelectedItem(),
                (String) modelField.getSelectedItem(),
                mileageField.getText(),
                descriptionArea.getText(),
                priceField.getText(),
                locationField.getText(),
                (String) vehicleTypeCombo.getSelectedItem(),
                selectedImage
            );
            control.getMyListings();
            dispose();
        }
    }

    private boolean validateFields() {
        if (brandField.getSelectedItem() == null || 
            lineField.getSelectedItem() == null || 
            modelField.getSelectedItem() == null || 
            mileageField.getText().isEmpty() ||
            priceField.getText().isEmpty() ||
            locationField.getText().isEmpty()) {
            
            JOptionPane.showMessageDialog(this,
                "Por favor complete todos los campos requeridos",
                "Error de validación",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public String getBrand() { return (String) brandField.getSelectedItem(); }
    public String getLine() { return (String) lineField.getSelectedItem(); }
    public String getModel() { return (String) modelField.getSelectedItem(); }
    public String getMileage() { return mileageField.getText(); }
    public String getDescription() { return descriptionArea.getText(); }
    public String getPrice() { return priceField.getText(); }
    public String getLocationText() { return locationField.getText(); }
    public String getVehicleType() { return (String) vehicleTypeCombo.getSelectedItem(); }
    public File getSelectedImage() { return selectedImage; }
} 