package pt.up.fe.cmov16.client.clientapp.logic;

import io.swagger.client.model.Product;

public class ProductMenuItem {
    private int quantity;
    private Product product;

    public ProductMenuItem(Product product){
        this.product=product;
        quantity=0;
    }

    public void setQuantity(int quantity){
        this.quantity=quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public String getName() {
        return product.getName();
    }

    public Double getUnitPrice() {
        return product.getUnitprice();
    }
}
