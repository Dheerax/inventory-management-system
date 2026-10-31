import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "http://localhost:8080/api",
});

export const getProducts = () => api.get("/products");
export const getProduct = (id) => api.get(`/products/${encodeURIComponent(id)}`);
export const searchProducts = (q) => api.get("/products/search", { params: { q } });
export const getLowStock = () => api.get("/products/low-stock");
export const addProduct = (product) => api.post("/products", product);
export const updateProduct = (id, changes) => api.put(`/products/${encodeURIComponent(id)}`, changes);
export const deleteProduct = (id) => api.delete(`/products/${encodeURIComponent(id)}`);

// pulls the message out of {"error": "..."} sent by the backend
export const errorMessage = (err) =>
  err.response?.data?.error || (err.response ? "Request failed" : "Cannot reach server");
