const BASE_URL = import.meta.env.VITE_API_URL || "http://localhost:8080";

export function getToken() {
  return localStorage.getItem("taskflow_token");
}

export function setToken(token) {
  localStorage.setItem("taskflow_token", token);
}

export function clearToken() {
  localStorage.removeItem("taskflow_token");
}

async function request(path, options = {}) {
  const headers = { "Content-Type": "application/json", ...options.headers };

  const token = getToken();
  if (token) {
    headers["Authorization"] = "Bearer " + token;
  }

  const response = await fetch(BASE_URL + path, { ...options, headers });

  if (response.status === 401) {
    clearToken();
    window.location.href = "/login";
    throw new Error("Oturum süresi doldu");
  }

  if (response.status === 204) {
    return null;
  }

  const data = await response.json().catch(() => null);

  if (!response.ok) {
    const message =
      data?.details?.join(", ") || data?.message || "Bir hata oluştu";
    throw new Error(message);
  }

  return data;
}

export const api = {
  login: (email, password) =>
    request("/api/auth/login", {
      method: "POST",
      body: JSON.stringify({ email, password }),
    }),

  register: (email, fullName, password) =>
    request("/api/users/register", {
      method: "POST",
      body: JSON.stringify({ email, fullName, password }),
    }),

  getTasks: (params = {}) => {
    const query = new URLSearchParams();
    if (params.status) query.set("status", params.status);
    if (params.projectId) query.set("projectId", params.projectId);
    query.set("page", params.page ?? 0);
    query.set("size", params.size ?? 20);
    return request("/api/tasks?" + query.toString());
  },

  createTask: (task) =>
    request("/api/tasks", { method: "POST", body: JSON.stringify(task) }),

  updateTaskStatus: (id, status) =>
    request("/api/tasks/" + id + "/status", {
      method: "PATCH",
      body: JSON.stringify({ status }),
    }),

  deleteTask: (id) => request("/api/tasks/" + id, { method: "DELETE" }),

  assignToProject: (taskId, projectId) =>
    request("/api/tasks/" + taskId + "/project/" + projectId, {
      method: "PATCH",
    }),

  getProjects: () => request("/api/projects"),

  createProject: (name) =>
    request("/api/projects", {
      method: "POST",
      body: JSON.stringify({ name }),
    }),
};
