import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { addProduct, errorMessage } from "../api";

const empty = { productId: "", name: "", price: "", quantity: "" };

function validate(form) {
  if (!form.productId.trim()) return "Product ID is required";
  if (!form.name.trim()) return "Name is required";
  if (form.price === "" || isNaN(form.price)) return "Enter a valid price";
  if (Number(form.price) < 0) return "Price cannot be negative";
  if (form.quantity === "" || !Number.isInteger(Number(form.quantity))) return "Enter a whole number for quantity";
  if (Number(form.quantity) < 0) return "Quantity cannot be negative";
  return "";
}

function AddProduct() {
  const [form, setForm] = useState(empty);
  const [error, setError] = useState("");
  const [saving, setSaving] = useState(false);
  const navigate = useNavigate();

  const change = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    const problem = validate(form);
    if (problem) return setError(problem);

    setSaving(true);
    setError("");
    try {
      await addProduct({
        productId: form.productId.trim(),
        name: form.name.trim(),
        price: Number(form.price),
        quantity: Number(form.quantity),
      });
      navigate("/");
    } catch (err) {
      setError(errorMessage(err));
      setSaving(false);
    }
  };

  return (
    <section>
      <h2>Add product</h2>
      <form className="form" onSubmit={handleSubmit}>
        <label>
          Product ID
          <input name="productId" value={form.productId} onChange={change} />
        </label>
        <label>
          Name
          <input name="name" value={form.name} onChange={change} />
        </label>
        <label>
          Price (₹)
          <input name="price" type="number" step="0.01" min="0" value={form.price} onChange={change} />
        </label>
        <label>
          Quantity in stock
          <input name="quantity" type="number" step="1" min="0" value={form.quantity} onChange={change} />
        </label>

        {error && <p className="error">{error}</p>}
        <button type="submit" disabled={saving}>{saving ? "Saving…" : "Save product"}</button>
      </form>
    </section>
  );
}

export default AddProduct;
