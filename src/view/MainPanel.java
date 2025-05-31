package view;

import javax.swing.*;
import javax.swing.border.Border;
import control.ClientControl;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainPanel extends JPanel {
    // Constants
    private static final int LISTING_WIDTH = 400;
    private static final int LISTING_HEIGHT = 250;
    private static final Color BUTTON_COLOR = new Color(46, 139, 87);
    private static final Color BORDER_COLOR = new Color(200, 200, 200);
    private static final int PADDING = 10;
    private static final int SPACING = 5;
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);
    private static final int MIN_COLUMNS = 2;
    private static final int ASIDE_WIDTH = 250; // Ancho fijo del panel lateral
    private static final int IMAGE_HEIGHT = 150;
    private static final int IMAGE_WIDTH = 200;

    // Components
    private final AsidePanel asidePanel;
    private final JPanel listingsPanel;
    private final JScrollPane scrollPane;

    //TO-DO: cada que el mainPanel se inicialice, pedir 10 listing existentes
    public MainPanel(MainFrame mainFrame, ClientControl control) {
        this.asidePanel = new AsidePanel(control);
        this.listingsPanel = createListingsPanel();
        this.scrollPane = createScrollPane();
        initializePanel();
    }

    private void initializePanel() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(asidePanel, BorderLayout.EAST);
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
            // Placeholder si no hay imagen
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

        addLabel(panel, "Referencia: " + reference, TITLE_FONT);
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
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(Color.WHITE);
        panel.add(createSaberMasButton(id));
        return panel;
    }

    private JButton createSaberMasButton(String id) {
        JButton button = new JButton("Saber Más");
        button.setActionCommand(id);
        styleButton(button);
        addButtonHoverEffect(button);
        
        // Agregar ActionListener que imprime el ID
        button.addActionListener(e -> {
            System.out.println("ID del vehículo: " + id);
        });
        
        return button;
    }

    private void styleButton(JButton button) {
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void addButtonHoverEffect(JButton button) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_COLOR.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_COLOR);
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
        // Obtener el ancho disponible restando el ancho del panel lateral
        int availableWidth = getWidth() - ASIDE_WIDTH;
        
        // Calcular cuántas columnas caben en el espacio disponible
        int columns = availableWidth / (LISTING_WIDTH + PADDING * 2);
        
        // Asegurar al menos MIN_COLUMNS columnas
        return Math.max(MIN_COLUMNS, columns);
    }

    public void clearListings() {
        listingsPanel.removeAll();
        listingsPanel.revalidate();
        listingsPanel.repaint();
    }

    public AsidePanel getAsidePanel() {
        return asidePanel;
    }
} 