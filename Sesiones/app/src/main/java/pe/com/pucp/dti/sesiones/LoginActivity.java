package pe.com.pucp.dti.sesiones;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pe.com.pucp.dti.sesiones.model.User;
import pe.com.pucp.dti.sesiones.utils.SessionUtils;
import pe.com.pucp.dti.sesiones.utils.VolleyRequestQueue;

public class LoginActivity extends ParentActivity {

    // Constants
    private static final String TAG = "LoginActivity";

    // Properties
    EditText useremailEditText;
    EditText passwordEditText;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setObjects();
        if (SessionUtils.userLogged(this)) {
            loginSuccess();
        }
    }

    private void setObjects() {
        this.useremailEditText = findViewById(R.id.useremailEditText);
        this.passwordEditText = findViewById(R.id.passwordEditText);
        this.loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAction();
            }
        });
    }

    private void loginAction() {
        // Datos de prueba
        // Email test1@gmail.com Password: 1234
        // Email test2@gmail.com Password: 1234
        // Email test3@gmail.com Password: 1234
        final String useremail = useremailEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        SessionUtils.login(useremail, password, this);
    }

    public void loginSuccess() {
        Intent toMainActivity = new Intent(this, MainActivity.class);
        startActivity(toMainActivity);
    }
}
