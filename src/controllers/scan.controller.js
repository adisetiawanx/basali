import { doc, getDocs, getDoc, updateDoc, addDoc, setDoc, query, where, collection, serverTimestamp } from "firebase/firestore";
import Config from "../config.js";
import { verifyAuthToken } from "../middlewares/authenticated.middleware.js";

export const UserScannedAksara = async (req, res) => {
  try {
    await verifyAuthToken(req, res, async () => {
      const { scannedAksara } = req.body;
      const userId = req.userData.id;

      if (!scannedAksara) {
        return res.status(400).json({
          msg: "Missing scannedAksara in the request body",
        });
      }

      const scansCollectionRef = collection(doc(Config.firebaseDB, 'scanAksara', userId), 'scans');

      const newScanDocRef = await addDoc(scansCollectionRef, {
        scanned_aksara: scannedAksara,
        timestamp: serverTimestamp(),
      });

      return res.json({
        msg: "Successfully added scan result to history",
        scanId: newScanDocRef.id,
        userId: userId,
      });
    });
  } catch (error) {
    return res.status(500).json({
      msg: error,
    });
  }
};

export const getUserHistoriesScanByUserId = async (req, res) => {
  try {
    await verifyAuthToken(req, res, async () => {
      const userId = req.userData.id;

      const scansCollectionRef = collection(doc(Config.firebaseDB, 'scanAksara', userId), 'scans');
      const historySnapshot = await getDocs(scansCollectionRef);

      if (!historySnapshot.empty) {
        const historyData = historySnapshot.docs.map(doc => {
          const data = doc.data();
          const timestamp = data.timestamp.toDate().toISOString();
          return { scanned_aksara: data.scanned_aksara, timestamp };
        });
        return res.json(historyData);
      } else {
        return res.status(400).json({
          msg : "History not found",
        });
      }
    });
  } catch (error) {
    return res.status(500).json({
      msg: error,
    });
  }
};

export const getUserHistoryById = async (req, res) => {
  try {
    await verifyAuthToken(req, res, async () => {
      const userId = req.userData.id;
      const historyId = req.params.id;

      const historyDocumentRef = doc(Config.firebaseDB, 'scanAksara', userId, 'scans', historyId);

      const historySnapshot = await getDoc(historyDocumentRef);

      if (historySnapshot.exists()) {
        const historyData = {
          scanned_aksara: historySnapshot.data().scanned_aksara,
          timestamp: historySnapshot.data().timestamp.toDate().toISOString(),
        };

        return res.json(historyData);
      } else {
        return res.status(404).json({
          msg: "History not found",
        });
      }
    });
  } catch (error) {
    return res.status(500).json({
      msg: error.toString(),
    });
  }
};
