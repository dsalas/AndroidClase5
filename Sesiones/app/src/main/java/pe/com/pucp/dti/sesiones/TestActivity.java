package pe.com.pucp.dti.sesiones;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TestActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setTitle(getResources().getString(R.string.test_activity_title));
    }
}
