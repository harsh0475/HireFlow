"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";

import { useAuth } from "@/hooks/use-auth";

export default function HomePage() {
  const router = useRouter();

  const { isLoading, isAuthenticated, user } = useAuth();

  useEffect(() => {
    if (isLoading) return;

    if (!isAuthenticated) {
      router.replace("/login");
      return;
    }

    switch (user?.role) {
      case "ADMIN":
        router.replace("/admin");
        break;

      case "RECRUITER":
        router.replace("/recruiter");
        break;

      case "CANDIDATE":
        router.replace("/candidate");
        break;

      default:
        router.replace("/login");
    }
  }, [isLoading, isAuthenticated, user, router]);

  return (
    <main className="flex min-h-screen items-center justify-center">
      <p className="text-muted-foreground">
        Loading...
      </p>
    </main>
  );
}