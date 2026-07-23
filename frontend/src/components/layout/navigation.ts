import type { LucideIcon } from "lucide-react";
import {
  BriefcaseBusiness,
  Building2,
  ClipboardList,
  LayoutDashboard,
  Bell,
  Search,
  User,
  Users,
  CalendarDays,
  Shield,
  BarChart3,
  Settings,
} from "lucide-react";

import type { UserRole } from "@/types/auth";

export interface NavigationItem {
  title: string;
  href: string;
  icon: LucideIcon;
}

export const navigation: Record<UserRole, NavigationItem[]> = {
  CANDIDATE: [
    {
      title: "Dashboard",
      href: "/dashboard/candidate",
      icon: LayoutDashboard,
    },
    {
      title: "Find Jobs",
      href: "/jobs",
      icon: Search,
    },
    {
      title: "Applications",
      href: "/applications",
      icon: ClipboardList,
    },
    {
      title: "Interviews",
      href: "/interviews",
      icon: CalendarDays,
    },
    {
      title: "Profile",
      href: "/profile",
      icon: User,
    },
    {
      title: "Notifications",
      href: "/notifications",
      icon: Bell,
    },
  ],

  RECRUITER: [
    {
      title: "Dashboard",
      href: "/dashboard/recruiter",
      icon: LayoutDashboard,
    },
    {
      title: "Company",
      href: "/company",
      icon: Building2,
    },
    {
      title: "Jobs",
      href: "/jobs",
      icon: BriefcaseBusiness,
    },
    {
      title: "Applications",
      href: "/applications",
      icon: ClipboardList,
    },
    {
      title: "Interviews",
      href: "/interviews",
      icon: CalendarDays,
    },
    {
      title: "Candidates",
      href: "/dashboard/candidates",
      icon: Users,
    },
    {
      title: "Notifications",
      href: "/notifications",
      icon: Bell,
    },
  ],

  ADMIN: [
    {
      title: "Dashboard",
      href: "/dashboard/admin",
      icon: LayoutDashboard,
    },
    {
      title: "Users",
      href: "/admin/users",
      icon: Users,
    },
    {
      title: "Companies",
      href: "/companies",
      icon: Building2,
    },
    {
      title: "Jobs",
      href: "/jobs",
      icon: BriefcaseBusiness,
    },
    {
      title: "Analytics",
      href: "/admin/analytics",
      icon: BarChart3,
    },
    {
      title: "Roles",
      href: "/admin/roles",
      icon: Shield,
    },
    {
      title: "Settings",
      href: "/admin/settings",
      icon: Settings,
    },
  ],
};