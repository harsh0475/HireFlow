"use client";

import Link from "next/link";
import { BriefcaseBusiness } from "lucide-react";

import {
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from "@/components/ui/sidebar";

export default function NavLogo() {
  return (
    <SidebarHeader>
      <SidebarMenu>
        <SidebarMenuItem>
          <SidebarMenuButton
            size="lg"
            tooltip="HireFlow"
            render={<Link href="/" />}
            >
            <div className="flex aspect-square size-8 items-center justify-center rounded-lg bg-primary text-primary-foreground">
                <BriefcaseBusiness className="size-4" />
            </div>

            <div className="grid flex-1 text-left leading-tight">
                <span className="truncate font-semibold">
                HireFlow
                </span>

                <span className="truncate text-xs text-muted-foreground">
                Recruitment Platform
                </span>
            </div>
          </SidebarMenuButton>
        </SidebarMenuItem>
      </SidebarMenu>
    </SidebarHeader>
  );
}