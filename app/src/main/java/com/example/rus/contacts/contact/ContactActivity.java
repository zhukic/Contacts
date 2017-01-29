package com.example.rus.contacts.contact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.rus.contacts.R;
import com.example.rus.contacts.repository.Repository;

public class ContactActivity extends AppCompatActivity {

    public static final String EXTRA_CONTACT = "EXTRA_CONTACT";

    private Repository repository;

    private TextInputEditText etName;
    private TextInputEditText etOrganization;
    private TextInputEditText etPosition;
    private TextInputEditText etPhone;
    private TextInputEditText etPhone2;
    private TextInputEditText etEmail;
    private TextInputEditText etEmail2;
    private TextInputEditText etAddress;
    private TextInputEditText etAddress2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etName = (TextInputEditText) findViewById(R.id.et_name);
        etOrganization = (TextInputEditText) findViewById(R.id.et_organization);
        etPosition = (TextInputEditText) findViewById(R.id.et_position);
        etPhone = (TextInputEditText) findViewById(R.id.et_phone);
        etPhone2 = (TextInputEditText) findViewById(R.id.et_phone2);
        etEmail = (TextInputEditText) findViewById(R.id.et_email);
        etEmail2 = (TextInputEditText) findViewById(R.id.et_email2);
        etAddress = (TextInputEditText) findViewById(R.id.et_address);
        etAddress2 = (TextInputEditText) findViewById(R.id.et_address2);

        repository = new Repository(getContentResolver());

        if (getIntent() != null) {
            if (getIntent().hasExtra(EXTRA_CONTACT)) {
                setContact((Contact) getIntent().getSerializableExtra(EXTRA_CONTACT));
            }
        }
    }

    private void setContact(Contact contact) {
        etName.setText(contact.getName());
        etOrganization.setText(contact.getOrganization());
        etPosition.setText(contact.getPosition());
        etPhone.setText(contact.getPhone());
        etPhone2.setText(contact.getPhone2());
        etEmail.setText(contact.getEmail());
        etEmail2.setText(contact.getEmail2());
        etAddress.setText(contact.getAddress());
        etAddress2.setText(contact.getAddress2());
    }

    private void saveContact() {
        Contact contact = new Contact();
        if (getIntent() != null) {
            if (getIntent().hasExtra(EXTRA_CONTACT)) {
                contact.setUri(((Contact) getIntent().getSerializableExtra(EXTRA_CONTACT)).getUri());
            }
        }
        contact.setName(etName.getText().toString());
        contact.setOrganization(etOrganization.getText().toString());
        contact.setPosition(etPosition.getText().toString());
        contact.setPhone(etPhone.getText().toString());
        contact.setPhone2(etPhone2.getText().toString());
        contact.setEmail(etEmail.getText().toString());
        contact.setEmail2(etEmail2.getText().toString());
        contact.setAddress(etAddress.getText().toString());
        contact.setAddress2(etAddress2.getText().toString());

        Intent intent = new Intent();
        intent.putExtra(EXTRA_CONTACT, contact);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();

        switch (id) {
            case R.id.action_save_contact:
                saveContact();
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}
