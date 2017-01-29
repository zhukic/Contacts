package com.example.rus.contacts.auth;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by RUS on 29.01.2017.
 */

public class AuthenticatorService extends Service {

    private static class Authenticator extends AbstractAccountAuthenticator {

        public Authenticator(Context context) {
            super(context);
        }

        @Override
        public Bundle editProperties(AccountAuthenticatorResponse r, String s) {
            return null;
        }

        @Override
        public Bundle addAccount(AccountAuthenticatorResponse r, String s, String s2, String[] strings,
                                 Bundle bundle) throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle confirmCredentials(AccountAuthenticatorResponse r, Account account,
                                         Bundle bundle) throws NetworkErrorException {
            return null;
        }

        @Override
        public Bundle getAuthToken(AccountAuthenticatorResponse r, Account account, String s,
                                   Bundle bundle) throws NetworkErrorException {
            return null;
        }

        @Override
        public String getAuthTokenLabel(String s) {
            return null;
        }

        @Override
        public Bundle updateCredentials(AccountAuthenticatorResponse r, Account account, String s,
                                        Bundle bundle) throws NetworkErrorException {
            return null;
        }
        @Override
        public Bundle hasFeatures(AccountAuthenticatorResponse r,
                                  Account account, String[] strings) throws NetworkErrorException {
            return null;
        }
    }

    private Authenticator authenticator;

    @Override
    public void onCreate() {
        super.onCreate();

        authenticator = new Authenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}
