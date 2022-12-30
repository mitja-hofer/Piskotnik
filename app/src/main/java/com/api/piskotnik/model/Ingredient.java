package com.api.piskotnik.model;

import java.util.HashMap;

public class Ingredient {
    Integer id;
    Integer recipeId;
    String name;
    Integer amount;
    String unit;

    public HashMap<String, String> parseToHashMap(){
        // tmp hash map for single contact
        HashMap<String, String> recipeMap = new HashMap<String, String>();

        // adding each child node to HashMap key => value
        recipeMap.put("name", this.name);
        recipeMap.put("amount", this.amount.toString());
        recipeMap.put("unit", this.unit);
        return recipeMap;
    }
}
