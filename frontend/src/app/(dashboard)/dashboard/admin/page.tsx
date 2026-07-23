"use client";

import {
  Users,
  UserCheck,
  Building2,
  BriefcaseBusiness,
  Briefcase,
  FileText,
  CalendarCheck2,
  Shield,
} from "lucide-react";

import {
  useAdminDashboard,
} from "@/features/dashboard/hooks/use-dashboard";

import DashboardError from "@/features/dashboard/components/dashboard-error";
import DashboardGrid from "@/features/dashboard/components/dashboard-grid";
import DashboardSection from "@/features/dashboard/components/dashboard-section";
import DashboardSkeleton from "@/features/dashboard/components/dashboard-skeleton";
import DashboardStatCard from "@/features/dashboard/components/dashboard-stat-card";

export default function AdminDashboardPage() {
  const {
    data,
    isLoading,
    isError,
    refetch,
  } = useAdminDashboard();

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
      title="Admin Dashboard"
      description="Platform-wide recruitment statistics."
    >
      <DashboardGrid>
        <DashboardStatCard
          title="Total Users"
          value={data.totalUsers}
          icon={<Users className="size-5" />}
        />

        <DashboardStatCard
          title="Candidates"
          value={data.totalCandidates}
          icon={<UserCheck className="size-5" />}
        />

        <DashboardStatCard
          title="Recruiters"
          value={data.totalRecruiters}
          icon={<Shield className="size-5" />}
        />

        <DashboardStatCard
          title="Companies"
          value={data.totalCompanies}
          icon={<Building2 className="size-5" />}
        />

        <DashboardStatCard
          title="Jobs"
          value={data.totalJobs}
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
          value={data.totalInterviews}
          icon={<CalendarCheck2 className="size-5" />}
        />
      </DashboardGrid>
    </DashboardSection>
  );
}