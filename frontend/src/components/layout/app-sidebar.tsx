"use client";

import NavLogo from "./nav-logo";
import NavMain from "./nav-main";
import NavUser from "./nav-user";

import {
  Sidebar,
  SidebarRail,
} from "@/components/ui/sidebar";

export default function AppSidebar() {
  return (
    <Sidebar
      variant="inset"
      collapsible="icon"
    >
      <NavLogo />

      <NavMain />

      <NavUser />

      <SidebarRail />
    </Sidebar>
  );
}