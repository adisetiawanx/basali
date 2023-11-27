import { Router } from "express";

import {
  loginUser,
  logoutUser,
  refreshAccessToken,
  registerUser,
  resendEmailVerificationCode,
  verifyEmailVerificationCode,
} from "../controllers/auth.controller.js";
import { verifyAuthToken } from "../middlewares/authenticated.middleware.js";

const router = Router();

router.post("/register", registerUser);
router.post("/login", loginUser);
router.delete("/logout", verifyAuthToken, logoutUser);

router.patch("/verify-email", verifyAuthToken, verifyEmailVerificationCode);
router.patch("/resend-code", verifyAuthToken, resendEmailVerificationCode);

router.put("/token", verifyAuthToken, refreshAccessToken);

export default router;
