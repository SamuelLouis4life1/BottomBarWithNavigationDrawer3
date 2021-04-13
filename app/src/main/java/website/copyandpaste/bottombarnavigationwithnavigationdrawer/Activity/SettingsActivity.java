package website.copyandpaste.bottombarnavigationwithnavigationdrawer.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import website.copyandpaste.bottombarnavigationwithnavigationdrawer.Models.ModelsContact;
//import website.copyandpaste.bottombarnavigationwithnavigationdrawer.PhotoFullPopup;
import website.copyandpaste.bottombarnavigationwithnavigationdrawer.R;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class SettingsActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101, CHOOSE_CAMERA = 102;
    private final int CODE_IMG_GALLERY = 1;
    private final String SAMPLE_CROPPED_IMG_NAME = "SampleCropimg";
    private EditText mDescription;
    private EditText editTextLastName, editTextFisrtName;
    private EditText txtPassword;
    private EditText txtComfirmPassword;
    private ImageView imageView;
    private FirebaseAuth firebaseAuth;
    Context context;
    String pathToFile;


    Uri uriProfileImage;
    String profileImageUrl;


    private EditText txtDescription;
    private EditText txtLastName;
    private EditText txtFirstName;
    private EditText txtPhone;
    private EditText txtEmail;
    private EditText txtDateOfBirth;
    private EditText sex;
    private RadioButton rbMale;
    private RadioButton rbFemale;


    private String txtorigem = "";
    private String txtdecription = "";
    private String txtlastname = "";
    private String txtfirstname = "";
    private String txtphone = "";
    private String txtemail = "";
    private String txtdatebirth = "";
    private String txtsex ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        if (Build.VERSION.SDK_INT >= 23){
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

        mDescription = (EditText) findViewById(R.id.description_setting);
        editTextLastName = (EditText)findViewById(R.id.lastname_setting);
        editTextFisrtName = (EditText) findViewById(R.id.first_name_setting);
        txtPassword = (EditText) findViewById(R.id.passord_setting);
        txtComfirmPassword = (EditText) findViewById(R.id.comfirm_password_setting);
        imageView = (ImageView) findViewById(R.id.imagem_setting);
        rbFemale = (RadioButton) findViewById(R.id.Female);
        rbMale = (RadioButton) findViewById(R.id.Male);


        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        txtorigem = bundle.getString("origem");

//        if (txtorigem.equals("editUserInfo")){
//            txtdecription = bundle.getString("description");
//            txtlastname = bundle.getString("LasteName");
//
//            txtDescription.setText(txtdecription.toString());
//            txtLastName.setText(txtlastname.toString());
//
//        }

//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Code to show image in full screen:
//                new PhotoFullPopup(context, R.layout.popup_photo_full_screen, view, URL, null);
//
//            }
//        });




        loadUserInformation();
        init();
    }

    private void init() {

    }

    // Crop options
    private void startCrop(@NonNull Uri uri){
        String destinationFileName = SAMPLE_CROPPED_IMG_NAME;
        destinationFileName +=".jpg";
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(),destinationFileName)));
        uCrop.withAspectRatio(1, 1);
//        uCrop.useSourceImageAspectRatio();
//        uCrop.withAspectRatio(3,4);
//        uCrop.withAspectRatio(2,3);
//        uCrop.withAspectRatio(16,9);
        uCrop.withMaxResultSize(450, 450);
        uCrop.withOptions(getCropOptions());
        uCrop.start(SettingsActivity.this);
    }


    private UCrop.Options getCropOptions(){
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(70);

        //CompressType
//        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
//        options.setCompressionFormat(Bitmap.CompressFormat.PNG);

        //UI
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);

        //Colors
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryLight));
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));

        options.setToolbarTitle("Crop image");
        return options;
    }

    private void loadUserInformation() {

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl()
                                .toString()).into(imageView);
            }
            if (user.getDisplayName() != null) {
                String displayName = user.getDisplayName();
                mDescription.setText(user.getDisplayName());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK &&  data != null && data.getData() != null){
            uriProfileImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                imageView.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == RESULT_OK){
            if (requestCode == 1){
                Bitmap bitmap = BitmapFactory.decodeFile(pathToFile);
                imageView.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/" +System.currentTimeMillis() + ".jpg");
        if (uriProfileImage !=null){
            profileImageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    profileImageUrl = taskSnapshot.getDownloadUrl().toString();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SettingsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    private void showImageCamera() {
        Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takepic.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            photoFile = createPhotoFile();

            if (photoFile != null){
                pathToFile = photoFile.getAbsolutePath();
                Uri photoUri = FileProvider.getUriForFile(SettingsActivity.this, "com.website.copyandpaste.bottombarnavigationwithnavigationdrawer.android.fileprovider", photoFile);
                takepic.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takepic, 1);
            }
        }
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent,"Select Profile Image"), CHOOSE_CAMERA);

//        Intent iCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (iCamera.resolveActivity(getPackageManager()) != null){
//            startActivityForResult(iCamera, CHOOSE_CAMERA);
//        }
    }

    private File createPhotoFile() {
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storsgeDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(name, ".jpg", storsgeDir);
        }catch (IOException e){
            Log.d("mylog", "Excep : " + e.toString() );

        }
        return image;
    }


    private void showImageGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Profile Image"), CHOOSE_IMAGE);
    }

    private void saveUserInformation() {
        String displayName = mDescription.getText().toString();


        if (displayName.isEmpty()){
            mDescription.setError("Your description is required");
            mDescription.requestFocus();
            return;
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null && profileImageUrl !=null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(SettingsActivity.this,"Profile image updated", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    // Botão confirma
    public void Confirmar(View view) {

//        if (txtPassword.getText().toString().equals(txtComfirmPassword.getText().toString())) {
//
//            ModelsContact modelsContact = new ModelsContact();
//            modelsContact.setDescription(txtDescription.getText().toString());
//            modelsContact.setLastName(txtLastName.getText().toString());
//
//        }

        saveUserInformation();
    }

    public void Back(View view) {
        finish();
        return;
    }

    public void ChooseCameroGalery(View view) {
        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.alert_choosecamgal);


        final CardView tbnChooseCam =(CardView) dialog.findViewById(R.id.choose_cam);
        final CardView tbnChooseGal =(CardView) dialog.findViewById(R.id.choose_gal);


        tbnChooseCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageCamera();
                dialog.dismiss();
            }
        });

        tbnChooseGal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageGallery();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
