import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

public class Client extends JComponent implements Runnable{
    private Client client;
    private boolean loggedIn;
    private String status = "";

    private JPanel topBar, userBar;
    private JScrollPane scrollPane;

    private JButton login, signup, logout;
    private JButton viewMarket, viewDashboard;
    private JButton viewShoppingCart, viewPurchaseHistory;
    private JButton viewStores, viewProducts, addStore, removeStore, editStore, addProduct, removeProduct, editProduct;
    private ArrayList<JButton> productButtons = new ArrayList<>();

    private JTextField username;
    private JTextField password;

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == login) {
                // log in condition
                loggedIn = true;
                login.setVisible(false);
                signup.setVisible(false);
                logout.setVisible(true);
                userBar.setVisible(true);
                viewDashboard.setVisible(true);
                status = "Seller";
                if (status.equals("Buyer")) {
                    viewShoppingCart.setVisible(true);
                    viewPurchaseHistory.setVisible(true);
                } else if (status.equals("Seller")) {
                    viewStores.setVisible(true);
                    viewProducts.setVisible(true);
                    addStore.setVisible(true);
                    removeStore.setVisible(true);
                    editStore.setVisible(true);
                    addProduct.setVisible(true);
                    removeProduct.setVisible(true);
                    editProduct.setVisible(true);
                }
            } else if (e.getSource() == signup) {
                // sign up condition
                loggedIn = true;
                login.setVisible(false);
                signup.setVisible(false);
                logout.setVisible(true);
                userBar.setVisible(true);
                viewDashboard.setVisible(true);
                if (status.equals("Buyer")) {
                    viewShoppingCart.setVisible(true);
                    viewPurchaseHistory.setVisible(true);
                } else if (status.equals("Seller")) {
                    viewStores.setVisible(true);
                    viewProducts.setVisible(true);
                    addStore.setVisible(true);
                    removeStore.setVisible(true);
                    editStore.setVisible(true);
                    addProduct.setVisible(true);
                    removeProduct.setVisible(true);
                    editProduct.setVisible(true);
                }
            } else if (e.getSource() == logout) {
                // logout conditions
                loggedIn = false;
                login.setVisible(true);
                signup.setVisible(true);
                logout.setVisible(false);
                status = "";
                userBar.setVisible(false);
                viewDashboard.setVisible(false);

                viewStores.setVisible(false);
                viewProducts.setVisible(false);
                addStore.setVisible(false);
                removeStore.setVisible(false);
                editStore.setVisible(false);
                addProduct.setVisible(false);
                removeProduct.setVisible(false);
                editProduct.setVisible(false);

                viewShoppingCart.setVisible(false);
                viewPurchaseHistory.setVisible(false);
            }
        }
    };

    public void sellerPage(JFrame frame) {
        JPanel sellerBar = new JPanel();
        sellerBar.setBackground(Color.DARK_GRAY);
        sellerBar.setLayout(new FlowLayout());
        viewStores = new JButton("View Stores");
        viewProducts = new JButton("View Products");
        addStore = new JButton("Add Store");
        removeStore = new JButton("Remove Store");
        editStore = new JButton("Edit Store");
        addProduct = new JButton("Add Product");
        removeProduct = new JButton("Remove Product");
        editProduct = new JButton("Edit Product");
        sellerBar.add(viewStores);

    }


    public void run() {
        JFrame frame = new JFrame();
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        Container container = frame.getContentPane();
        container.setLayout(new BorderLayout());
        username = new JTextField("Username", 20);
        password = new JTextField("Password", 20);
        username.addFocusListener(new FocusListener() { // Creates default text
            @Override
            public void focusGained(FocusEvent e) {
                if (username.getText().equals("Username")) {
                    username.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (username.getText().equals("")) {
                    username.setText("Username");
                }
            }
        });
        password.addFocusListener(new FocusListener() { // Creates default text
            @Override
            public void focusGained(FocusEvent e) {
                if (password.getText().equals("Password")) {
                    password.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (password.getText().equals("")) {
                    password.setText("Password");
                }
            }
        });

        topBar = new JPanel();
        userBar = new JPanel();
        topBar.setBackground(Color.DARK_GRAY);
        userBar.setBackground(Color.gray);
        topBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        userBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        topBar.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        userBar.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        userBar.setVisible(false);
        login = new JButton("Login");
        login.addActionListener(actionListener);
        signup = new JButton("Sign Up");
        signup.addActionListener(actionListener);
        logout = new JButton("Logout");
        logout.addActionListener(actionListener);

        viewStores = new JButton("View Stores");
        viewProducts = new JButton("View Products");
        addStore = new JButton("Add Store");
        removeStore = new JButton("Remove Store");
        editStore = new JButton("Edit Store");
        addProduct = new JButton("Add Product");
        removeProduct = new JButton("Remove Product");
        editProduct = new JButton("Edit Product");

        viewShoppingCart = new JButton("Shopping Cart");
        viewPurchaseHistory = new JButton("Purchase History");

//        viewMarket = new JButton("Market");
        viewDashboard = new JButton("Dashboard");
        topBar.add(username);
        topBar.add(password);
        topBar.add(login);
        topBar.add(signup);
        topBar.add(logout);
        topBar.add(viewDashboard);

        userBar.add(viewStores);
        userBar.add(addStore);
        userBar.add(removeStore);
        userBar.add(editStore);
        userBar.add(viewProducts);
        userBar.add(addProduct);
        userBar.add(removeProduct);
        userBar.add(editProduct);
        userBar.add(viewShoppingCart);
        userBar.add(viewPurchaseHistory);
        logout.setVisible(false);
//        viewMarket.setVisible(false);
        viewDashboard.setVisible(false);

        viewStores.setVisible(false);
        viewProducts.setVisible(false);
        addStore.setVisible(false);
        removeStore.setVisible(false);
        editStore.setVisible(false);
        addProduct.setVisible(false);
        removeProduct.setVisible(false);
        editProduct.setVisible(false);

        viewShoppingCart.setVisible(false);
        viewPurchaseHistory.setVisible(false);

        JPanel productPage = new JPanel();
        scrollPane = new JScrollPane(productPage);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        productPage.setBackground(Color.lightGray);
        productPage.setLayout(new GridBagLayout());

        int item = 0;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 8; j++) {
                JButton product = new JButton("Product" + item++);
                productButtons.add(product);
                product.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(null, product.getText() + " Information", "Info",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                });
                productButtons.add(product);
                productPage.add(product, new GridBagConstraints(j, i, 1, 1, 1.0, 1.0,
                        GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(20, 20, 20, 20), 0, 40));
            }
        }

        container.add(topBar, BorderLayout.PAGE_START);
        container.add(userBar, BorderLayout.PAGE_END);
        container.add(scrollPane, BorderLayout.CENTER);
        frame.pack();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Client());
    }
}
