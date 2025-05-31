package view;

import javax.swing.*;

import control.ClientControl;

import java.awt.*;

public class MainFrame extends JFrame {
    private static final String MAIN_PANEL = "MAIN_PANEL";
    private static final String MY_LISTINGS_PANEL = "MY_LISTINGS_PANEL";

    private HeaderPanel headerPanel;
    private MainPanel mainPanel;
    private MyListingsPanel myListingsPanel;
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private ClientControl control;

    public MainFrame(ClientControl control) {
        this.control = control;
        initializeFrame();
        initializePanels();
        setupNavigation();
        addPanelsToFrame();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("VehiculosColombia");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                control.closeConnection();
            }
        });
    }

    private void initializePanels() {
        headerPanel = new HeaderPanel(control);
        mainPanel = new MainPanel(this, control);
        myListingsPanel = new MyListingsPanel(this, control);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.add(mainPanel, MAIN_PANEL);
        contentPanel.add(myListingsPanel, MY_LISTINGS_PANEL);
    }

    private void setupNavigation() {
        headerPanel.addMyPostsListener(e -> showMyListings());
    }

    private void addPanelsToFrame() {
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    public void showMainPanel() {
        cardLayout.show(contentPanel, MAIN_PANEL);
    }

    public void showMyListings() {
        cardLayout.show(contentPanel, MY_LISTINGS_PANEL);
    }

    public void couldNotRegister() {
        headerPanel.getRegisterWindow().showValidationError("El correo electronico ya esta registrado");
    }

    public void registered() {
        control.saveEmail(headerPanel.getRegisterWindow().getEmail());
        headerPanel.setLoggedIn(true, headerPanel.getRegisterWindow().getUsername());
        headerPanel.getRegisterWindow().dispose();
    }

    public void couldNotLogIn() {
        headerPanel.getLoginWindow().showValidationError("Correo electronico o contraseña incorrectos");
    }

    public void loggedIn(String userName) {
        control.saveEmail(headerPanel.getLoginWindow().getEmail());
        headerPanel.setLoggedIn(true, userName);
        headerPanel.getLoginWindow().dispose();
    }

    public MainPanel getMainPanel() {
        return mainPanel;
    }

    public MyListingsPanel getMyListingsPanel() {
        return myListingsPanel;
    }
}
