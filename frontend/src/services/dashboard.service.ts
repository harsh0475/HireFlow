import api from "@/lib/axios";

import { ApiResponse } from "@/types/api";

import {
  AdminDashboard,
  CandidateDashboard,
  RecruiterDashboard,
} from "@/types/dashboard";

class DashboardService {
  candidate() {
    return api.get<ApiResponse<CandidateDashboard>>(
      "/dashboard/candidate"
    );
  }

  recruiter() {
    return api.get<ApiResponse<RecruiterDashboard>>(
      "/dashboard/recruiter"
    );
  }

  admin() {
    return api.get<ApiResponse<AdminDashboard>>(
      "/dashboard/admin"
    );
  }
}

export default new DashboardService();