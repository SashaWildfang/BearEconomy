# BearEconomy

A lightweight economy for the **Bear Nation** network. It handles player balances, payments, admin tools and a wealth leaderboard, with PlaceholderAPI support. BearShop and BearPrison are built on it.

![Paper](https://img.shields.io/badge/Paper-26.1.2-blue) ![Java](https://img.shields.io/badge/Java-25-orange) ![Build](https://img.shields.io/badge/build-Gradle-02303A)

## Features

- Player balances, with `/pay` between players
- `/balancetop` leaderboard with pages
- Admin give / take / set / reset tools
- **PlaceholderAPI expansion** for showing balances in scoreboards, tab lists and chat
- A simple API that other Bear Nation plugins depend on

## Commands

| Command | Aliases | Description | Permission |
|---|---|---|---|
| `/balance [player]` | `bal` | Check your balance or another player's | – |
| `/pay <player> <amount>` | – | Send money to another player | – |
| `/balancetop [page]` | `baltop` | The wealthiest players on the server | – |
| `/eco <give\|take\|set\|reset> <player> [amount]` | – | Change a player's balance | `bn.beareconomy.admin` (op) |

## Requirements

- Paper 26.1.2+
- Java 25
- [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) (optional, for placeholders)

## Building

```bash
./gradlew build
```

The plugin jar goes to `build/libs/`.

## Project layout

```
src/main/java/com/sasha/beareconomy/
├── BearEconomy.java     # Plugin entry point and API access
├── commands/            # /bal, /pay, /baltop, /eco
├── hooks/               # PlaceholderAPI expansion
└── managers/            # Balance storage and logic
```

---

Part of the Bear Nation plugin suite.
