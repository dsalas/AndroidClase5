package pe.com.pucp.dti.sesiones.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import pe.com.pucp.dti.sesiones.LoginActivity;
import pe.com.pucp.dti.sesiones.MainActivity;
import pe.com.pucp.dti.sesiones.ParentActivity;
import pe.com.pucp.dti.sesiones.model.User;

/**
 * Created by DIA on 29/05/18.
 */

public class SessionUtils {

    // Constants
    private static final String BASE_URL = "http://192.168.219.67/API_moviles";
    private static final String LOGOUT_URL = BASE_URL + "/logout.php";
    public static final String LOGIN_URL = BASE_URL + "/login.php";
    public static final String CHECK_TOKEN_URL = BASE_URL + "/checkToken.php";
    private static final String PREF_FILE_KEY = "SP_SESSION";
    private static final String PREF_TOKEN_KEY = "SP_TOKEN";

    private static final String PREF_USER_NAME_KEY = "SP_USER_NAME";
    private static final String PREF_USER_LAST_KEY = "SP_USER_LASTNAME";
    private static final String PREF_USER_EMAIL_KEY = "SP_USER_EMAIL";

    private static final String TAG = "SessionUtils";
    private static final String SESSION_STATUS_OK = "1";

    public static void saveSession(String token, User user, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_TOKEN_KEY, token);
        editor.commit();
        saveUser(user, context);
    }

    private static void saveUser(User user, Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_USER_NAME_KEY, user.getName());
        editor.putString(PREF_USER_LAST_KEY, user.getLastname());
        editor.putString(PREF_USER_EMAIL_KEY, user.getEmail());
        editor.commit();
    }

    private static void deleteUser(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(PREF_USER_NAME_KEY);
        editor.remove(PREF_USER_LAST_KEY);
        editor.remove(PREF_USER_EMAIL_KEY);
        editor.commit();
    }

    public static void deleteSession(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(PREF_TOKEN_KEY);
        editor.commit();
        deleteUser(context);
    }

    public static boolean userLogged(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE);
        return sharedPref.contains(PREF_TOKEN_KEY);
    }

    public static String getToken(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE);
        return sharedPref.getString(PREF_TOKEN_KEY,"");
    }

    public static String getUserFullname(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE);
        String name = sharedPref.getString(PREF_USER_NAME_KEY,"");
        String lastname = sharedPref.getString(PREF_USER_LAST_KEY,"");
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(lastname)) {
            return name + " " + lastname;
        }
        return "";
    }

    public static void login(String useremail, String password, final LoginActivity activity) {
        Log.i(TAG, "Login with email: " + useremail +" and password: " + password);
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("email", useremail);
            parameters.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, SessionUtils.LOGIN_URL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                SessionUtils.parseLoginResponse(response, activity);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });
        VolleyRequestQueue.getInstance(activity).getRequestQueue().add(jsonRequest);
    }

    private static void parseLoginResponse(JSONObject response, LoginActivity activity) {
        if (response.isNull("error")) {
            try {
                String token = response.getString("token");
                String name = response.getString("name");
                String lastname = response.getString("lastname");
                String email = response.getString("email");
                User user = new User(name, lastname, email);
                SessionUtils.saveSession(token, user, activity);
                activity.loginSuccess();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                String error = response.getString("error");
                Log.e(TAG,error);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void logout(final ParentActivity activity) {
        String token = SessionUtils.getToken(activity);
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, LOGOUT_URL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, response.toString());
                parseLogoutResponse(response, activity);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });
        VolleyRequestQueue.getInstance(activity).getRequestQueue().add(jsonRequest);
    }

    private static void parseLogoutResponse(JSONObject response, ParentActivity activity) {
        if (response.isNull("error")) {
            try {
                String message = response.getString("message");
                SessionUtils.deleteSession(activity);
                Log.i(TAG, message);
                activity.toLogin();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                String error = response.getString("error");
                Log.e(TAG,error);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void checkToken(final ParentActivity activity){
        String token = SessionUtils.getToken(activity);
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i(TAG, "Checkin session with token " + token);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, CHECK_TOKEN_URL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, response.toString());
                parseCheckTokenResponse(response, activity);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });
        VolleyRequestQueue.getInstance(activity).getRequestQueue().add(jsonRequest);
    }

    private static void parseCheckTokenResponse(JSONObject response, ParentActivity activity) {
        if (response.isNull("error")) {
            try {
                String message = response.getString("message");
                String status = response.getString("status");
                Log.i(TAG, message);
                if (!TextUtils.equals(status,SESSION_STATUS_OK)) {
                    deleteSession(activity);
                    activity.toLogin();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                String error = response.getString("error");
                Log.e(TAG,error);
                deleteSession(activity);
                activity.toLogin();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void checkSession(ParentActivity activity) {
        if (!SessionUtils.userLogged(activity)) {
            activity.toLogin();
        } else {
            checkToken(activity);
        }
    }

}
