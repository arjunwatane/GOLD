package com.glucose.arjunwatane.gold_v1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity
{
    DatabaseHelper helper = new DatabaseHelper(this);
    EditText tfUsername, tfPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        helper.load();

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        Button btnCreateUser = (Button) findViewById(R.id.btnCreateUser); // change this to create user
        tfUsername = (EditText)findViewById(R.id.TFusername);
        tfPassword = (EditText)findViewById(R.id.TFpassword);

        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                String str = tfUsername.getText().toString();


                String pass = tfPassword.getText().toString();

                String password = helper.searchPass(str);

                if(pass.equals(password))
                {
                    int ID = helper.searchID(str, pass);
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putExtra("Username", str);
                    i.putExtra("ID", ID);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast loginToast = Toast.makeText(getApplicationContext(), "The login credentials are incorrect.", Toast.LENGTH_SHORT);
                    loginToast.show();
                }


            }
        });

        btnCreateUser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                tfUsername.setText("");
                tfPassword.setText("");
                Intent i = new Intent(getApplicationContext(), CreateUser.class);
                startActivity(i);
            }
        });

    }



}