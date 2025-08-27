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
├── 🔄 Use Cases
├── 📋 Repository Interfaces
└── 🏷️ Domain Models

💾 Data Layer
├── 🌐 Remote Data Sources (TripAdvisor API + OpenWeatherMap API)
├── 💿 Local Data Source (Room Database)
└── 🗄️ Repository Implementation
```

## 🚀 Features

### 🔍 **Smart Discovery**
- Browse destinations by categories
- Location-based recommendations with GPS integration
- Popular and trending spots
- Advanced search functionality with category filtering

### 📍 **Intelligent Location Services**
- **GPS Location Detection** - Automatically find your current location
- **City Search & Selection** - Search for any city worldwide
- **Geocoding & Reverse Geocoding** - Convert between addresses and coordinates
- **Weather Integration** - Get current weather for any location

### 🗺️ **Rich Location Data**
- Detailed destination information
- High-quality photos and galleries
- Reviews and ratings
- Geographic coordinates and maps

### 📱 **Modern UI/UX**
- Material 3 Design System
- Smooth animations and transitions
- Dark/Light theme support
- Responsive layouts for all screen sizes
- **Native Splash Screen** with proper branding

### ⚡ **Performance & Reliability**
- **Offline-First Architecture** - Works seamlessly without internet
- Efficient image loading and caching
- Error handling with retry mechanisms
- Optimized for battery and data usage

### 🔜 **Coming Soon**
- Comprehensive unit testing

## 🛠️ Tech Stack

### **Core Framework**
- **Kotlin** - Modern, concise, and safe programming language
- **Jetpack Compose** - Declarative UI toolkit for native Android
- **Material 3** - Latest Material Design components

### **Architecture Components**
- **MVVM Pattern** - Separation of concerns and testability
- **Clean Architecture** - Maintainable and scalable code structure
- **Offline-First Architecture** - Seamless user experience without connectivity
- **Dagger Hilt** - Dependency injection for cleaner code
- **Navigation Compose** - Type-safe navigation between screens

### **Data & Networking**
- **Retrofit** - Type-safe HTTP client for API communication
- **Room Database** - Local data persistence and caching
- **DataStore Preferences** - Modern data storage solution for user preferences
- **Gson** - JSON serialization and deserialization
- **OkHttp Logging Interceptor** - Network request/response logging

### **Location & Services**
- **Google Play Location Services** - Accurate GPS positioning
- **Core SplashScreen** - Native Android 12+ splash screen implementation

### **UI & Media**
- **Coil Compose** - Fast and lightweight image loading library
- **Material Icons Extended** - Comprehensive icon library
- **Custom Animations** - Smooth and delightful user interactions

### **Development Tools**
- **KSP (Kotlin Symbol Processing)** - Faster annotation processing
- **Gradle Version Catalogs** - Centralized dependency management

## 🌐 Data Sources

### **TripAdvisor API**
Provides comprehensive travel data including:
- 🏨 Hotels and accommodations
- 🍽️ Restaurants and dining
- 🎯 Attractions and activities  
- 📍 Location details and reviews
- 📊 Real-time ratings and feedback

### **OpenWeatherMap API** ⭐ 
A fantastic **completely free** weather service (unlike TripAdvisor's paid tiers!) providing:
- 🌤️ Current weather conditions
- 🌍 City and country search with geocoding
- 🗺️ Reverse geocoding for coordinates
- 📍 Location-based weather data
- 🆓 **100% Free** - No hidden costs or usage limits for basic features!

*Special thanks to OpenWeatherMap for providing an excellent free service that makes location-based features accessible to all developers!*

## 🎥 App Demo

<div align="center">
  <video width="300" controls>
    <source src="https://raw.githubusercontent.com/Farajialireza82/VacationApp/screenshots/screenrecord.mp4" type="video/mp4">
    Your browser does not support the video tag.
  </video>
    <em>See Travelo in action: location discovery, search, and detailed views</em>
</div>

## 📱 Screenshots

<div align="center">
  
  **Core Features**
  
  <img src="https://raw.githubusercontent.com/Farajialireza82/VacationApp/main/screenshots/HomeScreen-Nature.jpg" width="250" alt="Home Screen - Nature Category" />
  <img src="https://raw.githubusercontent.com/Farajialireza82/VacationApp/main/screenshots/DetailScreen.jpg" width="250" alt="Location Detail Screen" />
  
  <br/>
  
  **Location & Search**
  
  <img src="https://raw.githubusercontent.com/Farajialireza82/VacationApp/main/screenshots/LocationScreen.jpg" width="250" alt="Location Selection Screen" />
  <img src="https://raw.githubusercontent.com/Farajialireza82/VacationApp/main/screenshots/SearchScreen.jpg" width="250" alt="Search Screen" />
  
  <br/>
  
  **App States**
  
  <img src="https://raw.githubusercontent.com/Farajialireza82/VacationApp/main/screenshots/HomeScreen-EmptyState.jpg" width="250" alt="Home Screen Empty State" />
  <img src="https://raw.githubusercontent.com/Farajialireza82/VacationApp/main/screenshots/SearchScreen-InitialState.jpg" width="250" alt="Search Screen Initial State" />
  
</div>

<div align="center">
  <em>
  <strong>Top row:</strong> Home screen with Nature category & detailed location view<br/>
  <strong>Middle row:</strong> Smart location selection with GPS & advanced search with filtering<br/>
  <strong>Bottom row:</strong> Empty state handling & clean initial search interface
  </em>
</div>

## 🚀 Getting Started

### Prerequisites
- **Android Studio** Hedgehog | 2023.1.1 or newer
- **JDK 21** or higher
- **Android SDK** with API level 24+
- **TripAdvisor API Key** (see setup instructions below)
- **OpenWeatherMap API Key** (see setup instructions below)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Farajialireza82/VacationApp.git
   cd VacationApp
   ```

2. **Get your TripAdvisor API key**
   - Visit [TripAdvisor Developer Portal](https://www.tripadvisor.com/developers)
   - Create an account and generate an API key

3. **Get your OpenWeatherMap API key**
   - Visit [OpenWeatherMap API](https://openweathermap.org/api)
   - Create a free account and generate an API key
   - Enjoy their completely free tier! 🎉

4. **Configure API keys**
   - Add your API keys to `gradle.properties`:
   ```properties
   TRIP_ADVISOR_API_KEY=your_trip_advisor_key_here
   OPEN_WEATHER_MAP_API_KEY=your_open_weather_map_key_here
   ```

5. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory and open it

6. **Build and Run**
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
│   │   ├── location/   # Location selector screen
│   │   ├── search/     # Search screen with filtering
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
- **[OpenWeatherMap](https://openweathermap.org/)** - For their amazing **completely free** weather and geocoding services! 🌟
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
