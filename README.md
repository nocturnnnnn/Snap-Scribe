# SnapScribe

[![Version](https://img.shields.io/badge/version-v0.2.0--alpha-orange.svg)](https://github.com/nocturnnnnn/Snap-Scribe/releases)
[![Status](https://img.shields.io/badge/status-Front--End%20Complete%20%7C%20Backend%20Planned-yellow.svg)](#-roadmap--current-progress)
[![Platform](https://img.shields.io/badge/platform-Android%208.0%2B%20(API%2026)-brightgreen.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/kotlin-2.3.20-blue.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20%2F%20Material%203-blueviolet.svg)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/license-MIT-lightgrey.svg)](#-license)

> [!NOTE]
> **Current Stage (`v0.2.0-alpha` — Front-End Complete)**: The complete **UI, navigation shell, dual-theme design system, accessibility controls, Indonesian/English localization, DataStore persistence, and unit/UI tests** are implemented and verified. Sample slips and account/subscription states currently run on in-memory fake repositories (`FakeSnapRepository`, `FakeSessionRepository`), ready for on-device ML Kit OCR, Room + FTS4, WorkManager, and CameraX integration next.

---

## 📱 Overview & Core Features

**SnapScribe** (`com.genius.snapscribe`) is an **offline-first, zero-learning-curve Android utility application** designed to eliminate **gallery clutter**—where important screenshots and document photos (bank transfer slips, shipping tracking numbers, Wi-Fi passwords, street addresses, and class schedules) get buried under thousands of personal photos and memes.

Built for **intergenerational usability**—spanning students, working professionals, and older parents—SnapScribe keeps core scanning and indexing **100% local on-device** with a calm, high-legibility paper-journal interface.

### A. Core Features (Free & 100% Offline)

* 🔒 **100% On-Device & Private**: Designed for local OCR scanning (**Google ML Kit Text Recognition**) and local indexing (**Room + SQLite FTS4**) with zero required cloud uploads.
* 🏷️ **4 Smart Auto-Categories**: Automatically organizes captures into four clear buckets:
  * 🧾 **Receipts** — Transfer proofs, grocery receipts, and utility bills (with monthly Rupiah totals)
  * 🔑 **Codes & Links** — Wi-Fi passwords, courier tracking numbers, URLs, and OTPs
  * 📍 **Addresses** — Street addresses, map pins, and clinic/store cards
  * 📝 **Notes** — Study schedules, reminders, and medicine/shopping notes
* ⚡ **Instant Search & Voice Input**: Real-time debounced keyword search with highlighted OCR text snippets, recent search history, and platform **Voice Search** (`RecognizerIntent`) for effortless hands-free lookup.
* 👆 **One-Tap Action Chips**: Surfaces contextual actions directly on cards and detail screens (`Copy Amount`, `Copy Password`, `Copy Resi`, `Open Maps`, `Open Link`, `Add Calendar`, `Copy Text`) with animated **`Copied ✓` / `Tersalin ✓`** confirmation and Android 13+ sensitive clipboard protection for passwords.
* 👓 **Cross-Generational Accessibility**:
  * Minimum **`48dp–56dp`** interactive touch targets throughout
  * Explicit text-labelled navigation (e.g., **`← Back` / `← Kembali`**, icon + label actions)
  * Live-preview **Display Scale** (*Standard `1.0x`*, *Large `1.15x`*, *Extra Large `1.30x`*)
  * **High Contrast Mode** targeting WCAG AAA readability
  * **Low-glare Espresso Dark Mode** and instant **Indonesian / English** per-app language switching

### B. SnapScribe Pro (Planned Tier — 7-Day Free Trial)

| Pro Feature | Key Benefit |
| :--- | :--- |
| **Encrypted Cloud Backup & Sync** | Encrypted cross-device backup when switching phones |
| **Export Receipts (PDF & Excel)** | Monthly expense reports ready to print or share |
| **Smart Expiry & Due Date Alerts** | Automatic reminders for bill due dates, warranties, and medicines |
| **Handwritten OCR & Custom Folders** | Handwritten note recognition and unlimited custom collections |
| **Early Access** | First access to upcoming SnapScribe capabilities |

*(Creating a free account never auto-activates a trial; eligible first-time subscribers receive a transparent 7-day free trial with a Day 5 reminder and safe cancellation without deleting local files.)*

---

## 🎨 Design System & Implemented Screens

SnapScribe pairs **tactile stationery aesthetics** with two complete Material 3 palettes (+ High Contrast variants) and bundled offline variable fonts (`Plus Jakarta Sans` & `Literata`):

| Theme | Base Canvas / Surface | Primary Accent | Headline Font | Body Font |
| :--- | :--- | :--- | :--- | :--- |
| **Warm Everyday Notebook** *(Light)* | `#FFF8F4` / `#FFFFFF` (`#E8E2D9` border) | `#A43716` (Terracotta Coral) | Plus Jakarta Sans | Plus Jakarta Sans |
| **Espresso Night Journal** *(Dark)* | `#181210` / `#261F1B` (`#3D322B` border) | `#E89A3C` (Warm Amber) | Literata | Plus Jakarta Sans |

### Navigation & Screen Map (12+ Screens Implemented)

```
Welcome ──▶ Auth / Register (Email · Google · Free Offline Mode) ──▶ Index Setup
   │                                                                      │
   └───────────────────────── Skip for now ───────────────────────────────┘
                                                                          ▼
  ┌────────────────────────── Main Shell (4-Tab Nav) ──────────────────────────┐
  │   [ Snaps ]         [ Folders ]          [ Search ]          [ Settings ]  │
  └──────┬───────────────────┬───────────────────┬────────────────────┬────────┘
         │                   │                   │                    │
         ▼                   ▼                   │                    ▼
   Snap Detail ◀─────────────┴───────────────────┘               Profile / Pro
    ├── Original / Highlight Text (OCR Canvas overlay)
    ├── Zoom Viewer (Pinch-zoom & Double-tap reset)
    └── Category Picker (Bottom Sheet)
```

* **Onboarding & Auth**: `WelcomeScreen`, `AuthScreen` (Email, Google, Free Offline Mode), `RegisterScreen`, `IndexSetupScreen` (folder selection)
* **Main 4-Tab Shell**: `HomeScreen` (*Snaps* 2-column adaptive grid + category filter chips + sort sheet), `FoldersScreen` (4 *Smart Folders* + Pro-locked *My Collections*), `SearchScreen` (history chips + OCR snippet keyword highlighting), `SettingsScreen` (reading scale + live preview, high contrast, language, indexed albums, theme cards, cache clear)
* **Full-Screen Flows**: `SnapDetailScreen`, `ZoomViewerScreen`, `FolderDetailScreen` (date-grouped ledger), `CameraCaptureScreen` (simulated viewfinder with flash/grid/capture modes), `ProfileScreen` (Signed-in & Guest Offline states), `ProScreen` (plan comparison & trial timeline)

---

## 🛠️ Architecture & Tech Stack

SnapScribe follows **MVVM + Unidirectional Data Flow (UDF)** with a single-activity Compose architecture (`MainActivity : AppCompatActivity`), repository abstractions for seamless backend swap-in, and manual DI via `AppContainer`.

```
  Composable Screens (Stateless UI + Previews)
                 │  UI Events
                 ▼
            ViewModels (StateFlow<UiState>)
                 │
     ┌───────────┼───────────────────┐
     ▼           ▼                   ▼
SnapRepository  SettingsRepository  SessionRepository
     │           │                   │
     ▼           ▼                   ▼
FakeSnapRepo    DataStore Prefs     FakeSessionRepo
(Planned: Room  (Persisted Live)    (Planned: Auth
 + FTS4 + ML Kit)                    + Play Billing)
```

| Layer / Area | Technology | Status |
| :--- | :--- | :--- |
| **Language & Build** | Kotlin `2.3.20`, AGP `9.0.1`, Gradle `9.1.0`, Version Catalog (`libs.versions.toml`) | Implemented |
| **UI Toolkit** | Jetpack Compose BOM `2026.02.01`, Material 3, Material Icons Extended `1.7.8` | Implemented |
| **Navigation** | Navigation Compose `2.9.7` (type-safe `@Serializable` routes + saved tab state) | Implemented |
| **State & Persistence** | Lifecycle ViewModel Compose `2.9.4`, Coroutines `1.10.2`, DataStore Preferences `1.1.7` | Implemented |
| **Localization & Splash** | AppCompat `1.7.1` (`per-app language` ID/EN), Core SplashScreen `1.2.0` | Implemented |
| **Testing & QA** | JUnit `4.13.2`, Coroutines Test, Turbine `1.2.1`, Compose UI Test, Android Lint | Implemented |
| **On-Device OCR & Camera** | Google ML Kit Text Recognition, CameraX | Planned |
| **Local Index & Sync** | Room + SQLite FTS4, WorkManager | Planned |

### Package Layout (`app/src/main/java/com/genius/snapscribe/`)

```text
com.genius.snapscribe/
├── MainActivity.kt · SnapScribeApplication.kt
├── di/AppContainer.kt
├── domain/model/        # Snap, SnapCategory, QuickAction, Session, AppSettings
├── data/
│   ├── local/           # SettingsRepository + DataStoreSettingsRepository
│   ├── repository/      # SnapRepository, FakeSnapRepository, SessionRepository, FakeSessionRepository
│   └── sample/          # SampleSnaps (20 categorized Indonesian/English demo slips)
├── util/                # Clipboard (EXTRA_IS_SENSITIVE), Share/Maps/Url/Calendar, VoiceSearch, Formatters
└── ui/
    ├── SnapScribeApp.kt
    ├── theme/           # Warm Light & Espresso Dark + High Contrast, Type (variable fonts), Shapes, Spacing
    ├── components/      # SnapTopBar, SnapBottomBar, SnapFab, SnapCard, ActionChipButton, SearchField, etc.
    ├── navigation/      # Routes, TopLevelDestination, SnapScribeAppState, SnapScribeNavHost
    └── screens/         # welcome, auth, onboarding, home, search, detail, folders, camera, settings, profile, pro
```

---

## 🗺️ Roadmap & Current Progress

* [x] **Milestone 1: Project Setup, Design System & Core Architecture**
  * [x] Android Studio project scaffold (`com.genius.snapscribe`), Version Catalog (`libs.versions.toml`), Gradle `9.1.0` + AGP `9.0.1`
  * [x] Dual Material 3 design system (*Warm Everyday Notebook* & *Espresso Night Journal*) + High Contrast + bundled variable fonts
  * [x] Clean domain models, repository interfaces, manual DI (`AppContainer`), and DataStore settings persistence
  * [x] Full Indonesian (`values/`) and English (`values-en/`) localization with runtime per-app language switching
* [x] **Milestone 2: Front-End UI, Navigation & Accessibility**
  * [x] Onboarding flow (`Welcome` → `Auth` / `Register` → `IndexSetup`) including Free Offline Mode
  * [x] 4-tab main shell (`Snaps`, `Folders`, `Search`, `Settings`) with persistent tab state and extended capture FAB
  * [x] `SnapDetail` (Original vs Highlight OCR Canvas overlay, `CategoryPickerSheet`), `ZoomViewer`, `FolderDetail`, `CameraCapture` simulation, `Profile`, and `Pro` paywall
  * [x] Platform utilities (Clipboard, Share Sheet, Maps/Browser/Calendar intents, Voice Search)
  * [x] 10/10 JVM unit tests passing, Compose instrumented navigation test compiled, 0 Android Lint errors
* [ ] **Milestone 3: On-Device ML Kit OCR, CameraX & Room FTS4 Engine** *(Next)*
  * [ ] Replace `FakeSnapRepository` with `RoomSnapRepository` + SQLite FTS4 virtual table
  * [ ] Integrate Google ML Kit Text Recognition & local `SmartClassifier` heuristics
  * [ ] Replace simulated camera viewfinder with real CameraX capture + runtime media/camera permissions
  * [ ] Implement WorkManager background scanner for `Screenshots`, `WhatsApp Images`, and `Camera Roll`
* [ ] **Milestone 4: Account Auth, Pro Features & Release Polish** *(Planned)*
  * [ ] Real Email/Google authentication and Google Play Billing integration
  * [ ] Encrypted cloud backup, PDF/Excel receipt export, and smart due-date notifications
  * [ ] Instrumented UI tests on emulator (`connectedDebugAndroidTest`) & GitHub Actions CI

---

## 🚀 Getting Started

### Prerequisites

* **Android Studio** (Meerkat / Narwhal or newer) or **Android CLI**
* **JDK 17** (configured via `org.gradle.java.home` in `gradle.properties`)
* **Android SDK Platform 37** (`compileSdk = 37`, `minSdk = 26`)
* Device or Emulator running **Android 8.0 (API Level 26) or higher**

### Build & Verify

```bash
# Clone the repository
git clone https://github.com/nocturnnnnn/Snap-Scribe.git
cd Snap-Scribe

# Build the debug APK
./gradlew assembleDebug

# Run local JVM unit tests (10 tests across 5 suites)
./gradlew testDebugUnitTest

# Run Android Lint checks
./gradlew lintDebug

# Compile & run Compose UI tests (requires a running emulator or connected device)
./gradlew assembleDebugAndroidTest
./gradlew connectedDebugAndroidTest
```

---

## 👥 About This Project

### Development Team

Developed by **Kelompok SnapScribe**:

| No | Name | Student ID (NIM) | Role |
| :---: | :--- | :---: | :--- |
| 1 | **Zala Risky Pratama** | `2024150064` | Android Developer / System Architecture |
| 2 | **Daffa Mahibi Kusumawardani** | `2024150071` | Android Developer / UI & Accessibility |
| 3 | **Abdul Hamid** | `2024150068` | Android Developer / Database & On-Device ML |

### 🎓 Academic Context

This project is developed as the **Tugas Akhir (Final Project)** for the **Pemrograman Perangkat Bergerak (Mobile Device Programming)** course:
* **Academic Level**: Semester 5
* **Department**: Teknik Informatika (Informatics Engineering)
* **Institution**: **Universitas Sains Al-Qur'an (UNSIQ) Jawa Tengah di Wonosobo**

---

## 📄 License

This project is licensed under the **MIT License**.
