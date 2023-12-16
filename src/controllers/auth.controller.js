import Nodemailer from "nodemailer";

import Config from "../config.js";
import { generateUniqueCode } from "../utils.js";

const auth = Config.firebaseAuth;
const db = Config.firebaseFirestore;

const sendEmailVerification = async (email, uniqueCode) => {
  const transporter = Nodemailer.createTransport({
    service: "gmail",
    auth: {
      user: process.env.APP_GMAIL_EMAIL,
      pass: process.env.APP_GMAIL_PASSWORD,
    },
  });

  const emailBody = {
    from: '"NocturService" <nocturservice@gmail.com>', // sender address
    to: email, // list of receivers
    subject: "Konfirmasi Pendaftaran Akun Anda", // Subject line
    text: `Terima kasih telah mendaftar di NocturService. Gunakan kode berikut untuk menyelesaikan registrasi anda. Kode ini hanya berlaku untuk 30 menit. Kode: ${uniqueCode}. Hormat kami, NocturService`, // plain text body
    html: `<div style=" font-family: Helvetica, Arial, sans-serif; min-width: 1000px; overflow: auto; line-height: 2; " > <div style="margin: 50px auto; width: 70%; padding: 20px 0"> <div style="border-bottom: 1px solid #eee"> <a href="http://127.0.0.1:3000/" style=" font-size: 1.4em; color: #00466a; text-decoration: none; font-weight: 600; " >NocturService</a > </div> <p style="font-size: 1em; font-weight: bold">Haii,</p> <p style="margin-top: -14px"> Terima kasih telah mendaftar di NocturService. Gunakan kode berikut untuk menyelesaikan registrasi anda. Kode ini hanya berlaku untuk 30 menit. </p> <h2 style=" width: max-content; padding: 0 14px; border: 1px solid gray; border-radius: 4px; letter-spacing: 5px; " > ${uniqueCode} </h2> <p style="font-size: 0.9em"> Hormat kami,<br /><strong>NocturService</strong> </p> <hr style="border: none; border-top: 1px solid #eee" /> <div style=" float: right; padding: 8px 0; color: #aaa; font-size: 0.8em; line-height: 1; font-weight: 300; " > <p>NocturService</p> <p>Badung, Bali</p> <p>Indonesia</p> </div> </div> </div>`, // html body
  };

  await transporter.sendMail(emailBody);
};

export const registerUser = async (req, res) => {
  try {
    const { email, password, confirmPassword, name } = req.body;

    if ((!email, !password, !confirmPassword, !name)) {
      return res.status(400).json({
        msg: "Invalid parameters",
      });
    }

    if (confirmPassword !== password) {
      return res.status(400).json({
        msg: "Password and Confirm Password does not match",
      });
    }

    const user = await auth.createUser({
      email,
      password,
      displayName: name,
    });

    const uniqueCode = generateUniqueCode();
    await sendEmailVerification(user.email, uniqueCode);

    await db
      .collection("users")
      .doc(user.uid)
      .create({
        email: user.email,
        isVerified: false,
        name: user.displayName,
        verificationCode: uniqueCode,
        photo: {
          name: null,
          url: null,
        },
      });

    return res.status(201).json({
      msg: "The verification code has been sent to your email",
      userId: user.uid,
    });
  } catch (error) {
    if (error.code === "auth/email-already-in-use") {
      return res.status(400).json({
        msg: "Email is already in use",
      });
    } else if (error.code === "auth/invalid-email") {
      return res.status(400).json({
        msg: "Invalid email",
      });
    } else if (error.code === "auth/weak-password") {
      return res.status(400).json({
        msg: "Weak password (Password must be at least 6 characters)",
      });
    } else {
      return res.status(500).json({
        msg: error,
      });
    }
  }
};

export const verifyEmailVerificationCode = async (req, res) => {
  try {
    const { code } = req.body;
    const userId = req.userData.id;

    if (!code) {
      return res.status(400).json({
        msg: "Invalid code format",
      });
    }

    const userDoc = await db.collection("users").doc(userId).get();

    if (!userDoc.exists) {
      return res.status(400).json({
        msg: "User not found",
        userId,
      });
    }

    const userData = userDoc.data();

    if (userData.isVerified === true) {
      return res.status(200).json({
        msg: "User has been verified",
        userId,
      });
    }

    if (Number(code.trim()) !== userData.verificationCode) {
      return res.status(400).json({
        msg: "Verification code is incorrect",
      });
    }

    await auth.updateUser(userId, {
      emailVerified: true,
    });

    await db.collection("users").doc(userId).update({
      isVerified: true,
    });

    return res.json({
      msg: "Email successfully verified",
      userId,
    });
  } catch (error) {
    return res.status(500).json({
      msg: error,
    });
  }
};

export const resendEmailVerificationCode = async (req, res) => {
  try {
    const userId = req.userData.id;
    const email = req.userData.email;
    const uniqueCode = generateUniqueCode();

    await sendEmailVerification(email, uniqueCode);

    const userDoc = await db.collection("users").doc(userId).get();

    if (!userDoc.exists) {
      return res.status(400).json({
        msg: "User not found",
        userId,
      });
    }

    const userData = userDoc.data();

    if (userData.isVerified === true) {
      return res.status(200).json({
        msg: "User has been verified",
        userId,
      });
    }

    await db.collection("users").doc(userId).update({
      verificationCode: uniqueCode,
    });

    return res.json({
      msg: "The verification code was successfully resent",
      userId,
    });
  } catch (error) {
    return res.status(500).json({
      msg: error,
    });
  }
};
