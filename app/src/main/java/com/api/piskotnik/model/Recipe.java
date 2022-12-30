package com.api.piskotnik.model;

import java.util.HashMap;
import java.util.List;

public class Recipe {
    Integer id;
    Integer userId;
    public String name;
    public String text;
    public Ingredient[] ingredients;

    public HashMap<String, String> parseToHashMap(){
        // tmp hash map for single contact
        HashMap<String, String> recipeMap = new HashMap<String, String>();

        // adding each child node to HashMap key => value
        recipeMap.put("name", this.name);
        int i = 0;
        StringBuilder ingredientsHint = new StringBuilder();
        while ((i <= this.ingredients.length - 1) && (i <= 5)) {
            ingredientsHint.append(this.ingredients[i].name);
            i++;
            if ((i <= this.ingredients.length - 1) && (i <= 5)) {
                ingredientsHint.append(", ");
            }
        }
        recipeMap.put("ingredients", String.valueOf(ingredientsHint));
        return recipeMap;
    }
}

