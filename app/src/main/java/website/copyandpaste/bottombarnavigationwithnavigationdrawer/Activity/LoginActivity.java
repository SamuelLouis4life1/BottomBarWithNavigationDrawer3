package website.copyandpaste.bottombarnavigationwithnavigationdrawer.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import website.copyandpaste.bottombarnavigationwithnavigationdrawer.Helper.Preference;
import website.copyandpaste.bottombarnavigationwithnavigationdrawer.Models.ModelsContact;
import website.copyandpaste.bottombarnavigationwithnavigationdrawer.R;

public class LoginActivity extends AppCompatActivity {

    private EditText emailView;
    private EditText passwordView;
    private FirebaseAuth firebaseAuth;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private AlertDialog alert;
    private TextView recoverPassword;
    private ModelsContact modelsContact;
    private FirebaseAuth.AuthStateListener firebaseAuthAuthStateListener ;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        emailView = (EditText) findViewById(R.id.email_login);
        passwordView = (EditText) findViewById(R.id.passord_login);
        recoverPassword = (TextView) findViewById(R.id.forget_password);
        firebaseAuth = FirebaseAuth.getInstance();
        loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        final EditText emailView = new EditText(LoginActivity.this);
        emailView.setHint("exemple@exemple.com");

//        firebaseAuthAuthStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                if (user !=null){
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//        };

        recoverPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setCancelable(false);
        builder.setTitle("Password recovery");
        builder.setMessage("Enter your email:");
        builder.setView(emailView);

        if (!emailView.getText().equals("")) {
            builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, final int i) {
                    firebaseAuth = FirebaseAuth.getInstance();
                    String emailRecuperar = emailView.getText().toString();
                    firebaseAuth.sendPasswordResetEmail(emailRecuperar).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "In a moment you will receive an e-mail!", Toast.LENGTH_LONG).show();
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Failed to send email!", Toast.LENGTH_LONG).show();
                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                        }
                    });
                }
            });


            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });

        } else {
            Toast.makeText(LoginActivity.this, "Please fill in your e-mail!", Toast.LENGTH_LONG).show();
        }
        alert = builder.create();
        alert.show();

            }
        });
    }

    public void Login(View view) {
        validateLogin();
    }

    private void validateLogin() {

        final String email = emailView.getText().toString();
        final String password = passwordView.getText().toString();

        if(TextUtils.isEmpty(email)){
            emailView.setError("E-mail field is required");
            emailView.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailView.setError("Please, enter a valid email");
            emailView.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(password)){
            passwordView.setError("Password field is required");
            passwordView.requestFocus();
            return;
        }


        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                 finish();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    Preference preference = new Preference(LoginActivity.this);
//                    preference.savedPreferenceUser(modelsContact.getEmail(), modelsContact.getPassword());

                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void AbrirRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void LoginFacebook(View view) {
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(LoginActivity.class.getName(), "Facebook Message" + loginResult);
                handleFacebookLogin(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(LoginActivity.class.getName(), "Facebook: Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(LoginActivity.class.getName(), "Facebook: Error" + error);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    // Initialize Facebook Login button
//    mCallbackManager = CallbackManager.Factory.create();
//    LoginButton loginButton = findViewById(R.id.login_button);
//    loginButton.setReadPermissions("email", "public_profile");
//    loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//        @Override
//        public void onSuccess(LoginResult loginResult) {
//            Log.d(TAG, "facebook:onSuccess:" + loginResult);
//            handleFacebookAccessToken(loginResult.getAccessToken());
//        }
//
//        @Override
//        public void onCancel() {
//            Log.d(TAG, "facebook:onCancel");
//            // ...
//        }
//
//        @Override
//        public void onError(FacebookException error) {
//            Log.d(TAG, "facebook:onError", error);
//            // ...
//        }
//    });

    public void handleFacebookLogin(AccessToken accessToken){
        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Log.d(LoginActivity.class.getName(), "Facebook " + "Login Sussessul");
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    startActivity(new Intent(LoginActivity.this,
                                            MainActivity.class));
                                    finish();
                                }else{
                                    Log.e(LoginActivity.class.getName(),
                                            task.getException().toString());
                                    Toast.makeText(getApplicationContext(),
                                            "Falha no " +
                                                    "login via facebook", Toast.LENGTH_LONG).show();
                                }
                            }

                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
