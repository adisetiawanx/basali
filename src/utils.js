import { signInWithCustomToken } from "firebase/auth";

import Config from "./config.js";

const generateUniqueCode = () => {
  return Math.floor(100000 + Math.random() * 900000);
};

const generateNewAccessToken = async (userId) => {
  try {
    const auth = Config.firebaseAuth;
    const clientAuth = Config.firebaseClientAuth;

    const customToken = await auth.createCustomToken(userId);
    const credential = await signInWithCustomToken(clientAuth, customToken);
    const jwtToken = await credential.user.getIdToken(true);
    return jwtToken;
  } catch (error) {
    console.log(error);
    throw error;
  }
};

export { generateUniqueCode, generateNewAccessToken };
