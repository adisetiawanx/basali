import { Router } from "express";

import {
  UserScannedAksara,
  getUserHistoriesScanByUserId,
  getUserHistoryById,
} from "../controllers/scan.controller.js";
import { verifyAuthToken } from "../middlewares/authenticated.middleware.js";

const router = Router();

router.post("/aksara", verifyAuthToken, UserScannedAksara);
router.get("/aksara", verifyAuthToken, getUserHistoriesScanByUserId);
router.get("/aksara/:id", verifyAuthToken, getUserHistoryById);

router.post("/test1", UserScannedAksara);
router.post("/test2", getUserHistoriesScanByUserId);

export default router;
