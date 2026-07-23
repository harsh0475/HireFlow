"use client";

interface DashboardEmptyProps {
  message: string;
}

export default function DashboardEmpty({
  message,
}: DashboardEmptyProps) {
  return (
    <div className="flex min-h-[250px] items-center justify-center rounded-lg border border-dashed">
      <p className="text-muted-foreground">
        {message}
      </p>
    </div>
  );
}