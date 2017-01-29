package com.example.rus.contacts.main;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.rus.contacts.contact.Contact;
import com.example.rus.contacts.contact.ContactActivity;
import com.example.rus.contacts.R;
import com.example.rus.contacts.repository.Repository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements ContactsAdapter.OnClickListener {

    public static final String ACCOUNT_TYPE = "by.idiscount.account";

    private RecyclerView rvContacts;
    private FloatingActionButton fabAddContact;

    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabAddContact = (FloatingActionButton) findViewById(R.id.fab_add_contact);
        rvContacts = (RecyclerView) findViewById(R.id.rv_contacts);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        //requestPermission();

        repository = new Repository(getContentResolver());

        fabAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openContactActivity(null);
            }
        });

        addContact();
    }

    @Override
    protected void onStart() {
        rvContacts.setAdapter(new ContactsAdapter(repository.getContacts(), MainActivity.this));
        super.onStart();
    }

    public void addContact() {
        AccountManager accountManager = AccountManager.get(this);
        accountManager.addAccountExplicitly(new Account("zhukic", ACCOUNT_TYPE), null, null);
    }

    public void openContactActivity(Contact contact) {
        Intent intent = new Intent(this, ContactActivity.class);
        if (contact != null) {
            intent.putExtra(ContactActivity.EXTRA_CONTACT, contact);
        }
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Contact contact = (Contact) data.getSerializableExtra(ContactActivity.EXTRA_CONTACT);
            if (contact.getUri() == null) {
                repository.addContact(contact);
            } else {
                repository.editContact(contact);
            }
        }
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS},
                1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        writeFile();
    }

    public void writeFile() {
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "iDiscount" + File.separator);
        root.mkdirs();
        final String fname = "vcf_" + System.currentTimeMillis() + ".vcf";
        File file = new File(root, fname);
        try {
            if (file.exists()) {
                file.delete();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(("BEGIN:VCARD\n" +
                    "VERSION:3.0\n" +
                    "FN: Станишевский Сергей Владимирович\n" +
                    "ORG: ООО'Бизнес-альянс информационных систем'\n" +
                    "EMAIL;TYPE=INTERNET:director@24shop.by\n" +
                    "ADR;TYPE=work: ул. Академика Купревича, д.1/5\n" +
                    "ADR;TYPE=work: ул. Пушкина, д.1/5\n" +
                    "TEL;TYPE=work: +375291234567\n" +
                    "TEL;TYPE=work: +375299876543\n" +
                    "ROLE: Директор\n" +
                    "END:VCARD").getBytes("UTF-8"));
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "text/x-vcard");
            startActivity(intent);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClicked(Contact contact) {
        openContactActivity(contact);
    }

    @Override
    public void onLongItemClick(Contact contact) {
        repository.deleteContact(contact.getUri());
        rvContacts.setAdapter(new ContactsAdapter(repository.getContacts(), MainActivity.this));
    }
}
