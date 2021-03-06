package msu.olive.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import msu.olive.R;
import msu.olive.Server.RequestHandler;
import msu.olive.Server.Server;

public class RegisterActivity extends AppCompatActivity {

    EditText register_username, register_password, register_retype, register_info;
    Button btnRegister;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        bumbum();
    }

    private void register() {
        String username = register_username.getText().toString();
        String password = register_password.getText().toString();
        String retype = register_retype.getText().toString();
        String info = register_info.getText().toString();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please insert your username address!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please insert your password!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(retype)) {
            Toast.makeText(this, "Please retype your password!", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(retype)) {
            Toast.makeText(this, "Passwords unmatched! Please retype your passwords correctly!", Toast.LENGTH_SHORT).show();
        } else {
            pushData(username, password, info);
        }
    }

    private void pushData(final String username, final String password, final String info) {
        class Push extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            RequestHandler requestHandler = new RequestHandler();
            int check = 0;

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> data = new HashMap<>();
                data.put("username", username);
                data.put("password", password);
                data.put("info", info);
                String result = requestHandler.sendPostRequest(Server.RegisterURL, data);
                return result;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RegisterActivity.this, "Creating account, please wait ...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equals("0")) {
                    Toast.makeText(RegisterActivity.this, "Register failed! Your username has been used, please choose another!", Toast.LENGTH_SHORT).show();
                } else if (s.equals("1")) {
                    Toast.makeText(RegisterActivity.this, "Register succeeded!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
                    Log.i("error", s);
                    Toast.makeText(RegisterActivity.this, "Register failed! Fatal errors occur in the process!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        Push push = new Push();
        push.execute();

    }

    private void bumbum() {
        register_username = (EditText) findViewById(R.id.register_username);
        register_password = (EditText) findViewById(R.id.register_password);
        register_retype = (EditText) findViewById(R.id.register_retype);
        register_info = (EditText) findViewById(R.id.register_info);
        btnRegister = (Button) findViewById(R.id.register_button_sign_up);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }
}
