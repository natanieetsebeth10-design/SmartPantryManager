package com.example.smartpantrymanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 1;
    //setting tables and their variables aka columns

    //table for pantry items
    public static final String TABLE_PANTRY = "pantry_items";
    public static final String COL_PANTRY_ID = "id";
    public static final String COL_PANTRY_NAME = "name";
    public static final String COL_PANTRY_QUANTITY = "quantity";
    public static final String COL_PANTRY_UNIT = "unit";
    public static final String COL_PANTRY_EXPIRY = "expiry_date";

    //table for recipes
    public static final String TABLE_RECIPES = "recipes";
    public static final String COL_RECIPE_ID = "id";
    public static final String COL_RECIPE_NAME = "name";
    public static final String COL_RECIPE_STEPS = "steps";

    //table for recipe ingredients
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";
    public static final String COL_RI_ID = "id";
    public static final String COL_RI_RECIPE_ID = "recipe_id";
    public static final String COL_RI_NAME = "name";
    public static final String COL_RI_QUANTITY = "required_quantity";
    public static final String COL_RI_UNIT = "unit";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //oncreate runs at start
        db.execSQL("CREATE TABLE " + TABLE_PANTRY + " (" +
                COL_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PANTRY_NAME + " TEXT NOT NULL, " +
                COL_PANTRY_QUANTITY + " REAL NOT NULL, " +
                COL_PANTRY_UNIT + " TEXT, " +
                COL_PANTRY_EXPIRY + " TEXT)");//creating variable structure in SQL for said tables

        db.execSQL("CREATE TABLE " + TABLE_RECIPES + " (" +
                COL_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RECIPE_NAME + " TEXT NOT NULL, " +
                COL_RECIPE_STEPS + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                COL_RI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RI_RECIPE_ID + " INTEGER NOT NULL, " +
                COL_RI_NAME + " TEXT NOT NULL, " +
                COL_RI_QUANTITY + " REAL NOT NULL, " +
                COL_RI_UNIT + " TEXT, " +
                "FOREIGN KEY(" + COL_RI_RECIPE_ID + ") REFERENCES " + TABLE_RECIPES + "(" + COL_RECIPE_ID + "))");

        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //on upgrade is called to update to new version automatically when change is detected by developer
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        onCreate(db);
    }

    private void seedRecipes(SQLiteDatabase db) {
        insertRecipe(db, "Pap en Sous",
                "Bring water to the boil with salt. Stir in mielie meal gradually. " +
                        "Cook on low heat, stirring often, for 25-30 minutes until firm. " +
                        "Serve with the tomato and onion sous.",
                new Object[][]{
                        {"mielie meal", 2.0, "cup"},
                        {"water", 4.0, "cup"},
                        {"salt", 1.0, "tsp"},
                        {"tomatoes", 3.0, null},
                        {"onion", 1.0, null}
                });

        insertRecipe(db, "Boerewors Rolls",
                "Braai the boerewors over medium coals for 15-20 minutes, turning occasionally. " +
                        "Slice the roll, add the boerewors, top with onion and tomato relish.",
                new Object[][]{
                        {"boerewors", 500.0, "g"},
                        {"bread roll", 4.0, null},
                        {"onion", 1.0, null},
                        {"tomato", 2.0, null}
                });

        insertRecipe(db, "Chakalaka",
                "Fry onion and garlic until soft. Add grated carrot, peppers and baked beans. " +
                        "Season with curry powder and chilli. Simmer 10 minutes.",
                new Object[][]{
                        {"onion", 1.0, null},
                        {"garlic", 2.0, "clove"},
                        {"carrot", 2.0, null},
                        {"green pepper", 1.0, null},
                        {"baked beans", 1.0, "can"},
                        {"curry powder", 1.0, "tsp"}
                });

        insertRecipe(db, "Bobotie",
                "Fry onion and garlic, add minced beef and curry powder, brown well. " +
                        "Stir in bread soaked in milk and raisins. Bake with an egg custard topping at 180C for 30 minutes.",
                new Object[][]{
                        {"minced beef", 500.0, "g"},
                        {"onion", 1.0, null},
                        {"garlic", 2.0, "clove"},
                        {"curry powder", 2.0, "tsp"},
                        {"bread", 2.0, "slice"},
                        {"milk", 1.0, "cup"},
                        {"eggs", 2.0, null},
                        {"raisins", 0.5, "cup"}
                });

        insertRecipe(db, "Vetkoek en Mince",
                "Fry onion and garlic, brown the minced beef with curry powder. " +
                        "Deep fry the vetkoek dough balls until golden. Slice open and fill with mince.",
                new Object[][]{
                        {"flour", 3.0, "cup"},
                        {"yeast", 1.0, "tsp"},
                        {"minced beef", 400.0, "g"},
                        {"onion", 1.0, null},
                        {"garlic", 2.0, "clove"},
                        {"curry powder", 1.0, "tsp"}
                });

        insertRecipe(db, "Sosaties",
                "Marinate cubed lamb in a mix of curry powder, apricot jam, onion and garlic overnight. " +
                        "Thread onto skewers with onion and dried apricots. Braai over medium coals.",
                new Object[][]{
                        {"lamb", 600.0, "g"},
                        {"onion", 2.0, null},
                        {"garlic", 2.0, "clove"},
                        {"curry powder", 1.0, "tsp"},
                        {"apricot jam", 2.0, "tbsp"},
                        {"dried apricots", 8.0, null}
                });

        insertRecipe(db, "Potjiekos",
                "Brown the beef in the potjie pot. Layer in onion, carrot and potato without stirring. " +
                        "Add stock, cover and simmer slowly for 2-3 hours over low coals.",
                new Object[][]{
                        {"beef", 600.0, "g"},
                        {"onion", 1.0, null},
                        {"carrot", 3.0, null},
                        {"potato", 4.0, null},
                        {"beef stock", 2.0, "cup"}
                });

        insertRecipe(db, "Braaibroodjies",
                "Butter the outside of the bread slices. Fill with cheese, tomato and onion. " +
                        "Grill over the coals in a toasted sandwich grid until golden on both sides.",
                new Object[][]{
                        {"bread", 4.0, "slice"},
                        {"cheese", 100.0, "g"},
                        {"tomato", 1.0, null},
                        {"onion", 0.5, null},
                        {"butter", 2.0, "tbsp"}
                });

        insertRecipe(db, "Tamatiesmoor",
                "Fry onion until soft. Add chopped tomatoes and a pinch of sugar. " +
                        "Simmer until it breaks down into a thick relish. Season with salt.",
                new Object[][]{
                        {"tomatoes", 4.0, null},
                        {"onion", 1.0, null},
                        {"sugar", 1.0, "tsp"},
                        {"salt", 0.5, "tsp"}
                });

        insertRecipe(db, "Melktert",
                "Make a shortcrust base and blind bake. Whisk milk, eggs, sugar, flour and cinnamon " +
                        "over heat until thickened into a custard. Pour into the base and chill.",
                new Object[][]{
                        {"milk", 3.0, "cup"},
                        {"eggs", 3.0, null},
                        {"sugar", 0.75, "cup"},
                        {"flour", 3.0, "tbsp"},
                        {"cinnamon", 1.0, "tsp"},
                        {"flour", 1.5, "cup"},
                        {"butter", 125.0, "g"}
                });

        insertRecipe(db, "Koeksisters",
                "Make a stiff dough, plait into small braids and deep fry until golden. " +
                        "Dip immediately into ice-cold sugar syrup infused with cinnamon and ginger.",
                new Object[][]{
                        {"flour", 4.0, "cup"},
                        {"sugar", 2.0, "cup"},
                        {"cinnamon", 1.0, "tsp"},
                        {"ginger", 1.0, "tsp"},
                        {"water", 2.0, "cup"}
                });

        insertRecipe(db, "Milk Tart Rusks",
                "Combine flour, sugar and butter into a crumbly dough. Add eggs and buttermilk, " +
                        "shape into a loaf, bake, then slice and dry out slowly in a low oven overnight.",
                new Object[][]{
                        {"flour", 5.0, "cup"},
                        {"sugar", 1.0, "cup"},
                        {"butter", 250.0, "g"},
                        {"eggs", 2.0, null},
                        {"buttermilk", 1.0, "cup"}
                });

        insertRecipe(db, "Biltong Salad",
                "Toss lettuce, tomato and onion together. Shave biltong thinly over the top. " +
                        "Finish with a simple oil and vinegar dressing.",
                new Object[][]{
                        {"biltong", 100.0, "g"},
                        {"lettuce", 1.0, "head"},
                        {"tomato", 2.0, null},
                        {"onion", 0.5, null}
                });

        insertRecipe(db, "Pampoenkoekies",
                "Mash cooked pumpkin and mix with flour, sugar, egg and a pinch of cinnamon. " +
                        "Fry spoonfuls in a pan until golden on both sides. Dust with cinnamon sugar.",
                new Object[][]{
                        {"pumpkin", 500.0, "g"},
                        {"flour", 1.0, "cup"},
                        {"sugar", 2.0, "tbsp"},
                        {"eggs", 1.0, null},
                        {"cinnamon", 1.0, "tsp"}
                });

        insertRecipe(db, "Groenboontjiebredie",
                "Brown the lamb with onion and garlic. Add potato and green beans, " +
                        "cover with stock and simmer slowly until the meat is tender.",
                new Object[][]{
                        {"lamb", 500.0, "g"},
                        {"onion", 1.0, null},
                        {"garlic", 2.0, "clove"},
                        {"potato", 3.0, null},
                        {"green beans", 300.0, "g"},
                        {"beef stock", 1.0, "cup"}
                });

        insertRecipe(db, "Beskuit",
                "Mix flour, sugar, buttermilk, eggs and melted butter into a soft dough. " +
                        "Bake as a tray of buns, break apart, then dry out in a low oven until crisp.",
                new Object[][]{
                        {"flour", 6.0, "cup"},
                        {"sugar", 1.0, "cup"},
                        {"buttermilk", 2.0, "cup"},
                        {"eggs", 2.0, null},
                        {"butter", 250.0, "g"}
                });

        insertRecipe(db, "Curried Mince and Rice",
                "Fry onion and garlic, brown the minced beef with curry powder and raisins. " +
                        "Simmer with a little stock, serve over cooked rice.",
                new Object[][]{
                        {"minced beef", 500.0, "g"},
                        {"onion", 1.0, null},
                        {"garlic", 2.0, "clove"},
                        {"curry powder", 2.0, "tsp"},
                        {"raisins", 0.25, "cup"},
                        {"rice", 2.0, "cup"}
                });
    }

    private void insertRecipe(SQLiteDatabase db, String name, String steps, Object[][] ingredients) {
        ContentValues recipeValues = new ContentValues();
        recipeValues.put(COL_RECIPE_NAME, name);
        recipeValues.put(COL_RECIPE_STEPS, steps);
        long recipeId = db.insert(TABLE_RECIPES, null, recipeValues);

        for (Object[] ingredient : ingredients) {
            ContentValues ingredientValues = new ContentValues();
            ingredientValues.put(COL_RI_RECIPE_ID, recipeId);
            ingredientValues.put(COL_RI_NAME, (String) ingredient[0]);
            ingredientValues.put(COL_RI_QUANTITY, (Double) ingredient[1]);
            ingredientValues.put(COL_RI_UNIT, (String) ingredient[2]); // can be null
            db.insert(TABLE_RECIPE_INGREDIENTS, null, ingredientValues);
        }
    }
}