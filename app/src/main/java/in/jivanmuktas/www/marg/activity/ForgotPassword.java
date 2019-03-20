package in.jivanmuktas.www.marg.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import in.jivanmuktas.www.marg.R;

public class ForgotPassword extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarTop);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Forgot Password");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView textView = findViewById(R.id.etEmail);
        Button button = findViewById(R.id.button_Sign_Up);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ForgotPassword.this, "This section is under Developement", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
