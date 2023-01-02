package com.api.piskotnik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.api.piskotnik.model.Recipe;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchByRecipeNameActivity extends AppCompatActivity {
    String URLBase;
    private ListView lv;
    List<Map<String, String>> recipeMap = new ArrayList<Map<String, String>>();
    String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        URLBase = getString(R.string.URL_base);
        Gson gson = new Gson();
        lv = findViewById(R.id.recipe_search_list);
        Intent intent = getIntent();
        searchQuery = intent.getStringExtra("search");
        new Thread() {
            @Override
            public void run() {
                Recipe[] recipes = gson.fromJson(getRecipes(searchQuery), Recipe[].class);
                SharedPreferences prefs = getSharedPreferences("piskotnik", Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = prefs.edit();
                String recipesJson = gson.toJson(recipes);
                prefsEditor.putString("current_recipes", recipesJson);
                prefsEditor.apply();
                if (recipes != null) {
                    for (int i = 0; i < recipes.length; i++) {
                        recipeMap.add(recipes[i].parseToHashMap());
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Nothing found.",Toast.LENGTH_SHORT).show();
                }
                runOnUiThread(() -> {
                    SimpleAdapter adapter = new SimpleAdapter(SearchByRecipeNameActivity.this.getApplicationContext(),
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

    public String getRecipes(String searchQuery) {
        HashMap<String, String> map = new HashMap<>();

        SharedPreferences prefs = getSharedPreferences("piskotnik", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");
        return HttpHelper.httpGetCall(URLBase+"/recipes/name-like/"+searchQuery, token, map);
    }

    public void startRecipeDetailsActivity(View v, int i) {
        Intent intent = new Intent(SearchByRecipeNameActivity.this, RecipeDetailsActivity.class);
        intent.putExtra("recipeIndex", i);
        startActivity(intent);
    }
}