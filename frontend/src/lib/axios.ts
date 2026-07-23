import axios, {
  AxiosError,
  AxiosResponse,
  InternalAxiosRequestConfig,
} from "axios";

import { API } from "@/constants/api";

import { TokenStorage } from "@/lib/auth";

import { refreshQueue } from "@/lib/refresh-queue";

import AuthService from "@/services/auth.service";

import { RetryRequestConfig } from "@/types/axios";
import { LoginResponse } from "@/types/auth";

const api = axios.create({
  baseURL: API.BASE_URL,

  timeout: 30000,

  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use(
  (
    config: InternalAxiosRequestConfig
  ): InternalAxiosRequestConfig => {
    const token = TokenStorage.getAccessToken();

    if (token) {
      config.headers.set(
        "Authorization",
        `Bearer ${token}`
      );
    }

    return config;
  },

  (error: AxiosError) => Promise.reject(error)
);

api.interceptors.response.use(
  (response: AxiosResponse) => response,

  async (error: AxiosError) => {
    const originalRequest =
      error.config as RetryRequestConfig;

    if (!originalRequest) {
      return Promise.reject(error);
    }

    if (originalRequest._retry) {
      return Promise.reject(error);
    }

    if (error.response?.status !== 401) {
      return Promise.reject(error);
    }

    originalRequest._retry = true;

    const refreshToken =
      TokenStorage.getRefreshToken();

    if (!refreshToken) {
      TokenStorage.clear();

      return Promise.reject(error);
    }

    if (refreshQueue.refreshing) {
      return new Promise((resolve, reject) => {
        refreshQueue.subscribe((token) => {
          if (!token) {
            reject(error);

            return;
          }

          originalRequest.headers.set(
            "Authorization",
            `Bearer ${token}`
          );

          resolve(api(originalRequest));
        });
      });
    }

    refreshQueue.startRefresh();

    try {
      const response =
        await AuthService.refresh(refreshToken);

      const data: LoginResponse =
        response.data.data;

      TokenStorage.setSession(
        data.accessToken,
        data.refreshToken,
        data.user
      );

      refreshQueue.finishRefresh(
        data.accessToken
      );

      originalRequest.headers.set(
        "Authorization",
        `Bearer ${data.accessToken}`
      );

      return api(originalRequest);
    } catch (refreshError) {
      TokenStorage.clear();

      refreshQueue.failRefresh();

      return Promise.reject(refreshError);
    }
  }
);

export default api;