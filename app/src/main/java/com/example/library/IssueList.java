package com.example.library;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class IssueList extends  ArrayAdapter<Taken> {
    private Activity context;
    private List<Taken> booksList;

    public IssueList(Activity context, List<Taken> booksList) {
        super(context, R.layout.activity_issue_list, booksList);
        this.context = context;
        this.booksList = booksList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_issue_list, null, true);
        TextView name = (TextView) listViewItem.findViewById(R.id.textViewname);
        //TextView id = (TextView) listViewItem.findViewById(R.id.textViewid);
        TextView author = (TextView) listViewItem.findViewById(R.id.textViewIssueDate);
        TextView available = (TextView) listViewItem.findViewById(R.id.textViewReturnDate);
TextView email=(TextView)listViewItem.findViewById(R.id.textViewEmail);

        Taken taken = booksList.get(position);
        name.setText("Name:"+taken.getBookname());
        available.setText("Issuedate:" + taken.getIssue_date());

        author.setText("Expected Return date:" + taken.getExpected_date());
        email.setText("Email id:"+taken.getUseremail());



        return listViewItem;
    }
}