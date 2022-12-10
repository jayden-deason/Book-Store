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
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.UIManager;

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

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == viewDashboard) { //runs if dashboard button is pressed
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
                JMenuItem alphabet = new JMenuItem("By Alphabet");
                menu.add(products);
                menu.add(purchases);
                menu.add(alphabet);
                root.setJMenuBar(bar);
                if (status) { // buyer
                    alphabet.setVisible(false);
                } else { // seller
                    purchases.setVisible(false);
                }

                scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                products.addActionListener(new ActionListener() { //sorts by products sold
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("By products sold");

                        ArrayList<String> stores = null;
                        if (status) {
                            stores = getBuyerDashboard("sales");
                        } else {
                            stores = getSellerStores("sales");
                            System.out.println("getting seller stores");
                            System.out.println(stores);
                        }

                        Collections.reverse(stores);

                        panel.removeAll();
                        if (stores.size() < 4) {
                            panel.setLayout(new GridLayout(stores.size(), 1));
                        } else {
                            panel.setLayout(new GridLayout(stores.size() / 4, stores.size() / 2));
                        }

                        if (status) { // if buyer, adds button for each store from sorted list that display products
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
                                        JOptionPane.showMessageDialog(null, storeProducts,
                                                storeInfo[0] + " Products", JOptionPane.INFORMATION_MESSAGE);
                                    }
                                });
                                panel.add(storeButton);
                            }
                        } else {
                            for (String store : stores) {
                                JButton storeButton = new JButton(store);
                                storeButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        String storeInfo = getSellerStoreInfo(store);
                                        JOptionPane.showMessageDialog(null, storeInfo,
                                                store, JOptionPane.INFORMATION_MESSAGE);
                                    }
                                });
                                panel.add(storeButton);
                            }
                        }
                        panel.updateUI();
                    }
                });
                purchases.addActionListener(new ActionListener() { //sorts by purchase history
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
                        for (String store : stores) { //adds button for each store from sorted list that display products
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
                                    JOptionPane.showMessageDialog(null, storeProducts,
                                            storeInfo[0] + " Products", JOptionPane.INFORMATION_MESSAGE);
                                }
                            });
                            panel.add(storeButton);
                        }
                        panel.updateUI();
                    }
                });

                alphabet.addActionListener(new ActionListener() { //sorts alphabetically
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("By alphabet");

                        ArrayList<String> stores = getSellerStores("alphabet");

                        Collections.reverse(stores);

                        panel.removeAll();
                        if (stores.size() < 4) {
                            panel.setLayout(new GridLayout(stores.size(), 1));
                        } else {
                            panel.setLayout(new GridLayout(stores.size() / 4, stores.size() / 2));
                        }

                        for (String store : stores) { //adds button for each store from sorted list that display products
                            JButton storeButton = new JButton(store);
                            storeButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    String storeInfo = getSellerStoreInfo(store);
                                    JOptionPane.showMessageDialog(null, storeInfo,
                                            store, JOptionPane.INFORMATION_MESSAGE);
                                }
                            });
                            panel.add(storeButton);

                        }
                        panel.updateUI();
                    }
                });

                content.add(scrollPanel);
            } else if (e.getSource() == viewShoppingCart) { //runs if shopping cart button is pressed
                HashMap<Product, Integer> cart = getShoppingCart();

                if (cart.isEmpty()) { //error message for empty cart
                    JOptionPane.showMessageDialog(null, "Cart is empty.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JFrame shoppingCartFrame = new JFrame("Shopping Cart");
                shoppingCartFrame.setVisible(true);
                Container content = shoppingCartFrame.getContentPane();
                content.setLayout(new GridLayout(cart.keySet().size(), 3));

                for (Product product : cart.keySet()) { //displays items in cart and adds ability to edit cart
                    JTextField name = new JTextField(product.getName());
                    name.setEditable(false);

                    int quant = cart.get(product);


                    SpinnerModel qSpinModel = new SpinnerNumberModel(quant, 0, product.getQuantity(), 1);
                    JSpinner quantity = new JSpinner(qSpinModel);
                    quantity.setPreferredSize(new Dimension(90, 25));
                    quantity.setBackground(Color.WHITE);
                    ((JSpinner.DefaultEditor) quantity.getEditor()).getTextField().setEditable(false);

                    JButton confirm = new JButton("\u2713");
                    confirm.setPreferredSize(new Dimension(20, 35));
                    confirm.setToolTipText("Confirm new quantity");
                    confirm.addActionListener(new ActionListener() { //confirms editing quantity in cart
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
                            } catch (NumberFormatException ex) { //error message for negative number
                                JOptionPane.showMessageDialog(null, "Invalid argument. " +
                                        "Enter a positive number.", "Error", JOptionPane.ERROR_MESSAGE);
                                quantity.setValue(oldQuantity);
                                return;
                            }
                            if (successCheck.equalsIgnoreCase("n")) { //error for insufficient stock
                                JOptionPane.showMessageDialog(null, "Not enough stock. " +
                                        "Decrease the amount in your cart.", "Error", JOptionPane.ERROR_MESSAGE);
                                quantity.setValue(oldQuantity);

                            } else if (!quantity.getValue().equals(oldQuantity)) { //confirmation message
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
                    remove.addActionListener(new ActionListener() { //removes product from cart
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int oldQuantity = cart.get(product);

                            int cont = JOptionPane.showConfirmDialog(null,
                                    String.format("Are you sure you want to remove '%s' from your cart?", product.getName()),
                                    "Remove Product", JOptionPane.YES_NO_OPTION);
                            if (cont == 0) {
                                String successCheck;

                                successCheck = editCart(product, 0);

                                if (successCheck.equalsIgnoreCase("n")) { //general error message
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
            } else if (e.getSource() == viewPurchaseHistory) { //runs if purchase history button is pressed
                HashMap<Product, Integer> products = getPurchaseHistory();

                String history = "";
                for (Product product : products.keySet()) { //sets string of purchased products
                    history += product.getName() + ": " + products.get(product) + "\n";
                }
                JOptionPane.showMessageDialog(null, history, "Purchase History", JOptionPane.INFORMATION_MESSAGE);

            } else if (e.getSource() == viewStores) {
                JFrame storesFrame = new JFrame("Stores");
                storesFrame.setSize(500, 500);
                storesFrame.setVisible(true);
                Container content = storesFrame.getContentPane();
                JPanel panel = new JPanel();
                String[][] storeInfo = getStoreInfo();
                JTable table = new JTable(storeInfo, new String[]{"Store Name", "Sales", "Revenue", "Products in Carts"});
                for (int row = 0; row < table.getRowCount(); row++) {
                    int rowHeight = table.getRowHeight();

                    for (int column = 0; column < table.getColumnCount(); column++) {
                        Component comp = table.prepareRenderer(table.getCellRenderer(row, column), row, column);
                        rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
                    }

                    table.setRowHeight(row, rowHeight);
                }
                table.setEnabled(false);
                content.add(table);
                storesFrame.pack();
            } else if (e.getSource() == viewProducts) {
                JFrame productFrame = new JFrame("Products");
                productFrame.setSize(900, JFrame.MAXIMIZED_VERT);
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
                JTextArea savedText = new JTextArea("");
                savedText.setEditable(false);
                savedText.setBackground(infoPanel.getBackground());
                savedText.setFont(new Font(infoText.getFont().getName(), Font.PLAIN, 16));
                infoPanel.add(infoText);
                ArrayList<Product> products = getSellerProducts("none");
                infoPanel.setLayout(new GridLayout(products.size(), 1));
                int item = 0;
                for (Product product : products) {
                    JButton productButton = new JButton(product.getName());
                    panel.add(productButton);
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
                            infoText.setText(getProductInfo(product));
                            infoPanel.updateUI();
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                            infoText.setText("");
                        }
                    });
                    productButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JTextArea savedText = new JTextArea("");
                            savedText.setEditable(false);
                            savedText.setBackground(infoPanel.getBackground());
                            savedText.setText(getProductInfo(product.getIndex()));
                            savedText.setFont(new Font(infoText.getFont().getName(), Font.PLAIN, 16));
                            infoPanel.add(savedText);
                            productFrame.pack();
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
                updateMarket.doClick();
            } else if (e.getSource() == removeProduct) {
                JFrame removeProductFrame = new JFrame("Remove Product");
                removeProductFrame.setSize(400, 500);
                removeProductFrame.setVisible(true);

                ArrayList<Product> products = getSellerProducts("none");

                final JList<Product> jList = new JList(new Vector<Product>(products));
                jList.setCellRenderer(new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                        if (renderer instanceof JLabel && value instanceof Product) {
                            // Here value will be of the Type 'CD'
                            ((JLabel) renderer).setText(((Product) value).getName());
                        }
                        return renderer;
                    }
                });
                JPanel panel = new JPanel();
                JButton remove = new JButton("Remove");
                remove.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int cont = JOptionPane.showConfirmDialog(null,
                                String.format("Are you sure you want to remove '%s'?", jList.getSelectedValue().getName()),
                                "Remove Product", JOptionPane.YES_NO_OPTION);
                        if (cont == 0) {
                            removeProduct(jList.getSelectedValue().getIndex());
                            removeProductFrame.dispose();
                            removeProduct.doClick();
                            updateMarket.doClick();
                        }
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
                final JList<Product> jList = new JList(new Vector<Product>(products));
                jList.setCellRenderer(new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                        if (renderer instanceof JLabel && value instanceof Product) {
                            // Here value will be of the Type 'CD'
                            ((JLabel) renderer).setText(((Product) value).getName());
                        }
                        return renderer;
                    }
                });
                JPanel panel = new JPanel();
                JPanel panel2 = new JPanel();
                panel2.setLayout(new BoxLayout(panel2, BoxLayout.PAGE_AXIS));

                JTextField name = new JTextField("Name");
                JTextField price = new JTextField("Price");
                JTextField description = new JTextField("Description");
                JTextField quantity = new JTextField("Quantity");

                JButton edit = new JButton("Edit");
                edit.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Product p = jList.getSelectedValue();
                        int idx = jList.getSelectedIndex();
                        editProduct(p.getIndex(), name.getText(), description.getText(), price.getText(), quantity.getText());
                        updateMarket.doClick();
                        jList.setSelectedValue(getProduct(p.getIndex()), false);
                        DefaultListModel<Product> list = new DefaultListModel<>();
                        list.addAll(getSellerProducts("none"));

                        // reset view of all products to match name
                        jList.setModel(list);
                        jList.updateUI();
                        jList.setSelectedIndex(idx);
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
                        edit.setVisible(true);
                        name.setVisible(true);
                        price.setVisible(true);
                        description.setVisible(true);
                        quantity.setVisible(true);
                        if (jList.getSelectedValue() != null) {
                            Product product = getProduct(jList.getSelectedValue().getIndex());
                            name.setText("Name: " + product.getName());
                            price.setText(String.format("Price: $%.2f", product.getSalePrice()));
                            description.setText("Description: " + product.getDescription());
                            quantity.setText("Quantity: " + product.getQuantity());
                        } else {
                            name.setText("Name");
                            price.setText("Price");
                            description.setText("Description");
                            quantity.setText("Quantity");
                        }
                        panel2.updateUI();
                        editProductFrame.pack();
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
            } else if (e.getSource() == search) { //runs if search button is pressed
                ArrayList<Product> products;

                if (searchType.equalsIgnoreCase("name")) { //searching by product name
                    System.out.println("name search");
                    products = search(searchText.getText() + ",n/a,n/a", status);
                } else if (searchType.equalsIgnoreCase("store")) { //searching by store name
                    products = search("n/a," + searchText.getText() + ",n/a", status);
                } else if (searchType.equalsIgnoreCase("description")) { //searching by product description
                    products = search("n/a,n/a," + searchText.getText(), status);
                } else { //initializes error value for invalid search results
                    System.out.println("dead");
                    products = null;
                }

                if (products == null || products.isEmpty()) { //error message for no results
                    JOptionPane.showMessageDialog(null, "No matching results.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JFrame searchFrame = new JFrame("Search Results");
                searchFrame.setVisible(true);
                JPanel results = new JPanel();
                JTextField itemsFound = new JTextField("Results for " + searchType + " search: '" + searchText.getText() + "'");
                searchText.setText("");
                itemsFound.setEditable(false);
                Container searchContainer = searchFrame.getContentPane();

                for (Product product : products) { //displays window with search results
                    if (products.size() == 1)
                        results.setLayout(new GridLayout(products.size(), products.size()));
                    else
                        results.setLayout(new GridLayout(products.size() / 2, products.size() / 4));

                    setProductButton(product, results); //sets product buttons
                }
                results.updateUI();
                searchContainer.add(itemsFound, BorderLayout.NORTH);
                searchContainer.add(results, BorderLayout.CENTER);
                searchFrame.pack();
            } else if (e.getSource() == checkout) { //runs if checkout button is pressed
                Object[] options = {"Confirm", "Cancel"};
                int result = JOptionPane.showOptionDialog(null, //confirmation prompt
                        "Are you sure you wish to checkout?", "Checkout",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                if (result == 0) { //makes purchase if confirmed
                    String success = makePurchase();
                    if (success.equalsIgnoreCase("n")) { //error message for insufficient stock
                        JOptionPane.showMessageDialog(null, "One or more books have insufficient " +
                                "stock.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else { //success message
                        JOptionPane.showMessageDialog(null, "Checked out successfully!", "",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    updateMarket.doClick();
                }
            } else if (e.getSource() == importSellerFile) { //runs if seller presses import button
                JFrame importFrame = new JFrame("Import");
                importFrame.setVisible(true);
                JPanel content = new JPanel();
                JLabel file = new JLabel("File Path");
                JTextField filePath = new JTextField("File Path", 10);
                JButton importFile = new JButton("Import File");

                importFile.addActionListener(new ActionListener() { //runs when import button in prompt is pressed
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean successCheck = importSellerFile(filePath.getText());

                        if (successCheck) { //success message
                            JOptionPane.showMessageDialog(null, "File Imported", "Import",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else { //error message for file name not existing
                            JOptionPane.showMessageDialog(null, "A file with that name does not" +
                                    "exist", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                content.add(file);
                content.add(filePath);
                content.add(importFile);

                importFrame.add(content);
                importFrame.pack();
            } else if (e.getSource() == exportSellerFile) { //runs when export button is pressed by seller
                JFrame exportFrame = new JFrame("Export");
                exportFrame.setVisible(true);
                JPanel content = new JPanel();
                JLabel file = new JLabel("File Path");
                JLabel store = new JLabel("Store Name");
                JTextField filePath = new JTextField("", 10);
                JComboBox<String> storeName = new JComboBox<>(getSellerStores("alphabet").toArray(new String[0]));
                JButton export = new JButton("Export to File");

                export.addActionListener(new ActionListener() { //runs if export is pressed in prompt
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean successCheck = exportSellerToFile(filePath.getText(), (String) storeName.getSelectedItem());

                        if (successCheck) { //success message
                            JOptionPane.showMessageDialog(null, "File Exported", "Export",
                                    JOptionPane.INFORMATION_MESSAGE);
                            exportFrame.dispose();
                        } else { //error message for file name already existing
                            JOptionPane.showMessageDialog(null, "A file with that name already exists,\n" +
                                    "or Store Name is invalid.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                content.add(file);
                content.add(filePath);
                content.add(store);
                content.add(storeName);
                content.add(export);

                exportFrame.add(content);
                exportFrame.pack();
            } else if (e.getSource() == exportToFile) { //runs if export button is pressed by buyer
                JFrame exportFrame = new JFrame("Export");
                exportFrame.setVisible(true);
                JPanel content = new JPanel();
                JLabel file = new JLabel("File Path");
                JTextField filePath = new JTextField("", 10);
                JButton export = new JButton("Export to File");

                export.addActionListener(new ActionListener() { //runs when export is pressed in prompt
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean success = exportToBuyerFile(filePath.getText());

                        if (success) { //success message
                            JOptionPane.showMessageDialog(null, "File Exported", "Export",
                                    JOptionPane.INFORMATION_MESSAGE);
                            exportFrame.dispose();
                        } else { //error message for file name already existing
                            JOptionPane.showMessageDialog(null, "A file with that name already exists.\n" +
                                    "Choose another.", "Error", JOptionPane.ERROR_MESSAGE);
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
        frame.setSize(new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 600,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 300));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
        searchText.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    search.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

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
        exportToFile = new JButton("Export Purchase History");
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

        sortMarket.setVisible(false);
        search.setVisible(false);
        searchOptions.setVisible(false);
        searchText.setVisible(false);
        updateMarket.setVisible(false);

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
                    if (response.equalsIgnoreCase("N")) { //error message for invalid account info
                        JOptionPane.showMessageDialog(null, "Invalid Username or Password." +
                                "\nEnter correct info or create an account.", "Error", JOptionPane.ERROR_MESSAGE);
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
                    if (response.equalsIgnoreCase("N")) { //error message for invalid account info
                        JOptionPane.showMessageDialog(null, "Invalid Username or Password." +
                                "\nEnter correct info or create an account.", "Error", JOptionPane.ERROR_MESSAGE);
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

                sortMarket.setVisible(true);
                search.setVisible(true);
                searchOptions.setVisible(true);
                searchText.setVisible(true);
                updateMarket.setVisible(true);
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
                System.out.println("Updated marketplace");
                updateMarket.doClick();
            }
        });
        signup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (socket.isClosed()) {
                    connectSocket();
                }

                String user = username.getText();
                String pass = password.getText();

                if (!isValidEmail(user)) { //error message for invalid email
                    JOptionPane.showMessageDialog(null, "Enter a valid email address.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (userType) { //signing up as buyer
                    writer.printf("%d,%d,%s,%s\n", 0, 1, user, pass);
                    writer.flush();
                    String response = null;
                    try {
                        response = (String) reader.readObject();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (response == null || response.equalsIgnoreCase("N")) { //error message for pre-existing account info
                        JOptionPane.showMessageDialog(null, "An account with that username " +
                                        "already exists.\n Choose a different one or sign in.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } else { //signing up as seller
                    writer.println(String.format("%d,%d,%s,%s", 1, 1, user, pass));
                    writer.flush();
                    String response = null;
                    try {
                        response = (String) reader.readObject();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (response.equalsIgnoreCase("N")) { //error message for pre-existing account info
                        JOptionPane.showMessageDialog(null, "An account with that username " +
                                        "already exists.\n Choose a different one or sign in.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                ArrayList<Product> products;
                try {
                    products = getAllProducts("none");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                isBuyer.setVisible(false);
                isSeller.setVisible(false);
                login.setVisible(false);
                signup.setVisible(false);
                logout.setVisible(true);
                userBar.setVisible(true);
                viewDashboard.setVisible(true);
                scrollPane.setVisible(true);

                sortMarket.setVisible(true);
                search.setVisible(true);
                searchOptions.setVisible(true);
                searchText.setVisible(true);
                updateMarket.setVisible(true);
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

                sortMarket.setVisible(false);
                search.setVisible(false);
                searchOptions.setVisible(false);
                searchText.setVisible(false);
                updateMarket.setVisible(false);

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
                if (choice.equalsIgnoreCase("Sort By: Alphabetically")) { //sorting alphabetically
                    products = getAllProducts("none");
                    Collections.sort(products, (p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));
                } else if (choice.equalsIgnoreCase("Sort By: Price")) { //sorting by price
                    products = getAllProducts("price");
                } else if (choice.equalsIgnoreCase("Sort By: Quantity")) { //sorting by quantity
                    products = getAllProducts("quantity");
                }

                if (products == null || products.isEmpty()) //ends if there are no products in the marketplace
                    return;

                productPage.setLayout(new GridLayout(products.size() / 2, products.size() / 4));
                purchaseListeners = new ArrayList<>();
                ArrayList<JButton> purchaseButtons = new ArrayList<>();

                productButtons.clear();
                for (Product product : products) {
                    setProductButton(product, productPage); //sets and displays products buttons
                }
                productPage.setVisible(false);
                productPage.setVisible(true);
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

    public Client() { //initializes a client by starting a connection and reader/writer
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

    private void removeProduct(int productIndex) {
        try {
            writer.println("12," + productIndex);
            writer.flush();
            System.out.println("removing " + productIndex);

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

    private ArrayList<String> getSellerStores(String sortType) {
        writer.println("7," + sortType);
        writer.flush();

        ArrayList<String> out = null;

        try {
            out = (ArrayList<String>) reader.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;

    }

    private String getSellerStoreInfo(String storeName) {
        writer.println("13," + storeName);
        writer.flush();

        String out = "";
        try {
            out = (String) reader.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;
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

    private String getProductInfo(int productIndex) {
        Product product = getProduct(productIndex);

        return getProductInfo(product);

    }

    private String getProductInfo(Product product) {
        String line1 = product.getName() + "\n";
        String line2 = String.format("Store %s | Price $%.2f | Quantity: %d\n",
                product.getStoreName(), product.getSalePrice(), product.getQuantity());
        String line3 = "Description: " + product.getDescription();
        String dashes = getDashes(Math.max(line1.length(), Math.max(line2.length(), line3.length())) - 5);


        return dashes + "\n" + line1 + line2 + line3 + "\n" + dashes;

    }

    private String getDashes(int length) {
        String out = "";
        for (int i = 0; i < length; i++) {
            out += "-";
        }
        return out;
    }

    private Product getProduct(int productIndex) {
        writer.println("11," + productIndex);
        writer.flush();

        Product product = null;
        try {
            product = (Product) reader.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return product;
    }

    private void editProduct(int productIndex, String newName, String newDescription, String newPrice, String newQuantity) {
        try {
            if (newName.length() > 6 && newName.substring(0, 6).equals("Name: ") &&
                    newDescription.length() > 13 && newDescription.substring(0, 13).equals("Description: ") &&
                    newPrice.length() > 8 && newPrice.substring(0, 8).equals("Price: $") &&
                    newQuantity.length() > 10 && newQuantity.substring(0, 10).equals("Quantity: ")) {

                // setting quantity to 0
                if (Integer.parseInt(newQuantity.substring(10)) == 0) {
                    int cont = JOptionPane.showConfirmDialog(null,
                            "Are you sure you want to remove '" + newName.substring(6) + "'?",
                            "", JOptionPane.YES_NO_OPTION);

                    if (cont == 0) removeProduct(productIndex);
                    return;
                }

                writer.printf("4,%d,%s,%s,%s,%s\n",
                        productIndex,
                        newName.substring(6),
                        newDescription.substring(13),
                        newPrice.substring(8),
                        newQuantity.substring(10)
                );

                System.out.printf("4,%d,%s,%s,%s,%s\n",
                        productIndex,
                        newName.substring(6),
                        newDescription.substring(13),
                        newPrice.substring(8),
                        newQuantity.substring(10)
                );
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

        return products;
    }

    /**
     * Helper method to get ArrayList of marketplace products from server
     *
     * @return ArrayList of products
     */
    private ArrayList<Product> getProductsArray() {
        ArrayList<Product> products = new ArrayList<>();

        try {
            products = (ArrayList<Product>) reader.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        System.out.println(products);
        return products;
    }

    /**
     * Helper method to get a String from the server
     *
     * @return String response
     */
    private String getString() {
        String response = "";
        try {
            response = (String) reader.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Helper method to get ArrayList of Strings from the server
     *
     * @return ArrayList of Strings
     */
    private ArrayList<String> getStringArray() {
        ArrayList<String> response = new ArrayList<>();

        try {
            response = (ArrayList<String>) reader.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * Helper method to get HashMap of Products and their quantities from the server
     *
     * @return HashMap of quantities with Product keys
     */
    private HashMap<Product, Integer> getProductHash() {
        HashMap<Product, Integer> products = new HashMap<>();

        try {
            products = (HashMap<Product, Integer>) reader.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    /**
     * Gets ArrayList of Products from server sorted by 'condition'
     *
     * @param condition
     * @return Sorted ArrayList of all Products
     */
    public ArrayList<Product> getAllProducts(String condition) {
        writer.println("1," + condition);
        System.out.println("1," + condition);
        writer.flush();
        return getProductsArray();
    }

    /**
     * Gets ArrayList of Products from server that match the search 'query'
     *
     * @param query
     * @return ArrayList of search results
     */
    public ArrayList<Product> search(String query, boolean status) {
        if (status) { // buyer
            writer.println("2," + query);
            System.out.println("2," + query);
        } else { // seller
            writer.println("14," + query);
            System.out.println("14," + query);
        }
        writer.flush();
        return getProductsArray();
    }

    /**
     * Adds 'quantity' number of 'product' to the buyers shopping cart
     *
     * @param product
     * @param quantity
     * @return String indicating success
     */
    public String addToCart(Product product, int quantity) {
        writer.println(String.format("4,%d,%d", product.getIndex(), quantity));
        System.out.println(String.format("4,%d,%d", product.getIndex(), quantity));

        writer.flush();
        return getString();
    }

    /**
     * Exports the buyers purchase history to the file at path 'filename'
     *
     * @param filename
     * @return boolean value indicating success
     */
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

    /**
     * Exports the products in the sellers store 'storeName' to a file at path 'filename'
     *
     * @param filename
     * @param storeName
     * @return boolean value indicating success
     */
    public boolean exportSellerToFile(String filename, String storeName) {
        File testExistence = new File(filename);

        // check regex invalid chars in filename
        if (filename.matches(".*[/\n\r\t\0\f`?*\\<>|\":].*")) {
            return false;
        }

        if (testExistence.exists()) {
            return false;
        } else {
            writer.println("8," + storeName);
            writer.flush();

            ArrayList<String> fileInfo = getStringArray();

            if (fileInfo.isEmpty())
                return false;

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
                PrintWriter exportWriter = new PrintWriter(bw);
                for (int i = 0; i < fileInfo.size(); i++) {
                    exportWriter.println(fileInfo.get(i));
                }
                exportWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    /**
     * Imports products for a seller from a file at path 'filename'
     *
     * @param filename
     * @return boolean value indicating success
     */
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

            if (successCheck.equalsIgnoreCase("y"))
                return true;
            else
                return false;
        }
    }

    /**
     * Sets and displays button for 'product' in 'panel'
     *
     * @param product
     * @param panel
     */
    private void setProductButton(Product product, JPanel panel) {
        String productLabel = String.format(
                "<html><h1 style=\"text-align:center\">%s</h1>" +
                        "<p style=\"text-align:center\">Store: %s<br />$%.2f</p></html>",
                product.getName(), product.getStoreName(), product.getSalePrice());
        JButton productButton = new JButton(productLabel);
        panel.add(productButton);
        productButtons.add(productButton);

        ActionListener productButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                purchaseListeners.clear();
                System.out.println(product.getName());
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
                info.setBackground(productFrame.getBackground());
                JButton addToCart = new JButton("Add to Cart");

                ActionListener purchaseListener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Adding to cart");
                        String success;
                        try {
                            if ((Integer) quantity.getValue() < 1) {
                                JOptionPane.showMessageDialog(null, "Cannot be 0 or negative",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            success = addToCart(product,
                                    (Integer) quantity.getValue());
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Enter an integer.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (success.equalsIgnoreCase("n")) {
                            JOptionPane.showMessageDialog(null, "Insufficient stock.",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        } else if (success.equalsIgnoreCase("y")) {
                            JOptionPane.showMessageDialog(null, "Added to cart.",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
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

    /**
     * Purchases the products in the buyers cart
     *
     * @return String indicating success
     */
    public String makePurchase() {
        writer.println("6");
        writer.flush();
        return getString();
    }

    /**
     * Gets the buyers shopping cart
     *
     * @return HashMap of quantities with product keys
     */
    public HashMap<Product, Integer> getShoppingCart() {
        writer.println("7");
        writer.flush();
        return getProductHash();
    }

    /**
     * Edits the quantity of 'product' in the cart to be 'newQuantity'
     *
     * @param product
     * @param newQuantity
     * @return String indicating success
     */
    public String editCart(Product product, int newQuantity) {
        writer.println(String.format("8,%d,%d", product.getIndex(), newQuantity));
        writer.flush();
        return getString();
    }

    /**
     * Gets the buyers purchase history
     *
     * @return HashMap of quantities with product keys
     */
    public HashMap<Product, Integer> getPurchaseHistory() {
        writer.println("9");
        writer.flush();
        return getProductHash();
    }

    /**
     * Gets the information to be displayed in the buyers dashboard sorted by 'condition'
     *
     * @param condition
     * @return ArrayList of Strings to be displayed
     */
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

    private boolean isValidEmail(String user) {
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(user);
        return matcher.find();

    }
}