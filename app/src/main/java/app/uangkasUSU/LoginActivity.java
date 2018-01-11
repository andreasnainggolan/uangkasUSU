package app.uangkasUSU;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import app.uangkasUSU.helper.Config;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText metNim,metPassword;
    private TextInputLayout mtilNIM,mtilPassword;
    private Button mButton;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String nim2;
    int sess_intro;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mButton = (Button) findViewById(R.id.button);
        mtilNIM = (TextInputLayout)findViewById(R.id.textInputLayoutNim);
        mtilPassword= (TextInputLayout)findViewById(R.id.textInputLayoutPassword);

        metNim = (TextInputEditText)findViewById(R.id.editTextNim);
        metPassword = (TextInputEditText)findViewById(R.id.editTextPassword);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mohon Menunggu");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        sess_intro = 0;
        sharedPreferences = getSharedPreferences("uangkasUSU", Context.MODE_APPEND);
        sess_intro = sharedPreferences.getInt("intro", 0);
        if (sess_intro == 0) {
            // set session
            sharedPreferences = getSharedPreferences("uangkasUSU", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putInt("intro", 1);
            editor.apply();
            // call intro
            startActivity(new Intent(LoginActivity.this, IntroActivity.class));
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nim = metNim.getText().toString();
                String password = metPassword.getText().toString();

                if (nim.equals("") || password.equals("")){
                    Toast.makeText(getApplicationContext(), "Isi data dengan benar",
                            Toast.LENGTH_LONG).show();
                }else {
                    _readMysql();
                }
            }
        });
    }

    private void _readMysql(){
        progressDialog.show();
        AndroidNetworking.post(Config.host + "login.php")
                .addBodyParameter("username", metNim.getText().toString())
                .addBodyParameter("password", metPassword.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("response").equals("success")) {
                                if(response.getString("role").equals("user")) {
                                    sharedPreferences = getSharedPreferences("uangkasUSU", Context.MODE_PRIVATE);
                                    editor = sharedPreferences.edit();
                                    editor.putString("nim", metNim.getText().toString());
                                    editor.apply();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("nim",metNim.getText().toString());
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(LoginActivity.this, "Password Anda Salah", Toast.LENGTH_LONG).show();
                                }
                                progressDialog.dismiss();

                            } else {
                                Toast.makeText(LoginActivity.this, "Gagal Masuk", Toast.LENGTH_SHORT).show();
                            }
                        } catch(JSONException e){
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        progressDialog.dismiss();
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences("uangkasUSU", Context.MODE_PRIVATE);
        nim2 = sharedPreferences.getString("nim", "");
        if(!nim2.isEmpty()){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("nim",metNim.getText().toString());
            startActivity(intent);
        }
    }

}

