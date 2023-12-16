import { Router } from "express";

import {
  registerUser,
  resendEmailVerificationCode,
  verifyEmailVerificationCode,
} from "../controllers/auth.controller.js";
import { verifyAuthToken } from "../middlewares/authenticated.middleware.js";

const router = Router();

router.post("/register", registerUser);

router.patch("/verify-email", verifyAuthToken, verifyEmailVerificationCode);
router.patch("/resend-code", verifyAuthToken, resendEmailVerificationCode);

export default router;
