package com.hireflow.service.impl;

import com.hireflow.dto.response.PageResponse;
import com.hireflow.dto.response.UserResponse;
import com.hireflow.entity.User;
import com.hireflow.exception.ResourceNotFoundException;
import com.hireflow.mapper.UserMapper;
import com.hireflow.repository.UserRepository;
import com.hireflow.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public PageResponse<UserResponse> getAllUsers(Pageable pageable) {

        Page<UserResponse> page = userRepository.findAll(pageable)
                .map(userMapper::toResponse);

        return PageResponse.from(page);
    }

    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        userRepository.delete(user);
    }

    @Override
    @Transactional
    public UserResponse activateUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        user.setEnabled(true);

        user = userRepository.save(user);

        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse deactivateUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found."));

        user.setEnabled(false);

        user = userRepository.save(user);

        return userMapper.toResponse(user);
    }
}