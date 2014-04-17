package com.example.canvas.models;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.os.Bundle;

public class AccountToken {

	public String updateToken(AccountManager am, boolean invalidateToken, Activity activity) {
		
		String response = "";
		String authToken = "null";
		
		String scopes = "oauth2: https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/calendar";
		
		Account[] accounts = am.getAccountsByType("com.google");
		AccountManagerFuture<Bundle> accountManagerFuture;
		accountManagerFuture = am.getAuthToken(accounts[0], scopes, null, activity, null, null);
			
		String sAccountManagerFuture = String.valueOf(accountManagerFuture);
			
		Bundle authTokenBundle = null;
		try {
			authTokenBundle = accountManagerFuture.getResult();
		} catch (OperationCanceledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AuthenticatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		authToken = authTokenBundle.getString(AccountManager.KEY_AUTHTOKEN).toString();
		
	    if(invalidateToken) {
            am.invalidateAuthToken("com.google", authToken);
            authToken = this.updateToken(am, false, activity);
        }
		
		return authToken;
		
	}

	
}
