# Architecture Layers & Integration
> **Platform:** Android (Jetpack Compose)
> **Architecture:** Multi-Module + Clean Architecture
> **Core Tech Stack:** Ktor (Network), Koin (DI)

---

## 1. Multi-Module Hierarchy

The project architecture strictly separates concerns into independent modules. The dependency flow strictly moves from outer layers (App, Feature) toward inner layers (Domain), while UI dependencies flow directly to the Design System.

| Module Layer | Responsibility | Dependencies |
| :--- | :--- | :--- |
| **App** | Application class, Koin DI initialization, Navigation host. | Feature modules, Core modules |
| **Presentation (Features)** | MVI Contracts (State, Intent, Effect), UI rendering. | Domain, Design System |
| **Domain** | Pure Kotlin business logic, Koin modules, `Result` wrapper, custom exceptions. | None (No Android dependencies) |
| **Data** | Mappers, data sources, Ktor network setup, `safeApi` utility, Koin DI. | Domain |
| **Design System** | Single source of truth for all visual elements. | Compose Foundation |

## 2. Layer Specifications

### 2.1 The Presentation Layer (Feature Modules)
Feature modules contain only the components necessary to bind the MVI contract to the UI.

*   Contains the `ui`, `screen`, `viewmodel`, `state`, `event`, `effect`, and `mapper` directories.
*   Delegates all visual styling, typography, spacing, and shapes to the Design System.
*   Communicates with the Domain layer exclusively by dispatching intents to Use Cases and observing Flow states.

### 2.2 The Domain Layer
The Domain layer is the pure Kotlin core of the application, entirely agnostic of the Android framework.

*   Defines the core `Result<T>` sealed class used universally to communicate state across the architectural boundary:
    ```kotlin
    sealed class Result<out T> {
        data class Success<out T>(val data: T) : Result<T>()
        data class Failure(val exception: Throwable) : Result<Nothing>()
        object Loading : Result<Nothing>()
    }
    ```
*   Contains Use Cases representing core business rules (e.g., calculating study streaks or formatting pedagogical summaries).
*   Defines Data Classes representing core domain models (`Course`, `StudySession`, `RoadmapEvent`).
*   Defines App Flow Repository interfaces to be implemented by the Data layer.
*   Houses Domain Koin DI modules and Custom Exceptions (`AppException`, `AuthException`, `NetworkException`).

### 2.3 The Data Layer
The Data layer coordinates the retrieval, caching, and mapping of raw data into Domain models utilizing Ktor for remote communication.

*   Implements the App Flow Repositories defined in the Domain layer.
*   Houses **Ktor** HTTP client configurations and remote endpoints.
*   Implements network safety utilities:
    *   `safeApi`: An extension function responsible for wrapping all Ktor API calls, ensuring safe execution.
    *   `handleException`: An extension function utilized within `safeApi` to map Ktor network errors and timeouts into domain-specific Custom Exceptions (`NetworkException`, `AuthException`).
*   Houses Data Transfer Object (DTO) Mappers to convert raw network/database responses into pure Domain models.
*   Contains Data Koin DI modules.

### 2.4 The Design System Layer
The Design System prevents UI duplication and enforces consistency. It is the single source of truth for the app's visual identity.

*   Provides reusable components (e.g., `AppButton`, `AppTextField`, `CourseCard`).
*   Exposes design tokens through `CompositionLocal` (e.g., `Theme.colorScheme`, `Theme.typography`, `Theme.spacing`).
*   Contains all fonts, icons, illustrations, animations, and common UI utilities.
*   Feature modules must never define hardcoded colors, padding, or custom text styles.

---