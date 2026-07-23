"use client";

import { ReactNode } from "react";
import { Toaster } from "sonner";

import QueryProvider from "./query-provider";
import ThemeProvider from "./theme-provider";

import { AuthProvider } from "@/features/auth/auth-context";

interface Props {
  children: ReactNode;
}

export default function AppProvider({
  children,
}: Props) {
  return (
    <ThemeProvider>
      <QueryProvider>
        <AuthProvider>
          {children}

          <Toaster
            richColors
            position="top-right"
          />
        </AuthProvider>
      </QueryProvider>
    </ThemeProvider>
  );
}