package view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import control.ClientControl;

public class MyListingsPanel extends JPanel {
    
    private static final int LISTING_WIDTH = 400;
    private static final int LISTING_HEIGHT = 250;
    private static final Color BUTTON_COLOR = new Color(46, 139, 87);
    private static final Color DELETE_BUTTON_COLOR = new Color(220, 53, 69);
    private static final Color BORDER_COLOR = new Color(200, 200, 200);
    private static final int PADDING = 10;
    private static final int SPACING = 5;
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    private static final int MIN_COLUMNS = 2;
    private static final int IMAGE_HEIGHT = 150;
    private static final int IMAGE_WIDTH = 200;

    private final MainFrame mainFrame;
    private final JPanel listingsPanel;
    private final JScrollPane scrollPane;
    private ClientControl control;

    public MyListingsPanel(MainFrame mainFrame, ClientControl control) {
        this.control = control;
        this.mainFrame = mainFrame;
        this.listingsPanel = createListingsPanel();
        this.scrollPane = createScrollPane();
        initializePanel();
    }

    private void initializePanel() {
        setupPanelProperties();
        addTopPanel();
        addScrollPane();
        addBottomPanel();
    }

    private void setupPanelProperties() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
    }

    private void addTopPanel() {
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(createBackButton());
        return topPanel;
    }

    private JButton createBackButton() {
        JButton backButton = new JButton("← Volver");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> mainFrame.showMainPanel());
        return backButton;
    }

    private void addScrollPane() {
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addBottomPanel() {
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        bottomPanel.add(createAddListingButton());
        return bottomPanel;
    }

    private JButton createAddListingButton() {
        JButton button = new JButton("Añadir Publicación");
        configureAddListingButton(button);
        button.addActionListener(e -> showAddListingDialog());
        return button;
    }

    private void configureAddListingButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void showAddListingDialog() {
        AddListingDialog dialog = new AddListingDialog((Frame)SwingUtilities.getWindowAncestor(this), control);
        dialog.setVisible(true);
    }

    private JPanel createListingsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        return panel;
    }

    private JScrollPane createScrollPane() {
        JScrollPane scrollPane = new JScrollPane(listingsPanel);
        configureScrollPane(scrollPane);
        return scrollPane;
    }

    private void configureScrollPane(JScrollPane scrollPane) {
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }

    public void addListing(String reference, double price, int year, int km, String location, String id, Image image) {
        JPanel listingPanel = createListingPanel(reference, price, year, km, location, id, image);
        addListingToGrid(listingPanel);
    }

    private JPanel createListingPanel(String reference, double price, int year, int km, String location, String id, Image image) {
        JPanel panel = createBasePanel();
        panel.add(createImagePanel(image), BorderLayout.WEST);
        panel.add(createInfoPanel(reference, price, year, km, location, id), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBasePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(createPanelBorder());
        panel.setPreferredSize(new Dimension(LISTING_WIDTH, LISTING_HEIGHT));
        return panel;
    }

    private JPanel createInfoPanel(String reference, double price, int year, int km, String location, String id) {
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.add(createContentPanel(reference, price, year, km, location), BorderLayout.CENTER);
        infoPanel.add(createButtonPanel(id), BorderLayout.SOUTH);
        return infoPanel;
    }

    private JPanel createImagePanel(Image image) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        
        if (image != null) {
            addImageToPanel(panel, image);
        } else {
            addPlaceholderToPanel(panel);
        }
        
        return panel;
    }

    private void addImageToPanel(JPanel panel, Image image) {
        Image scaledImage = image.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(imageLabel);
    }

    private void addPlaceholderToPanel(JPanel panel) {
        JLabel placeholderLabel = new JLabel("Sin imagen");
        placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        placeholderLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        panel.add(placeholderLabel);
    }

    private Border createPanelBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING)
        );
    }

    private JPanel createContentPanel(String reference, double price, int year, int km, String location) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        addContentLabels(panel, reference, price, year, km, location);
        return panel;
    }

    private void addContentLabels(JPanel panel, String reference, double price, int year, int km, String location) {
        addLabel(panel, reference, TITLE_FONT);
        addLabel(panel, String.format("Precio: $%,.2f", price), NORMAL_FONT);
        addLabel(panel, "Año: " + year, NORMAL_FONT);
        addLabel(panel, String.format("Kilometraje: %,d km", km), NORMAL_FONT);
        addLabel(panel, "Ubicación: " + location, NORMAL_FONT);
    }

    private void addLabel(JPanel panel, String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(SPACING));
    }

    private JPanel createButtonPanel(String id) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        JButton viewMoreButton = createViewMoreButton(id);
        JButton deleteButton = createDeleteButton(id);
        
        configureButtonAlignment(viewMoreButton, deleteButton);
        addButtonsToPanel(panel, viewMoreButton, deleteButton);
        
        return panel;
    }

    private void configureButtonAlignment(JButton viewMoreButton, JButton deleteButton) {
        viewMoreButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        deleteButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
    }

    private void addButtonsToPanel(JPanel panel, JButton viewMoreButton, JButton deleteButton) {
        panel.add(viewMoreButton);
        panel.add(Box.createVerticalStrut(5));
        panel.add(deleteButton);
    }

    private JButton createViewMoreButton(String id) {
        JButton button = new JButton("Ver más");
        configureViewMoreButton(button, id);
        return button;
    }

    private void configureViewMoreButton(JButton button, String id) {
        button.setActionCommand(id);
        button.setPreferredSize(new Dimension(120, 30));
        button.setMaximumSize(new Dimension(120, 30));
        styleViewMoreButton(button);
        addButtonHoverEffect(button, BUTTON_COLOR);
        button.addActionListener(e -> handleViewMoreClick(id));
    }

    private void handleViewMoreClick(String id) {
        try {
            Object[] listingInfo = control.getListingFullInfo(id);
            if (listingInfo != null) {
                ListingDetailsDialog dialog = new ListingDetailsDialog((Frame)SwingUtilities.getWindowAncestor(this));
                dialog.setListingDetails(
                    (String)listingInfo[0],  // reference
                    (String)listingInfo[1],  // vehicleType
                    (String)listingInfo[2],  // model
                    (int)listingInfo[3],     // mileage
                    (double)listingInfo[4],  // price
                    (Image)listingInfo[5],   // carImage
                    (String)listingInfo[6],  // description
                    (String)listingInfo[7],  // location
                    (int)listingInfo[8],     // id
                    (int)listingInfo[9]      // ownerPhoneNumber
                );
                dialog.setVisible(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al obtener los detalles del vehículo",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleViewMoreButton(JButton button) {
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JButton createDeleteButton(String id) {
        JButton button = new JButton("Eliminar");
        configureDeleteButton(button, id);
        return button;
    }

    private void configureDeleteButton(JButton button, String id) {
        button.setActionCommand(id);
        button.setPreferredSize(new Dimension(120, 30));
        button.setMaximumSize(new Dimension(120, 30));
        styleDeleteButton(button);
        addButtonHoverEffect(button, DELETE_BUTTON_COLOR);
        button.addActionListener(e -> handleDeleteClick(id));
    }

    private void handleDeleteClick(String id) {
        control.deleteListing(id);
        control.getMyListings();
    }

    private void styleDeleteButton(JButton button) {
        button.setBackground(DELETE_BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void addButtonHoverEffect(JButton button, Color baseColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
            }
        });
    }

    private void addListingToGrid(JPanel listingPanel) {
        GridBagConstraints gbc = calculateGridPosition();
        gbc.insets = new Insets(PADDING, PADDING, PADDING, PADDING);
        listingsPanel.add(listingPanel, gbc);
        listingsPanel.revalidate();
        listingsPanel.repaint();
    }

    private GridBagConstraints calculateGridPosition() {
        int count = listingsPanel.getComponentCount();
        int columns = calculateColumns();
        int row = count / columns;
        int col = count % columns;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = col;
        gbc.gridy = row;
        return gbc;
    }

    private int calculateColumns() {
        int availableWidth = getWidth();
        int columns = availableWidth / (LISTING_WIDTH + PADDING * 2);
        return Math.max(MIN_COLUMNS, columns);
    }

    public void clearListings() {
        listingsPanel.removeAll();
        listingsPanel.revalidate();
        listingsPanel.repaint();
    }
} 