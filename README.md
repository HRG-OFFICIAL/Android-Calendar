# Modern Calendar App

A comprehensive, modern calendar application built with Kotlin and Jetpack Compose for Android. This app provides a clean, modular, and scalable solution for managing events, reminders, and calendar functionality.

## 🚀 Features

### Core Features
- **Event Management**: Create, edit, delete events with full CRUD operations
- **Recurring Events**: Support for daily, weekly, monthly, and yearly recurrence patterns
- **Multiple Calendar Views**: Day, week, month, and year views
- **Material 3 Design**: Modern UI with dynamic theming and Material You support
- **Offline-First**: Local database with Room for reliable data storage
- **Real-time Updates**: Live data updates with StateFlow and Compose

### Advanced Features
- **Multi-Calendar Support**: Personal, work, and shared calendars
- **Event Reminders**: Customizable reminder notifications
- **Search & Filter**: Find events quickly with search functionality
- **Event Sharing**: Share events via deep links
- **Cloud Sync**: Firebase integration for cross-device synchronization
- **Accessibility**: Full accessibility support with TalkBack
- **Animations**: Smooth Lottie animations and transitions

## 🏗️ Architecture

The app follows **Clean Architecture** principles with **MVVM** pattern:

```
📁 app/                    # Main application module
📁 core/                   # Core modules
  ├── common/              # Common utilities and data classes
  ├── ui/                  # UI components and theming
  ├── data/                # Data layer (Room, repositories)
  ├── domain/              # Business logic and use cases
  └── network/             # Network layer (Retrofit, APIs)
📁 feature/                # Feature modules
  ├── calendar/            # Calendar UI and logic
  ├── events/              # Event management
  ├── settings/            # Settings and preferences
  ├── auth/                # Authentication
  └── sync/                # Cloud synchronization
```

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose with Material 3
- **Architecture**: MVVM + Clean Architecture
- **Dependency Injection**: Hilt
- **Database**: Room with SQLite
- **Networking**: Retrofit + OkHttp
- **Calendar Library**: [kizitonwose/Calendar](https://github.com/kizitonwose/Calendar)
- **Firebase**: Authentication, Firestore, Analytics, Crashlytics
- **Animations**: Lottie
- **Image Loading**: Coil
- **Testing**: JUnit, MockK, Turbine, Espresso

## 📱 Screenshots

*Screenshots will be added once the app is fully implemented*

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- JDK 11 or later
- Android SDK 26 or later
- Kotlin 2.0.21 or later

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/modern-calendar.git
   cd modern-calendar
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory and select it

3. **Sync Project**
   - Android Studio will automatically sync the project
   - Wait for the sync to complete

4. **Run the App**
   - Connect an Android device or start an emulator
   - Click the "Run" button or press `Shift + F10`

### Firebase Setup (Optional)

To enable cloud sync and authentication:

1. **Create a Firebase Project**
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Create a new project

2. **Add Android App**
   - Click "Add app" and select Android
   - Enter package name: `com.moderncalendar`
   - Download `google-services.json`
   - Place it in `app/` directory

3. **Enable Services**
   - Authentication (Email/Password, Google Sign-In)
   - Firestore Database
   - Analytics
   - Crashlytics

## 📚 Usage

### Creating Events

1. Open the app
2. Navigate to the calendar view
3. Tap on a date to select it
4. Tap the "+" button to create a new event
5. Fill in event details:
   - Title (required)
   - Description (optional)
   - Location (optional)
   - Date and time
   - Color
   - Recurrence pattern (optional)
6. Tap "Create Event"

### Managing Events

- **View Events**: Tap on any date to see events for that day
- **Edit Events**: Tap on an event to edit its details
- **Delete Events**: Long press on an event and select delete
- **Search Events**: Use the search bar to find specific events

### Calendar Views

- **Month View**: Default view showing the entire month
- **Week View**: Focused view of a single week
- **Day View**: Detailed view of a single day
- **Year View**: Overview of the entire year

## 🧪 Testing

The app includes comprehensive testing:

### Unit Tests
```bash
./gradlew test
```

### Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

### Test Coverage
```bash
./gradlew jacocoTestReport
```

## 🔧 Configuration

### Build Variants

- **Debug**: Development build with logging enabled
- **Release**: Production build with optimizations

### ProGuard

Release builds use ProGuard for code obfuscation and optimization. Rules are defined in `proguard-rules.pro`.

## 📦 Dependencies

### Core Dependencies
- **Jetpack Compose**: Modern UI toolkit
- **Room**: Local database
- **Hilt**: Dependency injection
- **Navigation**: Navigation between screens
- **WorkManager**: Background tasks
- **DataStore**: Preferences storage

### UI Dependencies
- **Material 3**: Design system
- **Lottie**: Animations
- **Coil**: Image loading
- **Calendar Library**: Calendar components

### Network Dependencies
- **Retrofit**: HTTP client
- **OkHttp**: Network interceptor
- **Moshi**: JSON serialization

### Firebase Dependencies
- **Firebase Auth**: Authentication
- **Firestore**: Cloud database
- **Analytics**: Usage analytics
- **Crashlytics**: Crash reporting

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Style

The project uses:
- **Kotlin Coding Conventions**
- **ktlint** for code formatting
- **detekt** for static analysis

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [kizitonwose/Calendar](https://github.com/kizitonwose/Calendar) - Calendar library
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - UI toolkit
- [Material Design](https://material.io/) - Design system
- [Firebase](https://firebase.google.com/) - Backend services

## 📞 Support

If you have any questions or need help, please:

1. Check the [Issues](https://github.com/yourusername/modern-calendar/issues) page
2. Create a new issue if your problem isn't already reported
3. Contact the maintainers

## 🗺️ Roadmap

### Version 1.0 (Current) ✅
- [x] Basic calendar functionality
- [x] Event creation and management
- [x] Material 3 theming
- [x] Offline storage
- [x] Event reminders and notifications
- [x] Cloud synchronization
- [x] User authentication
- [x] Search functionality
- [x] Settings and preferences
- [x] Event details and editing

### Version 1.1 (Planned)
- [ ] Event sharing via deep links
- [ ] Calendar import/export
- [ ] Advanced recurring patterns
- [ ] Multi-calendar management

### Version 1.2 (Future)
- [ ] Widget support
- [ ] Wear OS companion app
- [ ] Advanced analytics
- [ ] Team collaboration features

---

**Made with ❤️ using Kotlin and Jetpack Compose**
