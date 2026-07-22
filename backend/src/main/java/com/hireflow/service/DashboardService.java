package com.hireflow.service;

import com.hireflow.dto.response.AdminDashboardResponse;
import com.hireflow.dto.response.CandidateDashboardResponse;
import com.hireflow.dto.response.RecruiterDashboardResponse;

public interface DashboardService {

    CandidateDashboardResponse getCandidateDashboard(Long candidateId);

    RecruiterDashboardResponse getRecruiterDashboard(Long recruiterId);

    AdminDashboardResponse getAdminDashboard();
}