import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Client extends JComponent implements Runnable {
    private Client client;
    private Socket socket;
    private ObjectInputStream reader;
    private PrintWriter writer;
    private boolean loggedIn;
    private boolean status = true;
    private String searchType;
    private boolean userType = true; //true = buyer, false = seller
    private JPanel topBar, userBar, productPage;
    private JScrollPane scrollPane;

    private ButtonGroup buttonGroup;
    private JRadioButton isBuyer, isSeller;

    private JButton login, signup, logout, updateMarket;
    private JButton viewDashboard, search;
    private JButton viewShoppingCart, viewPurchaseHistory, checkout, purchase, exportToFile;
    private JButton viewStores, viewProducts, addStore, addProduct, removeProduct, editProduct, exportSellerFile, importSellerFile;
    private JButton confirmAddProduct, confirmAddStore;
    private ArrayList<JButton> productButtons = new ArrayList<>();
    private ArrayList<ActionListener> purchaseListeners;

    private JTextField username;
    private JTextField password;
    private JTextField productToAdd, storeToAdd;
    private JTextField searchText;
    private JSpinner quantity;

    private JComboBox<String> searchOptions, sortMarket;
    private ActionListener productButtonListener;

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

                scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                products.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("By products sold");

                        ArrayList<String> stores = getBuyerDashboard("sales");

                        Collections.reverse(stores);

                        panel.removeAll();
                        if (stores.size() < 4) {
                            panel.setLayout(new GridLayout(stores.size(), 1));
                        } else {
                            panel.setLayout(new GridLayout(stores.size() / 4, stores.size() / 2));
                        }
                        for (String store : stores) {
                            String[] storeInfo;
                            String[] products = new String[0];
                            if (store.contains(":")) {
                                storeInfo = store.split(":");
                                products = storeInfo[1].split(";");
                            } else {
                                storeInfo = new String[]{store};
                            }

                            JButton storeButton = new JButton(storeInfo[0]);
                            String[] finalProducts = products;
                            storeButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String storeProducts = "";

                                    for (int i = 0; i < finalProducts.length; i++) {
                                        String[] productInfo = finalProducts[i].split(",");

                                        storeProducts += productInfo[0] + ": " + productInfo[1] + " available\n";
                                    }
                                    JOptionPane.showMessageDialog(null, storeProducts, storeInfo[0] + " Products", JOptionPane.INFORMATION_MESSAGE);
                                }
                            });
                            panel.add(storeButton);
                        }
                        panel.updateUI();
                    }
                });
                purchases.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("got here");
                        System.out.println("By purchase history");

                        ArrayList<String> stores = getBuyerDashboard("history");

                        Collections.reverse(stores);

                        panel.removeAll();
                        if (stores.size() < 4) {
                            panel.setLayout(new GridLayout(stores.size(), 1));
                        } else {
                            panel.setLayout(new GridLayout(stores.size() / 4, stores.size() / 2));
                        }
                        for (String store : stores) {
                            String[] storeInfo;
                            String[] products = new String[0];
                            if (store.contains(":")) {
                                storeInfo = store.split(":");
                                products = storeInfo[1].split(";");
                            } else {
                                storeInfo = new String[]{store};
                            }

                            JButton storeButton = new JButton(storeInfo[0]);
                            String[] finalProducts = products;
//                            JButton storeButton = new JButton(storeInfo[0]);
                            storeButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String storeProducts = "";

                                    for (int i = 0; i < finalProducts.length; i++) {
                                        String[] productInfo = finalProducts[i].split(",");
                                        storeProducts += productInfo[0] + ": " + productInfo[1] + " available\n";
                                    }
                                    JOptionPane.showMessageDialog(null, storeProducts, storeInfo[0] + " Products", JOptionPane.INFORMATION_MESSAGE);
                                }
                            });
                            panel.add(storeButton);
                        }
                        panel.updateUI();
                    }
                });

                //TODO: add seller implementation
                customers.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("By products sold");

                        ArrayList<String> stores = getBuyerDashboard("sales");

                        Collections.reverse(stores);

                        panel.removeAll();
                        if (stores.size() < 4) {
                            panel.setLayout(new GridLayout(stores.size(), 1));
                        } else {
                            panel.setLayout(new GridLayout(stores.size() / 4, stores.size() / 2));
                        }
                        for (String store : stores) {
                            String[] storeInfo;
                            String[] products = new String[0];
                            if (store.contains(":")) {
                                storeInfo = store.split(":");
                                products = storeInfo[1].split(";");
                            } else {
                                storeInfo = new String[]{store};
                            }

                            JButton storeButton = new JButton(storeInfo[0]);
                            String[] finalProducts = products;
                            storeButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String storeProducts = "";

                                    for (int i = 0; i < finalProducts.length; i++) {
                                        String[] productInfo = finalProducts[i].split(",");

                                        storeProducts += productInfo[0] + ": " + productInfo[1] + " available\n";
                                    }
                                    JOptionPane.showMessageDialog(null, storeProducts, storeInfo[0] + " Products", JOptionPane.INFORMATION_MESSAGE);
                                }
                            });
                            panel.add(storeButton);
                        }
                        panel.updateUI();
                    }
                });

                content.add(scrollPanel);
            } else if (e.getSource() == viewShoppingCart) {
                HashMap<Product, Integer> cart = getShoppingCart();

                if (cart.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Cart is empty.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JFrame shoppingCartFrame = new JFrame("Shopping Cart");
                shoppingCartFrame.setVisible(true);
                Container content = shoppingCartFrame.getContentPane();
                content.setLayout(new GridLayout(cart.keySet().size(), 3));

                for (Product product : cart.keySet()) {
                    JTextField name = new JTextField(product.getName());
                    name.setEditable(false);

                    int quant = cart.get(product);


                    SpinnerModel qSpinModel = new SpinnerNumberModel(quant, 0, Integer.MAX_VALUE, 1);
                    JSpinner quantity = new JSpinner(qSpinModel);
                    quantity.setPreferredSize(new Dimension(90, 25));
                    quantity.setBackground(Color.WHITE);
                    ((JSpinner.DefaultEditor) quantity.getEditor()).getTextField().setEditable(false);

                    JButton confirm = new JButton("\u2713");
                    confirm.setPreferredSize(new Dimension(20, 35));
                    confirm.setToolTipText("Confirm new quantity");
                    confirm.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String successCheck;
                            int oldQuantity = cart.get(product);
                            try {
                                if ((Integer) quantity.getValue() < 0) {
                                    JOptionPane.showMessageDialog(null,
                                            "Cannot have negative quantity!", "Error",
                                            JOptionPane.ERROR_MESSAGE);
                                    quantity.setValue(oldQuantity);
                                    return;
                                } else if ((Integer) quantity.getValue() == 0) {
                                    int cont = JOptionPane.showConfirmDialog(null,
                                            String.format("Are you sure you want to remove '%s' from your cart?", product.getName()),
                                            "Remove Product", JOptionPane.YES_NO_OPTION);
                                    if (cont != 0) {
                                        quantity.setValue(oldQuantity);
                                        return;
                                    }
                                }


                                successCheck = editCart(product, (Integer) quantity.getValue());
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(null, "Invalid argument. " +
                                        "Enter a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
                                quantity.setValue(oldQuantity);
                                return;
                            }
                            if (successCheck.equalsIgnoreCase("n")) {
                                JOptionPane.showMessageDialog(null, "Not enough stock. " +
                                        "Decrease the amount in your cart.", "Error", JOptionPane.ERROR_MESSAGE);
                                quantity.setValue(oldQuantity);

                            } else if (!quantity.getValue().equals(oldQuantity)) {
                                JOptionPane.showMessageDialog(null, "Changed quantity!",
                                        "", JOptionPane.INFORMATION_MESSAGE);
                            }

                            if ((Integer) quantity.getValue() == 0) {
                                shoppingCartFrame.dispose();
                                viewShoppingCart.doClick();
                            }
                        }
                    });
                    JButton remove = new JButton("X");
                    remove.setPreferredSize(new Dimension(20, 35));
                    remove.setToolTipText("Remove item");
                    remove.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int oldQuantity = cart.get(product);

                            int cont = JOptionPane.showConfirmDialog(null,
                                    String.format("Are you sure you want to remove '%s' from your cart?", product.getName()),
                                    "Remove Product", JOptionPane.YES_NO_OPTION);
                            if (cont == 0) {
                                String successCheck;

                                successCheck = editCart(product, 0);

                                if (successCheck.equalsIgnoreCase("n")) {
                                    JOptionPane.showMessageDialog(null, "Error removing item!",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    quantity.setValue(oldQuantity);

                                }

                                shoppingCartFrame.dispose();
                                viewShoppingCart.doClick();
                            } else {
                                quantity.setValue(oldQuantity);
                            }

                        }
                    });
                    content.add(name);
                    content.add(quantity);
                    content.add(confirm);
                    content.add(remove);
                }
                shoppingCartFrame.pack();
            } else if (e.getSource() == viewPurchaseHistory) {
                HashMap<Product, Integer> products = getPurchaseHistory();

                String history = "";
                for (Product product : products.keySet()) {
                    history += product.getName() + ": " + products.get(product) + "\n";
                }
                JOptionPane.showMessageDialog(null, history, "Purchase History", JOptionPane.INFORMATION_MESSAGE);

                //TODO: implement following cases for sellers
            } else if (e.getSource() == viewStores) {
                JFrame storesFrame = new JFrame("Stores");
                storesFrame.setSize(500, 500);
                storesFrame.setVisible(true);
                Container content = storesFrame.getContentPane();
                JPanel panel = new JPanel();
                String[][] storeInfo = getStoreInfo();
                JTable table = new JTable(storeInfo, new String[]{"Store Name", "Sales", "Revenue", "Customer Info"});
                table.setEnabled(false);
                content.add(table);
                storesFrame.pack();
            } else if (e.getSource() == viewProducts) {
                JFrame productFrame = new JFrame("Products");
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
                ArrayList<Product> products = getSellerProducts("none");
                int item = 0;
                for (Product product : products) {
                    JButton productButton = new JButton(product.getName());
                    panel.add(productButton);
                    //TODO:change to a focus listener
                    productButton.addMouseListener(new MouseListener() {
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
                            infoText.setText(getProductInfo(productButton.getText()));
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            infoText.setText("");
                        }
                    });
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
                String storeName = storeToAdd.getText();
                addStore(storeName);
                storeToAdd.setVisible(false);
                storeToAdd.setText("Store name");
                confirmAddStore.setVisible(false);
                userBar.updateUI();
            } else if (e.getSource() == confirmAddProduct) {
                String productInfo = productToAdd.getText();
                addProduct(productInfo);
                productToAdd.setVisible(false);
                productToAdd.setText("Product name,store name,description,price,quantity");
                confirmAddProduct.setVisible(false);
                userBar.updateUI();
            } else if (e.getSource() == removeProduct) {
                JFrame removeProductFrame = new JFrame("Remove Product");
                removeProductFrame.setSize(400, 500);
                removeProductFrame.setVisible(true);
                final DefaultListModel<String> list = new DefaultListModel<>();

                ArrayList<Product> products = getSellerProducts("none");
                for (Product p : products) {
                    list.addElement(p.getName());
                }
                final JList<String> jList = new JList<>(list);
                JPanel panel = new JPanel();
                JButton remove = new JButton("Remove");
                remove.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        removeProduct(jList.getSelectedValue());
                    }
                });
                Container content = removeProductFrame.getContentPane();
                panel.add(jList, BorderLayout.EAST);
                panel.add(remove, BorderLayout.WEST);
                content.add(panel);
                removeProductFrame.pack();
            } else if (e.getSource() == editProduct) {
                JFrame editProductFrame = new JFrame("Edit Product");
                editProductFrame.setSize(750, 500);
                editProductFrame.setVisible(true);

                ArrayList<Product> products = getSellerProducts("none");
                final DefaultListModel<String> list = new DefaultListModel<>();
                for (Product p : products) {
                    list.addElement(p.getName());
                }
                final JList<String> jList = new JList<>(list);
                JPanel panel = new JPanel();
                JPanel panel2 = new JPanel();
                panel2.setLayout(new BoxLayout(panel2, BoxLayout.PAGE_AXIS));
                Product selected = getProduct(jList.getSelectedValue());

                JTextField name = new JTextField("Name");
                JTextField price = new JTextField("Price");
                JTextField description = new JTextField("Description");
                JTextField quantity = new JTextField("Quantity");

                JButton edit = new JButton("Edit");
                edit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        editProduct(jList.getSelectedValue(), name.getText(), description.getText(), price.getText(), quantity.getText());
                        updateMarket.doClick();
                    }
                });

                edit.setVisible(false);
                name.setVisible(false);
                price.setVisible(false);
                description.setVisible(false);
                quantity.setVisible(false);
                jList.addListSelectionListener(new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        Product selected = getProduct(jList.getSelectedValue());
                        System.out.println(selected);
                        edit.setVisible(true);
                        name.setVisible(true);
                        price.setVisible(true);
                        description.setVisible(true);
                        quantity.setVisible(true);
                        if (selected != null) {
                            name.setText("Name: " + selected.getName());
                            price.setText(String.format("Price: $%.2f", selected.getSalePrice()));
                            description.setText("Description: " + selected.getDescription());
                            quantity.setText("Quantity: " + selected.getQuantity());
                        } else {
                            name.setText("Name");
                            price.setText("Price");
                            description.setText("Description");
                            quantity.setText("Quantity");
                        }
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
                ArrayList<Product> products;

                if (searchType.equalsIgnoreCase("name")) {
                    System.out.println("name search");
                    products = search(searchText.getText() + ",n/a,n/a");
                } else if (searchType.equalsIgnoreCase("store")) {
                    products = search("n/a," + searchText.getText() + ",n/a");
                } else if (searchType.equalsIgnoreCase("description")) {
                    products = search("n/a,n/a," + searchText.getText());
                } else {
                    System.out.println("dead");
                    products = null;
                }

//                System.out.println(products.isEmpty());

                if (products == null || products.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No matching results.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JFrame searchFrame = new JFrame("Search Results");
                searchFrame.setVisible(true);
                JPanel results = new JPanel();
                JTextField itemsFound = new JTextField("Results for " + searchType + " search:");
                itemsFound.setEditable(false);
                Container searchContainer = searchFrame.getContentPane();

                for (Product product : products) {
                    if (products.size() == 1) results.setLayout(new GridLayout(products.size(), products.size()));
                    else results.setLayout(new GridLayout(products.size() / 2, products.size() / 4));

                    //todo: breaks if products.size() == 1   <----- probably fixed
                    setProductButton(product, results);
                }
                results.updateUI();
                searchContainer.add(itemsFound, BorderLayout.NORTH);
                searchContainer.add(results, BorderLayout.CENTER);
                searchFrame.pack();
            } else if (e.getSource() == checkout) {
                Object[] options = {"Confirm", "Cancel"};
                int result = JOptionPane.showOptionDialog(null, "Are you sure you wish to checkout?", "Checkout", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                if (result == 0) {
                    String success = makePurchase();
                    if (success.equalsIgnoreCase("n")) {
                        JOptionPane.showMessageDialog(null, "One or more books have insufficient " + "stock.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Checked out successfully!", "", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } else if (e.getSource() == importSellerFile) {
                //TODO: change all the "string" stuff to stores
                JFrame importFrame = new JFrame("Import");
                importFrame.setVisible(true);
                JPanel content = new JPanel();
                JLabel file = new JLabel("File Path");
                JTextField filePath = new JTextField("File Path", 10);
                JButton importFile = new JButton("Import File");

                importFile.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean successCheck = importSellerFile(filePath.getText());

                        if (successCheck) {
                            JOptionPane.showMessageDialog(null, "File Imported", "Import", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "A file with that name does not" + "exist", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

//                for (Store store : stores) {
//                for (String string : strings) {
//                    JButton storeButton = new JButton(store.getName());
//                    JButton storeButton = new JButton(string);
//                    storeButton.addActionListener(new ActionListener() {
//                        @Override
//                        public void actionPerformed(ActionEvent e) {
//                            //TODO: when this button is clicked import the file path to the store selected
//                        }
//                    });
//                    storePanel.add(storeButton);
//                }
                content.add(file);
                content.add(filePath);
                content.add(importFile);

                importFrame.add(content);
                importFrame.pack();
            } else if (e.getSource() == exportSellerFile) {
                //TODO: change all the "string" stuff to stores
                JFrame exportFrame = new JFrame("Export");
                exportFrame.setVisible(true);
                JPanel content = new JPanel();
                JLabel file = new JLabel("File Path");
                JLabel store = new JLabel("Store Name");
                JTextField filePath = new JTextField("File Path", 10);
                JTextField storeName = new JTextField("Store Name", 10);
                JButton export = new JButton("Export to File");

                export.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean successCheck = exportSellerToFile(filePath.getText(), storeName.getText());

                        if (successCheck) {
                            JOptionPane.showMessageDialog(null, "File Exported", "Export", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "A file with that name already exists,\n" + "or Store Name is invalid.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
//                ArrayList<Store> stores = null;
//                String[] strings = {"String1", "String2", "String3", "String4"};
//                JPanel storePanel = new JPanel(new GridLayout(stores.size() / 2, stores.size() / 4));
//                JPanel storePanel = new JPanel(new GridLayout(strings.length / 2, strings.length / 4));
//
//                for (Store store : stores) {
//                for (String string : strings) {
//                    JButton storeButton = new JButton(store.getName());
//                    JButton storeButton = new JButton(string);
//                    storeButton.addActionListener(new ActionListener() {
//                        @Override
//                        public void actionPerformed(ActionEvent e) {
//                            //TODO: when this button is clicked export the file path to the store selected
//                        }
//                    });
//                    storePanel.add(storeButton);
//                }
                content.add(file);
                content.add(filePath);
                content.add(store);
                content.add(storeName);
                content.add(export);

                exportFrame.add(content);
                exportFrame.pack();
            } else if (e.getSource() == exportToFile) {
                JFrame exportFrame = new JFrame("Export");
                exportFrame.setVisible(true);
                JPanel content = new JPanel();
                JLabel file = new JLabel("File Path");
                JTextField filePath = new JTextField("File Path", 10);
                JButton export = new JButton("Export to File");

                export.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean success = exportToBuyerFile(filePath.getText());

                        if (success) {
                            JOptionPane.showMessageDialog(null, "File Exported", "Export", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "A file with that name already exists.\n" + "Choose another.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });
                content.add(file);
                content.add(filePath);
                content.add(export);

                exportFrame.add(content);
                exportFrame.pack();
            }
        }
    };

    public void run() {
        JFrame frame = new JFrame("Online Bookstore");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                closeSocket();
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
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
        productToAdd = new JTextField("Product name,store name,description,price,quantity", 40);
        productToAdd.addFocusListener(new FocusListener() { // Creates default text
            @Override
            public void focusGained(FocusEvent e) {
                if (productToAdd.getText().equals("Product name,store name,description,price,quantity")) {
                    productToAdd.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (productToAdd.getText().equals("")) {
                    productToAdd.setText("Product name,store name,description,price,quantity");
                }
            }
        });
        productToAdd.setToolTipText("Product name,store name,description,price,quantity");
        storeToAdd = new JTextField("Store name", 40);
        storeToAdd.addFocusListener(new FocusListener() { // Creates default text
            @Override
            public void focusGained(FocusEvent e) {
                if (storeToAdd.getText().equals("Store name")) {
                    storeToAdd.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (storeToAdd.getText().equals("")) {
                    storeToAdd.setText("Store name");
                }
            }
        });
        storeToAdd.setToolTipText("Store name");
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
        updateMarket = new JButton("↺");
        sortMarket.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
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
        exportSellerFile = new JButton("Export");
        exportSellerFile.addActionListener(actionListener);
        importSellerFile = new JButton("Import");
        importSellerFile.addActionListener(actionListener);

        viewShoppingCart = new JButton("Shopping Cart");
        viewShoppingCart.addActionListener(actionListener);
        viewPurchaseHistory = new JButton("Purchase History");
        viewPurchaseHistory.addActionListener(actionListener);
        purchase = new JButton("Add to Cart");
        purchase.addActionListener(actionListener);
        checkout = new JButton("Checkout");
        checkout.addActionListener(actionListener);
        exportToFile = new JButton("Export To File");
        exportToFile.addActionListener(actionListener);

        viewDashboard = new JButton("Dashboard");
        viewDashboard.addActionListener(actionListener);

        confirmAddProduct = new JButton("\u2713");
        confirmAddProduct.setPreferredSize(new Dimension(45, 25));
        confirmAddProduct.addActionListener(actionListener);
        confirmAddStore = new JButton("\u2713");
        confirmAddStore.setPreferredSize(new Dimension(45, 25));
        confirmAddStore.addActionListener(actionListener);

        quantity = new JSpinner();

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
        userBar.add(importSellerFile);
        userBar.add(exportSellerFile);
        userBar.add(viewShoppingCart);
        userBar.add(viewPurchaseHistory);
        userBar.add(checkout);
        userBar.add(exportToFile);

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
        importSellerFile.setVisible(false);
        exportSellerFile.setVisible(false);

        viewShoppingCart.setVisible(false);
        viewPurchaseHistory.setVisible(false);
        checkout.setVisible(false);
        exportToFile.setVisible(false);

        productPage = new JPanel();
        scrollPane = new JScrollPane(productPage);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        productPage.setBackground(Color.lightGray);
        scrollPane.setVisible(true);
        productPage.setVisible(true);
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (socket.isClosed()) {
                    connectSocket();
                }
                if (userType) { //logging in as buyer
                    writer.println(String.format("%d,%d,%s,%s", 0, 0, username.getText(), password.getText()));
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
                        JOptionPane.showMessageDialog(null, "Invalid Username or Password." + "\nEnter correct info or create an account.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else { //logging in as seller
                    writer.println(String.format("%d,%d,%s,%s", 1, 0, username.getText(), password.getText()));
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
                        JOptionPane.showMessageDialog(null, "Invalid Username or Password." + "\nEnter correct info or create an account.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                ArrayList<Product> products = getAllProducts("none");

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
                    exportToFile.setVisible(true);
                    purchase.setVisible(true);
                    quantity.setVisible(true);
                } else if (!status) {
                    viewStores.setVisible(true);
                    viewProducts.setVisible(true);
                    addStore.setVisible(true);
                    addProduct.setVisible(true);
                    checkout.setVisible(false);
                    removeProduct.setVisible(true);
                    editProduct.setVisible(true);
                    importSellerFile.setVisible(true);
                    exportSellerFile.setVisible(true);
                    purchase.setVisible(false);
                    quantity.setVisible(false);
                    exportToFile.setVisible(false);
                }
                System.out.println("Updated");
                updateMarket.doClick();
            }
        });
        signup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = username.getText();
                String pass = password.getText();

                if (!(user.contains("@") && user.contains(".com"))) {
                    JOptionPane.showMessageDialog(null, "Enter a valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
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
                        JOptionPane.showMessageDialog(null, "An account with that username " + "already exists.\n Choose a different one or sign in.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else {
                    writer.println(String.format("%d,%d,%s,%s", 1, 1, user, pass));
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
                        JOptionPane.showMessageDialog(null, "An account with that username " + "already exists.\n Choose a different one or sign in.", "Error", JOptionPane.ERROR_MESSAGE);
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
                    exportToFile.setVisible(true);
                } else if (!status) {
                    viewStores.setVisible(true);
                    viewProducts.setVisible(true);
                    addStore.setVisible(true);
                    addProduct.setVisible(true);
                    removeProduct.setVisible(true);
                    editProduct.setVisible(true);
                    exportToFile.setVisible(false);
                    importSellerFile.setVisible(true);
                    exportSellerFile.setVisible(true);
                }
                updateMarket.doClick();
            }
        });
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeSocket();

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
                importSellerFile.setVisible(false);
                exportSellerFile.setVisible(false);

                viewShoppingCart.setVisible(false);
                viewPurchaseHistory.setVisible(false);
                scrollPane.updateUI();
            }
        });

        updateMarket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int item = 0;
                productPage.removeAll();

                String choice = (String) sortMarket.getSelectedItem();
                ArrayList<Product> products = null;
                if (choice.equalsIgnoreCase("Sort By: Alphabetically")) {
                    products = getAllProducts("none");
                    Collections.sort(products, (p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));
                    //TODO:fix sorting issue
                } else if (choice.equalsIgnoreCase("Sort By: Price")) {
                    products = getAllProducts("price");
                } else if (choice.equalsIgnoreCase("Sort By: Quantity")) {
                    products = getAllProducts("quantity");
                }

                if (products == null || products.isEmpty()) return;

                productPage.setLayout(new GridLayout(products.size() / 2, products.size() / 4));
                purchaseListeners = new ArrayList<>();
                ArrayList<JButton> purchaseButtons = new ArrayList<>();


                for (Product product : products) {
                    setProductButton(product, productPage);
                }
                productPage.updateUI();
            }
        });
        container.add(topBar, BorderLayout.PAGE_START);
        container.add(userBar, BorderLayout.PAGE_END);
        container.add(scrollPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            return;
        }
        SwingUtilities.invokeLater(new Client());
    }

    public Client() {
        connectSocket();
    }

    private void addStore(String storeName) {
        try {
            writer.println("5," + storeName);
            System.out.println("adding store");
            System.out.println("5," + storeName);
            writer.flush();

            String response = (String) reader.readObject();
            if (response.equalsIgnoreCase("n")) {
                JOptionPane.showMessageDialog(null, "Error adding store!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Added store!", "", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addProduct(String productString) {
        try {
            writer.println("3," + productString);
            writer.flush();

            String response = (String) reader.readObject();
            if (response.equalsIgnoreCase("n")) {
                JOptionPane.showMessageDialog(null, "Error adding product!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Added product!", "", JOptionPane.INFORMATION_MESSAGE);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeProduct(String productName) {
        try {
            writer.println("12," + productName);
            writer.flush();
            System.out.println("removing " + productName);

            String response = (String) reader.readObject();
            if (response.equalsIgnoreCase("n")) {
                JOptionPane.showMessageDialog(null, "Error removing product!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Removed product!", "", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String[][] getStoreInfo() {
        String[][] stores = null;

        try {
            writer.println("10");
            writer.flush();

            stores = (String[][]) reader.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (stores == null) return new String[][]{};

        return stores;
    }

    /**
     * Get a string for the seller dashboard from the server
     *
     * @param sortType either "none", "alphabet", or "sales"
     * @return a string with store stats
     */
    private String getSellerDashboard(String sortType) {
        String dashboard = "";
        try {
            // TODO: figure out how to use/display dashboard
            writer.println("7," + sortType);
            writer.flush();

            dashboard = (String) reader.readObject();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error", "Error loading dashboard!", JOptionPane.ERROR_MESSAGE);
        }

        return dashboard;

    }

    private String getProductInfo(String productName) {
        Product product = getProduct(productName);

        return getProductInfo(product);

    }

    private String getProductInfo(Product product) {
        String info = String.format("------------------------------------------\n" + "%s\n" + "Store: %s | Price: $%.2f | Quantity: %d\n" + "Description: %s\n" + "------------------------------------------\n", product.getName(), product.getStoreName(), product.getSalePrice(), product.getQuantity(), product.getDescription());

        return info;

    }

    private Product getProduct(String productName) {
        writer.println("11," + productName);
        writer.flush();

        Product product = null;
        try {
            product = (Product) reader.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return product;
    }

    private void editProduct(String productName, String newName, String newDescription, String newPrice, String newQuantity) {
        System.out.println(productName + ", " + newName + ", " + newDescription + ", " + newPrice + ", " + newQuantity);
        if (productName != null) {
            try {
                if (newName.substring(0, 6).equals("Name: ") && newDescription.substring(0, 13).equals("Description: ") && newPrice.substring(0, 8).equals("Price: $") && newQuantity.substring(0, 10).equals("Quantity: ")) {
                    writer.printf("4,%s,%s,%s,%s,%s\n", productName, newName.substring(6), newDescription.substring(13), newPrice.substring(8), newQuantity.substring(10));

                    System.out.printf("4,%s,%s,%s,%s,%s\n", productName, newName.substring(6), newDescription.substring(13), newPrice.substring(8), newQuantity.substring(10));
                    writer.flush();

                    String response = (String) reader.readObject();
                    if (response.equalsIgnoreCase("n")) {
                        JOptionPane.showMessageDialog(null, "Error editing product!", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Edited product!", "", JOptionPane.INFORMATION_MESSAGE);
                    }


                } else {
                    JOptionPane.showMessageDialog(null, "Bad formatting!", "Error", JOptionPane.ERROR_MESSAGE);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<Product> getSellerProducts(String sortType) {
        writer.println("2," + sortType);
        writer.flush();
        System.out.println("2,none");

        ArrayList<Product> products = null;
        try {
            products = (ArrayList<Product>) reader.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(products);
        return products;
    }

    private ArrayList<Product> getProductsArray() {
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

    private String getString() {
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

    private ArrayList<String> getStringArray() {
        ArrayList<String> response;

        try {
            response = (ArrayList<String>) reader.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    private HashMap<Product, Integer> getProductHash() {
        HashMap<Product, Integer> products;

        try {
            products = (HashMap<Product, Integer>) reader.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return products;
    }

    public ArrayList<Product> getAllProducts(String condition) {
        writer.println("1," + condition);
        writer.flush();
        return getProductsArray();
    }

    public ArrayList<Product> search(String query) {
        System.out.println(query);
        writer.println("2," + query);
        writer.flush();
        return getProductsArray();
    }

    //TODO: fix visv being silly and sending null
    public String addToCart(Product product, int quantity) {
        writer.println(String.format("4,%d,%d", product.getIndex(), quantity));
        writer.flush();
        return getString();
    }

    public boolean exportToBuyerFile(String filename) {
        File testExistence = new File(filename);

        if (testExistence.exists()) {
            return false;
        } else {
            writer.println("5");
            writer.flush();

            ArrayList<String> fileInfo = getStringArray();

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
                PrintWriter exportWriter = new PrintWriter(bw);
                for (int i = 0; i < fileInfo.size(); i++) {
                    exportWriter.print(fileInfo.get(i));
                }
                exportWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    public boolean exportSellerToFile(String filename, String storeName) {
        File testExistence = new File(filename);

        if (testExistence.exists()) {
            return false;
        } else {
            writer.println("8," + storeName);
            writer.flush();

            ArrayList<String> fileInfo = getStringArray();

            if (fileInfo.isEmpty()) return false;

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
                PrintWriter exportWriter = new PrintWriter(bw);
                for (int i = 0; i < fileInfo.size(); i++) {
                    exportWriter.print(fileInfo.get(i));
                }
                exportWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    public boolean importSellerFile(String filename) {
        File testExistence = new File(filename);

        if (!testExistence.exists()) {
            return false;
        } else {
            String fileContent = "";
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                String line = br.readLine();
                while (line != null) {
                    fileContent += line + "\n";
                    line = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            fileContent = fileContent.substring(0, fileContent.length() - 2);

            writer.println("8," + fileContent);
            writer.flush();

            String successCheck = getString();

            if (successCheck.equalsIgnoreCase("y")) return true;
            else return false;
        }
    }

    private void setProductButton(Product product, JPanel panel) {
        JButton productButton = new JButton(product.getName());
        panel.add(productButton);
        productButtons.add(productButton);

        productButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame productFrame = new JFrame(product.getName());
                productFrame.setVisible(true);
                productFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JPanel productPanel = new JPanel();
                Container content = productFrame.getContentPane();
                SpinnerModel value = new SpinnerNumberModel(1, 1, product.getQuantity(), 1);
                quantity = new JSpinner(value);
                quantity.setPreferredSize(new Dimension(90, 25));
                quantity.setBackground(Color.WHITE);
                ((JSpinner.DefaultEditor) quantity.getEditor()).getTextField().setEditable(false);

                JTextArea info = new JTextArea(getProductInfo(product));
                info.setEditable(false);
                JButton addToCart = new JButton("Add to Cart");

                ActionListener purchaseListener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Sending purchase");
                        String success;
                        try {
                            if ((Integer) quantity.getValue() < 1) {
                                JOptionPane.showMessageDialog(null, "Cannot be 0 or negative", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            success = addToCart(product, (Integer) quantity.getValue());
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Enter an integer.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (success.equalsIgnoreCase("n")) {
                            JOptionPane.showMessageDialog(null, "Insufficient stock.", "Error", JOptionPane.ERROR_MESSAGE);
                        } else if (success.equalsIgnoreCase("y")) {
                            JOptionPane.showMessageDialog(null, "Added to cart.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }

                        productFrame.dispose();
                    }
                };

                addToCart.addActionListener(purchaseListener);
                purchaseListeners.add(purchaseListener);

                productButton.add(addToCart);
                productPanel.add(info);
                productPanel.add(addToCart);
                productPanel.add(quantity);
                content.add(productPanel);
                productFrame.pack();
                if (userType) {
                    quantity.setVisible(true);
                    addToCart.setVisible(true);
                } else {
                    quantity.setVisible(false);
                    addToCart.setVisible(false);
                }
            }
        };
        productButton.addActionListener(productButtonListener);
    }

    public String makePurchase() {
        writer.println("6");
        writer.flush();
        return getString();
    }

    public HashMap<Product, Integer> getShoppingCart() {
        writer.println("7");
        writer.flush();
        return getProductHash();
    }

    public String editCart(Product product, int newQuantity) {
        writer.println(String.format("8,%d,%d", product.getIndex(), newQuantity));
        writer.flush();
        return getString();
    }

    public HashMap<Product, Integer> getPurchaseHistory() {
        writer.println("9");
        writer.flush();
        return getProductHash();
    }

    public ArrayList<String> getBuyerDashboard(String condition) {
        writer.println("10," + condition);
        writer.flush();
        return getStringArray();
    }

    public void closeSocket() {
        try {
            System.out.println("Send -1");
            writer.println("-1");
            writer.flush();

            writer.close();
            reader.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectSocket() {
        try {
            socket = new Socket("localhost", 1001);
            System.out.println("sent request");

            writer = new PrintWriter(this.socket.getOutputStream());
            reader = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}