export default function HomePage() {
  return (
    <main className="flex min-h-screen items-center justify-center">
      <div className="space-y-4 text-center">
        <h1 className="text-5xl font-bold tracking-tight">
          HireFlow
        </h1>

        <p className="text-muted-foreground text-lg">
          Enterprise Recruitment Management System
        </p>

        <div className="rounded-lg border p-6">
          <p className="font-medium">
            Frontend Foundation Initialized Successfully
          </p>

          <p className="mt-2 text-sm text-muted-foreground">
            Next.js 16 • React 19 • Tailwind v4 • shadcn/ui •
            TanStack Query • Axios
          </p>
        </div>
      </div>
    </main>
  );
}