package tech.techbug.booksearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by noblegas on 1/4/18.
 */

public class BookArrayAdapter extends ArrayAdapter {

    public BookArrayAdapter(@NonNull Context context, @NonNull List<BookDetails> bookDetailsList) {
        super(context, 0, bookDetailsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_list_item, parent, false);
        }

        BookDetails currentBook = (BookDetails) getItem(position);

        TextView title = listItemView.findViewById(R.id.title);
        TextView authors = listItemView.findViewById(R.id.authors);
        TextView pageCount = listItemView.findViewById(R.id.page_count);
        TextView pages = listItemView.findViewById(R.id.pages_text);
        ImageView bookCover = listItemView.findViewById(R.id.book_cover);

        if (!currentBook.getmTitle().equals(""))
            title.setText(currentBook.getmTitle());
        else
            title.setText(R.string.title_na_text);

        authors.setText(currentBook.getmAuthor());

        if (!currentBook.getmPageCount().equals(""))
            pageCount.setText(currentBook.getmPageCount());
        else {
            pageCount.setText("");
            pages.setText(R.string.count_na_text);
        }

        if(currentBook.getmThumbnailUrl() != null) {
            Picasso.get().load(currentBook.getmThumbnailUrl()).into(bookCover);
        } else
            bookCover.setImageResource(R.drawable.default_book_cover);

        return listItemView;
    }
}
