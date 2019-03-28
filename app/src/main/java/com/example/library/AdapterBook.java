package com.example.library;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AdapterBook extends RecyclerView.Adapter<AdapterBook.ViewHolder> {

    Context context;
    List<Books> booksList;

    //constructor for the adapter with context and booksList
    public AdapterBook(Context context, List<Books> books) {
        this.context = context;
        this.booksList = books;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_book,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Books b=booksList.get(i);
        viewHolder.bname.setText(b.getBookname());
        viewHolder.author.setText("Author: "+b.getAuthor());
        viewHolder.avail.setText("Available: "+Integer.toString(b.getAvailable()));

    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }

    //function to update the adapter
    //for the implementation of search
    public void update(List<Books> newList)
    {
        booksList=new ArrayList<>();
        booksList.addAll(newList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        TextView bname,author,avail;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            //adding book name, author,available to the recyclerView
            bname=itemView.findViewById(R.id.bname);
            author=itemView.findViewById(R.id.author);
            avail=itemView.findViewById(R.id.avail);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //acquiring position of the item
                    int pos=getAdapterPosition();
                    //fetching the Books object corresponding to the position in the recyclerView
                    final Books b =booksList.get(pos);
                    Toast.makeText(context, b.getBookname(), Toast.LENGTH_SHORT).show();

                    //For Admin
                    if(firebaseUser==null)
                    {
                        Toast.makeText(context, b.getBookname(), Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(context,BookDetail.class);
                        intent.putExtra("Books",b);
                        context.startActivity(intent);
                    }
                    //For Teachers,Students
                    else
                    {
                        Toast.makeText(context, b.getBookname(), Toast.LENGTH_SHORT).show();
                        //fetching the no of copies available for Books object b
                        if(b.getAvailable()<=0)
                        {
                            new AlertDialog.Builder(context)
                                    .setTitle("Unavailable book")
                                    .setMessage("do you want to add your name to the waiting list for this particular book : "+b.getBookname())
                                    .setPositiveButton("+Waiting list", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent=new Intent(context,WaitingList.class);
                                            intent.putExtra("Name",b.getBookname());
                                            context.startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).create().show();
                        }
                        else{
                            Intent intent=new Intent(context,BookDetail.class);
                            intent.putExtra("Books",b);
                            context.startActivity(intent);}
                    }

                    }


            });
        }


    }
}
