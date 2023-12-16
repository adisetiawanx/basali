import { jwtDecode } from "jwt-decode";

import Config from "../config.js";

export const verifyAuthToken = async (req, res, next) => {
  try {
    if (
      req.headers.authorization &&
      req.headers.authorization.split(" ")[0] === "Bearer"
    ) {
      req.authToken = req.headers.authorization.split(" ")[1];
    } else {
      return res.status(403).json({
        msg: "Unauthorized",
      });
    }
    const auth = Config.firebaseAuth;
    const decodedToken = await auth.verifyIdToken(req.authToken);
    req.userData = {
      id: decodedToken.uid,
      email: decodedToken.email,
    };
    next();
  } catch (error) {
    if (error.code === "auth/argument-error") {
      return res.status(400).json({
        msg: "Invalid token",
      });
    } else if (error.code === "auth/id-token-expired") {
      //using this method so that clients no longer need to apply the firebase SDK (unsafe and not recommended)
      const decodedToken = jwtDecode(req.authToken);
      req.userData = {
        id: decodedToken.user_id,
        email: decodedToken.email,
      };
      next();
    } else {
      return res.status(500).json({
        msg: error,
      });
    }
  }
};
