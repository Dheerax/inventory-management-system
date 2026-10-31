# Inventory Management System

A small app for a shop owner to keep track of products, prices and stock, and to see which items need restocking.

- **Frontend:** React (Vite) + React Router + Axios
- **Backend:** Java 21, Spring Boot 3 REST API (Spring Web, Spring Data JPA, Bean Validation)
- **Database:** PostgreSQL hosted on **Supabase**

## Features

1. Add a product (product ID, name, price, quantity)
2. View all products with their current quantities
3. Update a product's price or stock quantity
4. Search products by name or product ID
5. Low-stock warning when quantity is below 5 (highlighted rows + separate Low stock page)

Rules: product IDs are unique, price and quantity cannot be negative. These are checked in the frontend and enforced again in the backend.

## Project structure

```
backend/   Spring Boot REST API
frontend/  React app
```

## Database setup (Supabase)

1. Create a free project at https://supabase.com (any region).
2. Go to **Project Settings → Database → Connection string → Session pooler** and note the host, user and password.
3. Copy `backend/.env.example` to `backend/.env` and fill it in:
   ```
   DB_URL=jdbc:postgresql://aws-0-<region>.pooler.supabase.com:5432/postgres?sslmode=require
   DB_USER=postgres.<project-ref>
   DB_PASSWORD=<your-db-password>
   ```
   (These can also be set as normal environment variables.)
4. No SQL script needed. Hibernate creates the `products` table on first start (`ddl-auto=update`):

   ```sql
   CREATE TABLE products (
     product_id VARCHAR(50) PRIMARY KEY,
     name       VARCHAR(255) NOT NULL,
     price      FLOAT NOT NULL,
     quantity   INTEGER NOT NULL
   );
   ```

## Running

Backend (port 8080):
```bash
cd backend
mvn spring-boot:run
```

Frontend (port 5173):
```bash
cd frontend
npm install
npm run dev
```
The frontend calls `http://localhost:8080/api` by default. To change it, set `VITE_API_URL` in `frontend/.env`.

## REST API

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/products` | List all products |
| GET | `/api/products/{id}` | Get one product |
| GET | `/api/products/search?q=` | Search by name or product ID |
| GET | `/api/products/low-stock` | Products with quantity < 5 |
| POST | `/api/products` | Add a product |
| PUT | `/api/products/{id}` | Update price and/or quantity |
| DELETE | `/api/products/{id}` | Delete a product |

Every product in a response includes `lowStock: true/false`.

Errors come back as `{"error": "message"}`:
- `400` validation failed (missing field, negative price/quantity)
- `404` product not found
- `409` product ID already exists

Example:
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"productId":"P001","name":"Milk 1L","price":30,"quantity":24}'
```

## Testing

Backend tests use MockMvc against an in-memory H2 database, so they don't touch Supabase:
```bash
cd backend
mvn test
```
10 tests cover add, list, update, search, low stock, delete, duplicate IDs, negative values and 404s.

The app was also tested end to end: React UI → Spring Boot → Supabase (add, edit, search, delete, low-stock highlight and error messages).
