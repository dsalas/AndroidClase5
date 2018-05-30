package pe.com.pucp.dti.sesiones;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import pe.com.pucp.dti.sesiones.utils.SessionUtils;

public class MainActivity extends ParentActivity {

    // Propiedades
    TextView usernameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setObjects();
    }

    private void setObjects() {
        usernameTextView = findViewById(R.id.usernameTextView);
        usernameTextView.setText(SessionUtils.getUserFullname(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                SessionUtils.logout(this);
                return true;
            case R.id.toTest:
                goToTest();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToTest(){
        Intent toTest = new Intent(this, TestActivity.class);
        startActivity(toTest);
    }
}
