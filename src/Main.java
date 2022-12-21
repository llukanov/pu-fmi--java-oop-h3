import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;
import javax.swing.text.html.parser.Parser;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static javax.swing.GroupLayout.Alignment.*;
public class Main {
    static Shop shop = new Shop("My Shop");

    static JFrame frame = new JFrame("FlowLayoutDemo");
    static JFrame categoryFrame = new JFrame("FlowLayoutDemo");


    private static ArrayList<Category> categoryList = DBReader.readCategory(shop);

    private static ArrayList<OrderedProduct> orderedProducts = new ArrayList();


    private static ArrayList<JButton> buttons = new ArrayList<>();

    public static void main(String[] args) {
        shop.setCategories(DBReader.readCategory(shop));
        DBReader.readProducts(shop);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set up the content pane.
        addComponentsToPane(frame.getContentPane(), "home", "Home", null);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void addComponentsToPane(final Container pane, String pageType, String pageName, String sortingType) {
        final JPanel categoriesPanel = new JPanel();
        categoriesPanel.setLayout(new BoxLayout(categoriesPanel, BoxLayout.Y_AXIS));

        JPanel controls = new JPanel();
        controls.setLayout(new FlowLayout());

        for (Category category:
                categoryList) {
            JButton categoryButton = new JButton(category.getName());
            categoryButton.setFont(new Font("Arial", Font.PLAIN, 16));
            categoryButton.setBorder(null);
            Border border = categoryButton.getBorder();
            Border margin = new EmptyBorder(10,10,10,10);
            categoryButton.setBorder(new CompoundBorder(border, margin));

            categoryButton.setContentAreaFilled(false);

            categoryButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae)
                {
                    frame.dispose();
                    categoryFrame.dispose();

                    categoryFrame = new JFrame(pageName);

                    viewCategoryFrame(categoryButton.getText(), "name");
                }
            });


            categoriesPanel.add(categoryButton);
        }

        categoriesPanel.setComponentOrientation(
                ComponentOrientation.LEFT_TO_RIGHT);

        if (pageType.equals("home")) {
            pane.add(addWelcomeLabel(), BorderLayout.LINE_END);
        } else if (pageType.equals("category")) {
            pane.add(addCategoryPanel(pageName, sortingType), BorderLayout.LINE_END);
        }


        pane.add(categoriesPanel, BorderLayout.LINE_START);
        
    }

    public static JLabel addWelcomeLabel() {
        JLabel welcomeLabel = new JLabel("WELCOME!!!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        Border border = welcomeLabel.getBorder();
        Border margin = new EmptyBorder(10,100,10,100);
        welcomeLabel.setBorder(new CompoundBorder(border, margin));
        welcomeLabel.setOpaque(true);
        welcomeLabel.setBackground(Color.WHITE);
        welcomeLabel.setForeground(Color.BLUE);
        
        return welcomeLabel;
    }

    public static void viewCategoryFrame(String pageName, String sortingType) {

        categoryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set up the content pane.
        addComponentsToPane(categoryFrame.getContentPane(), "category", pageName, sortingType);
        //Display the window.
        categoryFrame.pack();
        categoryFrame.setVisible(true);
    }

    public static JPanel addCategoryPanel(String pageName, String sortingType){
        JPanel productsPanel = new JPanel();
        productsPanel.setLayout(new BoxLayout(productsPanel, BoxLayout.Y_AXIS));

        // Category name
        JLabel categoryNameLabel = new JLabel(pageName.toUpperCase(), SwingConstants.CENTER);
        categoryNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        categoryNameLabel.setBorder(new CompoundBorder(categoryNameLabel.getBorder(), new EmptyBorder(10,0,20,0)));
        categoryNameLabel.setOpaque(true);
        categoryNameLabel.setForeground(Color.BLUE);
        productsPanel.add(categoryNameLabel);

        // Get category
        Category foundCategory = shop.getCategories().stream().filter(category -> pageName.equals(category.getName())).findFirst().orElse(null);

        assert foundCategory != null;
        if(foundCategory.getProducts().size() > 0){


            JPanel grandPanel = new JPanel();
            grandPanel.setLayout(new BoxLayout(grandPanel, BoxLayout.Y_AXIS));

            // Sort Panel
            JPanel sortPanel = new JPanel();
            sortPanel.setLayout(new BoxLayout(sortPanel, BoxLayout.X_AXIS));
            grandPanel.add(sortPanel);

            JLabel sortLabel = new JLabel("Сортиране: ");
            String[] sortingTypes ={"Наименование", "Цена"};
            JComboBox sortComboBox = new JComboBox<>(sortingTypes);
            JButton sortButton = new JButton("Sort");
            sortButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae)
                {
                    frame.dispose();
                    categoryFrame.dispose();

                    categoryFrame = new JFrame(pageName);

                    if (sortComboBox.getSelectedItem().toString().equals("Наименование")) {
                        viewCategoryFrame(pageName, "name");
                    }
                    else {
                        viewCategoryFrame(pageName, "price");
                    }

                }
            });

            sortPanel.add(sortLabel);
            sortPanel.add(sortComboBox);
            sortPanel.add(sortButton);


            JPanel panel = new JPanel();
            grandPanel.add(sortPanel);
            grandPanel.add(panel);


            panel.setLayout(new GridLayout(2, 4));

            if (sortingType.equals("name")) {
                foundCategory.getProducts().sort(Comparator.comparing(Product::getName));
            }
            else {
                foundCategory.getProducts().sort(Comparator.comparing(Product::getPrice));
            }

            for (Product product:
                 foundCategory.getProducts()) {
                JPanel productPanel = new JPanel();
                Border productPBorder = categoryNameLabel.getBorder();
                Border productPMargin = new EmptyBorder(15,10,15,10);
                productPanel.setBorder(new CompoundBorder(productPBorder, productPMargin));
                productPanel.setOpaque(true);
                productPanel.setLayout(new BoxLayout (productPanel, BoxLayout.Y_AXIS));

                JLabel productNameLabel = new JLabel(product.getName(), SwingConstants.CENTER);
                productNameLabel.setFont(new Font("Arial", Font.ITALIC, 16));
                productNameLabel.setBackground(Color.WHITE);
                productNameLabel.setForeground(Color.BLUE);
                productPanel.add(productNameLabel);

                JLabel productImageLabel = new JLabel(new ImageIcon(product.getImageUrl()));
                productImageLabel.setPreferredSize(new Dimension(100, 100));
                productPanel.add(productImageLabel);

                JLabel productPriceLabel = new JLabel(String.valueOf(product.getPrice()), SwingConstants.CENTER);
                productPriceLabel.setFont(new Font("Arial", Font.ITALIC, 16));
                productPriceLabel.setBackground(Color.WHITE);
                productPriceLabel.setForeground(Color.BLUE);
                productPanel.add(productPriceLabel);

                JPanel quantityPanel = new JPanel();
                quantityPanel.setLayout(new BoxLayout (quantityPanel, BoxLayout.X_AXIS));
                Icon addToBasketIcon = new ImageIcon("src/files/add-to-cart.png");
                JButton addToBasketButton = new JButton(addToBasketIcon);

                JFormattedTextField productQuantityCB = new JFormattedTextField(
                        createFormatter("#"));
                productQuantityCB.setValue(1);

                quantityPanel.add(productQuantityCB);

                addToBasketButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae)
                    {
                        OrderedProduct orderedProduct = new OrderedProduct(product.getName(), product.getImageUrl(), product.getPrice(), Integer.parseInt(productQuantityCB.getValue().toString()),  Integer.parseInt(productQuantityCB.getValue().toString()) * product.getPrice());
                        orderedProducts.add(orderedProduct);
                    }
                });

                quantityPanel.add(addToBasketButton);

                productPanel.add(quantityPanel);

                panel.add(productPanel);
            }
            productsPanel.add(grandPanel);



        }
        else {
            JLabel errorLabel = new JLabel("Няма продукти в тази категория!!!", SwingConstants.CENTER);
            errorLabel.setFont(new Font("Arial", Font.ITALIC, 16));
            Border border = errorLabel.getBorder();
            Border margin = new EmptyBorder(10,100,10,100);
            errorLabel.setBorder(new CompoundBorder(border, margin));
            errorLabel.setOpaque(true);
            errorLabel.setBackground(Color.WHITE);
            errorLabel.setForeground(Color.BLUE);
            productsPanel.add(errorLabel);
        }


        Icon orderIcon = new ImageIcon("src/files/add-to-cart.png");
        JButton orderButton = new JButton(orderIcon);
        orderButton.setSize(60, 60);
        orderButton.setBackground(Color.WHITE);
        orderButton.setToolTipText("Брой артикули:" + String.valueOf(orderedProducts.size()));
        productsPanel.add(orderButton);



        orderButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JDialog modelDialog = createDialog(categoryFrame);
                modelDialog.setVisible(true);
            }
        });



        return productsPanel;
    }


    private static JDialog createDialog(JFrame frame){
        final JDialog modelDialog = new JDialog(frame, "Swing Tester",
                Dialog.ModalityType.DOCUMENT_MODAL);
        modelDialog.setBounds(132, 132, 450, 200);
        Container dialogContainer = modelDialog.getContentPane();
        dialogContainer.setLayout(null);

        JButton okButton = new JButton("Ok");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modelDialog.setVisible(false);
            }
        });

        JLabel nameTitleLabel = new JLabel("Име на продукт:");
        nameTitleLabel.setBounds(20, 20, 100, 20);
        JLabel priceTitleLabel = new JLabel("Единична цена:");
        priceTitleLabel.setBounds(130, 20, 100, 20);
        JLabel quantityTitleLabel = new JLabel("Количество:");
        quantityTitleLabel.setBounds(240, 20, 100, 20);
        JLabel totalPriceTitleLabel = new JLabel("Общо:");
        totalPriceTitleLabel.setBounds(350, 20, 100, 20);

        dialogContainer.add(nameTitleLabel);
        dialogContainer.add(priceTitleLabel);
        dialogContainer.add(quantityTitleLabel);
        dialogContainer.add(totalPriceTitleLabel);


        int counter = 1;
        for (OrderedProduct orderedProduct:
             orderedProducts) {
            JLabel nameProductLabel = new JLabel(orderedProduct.getName());
            nameProductLabel.setBounds(20, counter * 50, 100, 20);

            JLabel priceProductLabel = new JLabel(String.valueOf(orderedProduct.getPrice()));
            priceProductLabel.setBounds(130, counter * 50, 100, 20);

            JFormattedTextField quantityProductTextField = new JFormattedTextField(
                    createFormatter("#"));
            quantityProductTextField.setValue(orderedProduct.getQuantity());
            quantityProductTextField.setBounds(240, counter * 50, 70, 20);


            JButton editOrderedQuantity = new JButton("Р");
            editOrderedQuantity.setBounds(310, counter * 50, 30, 20);

            JLabel totalPriceProductLabel = new JLabel(String.valueOf(orderedProduct.getTotalPrice()));
            totalPriceProductLabel.setBounds(350, counter * 50, 100, 30);

            editOrderedQuantity.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    orderedProduct.setTotalPrice(orderedProduct.getPrice() * Integer.parseInt(quantityProductTextField.getText()));
                    totalPriceProductLabel.setText(String.valueOf(orderedProduct.getTotalPrice()));
                }
            });

            dialogContainer.add(nameProductLabel);
            dialogContainer.add(priceProductLabel);
            dialogContainer.add(quantityProductTextField);
            dialogContainer.add(totalPriceProductLabel);
            dialogContainer.add(editOrderedQuantity);

            counter++;
        }

        JButton payWithCardButton = new JButton("кредитна/дебитна карта");

        payWithCardButton.setBounds(20, counter * 50, 150, 50);

        dialogContainer.add(payWithCardButton);
        payWithCardButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                payAndPrintInvoice("кредитна/дебитна карта");
            }
        });

        JButton payWithPaypalButton = new JButton("Paypal");

        payWithPaypalButton.setBounds(200, counter * 50, 150, 50);

        dialogContainer.add(payWithPaypalButton);
        payWithPaypalButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                payAndPrintInvoice("Paypal");
            }
        });

        return modelDialog;
    }

    public static void payAndPrintInvoice(String paymentMethod) {
        int randomNumber = (int) (Math.random() * 100000);
        File f = new File("src/files/" + randomNumber + ".txt");

        FileWriter fw = null;
        try {
            fw = new FileWriter(f,true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try{
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("\t\t\t\t" + shop.getName());
            bw.newLine();
            bw.write("-----------------------------------------------------------");
            bw.newLine();
            bw.write("Поръчка: " + randomNumber + "\t Направена на "+ LocalDateTime.now());
            bw.newLine();
            bw.write("-----------------------------------------------------------");
            bw.newLine();

            double total = 0;
            for (OrderedProduct orderedProduct:
                    orderedProducts) {
                bw.write(orderedProduct.getName() + "\t" + orderedProduct.getPrice() + " * " + orderedProduct.getQuantity() + "\t" + "\t = " + orderedProduct.getTotalPrice());
                bw.newLine();

                total += orderedProduct.getTotalPrice();
            }

            bw.newLine();

            bw.write("-----------------------------------------------------------");
            bw.newLine();

            bw.write("Обща сума за плащане: " + total);
            bw.newLine();

            bw.write("Метод на плащане: " + paymentMethod);

            bw.flush();
            bw.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    protected static MaskFormatter createFormatter(String input) {
        MaskFormatter formatter = null;

        try {
            formatter = new MaskFormatter(input);
        } catch (java.text.ParseException exc) {
            System.exit(-1);
        }

        return formatter;
    }
}