package view;

import javax.swing.*;

import control.ClientControl;

import java.awt.*;

public class AsidePanel extends JPanel {
    private static final int PANEL_WIDTH = 250;
    private static final int PANEL_HEIGHT = 720;
    private static final int FIELD_WIDTH = 100;
    private static final int FIELD_HEIGHT = 25;
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 14);
    private static final Font FIELD_FONT = new Font("Arial", Font.PLAIN, 12);
    private static final Color BUTTON_COLOR = new Color(70, 130, 180);
    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 35;
    
    private final JTextField minPriceField;
    private final JTextField maxPriceField;
    private final JTextField minModelField;
    private final JTextField maxModelField;
    private final JCheckBox priceFilterCheck;
    private final JCheckBox modelFilterCheck;
    private final JButton filterButton;
    private ClientControl control;
    
    public AsidePanel(ClientControl control) {
        minPriceField = createTextField();
        maxPriceField = createTextField();
        minModelField = createTextField();
        maxModelField = createTextField();
        priceFilterCheck = createCheckBox("Filtrar por Precio");
        modelFilterCheck = createCheckBox("Filtrar por Modelo");
        filterButton = createFilterButton();
        this.control = control;
        
        initializePanel();
        setupFilterListeners();
    }

    private void initializePanel() {
        setBackground(new Color(220, 220, 220));
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        // Título
        JLabel titleLabel = new JLabel("Filtros");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(20));
        add(titleLabel);
        add(Box.createVerticalStrut(30));
        
        // Filtro de Precio
        add(createFilterSection(
            "Rango de Precio",
            minPriceField,
            maxPriceField,
            priceFilterCheck
        ));
        
        add(Box.createVerticalStrut(20));
        
        // Filtro de Modelo
        add(createFilterSection(
            "Rango de Modelo",
            minModelField,
            maxModelField,
            modelFilterCheck
        ));
        
        // Espacio flexible para empujar el botón hacia abajo
        add(Box.createVerticalGlue());
        
        // Panel para el botón
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(220, 220, 220));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(filterButton);
        
        add(buttonPanel);
        add(Box.createVerticalStrut(20));
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        field.setMaximumSize(new Dimension(FIELD_WIDTH, FIELD_HEIGHT));
        field.setFont(FIELD_FONT);
        return field;
    }

    private JCheckBox createCheckBox(String text) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setFont(LABEL_FONT);
        checkBox.setBackground(new Color(220, 220, 220));
        return checkBox;
    }

    private JPanel createFilterSection(String title, JTextField minField, JTextField maxField, JCheckBox checkBox) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(220, 220, 220));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Título y checkbox
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(220, 220, 220));
        headerPanel.add(checkBox);
        panel.add(headerPanel);
        
        // Campos de rango
        JPanel fieldsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        fieldsPanel.setBackground(new Color(220, 220, 220));
        
        JLabel minLabel = new JLabel("Min:");
        minLabel.setFont(FIELD_FONT);
        JLabel maxLabel = new JLabel("Max:");
        maxLabel.setFont(FIELD_FONT);
        
        fieldsPanel.add(minLabel);
        fieldsPanel.add(minField);
        fieldsPanel.add(maxLabel);
        fieldsPanel.add(maxField);
        
        panel.add(fieldsPanel);
        
        return panel;
    }

    private JButton createFilterButton() {
        JButton button = new JButton("Filtrar");
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_COLOR.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_COLOR);
            }
        });
        
        return button;
    }

    private void setupFilterListeners() {
        priceFilterCheck.addActionListener(e -> {
            if (priceFilterCheck.isSelected()) {
                modelFilterCheck.setSelected(false);
            }
        });
        
        modelFilterCheck.addActionListener(e -> {
            if (modelFilterCheck.isSelected()) {
                priceFilterCheck.setSelected(false);
            }
        });

        filterButton.addActionListener(e -> {
            if (priceFilterCheck.isSelected()) {
                control.filterByPrice(Integer.valueOf(maxPriceField.getText()), Integer.valueOf(minPriceField.getText()));
            } else if (modelFilterCheck.isSelected()) {
                control.filterByModel(Integer.valueOf(maxModelField.getText()), Integer.valueOf(minModelField.getText()));
            }
        });
    }

    // Getters para los valores de los filtros
    public Double getMinPrice() {
        return getDoubleValue(minPriceField);
    }

    public Double getMaxPrice() {
        return getDoubleValue(maxPriceField);
    }

    public Integer getMinModel() {
        return getIntegerValue(minModelField);
    }

    public Integer getMaxModel() {
        return getIntegerValue(maxModelField);
    }

    public boolean isPriceFilterActive() {
        return priceFilterCheck.isSelected();
    }

    public boolean isModelFilterActive() {
        return modelFilterCheck.isSelected();
    }

    private Double getDoubleValue(JTextField field) {
        try {
            String text = field.getText().trim();
            if (text.isEmpty()) return null;
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer getIntegerValue(JTextField field) {
        try {
            String text = field.getText().trim();
            if (text.isEmpty()) return null;
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void addFilterActionListener(java.awt.event.ActionListener listener) {
        filterButton.addActionListener(listener);
    }
} 