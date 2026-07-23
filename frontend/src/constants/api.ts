export const API = {
  BASE_URL: process.env.NEXT_PUBLIC_API_BASE_URL!,

  AUTH: {
    LOGIN: "/auth/login",
    REGISTER: "/auth/register",
    REFRESH: "/auth/refresh",
    LOGOUT: "/auth/logout",
    FORGOT_PASSWORD: "/auth/forgot-password",
    RESET_PASSWORD: "/auth/reset-password",
    CHANGE_PASSWORD: "/auth/change-password",
  },

  USERS: {
    ME: "/users/me",
  },

  COMPANIES: "/companies",

  JOBS: "/jobs",

  INTERVIEWS: "/interviews",

  APPLICATIONS: "/applications",

  CANDIDATES: "/candidates",
} as const;