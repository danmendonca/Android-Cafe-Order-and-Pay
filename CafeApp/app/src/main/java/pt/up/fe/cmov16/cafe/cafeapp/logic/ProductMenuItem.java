package pt.up.fe.cmov16.cafe.cafeapp.logic;


import java.io.Serializable;

import io.swagger.client.model.Product;

public class ProductMenuItem implements Serializable {
    private int quantity;
    private Product product;

    public ProductMenuItem(Product product){
        this.product=product;
        quantity=0;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public int getId() {
        return product.getId();
    }

    public void setUnitPrice(Double unitPrice) {
        product.setUnitprice(unitPrice);
    }

    public void setName(String name) {
        this.product.setName(name);
    }
}
