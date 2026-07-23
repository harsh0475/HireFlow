import api from "@/lib/axios";

import { API } from "@/constants/api";

import { ApiResponse } from "@/types/api";

import {
  LoginRequest,
  LoginResponse,
  RegisterRequest,
} from "@/types/auth";

const AuthService = {
  login(data: LoginRequest) {
    return api.post<ApiResponse<LoginResponse>>(
      API.AUTH.LOGIN,
      data
    );
  },

  register(data: RegisterRequest) {
    return api.post(API.AUTH.REGISTER, data);
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
    return api.post(API.AUTH.LOGOUT, {
      refreshToken,
    });
  },
};

export default AuthService;