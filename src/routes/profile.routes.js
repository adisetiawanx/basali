import { Router } from "express";

import {
    deleteProfilePhoto,
    profileUser, updateProfilePhoto,
} from "../controllers/profile.controller.js";

const router = Router();

router.get("/profile-user/:id", profileUser);
router.put("/photo-profile/:id", updateProfilePhoto);
router.post("/delete-photo/:id", deleteProfilePhoto);

export default router;