package com.shop.inventory.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @NotBlank(message = "Product ID is required")
    @Column(name = "product_id", length = 50)
    private String productId;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", message = "Price cannot be negative")
    @Column(nullable = false)
    private Double price;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    @Column(nullable = false)
    private Integer quantity;

    public Product() {}

    public Product(String productId, String name, Double price, Integer quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // low stock = quantity below 5, sent to frontend as "lowStock"
    public boolean isLowStock() {
        return quantity != null && quantity < 5;
    }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
