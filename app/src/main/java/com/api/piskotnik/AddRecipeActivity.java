package com.api.piskotnik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.api.piskotnik.model.Ingredient;
import com.api.piskotnik.model.Recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddRecipeActivity extends AppCompatActivity {

    EditText recipeNameInput;
    EditText recipeTextInput;
    EditText ingredientNameInput;
    EditText ingredientAmountInput;
    EditText ingredientUnitInput;
    List<Ingredient> ingredients = new ArrayList<>();
    List<Map<String, String>> ingredientsMap = new ArrayList<Map<String, String>>();
    private ListView lv;
    String URLBase;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        URLBase = getString(R.string.URL_base);
        recipeNameInput = findViewById(R.id.new_recipe_name);
        recipeTextInput = findViewById(R.id.new_recipe_text);
        ingredientNameInput = findViewById(R.id.new_recipe_ingredient_name);
        ingredientAmountInput = findViewById(R.id.new_recipe_ingredient_amount);
        ingredientUnitInput = findViewById(R.id.new_recipe_ingredient_unit);

        SharedPreferences prefs = getSharedPreferences("piskotnik", Context.MODE_PRIVATE);
        token = prefs.getString("token", "");
    }

    public void addIngredient(View v) {
        String ingredientName = String.valueOf(ingredientNameInput.getText());
        String ingredientAmount = String.valueOf(ingredientAmountInput.getText());
        String ingredientUnit = String.valueOf(ingredientUnitInput.getText());

        Ingredient ingredient = new Ingredient();
        ingredient.name = ingredientName;
        ingredient.amount = Integer.parseInt(ingredientAmount);
        if (!ingredientUnit.equals("")) {
            ingredient.unit = ingredientUnit;
        } else {
            ingredient.unit = "units";
        }
        ingredients.add(ingredient);
        ingredientsMap.add(ingredient.parseToHashMap());

        lv = findViewById(R.id.new_recipe_ingredients);
        SimpleAdapter adapter = new SimpleAdapter(this,
                ingredientsMap,
                R.layout.ingredient_list_item,
                new String[] {"name", "amount", "unit"},
                new int[] {R.id.ingredient_list_name, R.id.ingredient_list_amount, R.id.ingredient_list_unit}
        );

        lv.setAdapter(adapter);

        ingredientNameInput.getText().clear();
        ingredientAmountInput.getText().clear();
        ingredientUnitInput.getText().clear();

    }

    public void addRecipe(View v) {
        String recipeName = String.valueOf(recipeNameInput.getText());
        String recipeText = String.valueOf(recipeTextInput.getText());

        Recipe recipe = new Recipe();
        recipe.name = recipeName;
        recipe.text = recipeText;

        Ingredient[] ingredientsArray = new Ingredient[ingredients.size()];
        ingredients.toArray(ingredientsArray);
        recipe.ingredients = ingredientsArray;


        new Thread(() -> HttpHelper.httpPostCall(URLBase + "/recipes", token, recipe)).start();
        Intent intent = new Intent(AddRecipeActivity.this, ListRecipesActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.back_btn:
                this.onBackPressed();
                return true;
            case R.id.home_btn:
                Intent intent = new Intent(AddRecipeActivity.this, ListRecipesActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}