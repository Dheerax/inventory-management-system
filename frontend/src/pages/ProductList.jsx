import { useEffect, useState } from "react";
import { getProducts, searchProducts, deleteProduct, errorMessage } from "../api";
import ProductTable from "../components/ProductTable";

function ProductList() {
  const [products, setProducts] = useState([]);
  const [query, setQuery] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  const load = async (q = "") => {
    setError("");
    try {
      const res = q ? await searchProducts(q) : await getProducts();
      setProducts(res.data);
    } catch (err) {
      setError(errorMessage(err));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const handleSearch = (e) => {
    e.preventDefault();
    load(query.trim());
  };

  const clearSearch = () => {
    setQuery("");
    load();
  };

  const handleDelete = async (id) => {
    if (!window.confirm(`Delete product ${id}?`)) return;
    try {
      await deleteProduct(id);
      setProducts(products.filter((p) => p.productId !== id));
    } catch (err) {
      setError(errorMessage(err));
    }
  };

  const lowCount = products.filter((p) => p.lowStock).length;

  return (
    <section>
      <h2>Products</h2>

      <form className="search" onSubmit={handleSearch}>
        <input
          placeholder="Search by name or product ID"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
        <button type="submit">Search</button>
        {query && (
          <button type="button" className="plain" onClick={clearSearch}>
            Clear
          </button>
        )}
      </form>

      {lowCount > 0 && (
        <p className="warning">
          {lowCount} item{lowCount > 1 ? "s are" : " is"} below 5 units and need restocking.
        </p>
      )}
      {error && <p className="error">{error}</p>}

      {loading ? <p className="muted">Loading…</p> : <ProductTable products={products} onDelete={handleDelete} />}
    </section>
  );
}

export default ProductList;
