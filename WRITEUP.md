# Inventory Management System

This app helps a small shop owner keep track of products, prices and stock, and shows which items need restocking. The frontend is built with React, the backend is a REST API in Java Spring Boot, and the database is Supabase (PostgreSQL).

## System Architecture

```
 ┌──────────────┐   HTTP / JSON   ┌───────────────────────┐    JPA    ┌──────────────┐
 │    React     │ ──────────────▶ │  Spring Boot REST API │ ────────▶ │   Supabase   │
 │  (Frontend)  │     (Axios)     │       (Backend)       │   (SQL)   │  PostgreSQL  │
 │              │ ◀────────────── │                       │ ◀──────── │              │
 └──────────────┘                 └───────────────────────┘           └──────────────┘
                                   Controller → Service →
                                   Repository
```

## Features Implemented

1. Add a product with a product ID, name, price and quantity in stock.
2. Display all products with their current quantities.
3. Update a product's price or stock quantity.
4. Search products by name or product ID.
5. Low-stock warning: items with a quantity below 5 are highlighted and marked "LOW". There is also a separate Low stock page that lists only those items.
6. Delete a product.

Validation: product IDs must be unique, and price and quantity can't be negative. These are checked on the frontend and again on the backend. Errors such as a duplicate ID or a negative price show up as a message on the form.

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

| Method | Endpoint | Description |
|---|---|---|
| GET | /api/products | Get all products |
| GET | /api/products/{id} | Get one product |
| GET | /api/products/search?q= | Search by name or product ID |
| GET | /api/products/low-stock | Products with quantity below 5 |
| POST | /api/products | Add a new product |
| PUT | /api/products/{id} | Update price or quantity |
| DELETE | /api/products/{id} | Delete a product |

Error responses: 400 for invalid input (like a negative price), 404 if the product isn't found, and 409 if the product ID already exists.

### Entity class: Product

Product is mapped to the products table. It has four fields:

- productId: the primary key, so two products can't have the same ID
- name: can't be empty
- price: can't be negative
- quantity: can't be negative

### Other classes

- ProductRepository extends JpaRepository and does the database work.
- ProductService has the main logic, like checking for a duplicate ID before saving.
- GlobalExceptionHandler sends back a proper error message for invalid input, a missing product or a duplicate ID.
- ProductUpdate is the request body for PUT. It only has price and quantity.

## Database

Used PostgreSQL hosted on Supabase. Spring Data JPA connects to it, and the products table gets created automatically when the backend starts.

| Column | Type | Rule |
|---|---|---|
| product_id | VARCHAR | Primary key (unique) |
| name | VARCHAR | Required |
| price | FLOAT | Cannot be negative |
| quantity | INTEGER | Cannot be negative |

The project is also deployed. The frontend is on Vercel (inventory-tt.vercel.app) and the backend is on Render.

## How to Run

Backend (runs on port 8080). The Supabase connection details go in backend/.env first.

```
cd backend
mvn spring-boot:run
```

Frontend (runs on port 5173):

```
cd frontend
npm install
npm run dev
```

Run the backend tests:

```
cd backend
mvn test
```
