<p align="center">
  <img src="mongez_icon.svg" alt="Mongez Logo" width="120" height="120" />
</p>

<h1 align="center">Mongez — AI Smart Study Planner</h1>

<p align="center">
  <strong>Your AI-powered academic companion for smarter, stress-free studying.</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white" alt="Platform" />
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?logo=kotlin&logoColor=white" alt="Language" />
  <img src="https://img.shields.io/badge/UI-Jetpack_Compose-4285F4?logo=jetpackcompose&logoColor=white" alt="UI" />
  <img src="https://img.shields.io/badge/Architecture-MVI_+_Clean-blueviolet" alt="Architecture" />
  <img src="https://img.shields.io/badge/Min_SDK-26-brightgreen" alt="Min SDK" />
  <img src="https://img.shields.io/badge/Target_SDK-37-blue" alt="Target SDK" />
  <img src="https://img.shields.io/badge/Version-1.0.0-orange" alt="Version" />
</p>

---

## 📖 About

**Mongez** (Arabic: مُنْجَز — *"Accomplished"*) is a modern Android application that reimagines how students plan, track, and conquer their academic workload. Powered by an **AI-driven roadmap engine** and a **contextual study assistant**, Mongez dynamically schedules your semester, adapts when life happens, and provides grounded explanations right inside your study room — so you stay focused and always prepared.

---

## ✨ Features at a Glance

| Feature | Description |
| :--- | :--- |
| 🔐 **Authentication** | Sign in with **Google One Tap** or register with email via **Firebase Auth**. Secure credential management with the Android Credentials API. |
| 📋 **Preferences (Onboarding)** | First-launch wizard to collect study preferences — study hours per day, preferred study times, academic level, and semester schedule — to personalize the AI roadmap. |
| 🏠 **Dashboard** | A command center displaying **upcoming tasks**, **current streak**, **total study hours**, **completed task count**, and quick access to today's schedule. |
| 📚 **Courses** | Add and manage courses with full details. Each course opens to reveal two tabs: **Materials** (uploaded resources) and **Tasks** (assignments, quizzes, exams). |
| 🗺️ **Roadmap** | An AI-generated, dynamic semester timeline created automatically when a new course is added. The **Dynamic Roadmap Engine** reactively reschedules remaining tasks when assessments are injected (e.g., pop quizzes). |
| 👤 **Profile** | View and edit your name, avatar, and study preferences. Displays accumulated stats — study hours, completed tasks, and current streak. |
| 🤖 **AI Study Room** | Activated when a user clicks on a task. An in-context **Contextual AI Assistant** provides pedagogical summaries and deep-dive Q&A grounded solely in the uploaded course materials. |

---

## 🖼️ Screenshots

<p align="center">
  <img src="docs/screenshots/Mongez%20Cover.png" alt="Mongez Cover" width="100%" />
</p>

### Authentication
<p align="center">
  <img src="docs/screenshots/Auth.png" alt="Auth Screens" width="100%" /><br />
  <sub><b>Register · Login · Google One Tap Sign-In</b></sub>
</p>

### Onboarding
<p align="center">
  <img src="docs/screenshots/Onboarding.png" alt="Onboarding Screens" width="100%" /><br />
  <sub><b>AI Study Introduction · Time Optimization · Personal AI Tutor</b></sub>
</p>

### Preferences
<p align="center">
  <img src="docs/screenshots/Preferences.png" alt="Preferences Screens" width="100%" /><br />
  <sub><b>Daily Study Hours · Available Days · Google Calendar Sync</b></sub>
</p>

### Profile
<p align="center">
  <img src="docs/screenshots/Profile.png" alt="Profile Screens" width="100%" /><br />
  <sub><b>Profile Overview · Edit Profile · Edit Preferences</b></sub>
</p>

### Roadmap
<p align="center">
  <img src="docs/screenshots/Roadmap.png" alt="Roadmap Screens" width="100%" /><br />
  <sub><b>Timeline · Filter · Add Event (Type · Details · Course)</b></sub>
</p>

### Courses, Tasks & AI Study Room
<p align="center">
  <img src="docs/screenshots/Tasks%20-%20Courses%20-%20Chat.png" alt="Courses, Tasks, and AI Chat" width="100%" /><br />
  <sub><b>My Courses · Tasks · AI Chat with Material Summaries & Q&A</b></sub>
</p>

---

## 🏗️ Architecture

Mongez follows **Multi-Module Clean Architecture** with the **MVI (Model-View-Intent)** pattern on the presentation layer. The project enforces strict layer separation — dependencies flow **inward** from outer layers (App → Presentation → Domain), while the Data layer implements Domain-defined interfaces.

### Module Dependency Graph

```
┌──────────────────────────────────────────────────────┐
│                      :app                            │
│   (Hilt DI init, Navigation Host, Splash Screen)     │
└────┬────────┬──────────┬──────────┬─────────┬────────┘
     │        │          │          │         │
     ▼        ▼          ▼          ▼         ▼
┌─────────┐ ┌──────┐ ┌──────────┐ ┌────┐ ┌──────────────┐
│:present-│ │:data │ │:navigat- │ │:do-│ │:design_system│
│ation    │ │      │ │ion       │ │main│ │              │
└────┬────┘ └──┬───┘ └──────────┘ └────┘ └──────────────┘
     │         │                    ▲           ▲
     │         └────────────────────┘           │
     └──────────────────────────────────────────┘
```

### Layer Responsibilities

| Module | Layer | Responsibility | Depends On |
| :--- | :--- | :--- | :--- |
| **`:app`** | Application | Hilt DI initialization, `NavHost` setup, splash screen, shared signing keystore | All modules |
| **`:presentation`** | Presentation | MVI contracts (`State`, `Intent`, `Effect`), ViewModels, UI Screens, UI Mappers | `:domain`, `:design_system` |
| **`:domain`** | Domain | Pure Kotlin business logic, Use Cases, Repository interfaces, `Result<T>` wrapper, custom exceptions | None *(zero Android dependencies)* |
| **`:data`** | Data | Repository implementations, Retrofit API services, DTOs, Mappers, `safeApi` network wrapper, Room DB, DataStore | `:domain` |
| **`:design_system`** | Design System | Reusable Compose components, design tokens (colors, typography, spacing, radius, elevation, motion), fonts, icons, illustrations | Compose Foundation only |
| **`:navigation`** | Navigation | Centralized route definitions and navigation graph contracts | — |
| **`build-logic`** | Build Infrastructure | Convention plugins (`mongez.android.application`, `mongez.compose`, `mongez.android.hilt`, etc.) for consistent Gradle configuration | Gradle API |

### MVI Flow (Presentation Layer)

```
User Action ──▶ Intent ──▶ ViewModel ──▶ Use Case (Domain) ──▶ Repository (Data)
                               │                                       │
                               ◀────── Result<T> ◀─────────────────────┘
                               │
                          ┌────┴────┐
                          ▼         ▼
                       State     Effect
                     (persist)  (one-shot)
                          │         │
                          ▼         ▼
                    UI Render   Navigation /
                               Snackbar /
                               Toast
```

### Domain Result Wrapper

```kotlin
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
```

---

## 📂 Project Structure

```
Mongez/
├── app/                          # Application entry point
│   ├── src/main/java/.../
│   │   ├── MongezApp.kt         # Hilt Application class
│   │   ├── MainActivity.kt      # Single Activity host
│   │   └── ui/theme/            # App-level theme bridge
│   └── google-services.json     # Firebase config
│
├── presentation/                 # Feature screens (MVI)
│   └── src/main/java/.../presentation/
│       ├── auth/                 # Login & Register
│       │   ├── login/            # contract/ uiState/ view/ viewmodel/
│       │   └── register/        # contract/ uiState/ view/ viewmodel/
│       ├── onboarding/          # First-launch preferences wizard
│       ├── preferences/         # Study preferences configuration
│       ├── dashboard/           # Home screen with upcoming tasks
│       ├── courses/             # Course list & add course
│       ├── coursedetails/       # Materials & Tasks tabs
│       ├── roadmap/             # AI-generated semester timeline
│       ├── profile/             # User profile & edit preferences
│       ├── studyroom/           # AI Chat on task click
│       ├── main/                # Main scaffold & bottom nav
│       └── utils/               # Presentation utilities
│
├── domain/                       # Pure Kotlin business core
│   └── src/main/kotlin/.../domain/
│       ├── auth/                 # model/ repository/ usecase/
│       ├── courses/             # model/ repository/ usecase/
│       ├── dashboard/           # model/ repository/ usecase/
│       ├── roadmap/             # model/ repository/ usecase/
│       ├── profile/             # model/ repository/ usecase/
│       ├── preferences/         # model/ repository/ usecase/
│       ├── calendar/            # model/ repository/ usecase/
│       ├── onboarding/          # repository/ usecase/
│       ├── settings/            # model/ repository/ usecase/
│       ├── core/                # Result<T>, exceptions, base use cases
│       └── utils/               # Domain utilities
│
├── data/                         # Data access & networking
│   └── src/main/java/.../data/
│       ├── sources/
│       │   ├── remote/services/ # Retrofit API interfaces
│       │   └── local/           # Local data sources
│       ├── repositories/        # Repository implementations
│       │   ├── auth/            # Firebase + backend auth
│       │   ├── courses/         # Course CRUD operations
│       │   ├── dashboard/       # Dashboard data aggregation
│       │   ├── roadmap/         # Roadmap generation
│       │   ├── profile/         # Profile management
│       │   ├── preferences/     # DataStore preferences
│       │   ├── calendar/        # Calendar events
│       │   ├── onboarding/      # Onboarding state
│       │   └── settings/        # App settings
│       ├── dtos/                # Data Transfer Objects
│       ├── mapper/              # DTO → Domain model mappers
│       ├── local/               # Room DB (dao/ db/ entity/)
│       ├── di/                  # Hilt data modules
│       └── utils/network/       # safeApi, handleException
│
├── design_system/                # Visual identity & components
│   └── src/main/java/.../designsystem/
│       ├── theme/               # MongezTheme, CompositionLocals
│       ├── foundation/          # Design tokens
│       │   ├── color/           # Semantic color system
│       │   ├── typography/      # Poppins + IBM Plex Sans Arabic
│       │   ├── spacing/         # 8dp grid system
│       │   ├── radius/          # Corner radius scale
│       │   ├── elevation/       # Shadow levels
│       │   ├── motion/          # Animation durations & easings
│       │   └── modifier/        # Common modifier extensions
│       ├── components/          # Reusable App* composables
│       │   ├── button/          # AppButton variants
│       │   ├── textfield/       # AppTextField variants
│       │   ├── card/            # AppCard, CourseCard, TaskCard...
│       │   ├── navigation/      # AppNavigationBar
│       │   ├── appbar/          # AppTopAppBar
│       │   ├── dialog/          # AppDialog variants
│       │   ├── sheet/           # AppBottomSheet
│       │   ├── chip/ tabs/ fab/ # Additional components
│       │   └── ...
│       ├── screens/             # Screen-specific composables
│       └── resources/           # Fonts, icons, illustrations
│
├── navigation/                   # Route definitions
│   └── src/main/java/.../navigation/
│
├── build-logic/                  # Gradle convention plugins
│   └── convention/src/.../
│       ├── AndroidApplicationConventionPlugin.kt
│       ├── AndroidLibraryConventionPlugin.kt
│       ├── AndroidLibraryComposeConventionPlugin.kt
│       ├── ComposeConventionPlugin.kt
│       ├── AndroidHiltConventionPlugin.kt
│       └── KotlinLibraryConventionPlugin.kt
│
├── docs/                         # Documentation
│   ├── ArchitectureLayers.md
│   ├── DesignSystem.md
│   ├── GitBranchNamingConventions.md
│   └── handshake_contract.md
│
├── keystores/                    # Shared debug signing keystore
├── gradle/libs.versions.toml    # Centralized version catalog
└── settings.gradle.kts          # Module inclusion
```

---

## 🔐 Authentication Flow

Mongez uses **Firebase Authentication** with **Google One Tap Sign-In** through the modern Android **Credentials API**.

```
┌──────────────┐      Firebase Token      ┌──────────────┐
│   Google      │ ──────────────────────▶ │   Firebase    │
│   One Tap     │                         │   Auth        │
└──────────────┘                          └──────┬───────┘
                                                 │ JWT
                                                 ▼
                                          ┌──────────────┐
                                          │   Backend     │
                                          │   REST API    │
                                          └──────┬───────┘
                                                 │
                                    ┌────────────┴────────────┐
                                    ▼                         ▼
                            POST /auth/register       POST /auth/login
                            (token + name)            (token only)
                                    │                         │
                                    ▼                         ▼
                            Returns user +            Returns user +
                            stats (zeroed)            stats (current)
```

**Registration** sends the Firebase JWT token alongside the user's name. The backend securely initializes all stats to zero.

**Login** sends only the token. The backend verifies it, looks up the user, and returns their accumulated progress (study hours, completed tasks, current streak).

---

## 📱 Screen-by-Screen Breakdown

### 1. Authentication Screen
- Google One Tap sign-in button
- Email/password registration form
- Smooth animated transitions between login and register modes
- Input validation with real-time error feedback
- Secure credential management via `androidx.credentials`

### 2. Preferences Screen (Onboarding)
- Multi-step wizard collecting study preferences
- Study hours per day, preferred study times, academic level
- Semester start/end dates configuration
- Data persisted via DataStore and synced to backend
- Only shown on first launch or when preferences are reset

### 3. Dashboard Screen
- **Greeting header** with user's name and avatar
- **Stats cards**: Total Study Hours, Completed Tasks, Current Streak
- **Upcoming Tasks** list sorted by due date with priority indicators
- Quick-access to today's scheduled study sessions
- Pull-to-refresh for live data sync

### 4. Courses Screen
- Grid/list view of all enrolled courses
- **Floating Action Button** to add a new course
- Course cards showing name, progress percentage, and task count
- Search and filter functionality
- Clicking a course navigates to Course Details

### 5. Course Details Screen
- **Two-tab layout**: **Materials** | **Tasks**
- **Materials Tab**: View and manage uploaded study resources
- **Tasks Tab**: List of assignments, quizzes, and exams with due dates and completion status
- Clicking a task launches the **AI Study Room**

### 6. Roadmap Screen
- **AI-generated semester timeline** created when a course is added
- Visual timeline showing task distribution across weeks
- **Dynamic Roadmap Engine** reactively reschedules when:
  - A new assessment is injected (e.g., surprise quiz)
  - Task priorities shift based on upcoming deadlines
- Color-coded by course and task type

### 7. Profile Screen
- View and edit display name and avatar
- Modify study preferences (re-opens preferences editor)
- View accumulated stats (study hours, completed tasks, streak)
- Account settings and sign-out

### 8. AI Study Room (Chat)
- Activated on task click from Course Details
- **Contextual AI Assistant** grounded in uploaded course materials
- Pedagogical summaries auto-generated for each topic
- Deep-dive Q&A for focused, in-context learning
- Disabled for custom/external courses to maintain content grounding

---

## 🧩 Dependencies

### Core Android & Kotlin

| Library | Version |
| :--- | :--- |
| ![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-7F52FF?logo=kotlin&logoColor=white) | `2.2.10` |
| ![AGP](https://img.shields.io/badge/Android_Gradle_Plugin-9.2.1-3DDC84?logo=android&logoColor=white) | `9.2.1` |
| ![Core KTX](https://img.shields.io/badge/AndroidX_Core_KTX-1.19.0-blue) | `1.19.0` |
| ![Lifecycle](https://img.shields.io/badge/Lifecycle_Runtime_KTX-2.11.0-blue) | `2.11.0` |
| ![ViewModel Compose](https://img.shields.io/badge/Lifecycle_ViewModel_Compose-2.11.0-blue) | `2.11.0` |
| ![KSP](https://img.shields.io/badge/KSP-2.2.10--2.0.2-7F52FF) | `2.2.10-2.0.2` |

### Jetpack Compose

| Library | Version |
| :--- | :--- |
| ![Compose BOM](https://img.shields.io/badge/Compose_BOM-2026.02.01-4285F4?logo=jetpackcompose&logoColor=white) | `2026.02.01` |
| ![Activity Compose](https://img.shields.io/badge/Activity_Compose-1.13.0-4285F4) | `1.13.0` |
| ![Material 3](https://img.shields.io/badge/Material_3-BOM_managed-4285F4) | *BOM managed* |
| ![Material Icons Extended](https://img.shields.io/badge/Material_Icons_Extended-BOM_managed-4285F4) | *BOM managed* |

### Navigation

| Library | Version |
| :--- | :--- |
| ![Navigation Compose](https://img.shields.io/badge/Navigation_Compose-2.8.5-blue) | `2.8.5` |
| ![Navigation3 Runtime](https://img.shields.io/badge/Navigation3_Runtime-1.1.3-blue) | `1.1.3` |
| ![Navigation3 UI](https://img.shields.io/badge/Navigation3_UI-1.1.3-blue) | `1.1.3` |

### Dependency Injection

| Library | Version |
| :--- | :--- |
| ![Hilt](https://img.shields.io/badge/Dagger_Hilt-2.60.1-orange?logo=google&logoColor=white) | `2.60.1` |
| ![Hilt Navigation Compose](https://img.shields.io/badge/Hilt_Navigation_Compose-1.2.0-orange) | `1.2.0` |
| ![Javax Inject](https://img.shields.io/badge/Javax_Inject-1-gray) | `1` |

### Networking & Serialization

| Library | Version |
| :--- | :--- |
| ![Retrofit](https://img.shields.io/badge/Retrofit-2.11.0-green?logo=square&logoColor=white) | `2.11.0` |
| ![Gson](https://img.shields.io/badge/Gson-2.10.1-green) | `2.10.1` |
| ![Retrofit Gson Converter](https://img.shields.io/badge/Retrofit_Gson_Converter-2.11.0-green) | `2.11.0` |

### Firebase & Authentication

| Library | Version |
| :--- | :--- |
| ![Firebase Auth](https://img.shields.io/badge/Firebase_Auth-23.0.0-FFCA28?logo=firebase&logoColor=white) | `23.0.0` |
| ![Firebase Messaging](https://img.shields.io/badge/Firebase_Messaging-24.0.0-FFCA28?logo=firebase&logoColor=white) | `24.0.0` |
| ![Credentials](https://img.shields.io/badge/Credentials_API-1.5.0--rc01-blue) | `1.5.0-rc01` |
| ![Credentials Play Services](https://img.shields.io/badge/Credentials_Play_Services-1.5.0--rc01-blue) | `1.5.0-rc01` |
| ![Google ID](https://img.shields.io/badge/Google_Identity-1.1.1-4285F4?logo=google&logoColor=white) | `1.1.1` |
| ![Google Services Plugin](https://img.shields.io/badge/Google_Services_Plugin-4.4.2-4285F4) | `4.4.2` |

### Async & Coroutines

| Library | Version |
| :--- | :--- |
| ![Coroutines Core](https://img.shields.io/badge/Coroutines_Core-1.10.1-7F52FF) | `1.10.1` |
| ![Coroutines Play Services](https://img.shields.io/badge/Coroutines_Play_Services-1.10.1-7F52FF) | `1.10.1` |

### Local Storage

| Library | Version |
| :--- | :--- |
| ![Room Runtime](https://img.shields.io/badge/Room_Runtime-2.6.1-blue) | `2.6.1` |
| ![Room KTX](https://img.shields.io/badge/Room_KTX-2.6.1-blue) | `2.6.1` |
| ![Room Compiler](https://img.shields.io/badge/Room_Compiler-2.6.1-blue) | `2.6.1` |
| ![DataStore Preferences](https://img.shields.io/badge/DataStore_Preferences-1.1.2-blue) | `1.1.2` |

### Image Loading

| Library | Version |
| :--- | :--- |
| ![Coil Compose](https://img.shields.io/badge/Coil_Compose-2.7.0-E91E63) | `2.7.0` |

### UI & Splash

| Library | Version |
| :--- | :--- |
| ![Splash Screen](https://img.shields.io/badge/Core_SplashScreen-1.0.1-blue) | `1.0.1` |

### Testing

| Library | Version |
| :--- | :--- |
| ![JUnit](https://img.shields.io/badge/JUnit-4.13.2-25A162) | `4.13.2` |
| ![AndroidX JUnit](https://img.shields.io/badge/AndroidX_JUnit-1.3.0-blue) | `1.3.0` |
| ![Espresso](https://img.shields.io/badge/Espresso_Core-3.7.0-blue) | `3.7.0` |
| ![Compose UI Test](https://img.shields.io/badge/Compose_UI_Test-BOM_managed-4285F4) | *BOM managed* |

---

## 🛠️ Build & Convention Plugins

Mongez uses a **`build-logic`** module with custom Gradle convention plugins to enforce consistent configuration across all modules:

| Plugin ID | Purpose |
| :--- | :--- |
| `mongez.android.application` | Android Application defaults (compileSdk 37, minSdk 26, targetSdk 37, JVM 21) |
| `mongez.android.library` | Android Library module configuration |
| `mongez.android.library.compose` | Library + Compose compiler setup |
| `mongez.compose` | Jetpack Compose compiler configuration |
| `mongez.android.hilt` | Dagger Hilt + KSP annotation processing |
| `mongez.kotlin.library` | Pure Kotlin library module (for `:domain`) |

---

## 🌍 Localization

Mongez supports **dual-language** UI:

- 🇺🇸 **English** — Poppins font family
- 🇸🇦 **Arabic** — IBM Plex Sans Arabic font family

Full **RTL (Right-to-Left)** support with `Start`/`End` alignment semantics throughout the design system.

---

## 🎨 Design System

The Design System is the **single source of truth** for the app's visual identity, built on **Material 3 Foundation** with a fully custom token layer.

- **Brand Color**: Purple `#5B4CF6` — representing intelligence, trust, and focus
- **8dp Grid Spacing System** for consistent layout rhythm
- **Semantic Color Scheme** with full Light/Dark theme support
- **Accessibility First**: WCAG AA contrast ratios (4.5:1), minimum 48dp touch targets, font scaling up to 2.0x
- **Component Library**: All reusable components prefixed with `App` — `AppButton`, `AppTextField`, `AppCard`, `AppDialog`, `AppBottomSheet`, `AppNavigationBar`, `AppTopAppBar`, and more

> 📄 Full specification: [`docs/DesignSystem.md`](docs/DesignSystem.md) (3,700+ lines)

---

## 🚀 Getting Started

### Prerequisites

- **Android Studio** Ladybug (2024.3+) or newer
- **JDK 21**
- **Android SDK** with compileSdk 37
- A Firebase project with `google-services.json`

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-org/Mongez.git
   cd Mongez
   ```

2. **Add Firebase configuration**
   Place your `google-services.json` in the `app/` directory.

3. **Configure local properties**
   Create or edit `local.properties` in the project root:
   ```properties
   GOOGLE_WEB_CLIENT_ID=your_google_web_client_id_here
   ```

4. **Build & Run**
   ```bash
   ./gradlew assembleDebug
   ```
   Or open the project in Android Studio and click ▶️ Run.

---

## 📐 Development Guidelines

### Branch Naming Convention
```
<type>/<description>       e.g. feature/user-auth
<type>/<ticket>-<desc>     e.g. feature/PROJ-123-user-auth
```
Types: `feature/` · `bugfix/` · `hotfix/` · `refactor/` · `docs/` · `test/` · `chore/`

### PR Naming Convention
```
<type>: <short description>          e.g. feat: add login screen
<type>(<ticket>): <short desc>       e.g. feat(PROJ-123): add user authentication
```

### Coding Standards
- ✅ Use modern Compose APIs (e.g., `HorizontalDivider`, not `Divider`)
- ✅ Place reusable UI components in `:design_system`, not in feature modules
- ✅ Use design tokens (`Theme.spacing.*`, `Theme.colorScheme.*`, `Theme.typography.*`) — no hardcoded values
- ✅ Include `@Preview` with realistic dummy data for every screen
- ✅ Add new dependencies to `gradle/libs.versions.toml` first, then reference in `build.gradle.kts`

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`feature/amazing-feature`)
3. Commit your changes following the PR naming convention
4. Push to the branch and open a Pull Request
5. Include screenshots/recordings for any UI changes

---

## 📄 License

This project is developed as part of the **ITI (Information Technology Institute)** program.

---

<p align="center">
  <b>Built with ❤️ using Kotlin, Jetpack Compose, and a lot of ☕</b>
</p>
