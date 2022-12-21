import java.util.ArrayList;
import java.util.List;

public class Category {
    private String name;
    private List<Product> productList = new ArrayList<Product>();

    private String parrentCategory;

    public Category(String name, String parrentCategory) {
        this.name = name;
        this.parrentCategory = parrentCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return productList;
    }

    public void addProduct(Product product) {
        this.productList.add(product);
    }

}
