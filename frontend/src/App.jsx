import { BrowserRouter, Routes, Route, NavLink } from "react-router-dom";
import ProductList from "./pages/ProductList";
import AddProduct from "./pages/AddProduct";
import EditProduct from "./pages/EditProduct";
import LowStock from "./pages/LowStock";

function App() {
  return (
    <BrowserRouter>
      <header className="top">
        <span className="brand">Inventory</span>
        <nav>
          <NavLink to="/" end>Products</NavLink>
          <NavLink to="/low-stock">Low stock</NavLink>
          <NavLink to="/add">Add product</NavLink>
        </nav>
      </header>

      <main>
        <Routes>
          <Route path="/" element={<ProductList />} />
          <Route path="/add" element={<AddProduct />} />
          <Route path="/edit/:id" element={<EditProduct />} />
          <Route path="/low-stock" element={<LowStock />} />
          <Route path="*" element={<p>Page not found.</p>} />
        </Routes>
      </main>
    </BrowserRouter>
  );
}

export default App;
