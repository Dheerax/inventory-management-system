package com.shop.inventory.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

// body for PUT - only price and quantity can change, both optional
public class ProductUpdate {

    @DecimalMin(value = "0.0", message = "Price cannot be negative")
    private Double price;

    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
