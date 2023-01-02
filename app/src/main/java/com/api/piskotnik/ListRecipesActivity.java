package com.api.piskotnik;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.api.piskotnik.model.Recipe;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListRecipesActivity extends AppCompatActivity {
    String URLBase;
    private ListView lv;
    List<Map<String, String>> recipeMap = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_recipes);
        URLBase = getString(R.string.URL_base);
        Gson gson = new Gson();
        lv = findViewById(R.id.recipe_list);
        new Thread() {
            @Override
            public void run() {
                Recipe[] recipes = gson.fromJson(getRecipes(), Recipe[].class);
                SharedPreferences prefs = getSharedPreferences("piskotnik", Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = prefs.edit();
                String recipesJson = gson.toJson(recipes);
                prefsEditor.putString("current_recipes", recipesJson);
                prefsEditor.apply();
                for (int i = 0; i < recipes.length; i++) {
                    recipeMap.add(recipes[i].parseToHashMap());
                }
                runOnUiThread(() -> {
                    SimpleAdapter adapter = new SimpleAdapter(ListRecipesActivity.this.getApplicationContext(),
                        recipeMap,
                        R.layout.recipe_list_item,
                        new String[] {"name", "ingredients"},
                        new int[] {R.id.recipe_list_name, R.id.recipe_text}
                    );
                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener((adapterView, view, i, l) -> startRecipeDetailsActivity(view, i));
                });
            }
        }.start();
    }

    public String getRecipes() {
        HashMap<String, String> map = new HashMap<>();

        SharedPreferences prefs = getSharedPreferences("piskotnik", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");
        return HttpHelper.httpGetCall(URLBase+"/recipes", token, map);
    }

    public void startRecipeDetailsActivity(View v, int i) {
        Intent intent = new Intent(ListRecipesActivity.this, RecipeDetailsActivity.class);
        intent.putExtra("recipeIndex", i);
        startActivity(intent);
    }

    public void startSearchRecipesActivity(View v) {
        Intent intent = new Intent(ListRecipesActivity.this, SearchRecipesActivity.class);
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
                return true;
            case R.id.home_btn:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}