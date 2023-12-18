import * as tf from "@tensorflow/tfjs";
import multer from "multer";

import Config from "../config.js";

const db = Config.firebaseFirestore;
const bucket = Config.bucketStorage;

const model = await tf.loadGraphModel(Config.modelUrl);

const storage = multer.memoryStorage();
const imageUpload = multer({
  storage: storage,
  limits: {
    fieldSize: 5 * 1024 * 1024,
  },
});

export const UserScannedAksara = async (req, res) => {
  try {
    imageUpload.single("image")(req, res, async () => {
      const userId = req.userData.id;

      if (!req.file) {
        return res.status(400).json({
          msg: "Missing image in the request body",
        });
      }

      const imageBuffer = req.file.buffer;

      const classification = await Config.aksaraClassify(model, imageBuffer);

      // Get the result based on the predicted class
      const result = await Config.aksaraClassifyClass(classification);

      const uniqueFileName = `${Date.now()}_${req.file.originalname}`;
      const uploadedImageUrl = `https://storage.googleapis.com/basali-bucket/scan/${uniqueFileName}`;
      const blob = bucket.file(`scan/${uniqueFileName}`);
      const stream = blob.createWriteStream();

      stream.on("error", (e) => {
        res.status(500).json({
          msg: "Failed to upload image",
        });
      });
      stream.on("finish", async () => {
        await db
          .collection("users")
          .doc(userId)
          .collection("scannedAksara")
          .add({
            scannedAt: new Date(Date.now()),
            predictionResult: result.result,
            imageUrl: uploadedImageUrl,
          });

        return res.json({
          msg: "Successfully added scan result to history",
          userId: userId,
          data: {
            prediction: result.result,
            imageUrl: uploadedImageUrl,
          },
        });
      });
      stream.end(req.file.buffer);
    });
  } catch (error) {
    return res.status(500).json({
      msg: error,
    });
  }
};

export const getUserHistoriesScanByUserId = async (req, res) => {
  try {
    const userId = req.userData.id;

    const historiesScannedAksara = await db
      .collection("users")
      .doc(userId)
      .collection("scannedAksara")
      .get();

    const historiesData = [];

    historiesScannedAksara.forEach((scannedAkasara) => {
      const predictionId = scannedAkasara.id;
      const predictionResult = scannedAkasara.data().predictionResult;
      const scannedAt = new Date(
        scannedAkasara.data().scannedAt._seconds * 1000 +
          scannedAkasara.data().scannedAt._nanoseconds / 1000000
      ).toDateString();

      historiesData.push({
        predictionId,
        predictionResult,
        scannedAt,
      });
    });

    return res.json({
      msg: "Successfully retrieved the user's aksara scan history",
      data: historiesData,
    });
  } catch (error) {
    return res.status(500).json({
      msg: error,
    });
  }
};

export const getUserHistoryById = async (req, res) => {
  try {
    const userId = req.userData.id;
    const historyId = req.params.id;

    const historyScan = await db
      .collection("users")
      .doc(userId)
      .collection("scannedAksara")
      .doc(historyId)
      .get();

    if (!historyScan.exists) {
      return res.status(400).json({
        msg: "History scan not found",
      });
    }
    const scannedAt = new Date(
      historyScan.data().scannedAt._seconds * 1000 +
        historyScan.data().scannedAt._nanoseconds / 1000000
    ).toDateString();

    const historyScanData = {
      scannedAt,
      predictionResult: historyScan.data().predictionResult,
      imageUrl: historyScan.data().imageUrl,
    };

    return res.json({
      msg: "Successfully retrieved the user's aksara scan history",
      data: historyScanData,
    });
  } catch (error) {
    return res.status(500).json({
      msg: error.toString(),
    });
  }
};
