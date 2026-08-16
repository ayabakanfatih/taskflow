
## Arayüz

React tabanlı arayüz `frontend/` klasöründedir.

```bash
cd frontend
npm install
cp .env.example .env
npm run dev
```

Arayüz `http://localhost:5173` adresinde açılır. Backend farklı bir adreste
çalışıyorsa `frontend/.env` içindeki `VITE_API_URL` değerini güncelleyin.

## Docker ile Çalıştırma

Tek gereksinim: Docker

```bash
git clone https://github.com/ayabakanfatih/taskflow.git
cd taskflow
cp .env.example .env
# .env icindeki TASKFLOW_JWT_SECRET degerini uretin:
#   openssl rand -base64 64
docker compose up -d
```

| Servis | Adres |
|---|---|
| Arayüz | http://localhost:3000 |
| API | http://localhost:8080 |
| Swagger | http://localhost:8080/swagger-ui.html |

Durdurmak için `docker compose down`, verileri de silmek için `docker compose down -v`.
