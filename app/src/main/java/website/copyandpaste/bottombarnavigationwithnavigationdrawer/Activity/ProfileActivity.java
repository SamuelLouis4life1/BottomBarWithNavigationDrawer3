package website.copyandpaste.bottombarnavigationwithnavigationdrawer.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import website.copyandpaste.bottombarnavigationwithnavigationdrawer.DAO.FirebaseConfiguration;
import website.copyandpaste.bottombarnavigationwithnavigationdrawer.Models.ModelsContact;
import website.copyandpaste.bottombarnavigationwithnavigationdrawer.R;

public class ProfileActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;    private ImageView imageView;
    private TextView txvDescription;
    private TextView txvLastName;
    private TextView txvFirstName;
    private TextView txvPhone;
    private TextView txvEmail;
    private TextView txvDateOfBith;
    private TextView txvSex;
    private  Button btnEditUser;

    private String txtorigem = "";
    private String txtdescription = "";
    private String txtnome = "";
    private String txtlastname = "";
    private String txtfirstname ="";
    private String txtPhone = "";
    private String txtemail = "";
    private String txtdateofbirth = "";
    private String txtsex = "";

    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;


    //    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        reference = FirebaseConfiguration.getFirebase();

        txvDescription = (TextView) findViewById(R.id.profile_description);
        txvLastName = (TextView) findViewById(R.id.profil_user_lastname);
        txvFirstName = (TextView) findViewById(R.id.profile_user_firstname);
        txvPhone = (TextView) findViewById(R.id.profile_user_phone);
        txvEmail = (TextView) findViewById(R.id.profile_user_email);
        txvDateOfBith = (TextView) findViewById(R.id.profile_user_date_of_birth);
        txvSex = (TextView) findViewById(R.id.txt_Sex);
        imageView = (ImageView) findViewById(R.id.profileImageView);
        btnEditUser = (Button) findViewById(R.id.EditProfil);


//        String lastname = getIntent().getStringExtra("LAST_NAME_KEY");
//        String firstname = getIntent().getStringExtra("FIRST_NAME_KEY");
//
//        Toast.makeText(ProfileActivity.this, "Profile ok !", Toast.LENGTH_LONG).show();
//
//        txvLastName.setText(lastname);
//        txvFirstName.setText(firstname);

        String emailUserLogin = firebaseAuth.getCurrentUser().getEmail();

        reference.child("User").orderByChild("email").equalTo(emailUserLogin).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    ModelsContact modelsContact = postSnapshot.getValue(ModelsContact.class);

                    imageView.setImageURI(Uri.parse(modelsContact.getPhoto()));
                    txvDescription.setText(modelsContact.getDescription());
                    txvLastName.setText(modelsContact.getLastName());
                    txvFirstName.setText(modelsContact.getFirstName());
                    txvPhone.setText(modelsContact.getPhone());
                    txvEmail.setText(modelsContact.getEmail());
                    txvDateOfBith.setText((CharSequence) modelsContact.getDayOfbirth());
                    txvSex.setText(modelsContact.getSex());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btnEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUserProfil();
            }
        });
    }

    private void editUserProfil() {

        String emailUserLogin  = firebaseAuth.getCurrentUser().getEmail();
        reference = FirebaseConfiguration.getFirebase();
        reference.child("User").orderByChild("email").equalTo(emailUserLogin).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    ModelsContact modelsContact = postSnapshot.getValue(ModelsContact.class);

                    final Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                    final Bundle bundle = new Bundle();
                    bundle.putString("origem", "editUserInfo");

                    bundle.putString("description", modelsContact.getDescription());
                    bundle.putString("lastname", modelsContact.getLastName());
                    bundle.putString("firstname", modelsContact.getFirstName());
                    bundle.putString("phone", modelsContact.getPhone());
                    bundle.putString("email", modelsContact.getEmail());
                    bundle.putString("dateOfBirth", String.valueOf(modelsContact.getDayOfbirth()));
                    bundle.putString("sex", modelsContact.getSex());

                    intent.putExtras(bundle);

                    startActivity(intent);

                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    public void EditProfil(View view) {
////        EditProfileUser();
////        editUserProfil();
//        Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
//        startActivity(intent);
//    }

//    private void EditProfileUser() {
//        String emailUserLogin  = firebaseAuth.getCurrentUser().getEmail();
//        reference = FirebaseConfiguration.getFirebase();
//        reference.child("User").orderByChild("UserUId").equalTo(emailUserLogin).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//
//                    ModelsContact modelsContact = postSnapshot.getValue(ModelsContact.class);
//
//                    final Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
//                    final Bundle bundle = new Bundle();
//                    bundle.putString("origem", "editUserInfo");
//
//                    bundle.putString("description", modelsContact.getDescription());
//                    bundle.putString("lastname", modelsContact.getLastName());
//                    bundle.putString("firstname", modelsContact.getFirstName());
//                    bundle.putString("phone", modelsContact.getPhone());
//                    bundle.putString("email", modelsContact.getEmail());
//                    bundle.putString("dateOfBirth", String.valueOf(modelsContact.getDayOfbirth()));
//                    bundle.putString("sex", modelsContact.getSex());
//
//                    intent.putExtras(bundle);
//
//                    startActivity(intent);
//
//                    finish();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }

    //open exclusion dialog

    public void openExcluxionDialog() {
        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.alert_excluir);


        final Button tbnYes =(Button)dialog.findViewById(R.id.ConfirmYes);
        final Button tbnNo =(Button)dialog.findViewById(R.id.ConfirmNo);


        tbnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                excluirAcount();
                dialog.dismiss();
            }
        });

        tbnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfilActivity();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void Excluir(View view) {
        openExcluxionDialog();

    }

    private void openProfilActivity() {
        Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    private void openLoginActivity() {
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void excluirAcount() {

        String UserLoginEmail = firebaseAuth.getCurrentUser().getEmail();

        reference = FirebaseConfiguration.getFirebase();

        reference.child("User").orderByChild("UserUid").equalTo(UserLoginEmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    final ModelsContact modelsContact = postSnapshot.getValue(ModelsContact.class);

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("USER_DELETED", "User Deleted");

                                        Toast.makeText(ProfileActivity.this, "User deleted successfully !", Toast.LENGTH_LONG).show();

                                        reference = FirebaseConfiguration.getFirebase();
                                        reference.child("User").child(modelsContact.getGetKeyUser()).removeValue();

                                        firebaseAuth.signOut();

                                        openLoginActivity();
                                    } else {
                                        Toast.makeText(ProfileActivity.this, "Error occured !", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void AlbumPhoto(View view) {
        Intent intent = new Intent(ProfileActivity.this, PhotoAlbum.class);
        startActivity(intent);
    }

    public void Privacy(View view) {
        Intent intent = new Intent(ProfileActivity.this, PrivaciesActivity.class);
        startActivity(intent);
    }

    private void openProfileActivity() {
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void Back(View view) {
        finish();
        return;
    }
}
