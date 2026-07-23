import { User } from "@/types/auth";

type Listener = () => void;

class AuthStore {
  private accessToken: string | null = null;
  private refreshToken: string | null = null;
  private user: User | null = null;

  private listeners = new Set<Listener>();

  constructor() {
    this.restoreSession();
  }

  private restoreSession() {
    if (typeof window === "undefined") return;

    this.accessToken = localStorage.getItem("hireflow_access_token");
    this.refreshToken = localStorage.getItem("hireflow_refresh_token");

    const storedUser = localStorage.getItem("hireflow_user");

    if (storedUser) {
      try {
        this.user = JSON.parse(storedUser) as User;
      } catch {
        this.user = null;
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

  getAccessToken(): string | null {
    return this.accessToken;
  }

  getRefreshToken(): string | null {
    return this.refreshToken;
  }

  getUser(): User | null {
    return this.user;
  }

  isAuthenticated(): boolean {
    return this.accessToken !== null;
  }

  setSession(
    accessToken: string,
    refreshToken: string,
    user: User
  ) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.user = user;

    if (typeof window !== "undefined") {
      localStorage.setItem("hireflow_access_token", accessToken);
      localStorage.setItem("hireflow_refresh_token", refreshToken);
      localStorage.setItem("hireflow_user", JSON.stringify(user));
    }

    this.notify();
  }

  clearSession() {
    this.accessToken = null;
    this.refreshToken = null;
    this.user = null;

    if (typeof window !== "undefined") {
      localStorage.removeItem("hireflow_access_token");
      localStorage.removeItem("hireflow_refresh_token");
      localStorage.removeItem("hireflow_user");
    }

    this.notify();
  }

  reload() {
    this.restoreSession();
    this.notify();
  }
}

export const authStore = new AuthStore();