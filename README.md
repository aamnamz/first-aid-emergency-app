# 🚑 First Aid Emergency App

A multilingual emergency assistance Android application with voice recognition and emergency services integration, developed for Mobile Application Development (MAD) course.

## 📱 Features
- **Multilingual Voice Recognition**: Urdu and English voice input for symptoms
- **Emergency Services Integration**: One-touch dialing to 1122, local hospitals, ambulance
- **GPS-based Hospital Locator**: Find nearest medical facilities
- **Searchable First-Aid Database**: Categorized medical guidance
- **Dual-Language UI**: Full Urdu/English interface with RTL support
- **Accessibility Features**: Color-coded urgency levels, large buttons, voice feedback

## 🏥 Use Cases
1. **Emergency Situations**: Quick access to emergency numbers
2. **Symptom Guidance**: Voice/search-based first-aid instructions
3. **Hospital Navigation**: GPS directions to nearest medical help
4. **Multilingual Support**: Accessible to Urdu-speaking population

## 🛠️ Technical Implementation

### **Core Components**
- **Voice Recognition**: Android Speech-to-Text API with Urdu language pack
- **Emergency Dialer**: `Intent.ACTION_CALL` with emergency number integration
- **Location Services**: FusedLocationProvider for GPS coordinates
- **Database**: SQLite with first-aid information in both languages
- **UI**: ConstraintLayout with RTL support for Urdu

### **Permissions Required**
```xml
<uses-permission android:name="android.permission.CALL_PHONE" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />

🚀 Installation & Setup
Prerequisites
Android Studio Arctic Fox or later

Android SDK 30+

Minimum API Level: 21 (Android 5.0)

Build Instructions
bash
# Clone repository
git clone https://github.com/YOURUSERNAME/first-aid-emergency-app.git

# Open in Android Studio
# Build → Make Project
# Run on device/emulator
APK Generation
bash
# Generate debug APK
./gradlew assembleDebug

# APK location: app/build/outputs/apk/debug/
<uses-permission android:name="android.permission.INTERNET" />

🔧 Technologies Used
Language: Java (Android)

IDE: Android Studio

Database: SQLite

APIs: Google Maps, Android Speech-to-Text

Version Control: Git/GitHub

🤝 Contributing
Fork the repository

Create a feature branch (git checkout -b feature/improvement)

Commit changes (git commit -m 'Add some feature')

Push to branch (git push origin feature/improvement)

Open a Pull Request

📄 License
This project is licensed under the MIT License - see the LICENSE file for details.

👨‍💻 Developer
Amna Mumtaz - Software Engineering Student @ IIUI
