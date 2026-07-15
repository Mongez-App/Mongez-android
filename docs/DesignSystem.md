# AI Learning Assistant
# Design System Documentation

> **Version:** 1.0.0  
> **Platform:** Android (Jetpack Compose)  
> **Architecture:** Multi Module + Clean Architecture  
> **Design Language:** Material 3 Foundation + Custom Design System

---

# Table of Contents

1. Introduction
2. Design Principles
3. Design Goals
4. Multi Module Architecture
5. Design System Architecture
6. Folder Structure
7. Theme Setup
8. Composition Locals
9. Design Tokens
10. Usage Guidelines

---

# 1. Introduction

## Purpose

The Design System is the single source of truth for every visual element used throughout the application.

Instead of every feature creating its own colors, typography, spacing, buttons, or cards, every UI element should be built from reusable design tokens and reusable components provided by the **design-system** module.

The Design System provides consistency across the application while making the codebase easier to maintain, scale, and extend.

---

## Why a Design System?

As the application grows, different developers may unintentionally introduce:

- different shades of purple
- different corner radii
- inconsistent typography
- random spacing values
- duplicated button implementations
- duplicated cards
- inconsistent dialogs
- inconsistent bottom sheets

Without a design system, maintaining UI consistency becomes increasingly difficult.

The Design System solves this by defining one official implementation for every visual element.

---

## Objectives

The Design System aims to provide:

- Consistent UI across all screens
- Reusable UI components
- Semantic design tokens
- Light & Dark theme support
- RTL support
- Accessibility compliance
- Easy scalability
- Maintainable codebase
- Reduced UI duplication
- Faster feature development

---

## Scope

The Design System owns:

- Color Palette
- Color Scheme
- Typography
- Shapes
- Radius
- Spacing
- Elevation
- Icons
- Animations
- Components
- Theme
- Common UI utilities

Feature modules should **never** redefine these resources.

---

# 2. Design Principles

The application's visual identity follows several core principles.

---

## 2.1 Simplicity

The interface should remain clean and distraction-free.

Avoid unnecessary decorations, borders, and excessive shadows.

Every visual element must have a purpose.

---

## 2.2 Consistency

Every screen should feel like part of the same application.

Consistency includes:

- Typography
- Colors
- Icons
- Button styles
- Navigation
- Cards
- Input fields
- Animations

Users should never feel that they have navigated into a different application.

---

## 2.3 Hierarchy

Visual hierarchy guides users toward important actions.

Hierarchy should primarily be established using:

- Size
- Weight
- Contrast
- White space

Not by adding excessive colors.

---

## 2.4 Accessibility

The application should be usable by everyone.

Requirements include:

- Minimum touch target: 48dp
- Readable font sizes
- WCAG AA contrast ratio
- Screen reader compatibility
- RTL compatibility
- Dynamic font scaling support

---

## 2.5 Reusability

Every reusable UI element should exist only once inside the Design System.

Examples:

✅ PrimaryButton

✅ SearchField

✅ CourseCard

✅ SectionHeader

✅ LoadingIndicator

❌ Recreating the same button in every feature module

---

## 2.6 Scalability

The Design System should support future additions without requiring breaking changes.

Examples include:

- New themes
- Seasonal branding
- New button variants
- Additional languages
- Tablet layouts

---

# 3. Visual Identity

The application is designed to feel:

- Modern
- Clean
- Intelligent
- Friendly
- Calm
- Educational
- Professional

The visual language emphasizes:

- Rounded corners
- Soft shadows
- Purple branding
- Large touch targets
- Comfortable spacing
- Minimal visual noise

---

# 4. Multi Module Architecture

## Overview

The project follows a modular architecture.

```
App
│
├── app
│
├── core
│   ├── design-system
│   ├── common
│   ├── ui
│   └── navigation
│
├── feature-auth
├── feature-home
├── feature-course
├── feature-roadmap
├── feature-profile
│
├── data
│
├── domain
│
└── presentation
```

---

## Module Responsibilities

### app

Responsible for:

- Application class
- Dependency injection initialization
- Navigation host
- Entry point

---

### design-system

Responsible for every visual resource.

Contains:

- Theme
- Colors
- Typography
- Components
- Shapes
- Spacing
- Icons
- Animations

No business logic belongs here.

---

### feature modules

Each feature module should only contain:

```
ui/
screen/
viewmodel/
state/
event/
effect/
mapper/
```

Feature modules should never define:

- Colors
- Typography
- Buttons
- Shapes
- Radius

Everything should come from the Design System.

---

# Dependency Graph

```
feature-home
        │
        │
        ▼
design-system

feature-auth
        │
        ▼
design-system

feature-course
        │
        ▼
design-system
```

Every feature depends on the Design System.

The Design System depends only on Compose libraries.

---

# Benefits

- Easier maintenance
- Shared UI
- Less duplication
- Faster UI development
- Consistent branding

---

# 5. Design System Architecture

The Design System itself should also be modular internally.

```
design-system
│
├── foundation
│
├── components
│
├── theme
│
├── previews
│
├── util
│
└── resources
```

---

## Foundation

Foundation contains only design tokens.

No composables should exist here.

```
foundation/
```

Contains:

```
color/
spacing/
radius/
shape/
typography/
icon/
motion/
elevation/
```

Think of Foundation as the "design language."

---

## Components

Contains reusable composables.

```
components/
```

Examples:

```
PrimaryButton

SecondaryButton

SearchField

CourseCard

OutlinedTextField

Avatar

TopBar

NavigationBar

SectionTitle

ProgressBar

EmptyState

LoadingIndicator

ErrorView

Dialog

BottomSheet

FAB

TagChip

Pill

Badge
```

Every screen should be built using these components.

---

## Theme

Responsible for exposing the Design Tokens through CompositionLocal.

Example:

```
Theme.colorScheme

Theme.typography

Theme.spacing

Theme.radius

Theme.elevation

Theme.motion
```

The Theme object should be the only public entry point.

---

## Resources

Contains:

```
fonts/

icons/

illustrations/

animations/

lottie/

svg/

xml/
```

---

# 6. Folder Structure

Recommended folder organization.

```
design-system
│
├── src
│
│   ├── main
│   │
│   ├── kotlin
│   │
│   │   └── com.example.designsystem
│   │
│   │       ├── foundation
│   │       │
│   │       │   ├── color
│   │       │   ├── typography
│   │       │   ├── spacing
│   │       │   ├── radius
│   │       │   ├── elevation
│   │       │   ├── icon
│   │       │   └── motion
│   │
│   │       ├── components
│   │       │
│   │       │   ├── button
│   │       │   ├── textfield
│   │       │   ├── card
│   │       │   ├── navigation
│   │       │   ├── appbar
│   │       │   ├── dialog
│   │       │   ├── sheet
│   │       │   ├── chip
│   │       │   ├── progress
│   │       │   ├── loading
│   │       │   └── common
│   │
│   │       ├── theme
│   │       │
│   │       ├── preview
│   │       │
│   │       └── util
│   │
│   └── resources
│
└── build.gradle.kts
```

---

# 7. Theme Setup

The application theme should expose only semantic values.

Feature modules should never know implementation details.

Instead of:

```kotlin
Color(0xFF5B4CF6)
```

Use:

```kotlin
Theme.colorScheme.primary.primary
```

---

Instead of:

```kotlin
16.dp
```

Use:

```kotlin
Theme.spacing.md
```

---

Instead of:

```kotlin
20.dp
```

Use:

```kotlin
Theme.radius.large
```

---

Instead of:

```kotlin
TextStyle(...)
```

Use:

```kotlin
Theme.typography.title.large
```

---

## Theme Object

The public API should remain simple.

```kotlin
Theme
│
├── colorScheme
├── typography
├── spacing
├── radius
├── elevation
├── motion
└── shapes
```

Feature developers should rarely need anything outside of the `Theme` object.

---

# 8. Composition Locals

Every design token should be provided through `CompositionLocal`.

Example:

```kotlin
LocalColorScheme

LocalTypography

LocalSpacing

LocalRadius

LocalElevation

LocalMotion
```

Then exposed through:

```kotlin
Theme.colorScheme

Theme.typography

Theme.spacing

Theme.radius

Theme.elevation

Theme.motion
```

This approach ensures that changing themes (Light, Dark, Dynamic, Brand Variants) only requires updating the provided values, while feature modules continue to use the same API.

---

# 9. Design Tokens

Design tokens are the smallest reusable visual values.

Examples include:

- Primary Color
- Success Color
- 16.dp Spacing
- Large Radius
- Title Large Typography
- Level 2 Elevation

Components should consume design tokens instead of hardcoded values.

For example, a `PrimaryButton` should use:

- `Theme.colorScheme.brand.brand`
- `Theme.radius.large`
- `Theme.spacing.md`
- `Theme.typography.label.large`

This ensures consistency and makes global visual changes straightforward.

---

# 10. Usage Guidelines

To keep the UI consistent across the project, follow these rules:

### ✅ Do

- Use `Theme` values for colors, spacing, typography, and radius.
- Build screens from reusable components in the Design System.
- Add new reusable UI elements to the Design System instead of duplicating them.
- Prefer semantic names (e.g., `success`, `error`, `surface`) over raw color names (`Green500`, `Purple600`).

### ❌ Don't

- Hardcode colors (`Color(0xFF...)`) in feature modules.
- Use arbitrary `dp` values outside the spacing system.
- Create duplicate buttons, cards, or dialogs.
- Reference font resources directly from feature modules.

Following these guidelines ensures a scalable, maintainable, and visually consistent application.

# Part 2 — Foundations
# Color System, Typography, Spacing, Radius, Elevation & Motion

---

# Table of Contents

1. Foundations
2. Color Philosophy
3. Color Palette
4. Semantic Color Scheme
5. Light Theme
6. Dark Theme
7. Typography System
8. Font Families
9. Typography Scale
10. Spacing System
11. Radius System
12. Elevation System
13. Motion System
14. Icons
15. Best Practices
16. Do & Don't

---

# Foundations

The Foundation layer contains all design tokens used throughout the application.

These tokens are the smallest reusable visual values from which every component is built.

Examples include:

- Primary Color
- Success Color
- 16dp Spacing
- Large Radius
- Title Large Typography
- Medium Elevation
- Standard Animation Duration

Feature modules **must never** define these values directly.

Instead, they consume the Foundation through the `Theme` object.

```
Theme
├── colorScheme
├── typography
├── spacing
├── radius
├── elevation
├── motion
└── icon
```

---

# Color Philosophy

The application is an AI-powered learning platform.

The color system should communicate:

- Trust
- Intelligence
- Focus
- Simplicity
- Productivity

Purple is selected as the primary brand color because it represents:

- Intelligence
- Creativity
- Technology
- Innovation

Large surfaces remain neutral to reduce eye fatigue.

Color should only be used to communicate hierarchy and state—not decoration.

---

# Color Architecture

The project uses three color layers.

```
Primitive Colors
        │
        ▼
Color Palette
        │
        ▼
Semantic Color Scheme
        │
        ▼
Components
```

Never use primitive colors directly inside UI.

Instead of

```kotlin
Color(0xFF5B4CF6)
```

Always use

```kotlin
Theme.colorScheme.brand.primary
```

---

# Primitive Color Palette

Primitive colors should never be accessed outside the Design System.

```
Purple

Gray

Green

Red

Orange

Blue

White

Black
```

Each color contains multiple shades.

Example:

```
Purple50
Purple100
Purple200
Purple300
Purple400
Purple500
Purple600
Purple700
Purple800
Purple900
```

The same applies to Gray, Green, Red, Orange and Blue.

---

# Recommended Purple Palette

| Token | Hex |
|---------|---------|
| Purple50 | #F5F3FF |
| Purple100 | #EDE9FE |
| Purple200 | #DDD6FE |
| Purple300 | #C4B5FD |
| Purple400 | #A78BFA |
| Purple500 | #8B5CF6 |
| Purple600 | **#5B4CF6** |
| Purple700 | #4C3FE0 |
| Purple800 | #4338CA |
| Purple900 | #312E81 |

The generated designs are closest to **Purple600**, which becomes the application's Brand Primary.

---

# Gray Palette

| Token | Hex |
|---------|---------|
| Gray50 | #FCFCFD |
| Gray100 | #F8F9FC |
| Gray200 | #F2F4F7 |
| Gray300 | #EAECF0 |
| Gray400 | #D0D5DD |
| Gray500 | #98A2B3 |
| Gray600 | #667085 |
| Gray700 | #475467 |
| Gray800 | #344054 |
| Gray900 | #101828 |

---

# Semantic Color Scheme

Components should never know palette names.

Instead, they use semantic colors.

```
Theme.colorScheme
```

Recommended structure:

```kotlin
ColorScheme
│
├── brand
│
├── text
│
├── background
│
├── surface
│
├── border
│
├── state
│
├── button
│
├── input
│
├── chip
│
├── navigation
│
└── card
```

Instead of a flat structure containing dozens of colors, group related colors together.

---

# Brand Colors

```kotlin
Brand

primary

primaryVariant

primaryContainer

onPrimary

onPrimaryContainer
```

---

# Text Colors

```kotlin
Text

primary

secondary

tertiary

hint

disabled

inverse
```

---

# Surface Colors

```kotlin
Surface

background

surface

surfaceVariant

surfaceContainer

surfaceHigh

surfaceHighest
```

---

# Border Colors

```kotlin
Border

primary

secondary

focused

error

success

disabled
```

---

# State Colors

```kotlin
State

success

warning

error

info
```

---

# Light Theme

Primary

Purple600

Background

White

Surface

White

Cards

White

Primary Text

Gray900

Secondary Text

Gray600

Hints

Gray500

Divider

Gray300

Border

Gray300

Disabled

Gray400

Success

Green600

Warning

Orange500

Error

Red600

---

# Dark Theme

Background

#111317

Surface

#181A20

Card

#20232B

Primary Text

White

Secondary Text

Gray300

Hint

Gray500

Divider

Gray700

Border

Gray700

Primary

Purple400

Success

Green500

Warning

Orange400

Error

Red500

---

# Color Usage Guidelines

Purple

Primary actions

Selected state

Progress bars

FAB

Bottom Navigation

Primary Buttons

Links

Gray

Body text

Borders

Background

Dividers

Green

Completed tasks

Progress

Success messages

Red

Errors

Delete

Failed operations

Orange

Warnings

Upcoming deadlines

Blue

Information

Only informational UI

Never use Blue as the application's primary color.

---

# Typography Philosophy

Typography should maximize readability while creating a clear visual hierarchy.

The application primarily displays educational content.

Therefore:

- High readability
- Comfortable line height
- Consistent sizing
- Minimal font weights

---

# Font Families

English

```
Poppins
```

Arabic

```
IBM Plex Sans Arabic
```

Application Logo

```
Madimi One
```

Exactly matching your existing implementation.

---

# Font Weights

Use only four weights.

```
Regular

Medium

SemiBold

Bold
```

Avoid using Thin, ExtraBold, Black.

---

# Typography Scale

```
Display

Headline

Title

Body

Label
```

---

# Display

Used only for splash screen and onboarding hero text.

| Token | Size |
|--------|------|
| Large | 40sp |
| Medium | 36sp |
| Small | 32sp |

---

# Headline

Large page titles.

| Token | Size |
|--------|------|
| Large | 28sp |
| Medium | 24sp |
| Small | 20sp |

---

# Title

Cards

Dialogs

Sections

| Token | Size |
|--------|------|
| Large | 20sp |
| Medium | 18sp |
| Small | 16sp |

---

# Body

Paragraphs

Descriptions

Messages

| Token | Size |
|--------|------|
| Large | 16sp |
| Medium | 14sp |
| Small | 12sp |

---

# Label

Buttons

Tags

Badges

Chips

| Token | Size |
|--------|------|
| Large | 16sp |
| Medium | 14sp |
| Small | 12sp |
| ExtraSmall | 10sp |

---

# Typography Rules

✔ Use predefined typography tokens.

✔ Never create custom TextStyles in feature modules.

✔ Use SemiBold for headings.

✔ Use Regular for paragraphs.

✔ Maintain consistent line height.

---

# Spacing Philosophy

Whitespace is one of the strongest tools for creating hierarchy.

Never use arbitrary spacing values.

Instead:

```
Theme.spacing
```

---

# Spacing Scale

| Token | Value |
|---------|----------|
| xxs | 2.dp |
| xs | 4.dp |
| sm | 8.dp |
| md | 12.dp |
| lg | 16.dp |
| xl | 24.dp |
| xxl | 32.dp |
| xxxl | 40.dp |
| huge | 48.dp |
| giant | 64.dp |

---

# Common Usage

Between icons and text

```
8.dp
```

Screen padding

```
24.dp
```

Between cards

```
16.dp
```

Inside cards

```
16.dp
```

Between sections

```
24.dp
```

Large screen margins

```
32.dp
```

---

# Radius Philosophy

Rounded corners create a friendlier experience.

The application consistently uses large radii.

Never use inconsistent values.

---

# Radius Scale

| Token | Value |
|---------|---------|
| xs | 4.dp |
| sm | 8.dp |
| md | 12.dp |
| lg | 16.dp |
| xl | 20.dp |
| xxl | 24.dp |
| sheet | 28.dp |
| dialog | 32.dp |
| full | 999.dp |

---

# Component Radius

Buttons

16dp

Cards

20dp

Bottom Sheets

28dp

Dialogs

32dp

FAB

28dp

Avatar

Full

---

# Elevation Philosophy

The application follows a soft elevation model.

Heavy shadows should be avoided.

Elevation is used to communicate hierarchy—not decoration.

---

# Elevation Levels

| Token | Usage |
|---------|------------|
| Level0 | Flat |
| Level1 | Cards |
| Level2 | FAB |
| Level3 | Dialog |
| Level4 | Bottom Sheet |

---

# Recommended Elevation Values

| Token | dp |
|---------|------|
| none | 0 |
| xs | 1 |
| sm | 2 |
| md | 4 |
| lg | 8 |
| xl | 12 |

Prefer lower elevation values with subtle shadows rather than large Material defaults to keep the UI lightweight.

---

# Motion Philosophy

Motion should reinforce interactions, not distract users.

Animations should feel:

- Fast
- Responsive
- Natural

---

# Motion Tokens

```
Theme.motion
```

Recommended structure:

```kotlin
Motion

duration

easing

spring

fade

scale
```

---

# Duration Scale

| Token | Duration |
|---------|-----------|
| Instant | 0ms |
| Fast | 150ms |
| Normal | 250ms |
| Slow | 350ms |
| ExtraSlow | 500ms |

---

# Standard Easings

```
Linear

EaseOut

FastOutSlowIn

LinearOutSlowIn

EaseInOut
```

Default recommendation:

```
FastOutSlowInEasing
```

---

# Motion Usage

Screen transition

250ms

Button press

150ms

Bottom sheet

300ms

FAB

250ms

Snackbar

250ms

Progress animation

500ms

Shimmer

1000ms repeating

Loading spinner

Infinite

---

# Icon System

Standard size:

24.dp

Small:

20.dp

Large:

32.dp

Hero:

48.dp

Preferred icon family:

Material Symbols Rounded

Maintain consistent stroke weight and avoid mixing icon packs.

---

# Best Practices

✔ Always use semantic colors.

✔ Consume all design tokens through `Theme`.

✔ Use the predefined typography scale.

✔ Stick to the spacing system.

✔ Keep radii consistent across components.

✔ Use subtle elevation.

✔ Animate only meaningful interactions.

✔ Ensure all tokens support both Light and Dark themes.

---

# Do & Don't

### ✅ Do

- `Theme.colorScheme.brand.primary`
- `Theme.typography.title.large`
- `Theme.spacing.lg`
- `Theme.radius.xl`
- `Theme.elevation.md`
- `Theme.motion.normal`

### ❌ Don't

- `Color(0xFF5B4CF6)`
- `18.dp`
- `RoundedCornerShape(19.dp)`
- `TextStyle(fontSize = 17.sp)`
- `animate*AsState(durationMillis = 173)`

Always rely on the Design System tokens rather than hardcoded values. This keeps the UI consistent, simplifies maintenance, and allows global design changes with minimal code modifications.



# Part 3 — Components
# Reusable UI Components

---

# Table of Contents

1. Overview
2. Component Philosophy
3. Component Architecture
4. Buttons
5. Text Fields
6. Search Bar
7. Cards
8. Chips
9. Progress Indicators
10. Floating Action Button
11. App Bars
12. Navigation Bar
13. Dialogs
14. Bottom Sheets
15. Snackbars
16. Empty States
17. Loading Components
18. Badges
19. Avatars
20. Dividers
21. Lists
22. Accessibility
23. Component States
24. Naming Conventions
25. Do & Don't

---

# Overview

The Design System provides reusable UI components built from the Foundation layer (colors, typography, spacing, radius, elevation, and motion).

Every feature module must compose its UI using these components instead of directly using Material components.

```
Feature Screen
      │
      ▼
Design System Components
      │
      ▼
Foundation Tokens
      │
      ▼
Compose UI
```

---

# Component Philosophy

Every reusable component should:

- Follow the design language.
- Be stateless whenever possible.
- Be customizable through parameters.
- Hide implementation details.
- Support Light & Dark themes.
- Support RTL.
- Be accessible.
- Be previewable.

Each component should expose a simple, semantic API.

---

# Component Organization

```
components/
│
├── button/
│
├── textfield/
│
├── card/
│
├── search/
│
├── navigation/
│
├── dialog/
│
├── bottomsheet/
│
├── progress/
│
├── appbar/
│
├── chip/
│
├── snackbar/
│
├── loading/
│
├── avatar/
│
├── badge/
│
├── divider/
│
└── common/
```

---

# Component Naming

Always prefix reusable components with **App**.

Good:

```
AppButton

AppTextField

AppCard

AppSearchBar

AppNavigationBar

AppDialog

AppBottomSheet
```

Avoid:

```
Button2

MyButton

PurpleButton

DefaultButton
```

---

# Buttons

Buttons communicate user actions.

There are four button types.

```
Primary

Secondary

Outlined

Text
```

---

## Primary Button

Used for the main action on a screen.

Examples:

- Continue
- Login
- Register
- Start Session
- Add Course
- Save

Properties

```
Height

56.dp
```

```
Radius

Theme.radius.large
```

```
Background

Theme.colorScheme.brand.primary
```

```
Content

White
```

```
Typography

Theme.typography.label.large
```

States

- Enabled
- Disabled
- Loading
- Pressed

---

## Secondary Button

Used for secondary actions.

Background

Surface

Border

Primary

Text

Primary

---

## Outlined Button

Transparent background.

Primary border.

Primary text.

---

## Text Button

No background.

Used for:

- Skip
- Forgot Password
- View All
- Cancel

---

## Loading Button

Instead of disabling the screen:

```
Login
```

becomes

```
Loading Spinner
```

Button remains visible.

---

# Text Fields

All input fields should use the same component.

```
AppTextField()
```

Supports:

- Leading icon
- Trailing icon
- Password toggle
- Error state
- Helper text
- Placeholder
- Prefix
- Suffix

---

## Variants

```
Filled

Outlined

Search

Password

ReadOnly
```

---

## States

```
Focused

Unfocused

Error

Disabled

ReadOnly
```

---

## Height

```
56.dp
```

---

## Radius

```
Theme.radius.large
```

---

## Padding

```
16.dp
```

---

# Search Bar

The Home and Courses screens share the same search component.

```
AppSearchBar()
```

Contains

- Search icon
- Placeholder
- Clear button
- Optional filter action

Height

```
48.dp
```

Radius

```
16.dp
```

---

# Cards

Cards group related information.

Cards should never be recreated inside feature modules.

---

## AppCard

Generic reusable card.

Properties

- Elevation
- Radius
- Padding
- Clickable
- Enabled

---

## Course Card

Used on the Courses screen.

Contains

- Thumbnail
- Course Title
- Progress
- Percentage
- Optional badge

---

## Task Card

Contains

- Status icon
- Title
- Duration
- Priority
- Completion indicator

---

## Deadline Card

Contains

- Course
- Deadline
- Remaining days
- Status color

---

## Statistics Card

Contains

- Title
- Number
- Progress
- Icon

---

# Chips

Used for:

- Filters
- Categories
- Selected days
- Tags

Variants

```
Filled

Outlined

Assist

Suggestion

Input
```

States

```
Selected

Unselected

Disabled
```

---

# Progress Indicators

Two types.

```
Linear

Circular
```

---

## Linear Progress

Rounded corners.

Purple fill.

Gray track.

Used in:

- Courses
- Weekly Progress
- Study Goal

---

## Circular Progress

Used for:

- Loading
- Statistics
- Completion

---

# Floating Action Button

Used on:

Courses screen

Properties

Size

```
56.dp
```

Icon

24dp

Shape

Circle

Color

Primary Purple

Elevation

Medium

---

# App Bars

Provide screen navigation.

---

## Top App Bar

Contains

- Back button
- Title
- Optional actions

Height

```
64.dp
```

---

## Home App Bar

Contains

- Greeting
- Avatar
- Notification
- Optional streak

Used only on Home.

---

# Navigation Bar

Bottom Navigation.

Contains

```
Home

Courses

Roadmap

Profile
```

Properties

Height

```
80.dp
```

Active

Purple

Inactive

Gray

Label

Always visible

No shifting animation.

---

# Dialogs

Dialogs interrupt the user.

Should be used sparingly.

---

## Confirmation Dialog

Contains

Title

Description

Primary Action

Secondary Action

---

## Success Dialog

Contains

Illustration

Title

Description

Button

---

## Error Dialog

Contains

Error icon

Message

Retry

Cancel

---

## Dialog Layout

Radius

```
32.dp
```

Padding

```
24.dp
```

Max Width

```
340.dp
```

---

# Bottom Sheets

The application heavily uses modal bottom sheets.

Examples

- Add Course
- Reschedule
- Default Schedule

---

## Structure

Handle

Title

Content

Actions

---

## Radius

```
28.dp
```

---

## Variants

```
Modal

Persistent

Full Screen
```

---

# Snackbars

Used for lightweight feedback.

Examples

```
Course Added

Task Deleted

Network Error

Saved
```

Variants

Success

Error

Warning

Info

---

# Empty States

Instead of blank screens.

Contain

Illustration

Title

Description

Primary Action

Examples

```
No Courses

No Tasks

Offline

No Search Results
```

---

# Loading Components

```
Circular Loading

Linear Loading

Skeleton Loading

Shimmer
```

Prefer Skeleton Loading over fullscreen spinners.

---

# Badges

Used for

Unread notifications

New courses

Priority

Examples

```
3

NEW

HOT
```

---

# Avatars

Variants

Image

Initials

Placeholder

Online indicator optional.

---

# Dividers

Horizontal

Vertical

Inset

Use semantic divider color.

Avoid custom divider implementations.

---

# Lists

Use LazyColumn and LazyRow.

Spacing should come from

```
Theme.spacing
```

Never hardcode spacing.

---

# Component States

Every interactive component should support:

```
Enabled

Disabled

Focused

Pressed

Loading

Selected

Error

Success
```

Avoid creating separate composables for each state.

---

# Accessibility

Every component must support:

Minimum touch target

```
48.dp
```

Screen readers

Content descriptions

RTL

Large font sizes

Keyboard navigation

---

# Component API Guidelines

Prefer APIs like:

```kotlin
AppButton(
    text = "Continue",
    onClick = {},
    enabled = true,
    loading = false
)
```

Instead of exposing colors and typography directly.

The component should obtain design tokens from `Theme`.

---

# Preview Requirements

Every reusable component should have Compose previews.

Examples

```
Light Theme

Dark Theme

RTL

Loading

Disabled

Error

Large Font
```

Preview files should live inside the Design System module.

---

# Do & Don't

## ✅ Do

- Build every screen using reusable Design System components.
- Keep components stateless whenever possible.
- Consume design tokens through `Theme`.
- Expose semantic parameters instead of styling parameters.
- Add new reusable components to the Design System rather than duplicating them.

## ❌ Don't

- Use `Button`, `OutlinedButton`, or `TextField` directly in feature modules.
- Hardcode colors, typography, spacing, or shapes.
- Create multiple implementations of the same component.
- Expose internal styling decisions (e.g., colors or padding) in public APIs.

---

# Component Checklist

Every new component added to the Design System should satisfy the following checklist:

- ✅ Uses only Foundation tokens (`Theme.*`).
- ✅ Supports Light & Dark themes.
- ✅ Supports RTL.
- ✅ Meets accessibility guidelines.
- ✅ Includes Compose previews.
- ✅ Is documented with usage examples.
- ✅ Has semantic, reusable parameters.
- ✅ Is independent of any feature module.
- ✅ Follows consistent naming (`App*` prefix).





# Part 4 — Screen Guidelines, Accessibility, Naming Conventions & Multi-Module Integration

---

# Table of Contents

1. Screen Design Guidelines
2. Layout Guidelines
3. Responsive Design
4. Accessibility
5. Naming Conventions
6. Code Style Guidelines
7. Design System Usage
8. Multi-Module Integration
9. Feature Development Guidelines
10. Preview Guidelines
11. Testing Guidelines
12. Performance Guidelines
13. Do & Don't
14. Code Samples
15. Future Expansion

---

# Screen Design Guidelines

The application follows a **clean, modern, educational** design language.

Every screen should feel like it belongs to the same ecosystem.

Each screen consists of predictable sections:

```
Top App Bar

↓

Header

↓

Primary Content

↓

Secondary Content

↓

Floating Action (Optional)

↓

Bottom Navigation
```

Avoid designing every screen from scratch.

Instead, compose screens using reusable Design System components.

---

# Standard Screen Layout

Every screen should follow this structure.

```kotlin
Scaffold
│
├── TopBar
│
├── SnackbarHost
│
├── FloatingActionButton
│
├── BottomBar
│
└── Content
```

Inside the content:

```
LazyColumn

↓

Section Header

↓

Card

↓

Spacing

↓

Another Section

↓

Card
```

---

# Screen Padding

The application uses one standard padding.

```kotlin
Theme.spacing.xl
```

Equivalent

```
24.dp
```

Never write

```kotlin
padding(23.dp)

padding(17.dp)

padding(19.dp)
```

---

# Vertical Rhythm

Consistent spacing greatly improves readability.

Recommended spacing:

Between AppBar and content

```
24.dp
```

Between sections

```
24.dp
```

Between cards

```
16.dp
```

Inside cards

```
16.dp
```

Between icon and text

```
8.dp
```

Between button and text

```
12.dp
```

---

# Grid System

The application follows an **8dp Grid System**.

Every spacing should be a multiple of:

```
4

8

12

16

24

32

40

48

64
```

Avoid values such as:

```
13

19

27

31
```

---

# Layout Rules

Prefer

```
Column

Row

Box

LazyColumn

LazyRow
```

Avoid deeply nested layouts.

Maximum recommended nesting:

```
4 Levels
```

If nesting becomes deeper, extract reusable composables.

---

# Scrollable Screens

Always use

```
LazyColumn
```

instead of

```
Column + verticalScroll
```

unless the content is guaranteed to be small.

---

# Responsive Design

Although the application targets phones, layouts should scale gracefully.

Support:

- Small phones
- Standard phones
- Foldables
- Tablets (future)

Avoid hardcoded widths.

Instead use:

```kotlin
Modifier.fillMaxWidth()
```

or

```kotlin
Modifier.weight()
```

---

# Window Size Classes (Future)

Future support should include:

```
Compact

Medium

Expanded
```

Large layouts should increase content width rather than simply scaling components.

---

# Landscape Support

Avoid layouts that depend entirely on screen height.

Use Lazy layouts and flexible constraints to ensure usability in landscape orientation.

---

# Accessibility

Accessibility is not optional.

Every component must support accessibility.

---

# Touch Targets

Minimum touch target

```
48.dp
```

Recommended

```
56.dp
```

Buttons smaller than 48dp should not exist.

---

# Font Scaling

Support

```
1.3x

1.5x

2.0x
```

Never clip text.

Allow layouts to expand vertically when necessary.

---

# Color Contrast

Follow

```
WCAG AA
```

Minimum contrast ratio

```
4.5 : 1
```

Avoid light gray text on white backgrounds.

---

# Screen Readers

Every interactive element must include meaningful content descriptions.

Good

```
Search courses
```

Bad

```
Search icon
```

The description should communicate intent, not appearance.

---

# Decorative Images

Decorative illustrations should use:

```kotlin
contentDescription = null
```

This prevents unnecessary announcements by screen readers.

---

# RTL Support

The application supports:

- English
- Arabic

Never manually position elements using left/right.

Always use:

```
Start

End
```

instead of

```
Left

Right
```

---

# Naming Conventions

Consistency in naming improves readability and discoverability.

---

## Packages

Use lowercase.

```
button

textfield

navigation

card

dialog
```

Avoid:

```
Buttons

ButtonPackage

UIButtons
```

---

## Components

Always use PascalCase.

```
PrimaryButton

AppTextField

CourseCard

DeadlineCard

StudyGoalCard
```

---

## State Classes

```
HomeUiState

CoursesUiState

RoadmapUiState
```

---

## Events

```
HomeEvent

CoursesEvent
```

---

## Actions

```
onClick

onDelete

onSearch

onRetry
```

Avoid:

```
click()

btnClicked()

performAction()
```

---

## Booleans

Always start with

```
is

has

can

should
```

Examples

```
isLoading

isSelected

hasNotification

canContinue
```

---

## Constants

Use uppercase snake case.

```
MAX_COURSES

ANIMATION_DURATION

DEFAULT_PADDING
```

---

# Code Style Guidelines

Prefer immutable state.

```kotlin
val
```

instead of

```kotlin
var
```

Use data classes for UI state.

---

Avoid passing many parameters.

Instead of:

```kotlin
CourseCard(
    title,
    progress,
    duration,
    image,
    completed,
    deadline,
    favorite,
    ...
)
```

Use

```kotlin
CourseCard(
    courseUi
)
```

---

# Design System Usage

Every screen should consume the Theme.

Good

```kotlin
Theme.colorScheme.text.primary
```

Bad

```kotlin
Color.Black
```

---

Good

```kotlin
Theme.spacing.lg
```

Bad

```kotlin
16.dp
```

---

Good

```kotlin
Theme.radius.large
```

Bad

```kotlin
RoundedCornerShape(18.dp)
```

---

# Multi-Module Integration

Recommended project structure.

```
app

core
│
├── design-system
├── common
├── ui
├── navigation
└── datastore

feature
│
├── auth
├── home
├── courses
├── roadmap
├── profile

domain

data
```

---

# Module Responsibilities

## app

Application entry point.

Contains

- Application
- Navigation Host
- Dependency Injection initialization

---

## core:design-system

Contains

- Theme
- Colors
- Typography
- Components
- Icons
- Motion
- Shapes
- Spacing
- Radius

No business logic.

---

## core:common

Shared utilities.

Examples

```
Result

Dispatcher

Extensions

Logger

Constants
```

---

## core:navigation

Navigation abstractions.

Routes.

Destinations.

Deep links.

---

## data

Repositories.

Networking.

Room.

Remote APIs.

---

## domain

Pure Kotlin.

Contains

```
Models

Repositories

Use Cases
```

No Android dependencies.

---

## Feature Modules

Each feature should contain:

```
ui

presentation

navigation

di
```

Typical structure:

```
feature-home

│

├── ui
│   ├── screen
│   ├── component
│   ├── state
│   ├── event
│   └── preview
│
├── navigation
│
├── di
│
└── HomeModule
```

Avoid placing design tokens or shared UI components inside feature modules.

---

# Dependency Flow

```
Feature

↓

Domain

↓

Data
```

UI dependency flow:

```
Feature

↓

Design System

↓

Compose
```

Features should never depend on each other.

Communication should happen through navigation or shared abstractions.

---

# Preview Guidelines

Every screen should include previews for:

```
Light

Dark

RTL

Font Scale

Loading

Empty

Success

Error
```

This helps catch UI issues early without running the app.

---

# Testing Guidelines

The Design System should be tested independently.

Recommended tests:

- Typography tokens
- Color scheme selection
- Theme switching
- Component behavior
- Accessibility labels
- Layout correctness
- Snapshot tests (optional)

Feature modules should test business logic separately from UI.

---

# Performance Guidelines

Avoid unnecessary recomposition.

Use:

```
remember

rememberSaveable

derivedStateOf
```

when appropriate.

Prefer immutable models.

Use `LazyColumn` and `LazyRow` for long lists.

Use stable data classes where possible.

Keep composables focused and reusable.

---

# Do & Don't

## ✅ Do

- Build screens from reusable Design System components.
- Use semantic Theme tokens.
- Keep feature modules independent.
- Prefer immutable state.
- Create reusable composables instead of duplicating UI.
- Add previews for all reusable components.
- Use `Scaffold` as the standard screen container.
- Respect accessibility and RTL guidelines.

---

## ❌ Don't

- Hardcode colors, spacing, typography, or shapes.
- Recreate buttons, cards, dialogs, or text fields in feature modules.
- Mix business logic with UI components.
- Allow feature modules to depend on each other.
- Expose Material styling directly from features.
- Use arbitrary spacing values or custom text styles.

---

# Example Screen

```kotlin
@Composable
fun HomeScreen(
    state: HomeUiState,
    onAction: (HomeAction) -> Unit
) {
    Scaffold(
        topBar = {
            HomeTopBar(
                userName = state.userName,
                onNotificationClick = {
                    onAction(HomeAction.NotificationsClicked)
                }
            )
        },
        bottomBar = {
            AppNavigationBar(...)
        },
        floatingActionButton = {
            AppFab(
                onClick = {
                    onAction(HomeAction.AddCourse)
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = Theme.spacing.xl),
            verticalArrangement = Arrangement.spacedBy(
                Theme.spacing.lg
            )
        ) {

            item {
                FocusCourseCard(...)
            }

            item {
                StudyGoalCard(...)
            }

            item {
                SectionHeader(...)
            }

            items(state.tasks) {

                TaskCard(...)
            }
        }
    }
}
```

---

# Final Goal

The Design System should become the **single source of truth** for every visual element in the application.

Every feature module should focus only on:

- Business logic
- State management
- Navigation

Everything related to UI consistency—colors, typography, spacing, components, animations, and accessibility—must live inside the **Design System**.

A well-structured Design System enables faster development, easier maintenance, consistent user experience, and long-term scalability as the application grows.

