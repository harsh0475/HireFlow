export interface CandidateDashboard {
  appliedJobs: number;
  shortlisted: number;
  interviewsScheduled: number;
  rejected: number;
  hired: number;
  withdrawn: number;
}

export interface RecruiterDashboard {
  jobsPosted: number;
  activeJobs: number;
  totalApplications: number;
  interviewsScheduled: number;
  hired: number;
}

export interface AdminDashboard {
  totalUsers: number;
  totalCandidates: number;
  totalRecruiters: number;
  totalCompanies: number;
  totalJobs: number;
  activeJobs: number;
  totalApplications: number;
  totalInterviews: number;
}