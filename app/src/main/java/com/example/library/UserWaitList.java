package com.example.library;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class UserWaitList extends ArrayAdapter<AdminWait> {


    private Activity context;
    private List<AdminWait> booksList;
    ArrayList<AdminWait> arrayList;


    public UserWaitList(Activity context, List<AdminWait> booksList) {
        super(context, R.layout.user_wait_list_layout, booksList);
        this.context = context;
        this.booksList = booksList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.user_wait_list_layout, null, true);
        TextView name = (TextView) listViewItem.findViewById(R.id.textView);
        //TextView id = (TextView) listViewItem.findViewById(R.id.textViewid);
        TextView author = (TextView) listViewItem.findViewById(R.id.textViewDate);
        TextView available = (TextView) listViewItem.findViewById(R.id.textViewWaitNo);


        AdminWait adminWait = booksList.get(position);
        name.setText(adminWait.getBookname());
        available.setText("Date:" + adminWait.getDate());

        author.setText("Wait No:" + adminWait.getWaitno());

        return listViewItem;
    }

//        final Books b=(Books)this.getItem(position);
//
//        context.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openDetailActivity(b.getBookname(),b.getAuthor(),b.getAvailable());
//
//            }
//        });
//        //imageViewBook.setImageURI(Uri.parse("https://firebasestorage.googleapis.com/v0/b/library-d2641.appspot.com/o/red%20circle.png?alt=media&token=6df71670-67b8-4f36-a4ec-fb0f0acce78b"));
//        //return listViewItem;
//        return convertView;
//    }
//
//    public void openDetailActivity(String name,String author,int avail)
//    {
//        Intent i =new Intent(context,UserBook.class);
//        i.putExtra("Name",name);
//        i.putExtra("Author",author);
//        i.putExtra("Available",avail);
//
////        context.startActivity(i);
//
//        new AlertDialog.Builder(context)
//                .setTitle("Issue")
//                .setMessage("Are you sure u want to issue this book")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .setNegativeButton("No",null)
//                .setIcon(R.drawable.common_full_open_on_phone)
//                .show();
//
//    }
}
