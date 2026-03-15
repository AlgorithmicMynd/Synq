# SynQ - Real-Time Chat Application

SynQ is a streamlined Android chat application built with Java and Firebase. It focuses on core real-time communication between users with a secure authentication system.
the Project is created to implement the learnings of android development and object oriented programming through java.

## 🚀 Features

- **User Authentication:** Secure signup and login using Firebase Authentication with SHA-256 password hashing.
- **Real-Time Messaging:** Instant message exchange between users powered by Firebase Realtime Database.
- **Profile Viewing:** A dedicated profile section where users can view their current account details and profile picture.
- **One-Time Registration Setup:** Users set their username, email, and profile picture (via ImgBB) during registration.
- **Modern UI:** Responsive design utilizing the SDP/SSP libraries to ensure a consistent experience across different device sizes.

## 🛠️ Technical Stack

- **Language:** Java
- **Backend:** Firebase (Realtime Database, Authentication)
- **Image Storage:** ImgBB API (Used during registration)
- **Libraries:**
    - **Glide:** For efficient image loading and caching.
    - **CircleImageView:** For circular profile image displays.
    - **OkHttp:** For networking and ImgBB API integration.
    - **SDP/SSP:** For scalable and responsive UI layouts.
    - **Material Design Components:** For a clean and professional user interface.

## 📁 Project Structure

- `registration.java`: Handles user account creation, password hashing, and initial profile image upload to ImgBB.
- `login.java`: Manages user login and session authentication.
- `MainActivity.java`: The primary dashboard showing the list of available users for chatting.
- `chat_Win.java`: The main messaging interface for real-time conversation.
- `ActivityProfile.java`: A view-only section to display the currently logged-in user's profile details.
- `Users.java` & `msgModelclass.java`: Data models representing users and chat messages.
- `UserAdapter.java` & `messageAdpter.java`: Adapters to handle data binding for user lists and message bubbles.
- `PassEncrypt.java`: A utility class for SHA-256 password encryption.

## 🔧 Setup & Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/AlgorithmicMynd/SynQ.git
   ```
2. **Firebase Configuration:**
    - Create a project in the [Firebase Console](https://console.firebase.google.com/).
    - Register the app with package name `com.example.synq`.
    - Place the `google-services.json` file in the `app/` directory.
    - Enable **Email/Password Authentication** and **Realtime Database**.
3. **ImgBB API Setup:**
    - Obtain an API key from [ImgBB](https://api.imgbb.com/).
    - Configure the `IMG_BB_API_KEY` in `registration.java`.
4. **Build:**
    - Sync the project with Gradle files in Android Studio and run.

