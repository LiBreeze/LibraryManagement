package com.example.library;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.ViewHolder>{
    RecyclerView recyclerView;
    Context context;
    ArrayList<String> items=new ArrayList<>();
    ArrayList<String> uid1=new ArrayList<>();
    ArrayList<String> email1=new ArrayList<>();
    ArrayList<String> sapid1=new ArrayList<>();


    public void update(String uid,String name,String email,String sapid)
    {
        items.add(name);
        uid1.add(uid);
        email1.add(email);
        sapid1.add(sapid);
        notifyDataSetChanged();
    }

    public AdapterUsers(RecyclerView recyclerView, Context context, ArrayList<String> items,ArrayList<String> sapid1,ArrayList<String> email1,ArrayList<String> uid1) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.items = items;
        this.sapid1=sapid1;
        this.email1=email1;
        this.uid1=uid1;

    }

    @NonNull
    @Override
    public AdapterUsers.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_users,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterUsers.ViewHolder viewHolder, int i) {
        viewHolder.name.setText(items.get(i));
        viewHolder.email.setText(email1.get(i));
        viewHolder.sapid.setText(sapid1.get(i));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        TextView sapid;
        TextView email;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            sapid=itemView.findViewById(R.id.sapid);
            email=itemView.findViewById(R.id.email);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=recyclerView.getChildLayoutPosition(v);
                    Intent intent=new Intent(itemView.getContext(),UpdateAccount.class);
                    intent.putExtra("email",email1.get(position));
                    intent.putExtra("uid",uid1.get(position));
                    context.startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position=recyclerView.getChildLayoutPosition(v);
                    Intent intent=new Intent(itemView.getContext(),AdminUser.class);
                    intent.putExtra("email",email1.get(position));
                    intent.putExtra("uid",uid1.get(position));
                    context.startActivity(intent);
                    return true;
                }

            });
        }


    }

}
