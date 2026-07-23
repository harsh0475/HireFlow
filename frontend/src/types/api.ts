export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

export interface PageableResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}