package view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import control.ClientControl;

//TO-DO:refactorizar y eliminar comments
public class MyListingsPanel extends JPanel {
    // Constants
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

    //TO-DO: cuando se inicialice, pedir los myListings al server
    public MyListingsPanel(MainFrame mainFrame, ClientControl control) {
        this.control = control;
        this.mainFrame = mainFrame;
        this.listingsPanel = createListingsPanel();
        this.scrollPane = createScrollPane();
        initializePanel();
    }

    private void initializePanel() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        
        // Create back button panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        
        JButton backButton = new JButton("← Volver");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> mainFrame.showMainPanel());
        
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Add bottom panel with "Add Listing" button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JButton addListingButton = new JButton("Añadir Publicación");
        addListingButton.setFont(new Font("Arial", Font.BOLD, 14));
        addListingButton.setBackground(BUTTON_COLOR);
        addListingButton.setForeground(Color.WHITE);
        addListingButton.setFocusPainted(false);
        addListingButton.setBorderPainted(false);
        addListingButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addListingButton.addActionListener(e -> {
            AddListingDialog dialog = new AddListingDialog((Frame)SwingUtilities.getWindowAncestor(this), control);
            dialog.setVisible(true);
        });

        bottomPanel.add(addListingButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createListingsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        return panel;
    }

    private JScrollPane createScrollPane() {
        JScrollPane scrollPane = new JScrollPane(listingsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    public void addListing(String reference, double price, int year, int km, String location, String id, Image image) {
        JPanel listingPanel = createListingPanel(reference, price, year, km, location, id, image);
        addListingToGrid(listingPanel);
    }

    private JPanel createListingPanel(String reference, double price, int year, int km, String location, String id, Image image) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(createPanelBorder());
        panel.setPreferredSize(new Dimension(LISTING_WIDTH, LISTING_HEIGHT));

        // Panel izquierdo para la imagen
        JPanel imagePanel = createImagePanel(image);
        panel.add(imagePanel, BorderLayout.WEST);

        // Panel derecho para la información
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.add(createContentPanel(reference, price, year, km, location), BorderLayout.CENTER);
        infoPanel.add(createButtonPanel(id), BorderLayout.SOUTH);
        panel.add(infoPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createImagePanel(Image image) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        
        if (image != null) {
            Image scaledImage = image.getScaledInstance(
                IMAGE_WIDTH, 
                IMAGE_HEIGHT, 
                Image.SCALE_SMOOTH
            );
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(imageLabel);
        } else {
            JLabel placeholderLabel = new JLabel("Sin imagen");
            placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
            placeholderLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            panel.add(placeholderLabel);
        }
        
        return panel;
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

        addLabel(panel, reference, TITLE_FONT);
        addLabel(panel, String.format("Precio: $%,.2f", price), NORMAL_FONT);
        addLabel(panel, "Año: " + year, NORMAL_FONT);
        addLabel(panel, String.format("Kilometraje: %,d km", km), NORMAL_FONT);
        addLabel(panel, "Ubicación: " + location, NORMAL_FONT);

        return panel;
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
        
        // Alinear botones a la derecha
        viewMoreButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        deleteButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        panel.add(viewMoreButton);
        panel.add(Box.createVerticalStrut(5)); // Espacio entre botones
        panel.add(deleteButton);
        
        return panel;
    }

    //TO-DO: crear el actionListener e iniciar una nueva pantalla
    private JButton createViewMoreButton(String id) {
        JButton button = new JButton("Ver más");
        button.setActionCommand(id);
        button.setPreferredSize(new Dimension(120, 30));
        button.setMaximumSize(new Dimension(120, 30));
        styleViewMoreButton(button);
        addButtonHoverEffect(button, BUTTON_COLOR);
        
        button.addActionListener(e -> {
            System.out.println("Ver más detalles de publicación con ID: " + id);
        });
        
        return button;
    }

    private void styleViewMoreButton(JButton button) {
        button.setBackground(BUTTON_COLOR);
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

    //TO-DO: crear el actionListener e iniciar una nueva pantalla
    private JButton createDeleteButton(String id) {
        JButton button = new JButton("Eliminar");
        button.setActionCommand(id);
        button.setPreferredSize(new Dimension(120, 30));
        button.setMaximumSize(new Dimension(120, 30));
        styleDeleteButton(button);
        addButtonHoverEffect(button, DELETE_BUTTON_COLOR);
        
        button.addActionListener(e -> {
            control.deleteListing(id);
            control.getMyListings();
        });
        
        return button;
    }

    private void styleDeleteButton(JButton button) {
        button.setBackground(DELETE_BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
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