package tech.techbug.booksearch;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
    private String mQuery;
    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;
    private RelativeLayout resultParent;
    private TextView descriptionTV;
    private View dimBackground;
    private Button buyButton;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Intent intent = getIntent();
        mQuery = intent.getStringExtra("query");
        mRequestUrl = createRequestUrl();

        resultParent = findViewById(R.id.resultParentLayout);
        dimBackground = findViewById(R.id.dim_background);
        spinner = findViewById(R.id.spinner);

        //Checking networking connectivity and addressing about it to user if necessary
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new BookAsyncTask().execute(mRequestUrl);
        } else {
            TextView internetConnectionTV = findViewById(R.id.internet_connection);
            spinner.setVisibility(View.GONE);
            internetConnectionTV.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Private inner class to do networking task in background thread
     * while UI related task in UI thread i.e. main thread
     */
    private class BookAsyncTask extends AsyncTask<String, Void, BookArrayAdapter> {
        @Override
        protected void onPostExecute(final BookArrayAdapter bookArrayAdapter) {

            if(bookArrayAdapter == null) {
                TextView noMatch = findViewById(R.id.no_book_found);
                spinner.setVisibility(View.GONE);
                noMatch.setVisibility(View.VISIBLE);
                return;
            }
            //Setting bookAdapter in listView
            ListView listView = findViewById(R.id.books_list_view);
            listView.setAdapter(bookArrayAdapter);

            //On Click Listener for every item in ListView
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                    //Getting the book details object of clicked item
                    BookDetails currentBook = (BookDetails) bookArrayAdapter.getItem(position);

                    //Getting description and information link of clicked book
                    final String description, infoUrl;
                    if (currentBook.getmDescription().equals(""))
                        description = "Description N/A";
                    else
                        description = currentBook.getmDescription();
                    infoUrl = currentBook.getmInfoUrl();

                    //Creating layout inflater object and container view for popup window
                    layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.activity_pop_up_window, null);

                    //Setting description to text view and info link to buy button
                    descriptionTV = container.findViewById(R.id.description);
                    descriptionTV.setText(description);
                    buyButton = container.findViewById(R.id.buy_button);
                    buyButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(infoUrl));
                            startActivity(browserIntent);
                        }
                    });

                    //Inflating the popup activity and showing light black background behind it
                    //plus making background invisible too as the popup gets dismissed
                    popupWindow = new PopupWindow(container, 900, 900, true);
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

            //Handling spinners visibility as whole data gets loaded
            spinner.setVisibility(View.GONE);
        }

        @Override
        protected BookArrayAdapter doInBackground(String... strings) {
            ArrayList<BookDetails> bookList = Utils.fetchDataFrom(strings[0]);
            if (bookList.size() != 0)
                return new BookArrayAdapter(ResultsActivity.this, bookList);
            else
                return null;
        }
    }

    /**
     * This method creates the request url from the search string given by user
     * @return the Request URL in form of string
     */
    String createRequestUrl() {
        return (mRequestUrl + mQuery.replaceAll(" ", "+").toLowerCase() + "&maxResults=40");
    }
}