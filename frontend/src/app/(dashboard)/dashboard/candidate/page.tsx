"use client";

import {
  Briefcase,
  CheckCircle2,
  Clock3,
  XCircle,
  Trophy,
  Undo2,
} from "lucide-react";

import {
  useCandidateDashboard,
} from "@/features/dashboard/hooks/use-dashboard";

import DashboardGrid from "@/features/dashboard/components/dashboard-grid";
import DashboardSection from "@/features/dashboard/components/dashboard-section";
import DashboardSkeleton from "@/features/dashboard/components/dashboard-skeleton";
import DashboardStatCard from "@/features/dashboard/components/dashboard-stat-card";
import DashboardError from "@/features/dashboard/components/dashboard-error";

export default function CandidateDashboardPage() {
  const {
    data,
    isLoading,
    isError,
    refetch,
  } = useCandidateDashboard();

  if (isLoading) {
    return <DashboardSkeleton />;
  }

  if (isError || !data) {
    return (
      <DashboardError
        onRetry={() => refetch()}
      />
    );
  }

  return (
    <DashboardSection
      title="Candidate Dashboard"
      description="Track your applications and hiring progress."
    >
      <DashboardGrid>
        <DashboardStatCard
          title="Applied Jobs"
          value={data.appliedJobs}
          icon={<Briefcase className="size-5" />}
        />

        <DashboardStatCard
          title="Shortlisted"
          value={data.shortlisted}
          icon={<CheckCircle2 className="size-5" />}
        />

        <DashboardStatCard
          title="Interviews"
          value={data.interviewsScheduled}
          icon={<Clock3 className="size-5" />}
        />

        <DashboardStatCard
          title="Hired"
          value={data.hired}
          icon={<Trophy className="size-5" />}
        />

        <DashboardStatCard
          title="Rejected"
          value={data.rejected}
          icon={<XCircle className="size-5" />}
        />

        <DashboardStatCard
          title="Withdrawn"
          value={data.withdrawn}
          icon={<Undo2 className="size-5" />}
        />
      </DashboardGrid>
    </DashboardSection>
  );
}