package com.example.tortuga.myapplication;

/**
 * Created by Suoemi on 2/29/2016.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity implements View.OnClickListener {
    private EditText user, pass;
    public EditText pname;
    private Button bLogin;
    // Progress Dialog
    private ProgressDialog pDialog;
    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    private static final String LOGIN_URL = "http://fdb12.awardspace.net/wasppot.atwebpages.com/users.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        pname = (EditText) findViewById(R.id.plantname);

        bLogin = (Button) findViewById(R.id.login);
        bLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                System.out.print("Yes " + pname.getText().toString() + "Of course");
                try {
                    File file = new File("name.txt");
                    FileWriter out = new FileWriter(file);
                    out.write(pname.getText().toString());
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent ii = new Intent(Login.this, MainActivity.class);
                this.startActivity(ii);
                //here we have used, switch case, because on login activity you may
                //also want to show registration button, so if the user is new ! we can go the
                //registration activity , other than this we could also do this without switch case.
            default:
                break;
        }
    }

}
