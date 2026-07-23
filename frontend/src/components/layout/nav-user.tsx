"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";

import {
  LogOut,
  Settings,
  UserCircle2,
} from "lucide-react";

import { toast } from "sonner";

import AuthService from "@/services/auth.service";

import { useAuth } from "@/hooks/use-auth";

import { Button } from "@/components/ui/button";

import {
  SidebarFooter,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from "@/components/ui/sidebar";

export default function NavUser() {
  const router = useRouter();

  const { user, logout } = useAuth();

  if (!user) {
    return null;
  }

  async function handleLogout() {
    try {
      await AuthService.logout();
    } catch {
      // Ignore backend failure and clear local session.
    }

    logout();

    toast.success("Logged out successfully.");

    router.replace("/login");
  }

  const initials = `${user.firstName.charAt(0)}${user.lastName.charAt(0)}`;

  return (
    <SidebarFooter>
      <div className="rounded-xl border bg-card p-4">

        <div className="flex items-center gap-3">

          <div className="flex h-11 w-11 items-center justify-center rounded-full bg-primary font-semibold text-primary-foreground">
            {initials}
          </div>

          <div className="min-w-0 flex-1">

            <p className="truncate font-semibold">
              {user.firstName} {user.lastName}
            </p>

            <p className="truncate text-xs text-muted-foreground">
              {user.email}
            </p>

            <p className="mt-1 text-xs font-medium uppercase tracking-wide text-primary">
              {user.role}
            </p>

          </div>

        </div>

        <SidebarMenu className="mt-4">

          <SidebarMenuItem>

            <SidebarMenuButton
              render={<Link href="/profile" />}
            >
              <UserCircle2 />
              <span>Profile</span>
            </SidebarMenuButton>

          </SidebarMenuItem>

          <SidebarMenuItem>

            <SidebarMenuButton
              render={<Link href="/settings" />}
            >
              <Settings />
              <span>Settings</span>
            </SidebarMenuButton>

          </SidebarMenuItem>

        </SidebarMenu>

        <Button
          variant="destructive"
          className="mt-4 w-full"
          onClick={handleLogout}
        >
          <LogOut className="mr-2 h-4 w-4" />
          Logout
        </Button>

      </div>
    </SidebarFooter>
  );
}