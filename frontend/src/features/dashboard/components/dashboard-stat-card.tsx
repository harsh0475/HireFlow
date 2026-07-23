"use client";

import { ReactNode } from "react";

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

interface DashboardStatCardProps {
  title: string;
  value: number | string;
  icon: ReactNode;
}

export default function DashboardStatCard({
  title,
  value,
  icon,
}: DashboardStatCardProps) {
  return (
    <Card>
      <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
        <CardTitle className="text-sm font-medium text-muted-foreground">
          {title}
        </CardTitle>

        <div className="text-muted-foreground">
          {icon}
        </div>
      </CardHeader>

      <CardContent>
        <div className="text-3xl font-bold">
          {value}
        </div>
      </CardContent>
    </Card>
  );
}