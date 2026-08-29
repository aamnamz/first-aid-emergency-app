# 🚑 First Aid Emergency App

### Multilingual First Aid & Emergency Assistance for Android

A multilingual Android application developed for the **Mobile Application Development (MAD)** course, designed to provide accessible first-aid guidance and quick access to emergency services through **Urdu and English voice recognition, GPS-based hospital discovery, and emergency calling**.

## 📱 Features

* 🗣️ **Multilingual Voice Recognition** — Accepts Urdu and English voice input for symptom searches.
* 🚨 **Emergency Services** — One-touch access to emergency services, ambulances, and hospitals.
* 🔎 **First-Aid Database** — Searchable and categorized first-aid guidance.
* ♿ **Accessibility Features** — Large controls, color-coded urgency levels, and voice feedback.

## 🏥 Use Cases

* **Emergency Situations** — Quickly access emergency numbers and medical facilities.
* **Symptom Guidance** — Search or use voice input to find relevant first-aid information.

## 🛠️ Technical Implementation

### Core Components

* **Voice Recognition** — Android Speech-to-Text API with Urdu and English support.
* **Emergency Dialer** — `Intent.ACTION_CALL` for emergency number integration.
* **Database** — SQLite database containing bilingual first-aid information.
* **UI** — Android ConstraintLayout with Urdu RTL support.

### Required Permissions

```xml
<uses-permission android:name="android.permission.CALL_PHONE" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.INTERNET" />
```

## 🛠️ Technologies

| Category          | Technology                           |
| ----------------- | ------------------------------------ |
| Language          | Java                                 |
| Platform          | Android                              |
| IDE               | Android Studio                       |
| Database          | SQLite                               |
| Voice Recognition | Android Speech-to-Text               |
| UI                | ConstraintLayout, RTL                |
| Version Control   | Git / GitHub                         |

## 🚀 Installation & Setup

### Prerequisites

* Android Studio Arctic Fox or later
* Android SDK 30+
* Minimum API Level 21 (Android 5.0)

### Build Instructions

Clone the repository:

```bash
git clone https://github.com/YOURUSERNAME/first-aid-emergency-app.git
```

Open the project in **Android Studio**, allow Gradle to sync, then select:

**Build → Make Project**

Connect an Android device or start an emulator and run the application.

### Generate APK

```bash
./gradlew assembleDebug
```

The generated APK will be available at:

```text
app/build/outputs/apk/debug/
```

## 🎯 Project Purpose

The application was developed to explore **mobile application development, location-based services, voice interfaces, multilingual UI design, local data storage, and Android system integrations** while addressing accessibility in emergency situations.

## 📌 Project Status

**Completed — developed as a Mobile Application Development course project.**

## 👩‍💻 Developer

**Amna Mumtaz**

[GitHub](https://github.com/aamnamz) · [LinkedIn](https://www.linkedin.com/in/amnaamumtaz/)

> ⚠️ **Disclaimer:** This application was developed for educational purposes and is not a substitute for professional medical advice or emergency services. In a real emergency, contact qualified medical professionals immediately.
