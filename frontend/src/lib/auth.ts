import { authStore } from "@/store/auth-store";

export const TokenStorage = {
  getAccessToken() {
    return authStore.getAccessToken();
  },

  getRefreshToken() {
    return authStore.getRefreshToken();
  },

  getUser() {
    return authStore.getUser();
  },

  setSession(
    accessToken: string,
    refreshToken: string,
    user: Parameters<typeof authStore.setSession>[2]
  ) {
    authStore.setSession(accessToken, refreshToken, user);
  },

  clear() {
    authStore.clearSession();
  },

  isAuthenticated() {
    return authStore.isAuthenticated();
  },
};