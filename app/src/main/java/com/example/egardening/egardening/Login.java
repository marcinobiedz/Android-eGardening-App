package com.example.egardening.egardening;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.egardening.egardening.Session.App;
import com.example.egardening.egardening.Session.TypeFaceUtil;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.ArrayList;


public class Login extends Activity implements OnClickListener {

    EditText et_pass, et_username, et_signup_username, et_signup_email, et_signup_pass;
    Button btn_login, btn_cancel;
    TextView txt_register;
    String username, password;


    //Ayman 15/02/15 Facebook login variables
    //Facebook fb; // Facebook object
    private UiLifecycleHelper uihelper;
    String new_username = null;
    String new_email = null;
    String new_password = "";
    //End Ayman

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        TypeFaceUtil.applyFont(this, findViewById(R.id.loginlayout), "Montserrat-Regular.ttf");

        btn_login = (Button) findViewById(R.id.button_login);
        btn_cancel = (Button) findViewById(R.id.button_cancel);
        et_username = (EditText) findViewById(R.id.editText);
        et_pass = (EditText) findViewById(R.id.editText2);
        txt_register = (TextView) findViewById(R.id.link_register);
        txt_register.setPaintFlags(txt_register.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG );


        btn_login.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        txt_register.setOnClickListener(this);



        //Ayman 15/02/15 Facebook login
        uihelper = new UiLifecycleHelper(this,callback);
        uihelper.onCreate(savedInstanceState);

        ArrayList<String> permission = new ArrayList<String>();
        permission.add("email");
        permission.add("public_profile");
        permission.add("user_friends");

        LoginButton btn_fb_login = (LoginButton) findViewById(R.id.button_fb_login);
        //btn_fb_login.setPublishPermissions(permission);
        btn_fb_login.setReadPermissions(permission);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.egardening.egardening",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        //End Ayman

    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_login:
                App app = (App) getApplication();/////////////
                app.setGarden(null);
                username = et_username.getText().toString();
                password = et_pass.getText().toString();
                new LoginTask().execute();
                break;

            case R.id.button_cancel:
                et_username.setText("");
                et_pass.setText("");
                break;

            case R.id.link_register:
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                break;

            default:
                break;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Login.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    //Ayman 15/02/15 Facebook login methods
    @Override
    protected void onResume() {
        super.onResume();
        uihelper.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uihelper.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        uihelper.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uihelper.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uihelper.onActivityResult(requestCode, resultCode, data);
    }

    void showMsg(String string)
    {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
    }


    private Session.StatusCallback callback =new Session.StatusCallback()
    {

        @Override
        public void call(Session session, SessionState state, Exception exception)
        {
            onSessionStateChange(session,state,exception);
        }
    };


    void onSessionStateChange(Session session, SessionState state, Exception exception)
    {
        if (state.isOpened())
        {
            Log.i("facebook", "Logged in...");
            Request.newMeRequest(session, new Request.GraphUserCallback() {

                @Override
                public void onCompleted(GraphUser user, Response response) {

                    if (user != null) {
                        try {
                            new_username = user.getName();
                            new_email = user.getProperty("email").toString();
                        } catch(Exception e){}

                        //showMsg(usr);
                        //this is the session object
                        SharedPreferences sp = getSharedPreferences("SessionUser", 0);
                        SharedPreferences.Editor speditor = sp.edit();
                        speditor.putString("USERNAME", new_username);
                        speditor.commit();
                        Intent intent = new Intent(Login.this, Main.class);
                        startActivity(intent);
                        //Save to database if user is new
                        new RegisterTask().execute();
                        username = new_username;
                        password = "";

                        new LoginTask().execute();
                        System.out.println("facebook user id is : "+sp.getInt("USERID",0));

                    } else {
                        showMsg("its null");
                        showMsg(response.getError().getErrorMessage());
                    }
                }
            }).executeAsync();

        }
        else if (state.isClosed())
        {
            Log.i("facebook", "Logged out...");
        }
    }

    //End Ayman







    ///LoginTask Inner class
    class LoginTask extends AsyncTask<String, String, Void> {

        private ProgressDialog progressDialog = new ProgressDialog(Login.this);
        InputStream is = null ;
        String result = "";
        HttpEntity entity = null;
        ArrayList<NameValuePair> nvps;
        HttpClient http_client;
        HttpPost http_post;
        HttpResponse response;


        protected void onPreExecute() {
            progressDialog.setMessage("Logging you in...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    LoginTask.this.cancel(true);
                }
            });
            //super.onPreExecute();
        }



        @Override
        protected Void doInBackground(String...params) {
            http_client = new DefaultHttpClient(); //form container
            http_post = new HttpPost("http://egarden-pl.byethost17.com/egardening/login/login.php");

            try {

                nvps = new ArrayList<NameValuePair>();
                nvps.add(new BasicNameValuePair("username", username));
                nvps.add(new BasicNameValuePair("password", password));

                System.out.println(username+" "+password);
                Thread.sleep(2000);
                http_post.setEntity(new UrlEncodedFormEntity(nvps));
                response = http_client.execute(http_post);


            } catch (Exception e) {
                // TODO: handle exception
                Log.e("log_tag", "Error converting result " + e.toString());
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(Void result) {


            try {
                    //Thread.sleep(4000);
                    entity = response.getEntity();
                    is = entity.getContent();

                    JSONObject json_response = new JSONObject(convertStreamToString(is));
                    String retrieved_usr = json_response.getString("username"); //the name of the field in the table in the DB
                    String retrieved_psd = json_response.getString("password"); //the name of the field in the table in the DB
                    //Ayman 18/02/15
                    int retrieved_id = Integer.parseInt(json_response.getString("user_id")); //the name of the field in the table in the DB/
                    //End Ayman

                    //Validate login
                    if((username.equals(retrieved_usr)) && (password.equals(retrieved_psd))) {

                        //this is the session object
                        SharedPreferences sp = getSharedPreferences("SessionUser", 0);
                        SharedPreferences.Editor speditor = sp.edit();
                        speditor.putString("USERNAME", username);
                        speditor.putString("PASSWORD", password); //Not really needed to store password in session
                        speditor.putInt("USERID", retrieved_id);
                        speditor.commit();

                        this.progressDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, Main.class);
                        startActivity(intent);
                        Login.this.finish();

                }

                else {
                    this.progressDialog.dismiss();
                    Toast.makeText(getBaseContext(), "Invalid username or password!", Toast.LENGTH_SHORT).show();
                }

                //this.progressDialog.dismiss();

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                Log.e("log_tag", "Error parsing data "+e.toString());
                this.progressDialog.dismiss();
                Toast.makeText(getBaseContext(), "Maybe your username or password is wrong", Toast.LENGTH_SHORT).show();

            }


        }


        public  String convertStreamToString(InputStream is) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try {
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println(sb);
            return sb.toString();
        }


    }



    //Ayman 16/02/15 RegisterTask Inner Class
    class RegisterTask extends AsyncTask<String, String, Void> {

        InputStream is = null ;
        String result = "";

        protected void onPreExecute() {
            //super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String...params) {
            addUser(new_username, new_password, new_email);
            return null;
        }

        protected void onPostExecute(Void result) {

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

                if(code==1)
                {
                    Toast.makeText(getBaseContext(), "Account created successfully",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Sorry, Try Again",
                            Toast.LENGTH_LONG).show();
                }
            }
            catch(Exception e)
            {
                Log.e("Fail 3", e.toString());
            }
        }

    }
    //End Ayman


}



