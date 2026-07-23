import { STORAGE_KEYS } from "@/constants/storage";

export const TokenStorage = {
  getAccessToken(): string | null {
    if (typeof window === "undefined") return null;

    return localStorage.getItem(STORAGE_KEYS.ACCESS_TOKEN);
  },

  getRefreshToken(): string | null {
    if (typeof window === "undefined") return null;

    return localStorage.getItem(STORAGE_KEYS.REFRESH_TOKEN);
  },

  setTokens(accessToken: string, refreshToken: string) {
    localStorage.setItem(STORAGE_KEYS.ACCESS_TOKEN, accessToken);

    localStorage.setItem(STORAGE_KEYS.REFRESH_TOKEN, refreshToken);
  },

  clear() {
    localStorage.removeItem(STORAGE_KEYS.ACCESS_TOKEN);

    localStorage.removeItem(STORAGE_KEYS.REFRESH_TOKEN);
  },

  isAuthenticated() {
    return !!this.getAccessToken();
  },
};