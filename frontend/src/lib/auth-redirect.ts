import { User } from "@/types/auth";

export function getDefaultRoute(role: User["role"]): string {
  switch (role) {
    case "ADMIN":
      return "/dashboard/admin";

    case "RECRUITER":
      return "/dashboard/recruiter";

    case "CANDIDATE":
      return "/dashboard/candidate";

    default:
      return "/";
  }
}