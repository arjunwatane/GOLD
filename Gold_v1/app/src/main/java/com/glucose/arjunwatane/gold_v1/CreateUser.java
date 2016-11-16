package com.glucose.arjunwatane.gold_v1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateUser extends AppCompatActivity
{
    DatabaseHelper helper = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        Button btnSignUp = (Button) findViewById(R.id.btnSignUp);  // change to create

        btnSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditText tfName = (EditText) findViewById(R.id.tfName);
                EditText tfPassword1 = (EditText) findViewById(R.id.tfPassword1);
                EditText tfPassword2 = (EditText) findViewById(R.id.tfPassword2);

                String namestr = tfName.getText().toString();
                String pass1 = tfPassword1.getText().toString();
                String pass2 = tfPassword2.getText().toString();

                if(!pass1.equals(pass2))
                {
                    // popup msg
                    Toast passToast = Toast.makeText(getApplicationContext(), "The passwords do not match.", Toast.LENGTH_SHORT);
                    passToast.show();
                }
                else
                {
                    // insert details into database
                    User u = new User();
                    u.setName(namestr);
                    u.setPassword(pass1);

                    helper.insertUser(u);
                    // clear out the edit text fields and create a popup letting the user know that the user was created.
                }

            }
        });

    }
}