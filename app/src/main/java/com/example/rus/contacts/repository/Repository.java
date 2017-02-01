package com.example.rus.contacts.repository;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;

import com.example.rus.contacts.contact.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RUS on 27.01.2017.
 */

public class Repository {
    private static final String MIMETYPE = "vnd.android.cursor.item/vnd.by.idiscount.businesscard";

    private ContentResolver contentResolver;

    public Repository(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Contact contact = new Contact();
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contact.setName(name);
                Long contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Long id = cursor.getLong(cursor.getColumnIndex(ContactsContract.RawContacts._ID));
                Cursor organizationCursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "=?",
                        new String[]{String.valueOf(id), ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE}, null);
                while (organizationCursor.moveToNext()) {
                    String organization = organizationCursor.getString(organizationCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY));
                    contact.setOrganization(organization);
                    String position = organizationCursor.getString(organizationCursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
                    contact.setPosition(position);
                }
                organizationCursor.close();
                if (cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)).equals("1")) {
                    String whereId = ContactsContract.Data.CONTACT_ID + " = ?";
                    Cursor phoneCursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, whereId, new String[]{String.valueOf(id)}, null);
                    if (phoneCursor.getCount() > 0) {
                        while (phoneCursor.moveToNext()) {
                            String phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            if (contact.getPhone() == null) {
                                contact.setPhone(phone);
                            } else {
                                contact.setPhone2(phone);
                            }
                        }
                        phoneCursor.close();
                    }
                }
                Cursor emailCursor = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + " = ?", new String[]{String.valueOf(id)}, null);
                if (emailCursor.getCount() > 0) {
                    while (emailCursor.moveToNext()) {
                        String email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
                        if (contact.getEmail() == null) {
                            contact.setEmail(email);
                        } else {
                            contact.setEmail2(email);
                        }
                    }
                    emailCursor.close();
                }
                Cursor addressCursor = contentResolver.query(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + " = ?", new String[]{String.valueOf(id)}, null);
                if (addressCursor.getCount() > 0) {
                    while (addressCursor.moveToNext()) {
                        String address = addressCursor.getString(addressCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
                        if (contact.getAddress() == null) {
                            contact.setAddress(address);
                        } else {
                            contact.setAddress2(address);
                        }
                    }
                    addressCursor.close();
                }
                Uri rawContactUri = ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, id);
                contact.setUri(rawContactUri.toString());
                contacts.add(contact);
            }
            cursor.close();
        }
        return contacts;
    }

    public void addContact(Contact contact) {
        ArrayList<ContentProviderOperation> cpo = new ArrayList<>();

        cpo.add(ContentProviderOperation.newInsert(getUriWithSyncAdapterParameter(ContactsContract.RawContacts.CONTENT_URI))
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, "6421")
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, "by.idiscount")
                .build());

        cpo.add(ContentProviderOperation.newInsert(getUriWithSyncAdapterParameter(ContactsContract.Data.CONTENT_URI))
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getName())
                .build());

        cpo.add(ContentProviderOperation.newInsert(getUriWithSyncAdapterParameter(ContactsContract.Data.CONTENT_URI))
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, contact.getOrganization())
                .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, contact.getPosition())
                .build());

        cpo.add(ContentProviderOperation.newInsert(getUriWithSyncAdapterParameter(ContactsContract.Data.CONTENT_URI))
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getPhone())
                .build());

        cpo.add(ContentProviderOperation.newInsert(getUriWithSyncAdapterParameter(ContactsContract.Data.CONTENT_URI))
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getPhone2())
                .build());

        cpo.add(ContentProviderOperation.newInsert(getUriWithSyncAdapterParameter(ContactsContract.Data.CONTENT_URI))
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, contact.getEmail())
                .build());

        cpo.add(ContentProviderOperation.newInsert(getUriWithSyncAdapterParameter(ContactsContract.Data.CONTENT_URI))
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, contact.getEmail2())
                .build());

        cpo.add(ContentProviderOperation.newInsert(getUriWithSyncAdapterParameter(ContactsContract.Data.CONTENT_URI))
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, contact.getAddress())
                .build());

        cpo.add(ContentProviderOperation.newInsert(getUriWithSyncAdapterParameter(ContactsContract.Data.CONTENT_URI))
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, contact.getAddress2())
                .build());

        try {
            ContentProviderResult[] contentProviderResults = contentResolver.applyBatch(ContactsContract.AUTHORITY, cpo);
            Uri uri = contentProviderResults[0].uri;
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    public void deleteContact(Contact contact) {
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.NUMBER + "=?", new String[]{contact.getPhone()}, null);
        Long rawId = -1L;
        if (cursor.moveToFirst()) {
            rawId = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
        }
        cursor.close();
        if (rawId != -1) {
            contentResolver.delete(getUriWithSyncAdapterParameter(Uri.withAppendedPath(ContactsContract.RawContacts.CONTENT_URI, String.valueOf(rawId))), null, null);
        }
    }

    public void editContact(Contact contact) {
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.NUMBER + "=?", new String[]{contact.getPhone()}, null);
        Long rawId = -1L;
        if (cursor.moveToFirst()) {
            rawId = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
        }
        cursor.close();
        if (rawId == -1) {
            return;
        }
        ArrayList<ContentProviderOperation> cpo = new ArrayList<>();

        cpo.add(ContentProviderOperation.newUpdate(getUriWithSyncAdapterParameter(ContactsContract.Data.CONTENT_URI))
                .withYieldAllowed(true)
                .withSelection(ContactsContract.Data.RAW_CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "=?", new String[]{String.valueOf(rawId),
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE})
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getName())
                .build());

        cpo.add(ContentProviderOperation.newUpdate(getUriWithSyncAdapterParameter(ContactsContract.Data.CONTENT_URI))
                .withYieldAllowed(true)
                .withSelection(ContactsContract.Data.RAW_CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "=?", new String[]{String.valueOf(rawId),
                        ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE})
                .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, contact.getOrganization())
                .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, contact.getPosition())
                .build());

        cpo.add(ContentProviderOperation.newDelete(getUriWithSyncAdapterParameter(ContactsContract.Data.CONTENT_URI))
                .withYieldAllowed(true)
                .withSelection(ContactsContract.Data.RAW_CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "=?", new String[]{String.valueOf(rawId),
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE})
                .build());

        cpo.add(ContentProviderOperation.newInsert(getUriWithSyncAdapterParameter(ContactsContract.Data.CONTENT_URI))
                .withYieldAllowed(true)
                .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getPhone())
                .build());

        cpo.add(ContentProviderOperation.newInsert(getUriWithSyncAdapterParameter(ContactsContract.Data.CONTENT_URI))
                .withYieldAllowed(true)
                .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getPhone2())
                .build());

        cpo.add(ContentProviderOperation.newDelete(getUriWithSyncAdapterParameter(ContactsContract.Data.CONTENT_URI))
                .withYieldAllowed(true)
                .withSelection(ContactsContract.Data.RAW_CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "=?", new String[]{String.valueOf(rawId),
                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE})
                .build());

        cpo.add(ContentProviderOperation.newInsert(getUriWithSyncAdapterParameter(ContactsContract.Data.CONTENT_URI))
                .withYieldAllowed(true)
                .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, contact.getEmail())
                .build());

        cpo.add(ContentProviderOperation.newInsert(getUriWithSyncAdapterParameter(ContactsContract.Data.CONTENT_URI))
                .withYieldAllowed(true)
                .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, contact.getEmail2())
                .build());

        cpo.add(ContentProviderOperation.newDelete(getUriWithSyncAdapterParameter(ContactsContract.Data.CONTENT_URI))
                .withYieldAllowed(true)
                .withSelection(ContactsContract.Data.RAW_CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "=?", new String[]{String.valueOf(rawId),
                        ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE})
                .build());

        cpo.add(ContentProviderOperation.newInsert(getUriWithSyncAdapterParameter(ContactsContract.Data.CONTENT_URI))
                .withYieldAllowed(true)
                .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, contact.getAddress())
                .build());

        cpo.add(ContentProviderOperation.newInsert(getUriWithSyncAdapterParameter(ContactsContract.Data.CONTENT_URI))
                .withYieldAllowed(true)
                .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, contact.getAddress2())
                .build());

        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, cpo);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    private Uri getUriWithSyncAdapterParameter(Uri uri) {
        return uri.buildUpon().appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER, "true").build();
    }
}
