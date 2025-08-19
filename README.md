# 🌍 Travelo

<div align="center">
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" />
  <img src="https://img.shields.io/badge/Material%20Design-757575?style=for-the-badge&logo=material-design&logoColor=white" />
</div>

<div align="center">
  <h3>🧳 Travel & Code! A modern travel guide app built to inspire wanderlust and showcase clean Android development</h3>
  <p><em>Discover amazing destinations • Learn modern Android development • Explore the world</em></p>
</div>

---

## ✨ About

**Travelo** is more than just a travel app—it's a learning playground for modern Android development! Built with the latest technologies and best practices, this app demonstrates how to create beautiful, performant, and maintainable Android applications while helping users discover incredible travel destinations worldwide.

Whether you're a developer looking to learn clean architecture patterns or a traveler seeking your next adventure, this app has something for you!

## 🎨 Design Inspiration

The UI is inspired by the gorgeous **"Aspen Travel App Exploration- Mobile App Design"** by [Nickelfox Design](https://www.figma.com/community/file/1091615514005406765) that showcases modern travel app design patterns with:
- Clean, minimalist interface
- Beautiful imagery and typography
- Intuitive navigation patterns
- Mobile-first responsive design

## 🏗️ Architecture

Built following **Clean Architecture** principles with **MVVM** pattern:

```
📱 Presentation Layer
├── 🎨 UI Components (Jetpack Compose)
├── 📄 Screens & Navigation  
└── 🧠 ViewModels & State Management

💼 Domain Layer
├── 📋 Repository Interfaces
└── 🏷️ Domain Models

💾 Data Layer
├── 🌐 Remote Data Source (TripAdvisor API)
├── 💿 Local Data Source (Room Database)
└── 🗄️ Repository Implementation
```

## 🚀 Features

### 🔍 **Smart Discovery**
- Browse destinations by categories
- Location-based recommendations
- Popular and trending spots
- Advanced search functionality

### 🗺️ **Rich Location Data**
- Detailed destination information
- High-quality photos and galleries
- Reviews and ratings
- Geographic coordinates and maps

### 📱 **Modern UI/UX**
- Smooth animations and transitions
- Responsive layouts for all screen sizes

### ⚡ **Performance & Reliability**
- Efficient image loading and caching
- Error handling with retry mechanisms
- Optimized for battery and data usage

### 🔜 **Coming Soon**
- Offline-first architecture
- Comprehensive unit testing
- Dark/Light theme support

## 🛠️ Tech Stack

### **Core Framework**
- **Kotlin** - Modern, concise, and safe programming language
- **Jetpack Compose** - Declarative UI toolkit for native Android
- **Material 3** - Latest Material Design components

### **Architecture Components**
- **MVVM Pattern** - Separation of concerns and testability
- **Clean Architecture** - Maintainable and scalable code structure
- **Dagger Hilt** - Dependency injection for cleaner code
- **Navigation Compose** - Type-safe navigation between screens

### **Data & Networking**
- **Retrofit** - Type-safe HTTP client for API communication
- **Room Database** - Local data persistence and caching
- **DataStore** - Modern data storage solution for preferences
- **Gson** - JSON serialization and deserialization

### **UI & Media**
- **Coil** - Fast and lightweight image loading library
- **Material Icons Extended** - Comprehensive icon library
- **Custom Animations** - Smooth and delightful user interactions

### **Development Tools**
- **KSP (Kotlin Symbol Processing)** - Faster annotation processing
- **Gradle Version Catalogs** - Centralized dependency management

## 🌐 Data Source

Powered by the **TripAdvisor API**, providing access to:
- 🏨 Hotels and accommodations
- 🍽️ Restaurants and dining
- 🎯 Attractions and activities  
- 📍 Location details and reviews
- 📊 Real-time ratings and feedback

## 📱 Screenshots

*Coming soon! We're putting the finishing touches on the UI.*

## 🚀 Getting Started

### Prerequisites
- **Android Studio** Hedgehog | 2023.1.1 or newer
- **JDK 17** or higher
- **Android SDK** with API level 24+
- **TripAdvisor API Key** (see setup instructions below)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Farajialireza82/VacationApp.git
   cd VacationApp
   ```

2. **Get your TripAdvisor API key**
   - Visit [TripAdvisor Developer Portal](https://www.tripadvisor.com/developers)
   - Create an account and generate an API key
   - Add your API key to `local.properties`:
   ```properties
   TRIPADVISOR_API_KEY=your_api_key_here
   ```
   - Update your `Constants.kt` file to read from BuildConfig:
   ```kotlin
   // In your app-level build.gradle.kts
   android {
       buildTypes {
           debug {
               buildConfigField("String", "TRIPADVISOR_API_KEY", "\"${project.findProperty("TRIPADVISOR_API_KEY") ?: ""}\"")
           }
       }
   }
   
   // In Constants.kt
   const val API_KEY = BuildConfig.TRIPADVISOR_API_KEY
   ```

3. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory and open it

4. **Build and Run**
   - Sync the project with Gradle files
   - Run the app on an emulator or physical device

## 🧪 Testing

Testing infrastructure is currently being developed:

```bash
# Unit tests (Coming Soon!)
./gradlew test

# Instrumented tests (Coming Soon!)
./gradlew connectedAndroidTest
```

## 📂 Project Structure

```
app/
├── src/main/kotlin/com/cromulent/vacationapp/
│   ├── data/           # Data layer implementation
│   │   ├── local/      # Room database, DAOs
│   │   ├── remote/     # API services, DTOs
│   │   └── repository/ # Repository implementations
│   ├── domain/         # Business logic layer
│   │   └── repository/ # Repository interfaces
│   ├── model/          # Data models and entities
│   ├── presentation/   # UI layer
│   │   ├── components/ # Reusable UI components
│   │   ├── home/       # Home screen and components
│   │   ├── navigation/ # Navigation setup
│   │   └── theme/      # App theming and styling
│   └── util/           # Utility classes and constants
```

## 🤝 Contributing

We love contributions! Whether you're fixing bugs, adding features, or improving documentation, your help makes this project better.

### How to Contribute

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add some amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

### Development Guidelines

- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Write unit tests for new features
- Update documentation as needed
- Use meaningful commit messages

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 🙏 Acknowledgments

- **[Nickelfox Design](https://www.figma.com/community/file/1091615514005406765)** - For the beautiful "Aspen Travel App Exploration" design inspiration
- **[TripAdvisor](https://www.tripadvisor.com/)** - For providing comprehensive travel data
- **[Android Developer Community](https://developer.android.com/)** - For excellent documentation and resources
- **[Jetpack Compose Community](https://developer.android.com/jetpack/compose)** - For pushing the boundaries of Android UI development

## 📞 Contact & Support

- **Issues**: [GitHub Issues](https://github.com/Farajialireza82/VacationApp/issues)
- **Email**: farajialireza82@gmail.com

---

<div align="center">
  <p><strong>Made with ❤️ for travelers and developers worldwide</strong></p>
  <p><em>Happy coding and safe travels! 🌍✈️</em></p>
</div>
