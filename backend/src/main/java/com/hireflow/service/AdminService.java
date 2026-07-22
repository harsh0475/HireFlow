package com.hireflow.service;

import com.hireflow.dto.response.PageResponse;
import com.hireflow.dto.response.UserResponse;
import org.springframework.data.domain.Pageable;

public interface AdminService {

    PageResponse<UserResponse> getAllUsers(Pageable pageable);

    PageResponse<UserResponse> getCandidates(Pageable pageable);

    PageResponse<UserResponse> getRecruiters(Pageable pageable);

    PageResponse<UserResponse> getAdmins(Pageable pageable);

    UserResponse activateUser(Long userId);

    UserResponse deactivateUser(Long userId);

    void deleteUser(Long userId);
}