package com.msai.gfb_login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.PlusShare;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    GoogleApiClient mGoogleApiClient;
    private static final int SIGN_IN_CODE = 9001;
    String Check;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.sign_out_button).setOnClickListener(this);



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        Intent intent = getIntent();
        String nameSurname = intent.getStringExtra("displayname");
        String imageUrl = intent.getStringExtra("imageurl");
        Check=intent.getStringExtra("check");



        TextView displayName = (TextView)findViewById(R.id.nameSurnameText);
        displayName.setText("" + nameSurname);

        new DownloadImageTask((RoundedImageView) findViewById(R.id.imageView)).execute(imageUrl);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;

        }
    }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
        }

        private void signOut() {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {

                            if (Check.equals("fb"))
                            {
                                LoginManager.getInstance().logOut();
                                finish();
                            }
                            else
                            {
                                Intent login = new Intent(MainActivity.this, Login_act.class);
                                startActivity(login);
                                finish();
                            }


                        }
                    });






        }

        private void signIn() {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, SIGN_IN_CODE);
        }

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        RoundedImageView bmImage;

        public DownloadImageTask(RoundedImageView mImage) {
            this.bmImage = mImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap mBitmap = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mBitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mBitmap;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}