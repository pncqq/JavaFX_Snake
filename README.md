# 🐍 JavaFX_Snake

Klasyczna gra **Snake** zrealizowana z wykorzystaniem biblioteki **JavaFX**. Prosty, zabawny projekt demonstrujący użycie GUI i podstaw animacji w Javie.

## 📂 Zawartość repozytorium

- `src/` – kod źródłowy gry
- `pom.xml` – plik konfiguracyjny projektu (JavaFX + Maven)
- `.idea/` – konfiguracja IDE (IntelliJ)

## ⚙️ Technologie

- Java 17+
- JavaFX
- Maven (opcjonalnie)

## 🤖 Funkcjonalności

- Gra Snake z kontrolą za pomocą klawiszy strzałek
- System punktacji
- Prosty system kolizji (ściany i ciało węża)
- Losowe generowanie jedzenia
- Dynamiczne wydłużanie węża

## ▶️ Jak uruchomić

1. Upewnij się, że masz zainstalowaną Javę (17+) oraz JavaFX.
2. Sklonuj repozytorium:
```bash
git clone https://github.com/pncqq/JavaFX_Snake.git
cd JavaFX_Snake
```
3. Otwórz projekt w IntelliJ IDEA (lub innym IDE wspierającym JavaFX).
4. Uruchom klasę główną w folderze `src`.

> 🔎 Jeśli używasz JDK 11+, pamiętaj o dodaniu modułów JavaFX do VM options, np.:
```
--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
```

## 👨‍💼 Autor
**Filip Michalski**  
Projekt wykonany jako przykład użycia JavaFX do stworzenia prostej gry 2D z GUI i logiką czasu rzeczywistego.
