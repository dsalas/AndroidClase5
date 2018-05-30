package pe.com.pucp.dti.sesiones;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import pe.com.pucp.dti.sesiones.utils.SessionUtils;

/**
 * Created by DIA on 29/05/18.
 */

public class ParentActivity extends AppCompatActivity {

    private static final String TAG = "ParentActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkSession();
    }

    protected void checkSession() {
        Log.i(TAG, "Checking Session in class " + this.getClass().toString());
        if (this.getClass() != LoginActivity.class) {
            SessionUtils.checkSession(this);
        }
    }

    public void toLogin() {
        Intent toLogin = new Intent(this, LoginActivity.class);
        toLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(toLogin);
    }
}
