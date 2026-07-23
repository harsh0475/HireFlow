"use client";

import {
  createContext,
  ReactNode,
  useContext,
  useEffect,
  useMemo,
  useState,
} from "react";

import { LoginResponse } from "@/types/auth";
import { authStore } from "@/store/auth-store";

interface AuthContextType {
  user: ReturnType<typeof authStore.getUser>;
  isAuthenticated: boolean;
  isLoading: boolean;

  login(data: LoginResponse): void;
  logout(): void;
}

const AuthContext = createContext<AuthContextType | undefined>(
  undefined
);

export function AuthProvider({
  children,
}: {
  children: ReactNode;
}) {
  const [, forceUpdate] = useState(0);

  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const unsubscribe = authStore.subscribe(() =>
      forceUpdate((v) => v + 1)
    );

    setIsLoading(false);

    return unsubscribe;
  }, []);

  const value = useMemo<AuthContextType>(
    () => ({
      user: authStore.getUser(),

      isAuthenticated:
        authStore.isAuthenticated(),

      isLoading,

      login(response) {
        authStore.setSession(
          response.accessToken,
          response.refreshToken,
          response.user
        );
      },

      logout() {
        authStore.clearSession();
      },
    }),
    [isLoading]
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