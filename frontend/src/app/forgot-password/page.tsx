"use client";

import { useRouter } from "next/navigation";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

import { AxiosError } from "axios";
import { Loader2 } from "lucide-react";
import { toast } from "sonner";

import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";

import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";

import AuthService from "@/services/auth.service";

import {
  forgotPasswordSchema,
  ForgotPasswordFormData,
} from "@/lib/validators/forgot-password-schema";

export default function ForgotPasswordPage() {
  const router = useRouter();

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<ForgotPasswordFormData>({
    resolver: zodResolver(forgotPasswordSchema),
  });

  async function onSubmit(
    data: ForgotPasswordFormData
  ) {
    try {
      await AuthService.forgotPassword(data.email);

      toast.success(
        "If an account exists, a reset email has been sent."
      );

      router.push("/login");
    } catch (err) {
      const error = err as AxiosError<{ message?: string }>;

      toast.error(
        error.response?.data?.message ??
          "Unable to process request."
      );
    }
  }

  return (
    <main className="flex min-h-screen items-center justify-center p-6">
      <Card className="w-full max-w-md">
        <CardHeader>
          <CardTitle>Forgot Password</CardTitle>

          <CardDescription>
            Enter your email to receive a reset link.
          </CardDescription>
        </CardHeader>

        <CardContent>
          <form
            onSubmit={handleSubmit(onSubmit)}
            className="space-y-4"
          >
            <div>
              <Input
                type="email"
                placeholder="Email"
                {...register("email")}
              />

              {errors.email && (
                <p className="mt-1 text-sm text-destructive">
                  {errors.email.message}
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
                ? "Sending..."
                : "Send Reset Link"}
            </Button>

            <Button
              type="button"
              variant="link"
              className="w-full"
              onClick={() => router.push("/login")}
            >
              Back to Login
            </Button>
          </form>
        </CardContent>
      </Card>
    </main>
  );
}