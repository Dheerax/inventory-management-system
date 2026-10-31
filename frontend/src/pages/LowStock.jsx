import { useEffect, useState } from "react";
import { getLowStock, deleteProduct, errorMessage } from "../api";
import ProductTable from "../components/ProductTable";

function LowStock() {
  const [products, setProducts] = useState([]);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getLowStock()
      .then((res) => setProducts(res.data))
      .catch((err) => setError(errorMessage(err)))
      .finally(() => setLoading(false));
  }, []);

  const handleDelete = async (id) => {
    if (!window.confirm(`Delete product ${id}?`)) return;
    try {
      await deleteProduct(id);
      setProducts(products.filter((p) => p.productId !== id));
    } catch (err) {
      setError(errorMessage(err));
    }
  };

  return (
    <section>
      <h2>Low stock</h2>
      <p className="muted">Products with fewer than 5 units left.</p>
      {error && <p className="error">{error}</p>}
      {loading ? <p className="muted">Loading…</p> : <ProductTable products={products} onDelete={handleDelete} />}
    </section>
  );
}

export default LowStock;
