import { doc, getDocs, getDoc, updateDoc, addDoc, setDoc, query, where, collection, serverTimestamp } from "firebase/firestore";
import Config from "../config.js";
import { verifyAuthToken } from "../middlewares/authenticated.middleware.js";
import * as tf from "@tensorflow/tfjs";
import * as tfnode from "@tensorflow/tfjs-node";

const handler = tfnode.io.fileSystem("C:/Dokumen/Bangkit/Capstone/basali/src/modelpbconvertgraph/model.json");
const model = await tf.loadGraphModel(handler);

import multer from 'multer';
const storage = multer.memoryStorage();
const imageUpload = multer({ storage: storage });

export const UserScannedAksara = async (req, res) => {
  try {
    await verifyAuthToken(req, res, async () => {
      imageUpload.single("image")(req, res, async () => {
        const userId = req.userData.id;

        if (!req.file) {
          return res.status(400).json({
            msg: "Missing scannedAksara or image in the request body",
          });
        }

        const scansCollectionRef = collection(doc(Config.firebaseDB, 'scanAksara', userId), 'scans');
        const newScanDocRef = await addDoc(scansCollectionRef, {
          timestamp: serverTimestamp(),
        });

        const imageBuffer = req.file.buffer;

        const classification = await Config.aksaraClassify(model, imageBuffer);
        console.log("Prediction:", classification);

        // Get the result based on the predicted class
        const result = await Config.aksaraClassifyClass(classification);
        console.log("Result:", result);

        // For now, let's assume you want to add the result to the scanned document
        await updateDoc(newScanDocRef, { predictionResult: result.result });

        return res.json({
          msg: "Successfully added scan result to history",
          scanId: newScanDocRef.id,
          userId: userId,
          prediction: result.result,
        });
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