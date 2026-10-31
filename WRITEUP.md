# Inventory Management System

This app helps a small shop owner keep track of products, prices and stock, and shows which items need restocking. The frontend is built with React, the backend is a REST API in Java Spring Boot, and the database is Supabase (PostgreSQL).

```
React (Frontend) ──Axios──▶ Spring Boot REST API ──JPA──▶ Supabase PostgreSQL
```

## Frontend

```
frontend/src/
├── App.jsx
├── api.js
├── main.jsx
├── index.css
├── components/
│   └── ProductTable.jsx
└── pages/
    ├── ProductList.jsx
    ├── AddProduct.jsx
    ├── EditProduct.jsx
    └── LowStock.jsx
```

### App.jsx

App.jsx is the main component of the app. It holds the navigation bar and all the routes, so it decides which page shows up for each URL. The pages call the backend through Axios, and all those calls are kept together in api.js.

| URL | Page | Backend endpoint |
|---|---|---|
| / | ProductList | GET /api/products, GET /api/products/search?q=, DELETE /api/products/{id} |
| /add | AddProduct | POST /api/products |
| /edit/:id | EditProduct | GET /api/products/{id}, PUT /api/products/{id} |
| /low-stock | LowStock | GET /api/products/low-stock |

On the products page, any item with less than 5 in stock is highlighted and marked "LOW", so it's easy to see what needs restocking.

## Backend

```
backend/src/main/java/com/shop/inventory/
├── InventoryApplication.java
├── controller/ProductController.java
├── model/Product.java
├── model/ProductUpdate.java
├── repository/ProductRepository.java
├── service/ProductService.java
└── exception/
    ├── GlobalExceptionHandler.java
    ├── ProductNotFoundException.java
    └── DuplicateProductException.java
```

### Controller class: ProductController

This is a REST controller mapped to /api/products. It has these endpoints:

- GET /api/products returns all products
- GET /api/products/{id} returns one product
- GET /api/products/search?q= searches by name or product ID
- GET /api/products/low-stock returns products with quantity below 5
- POST /api/products adds a new product
- PUT /api/products/{id} updates the price or quantity
- DELETE /api/products/{id} deletes a product

### Entity class: Product

Product is mapped to the products table. It has four fields:

- productId: the primary key, so two products can't have the same ID
- name: can't be empty
- price: can't be negative
- quantity: can't be negative

### Other classes

- ProductRepository extends JpaRepository and does the database work.
- ProductService has the main logic, like checking for a duplicate ID before saving.
- GlobalExceptionHandler sends back a proper error message: 400 for invalid input, 404 if the product isn't found, and 409 if the ID already exists.
- ProductUpdate is the request body for PUT. It only has price and quantity.

## Database

Used PostgreSQL hosted on Supabase. Spring Data JPA connects to it, and the products table gets created automatically when the backend starts.

The project is also deployed. The frontend is on Vercel (inventory-tt.vercel.app) and the backend is on Render.
