# Architecture Agents
> **Platform:** Android (Jetpack Compose)
> **Architecture:** MVI + Clean Architecture
> **Domain:** AI Smart Study Planner

---

## 1. System Agents (MVI Actors)

The presentation layer operates on a unidirectional data flow governed by specific MVI agents. These agents ensure a predictable state machine for complex screens like the Dashboard and the Active Learning Study Room.

*   **State Manager (The Single Source of Truth):** Holds the mutually exclusive `ViewState` and persists critical data across state changes. 
*   **Intent Processor (User Actions & System Triggers):** Acts as the ingestion engine for all UI interactions. It maps intents (e.g., `SearchSubmitted`, `LoadInitialData`) to specific Domain Use Cases.
*   **Effect Dispatcher (One-Off Side Effects):** Handles transient UI events that should not be persisted in the state, such as navigation routing, displaying Snackbars, or triggering one-time Toasts.

## 2. Domain & Data Agents (App Flow Coordinators)

To prevent repository bloat, data coordination is handled per *App Flow* rather than per feature. 

*   **App Flow Repositories:** Coordinators that manage data operations for a complete user journey (e.g., `AuthenticationFlowRepository`, `CourseManagementFlowRepository`). They orchestrate multiple data sources to serve the domain layer.
*   **Use Case Interactors:** Granular, single-responsibility agents residing in the Domain layer. They execute specific business rules, such as validating a user's study capacity parameters or executing a custom exception mapping (e.g., `AuthException`, `NetworkException`).

## 3. AI & Business Logic Agents

The AI Smart Study Planner relies on specialized intelligent agents to handle dynamic scheduling and contextual learning[cite: 2].

### Dynamic Roadmap Engine
This algorithmic agent recalculates and reschedules remaining tasks to ensure adequate preparation time[cite: 2]. 

| Trigger | Action | Result |
| :--- | :--- | :--- |
| Injection of upcoming assessment (e.g., pop quiz)[cite: 2] | Increases priority weight of the corresponding course[cite: 2] | Reactive rescheduling of the semester timeline[cite: 2] |

### Contextual AI Assistant
This active learning agent operates within the Study Room to provide grounded explanations without breaking user focus[cite: 2].

| Capability | Constraint | Output |
| :--- | :--- | :--- |
| Pedagogical Summaries | Based solely on uploaded materials[cite: 2] | Auto-generated, simplified topic breakdowns[cite: 2] |
| Deep-dive Q&A | Disabled for Custom Courses (Online/External)[cite: 2] | Contextual answers to reduce cognitive load[cite: 2] |

---

## 4. Development Guidelines

### Dependency Management
When adding a new dependency, it must be added to the version catalog file (`gradle/libs.versions.toml`) first, and then referenced in the appropriate `build.gradle.kts` file.

### Coding Conventions
- **No Fully Qualified Names (FQNs):** When using a class or function, do not use its fully qualified name inline. Always import it and use its simple name.
  *   **Do:** `private fun navigateToHome(user: User)` or `delay(1000L)`
  *   **Don't:** `private fun navigateToHome(user: com.iti.mongez.domain.auth.model.User)` or `kotlinx.coroutines.delay(1000L)`
- **Modern UI Components:** Always use the most up-to-date and modern Jetpack Compose APIs. Avoid using deprecated or obsolete composables.
  *   **Do:** `HorizontalDivider(...)`
  *   **Don't:** `Divider(...)` (Deprecated)
- **Component Reusability:** When creating generic UI elements (like `SocialButton`), place them directly in the `design_system` module rather than duplicating them locally inside presentation screens.
- **No Hardcoded Values:** Do not use hardcoded dimensions (dp/sp), colors, or alpha values in the UI. Always use `Theme.spacing.*`, `Theme.colorScheme.*`, `Theme.typography.*`, and `Theme.radius.*` from the design system to ensure consistency.
- **Compose Previews:** Every screen must include a `@Preview` function to visualize its layout and state easily during development. Provide dummy data that reflects a realistic state.