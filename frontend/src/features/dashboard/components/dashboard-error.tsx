"use client";

import { Button } from "@/components/ui/button";

interface DashboardErrorProps {
  onRetry: () => void;
}

export default function DashboardError({
  onRetry,
}: DashboardErrorProps) {
  return (
    <div className="flex min-h-[300px] flex-col items-center justify-center gap-4">
      <p className="text-muted-foreground">
        Failed to load dashboard.
      </p>

      <Button onClick={onRetry}>
        Try Again
      </Button>
    </div>
  );
}