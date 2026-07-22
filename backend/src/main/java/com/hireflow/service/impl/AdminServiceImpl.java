package com.hireflow.service.impl;

import com.hireflow.dto.response.PageResponse;
import com.hireflow.dto.response.UserResponse;
import com.hireflow.entity.User;
import com.hireflow.entity.enums.Role;
import com.hireflow.exception.BadRequestException;
import com.hireflow.exception.ResourceNotFoundException;
import com.hireflow.mapper.UserMapper;
import com.hireflow.repository.UserRepository;
import com.hireflow.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> getAllUsers(Pageable pageable) {

        Page<User> page = userRepository.findAll(pageable);

        return PageResponse.from(
                page.map(userMapper::toResponse)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> getCandidates(Pageable pageable) {

        Page<User> page = userRepository.findByRole(Role.CANDIDATE, pageable);

        return PageResponse.from(
                page.map(userMapper::toResponse)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> getRecruiters(Pageable pageable) {

        Page<User> page = userRepository.findByRole(Role.RECRUITER, pageable);

        return PageResponse.from(
                page.map(userMapper::toResponse)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> getAdmins(Pageable pageable) {

        Page<User> page = userRepository.findByRole(Role.ADMIN, pageable);

        return PageResponse.from(
                page.map(userMapper::toResponse)
        );
    }

    @Override
    public UserResponse activateUser(Long userId) {

        User user = getUser(userId);

        user.setEnabled(true);

        return userMapper.toResponse(
                userRepository.save(user)
        );
    }

    @Override
    public UserResponse deactivateUser(Long userId) {

        User user = getUser(userId);

        if (user.getRole() == Role.ADMIN) {
            throw new BadRequestException("Admin users cannot be deactivated.");
        }

        user.setEnabled(false);

        return userMapper.toResponse(
                userRepository.save(user)
        );
    }

    @Override
    public void deleteUser(Long userId) {

        User user = getUser(userId);

        if (user.getRole() == Role.ADMIN) {
            throw new BadRequestException("Admin users cannot be deleted.");
        }

        userRepository.delete(user);
    }

    private User getUser(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        ));
    }
}