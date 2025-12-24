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
<uses-permission android:name="android.permission.INTERNET" />
