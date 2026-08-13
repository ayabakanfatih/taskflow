import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { api, clearToken } from "../api/client";

const STATUSES = ["TODO", "IN_PROGRESS", "DONE"];
const PRIORITIES = ["LOW", "MEDIUM", "HIGH", "CRITICAL"];

export default function Tasks() {
  const [tasks, setTasks] = useState([]);
  const [projects, setProjects] = useState([]);
  const [filter, setFilter] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(true);

  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [priority, setPriority] = useState("MEDIUM");
  const [projectId, setProjectId] = useState("");

  const navigate = useNavigate();

  async function loadTasks() {
    setLoading(true);
    setError("");
    try {
      const params = filter ? { status: filter } : {};
      const page = await api.getTasks(params);
      setTasks(page.content);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  async function loadProjects() {
    try {
      const list = await api.getProjects();
      setProjects(list);
    } catch (err) {
      setError(err.message);
    }
  }

  useEffect(() => {
    loadProjects();
  }, []);

  useEffect(() => {
    loadTasks();
  }, [filter]);

  async function handleCreate(event) {
    event.preventDefault();
    setError("");
    try {
      const created = await api.createTask({ title, description, priority });
      if (projectId) {
        await api.assignToProject(created.id, projectId);
      }
      setTitle("");
      setDescription("");
      setPriority("MEDIUM");
      setProjectId("");
      loadTasks();
    } catch (err) {
      setError(err.message);
    }
  }

  async function handleStatusChange(id, status) {
    try {
      await api.updateTaskStatus(id, status);
      loadTasks();
    } catch (err) {
      setError(err.message);
    }
  }

  async function handleDelete(id) {
    try {
      await api.deleteTask(id);
      loadTasks();
    } catch (err) {
      setError(err.message);
    }
  }

  function handleLogout() {
    clearToken();
    navigate("/login");
  }

  return (
    <div className="page">
      <header className="topbar">
        <h1>TaskFlow</h1>
        <button className="ghost" onClick={handleLogout}>
          Çıkış
        </button>
      </header>

      <section className="card">
        <h2>Yeni Görev</h2>
        <form className="task-form" onSubmit={handleCreate}>
          <input
            type="text"
            placeholder="Başlık"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
          />
          <input
            type="text"
            placeholder="Açıklama (opsiyonel)"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />
          <select value={priority} onChange={(e) => setPriority(e.target.value)}>
            {PRIORITIES.map((p) => (
              <option key={p} value={p}>
                {p}
              </option>
            ))}
          </select>
          <select
            value={projectId}
            onChange={(e) => setProjectId(e.target.value)}
          >
            <option value="">Proje seçilmedi</option>
            {projects.map((p) => (
              <option key={p.id} value={p.id}>
                {p.name}
              </option>
            ))}
          </select>
          <button type="submit">Ekle</button>
        </form>
      </section>

      <section className="card">
        <div className="list-head">
          <h2>Görevler</h2>
          <select value={filter} onChange={(e) => setFilter(e.target.value)}>
            <option value="">Tümü</option>
            {STATUSES.map((s) => (
              <option key={s} value={s}>
                {s}
              </option>
            ))}
          </select>
        </div>

        {error && <div className="error">{error}</div>}
        {loading && <p className="muted">Yükleniyor...</p>}
        {!loading && tasks.length === 0 && (
          <p className="muted">Henüz görev yok.</p>
        )}

        <div className="task-list">
          {tasks.map((task) => (
            <div className="task-row" key={task.id}>
              <div className="task-main">
                <strong>{task.title}</strong>
                {task.description && (
                  <span className="muted"> — {task.description}</span>
                )}
                <div className="badges">
                  <span className={"badge p-" + (task.priority || "NONE")}>
                    {task.priority || "-"}
                  </span>
                  {task.project && (
                    <span className="badge project">{task.project.name}</span>
                  )}
                </div>
              </div>

              <select
                value={task.status}
                onChange={(e) => handleStatusChange(task.id, e.target.value)}
              >
                {STATUSES.map((s) => (
                  <option key={s} value={s}>
                    {s}
                  </option>
                ))}
              </select>

              <button className="ghost" onClick={() => handleDelete(task.id)}>
                Sil
              </button>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}
