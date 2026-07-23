import { InternalAxiosRequestConfig } from "axios";

export interface RetryRequestConfig
  extends InternalAxiosRequestConfig {
  _retry?: boolean;
}