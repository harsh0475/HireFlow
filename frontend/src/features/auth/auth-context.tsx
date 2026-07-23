"use client";

import {
  createContext,
  ReactNode,
  useContext,
  useEffect,
  useMemo,
  useState,
} from "react";

import { LoginResponse, User } from "@/types/auth";
import { authStore } from "@/store/auth-store";

interface AuthContextType {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;

  login(data: LoginResponse): void;
  logout(): void;
  reload(): void;
}

const AuthContext = createContext<AuthContextType | undefined>(
  undefined
);

export function AuthProvider({
  children,
}: {
  children: ReactNode;
}) {
  const [version, forceUpdate] = useState(0);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    authStore.reload();

    const unsubscribe = authStore.subscribe(() => {
      forceUpdate((v) => v + 1);
    });

    setIsLoading(false);

    return unsubscribe;
  }, []);

  const user = authStore.getUser();
  const isAuthenticated = authStore.isAuthenticated();

  const value = useMemo<AuthContextType>(
    () => ({
      user,

      isAuthenticated,

      isLoading,

      login(data: LoginResponse) {
        authStore.setSession(
          data.accessToken,
          data.refreshToken,
          data.user
        );
      },

      logout() {
        authStore.clearSession();
      },

      reload() {
        authStore.reload();
      },
    }),
    [user, isAuthenticated, isLoading, version]
  );

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error(
      "useAuth must be used within AuthProvider."
    );
  }

  return context;
}