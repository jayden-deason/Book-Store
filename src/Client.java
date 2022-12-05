import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.UIManager;

public class Client extends JComponent implements Runnable{
    private Client client;
    private boolean loggedIn;
    private boolean status = true;
    private String searchType;
    private JPanel topBar, userBar, productPage;
    private JScrollPane scrollPane;

    private ButtonGroup buttonGroup;
    private JRadioButton isBuyer, isSeller;

    private JButton login, signup, logout, updateMarket;
    private JButton viewDashboard, search;
    private JButton viewShoppingCart, viewPurchaseHistory, checkout, purchase;
    private JButton viewStores, viewProducts, addStore, addProduct, removeProduct, editProduct;
    private JButton confirmAddProduct, confirmAddStore;
    private ArrayList<JButton> productButtons = new ArrayList<>();

    private JTextField username;
    private JTextField password;
    private JTextField productToAdd, storeToAdd;
    private JTextField searchText;

    private JComboBox<String> searchOptions, sortMarket;

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == viewDashboard) {
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
                JMenuItem customers = new JMenuItem("By Customer");
                menu.add(products);
                menu.add(purchases);
                menu.add(customers);
                root.setJMenuBar(bar);
                if (status) {
                    customers.setVisible(false);
                } else {
                    purchases.setVisible(false);
                }

                panel.setLayout(new GridBagLayout());
                scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                // TODO: add data connection
                products.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int item = 0;
                        panel.removeAll();
                        // Receive arraylist
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
                customers.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("By Customer Info");
                        int item = 0;
                        panel.removeAll();
                        for (int i = 0; i < 5; i++) {
                            for (int j = 0; j < 5; j++) {
                                JButton product = new JButton("Customer" + item++);
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
                JFrame shoppingCartFrame = new JFrame("Shopping Cart");
                shoppingCartFrame.setVisible(true);
                Container content = shoppingCartFrame.getContentPane();
                content.setLayout(new GridLayout(4, 3));

                for (int i = 0; i < 4; i++) {
                    JTextField name = new JTextField("Product");
                    name.setEditable(false);
                    JTextField quantity = new JTextField("1");
                    quantity.setToolTipText("Set to 0 and confirm to remove item");
                    JButton confirm = new JButton("\u2713");
                    confirm.setPreferredSize(new Dimension(20, 35));
                    confirm.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            //TODO: code to update shopping cart quantity
                        }
                    });
                    content.add(name);
                    content.add(quantity);
                    content.add(confirm);
                }
                shoppingCartFrame.pack();
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
                        //TODO:change to a focus listener
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
            } else if (e.getSource() == addStore) {
                if (storeToAdd.isVisible()) {
                    storeToAdd.setVisible(false);
                    confirmAddStore.setVisible(false);
                    userBar.updateUI();
                } else {
                    storeToAdd.setVisible(true);
                    confirmAddStore.setVisible(true);
                    userBar.updateUI();
                }
            } else if (e.getSource() == confirmAddStore) {
                //TODO: code to actually add store
            }
            else if (e.getSource() == confirmAddProduct) {
                //TODO: code to actually add product
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
                        //TODO: code to actually remove the product
                    }
                });
                Container content = removeProductFrame.getContentPane();
                panel.add(jList, BorderLayout.EAST);
                panel.add(remove, BorderLayout.WEST);
                content.add(panel);
                removeProductFrame.pack();
            } else if (e.getSource() == editProduct) {
                JFrame editProductFrame = new JFrame();
                editProductFrame.setSize(400, 500);
                editProductFrame.setVisible(true);
                final DefaultListModel<String> list = new DefaultListModel<>();
                for (int i = 0; i < 10; i++) {
                    list.addElement("Product" + i);
                }
                final JList<String> jList = new JList<>(list);
                JPanel panel = new JPanel();
                JPanel panel2 = new JPanel();
                panel2.setLayout(new BoxLayout(panel2, BoxLayout.PAGE_AXIS));
                JButton edit = new JButton("Edit");
                JTextField name = new JTextField("Name");
                JTextField price = new JTextField("Price");
                JTextField description = new JTextField("Description");
                JTextField quantity = new JTextField("Quantity");
                edit.setVisible(false);
                name.setVisible(false);
                price.setVisible(false);
                description.setVisible(false);
                quantity.setVisible(false);
                jList.addListSelectionListener(new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        edit.setVisible(true);
                        name.setVisible(true);
                        price.setVisible(true);
                        description.setVisible(true);
                        quantity.setVisible(true);
                    }
                });
                Container content = editProductFrame.getContentPane();
                panel.add(jList);
                panel2.add(edit);
                panel2.add(name);
                panel2.add(price);
                panel2.add(description);
                panel2.add(quantity);
                content.add(panel, BorderLayout.WEST);
                content.add(panel2, BorderLayout.CENTER);
                editProductFrame.pack();
            } else if (e.getSource() == search) {
                JFrame searchFrame = new JFrame("Search Results");
                searchFrame.setVisible(true);
                JPanel results = new JPanel();
                results.setLayout(new GridBagLayout());
                JTextField itemsFound = new JTextField("# results for " + searchType + " search:");
                itemsFound.setEditable(false);
                Container searchContainer = searchFrame.getContentPane();
                int item = 0;
                // Receive arraylist
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 5; j++) {
                        JButton product = new JButton("Product" + item++);
                        product.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                JFrame productFrame = new JFrame("Product");
                                productFrame.setVisible(true);
                                JPanel productPanel = new JPanel();
                                Container content = productFrame.getContentPane();
                                JTextField quantity = new JTextField();
                                quantity.setPreferredSize(new Dimension(20, 25));
                                JTextArea info = new JTextArea("Product Information\nInfo\nInfo");
                                info.setEditable(false);
                                productPanel.add(info);
                                productPanel.add(purchase);
                                productPanel.add(quantity);
                                content.add(productPanel);
                                productFrame.pack();
                            }
                        });
                        results.add(product, new GridBagConstraints(j, i, 1, 1, 1.0, 1.0,
                                GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(20, 20, 20, 20), 0, 40));
                    }
                }
                searchContainer.add(itemsFound, BorderLayout.NORTH);
                searchContainer.add(results, BorderLayout.CENTER);
                searchFrame.pack();
            } else if (e.getSource() == checkout) {
                Object[] options = {"Confirm", "Cancel"};
                JOptionPane.showOptionDialog(null, "Are you sure you wish to checkout?", "Checkout",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            }
        }
    };

    public void run() {
        JFrame frame = new JFrame();
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        Container container = frame.getContentPane();
        container.setLayout(new BorderLayout());
        username = new JTextField("Username", 20);
        password = new JTextField("Password", 20);
        buttonGroup = new ButtonGroup();
        isBuyer = new JRadioButton("Buyer");
        isBuyer.setSelected(true);
        isSeller = new JRadioButton("Seller");
        buttonGroup.add(isBuyer);
        buttonGroup.add(isSeller);
        productToAdd = new JTextField("", 40);
        productToAdd.setToolTipText("Example: index,bookname,storename,description,quantity,price");
        storeToAdd = new JTextField("", 40);
        storeToAdd.setToolTipText("Example: index,storename,username,#sales,#revenue," +
                "<product index/product index>,<product index/#sales:product index/#sales>");
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

        isBuyer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status = !status;
            }
        });
        isSeller.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status = !status;
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
        signup = new JButton("Sign Up");
        logout = new JButton("Logout");
        search = new JButton("Search");
        search.addActionListener(actionListener);
        searchText = new JTextField(40);
        searchOptions = new JComboBox<>();
        searchOptions.addItem("Name");
        searchOptions.addItem("Store");
        searchOptions.addItem("Description");
        sortMarket = new JComboBox<>();
        sortMarket.addItem("Sort By: Alphabetically");
        sortMarket.addItem("Sort By: Price");
        sortMarket.addItem("Sort By: Quantity");
        updateMarket = new JButton("Update Market");
        sortMarket.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //TODO: make this use an arraylist of products somehow
                if (e.getStateChange() == ItemEvent.SELECTED && e.getItem() == sortMarket.getItemAt(0)) {
                    updateMarket.doClick();
                } else if (e.getStateChange() == ItemEvent.SELECTED && e.getItem() == sortMarket.getItemAt(1)) {
                    updateMarket.doClick();
                } else if (e.getStateChange() == ItemEvent.SELECTED && e.getItem() == sortMarket.getItemAt(2)) {
                    updateMarket.doClick();
                }
            }
        });
        searchOptions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox cb = (JComboBox) e.getSource();
                searchType = (String) cb.getSelectedItem();
            }
        });
        searchOptions.setSelectedIndex(0);

        viewStores = new JButton("View Stores");
        viewStores.addActionListener(actionListener);
        viewProducts = new JButton("View Products");
        viewProducts.addActionListener(actionListener);
        addStore = new JButton("Add Store");
        addStore.addActionListener(actionListener);
        addProduct = new JButton("Add Product");
        addProduct.addActionListener(actionListener);
        removeProduct = new JButton("Remove Product");
        removeProduct.addActionListener(actionListener);
        editProduct = new JButton("Edit Product");
        editProduct.addActionListener(actionListener);

        viewShoppingCart = new JButton("Shopping Cart");
        viewShoppingCart.addActionListener(actionListener);
        viewPurchaseHistory = new JButton("Purchase History");
        viewPurchaseHistory.addActionListener(actionListener);
        purchase = new JButton("Buy");
        checkout = new JButton("Checkout");
        checkout.addActionListener(actionListener);

        viewDashboard = new JButton("Dashboard");
        viewDashboard.addActionListener(actionListener);

        confirmAddProduct = new JButton("\u2713");
        confirmAddProduct.setPreferredSize(new Dimension(45, 25));
        confirmAddProduct.addActionListener(actionListener);
        confirmAddStore = new JButton("\u2713");
        confirmAddStore.setPreferredSize(new Dimension(45, 25));
        confirmAddStore.addActionListener(actionListener);

        topBar.add(username);
        topBar.add(password);
        topBar.add(login);
        topBar.add(signup);
        topBar.add(isBuyer);
        topBar.add(isSeller);
        topBar.add(logout);
        topBar.add(viewDashboard);
        topBar.add(searchOptions);
        topBar.add(searchText);
        topBar.add(search);
        topBar.add(sortMarket);
        topBar.add(updateMarket);

        userBar.add(viewStores);
        userBar.add(addStore);
        userBar.add(storeToAdd);
        userBar.add(confirmAddStore);
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
        viewDashboard.setVisible(false);

        viewStores.setVisible(false);
        viewProducts.setVisible(false);
        addStore.setVisible(false);
        storeToAdd.setVisible(false);
        confirmAddStore.setVisible(false);
        productToAdd.setVisible(false);
        confirmAddProduct.setVisible(false);
        addProduct.setVisible(false);
        removeProduct.setVisible(false);
        editProduct.setVisible(false);

        viewShoppingCart.setVisible(false);
        viewPurchaseHistory.setVisible(false);
        checkout.setVisible(false);

        productPage = new JPanel();
        scrollPane = new JScrollPane(productPage);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        productPage.setBackground(Color.lightGray);
        productPage.setLayout(new GridBagLayout());
        scrollPane.setVisible(true);
        productPage.setVisible(true);
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: add data connection
                isBuyer.setVisible(false);
                isSeller.setVisible(false);
                login.setVisible(false);
                signup.setVisible(false);
                logout.setVisible(true);
                userBar.setVisible(true);
                viewDashboard.setVisible(true);
                scrollPane.setVisible(true);
                if (status) {
                    viewShoppingCart.setVisible(true);
                    viewPurchaseHistory.setVisible(true);
                    checkout.setVisible(true);
                } else if (!status) {
                    viewStores.setVisible(true);
                    viewProducts.setVisible(true);
                    addStore.setVisible(true);
                    addProduct.setVisible(true);
                    removeProduct.setVisible(true);
                    editProduct.setVisible(true);
                }

                System.out.println("Updated");

                int item = 0;
                for (int i = 0; i < 20; i++) {
                    for (int j = 0; j < 8; j++) {
                        JButton product = new JButton("Product" + item++);
                        product.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                JFrame productFrame = new JFrame("Product");
                                productFrame.setVisible(true);
                                JPanel productPanel = new JPanel();
                                Container content = productFrame.getContentPane();
                                JTextField quantity = new JTextField();
                                quantity.setPreferredSize(new Dimension(20, 25));
                                JTextArea info = new JTextArea("Product Information\nInfo\nInfo");
                                info.setEditable(false);
                                productPanel.add(info);
                                productPanel.add(purchase);
                                productPanel.add(quantity);
                                content.add(productPanel);
                                productFrame.pack();
                            }
                        });
                        productButtons.add(product);
                        productPage.add(product, new GridBagConstraints(j, i, 1, 1, 1.0, 1.0,
                                GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(20, 20, 20, 20), 0, 40));
                    }
                }
            }
        });
        signup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isBuyer.setVisible(false);
                isSeller.setVisible(false);
                login.setVisible(false);
                signup.setVisible(false);
                logout.setVisible(true);
                userBar.setVisible(true);
                viewDashboard.setVisible(true);
                scrollPane.setVisible(true);
                if (status) {
                    viewShoppingCart.setVisible(true);
                    viewPurchaseHistory.setVisible(true);
                    checkout.setVisible(true);
                } else if (!status) {
                    viewStores.setVisible(true);
                    viewProducts.setVisible(true);
                    addStore.setVisible(true);
                    addProduct.setVisible(true);
                    removeProduct.setVisible(true);
                    editProduct.setVisible(true);
                }

                System.out.println("Updated");

                int item = 0;
                for (int i = 0; i < 20; i++) {
                    for (int j = 0; j < 8; j++) {
                        JButton product = new JButton("Product" + item++);
                        product.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                JFrame productFrame = new JFrame("Product");
                                productFrame.setVisible(true);
                                JPanel productPanel = new JPanel();
                                Container content = productFrame.getContentPane();
                                JTextField quantity = new JTextField();
                                quantity.setPreferredSize(new Dimension(20, 25));
                                JTextArea info = new JTextArea("Product Information\nInfo\nInfo");
                                info.setEditable(false);
                                productPanel.add(info);
                                productPanel.add(purchase);
                                productPanel.add(quantity);
                                content.add(productPanel);
                                productFrame.pack();
                            }
                        });
                        productButtons.add(product);
                        productPage.add(product, new GridBagConstraints(j, i, 1, 1, 1.0, 1.0,
                                GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(20, 20, 20, 20), 0, 40));
                    }
                }
            }
        });
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loggedIn = false;
                isBuyer.setVisible(true);
                isSeller.setVisible(true);
                scrollPane.setVisible(false);
                login.setVisible(true);
                signup.setVisible(true);
                logout.setVisible(false);
                userBar.setVisible(false);
                viewDashboard.setVisible(false);

                viewStores.setVisible(false);
                viewProducts.setVisible(false);
                addStore.setVisible(false);
                addProduct.setVisible(false);
                removeProduct.setVisible(false);
                editProduct.setVisible(false);

                viewShoppingCart.setVisible(false);
                viewPurchaseHistory.setVisible(false);
                System.out.println("Updated");
                scrollPane.updateUI();
            }
        });

        updateMarket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int item = 0;
                productPage.removeAll();
                for (int i = 0; i < 20; i++) {
                    for (int j = 0; j < 8; j++) {
                        JButton product = new JButton("Produc0t" + item++);
                        product.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                JFrame productFrame = new JFrame("Product");
                                productFrame.setVisible(true);
                                JPanel productPanel = new JPanel();
                                Container content = productFrame.getContentPane();
                                JTextField quantity = new JTextField();
                                quantity.setPreferredSize(new Dimension(20, 25));
                                JTextArea info = new JTextArea("Product Information\nInfo\nInfo");
                                info.setEditable(false);
                                productPanel.add(info);
                                productPanel.add(purchase);
                                productPanel.add(quantity);
                                content.add(productPanel);
                                productFrame.pack();
                            }
                        });
                        productButtons.add(product);
                        productPage.add(product, new GridBagConstraints(j, i, 1, 1, 1.0, 1.0,
                                GridBagConstraints.PAGE_START, GridBagConstraints.BOTH, new Insets(20, 20, 20, 20), 0, 40));
                    }
                }
                productPage.updateUI();
            }
        });
        container.add(topBar, BorderLayout.PAGE_START);
        container.add(userBar, BorderLayout.PAGE_END);
        container.add(scrollPane, BorderLayout.CENTER);
        frame.pack();
    }
    public static void main(String[] args) {
        //TODO: create socket connection
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (Exception e) {
            return;
        }
        SwingUtilities.invokeLater(new Client());
    }
}