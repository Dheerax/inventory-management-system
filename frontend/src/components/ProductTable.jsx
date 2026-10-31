import { Link } from "react-router-dom";

function ProductTable({ products, onDelete }) {
  if (products.length === 0) {
    return <p className="muted">No products found.</p>;
  }

  return (
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th className="num">Price (₹)</th>
          <th className="num">Qty</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        {products.map((p) => (
          <tr key={p.productId} className={p.lowStock ? "low" : ""}>
            <td className="mono">{p.productId}</td>
            <td>{p.name}</td>
            <td className="num">{p.price.toFixed(2)}</td>
            <td className="num">
              {p.quantity}
              {p.lowStock && <span className="tag">low</span>}
            </td>
            <td className="actions">
              <Link to={`/edit/${encodeURIComponent(p.productId)}`}>Edit</Link>
              <button className="link danger" onClick={() => onDelete(p.productId)}>
                Delete
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}

export default ProductTable;
