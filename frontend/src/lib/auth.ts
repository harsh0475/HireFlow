import { authStore } from "@/store/auth-store";
import { User } from "@/types/auth";

export const TokenStorage = {
  getAccessToken(): string | null {
    return authStore.getAccessToken();
  },

  getRefreshToken(): string | null {
    return authStore.getRefreshToken();
  },

  getUser(): User | null {
    return authStore.getUser();
  },

  setSession(
    accessToken: string,
    refreshToken: string,
    user: User
  ) {
    authStore.setSession(
      accessToken,
      refreshToken,
      user
    );
  },

  clear() {
    authStore.clearSession();
  },

  isAuthenticated() {
    return authStore.isAuthenticated();
  },
};