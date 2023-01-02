package com.api.piskotnik;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class SearchRecipesActivity extends AppCompatActivity {
    EditText searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_recipes);
        searchInput= findViewById(R.id.search_bar);
    }

    public void startSearchByRecipeName(View v) {
        String searchQuery = String.valueOf(searchInput.getText());
        Intent intent = new Intent(SearchRecipesActivity.this, SearchByRecipeNameActivity.class);
        intent.putExtra("search", searchQuery);
        startActivity(intent);
    }

    public void startSearchByIngredient(View v) {
        String searchQuery = String.valueOf(searchInput.getText());
        Intent intent = new Intent(SearchRecipesActivity.this, SearchByIngredientActivity.class);
        intent.putExtra("search", searchQuery);
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
                Intent intent = new Intent(SearchRecipesActivity.this, ListRecipesActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}