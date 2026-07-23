"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

import { AxiosError } from "axios";
import { Eye, EyeOff, Loader2 } from "lucide-react";
import { toast } from "sonner";

import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";

import AuthService from "@/services/auth.service";

import { useAuth } from "@/hooks/use-auth";
import { getDefaultRoute } from "@/lib/auth-redirect";

import {
  registerSchema,
  RegisterFormData,
} from "@/lib/validators/register-schema";

export default function RegisterPage() {
  const router = useRouter();

  const { login } = useAuth();

  const [showPassword, setShowPassword] = useState(false);

  const [showConfirmPassword, setShowConfirmPassword] =
    useState(false);

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<RegisterFormData>({
    resolver: zodResolver(registerSchema),
  });

  async function onSubmit(data: RegisterFormData) {
    try {
      const response = await AuthService.register({
        firstName: data.firstName,
        lastName: data.lastName,
        email: data.email,
        password: data.password,
      });

      login(response.data.data);

      toast.success("Welcome to HireFlow!");

      router.replace(
        getDefaultRoute(response.data.data.user.role)
      );
      
    } catch (err) {
      const error = err as AxiosError<{ message?: string }>;

      toast.error(
        error.response?.data?.message ??
          "Registration failed."
      );
    }
  }

  return (
    <main className="flex min-h-screen items-center justify-center p-6">
      <Card className="w-full max-w-md">
        <CardHeader>
          <CardTitle>Create Account</CardTitle>

          <CardDescription>
            Join HireFlow
          </CardDescription>
        </CardHeader>

        <CardContent>
          <form
            onSubmit={handleSubmit(onSubmit)}
            className="space-y-4"
          >
            <div>
              <Input
                placeholder="First Name"
                {...register("firstName")}
              />

              {errors.firstName && (
                <p className="mt-1 text-sm text-destructive">
                  {errors.firstName.message}
                </p>
              )}
            </div>

            <div>
              <Input
                placeholder="Last Name"
                {...register("lastName")}
              />

              {errors.lastName && (
                <p className="mt-1 text-sm text-destructive">
                  {errors.lastName.message}
                </p>
              )}
            </div>

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

            <div className="relative">
              <Input
                type={
                  showPassword ? "text" : "password"
                }
                placeholder="Password"
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
                ? "Creating Account..."
                : "Create Account"}
            </Button>

            <Button
              type="button"
              variant="link"
              className="w-full"
              onClick={() =>
                router.push("/login")
              }
            >
              Already have an account?
            </Button>
          </form>
        </CardContent>
      </Card>
    </main>
  );
}