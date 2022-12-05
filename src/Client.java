import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends JComponent implements Runnable{
    private Client client;
    private Socket socket;
    private ObjectInputStream reader;
    private PrintWriter writer;
    private boolean loggedIn;
    private boolean status = true;
    private boolean userType = true; //true = buyer, false = seller
    private JPanel topBar, userBar, productPage;
    private JScrollPane scrollPane;

    private ButtonGroup buttonGroup;
    private JRadioButton isBuyer, isSeller;

    private JButton login, signup, logout;
    private JButton viewDashboard, search;
    private JButton viewShoppingCart, viewPurchaseHistory, checkout, purchase;
    private JButton viewStores, viewProducts, addStore, removeStore, editStore, addProduct, removeProduct, editProduct;
    private JButton confirmAddProduct;
    private ArrayList<JButton> productButtons = new ArrayList<>();

    private JTextField username;
    private JTextField password;
    private JTextField productToAdd;
    private JTextField searchText;

    private JComboBox<String> searchOptions, sortMarket;

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
//            if (e.getSource() == login) {
//                // log in condition
//                loggedIn = true;
//                isBuyer.setVisible(false);
//                isSeller.setVisible(false);
//                login.setVisible(false);
//                signup.setVisible(false);
//                logout.setVisible(true);
//                userBar.setVisible(true);
//                viewDashboard.setVisible(true);
//                displayMarket(null, "login");
//                if (status) {
//                    viewShoppingCart.setVisible(true);
//                    viewPurchaseHistory.setVisible(true);
//                    checkout.setVisible(true);
//                } else if (!status) {
//                    viewStores.setVisible(true);
//                    viewProducts.setVisible(true);
//                    addStore.setVisible(true);
//                    removeStore.setVisible(true);
//                    editStore.setVisible(true);
//                    addProduct.setVisible(true);
//                    removeProduct.setVisible(true);
//                    editProduct.setVisible(true);
//                }
//            } else if (e.getSource() == signup) {
//                // sign up condition
//                loggedIn = true;
//                isBuyer.setVisible(false);
//                isSeller.setVisible(false);
//                scrollPane.setVisible(true);
//                login.setVisible(false);
//                signup.setVisible(false);
//                logout.setVisible(true);
//                userBar.setVisible(true);
//                viewDashboard.setVisible(true);
//                displayMarket(null, "signup");
//                if (status) {
//                    viewShoppingCart.setVisible(true);
//                    viewPurchaseHistory.setVisible(true);
//                    checkout.setVisible(true);
//                } else if (!status) {
//                    viewStores.setVisible(true);
//                    viewProducts.setVisible(true);
//                    addStore.setVisible(true);
//                    removeStore.setVisible(true);
//                    editStore.setVisible(true);
//                    addProduct.setVisible(true);
//                    removeProduct.setVisible(true);
//                    editProduct.setVisible(true);
//                }
//            } else if (e.getSource() == logout) {
//                // logout conditions
//                loggedIn = false;
//                isBuyer.setVisible(true);
//                isSeller.setVisible(true);
//                scrollPane.setVisible(false);
//                login.setVisible(true);
//                signup.setVisible(true);
//                logout.setVisible(false);
//                userBar.setVisible(false);
//                viewDashboard.setVisible(false);
//
//                viewStores.setVisible(false);
//                viewProducts.setVisible(false);
//                addStore.setVisible(false);
//                removeStore.setVisible(false);
//                editStore.setVisible(false);
//                addProduct.setVisible(false);
//                removeProduct.setVisible(false);
//                editProduct.setVisible(false);
//
//                viewShoppingCart.setVisible(false);
//                viewPurchaseHistory.setVisible(false);
            /*} else*/ if (e.getSource() == viewDashboard) {
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
                products.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("By products sold");

                        ArrayList<Product> products = getAllProducts("sales");

                        //int item = 0;
                        panel.removeAll();
                        for (int i = 0; i < products.size(); i++) {
                            for (int j = 0; j < 5; j++) {
                                JButton product = new JButton(products.get(i).getName());
                                int finalI = i;
                                product.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        JOptionPane.showMessageDialog(null, product.getText() +
                                                        " Information\n" + products.get(finalI).getDescription(), "Info",
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

                        ArrayList<Product> products = getAllProducts("history");

                        //int item = 0;
                        panel.removeAll();
                        for (int i = 0; i < products.size(); i++) {
                            for (int j = 0; j < 5; j++) {
                                JButton product = new JButton(products.get(i).getName());
                                int finalI = i;
                                product.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        JOptionPane.showMessageDialog(null, product.getText() +
                                                        " Information\n" + products.get(finalI).getDescription(), "Info",
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

                //TODO: add seller implementation
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

                ArrayList<Product> products = getShoppingCart();

                for (int i = 0; i < products.size(); i++) {
                    JTextField name = new JTextField(products.get(i).getName());
                    name.setEditable(false);
                    //TODO: fix for proper quantity
                    JTextField quantity = new JTextField(products.get(i).getQuantity());
                    quantity.setToolTipText("Set to 0 and confirm to remove item");
                    JButton confirm = new JButton("\u2713");
                    confirm.setPreferredSize(new Dimension(20, 35));
                    int finalI = i;
                    confirm.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String successCheck;
                            try {
                                successCheck = editCart(products.get(finalI), Integer.parseInt(quantity.getText()));
                            } catch (NumberFormatException ex){
                                JOptionPane.showMessageDialog(null, "Invalid argument. Enter an " +
                                        "integer.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            if (successCheck.equalsIgnoreCase("n")) {
                                JOptionPane.showMessageDialog(null, "Not enough stock. Decrease" +
                                        " the amount in your cart.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });
                    content.add(name);
                    content.add(quantity);
                    content.add(confirm);
                }
                shoppingCartFrame.pack();
            } else if (e.getSource() == viewPurchaseHistory) {
                ArrayList<Product> products = getPurchaseHistory();

                String history = "";
                for (int i = 0; i < products.size(); i++) {
                    history += products.get(i).getName() + "\n";
                }
                JOptionPane.showMessageDialog(null, history, "Purchase History", JOptionPane.INFORMATION_MESSAGE);

            //TODO: implement following cases for sellers
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
            } else if (e.getSource() == confirmAddProduct) {
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
                JTextField storeName = new JTextField("Store Name");
                JTextField description = new JTextField("Description");
                edit.setVisible(false);
                name.setVisible(false);
                storeName.setVisible(false);
                description.setVisible(false);
                jList.addListSelectionListener(new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        edit.setVisible(true);
                        name.setVisible(true);
                        storeName.setVisible(true);
                        description.setVisible(true);
                    }
                });
                Container content = editProductFrame.getContentPane();
                panel.add(jList);
                panel2.add(edit);
                panel2.add(name);
                panel2.add(storeName);
                panel2.add(description);
                content.add(panel, BorderLayout.WEST);
                content.add(panel2, BorderLayout.CENTER);
                editProductFrame.pack();
            } else if (e.getSource() == search) {
                JFrame searchFrame = new JFrame("Search");
                searchFrame.setVisible(true);
            }

            //TODO: add search, export, and make purchase
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
                userType = true;
            }
        });
        isSeller.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                status = !status;
                userType = false;
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
//        login.addActionListener(actionListener);
        signup = new JButton("Sign Up");
//        signup.addActionListener(actionListener);
        logout = new JButton("Logout");
//        logout.addActionListener(actionListener);
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
        editProduct.addActionListener(actionListener);

        viewShoppingCart = new JButton("Shopping Cart");
        viewShoppingCart.addActionListener(actionListener);
        viewPurchaseHistory = new JButton("Purchase History");
        viewPurchaseHistory.addActionListener(actionListener);
        purchase = new JButton("Buy");
        checkout = new JButton("Checkout");

        viewDashboard = new JButton("Dashboard");
        viewDashboard.addActionListener(actionListener);

        confirmAddProduct = new JButton("\u2713");
        confirmAddProduct.setPreferredSize(new Dimension(45, 25));
        confirmAddProduct.addActionListener(actionListener);

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
                //TODO: figure out why this kills it
                if (userType) { //logging in as buyer
                    writer.printf("%d,%d,%s,%s\n", 0, 0, username.getText(), password.getText());
                    System.out.printf("%d,%d,%s,%s\n", 0, 0, username.getText(), password.getText());
                    writer.flush();
                    String response;
                    try {
                        response = (String) reader.readObject();
                        System.out.println(response);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (response.equalsIgnoreCase("N")) {
                        JOptionPane.showMessageDialog(null, "Invalid Username or Password." +
                                "\nEnter correct info or create an account.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else { //logging in as seller
                    writer.printf("%d,%d,%s,%s\n", 1, 0, username.getText(), password.getText());
                    writer.flush();
                    String response;
                    try {
                        response = (String) reader.readObject();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (response.equalsIgnoreCase("N")) {
                        JOptionPane.showMessageDialog(null, "Invalid Username or Password." +
                                "\nEnter correct info or create an account.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                ArrayList<Product> products;
                try {
                    products = (ArrayList<Product>) reader.readObject();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }

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
                    removeStore.setVisible(true);
                    editStore.setVisible(true);
                    addProduct.setVisible(true);
                    removeProduct.setVisible(true);
                    editProduct.setVisible(true);
                }

                System.out.println("Updated");

                //int item = 0;
                for (int i = 0; i < products.size(); i++) {
                    for (int j = 0; j < 8; j++) {
                        JButton product = new JButton(products.get(i).getName());
                        int finalI = i;
                        product.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                JFrame productFrame = new JFrame(products.get(finalI).getName());
                                productFrame.setVisible(true);
                                JPanel productPanel = new JPanel();
                                Container content = productFrame.getContentPane();
                                JTextField quantity = new JTextField();
                                quantity.setPreferredSize(new Dimension(20, 25));
                                JTextArea info = new JTextArea(String.format("Product Information\n%f\n%s",
                                        products.get(finalI).getSalePrice(), products.get(finalI).getDescription()));
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
                String user = username.getText();
                String pass = password.getText();

                if (!(user.contains("@") && user.contains(".com"))) {
                    JOptionPane.showMessageDialog(null, "Enter a valid email address.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (userType) {
                    writer.printf("%d,%d,%s,%s\n", 0, 1, user, pass);
                    writer.flush();
                    String response;
                    try {
                        response = (String) reader.readObject();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (response.equalsIgnoreCase("N")) {
                        JOptionPane.showMessageDialog(null, "An account with that username " +
                                "already exists.\n Choose a different one or sign in.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    writer.printf("%d,%d,%s,%s\n", 1, 1, user, pass);
                    writer.flush();
                    String response;
                    try {
                        response = (String) reader.readObject();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (response.equalsIgnoreCase("N")) {
                        JOptionPane.showMessageDialog(null, "An account with that username " +
                                        "already exists.\n Choose a different one or sign in.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                ArrayList<Product> products;
                try {
                    products = (ArrayList<Product>) reader.readObject();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }

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
                    removeStore.setVisible(true);
                    editStore.setVisible(true);
                    addProduct.setVisible(true);
                    removeProduct.setVisible(true);
                    editProduct.setVisible(true);
                }

                System.out.println("Updated");

                int item = 0;
                for (int i = 0; i < products.size(); i++) {
                    for (int j = 0; j < 8; j++) {
                        JButton product = new JButton(products.get(i).getName());
                        int finalI = i;
                        product.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                JFrame productFrame = new JFrame(products.get(finalI).getName());
                                productFrame.setVisible(true);
                                JPanel productPanel = new JPanel();
                                Container content = productFrame.getContentPane();
                                JTextField quantity = new JTextField();
                                quantity.setPreferredSize(new Dimension(20, 25));
                                JTextArea info = new JTextArea(String.format("Product Information\n%f\n%s",
                                        products.get(finalI).getSalePrice(), products.get(finalI).getDescription()));
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
                writer.println("-1");
                writer.flush();

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
                removeStore.setVisible(false);
                editStore.setVisible(false);
                addProduct.setVisible(false);
                removeProduct.setVisible(false);
                editProduct.setVisible(false);

                viewShoppingCart.setVisible(false);
                viewPurchaseHistory.setVisible(false);
                System.out.println("Updated");
                scrollPane.updateUI();
            }
        });


        container.add(topBar, BorderLayout.PAGE_START);
        container.add(userBar, BorderLayout.PAGE_END);
        container.add(scrollPane, BorderLayout.CENTER);
        frame.pack();
    }

    //TODO: make this actually use the arraylist
    public void displayMarket(ArrayList<Product> products, String flag) {
        System.out.println("Updated");
        productPage = new JPanel();
        scrollPane = new JScrollPane(productPage);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        productPage.setBackground(Color.lightGray);
        productPage.setLayout(new GridBagLayout());
        scrollPane.setVisible(true);
        productPage.setVisible(true);

        int item = 0;
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 8; j++) {
                JButton product = new JButton("Product" + item++ + flag);
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
        scrollPane.updateUI();
    }
    public static void main(String[] args) {
        Client client = new Client();
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            return;
        }
        SwingUtilities.invokeLater(new Client());
    }

    public Client() {
        try {
            socket = new Socket("localhost", 1001);

            writer = new PrintWriter(this.socket.getOutputStream());
            reader = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private ArrayList<Product> getProductsArray() {
        writer.flush();

        ArrayList<Product> products;

        try {
            products = (ArrayList<Product>) reader.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return products;
    }

    private String getStringArray() {
        writer.flush();

        String response;
        try {
            response = (String) reader.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    public ArrayList<Product> getAllProducts(String condition) {
        writer.println("1," + condition);
        return getProductsArray();
    }

    public ArrayList<Product> search(String query) {
        writer.printf("%d,%s", 2, query);
        return getProductsArray();
    }

    public String addToCart(Product product, int quantity) {
        writer.printf("4,%d,%d", product.getIndex(), quantity);
        return getStringArray();
    }

    public boolean exportToFile(String filename) {
        File testExistence = new File("filename");

        if (testExistence.exists()) {
            return false;
        } else {
            writer.println("5");
            writer.flush();

            ArrayList<String> fileInfo;
            try {
                fileInfo = (ArrayList<String>) reader.readObject();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))){
                PrintWriter exportWriter = new PrintWriter(bw);
                for (int i = 0; i < fileInfo.size(); i++) {
                    exportWriter.println(fileInfo.get(i));
                }
                exportWriter.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            return true;
        }
    }

    public String makePurchase() {
        writer.println("6");
        return getStringArray();
    }

    public ArrayList<Product> getShoppingCart() {
        writer.println("7");
        return getProductsArray();
    }

    public String editCart(Product product, int newQuantity) {
        writer.printf("8,%d,%d", product.getIndex(), newQuantity);
        return getStringArray();
    }

    public ArrayList<Product> getPurchaseHistory() {
        writer.println("9");
        return getProductsArray();
    }


}