package tech.techbug.booksearch;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    private String mRequestUrl = "https://www.googleapis.com/books/v1/volumes?q=";
    private String mQuery ;
    private ArrayList<BookDetails> booksList = new ArrayList<>();
    private BookArrayAdapter adapter;
    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;
    private RelativeLayout resultParent;
    private TextView descriptionTV;
    private View dimBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        mQuery = intent.getStringExtra("query");
        mRequestUrl = createRequestUrl();

        resultParent = findViewById(R.id.resultParentLayout);
        dimBackground = findViewById(R.id.dim_background);

        new BookAsyncTask().execute(mRequestUrl);

    }

    private class BookAsyncTask extends AsyncTask<String, Void, BookArrayAdapter> {
        @Override
        protected void onPostExecute(final BookArrayAdapter bookArrayAdapter) {
            ListView listView = findViewById(R.id.books_list_view);
            listView.setAdapter(bookArrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    BookDetails currentBook = (BookDetails) bookArrayAdapter.getItem(position);
                    String description;
                    if(currentBook.getmDescription().equals(""))
                        description = "Description N/A";
                    else
                        description = currentBook.getmDescription();

                    layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.activity_pop_up_window, null);

                    descriptionTV = container.findViewById(R.id.description);
                    descriptionTV.setText(description);

                    popupWindow = new PopupWindow(container, 900, 450, true);
                    popupWindow.showAtLocation(resultParent, Gravity.CENTER, 0, 0);
                    dimBackground.setVisibility(View.VISIBLE);

                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            dimBackground.setVisibility(View.GONE);
                        }
                    });
                }
            });

            ProgressBar spinner = findViewById(R.id.spinner);
            spinner.setVisibility(View.GONE);
        }

        @Override
        protected BookArrayAdapter doInBackground(String... strings) {
            return new BookArrayAdapter(ResultsActivity.this, Utils.fetchDataFrom(strings[0]));
        }
    }

    String createRequestUrl() {
        return (mRequestUrl+mQuery.replaceAll(" ", "+").toLowerCase()+"&maxResults=40");
    }
}