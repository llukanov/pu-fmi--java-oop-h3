public class OrderedProduct extends Product{
    private int quantity;

    private double totalPrice;

    public OrderedProduct(String name, String imageUrl, double price, int quantity, double totalPrice) {
        super(name, imageUrl, price);
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public int getQuantity () {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}


