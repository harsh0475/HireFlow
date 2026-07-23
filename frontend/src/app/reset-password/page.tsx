"use client";

import { useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

import { AxiosError } from "axios";
import { Eye, EyeOff, Loader2 } from "lucide-react";
import { toast } from "sonner";

import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";

import AuthService from "@/services/auth.service";

import {
  resetPasswordSchema,
  ResetPasswordFormData,
} from "@/lib/validators/reset-password-schema";

export default function ResetPasswordPage() {
  const router = useRouter();

  const params = useSearchParams();

  const token = params.get("token") ?? "";

  const [showPassword, setShowPassword] =
    useState(false);

  const [
    showConfirmPassword,
    setShowConfirmPassword,
  ] = useState(false);

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<ResetPasswordFormData>({
    resolver: zodResolver(resetPasswordSchema),
  });

  async function onSubmit(
    data: ResetPasswordFormData
  ) {
    if (!token) {
      toast.error("Invalid or expired reset link.");

      return;
    }

    try {
      await AuthService.resetPassword(
        token,
        data.password
      );

      toast.success(
        "Password reset successfully."
      );

      router.replace("/login");
    } catch (err) {
      const error = err as AxiosError<{ message?: string }>;

      toast.error(
        error.response?.data?.message ??
          "Unable to reset password."
      );
    }
  }

  return (
    <main className="flex min-h-screen items-center justify-center p-6">
      <Card className="w-full max-w-md">
        <CardHeader>
          <CardTitle>Reset Password</CardTitle>

          <CardDescription>
            Create a new password.
          </CardDescription>
        </CardHeader>

        <CardContent>
          <form
            onSubmit={handleSubmit(onSubmit)}
            className="space-y-4"
          >
            <div className="relative">
              <Input
                type={
                  showPassword ? "text" : "password"
                }
                placeholder="New Password"
                {...register("password")}
              />

              <button
                type="button"
                className="absolute right-3 top-2.5"
                onClick={() =>
                  setShowPassword((v) => !v)
                }
              >
                {showPassword ? (
                  <EyeOff size={18} />
                ) : (
                  <Eye size={18} />
                )}
              </button>

              {errors.password && (
                <p className="mt-1 text-sm text-destructive">
                  {errors.password.message}
                </p>
              )}
            </div>

            <div className="relative">
              <Input
                type={
                  showConfirmPassword
                    ? "text"
                    : "password"
                }
                placeholder="Confirm Password"
                {...register("confirmPassword")}
              />

              <button
                type="button"
                className="absolute right-3 top-2.5"
                onClick={() =>
                  setShowConfirmPassword(
                    (v) => !v
                  )
                }
              >
                {showConfirmPassword ? (
                  <EyeOff size={18} />
                ) : (
                  <Eye size={18} />
                )}
              </button>

              {errors.confirmPassword && (
                <p className="mt-1 text-sm text-destructive">
                  {errors.confirmPassword.message}
                </p>
              )}
            </div>

            <Button
              className="w-full"
              disabled={isSubmitting}
            >
              {isSubmitting && (
                <Loader2 className="mr-2 size-4 animate-spin" />
              )}

              {isSubmitting
                ? "Updating..."
                : "Reset Password"}
            </Button>
          </form>
        </CardContent>
      </Card>
    </main>
  );
}