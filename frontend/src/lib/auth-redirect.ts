import { User } from "@/types/auth";

export function getDefaultRoute(role: User["role"]): string {
  switch (role) {
    case "ADMIN":
      return "/admin";

    case "RECRUITER":
      return "/recruiter";

    case "CANDIDATE":
      return "/candidate";

    default:
      return "/";
  }
}