import { STORAGE_KEYS } from "@/constants/storage";

export const TokenStorage = {
  getAccessToken() {
    if (typeof window === "undefined") return null;

    return localStorage.getItem(
      STORAGE_KEYS.ACCESS_TOKEN
    );
  },

  getRefreshToken() {
    if (typeof window === "undefined") return null;

    return localStorage.getItem(
      STORAGE_KEYS.REFRESH_TOKEN
    );
  },

  getUser() {
    if (typeof window === "undefined") return null;

    return localStorage.getItem(
      STORAGE_KEYS.USER
    );
  },

  setTokens(
    accessToken: string,
    refreshToken: string
  ) {
    localStorage.setItem(
      STORAGE_KEYS.ACCESS_TOKEN,
      accessToken
    );

    localStorage.setItem(
      STORAGE_KEYS.REFRESH_TOKEN,
      refreshToken
    );
  },

  setUser(user: unknown) {
    localStorage.setItem(
      STORAGE_KEYS.USER,
      JSON.stringify(user)
    );
  },

  clear() {
    localStorage.removeItem(
      STORAGE_KEYS.ACCESS_TOKEN
    );

    localStorage.removeItem(
      STORAGE_KEYS.REFRESH_TOKEN
    );

    localStorage.removeItem(STORAGE_KEYS.USER);
  },

  isAuthenticated() {
    return !!this.getAccessToken();
  },
};