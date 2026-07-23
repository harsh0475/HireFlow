"use client";

import { ReactNode, useEffect } from "react";
import { useRouter } from "next/navigation";

import { useAuth } from "@/hooks/use-auth";
import { getDefaultRoute } from "@/lib/auth-redirect";

interface RoleGuardProps {
  children: ReactNode;
  roles: string[];
}

export default function RoleGuard({
  children,
  roles,
}: RoleGuardProps) {
  const router = useRouter();

  const { user } = useAuth();

  useEffect(() => {
    if (!user) {
      return;
    }

    if (!roles.includes(user.role)) {
      router.replace(getDefaultRoute(user.role));
    }
  }, [router, roles, user]);

  if (!user) {
    return null;
  }

  if (!roles.includes(user.role)) {
    return null;
  }

  return <>{children}</>;
}