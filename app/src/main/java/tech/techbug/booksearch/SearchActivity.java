package tech.techbug.booksearch;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final TextInputEditText searchBoxEditTV = findViewById(R.id.search_edit_text_view);
        Button searchButton = findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = searchBoxEditTV.getText().toString();
                if (query.equals("")) {
                    Toast.makeText(SearchActivity.this, "Enter Something", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent resultIntent = new Intent(SearchActivity.this, ResultsActivity.class);
                resultIntent.putExtra("query", query);
                startActivity(resultIntent);
            }
        });
    }
}
