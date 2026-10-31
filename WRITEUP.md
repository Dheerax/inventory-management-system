# Inventory Management System

## Structure

```
React (Frontend)
      │  Axios
      ▼
Spring Boot REST API (Backend)
      │  JPA
      ▼
Supabase PostgreSQL (Database)
```

## Frontend

### App.jsx

Handles the routes of the app.

- `/` → all products, search, delete → `GET`, `DELETE /api/products`
- `/add` → add product → `POST /api/products`
- `/edit/:id` → update price / qty → `PUT /api/products/{id}`
- `/low-stock` → items below 5 → `GET /api/products/low-stock`

## Backend

### Controller class

`ProductController`: has the REST endpoints at `/api/products` (GET, POST, PUT, DELETE).

### Entity class

`Product`: productId, name, price, quantity

### Other

- `ProductService`: the logic
- `ProductRepository`: talks to the database
- `GlobalExceptionHandler`: handles errors
