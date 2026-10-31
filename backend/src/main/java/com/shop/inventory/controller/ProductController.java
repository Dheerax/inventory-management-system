package com.shop.inventory.controller;

import com.shop.inventory.model.Product;
import com.shop.inventory.model.ProductUpdate;
import com.shop.inventory.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    // GET /api/products  -> all products
    @GetMapping
    public List<Product> getAll() {
        return service.getAll();
    }

    // GET /api/products/search?q=milk  -> match on name or id
    @GetMapping("/search")
    public List<Product> search(@RequestParam("q") String q) {
        return service.search(q.trim());
    }

    // GET /api/products/low-stock  -> quantity < 5
    @GetMapping("/low-stock")
    public List<Product> lowStock() {
        return service.getLowStock();
    }

    // GET /api/products/{id}
    @GetMapping("/{id}")
    public Product getOne(@PathVariable String id) {
        return service.getById(id);
    }

    // POST /api/products  -> add new product
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product add(@Valid @RequestBody Product product) {
        return service.add(product);
    }

    // PUT /api/products/{id}  -> update price and/or quantity
    @PutMapping("/{id}")
    public Product update(@PathVariable String id, @Valid @RequestBody ProductUpdate changes) {
        return service.update(id, changes);
    }

    // DELETE /api/products/{id}
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
