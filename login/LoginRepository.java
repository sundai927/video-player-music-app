package edu.ds4jb.videoplayer.login;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginRepository  {
    private String userId;
    private boolean isLoggedIn;
    private final SharedPreferences sharedPreferences;

    public LoginRepository(Context context) {
        sharedPreferences = context.getSharedPreferences("LoginRepository", Context.MODE_PRIVATE);
        String currentUser = getStoredId();
        if(!currentUser.isEmpty()) {
            login(currentUser);
        }
    }


    public void login(String userId) {
        this.userId = userId;
        this.isLoggedIn = true;
        storeUserId();
    }
    public void logout() {
        this.userId = "";
        this.isLoggedIn = false;
        storeUserId();
    }
    public String getUserId() {
        return this.userId;
    }
    public boolean getIsLoggedIn() {
        return this.isLoggedIn;
    }

    private void storeUserId() {
        sharedPreferences.edit().putString("userId", userId).apply();
    }
    private String getStoredId() {
        return sharedPreferences.getString("userId", "");
    }


}
