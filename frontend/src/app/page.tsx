"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";

import { useAuth } from "@/hooks/use-auth";
import { getDefaultRoute } from "@/lib/auth-redirect";

export default function HomePage() {
  const router = useRouter();

  const {
    isLoading,
    isAuthenticated,
    user,
  } = useAuth();

  useEffect(() => {
    if (isLoading) {
      return;
    }

    if (!isAuthenticated || !user) {
      router.replace("/login");
      return;
    }

    router.replace(getDefaultRoute(user.role));
  }, [
    isLoading,
    isAuthenticated,
    user,
    router,
  ]);

  return (
    <main className="flex min-h-screen items-center justify-center">
      <p className="text-muted-foreground">
        Redirecting...
      </p>
    </main>
  );
}