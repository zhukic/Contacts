package com.example.rus.contacts.sync;

import android.accounts.Account;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by RUS on 29.01.2017.
 */

public class SyncService extends Service {

    private static class SyncAdapterImpl extends AbstractThreadedSyncAdapter {

        public SyncAdapterImpl(Context context) {
            super(context, true);
        }

        @Override
        public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
            Log.d("TAG", "performSync");
        }
    }

    private SyncAdapterImpl syncAdapter;

    @Override
    public void onCreate() {
        super.onCreate();

        syncAdapter = new SyncAdapterImpl(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return syncAdapter.getSyncAdapterBinder();
    }
}
