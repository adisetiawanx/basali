import multer from "multer";

import Config from "../config.js";

const bucket = Config.bucketStorage;
const db = Config.firebaseFirestore;

const storage = multer.memoryStorage();
const imageUpload = multer({
  storage: storage,
  limits: {
    fieldSize: 5 * 1024 * 1024,
  },
});

export const userProfile = async (req, res) => {
  try {
    const db = Config.firebaseFirestore;

    const userId = req.userData.id;
    const userDoc = await db.collection("users").doc(userId).get();

    if (!userDoc.exists) {
      return res.status(400).json({
        msg: "Profile not found",
      });
    }

    const userData = userDoc.data();

    return res.json({
      msg: "Successfully retrieved user profile",
      data: userData,
    });
  } catch (error) {
    return res.status(500).json({
      msg: error,
    });
  }
};

export const updateProfilePhoto = async (req, res) => {
  try {
    const userId = req.userData.id;

    let uploadedImageUrl = null;
    imageUpload.single("image")(req, res, async () => {
      if (!req.file) {
        return res.status(400).json({
          msg: "Missing image in the request body",
        });
      }
      const uniqueFileName = `${Date.now()}_${req.file.originalname}`;
      uploadedImageUrl = `https://storage.googleapis.com/basali-bucket/images/${uniqueFileName}`;
      const blob = bucket.file(`images/${uniqueFileName}`);
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
          .update({
            photo: {
              name: uniqueFileName,
              url: uploadedImageUrl,
            },
          });

        return res.json({
          msg: "Profile updated successfully with photo",
          userId,
          data: {
            name: uniqueFileName,
            url: uploadedImageUrl,
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

export const deleteProfilePhoto = async (req, res) => {
  try {
    const userId = req.userData.id;

    const userData = await db.collection("users").doc(userId).get();
    const photoName = userData.data().photo.name;

    if (!photoName) {
      return res.json({
        msg: "Profile picture deleted successfully",
        userId,
      });
    }

    bucket.file("images/" + photoName).delete();

    await db
      .collection("users")
      .doc(userId)
      .update({
        photo: {
          name: null,
          url: null,
        },
      });

    return res.json({
      msg: "Profile picture deleted successfully",
      userId,
    });
  } catch (error) {
    return res.status(500).json({
      msg: error,
    });
  }
};
