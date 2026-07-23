"use client";

import { useQuery } from "@tanstack/react-query";

import DashboardService from "@/services/dashboard.service";

export function useCandidateDashboard() {
  return useQuery({
    queryKey: ["candidate-dashboard"],
    queryFn: async () => {
      const response =
        await DashboardService.candidate();

      return response.data.data;
    },
  });
}

export function useRecruiterDashboard() {
  return useQuery({
    queryKey: ["recruiter-dashboard"],
    queryFn: async () => {
      const response =
        await DashboardService.recruiter();

      return response.data.data;
    },
  });
}

export function useAdminDashboard() {
  return useQuery({
    queryKey: ["admin-dashboard"],
    queryFn: async () => {
      const response =
        await DashboardService.admin();

      return response.data.data;
    },
  });
}