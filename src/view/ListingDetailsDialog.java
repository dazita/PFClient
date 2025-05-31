package view;

import javax.swing.*;
import java.awt.*;

public class ListingDetailsDialog extends JDialog {
    private static final int DIALOG_WIDTH = 800;
    private static final int DIALOG_HEIGHT = 600;
    private static final int IMAGE_WIDTH = 400;
    private static final int IMAGE_HEIGHT = 300;
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 20);
    private static final Font SUBTITLE_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final int PADDING = 20;
    private static final int SPACING = 10;

    private JLabel imageLabel;
    private JLabel referenceLabel;
    private JLabel vehicleTypeLabel;
    private JLabel modelLabel;
    private JLabel mileageLabel;
    private JLabel priceLabel;
    private JLabel descriptionLabel;
    private JLabel locationLabel;
    private JLabel ownerPhoneLabel;

    public ListingDetailsDialog(Frame parent) {
        super(parent, "Detalles del Vehículo", true);
        initializeDialog();
        createComponents();
        layoutComponents();
    }

    private void initializeDialog() {
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
    }

    private void createComponents() {
        createImageLabel();
        createReferenceLabel();
        createVehicleTypeLabel();
        createModelLabel();
        createMileageLabel();
        createPriceLabel();
        createDescriptionLabel();
        createLocationLabel();
        createOwnerPhoneLabel();
    }

    private void createImageLabel() {
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void createReferenceLabel() {
        referenceLabel = new JLabel();
        referenceLabel.setFont(TITLE_FONT);
    }

    private void createVehicleTypeLabel() {
        vehicleTypeLabel = new JLabel();
        vehicleTypeLabel.setFont(SUBTITLE_FONT);
    }

    private void createModelLabel() {
        modelLabel = new JLabel();
        modelLabel.setFont(NORMAL_FONT);
    }

    private void createMileageLabel() {
        mileageLabel = new JLabel();
        mileageLabel.setFont(NORMAL_FONT);
    }

    private void createPriceLabel() {
        priceLabel = new JLabel();
        priceLabel.setFont(SUBTITLE_FONT);
        priceLabel.setForeground(new Color(46, 139, 87));
    }

    private void createDescriptionLabel() {
        descriptionLabel = new JLabel();
        descriptionLabel.setFont(NORMAL_FONT);
    }

    private void createLocationLabel() {
        locationLabel = new JLabel();
        locationLabel.setFont(NORMAL_FONT);
    }

    private void createOwnerPhoneLabel() {
        ownerPhoneLabel = new JLabel();
        ownerPhoneLabel.setFont(NORMAL_FONT);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(PADDING, PADDING));
        add(createMainPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(PADDING, 0));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.add(createImagePanel(), BorderLayout.WEST);
        mainPanel.add(createInfoPanel(), BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(Color.WHITE);
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        return imagePanel;
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        addInfoComponents(infoPanel);
        return infoPanel;
    }

    private void addInfoComponents(JPanel infoPanel) {
        infoPanel.add(referenceLabel);
        infoPanel.add(Box.createVerticalStrut(SPACING));
        infoPanel.add(vehicleTypeLabel);
        infoPanel.add(Box.createVerticalStrut(SPACING));
        infoPanel.add(modelLabel);
        infoPanel.add(Box.createVerticalStrut(SPACING));
        infoPanel.add(mileageLabel);
        infoPanel.add(Box.createVerticalStrut(SPACING));
        infoPanel.add(priceLabel);
        infoPanel.add(Box.createVerticalStrut(SPACING * 2));
        infoPanel.add(descriptionLabel);
        infoPanel.add(Box.createVerticalStrut(SPACING * 2));
        infoPanel.add(locationLabel);
        infoPanel.add(Box.createVerticalStrut(SPACING));
        infoPanel.add(ownerPhoneLabel);
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(createCloseButton());
        return bottomPanel;
    }

    private JButton createCloseButton() {
        JButton closeButton = new JButton("Cerrar");
        closeButton.addActionListener(e -> dispose());
        return closeButton;
    }

    public void setListingDetails(String reference, String vehicleType, String model, 
                                int mileage, double price, Image carImage, 
                                String description, String location, int id, 
                                int ownerPhoneNumber) {
        setLabels(reference, vehicleType, model, mileage, price, description, location, ownerPhoneNumber);
        setCarImage(carImage);
    }

    private void setLabels(String reference, String vehicleType, String model, 
                          int mileage, double price, String description, 
                          String location, int ownerPhoneNumber) {
        referenceLabel.setText(reference);
        vehicleTypeLabel.setText(vehicleType);
        modelLabel.setText("Modelo: " + model);
        mileageLabel.setText(String.format("Kilometraje: %,d km", mileage));
        priceLabel.setText(String.format("$%,.2f", price));
        descriptionLabel.setText("<html><body style='width: 300px'>" + description + "</body></html>");
        locationLabel.setText("Ubicación: " + location);
        ownerPhoneLabel.setText("Teléfono del vendedor: " + ownerPhoneNumber);
    }

    private void setCarImage(Image carImage) {
        if (carImage != null) {
            Image scaledImage = carImage.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            imageLabel.setText("Sin imagen");
            imageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        }
    }
} 