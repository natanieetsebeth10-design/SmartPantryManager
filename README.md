# Smart Pantry Manager

A Java Android app that reduces food waste by suggesting recipes you can cook
using *only* the ingredients already in your pantry. A recipe is suggested only
if every ingredient is present in at least the required quantity.

## Features
- Add, edit and delete pantry items (name, quantity, unit, optional expiry date)
- 18 pre-loaded South African staple recipes (seeded on first run)
- Strict-matching Suggested Recipes screen, robust to singular/plural names
  ("tomato" vs "tomatoes") and unit differences (tsp vs ml, kg vs g)
- Optional "Almost There" list for recipes missing exactly one ingredient
- Recipe detail screen with ingredients and method
- Settings: highlight ingredients expiring within 3 days

## Database: SQLite (SQLiteOpenHelper)
Chosen because it is local, needs no network or account, persists between
sessions, and suits a small relational model (pantry items, recipes, recipe
ingredients). Matching is done in Java so the normalisation logic is testable.

## Setup and run
1. Clone the repository: `git clone https://github.com/natanieetsebeth10-design/SmartPantryManager`
2. Open the project in Android Studio (Java, minimum SDK 24)
3. Let Gradle sync, then run on an emulator or physical device (Run > Run 'app')

## Author
Natanie Etsebeth, 402309831 - Mobile App Development 700