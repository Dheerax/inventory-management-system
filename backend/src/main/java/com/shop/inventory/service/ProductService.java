package com.shop.inventory.service;

import com.shop.inventory.exception.DuplicateProductException;
import com.shop.inventory.exception.ProductNotFoundException;
import com.shop.inventory.model.Product;
import com.shop.inventory.model.ProductUpdate;
import com.shop.inventory.repository.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private static final int LOW_STOCK_LIMIT = 5;

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> getAll() {
        return repo.findAll(Sort.by("productId"));
    }

    public Product getById(String id) {
        return repo.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public List<Product> search(String query) {
        return repo.findByNameContainingIgnoreCaseOrProductIdContainingIgnoreCase(query, query);
    }

    public List<Product> getLowStock() {
        return repo.findByQuantityLessThan(LOW_STOCK_LIMIT);
    }

    public Product add(Product product) {
        product.setProductId(product.getProductId().trim());
        product.setName(product.getName().trim());
        if (repo.existsById(product.getProductId())) {
            throw new DuplicateProductException(product.getProductId());
        }
        return repo.save(product);
    }

    public Product update(String id, ProductUpdate changes) {
        Product product = getById(id);
        if (changes.getPrice() != null) product.setPrice(changes.getPrice());
        if (changes.getQuantity() != null) product.setQuantity(changes.getQuantity());
        return repo.save(product);
    }

    public void delete(String id) {
        Product product = getById(id);
        repo.delete(product);
    }
}
