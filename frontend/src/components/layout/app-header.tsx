"use client";

import { useRouter } from "next/navigation";

import {
  LogOut,
  Settings,
  User,
} from "lucide-react";

import { toast } from "sonner";

import AuthService from "@/services/auth.service";
import { useAuth } from "@/hooks/use-auth";

import {
  Avatar,
  AvatarFallback,
} from "@/components/ui/avatar";

import { Button } from "@/components/ui/button";

import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";

import {
  SidebarTrigger,
} from "@/components/ui/sidebar";

export default function AppHeader() {
  const router = useRouter();

  const { user, logout } = useAuth();

  async function handleLogout() {
    try {
      await AuthService.logout();
    } catch {
      // Ignore API failure.
    }

    logout();

    toast.success("Logged out successfully.");

    router.replace("/login");
  }

  const initials =
    user != null
      ? `${user.firstName.charAt(0)}${user.lastName.charAt(0)}`
      : "HF";

  return (
    <header className="sticky top-0 z-40 flex h-16 items-center justify-between border-b bg-background/95 px-6 backdrop-blur supports-[backdrop-filter]:bg-background/60">

      <div className="flex items-center gap-3">

        <SidebarTrigger />

        <div>
          <h1 className="text-lg font-semibold">
            HireFlow Dashboard
          </h1>

          <p className="text-sm text-muted-foreground">
            Recruitment Management System
          </p>
        </div>

      </div>

      <DropdownMenu>

        <DropdownMenuTrigger
          render={
            <Button
              variant="ghost"
              className="h-auto px-2 py-1"
            />
          }
        >
          <div className="flex items-center gap-3">

            <Avatar>

              <AvatarFallback>
                {initials}
              </AvatarFallback>

            </Avatar>

            <div className="hidden text-left md:block">

              <p className="text-sm font-medium">
                {user?.firstName} {user?.lastName}
              </p>

              <p className="text-xs text-muted-foreground">
                {user?.email}
              </p>

            </div>

          </div>
        </DropdownMenuTrigger>

        <DropdownMenuContent
          align="end"
          className="w-56"
        >
          <DropdownMenuGroup>

            <DropdownMenuItem
              onClick={() =>
                router.push("/profile")
              }
            >
              <User className="mr-2 h-4 w-4" />

              Profile
            </DropdownMenuItem>

            <DropdownMenuItem
              onClick={() =>
                router.push("/settings")
              }
            >
              <Settings className="mr-2 h-4 w-4" />

              Settings
            </DropdownMenuItem>

          </DropdownMenuGroup>

          <DropdownMenuSeparator />

          <DropdownMenuItem
            onClick={handleLogout}
          >
            <LogOut className="mr-2 h-4 w-4" />

            Logout
          </DropdownMenuItem>

        </DropdownMenuContent>

      </DropdownMenu>

    </header>
  );
}