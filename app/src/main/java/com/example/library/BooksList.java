package com.example.library;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;
public class BooksList extends ArrayAdapter<Books> implements Filterable {


    private Activity context;
    private List<Books> booksList;

    ArrayList<Books> arrayList, templist;


    public BooksList(Activity context, List<Books> booksList) {
        super(context, R.layout.books_list_layout, booksList);
        this.context = context;
        this.booksList = booksList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.books_list_layout, null, true);
        TextView name = (TextView) listViewItem.findViewById(R.id.textViewname);
        //TextView id = (TextView) listViewItem.findViewById(R.id.textViewid);
        TextView author = (TextView) listViewItem.findViewById(R.id.textViewAuthor);
        TextView available = (TextView) listViewItem.findViewById(R.id.textViewAvailable);


        Books books = booksList.get(position);
        name.setText(books.getBookname());
        available.setText("Available:" + books.getAvailable());

        author.setText("Author:" + books.getAuthor());

        return listViewItem;
    }

//
//    @Override
//    public Filter getFilter() {
//        return super.getFilter();
//    }
//
//    class customfilter extends Filter
//    {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            if(constraint!=null&&constraint.length()>0)
//            {
//                constraint=constraint.toString().toUpperCase();
//            }
//            ArrayList<Books> filters= new ArrayList<>();
//            for(int i=0;i<templist.size();i++)
//            {
//                if(templist.get(i).getBookname().contains(constraint))
//                {
//                    B
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//
//        }
//    }
}
