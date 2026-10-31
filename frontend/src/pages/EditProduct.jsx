import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getProduct, updateProduct, errorMessage } from "../api";

function EditProduct() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [product, setProduct] = useState(null);
  const [price, setPrice] = useState("");
  const [quantity, setQuantity] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    getProduct(id)
      .then((res) => {
        setProduct(res.data);
        setPrice(res.data.price);
        setQuantity(res.data.quantity);
      })
      .catch((err) => setError(errorMessage(err)));
  }, [id]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (price === "" || Number(price) < 0) return setError("Price cannot be empty or negative");
    if (quantity === "" || !Number.isInteger(Number(quantity)) || Number(quantity) < 0)
      return setError("Quantity must be a whole number, 0 or more");

    try {
      await updateProduct(id, { price: Number(price), quantity: Number(quantity) });
      navigate("/");
    } catch (err) {
      setError(errorMessage(err));
    }
  };

  if (!product) {
    return <section>{error ? <p className="error">{error}</p> : <p className="muted">Loading…</p>}</section>;
  }

  return (
    <section>
      <h2>Edit {product.name}</h2>
      <p className="muted mono">{product.productId}</p>
      <form className="form" onSubmit={handleSubmit}>
        <label>
          Price (₹)
          <input type="number" step="0.01" min="0" value={price} onChange={(e) => setPrice(e.target.value)} />
        </label>
        <label>
          Quantity in stock
          <input type="number" step="1" min="0" value={quantity} onChange={(e) => setQuantity(e.target.value)} />
        </label>

        {error && <p className="error">{error}</p>}
        <div className="row">
          <button type="submit">Update</button>
          <button type="button" className="plain" onClick={() => navigate(-1)}>Cancel</button>
        </div>
      </form>
    </section>
  );
}

export default EditProduct;
