# DartScore

MobPro Abschlussprojekt | HSLU | FS 2026  
**Team 2** — Thomas Sartini · Nicola Anghileri

---

## Projektbeschreibung

DartScore ist eine Android-App zur digitalen Verwaltung von Dart-Spielen. Sie unterstützt die Spielmodi 301, 501 und 701, ermöglicht mehrere Spieler pro Spiel, führt Live-Scoring mit automatischer Bust-Erkennung durch und speichert die Spielhistorie inkl. Statistiken vollständig offline.

---

## Technische Anforderungen

| Punkte | Anforderung |
|--------|-------------|
| 3P | Room Datenbank |
| 2P | Dependency Injection (Hilt) |
| 1P | Unit Tests |
| **6P** | **Gesamt** |

### Room Datenbank
- 3 Entitäten: `GameEntity`, `PlayerEntity`, `RoundEntity`
- Foreign Keys mit CASCADE DELETE zwischen allen Tabellen
- Zugriff ausschliesslich über DAOs mit `suspend`-Funktionen
- Konfiguriert mit `Dispatchers.IO` als Query-Executor

### Dependency Injection (Hilt)
- `@HiltAndroidApp` auf `DartScoreApplication`
- `@AndroidEntryPoint` auf `MainActivity`
- `@HiltViewModel` + `@Inject constructor` auf allen ViewModels
- `DatabaseModule` stellt DAOs und Repositories als Singletons bereit

### Unit Tests
- 6 Testklassen mit JUnit 4 + Mockito Kotlin
- `GameViewModelTest` — addScore, Bust-Erkennung, Spielabschluss, Dart löschen
- `HomeViewModelTest` — startGame-Ablauf, Reihenfolge der DB-Operationen
- `StatsViewModelTest` — loadStats, Sortierung, Fehlerbehandlung
- `GameRulesTest` — isBust (alle 3 Fälle), initialScore, padWithZeroes
- `PlayerValidatorTest` — Leer, zu kurz, zu wenige Spieler, Duplikate
- `StatsCalculatorTest` — averageScore, highestScore, throwCount

---

## Architektur

```
Composables → ViewModel → Repository → DAO → Room DB
```

**MVVM** — strikte Schichtentrennung:

- **Screens:** HomeScreen, GameScreen, StatsScreen, WinScreen
- **ViewModels:** HomeViewModel, GameViewModel, StatsViewModel
- **Repositories:** GameRepository, PlayerRepository, RoundRepository

---

## Tech Stack

- Kotlin + Jetpack Compose + Material 3
- Room, Hilt, Coroutines, StateFlow
- Compose Navigation
- minSdk 33 / targetSdk 36
