TEAM-2-README — DartScore
  MobPro Abschlussprojekt | HSLU | FS 2026

Team
----
  Thomas Sartini
  Nicola Anghileri


Projektbeschreibung
-------------------
DartScore ist eine Android-App zur digitalen Verwaltung von Dart-Spielen.
Sie unterstützt die Spielmodi 301, 501 und 701, ermöglicht mehrere Spieler
pro Spiel, führt Live-Scoring mit automatischer Bust-Erkennung durch und
speichert die Spielhistorie inkl. Statistiken vollständig offline.


Verwendete technische Anforderungen
-------------------------------------

  [3 Punkte] Room Datenbank
    - 3 Entitäten: GameEntity, PlayerEntity, RoundEntity
    - Foreign Keys mit CASCADE DELETE zwischen allen Tabellen
    - Zugriff ausschliesslich über DAOs mit suspend-Funktionen
    - Konfiguriert mit Dispatchers.IO als Query-Executor

  [2 Punkte] Dependency Injection (Hilt)
    - @HiltAndroidApp auf DartScoreApplication
    - @AndroidEntryPoint auf MainActivity
    - @HiltViewModel + @Inject constructor auf allen ViewModels
    - DatabaseModule stellt DAOs und Repositories als Singletons bereit

  [1 Punkt] Unit Tests
    - 6 Testklassen
    - GameViewModelTest: addScore, Bust-Erkennung, Spielabschluss, Dart löschen
    - HomeViewModelTest: startGame-Ablauf, Reihenfolge der DB-Operationen
    - StatsViewModelTest: loadStats, Sortierung, Fehlerbehandlung
    - GameRulesTest: isBust (alle 3 Fälle), initialScore, padWithZeroes
    - PlayerValidatorTest: Leer, zu kurz, zu wenige Spieler, Duplikate
    - StatsCalculatorTest: averageScore, highestScore, throwCount
    - ViewModels mit UnconfinedTestDispatcher und gemockten Repositories getestet


Architektur
-----------
  MVVM — strikte Schichtentrennung:
    Composables → ViewModel → Repository → DAO → Room DB

  Screens: HomeScreen, GameScreen, StatsScreen, WinScreen
  ViewModels: HomeViewModel, GameViewModel, StatsViewModel


Bemerkungen zur Abgabe
-----------------------
  - Die App wurde auf Android API 33+ (minSdk 33) entwickelt und getestet.
  - Alle Texte sind über strings.xml externalisiert (Lokalisierbarkeit).
  - Farben sind über Color.kt semantisch benannt (kein Hardcoding).
  - Die Datenbank verwendet fallbackToDestructiveMigration für den
    Entwicklungsbetrieb — in einer Produktionsversion würden Migrationen
    sauber implementiert.
