# Inventory Management System

React.js + Spring Boot REST API + Supabase (PostgreSQL)

```
React (Frontend) ──Axios──▶ Spring Boot REST API ──JPA──▶ Supabase PostgreSQL
```

## FRONTEND

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

The main component. It sets up the routes and the nav bar, and shows the right page for each URL.
Pages call the backend with Axios (in `api.js`).

| URL | Page | Endpoint used |
|---|---|---|
| `/` | ProductList | `GET /api/products`, `GET /api/products/search?q=`, `DELETE /api/products/{id}` |
| `/add` | AddProduct | `POST /api/products` |
| `/edit/:id` | EditProduct | `GET /api/products/{id}`, `PUT /api/products/{id}` |
| `/low-stock` | LowStock | `GET /api/products/low-stock` |

Products with quantity below 5 are highlighted with a "LOW" tag.

## BACKEND

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

`@RestController`, base path `/api/products`

- `GET /` gets all products
- `GET /{id}` gets one product
- `GET /search?q=` searches by name or ID
- `GET /low-stock` gets products with qty < 5
- `POST /` adds a product
- `PUT /{id}` updates price or quantity
- `DELETE /{id}` deletes a product

### Entity class: Product

`@Entity`, table `products`

- `productId` (String, `@Id`, so it is unique)
- `name` (String, `@NotBlank`)
- `price` (Double, `@DecimalMin(0)`, cannot be negative)
- `quantity` (Integer, `@Min(0)`, cannot be negative)

### Other classes

- **ProductRepository:** extends JpaRepository for database operations
- **ProductService:** business logic and the duplicate ID check
- **GlobalExceptionHandler:** returns errors as 400, 404 or 409 with a message
- **ProductUpdate:** request body for PUT (price, quantity)

## DATABASE

Supabase PostgreSQL, connected using Spring Data JPA. The table is created automatically.

Live: https://inventory-tt.vercel.app
