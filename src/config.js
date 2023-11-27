import { initializeApp } from "firebase/app";
import { getFirestore } from "firebase/firestore";
import { getAuth } from "firebase/auth";
import Admin from "firebase-admin";
import Dotenv from "dotenv";
import Fs from "fs";
import path from "path";
import { fileURLToPath } from "url";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const ServiceAccount = JSON.parse(
  Fs.readFileSync(path.join(__dirname, "serviceAccountKey.json"))
);

Dotenv.config();

const {
  APP_PORT,
  APP_GMAIL_EMAIL,
  APP_GMAIL_PASSWORD,
  FIREBASE_API_KEY,
  FIREBASE_AUTH_DOMAIN,
  FIREBASE_PROJECT_ID,
  FIREBASE_STORAGE_BUCKET,
  FIREBASE_MESSAGING_SENDER_ID,
  FIREBASE_APP_ID,
  FIREBASE_MEASUREMENT_ID,
} = process.env;

const firebaseAuth = Admin.initializeApp({
  credential: Admin.credential.cert(ServiceAccount),
}).auth();

const firebaseApp = initializeApp({
  apiKey: FIREBASE_API_KEY,
  authDomain: FIREBASE_AUTH_DOMAIN,
  projectId: FIREBASE_PROJECT_ID,
  storageBucket: FIREBASE_STORAGE_BUCKET,
  messagingSenderId: FIREBASE_MESSAGING_SENDER_ID,
  appId: FIREBASE_APP_ID,
  measurementId: FIREBASE_MEASUREMENT_ID,
});

const firebaseDB = getFirestore();
const firebaseClientAuth = getAuth();

export default {
  PORT: APP_PORT,
  gmailEmail: APP_GMAIL_EMAIL,
  gmailPassword: APP_GMAIL_PASSWORD,
  firebaseApp,
  firebaseDB,
  firebaseAuth,
  firebaseClientAuth,
};
