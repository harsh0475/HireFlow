"use client";

export default function AuthLoading() {
  return (
    <main className="flex min-h-screen items-center justify-center">
      <div className="space-y-3 text-center">

        <div className="mx-auto h-10 w-10 animate-spin rounded-full border-4 border-primary border-t-transparent" />

        <p className="text-sm text-muted-foreground">
          Loading...
        </p>

      </div>
    </main>
  );
}