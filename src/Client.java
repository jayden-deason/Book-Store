import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Client extends JComponent implements Runnable{
    private Client client;
    private boolean loggedIn;
    private String status = "";
    private JPanel topBar, userBar;
    private JScrollPane scrollPane;

    private JButton login, signup, logout;
    private JButton viewMarket, viewDashboard;
    private JButton viewShoppingCart, viewPurchaseHistory, checkout, purchase;
    private JButton viewStores, viewProducts, addStore, removeStore, editStore, addProduct, removeProduct, editProduct;
    private JButton confirmAddProduct;
    private ArrayList<JButton> productButtons = new ArrayList<>();

    private JTextField username;
    private JTextField password;
    private JTextField productToAdd;

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
                    checkout.setVisible(true);
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
                status = "Buyer";
                if (status.equals("Buyer")) {
                    viewShoppingCart.setVisible(true);
                    viewPurchaseHistory.setVisible(true);
                    checkout.setVisible(true);
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
            } else if (e.getSource() == viewDashboard) {
                JFrame dashFrame = new JFrame("Dashboard");
                dashFrame.setSize(500, 500);
                dashFrame.setVisible(true);
                Container content = dashFrame.getContentPane();
                JPanel panel = new JPanel();
                JScrollPane scrollPanel = new JScrollPane(panel);

                JRootPane root = dashFrame.getRootPane();
                JMenuBar bar = new JMenuBar();
                JMenu menu = new JMenu("Stats");
                bar.add(menu);
                JMenuItem products = new JMenuItem("By Products Sold");
                JMenuItem purchases = new JMenuItem("By Purchase History");
                menu.add(products);
                menu.add(purchases);
                root.setJMenuBar(bar);

                panel.setLayout(new GridBagLayout());
                scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                products.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("By products sold");
                        int item = 0;
                        panel.removeAll();
                        for (int i = 0; i < 5; i++) {
                            for (int j = 0; j < 5; j++) {
                                JButton product = new JButton("Product" + item++);
                                product.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        JOptionPane.showMessageDialog(null, product.getText() + " Information", "Info",
                                                JOptionPane.INFORMATION_MESSAGE);
                                    }
                                });
                                panel.add(product, new GridBagConstraints(j, i, 1, 1, 1.0, 1.0,
                                        GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(20, 20, 20, 20), 0, 40));
                            }
                        }
                        panel.updateUI();
                    }
                });

                purchases.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("By purchase history");
                        int item = 0;
                        panel.removeAll();
                        for (int i = 0; i < 5; i++) {
                            for (int j = 0; j < 5; j++) {
                                JButton product = new JButton("Purchased" + item++);
                                product.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        JOptionPane.showMessageDialog(null, product.getText() + " Information", "Info",
                                                JOptionPane.INFORMATION_MESSAGE);
                                    }
                                });
                                panel.add(product, new GridBagConstraints(j, i, 1, 1, 1.0, 1.0,
                                        GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(20, 20, 20, 20), 0, 40));
                            }
                        }
                        panel.updateUI();
                    }
                });
                content.add(scrollPanel);
            } else if (e.getSource() == viewShoppingCart) {
                JOptionPane.showMessageDialog(null, "Shopping cart items", "Shopping Cart", JOptionPane.INFORMATION_MESSAGE);
            } else if (e.getSource() == viewPurchaseHistory) {
                JOptionPane.showMessageDialog(null, "Purchase History", "Purchase History", JOptionPane.INFORMATION_MESSAGE);
            } else if (e.getSource() == viewStores) {
                JFrame storesFrame = new JFrame();
                storesFrame.setSize(500, 500);
                storesFrame.setVisible(true);
                Container content = storesFrame.getContentPane();
                JPanel panel = new JPanel();
                JTable table = new JTable(new String[][]{{"Name", "#Sales", "#Revenue", "#Customer Info"},{"Name", "#Sales", "#Revenue", "#Customer Info"},
                                {"Name", "#Sales", "#Revenue", "#Customer Info"}},
                        new String[]{"Store Name", "Sales", "Revenue", "Customer Info"});
                table.setEnabled(false);
//                panel.setLayout(new GridBagLayout());
//                content.add(panel);
//                panel.add(table);
//
//                for (int i = 0; i < 5; i++) {
//                    panel.add(new JLabel("Store Name"), new GridBagConstraints(0, i, 1, 1, 1.0, 1.0,
//                            GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(20, 20, 20, 20), 0, 40));
//                    panel.add(new JLabel("#Sales"), new GridBagConstraints(1, i, 1, 1, 1.0, 1.0,
//                            GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(20, 20, 20, 20), 0, 40));
//                    panel.add(new JLabel("#Revenue"), new GridBagConstraints(2, i, 1, 1, 1.0, 1.0,
//                            GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(20, 20, 20, 20), 0, 40));
//                    panel.add(new JLabel("Customer Info"), new GridBagConstraints(3, i, 1, 1, 1.0, 1.0,
//                            GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(20, 20, 20, 20), 0, 40));
//                }
                content.add(table);
                storesFrame.pack();
            } else if (e.getSource() == viewProducts) {
                JFrame productFrame = new JFrame();
                productFrame.setSize(750, 500);
                productFrame.setVisible(true);
                Container content = productFrame.getContentPane();
                JPanel panel = new JPanel();
                JPanel infoPanel = new JPanel();
                panel.setLayout(new GridLayout(5, 5, 10, 10));
                JScrollPane scrollPane = new JScrollPane(panel);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

                JTextArea infoText = new JTextArea("");
                infoText.setBackground(infoPanel.getBackground());
                infoText.setEditable(false);
                infoText.setFont(new Font(infoText.getFont().getName(), Font.PLAIN, 16));
                infoPanel.add(infoText);

                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        JButton button = new JButton("Product");
                        panel.add(button);
                        button.addMouseListener(new MouseListener() {
                            @Override
                            public void mouseClicked(MouseEvent e) {

                            }
                            @Override
                            public void mousePressed(MouseEvent e) {

                            }
                            @Override
                            public void mouseReleased(MouseEvent e) {

                            }
                            @Override
                            public void mouseEntered(MouseEvent e) {
                                infoText.setText("Product info");
                            }
                            @Override
                            public void mouseExited(MouseEvent e) {
                                infoText.setText("");
                            }
                        });
                    }
                }
                content.add(scrollPane, BorderLayout.EAST);
                content.add(infoPanel, BorderLayout.WEST);
            } else if (e.getSource() == addProduct) {
                if (productToAdd.isVisible()) {
                    productToAdd.setVisible(false);
                    confirmAddProduct.setVisible(false);
                    userBar.updateUI();
                } else {
                    productToAdd.setVisible(true);
                    confirmAddProduct.setVisible(true);
                    userBar.updateUI();
                }
            } else if (e.getSource() == confirmAddProduct) {
                // Code to add product
            } else if (e.getSource() == removeProduct) {
                JFrame removeProductFrame = new JFrame();
                removeProductFrame.setSize(400, 500);
                removeProductFrame.setVisible(true);
                final DefaultListModel<String> list = new DefaultListModel<>();
                for (int i = 0; i < 10; i++) {
                    list.addElement("Product" + i);
                }
                final JList<String> jList = new JList<>(list);
                JPanel panel = new JPanel();
                JButton remove = new JButton("Remove");
                remove.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Code to remove product
                    }
                });
                Container content = removeProductFrame.getContentPane();
                panel.add(jList, BorderLayout.EAST);
                panel.add(remove, BorderLayout.WEST);
                content.add(panel);
                removeProductFrame.pack();
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
        productToAdd = new JTextField("", 40);
        productToAdd.setToolTipText("Example: index,bookname,storename,description,quantity,price");
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
        viewStores.addActionListener(actionListener);
        viewProducts = new JButton("View Products");
        viewProducts.addActionListener(actionListener);
        addStore = new JButton("Add Store");
        removeStore = new JButton("Remove Store");
        editStore = new JButton("Edit Store");
        addProduct = new JButton("Add Product");
        addProduct.addActionListener(actionListener);
        removeProduct = new JButton("Remove Product");
        removeProduct.addActionListener(actionListener);
        editProduct = new JButton("Edit Product");

        viewShoppingCart = new JButton("Shopping Cart");
        viewShoppingCart.addActionListener(actionListener);
        viewPurchaseHistory = new JButton("Purchase History");
        viewPurchaseHistory.addActionListener(actionListener);
        purchase = new JButton("Buy");
        checkout = new JButton("Checkout");
//        viewMarket = new JButton("Market");
        viewDashboard = new JButton("Dashboard");
        viewDashboard.addActionListener(actionListener);

        confirmAddProduct = new JButton("\u2713");
        confirmAddProduct.setPreferredSize(new Dimension(45, 25));
        confirmAddProduct.addActionListener(actionListener);

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
        userBar.add(productToAdd);
        userBar.add(confirmAddProduct);
        userBar.add(removeProduct);
        userBar.add(editProduct);
        userBar.add(viewShoppingCart);
        userBar.add(viewPurchaseHistory);
        userBar.add(checkout);

        logout.setVisible(false);
//        viewMarket.setVisible(false);
        viewDashboard.setVisible(false);

        viewStores.setVisible(false);
        viewProducts.setVisible(false);
        addStore.setVisible(false);
        productToAdd.setVisible(false);
        confirmAddProduct.setVisible(false);
        removeStore.setVisible(false);
        editStore.setVisible(false);
        addProduct.setVisible(false);
        removeProduct.setVisible(false);
        editProduct.setVisible(false);

        viewShoppingCart.setVisible(false);
        viewPurchaseHistory.setVisible(false);
        checkout.setVisible(false);

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
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            return;
        }
        SwingUtilities.invokeLater(new Client());
    }
}
