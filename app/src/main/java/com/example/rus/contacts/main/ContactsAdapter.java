package com.example.rus.contacts.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rus.contacts.contact.Contact;
import com.example.rus.contacts.R;

import java.util.List;

/**
 * Created by RUS on 27.01.2017.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    public interface OnClickListener {
        void onItemClicked(Contact contact);
        void onLongItemClick(Contact contact);
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {

        TextView tvContactInfo;

        public ContactViewHolder(View itemView) {
            super(itemView);
            tvContactInfo = (TextView) itemView.findViewById(R.id.tv_contact_info);
        }
    }

    private List<Contact> contacts;
    private OnClickListener onClickListener;

    ContactsAdapter(List<Contact> contacts, OnClickListener onClickListener) {
        this.contacts = contacts;
        this.onClickListener = onClickListener;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, final int position) {
        holder.tvContactInfo.setText(contacts.get(position).toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onItemClicked(contacts.get(position));
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onClickListener.onLongItemClick(contacts.get(position));
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
}
