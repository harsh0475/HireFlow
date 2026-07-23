import { User } from "@/types/auth";

type Listener = () => void;

class AuthStore {
  private accessToken: string | null = null;
  private refreshToken: string | null = null;
  private user: User | null = null;

  private listeners = new Set<Listener>();

  constructor() {
    if (typeof window !== "undefined") {
      this.accessToken = localStorage.getItem("hireflow_access_token");
      this.refreshToken = localStorage.getItem("hireflow_refresh_token");

      const storedUser = localStorage.getItem("hireflow_user");

      if (storedUser) {
        this.user = JSON.parse(storedUser);
      }
    }
  }

  subscribe(listener: Listener) {
    this.listeners.add(listener);

    return () => {
        this.listeners.delete(listener);
    };
  }

  private notify() {
    this.listeners.forEach((listener) => listener());
  }

  getAccessToken() {
    return this.accessToken;
  }

  getRefreshToken() {
    return this.refreshToken;
  }

  getUser() {
    return this.user;
  }

  isAuthenticated() {
    return !!this.accessToken;
  }

  setSession(
    accessToken: string,
    refreshToken: string,
    user: User
  ) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.user = user;

    localStorage.setItem("hireflow_access_token", accessToken);
    localStorage.setItem("hireflow_refresh_token", refreshToken);
    localStorage.setItem("hireflow_user", JSON.stringify(user));

    this.notify();
  }

  clearSession() {
    this.accessToken = null;
    this.refreshToken = null;
    this.user = null;

    localStorage.removeItem("hireflow_access_token");
    localStorage.removeItem("hireflow_refresh_token");
    localStorage.removeItem("hireflow_user");

    this.notify();
  }
}

export const authStore = new AuthStore();