package tech.techbug.booksearch;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by noblegas on 1/4/18.
 */

public final class Utils {

    public static ArrayList<BookDetails> fetchDataFrom(String requestUrl) {
        ArrayList<BookDetails> booksList;

        URL url = createUrl(requestUrl);
        String JSONresponse = makeHttpRequest(url);
        booksList = parseJSONinJava(JSONresponse);

        return booksList;
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e("createUrl", "Exception thrown while creating URL object", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) {
        HttpURLConnection urlConnection;
        InputStream inputStream;
        String JSONresponse = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(15000 /*milliseconds*/);
            urlConnection.setReadTimeout(10000 /*milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                JSONresponse = readInputStream(inputStream);
            }
        } catch (IOException e) {
            Log.e("makeHttpRequest", "Exception thrown while doing connection stuff", e);
        }
        return JSONresponse;
    }

    private static String readInputStream(InputStream inputStream) {
        StringBuilder jsonResponse = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.defaultCharset());
        BufferedReader reader = new BufferedReader(inputStreamReader);

        try {
            String line = reader.readLine();
            while (line != null) {
                jsonResponse.append(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            Log.e("readInputStream", "Exception thrown while reader Buffer", e);
        }

        return jsonResponse.toString();
    }

    private static ArrayList<BookDetails> parseJSONinJava(String jsonResponse) {
        ArrayList<BookDetails> bookList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonResponse);
            JSONArray items = root.getJSONArray("items");

            for (int i = 0; i < items.length(); ++i) {
                JSONObject item = items.getJSONObject(i);
                JSONObject volumeInfo = item.optJSONObject("volumeInfo");
                JSONArray authors = volumeInfo.optJSONArray("authors");
                JSONObject imageUrls = volumeInfo.optJSONObject("imageLinks");

                if (imageUrls == null)
                    bookList.add(new BookDetails(volumeInfo.optString("title"), getAuthorList(authors),
                            volumeInfo.optString("description"), volumeInfo.optString("pageCount"),
                            null, volumeInfo.optString("infoLink")));
                else
                    bookList.add(new BookDetails(volumeInfo.optString("title"), getAuthorList(authors),
                            volumeInfo.optString("description"), volumeInfo.optString("pageCount"),
                            imageUrls.optString("smallThumbnail"), volumeInfo.optString("infoLink")));
            }
        } catch (JSONException e) {
            Log.e("parseJSONinJava", "Problem creating java objects from json.", e);
        }
        return bookList;
    }

    private static String getAuthorList(JSONArray authors) {
        StringBuilder author = new StringBuilder();

        if (authors == null)
            return author.append("Information N/A").toString();

        try {
            for (int i = 0; i < authors.length(); ++i) {
                if (i == authors.length() - 1)
                    author.append(authors.getString(i));
                else
                    author.append(authors.getString(i)).append(", ");
            }
        } catch (JSONException e) {
            Log.e("createIAuthorList", "Problem creating author", e);
        }

        return author.toString();
    }
}
