"use client";

import { ReactNode } from "react";
import { Toaster } from "sonner";

import QueryProvider from "./query-provider";
import ThemeProvider from "./theme-provider";

interface AppProviderProps {
  children: ReactNode;
}

export default function AppProvider({
  children,
}: AppProviderProps) {
  return (
    <ThemeProvider>
      <QueryProvider>
        {children}
        <Toaster richColors position="top-right" />
      </QueryProvider>
    </ThemeProvider>
  );
}