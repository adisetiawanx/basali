import { Router } from "express";

import {
  deleteProfilePhoto,
  userProfile,
  updateProfilePhoto,
} from "../controllers/profile.controller.js";
import { verifyAuthToken } from "../middlewares/authenticated.middleware.js";

const router = Router();

router.get("/profile", verifyAuthToken, userProfile);
router.put("/profile/photo", verifyAuthToken, updateProfilePhoto);
router.delete("/profile/photo", verifyAuthToken, deleteProfilePhoto);

export default router;
