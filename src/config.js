import Admin from "firebase-admin";
import Dotenv from "dotenv";
import Fs from "fs";
import path from "path";
import { fileURLToPath } from "url";
import * as tfnode from "@tensorflow/tfjs-node";
import { Storage } from "@google-cloud/storage";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const serviceAccountKeyPath = path.join(__dirname, "serviceAccountKey.json");

Dotenv.config();

const {
  APP_PORT,
  APP_GMAIL_EMAIL,
  APP_GMAIL_PASSWORD,
  APP_PROJECT_ID,
  APP_BUCKET_NAME,
  APP_MODEL_URL,
} = process.env;

const storage = new Storage({
  projectId: APP_PROJECT_ID,
  keyFilename: serviceAccountKeyPath,
});
const bucketStorage = storage.bucket(APP_BUCKET_NAME);

const serviceAccount = JSON.parse(Fs.readFileSync(serviceAccountKeyPath));

const firebaseAdmin = Admin.initializeApp({
  credential: Admin.credential.cert(serviceAccount),
});
const firebaseAuth = firebaseAdmin.auth();
const firebaseFirestore = firebaseAdmin.firestore();

function aksaraClassify(model, imageBuffer) {
  const tensor = tfnode.node
    .decodeImage(imageBuffer, 3) // Specify 3 channels to decode RGB images
    .resizeNearestNeighbor([150, 150])
    .expandDims()
    .toFloat()
    .div(255.0);

  // Ensure that the shape matches the expected shape [-1, 150, 150, 3]
  if (tensor.shape[3] !== 3) {
    throw new Error(
      `Expected 3 channels in the image, but found ${tensor.shape[3]} channels.`
    );
  }

  return model.predict(tensor).data();
}

function aksaraClassifyClass(aksaraClassifyResult) {
  const aksaraClassifyClass = aksaraClassifyResult.indexOf(
    Math.max(...aksaraClassifyResult)
  );
  let result = {};

  if (aksaraClassifyClass === 0) {
    result = { result: "Adeg-Adeg" };
  } else if (aksaraClassifyClass === 1) {
    result = { result: "Ba" };
  } else if (aksaraClassifyClass === 2) {
    result = { result: "Bisah" };
  } else if (aksaraClassifyClass === 3) {
    result = { result: "Ca" };
  } else if (aksaraClassifyClass === 4) {
    result = { result: "Cecek" };
  } else if (aksaraClassifyClass === 5) {
    result = { result: "Da" };
  } else if (aksaraClassifyClass === 6) {
    result = { result: "Delapan" };
  } else if (aksaraClassifyClass === 7) {
    result = { result: "Dua" };
  } else if (aksaraClassifyClass === 8) {
    result = { result: "Empat" };
  } else if (aksaraClassifyClass === 9) {
    result = { result: "Enam" };
  } else if (aksaraClassifyClass === 10) {
    result = { result: "Ga" };
  } else if (aksaraClassifyClass === 11) {
    result = { result: "Gantungan Ba" };
  } else if (aksaraClassifyClass === 12) {
    result = { result: "Gantungan Ca" };
  } else if (aksaraClassifyClass === 13) {
    result = { result: "Gantungan Da" };
  } else if (aksaraClassifyClass === 14) {
    result = { result: "Gantungan Ga" };
  } else if (aksaraClassifyClass === 15) {
    result = { result: "Gantungan Ha" };
  } else if (aksaraClassifyClass === 16) {
    result = { result: "Gantungan Ja" };
  } else if (aksaraClassifyClass === 17) {
    result = { result: "Gantungan Ka" };
  } else if (aksaraClassifyClass === 18) {
    result = { result: "Gantungan La" };
  } else if (aksaraClassifyClass === 19) {
    result = { result: "Gantungan Ma" };
  } else if (aksaraClassifyClass === 20) {
    result = { result: "Gantungan Na" };
  } else if (aksaraClassifyClass === 21) {
    result = { result: "Gantungan Nga" };
  } else if (aksaraClassifyClass === 22) {
    result = { result: "Gantungan Nya" };
  } else if (aksaraClassifyClass === 23) {
    result = { result: "Gantungan Pa" };
  } else if (aksaraClassifyClass === 24) {
    result = { result: "Gantungan Ra" };
  } else if (aksaraClassifyClass === 25) {
    result = { result: "Gantungan Sa" };
  } else if (aksaraClassifyClass === 26) {
    result = { result: "Gantungan Ta" };
  } else if (aksaraClassifyClass === 27) {
    result = { result: "Gantungan Wa" };
  } else if (aksaraClassifyClass === 28) {
    result = { result: "Gantungan Ya" };
  } else if (aksaraClassifyClass === 29) {
    result = { result: "Ha" };
  } else if (aksaraClassifyClass === 30) {
    result = { result: "Ja" };
  } else if (aksaraClassifyClass === 31) {
    result = { result: "Ka" };
  } else if (aksaraClassifyClass === 32) {
    result = { result: "La" };
  } else if (aksaraClassifyClass === 33) {
    result = { result: "Lima" };
  } else if (aksaraClassifyClass === 34) {
    result = { result: "Ma" };
  } else if (aksaraClassifyClass === 35) {
    result = { result: "Na" };
  } else if (aksaraClassifyClass === 36) {
    result = { result: "Nga" };
  } else if (aksaraClassifyClass === 37) {
    result = { result: "Nya" };
  } else if (aksaraClassifyClass === 38) {
    result = { result: "Pa" };
  } else if (aksaraClassifyClass === 39) {
    result = { result: "Pepet" };
  } else if (aksaraClassifyClass === 40) {
    result = { result: "Ra" };
  } else if (aksaraClassifyClass === 41) {
    result = { result: "Sa" };
  } else if (aksaraClassifyClass === 42) {
    result = { result: "Satu" };
  } else if (aksaraClassifyClass === 43) {
    result = { result: "Sembilan" };
  } else if (aksaraClassifyClass === 44) {
    result = { result: "Suku" };
  } else if (aksaraClassifyClass === 45) {
    result = { result: "Surang" };
  } else if (aksaraClassifyClass === 46) {
    result = { result: "Ta" };
  } else if (aksaraClassifyClass === 47) {
    result = { result: "Taleng" };
  } else if (aksaraClassifyClass === 48) {
    result = { result: "Taleng Tedong" };
  } else if (aksaraClassifyClass === 49) {
    result = { result: "Tedong" };
  } else if (aksaraClassifyClass === 50) {
    result = { result: "Tiga" };
  } else if (aksaraClassifyClass === 51) {
    result = { result: "Tujuh" };
  } else if (aksaraClassifyClass === 52) {
    result = { result: "Ulu" };
  } else if (aksaraClassifyClass === 53) {
    result = { result: "Wa" };
  } else if (aksaraClassifyClass === 54) {
    result = { result: "Ya" };
  } else {
    result = { result: "Missing aksara character" };
  }
  return result;
}

export default {
  PORT: APP_PORT,
  gmailEmail: APP_GMAIL_EMAIL,
  gmailPassword: APP_GMAIL_PASSWORD,
  firebaseFirestore,
  firebaseAuth,
  bucketStorage,

  modelUrl: APP_MODEL_URL,
  aksaraClassify,
  aksaraClassifyClass,
};
