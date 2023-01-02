package com.api.piskotnik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.api.piskotnik.model.Recipe;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecipeDetailsActivity extends AppCompatActivity {

    int recipeIndex;
    private ListView lv;
    Recipe recipe;
    List<Map<String, String>> ingredientsMap = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        Intent intent = getIntent();
        recipeIndex = intent.getIntExtra("recipeIndex", 0);
        SharedPreferences prefs = getSharedPreferences("piskotnik", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("current_recipes", "[]");
        Recipe[] programs = gson.fromJson(json, Recipe[].class);
        recipe = programs[recipeIndex];
        ((TextView) findViewById(R.id.recipe_details_name)).setText(recipe.name);
        ((TextView) findViewById(R.id.recipe_details_text)).setText(recipe.text);

        for (int i = 0; i < recipe.ingredients.length; i++) {
            ingredientsMap.add(recipe.ingredients[i].parseToHashMap());
        }
        lv = findViewById(R.id.recipe_details_ingredients);
    }

    @Override
    protected void onStart(){
        super.onStart();

        SimpleAdapter adapter = new SimpleAdapter(this,
                ingredientsMap,
                R.layout.ingredient_list_item,
                new String[] {"name", "amount", "unit"},
                new int[] {R.id.ingredient_list_name, R.id.ingredient_list_amount, R.id.ingredient_list_unit}
        );

        lv.setAdapter(adapter);
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
                Intent intent = new Intent(RecipeDetailsActivity.this, ListRecipesActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}