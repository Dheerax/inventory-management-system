# Inventory Management System — Write-up

**Stack:** React.js (frontend) · Java Spring Boot REST API (backend) · Supabase PostgreSQL (database)

---

## 1. FRONTEND

### App.jsx

**Purpose:** App.jsx is the root component. It sets up routing with React Router, shows the
navigation bar (Products, Low stock, Add product) and decides which page to display for each URL.
The pages talk to the backend through Axios functions kept in `api.js`.

```jsx
<BrowserRouter>
  <header>
    <NavLink to="/">Products</NavLink>
    <NavLink to="/low-stock">Low stock</NavLink>
    <NavLink to="/add">Add product</NavLink>
  </header>
  <Routes>
    <Route path="/"          element={<ProductList />} />
    <Route path="/add"       element={<AddProduct />} />
    <Route path="/edit/:id"  element={<EditProduct />} />
    <Route path="/low-stock" element={<LowStock />} />
    <Route path="*"          element={<p>Page not found.</p>} />
  </Routes>
</BrowserRouter>
```

**Routes and the backend endpoints they use** (base URL `http://localhost:8080/api`)

| Frontend URL | Page | Backend endpoint | What it does |
|---|---|---|---|
| `/` | ProductList | `GET /products` | Shows all products with quantities. Low-stock rows are highlighted |
| `/` (search box) | ProductList | `GET /products/search?q=` | Search by name or product ID |
| `/` (Delete) | ProductList | `DELETE /products/{id}` | Removes a product |
| `/add` | AddProduct | `POST /products` | Form to add a product (ID, name, price, qty) |
| `/edit/:id` | EditProduct | `GET /products/{id}`, `PUT /products/{id}` | Loads the product, then updates price / quantity |
| `/low-stock` | LowStock | `GET /products/low-stock` | Lists only items with quantity below 5 |
| `*` | — | — | Unknown URL shows "Page not found" |

**Low-stock warning:** every product from the backend has a `lowStock` field. Rows with
`lowStock = true` get an orange background and a "LOW" tag, and a message at the top says
how many items need restocking.

---

## 2. BACKEND (Spring Boot)

Package `com.shop.inventory`, layered as **Controller → Service → Repository → Database**.

### Controller class — `ProductController`

`@RestController` with `@RequestMapping("/api/products")`. `@CrossOrigin` allows the React app to call it.

| Method | Endpoint | Method name | Response |
|---|---|---|---|
| GET | `/api/products` | `getAll()` | 200, list of products |
| GET | `/api/products/{id}` | `getOne()` | 200 or 404 |
| GET | `/api/products/search?q=` | `search()` | 200, matching products |
| GET | `/api/products/low-stock` | `lowStock()` | 200, products with qty < 5 |
| POST | `/api/products` | `add()` | 201 Created, 400 or 409 |
| PUT | `/api/products/{id}` | `update()` | 200, 400 or 404 |
| DELETE | `/api/products/{id}` | `delete()` | 204 or 404 |

`@Valid @RequestBody` checks the input before the method runs.

### Entity class — `Product`

`@Entity @Table(name = "products")`

| Field | Type | Annotations |
|---|---|---|
| productId | String | `@Id`, `@NotBlank` → primary key, so it is always unique |
| name | String | `@NotBlank` |
| price | Double | `@NotNull`, `@DecimalMin("0.0")` → cannot be negative |
| quantity | Integer | `@NotNull`, `@Min(0)` → cannot be negative |

`isLowStock()` returns `quantity < 5`. It is sent in the JSON as `"lowStock"`.

### Other classes

- **ProductUpdate (DTO):** the body for PUT. It only has `price` and `quantity`, both optional and
  both non-negative, so the ID and name can't be changed by mistake.
- **ProductRepository:** `extends JpaRepository<Product, String>`. It provides save, findAll,
  findById, delete, plus two custom query methods:
  - `findByNameContainingIgnoreCaseOrProductIdContainingIgnoreCase()` for search
  - `findByQuantityLessThan(5)` for low stock
- **ProductService (`@Service`):** business logic. It checks for a duplicate ID before saving
  (`existsById`), throws `ProductNotFoundException` when an ID doesn't exist, and applies partial updates.
- **GlobalExceptionHandler (`@RestControllerAdvice`):** turns exceptions into JSON `{"error": "..."}`:
  - `ProductNotFoundException` → 404
  - `DuplicateProductException` → 409
  - validation failure → 400 (e.g. "Price cannot be negative")
  - bad JSON → 400
- **InventoryApplication:** the main class with `@SpringBootApplication`.

---

## 3. DATABASE

**Chosen database:** PostgreSQL on **Supabase** (a cloud-hosted Postgres).

- Connected with the PostgreSQL JDBC driver and Spring Data JPA (Hibernate).
- `spring.jpa.hibernate.ddl-auto=update` creates the table automatically.
- The credentials (`DB_URL`, `DB_USER`, `DB_PASSWORD`) are kept in environment variables / `.env`, not in the code.

```sql
products(
  product_id VARCHAR(50) PRIMARY KEY,
  name       VARCHAR(255) NOT NULL,
  price      FLOAT NOT NULL,
  quantity   INTEGER NOT NULL
)
```

**Setup:** create a Supabase project → copy the session pooler connection details → put them
in `backend/.env` → run `mvn spring-boot:run`.

---

## 4. VALIDATION AND ERROR HANDLING

| Rule | Frontend (React) | Backend (Spring) |
|---|---|---|
| ID and name required | form check before submit | `@NotBlank` |
| Price ≥ 0 | check + `min="0"` | `@DecimalMin("0.0")` |
| Quantity ≥ 0, whole number | check + `min="0"` | `@Min(0)` |
| Unique product ID | shows server message | `existsById` → 409 |
| Product not found | shows server message | 404 |

The frontend shows the backend's `error` message in red under the form. If the server is down,
it shows "Cannot reach server".

---

## 5. TESTING

- **Unit/integration tests:** `ProductControllerTest` uses `@SpringBootTest` + MockMvc with an
  in-memory H2 database. All **10 tests pass** (`mvn test`): list, add, duplicate ID (409),
  negative price (400), update stock, negative quantity on update (400), search by name and ID,
  low stock, 404 for an unknown ID, and delete.
- **End-to-end:** tested from the React UI through Spring Boot to Supabase. I added products,
  edited price and quantity, searched, deleted, checked the low-stock highlight, and tried
  invalid input and duplicate IDs.
- **API checks:** tested each endpoint with curl/Postman.
