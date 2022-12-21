import java.util.ArrayList;
import java.util.List;

public class Shop {
    private String name;
    public List<Category> categories = new ArrayList<>();

    public Shop (String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }
}
