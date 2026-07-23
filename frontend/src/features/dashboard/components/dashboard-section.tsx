"use client";

import { ReactNode } from "react";

interface DashboardSectionProps {
  title: string;
  description?: string;
  children: ReactNode;
}

export default function DashboardSection({
  title,
  description,
  children,
}: DashboardSectionProps) {
  return (
    <section className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold tracking-tight">
          {title}
        </h1>

        {description && (
          <p className="text-muted-foreground mt-2">
            {description}
          </p>
        )}
      </div>

      {children}
    </section>
  );
}