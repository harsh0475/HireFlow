import { z } from "zod";

export const registerSchema = z
  .object({
    firstName: z.string().min(1, "First name is required."),

    lastName: z.string().min(1, "Last name is required."),

    email: z
      .string()
      .min(1, "Email is required.")
      .email("Please enter a valid email address."),

    password: z
      .string()
      .min(8, "Password must be at least 8 characters."),

    confirmPassword: z
      .string()
      .min(1, "Confirm password is required."),
  })
  .refine((data) => data.password === data.confirmPassword, {
    path: ["confirmPassword"],
    message: "Passwords do not match.",
  });

export type RegisterFormData = z.infer<typeof registerSchema>;