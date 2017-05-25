package com.example.egardening.egardening;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.egardening.egardening.Session.TypeFaceUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class SignUp extends Activity implements View.OnClickListener {

    EditText et_signup_username, et_signup_email, et_signup_pass;
    Button btn_cancel2, btn_signup, btn_back;

    String new_username = null;
    String new_email = null;
    String new_password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        TypeFaceUtil.applyFont(this, findViewById(R.id.signuplayout), "Montserrat-Regular.ttf");


        btn_cancel2 = (Button) findViewById(R.id.button_signup_cancel);
        btn_signup = (Button) findViewById(R.id.button_signup);
        btn_back = (Button) findViewById(R.id.button_back_to_login);
        et_signup_username = (EditText) findViewById(R.id.editText_signup_username);
        et_signup_email = (EditText) findViewById(R.id.editText_signup_email);
        et_signup_pass = (EditText) findViewById(R.id.editText_signup_pass);

        btn_cancel2.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        btn_back.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button_signup_cancel:
                et_signup_username.setText("");
                et_signup_email.setText("");
                et_signup_pass.setText("");
                break;

            case R.id.button_signup:
                new_username = et_signup_username.getText().toString();
                new_email = et_signup_email.getText().toString();
                new_password = et_signup_pass.getText().toString();

                if(new_username.equals("") || new_email.equals("") || new_password.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("All fields must be filled")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

                else {
                    new RegisterTask().execute();
                }

                break;


            case R.id.button_back_to_login:
                SignUp.this.finish();
                break;

            default:
                break;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //Ayman 16/02/15 RegisterTask Inner Class
    class RegisterTask extends AsyncTask<String, String, Void> {

        InputStream is = null ;
        String result = "";
        boolean b = false;

        protected void onPreExecute() {
            //super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String...params) {
            addUser(new_username, new_password, new_email);
            return null;
        }

        protected void onPostExecute(Void result) {
            if(b) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                builder.setMessage("Account created successfully!")
                        .setCancelable(false)
                        .setPositiveButton("Back to login", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                SignUp.this.finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }

            else {
                Toast.makeText(getBaseContext(), "There was something wrong.\nPlease Try Again",
                        Toast.LENGTH_LONG).show();
            }
        }


        public void addUser(String username, String password ,String email) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("username",username));
            nameValuePairs.add(new BasicNameValuePair("password",password));
            nameValuePairs.add(new BasicNameValuePair("email",email));

            try
            {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://egarden-pl.byethost17.com/egardening/login/register.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection success ");
            }
            catch(Exception e)
            {
                Log.e("Fail 1", e.toString());
                Toast.makeText(getApplicationContext(), "Invalid IP Address",
                        Toast.LENGTH_LONG).show();
            }

            try
            {
                BufferedReader reader = new BufferedReader
                        (new InputStreamReader(is,"iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
                Log.e("pass 2", "connection success ");
            }
            catch(Exception e)
            {
                Log.e("Fail 2", e.toString());
            }

            try
            {
                JSONObject json_data = new JSONObject(result);
                int code = (json_data.getInt("code"));
                System.out.println(code);
                if(code==1)
                {
                    b = true;

                }
                else {}
            }
            catch(Exception e)
            {
                Log.e("Fail 3", e.toString());
            }

        }

    }
    //End Ayman
}
