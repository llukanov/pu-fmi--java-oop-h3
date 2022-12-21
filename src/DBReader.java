import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DBReader {
    private static final String PERSON_FILE_EXTENSION = ".file";
    private static final String PERSON_FILE_NAME = "online_shop_database";
    private static final String FILE_PATH = "src/files/";
    private static final String FULL_PATH = FILE_PATH + PERSON_FILE_NAME + PERSON_FILE_EXTENSION;

    private static final String PRODUCTS_FILE_NAME = "db_table_products";

    private static final String PRODUCTS_FULL_PATH = FILE_PATH + PRODUCTS_FILE_NAME + PERSON_FILE_EXTENSION;

    private static ArrayList<Category> categoryList = new ArrayList<>();

    public static ArrayList<Category> readCategory(Shop shop) {
        try {
            FileInputStream fileStream = new FileInputStream(FULL_PATH);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileStream));
            String stringLine;

            while((stringLine = bufferedReader.readLine()) != null) {
                String[] data = stringLine.split("\t");

                for (String category:
                     data) {
                    if (category.equals(data[0])) {
                        addCategory(category, null);
                    }
                    else {
                        addCategory(category, data[0]);
                    }
                }
            }

        } catch (FileNotFoundException ex) {
            System.err.println("Файлът не е намерен!");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.err.println("Възникна грешка по време на прочитането на данните от файла!");
            ex.printStackTrace();
        }

        Category[] categories = new Category[categoryList.size()];
        return categoryList;
    }

    private static void addCategory(String name, String parrentCategory) {
        Category category = new Category(name, parrentCategory);
        categoryList.add(category);
    }

    public static Shop readProducts(Shop shop) {
        try {
            FileInputStream fileStream = new FileInputStream(PRODUCTS_FULL_PATH);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileStream));
            String stringLine;

            while((stringLine = bufferedReader.readLine()) != null) {
                String[] lines = stringLine.split("\n");

                for (String line:
                     lines) {
                    String[] data = stringLine.split(",");

                    Product product = new Product(data[0], data[2], Double.parseDouble(data[1]));
                    String productCategory = data[3];


                    for (int i = 0; i < shop.getCategories().size(); i++)
                        if (shop.getCategories().get(i).getName().equals(productCategory)) {
                            shop.getCategories().get(i).addProduct(product);

                            String tr = "";
                        }
                }


            }

        } catch (FileNotFoundException ex) {
            System.err.println("Файлът не е намерен!");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.err.println("Възникна грешка по време на прочитането на данните от файла!");
            ex.printStackTrace();
        }

        return shop;
    }
}
