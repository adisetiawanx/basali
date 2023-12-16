import { Router } from "express";

import {
    UserScannedAksara, getUserHistoriesScanByUserId, getUserHistoryById,
} from "../controllers/scan.controller.js";

const router = Router();

router.post("/scan-aksara", UserScannedAksara);
router.get("/scan-aksara", getUserHistoriesScanByUserId);
router.get("/scan-aksara/:id", getUserHistoryById);

export default router;