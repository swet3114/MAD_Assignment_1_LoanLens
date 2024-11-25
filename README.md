# LoanLens: Loan Eligibility Prediction App
## 1.Introduction
LoanLens is a user-friendly mobile application designed to assist users in determining their loan eligibility. By analyzing user-provided financial and personal data, the app predicts whether a user qualifies for a loan. The backend uses a machine learning model implemented in Python, connected to the mobile frontend via a Flask API. Firebase Authentication ensures secure login, making the application reliable and efficient for financial decision-making.

## 2. Key Features
  Accurate Predictions: Leverages machine learning to analyze data and predict loan eligibility.
  Secure Login: Uses Firebase Authentication for safe and seamless user login.
  Mobile Integration: A user-friendly mobile app interface developed in Android Studio.
  Real-Time API Communication: Backend API hosted on Render for fast predictions.
## 3. Technology Stack
Backend
Programming Language: Python
Framework: Flask
Machine Learning Library: Scikit-learn
Hosting Platform: Render
Frontend
Development Environment: Android Studio
Programming Languages: Java/Kotlin
Authentication: Firebase

## 4. Project Workflow
Backend (Python + Flask)
Data Preparation:

Historical loan data is cleaned and preprocessed.
Machine learning algorithms (e.g., Random Forest) are used to train the model.
The model is saved using pickle for reuse.
API Development:

A Flask API with a /predict endpoint processes user data and generates predictions.
JSON responses include prediction results and confidence scores.
Frontend (Android Studio)
User Login:
Firebase Authentication enables secure login using email/password or Google sign-in.
User Input:
Users enter details like age, income, loan amount, and credit score through the app's interface.
API Integration:
The app sends user data to the Flask API, retrieves predictions, and displays the results.

## 5. Setup Instructions
Backend Setup
Clone the repository:

git clone [repository-link]
cd backend
Install dependencies:
pip install -r requirements.txt
Run the Flask server:
bash

app.py
The API will be accessible locally at http://localhost:5000.
Frontend Setup
Open the Android Studio project in the frontend folder.
Connect the project to Firebase for authentication.
Follow Firebase Authentication setup.
Build and run the app on an emulator or physical device.
Test the /predict API endpoint by entering user data in the app interface.

## 6. API Details
Endpoint: /predict
Method: POST
Input: JSON payload containing user data fields:

### Example Response:
{
    "status": "Approved",
    "confidence": 0.92
}

## 7. Results
The LoanLens app provides accurate predictions with a confidence score, offering users insights into their loan eligibility. Firebase Authentication ensures secure access, while the machine learning model enables robust and efficient decision-making.

## 8. Authors
Swet Gajjar


