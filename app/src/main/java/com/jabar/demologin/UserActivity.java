package com.jabar.demologin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

public class UserActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private int nrp = 2103187013;
    private String kelas = "D3 PJJ Teknik Informatika 2018";
    private ImageView profilImg;
    private TextView namaText,
            nrpText,
            kelasText;
    private Button logoutButton;

    private GoogleApiClient client;
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        profilImg = findViewById(R.id.profilImg);
        namaText = findViewById(R.id.namaText);
        nrpText = findViewById(R.id.nrpText);
        kelasText = findViewById(R.id.kelasText);
        logoutButton = findViewById(R.id.logout);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        client = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.signOut(client).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if(status.isSuccess()){
                            gotoMainActivity();
                        } else {
                            Toast.makeText(UserActivity.this,"Anda Gagal Log Out!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void gotoMainActivity() {
        startActivity(new Intent(UserActivity.this, MainActivity.class));
        this.finish();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            namaText.setText(account.getDisplayName());
        //    nrpText.setText(nrp);
          //  kelasText.setText(kelas);

            Picasso.get().load(account.getPhotoUrl()).placeholder(R.drawable.red).into(profilImg);
        } else {
            gotoMainActivity();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(client);

        if(opr.isDone()){
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult result) {
                    handleSignInResult(result);
                }
            });
        }
    }
}