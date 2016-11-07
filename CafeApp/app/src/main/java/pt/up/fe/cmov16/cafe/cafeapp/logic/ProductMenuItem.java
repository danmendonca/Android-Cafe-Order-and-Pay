package pt.up.fe.cmov16.cafe.cafeapp.logic;


import java.io.Serializable;

import io.swagger.client.model.Product;

public class ProductMenuItem implements Serializable {
    private int quantity;
    private Product product;
    private int prodId;

    public ProductMenuItem(Product product){
        this.product=product;
        quantity=0;
        prodId = product.getId();
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
        return (prodId != 0) ? prodId : 1;
    }
}
