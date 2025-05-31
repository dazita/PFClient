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
        setupPanelProperties();
        addTitleSection();
        addFilterSections();
        addButtonSection();
    }

    private void setupPanelProperties() {
        setBackground(new Color(220, 220, 220));
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    private void addTitleSection() {
        JLabel titleLabel = createTitleLabel();
        add(Box.createVerticalStrut(20));
        add(titleLabel);
        add(Box.createVerticalStrut(30));
    }

    private JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("Filtros");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return titleLabel;
    }

    private void addFilterSections() {
        addPriceFilterSection();
        add(Box.createVerticalStrut(20));
        addModelFilterSection();
        add(Box.createVerticalGlue());
    }

    private void addPriceFilterSection() {
        add(createFilterSection(
            "Rango de Precio",
            minPriceField,
            maxPriceField,
            priceFilterCheck
        ));
    }

    private void addModelFilterSection() {
        add(createFilterSection(
            "Rango de Modelo",
            minModelField,
            maxModelField,
            modelFilterCheck
        ));
    }

    private void addButtonSection() {
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel);
        add(Box.createVerticalStrut(20));
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(220, 220, 220));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(filterButton);
        return buttonPanel;
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
        JPanel panel = createBasePanel();
        panel.add(createHeaderPanel(checkBox));
        panel.add(createFieldsPanel(minField, maxField));
        return panel;
    }

    private JPanel createBasePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(220, 220, 220));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return panel;
    }

    private JPanel createHeaderPanel(JCheckBox checkBox) {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(220, 220, 220));
        headerPanel.add(checkBox);
        return headerPanel;
    }

    private JPanel createFieldsPanel(JTextField minField, JTextField maxField) {
        JPanel fieldsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        fieldsPanel.setBackground(new Color(220, 220, 220));
        
        JLabel minLabel = createLabel("Min:");
        JLabel maxLabel = createLabel("Max:");
        
        fieldsPanel.add(minLabel);
        fieldsPanel.add(minField);
        fieldsPanel.add(maxLabel);
        fieldsPanel.add(maxField);
        
        return fieldsPanel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FIELD_FONT);
        return label;
    }

    private JButton createFilterButton() {
        JButton button = new JButton("Filtrar");
        configureButtonSize(button);
        styleButton(button);
        addHoverEffect(button);
        return button;
    }

    private void configureButtonSize(JButton button) {
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
    }

    private void styleButton(JButton button) {
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void addHoverEffect(JButton button) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_COLOR.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_COLOR);
            }
        });
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