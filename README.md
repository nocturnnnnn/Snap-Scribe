# SnapScribe

[![Version](https://img.shields.io/badge/version-v0.0.1--alpha-orange.svg)](https://github.com/nocturnnnnn/Snap-Scribe/releases)
[![Status](https://img.shields.io/badge/status-Heavily%20Under%20Development-critical.svg)](#-roadmap--current-checklist-v001-alpha)
[![Platform](https://img.shields.io/badge/platform-Android-brightgreen.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/kotlin-2.3.20-blue.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20%2F%20Material%203-blueviolet.svg)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/license-MIT-lightgrey.svg)](LICENSE)

> [!WARNING]
> **Pre-release Notice (`v0.0.1-alpha`)**: SnapScribe is currently in early, active development. Architecture, schema, and API contracts may change significantly.

---

## 📱 Overview & Core Features

**SnapScribe** is an **offline-first, zero-learning-curve Android utility application** created to solve everyday gallery clutter and screenshot overload for users of all generations—from tech-savvy students to older parents and seniors.

Billions of screenshots, transaction slips, grocery receipts, verification codes, and address notes are snapped daily, only to get lost in unorganized phone galleries. SnapScribe solves this problem with privacy and simplicity at its core:

* 🔒 **100% Offline & Private**: Powered entirely on-device by **Google ML Kit Text Recognition (OCR)** and local **SQLite FTS4 Full-Text Search**. Zero server reliance, no cloud sync required, and no risk of personal data leaks.
* ⚡ **Instant Search (Room FTS4)**: Virtual tables powered by SQLite FTS4 enable millisecond keyword searches across thousands of screenshots and notes using the SQLite `MATCH` operator.
* 🏷️ **Smart Automatic Categorization**: Automatically detects context and categorizes extracted text into intuitive buckets:
  * 💳 *Receipts & Payments* (Transfer proofs, invoice IDs, totals)
  * 🔗 *Codes & Links* (OTP codes, tracking numbers, URLs)
  * 📍 *Addresses & Contacts* (Shipping destinations, phone numbers)
  * 📝 *Notes & Memos* (Text fragments, whiteboards, book passages)
* 👆 **One-Tap Actions**: Actionable suggestions directly on card items (Copy Code, Open URL, Call/Message, Share Text) so users never need to retype data.
* 👓 **Accessible Multi-Generational UI**: High-contrast, large-touch-target Jetpack Compose Material 3 interface crafted for effortless accessibility across all age demographics.

---

## 🛠️ Architecture & Tech Stack

SnapScribe adheres to modern Android development best practices, following **Clean Architecture** and **MVVM (Model-View-ViewModel)** with reactive streams.

```
                    ┌───────────────────────────────┐
                    │       Presentation Layer      │
                    │   (Jetpack Compose + M3)      │
                    └───────────────▲───────────────┘
                                    │ StateFlow / UI Events
                    ┌───────────────┴───────────────┐
                    │        ViewModel Layer        │
                    │   (State Holders & Scopes)    │
                    └───────────────▲───────────────┘
                                    │ Coroutines / Flow
                    ┌───────────────┴───────────────┐
                    │        Repository Layer       │
                    │ (Data Abstraction & Offline)  │
                    └───────┬───────────────┬───────┘
                            │               │
            ┌───────────────▼──────┐ ┌──────▼──────────────┐
            │   Google ML Kit OCR  │ │   Room Database     │
            │  (On-Device Vision)  │ │ (SQLite FTS4 Index) │
            └──────────────────────┘ └─────────────────────┘
```

| Component | Technology | Description |
| :--- | :--- | :--- |
| **Language** | Kotlin 2.3+ | Modern concise syntax, coroutines, and type safety |
| **UI Framework** | Jetpack Compose | Declarative UI toolkit with Material Design 3 |
| **Architecture** | Clean Architecture + MVVM | Strict separation of concerns and testability |
| **On-Device Vision** | Google ML Kit Text Recognition | Offline on-device Optical Character Recognition (OCR) |
| **Local Database** | Room + SQLite FTS4 | High-performance full-text search indexing |
| **Asynchronous** | Kotlin Coroutines & Flow | Non-blocking reactive background operations |
| **Image Loading** | Coil | Fast, lightweight image loading for Android |
| **Background Work** | WorkManager | Guaranteed opportunistic background sync & indexing |

---

## 🗺️ Roadmap & Current Checklist (`v0.0.1-alpha`)

- [ ] **Milestone 1: Project Setup & Core Architecture** *(In Progress)*
  - [x] Initial Android project scaffolding via Android CLI
  - [x] Version Catalogs (`libs.versions.toml`) configuration
  - [x] Kotlin Symbol Processing (KSP) setup
  - [x] Git repository initialization & pre-release tagging
  - [ ] Continuous Integration (CI) build workflow setup
- [ ] **Milestone 2: ML Kit OCR & Room FTS4 Indexing Engine** *(Planned)*
  - [x] Room FTS4 Entity, DAO, and Database definition
  - [x] Text Recognition Manager implementation
  - [ ] Real-time background screenshot detection via ContentObserver / WorkManager
  - [ ] Batch processing pipeline for existing gallery images
- [ ] **Milestone 3: Smart Classifier & One-Tap Actions** *(Planned)*
  - [ ] Regex and heuristic-based text classification (Receipts, Codes, Addresses, Notes)
  - [ ] Contextual action triggers (Copy Code, Dial Number, Open Maps, Open URL)
  - [ ] Export & share capabilities (Text, PDF summary)
- [ ] **Milestone 4: Jetpack Compose UI & Accessibility Polish** *(Planned)*
  - [x] Material 3 Search Bar and Document Card list
  - [ ] High-contrast Senior/Elderly accessibility mode
  - [ ] Bottom Sheet details and full-text highlighting
  - [ ] Complete unit and instrumented test coverage

---

## 🚀 Getting Started

### Prerequisites
* **Android Studio Ladybug (2024.2.1+)** or the standalone **Android CLI**
* **JDK 17+**
* **Android SDK Platform 34+** (compileSdk 37 configured)
* Device or Emulator running **Android 7.0 (API Level 24) or higher**

### Building from Source

```bash
# Clone the repository
git clone https://github.com/nocturnnnnn/Snap-Scribe.git
cd Snap-Scribe

# Build the debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew testDebugUnitTest
```

---

## 👥 Development Team

Developed by **Kelompok SnapScribe**:

| No | Name | Student ID (NIM) | Role |
| :---: | :--- | :---: | :--- |
| 1 | **Zala Risky Pratama** | `2024150064` | Android Developer / System Architecture |
| 2 | **Daffa Mahibi Kusumawardani** | `2024150071` | Android Developer / UI & Accessibility |
| 3 | **Abdul Hamid** | `2024150068` | Android Developer / Database & On-Device ML |

---

## 🎓 Academic Context

This project is developed as the **Tugas Akhir (Final Project)** for the **Pemrograman Perangkat Bergerak (Mobile Device Programming)** course:
* **Academic Level**: Semester 5
* **Department**: Teknik Informatika (Informatics Engineering)
* **Institution**: **Universitas Sains Al-Qur'an (UNSIQ) Jawa Tengah di Wonosobo**

---

## 📄 License

This project is licensed under the [MIT License](LICENSE) - see the LICENSE file for details.
