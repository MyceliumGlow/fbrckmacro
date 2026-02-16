# Fbrck Macro (Fabric Client Mod)

A Fabric **client-side** macro mod for Java 21 that adds:

- A configurable keybind to toggle auto-clicking.
- A client command to change click delay (`/macro set <delayMs>`).
- Optional setting to click only while holding a sword/trident (`/macro weaponOnly <true|false>`).
- Optional **hold-to-click** mode so clicks only happen while you physically hold left click (`/macro holdToClick <true|false>`).

> This project is configured for **Minecraft 1.21.11** (Fabric Loader + Fabric API). This repo is already pinned for 1.21.11. If an artifact is temporarily unavailable, bump the version values in `gradle.properties` to the latest published ones for 1.21.11.

## Features

- **Controls menu integration**
  - Keybind: `Toggle Auto Clicker`
  - Category: `Fbrck Macro`
- **Commands (client-side)**
  - `/macro` shows current state
  - `/macro toggle`
  - `/macro set <delayMs>` (1 to 2000 ms)
  - `/macro weaponOnly <true|false>`
  - `/macro holdToClick <true|false>`
- **Persistent config**
  - Saved to `config/fbrckmacro.json`

## Step-by-step build tutorial (no Fabric experience required)

### 1) Install prerequisites

1. Install **Java Development Kit (JDK) 21**.
2. Install **Git**.
3. Install **Gradle 8.2+** (or generate a wrapper that uses 8.2+).
4. (Optional but recommended) Install IntelliJ IDEA Community Edition.

Check Java:

```bash
java -version
```

You should see Java 21.

Check Gradle:

```bash
gradle --version
```

You should see **Gradle 8.2 or newer**.

### 2) Get this project

```bash
git clone <your-repo-url>
cd fbrckmacro
```

### 3) Generate Gradle wrapper (first time only)

If `gradlew` is not present yet:

```bash
gradle wrapper
```

Then use wrapper commands (`./gradlew ...`) after that.

### 4) Build the mod

```bash
./gradlew build
```

If successful, your jar is created at:

- `build/libs/fbrckmacro-1.0.0.jar`

### 5) Run Minecraft from dev environment (optional)

```bash
./gradlew runClient
```

This starts a dev client with your mod loaded.

### 6) Install the mod into normal Minecraft

1. Install Fabric Loader for your target MC version.
2. Install Fabric API in the `mods` folder.
3. Copy this mod jar from `build/libs/` into your `.minecraft/mods` folder.
4. Launch the Fabric profile.

### 7) Use in game

1. Open **Options → Controls → Key Binds → Fbrck Macro**.
2. Change `Toggle Auto Clicker` key if you want.
3. Join a world/server.
4. Press your macro toggle key to enable/disable auto clicking.
5. Set delay with:

```mcfunction
/macro set 75
```

Example: `75` means one click every 75 milliseconds.

### 8) Tune settings

- Enable hold-to-click mode (macro must be ON, and you must hold left click):

```mcfunction
/macro holdToClick true
```

- Disable hold-to-click mode:

```mcfunction
/macro holdToClick false
```

- Enable weapon-only mode:

```mcfunction
/macro weaponOnly true
```

- Disable weapon-only mode:

```mcfunction
/macro weaponOnly false
```

### 9) Troubleshooting

- **Command not found**: ensure Fabric API is installed and you are on Fabric profile.
- **Jar not loading**: verify Java 21 and matching Minecraft/Fabric versions.
- **Unsupported class file major version 69**: you are running Gradle on Java 25; switch Gradle to JDK 21 (`JAVA_HOME` -> JDK 21) before building.
- **Wrong game version**: update values in `gradle.properties`, then rebuild.
- **Loom/Gradle API error** (`Problems.forNamespace`): your Gradle is too old for the Loom version in this project. Upgrade to **Gradle 8.2+** and use JDK 21 for Gradle itself. If needed, run: `gradle wrapper --gradle-version 8.14.3`.

## Notes

- This is a client automation mod. Always follow server rules before using macros/auto-clickers.
