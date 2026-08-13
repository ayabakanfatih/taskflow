import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { api, setToken } from "../api/client";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [fullName, setFullName] = useState("");
  const [isRegister, setIsRegister] = useState(false);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  async function handleSubmit(event) {
    event.preventDefault();
    setError("");
    setLoading(true);

    try {
      if (isRegister) {
        await api.register(email, fullName, password);
      }
      const result = await api.login(email, password);
      setToken(result.token);
      navigate("/tasks");
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="auth-wrapper">
      <form className="auth-card" onSubmit={handleSubmit}>
        <h1>TaskFlow</h1>
        <p className="subtitle">
          {isRegister ? "Yeni hesap oluştur" : "Hesabına giriş yap"}
        </p>

        {isRegister && (
          <input
            type="text"
            placeholder="Ad Soyad"
            value={fullName}
            onChange={(e) => setFullName(e.target.value)}
            required
          />
        )}

        <input
          type="email"
          placeholder="E-posta"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />

        <input
          type="password"
          placeholder="Şifre"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />

        {error && <div className="error">{error}</div>}

        <button type="submit" disabled={loading}>
          {loading ? "Bekleyin..." : isRegister ? "Kayıt Ol" : "Giriş Yap"}
        </button>

        <button
          type="button"
          className="link"
          onClick={() => {
            setIsRegister(!isRegister);
            setError("");
          }}
        >
          {isRegister
            ? "Zaten hesabın var mı? Giriş yap"
            : "Hesabın yok mu? Kayıt ol"}
        </button>
      </form>
    </div>
  );
}
