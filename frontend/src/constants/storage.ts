export const STORAGE_KEYS = {
  ACCESS_TOKEN:
    process.env.NEXT_PUBLIC_TOKEN_KEY ?? "hireflow_access_token",

  REFRESH_TOKEN:
    process.env.NEXT_PUBLIC_REFRESH_TOKEN_KEY ??
    "hireflow_refresh_token",
} as const;