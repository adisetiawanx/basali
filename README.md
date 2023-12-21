# basali api
This is the Bangkit Academy 2023 capstone project

## Requirements
- Node 16
- NPM
- Node gyp
- Python
- Git
- Google cloud project
- Firebase

## Common setup
Step 1: Clone the repository
```bash
git clone -b cc/master https://github.com/adisetiawanx/basali.git
```

Step 2: Install requirment packages (Windows)
- Download and install python (https://www.python.org/downloads/windows/)
- Install node gyp
```bash
npm -g node-gyp
```
- Install Visual C++ Build Environment (https://visualstudio.microsoft.com/thank-you-downloading-visual-studio/?sku=BuildTools)
- Install project requirment packages
```bash
cd basali
npm install
```

Step 3: Set up firebase and google cloud project
You have to create a new project in Google Cloud and set up Firebase, enable Firebase Auth then allow auth via email/password and Google. After that, activate Firebase Firestore.
After that, create a Google Cloud bucket and allow public access to the bucket

Step 4: Setting env variables and config
- setting the env variables
```bash
#app config
APP_PORT= the port where the application will run

#gmail service acc
APP_GMAIL_EMAIL= your gmail email service
APP_GMAIL_PASSWORD= your gmail password service

#gcloud project id
APP_PROJECT_ID= your gcloud project id
APP_BUCKET_NAME= your gcloud bucket name

#basali model
APP_MODEL_URL= your model url stored on gcloud bucket
```
- setting the serviceAccountKey.json
You must create a service account in the Google Cloud project and allow Firebase admin and storage admin access. Then copy the .json file to /src/serviceAccountKey.json (rename)
```bash
basali/src/serviceAccountKey.json
```

Step 5: Run the project
You can run it using
```bash
npm start
```
or (for development)
```bash
npm run dev
```
