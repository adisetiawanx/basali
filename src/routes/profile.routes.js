import { Router } from "express";

import {
    deleteProfilePhoto,
    userProfile, updateProfilePhoto,
} from "../controllers/profile.controller.js";

const router = Router();

router.get("/profile-user", userProfile);
router.put("/photo-profile", updateProfilePhoto);
router.delete("/delete-photo", deleteProfilePhoto);

export default router;