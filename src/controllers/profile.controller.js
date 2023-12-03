import { doc, getDoc, addDoc, setDoc, updateDoc } from "firebase/firestore";
import Config from "../config.js";

export const profileUser = async (req, res) => {
  try {
    const userId = req.params.id;
    const profileDocumentRef = doc(Config.firebaseDB, 'profile', userId);
    const profileSnapshot = await getDoc(profileDocumentRef);

    if (profileSnapshot.exists()) {
      const profileData = profileSnapshot.data();
      return res.json(profileData);
    } else {
      return res.status(400).json({
        msg : "Profile not found",
       });
    }
  } catch (error) {
    return res.status(500).json({
      msg: error,
    });
  }
};

export const updateProfilePhoto = async (req, res) => {
  try {
    const userId = req.params.id;
    const { photoUrl } = req.body;
    const profileDocumentRef = doc(Config.firebaseDB, 'profile', userId);
    
    if (!photoUrl) {
      return res.status(400).json({
        msg: "Missing 'photoUrl' in the request body",
      });
    }

    await updateDoc(profileDocumentRef, {
      photoUrl: photoUrl,
    });

    return res.json({
      msg: "Profile updated successfully with photo",
      userId,
    });
  } catch (error) {
    return res.status(500).json({
      msg: error,
    });
  }
};

export const deleteProfilePhoto = async (req, res) => {
  try {
    const userId = req.params.id;
    const profileDocumentRef = doc(Config.firebaseDB, 'profile', userId);
    
    await updateDoc(profileDocumentRef, {
      photoUrl: null,
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



