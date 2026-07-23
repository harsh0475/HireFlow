"use client";

import {
  createContext,
  useContext,
  useEffect,
  useMemo,
  useState,
  ReactNode,
} from "react";

import { TokenStorage } from "@/lib/auth";
import {
  LoginResponse,
  User,
} from "@/types/auth";

interface AuthContextType {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;

  login: (response: LoginResponse) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(
  undefined
);

interface Props {
  children: ReactNode;
}

export function AuthProvider({
  children,
}: Props) {
  const [user, setUser] = useState<User | null>(null);

  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const storedUser = localStorage.getItem("hireflow_user");

    if (storedUser) {
      setUser(JSON.parse(storedUser));
    }

    setIsLoading(false);
  }, []);

  const login = (response: LoginResponse) => {
    TokenStorage.setTokens(
      response.accessToken,
      response.refreshToken
    );

    localStorage.setItem(
      "hireflow_user",
      JSON.stringify(response.user)
    );

    setUser(response.user);
  };

  const logout = () => {
    TokenStorage.clear();

    localStorage.removeItem("hireflow_user");

    setUser(null);
  };

  const value = useMemo(
    () => ({
      user,
      isLoading,
      login,
      logout,
      isAuthenticated:
        !!user && TokenStorage.isAuthenticated(),
    }),
    [user, isLoading]
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
      "useAuth must be used inside AuthProvider."
    );
  }

  return context;
}