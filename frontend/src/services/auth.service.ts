import api from "@/lib/axios";

import { API } from "@/constants/api";

import { ApiResponse } from "@/types/api";

import {
  LoginRequest,
  LoginResponse,
  RegisterRequest,
  User,
} from "@/types/auth";

const AuthService = {
  login(data: LoginRequest) {
    return api.post<ApiResponse<LoginResponse>>(
      API.AUTH.LOGIN,
      data
    );
  },

  register(data: RegisterRequest) {
    return api.post<ApiResponse<void>>(
      API.AUTH.REGISTER,
      data
    );
  },

  refresh(refreshToken: string) {
    return api.post<ApiResponse<LoginResponse>>(
      API.AUTH.REFRESH,
      {
        refreshToken,
      }
    );
  },

  logout(refreshToken: string) {
    return api.post<ApiResponse<void>>(
      API.AUTH.LOGOUT,
      {
        refreshToken,
      }
    );
  },

  logoutAll() {
    return api.post<ApiResponse<void>>(
      API.AUTH.LOGOUT_ALL
    );
  },

  forgotPassword(email: string) {
    return api.post<ApiResponse<void>>(
      API.AUTH.FORGOT_PASSWORD,
      {
        email,
      }
    );
  },

  resetPassword(
    token: string,
    newPassword: string,
  ) {
    return api.post<ApiResponse<void>>(
      API.AUTH.RESET_PASSWORD,
      {
        token,
        newPassword,
      }
    );
  },

  changePassword(data: {
    currentPassword: string;
    newPassword: string;
    confirmPassword: string;
  }) {
    return api.post<ApiResponse<void>>(
      API.AUTH.CHANGE_PASSWORD,
      data
    );
  },

  me() {
    return api.get<ApiResponse<User>>(
      API.USERS.ME
    );
  },
};

export default AuthService;