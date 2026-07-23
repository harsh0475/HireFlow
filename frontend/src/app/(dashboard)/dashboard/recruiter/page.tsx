"use client";

import {
  Briefcase,
  BriefcaseBusiness,
  FileText,
  CalendarCheck2,
  Trophy,
} from "lucide-react";

import {
  useRecruiterDashboard,
} from "@/features/dashboard/hooks/use-dashboard";

import DashboardError from "@/features/dashboard/components/dashboard-error";
import DashboardGrid from "@/features/dashboard/components/dashboard-grid";
import DashboardSection from "@/features/dashboard/components/dashboard-section";
import DashboardSkeleton from "@/features/dashboard/components/dashboard-skeleton";
import DashboardStatCard from "@/features/dashboard/components/dashboard-stat-card";

export default function RecruiterDashboardPage() {
  const {
    data,
    isLoading,
    isError,
    refetch,
  } = useRecruiterDashboard();

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
      title="Recruiter Dashboard"
      description="Monitor your jobs, applications, and hiring pipeline."
    >
      <DashboardGrid>
        <DashboardStatCard
          title="Jobs Posted"
          value={data.jobsPosted}
          icon={<Briefcase className="size-5" />}
        />

        <DashboardStatCard
          title="Active Jobs"
          value={data.activeJobs}
          icon={<BriefcaseBusiness className="size-5" />}
        />

        <DashboardStatCard
          title="Applications"
          value={data.totalApplications}
          icon={<FileText className="size-5" />}
        />

        <DashboardStatCard
          title="Interviews"
          value={data.interviewsScheduled}
          icon={<CalendarCheck2 className="size-5" />}
        />

        <DashboardStatCard
          title="Hired"
          value={data.hired}
          icon={<Trophy className="size-5" />}
        />
      </DashboardGrid>
    </DashboardSection>
  );
}