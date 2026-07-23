import axios, {
  AxiosError,
  InternalAxiosRequestConfig,
} from "axios";

import { API } from "@/constants/api";
import { TokenStorage } from "./auth";

const api = axios.create({
  baseURL: API.BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
  timeout: 30000,
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

  (error: AxiosError) => {
    return Promise.reject(error);
  }
);

export default api;