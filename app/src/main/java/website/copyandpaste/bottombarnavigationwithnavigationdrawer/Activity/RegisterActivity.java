package website.copyandpaste.bottombarnavigationwithnavigationdrawer.Activity;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

import website.copyandpaste.bottombarnavigationwithnavigationdrawer.DAO.FirebaseConfiguration;
import website.copyandpaste.bottombarnavigationwithnavigationdrawer.DataPickerFragmento;
import website.copyandpaste.bottombarnavigationwithnavigationdrawer.Models.ModelsContact;
import website.copyandpaste.bottombarnavigationwithnavigationdrawer.R;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

//    private static final Pattern PASSWORD_PATTERN =
//            Pattern.compile("^"+"(?=.*[0-9])"+ "(?=.*[a-z])"+"(?=.*[A-Z])+"+"(?=.*[@#$%^&+=])"+"(?=\\s+$])"+".{6,}"+"$" );
    private EditText lastNameEdt, firstNameEdt, phoneEdt, dateOfBirthEdt, confirmPasswordRegister, passwordRegister,  emailRegister;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth.AuthStateListener firebaseAuthAuthStateListener ;
    private ModelsContact modelsContact;
    private RadioButton rbMale, rbFemale;
    private RadioGroup mradioGroup;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        lastNameEdt = (EditText) findViewById(R.id.lastname_register);
        firstNameEdt = (EditText)findViewById(R.id.firstname_register);
        phoneEdt = (EditText) findViewById(R.id.phone_register);
        emailRegister = (EditText) findViewById(R.id.email_register);
        dateOfBirthEdt = (EditText) findViewById(R.id.day_of_birth);
        passwordRegister = (EditText) findViewById(R.id.passord_edt);
        confirmPasswordRegister = (EditText)findViewById(R.id.comfirm_password_edt);
        mradioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        rbMale = (RadioButton) findViewById(R.id.RbMale);
        rbFemale = (RadioButton) findViewById(R.id.RbFemale);
        firebaseAuth = FirebaseAuth.getInstance();
        btnRegister = (Button) findViewById(R.id.register_user);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordRegister.getText().toString().equals(confirmPasswordRegister.getText().toString())) {
                    modelsContact = new ModelsContact();

                    modelsContact.setLastName(lastNameEdt.getText().toString());
                    modelsContact.setFirstName(firstNameEdt.getText().toString());
                    modelsContact.setPhone(phoneEdt.getText().toString());
                    modelsContact.setEmail(emailRegister.getText().toString());
                    modelsContact.setDayOfbirth(dateOfBirthEdt.getText().toString());
                    modelsContact.setPassword(passwordRegister.getText().toString());
                    modelsContact.setConfirmPassword(confirmPasswordRegister.getText().toString());

                    if (rbMale.isChecked()) {
                        modelsContact.setSex("Male");
                    } else if (rbFemale.isChecked()) {
                        modelsContact.setSex("Female");
                    }

                    registerUser();

                }else {
                    Toast.makeText(RegisterActivity.this, "As senhas não se correspondem!", Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    private void registerUser() {


        firebaseAuth = FirebaseConfiguration.getFirebaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(
                modelsContact.getEmail(),
                modelsContact.getPassword()
        ).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    insertUser(modelsContact);


                } else {

                    String erroExcecao = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = "Digite uma senha mais forte, contendo no mínimo 8 caracteres e que contenha letras e números!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "O e-mail digitado é invalido, digite um novo e-mail";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Esse e-mail já está cadastro!";
                    } catch (Exception e) {
                        erroExcecao = "Erro ao efetuar o cadastro!";
                        e.printStackTrace();
                    }

                    Toast.makeText(RegisterActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private boolean insertUser(ModelsContact modelsContact) {
        try {
            databaseReference = FirebaseConfiguration.getFirebase().child("User");
            String key = databaseReference.push().getKey();
            modelsContact.getGetKeyUser();
            databaseReference.child(key).setValue(modelsContact);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
//
//    public void Register(View view) {
//        validateRegister();
//    }
//
////      validation in Register
//    private void validateRegister() {
//        final String email = emailRegister.getText().toString();
//        final Pattern NOME_PATTERN = Pattern.compile("^[a-zA-Z ]+$");
//        final Pattern SOBRENOME_PATTERN = Pattern.compile("^[a-zA-Z ]+$");
//        final String lastName = lastNameEdt.getText().toString();
//        final String password = passwordRegister.getText().toString();
//        final String confirmPassord = confirmPasswordRegister.getText().toString();
//        final String firstName = firstNameEdt.getText().toString();
//        final String phone = phoneEdt.getText().toString();
//        final String dayOfbirth = dateOfBirthEdt.getText().toString();
//
//        final String seletedSex = String.valueOf(mradioGroup.getCheckedRadioButtonId());
//        final  RadioButton radioButton = (RadioButton)findViewById(Integer.parseInt(seletedSex));
//
//
//        // validate lastname
//        if(TextUtils.isEmpty(lastName)){
//            lastNameEdt.setError("Last name field cannot be empty");
//            lastNameEdt.requestFocus();
//            return;
//        }
//        if (!NOME_PATTERN.matcher(lastName).matches()){
//            lastNameEdt.setError("Last name field cannot have number or special characters");
//            lastNameEdt.requestFocus();
//            return;
//        }
//        // validate firstname
//        if(TextUtils.isEmpty(firstName)){
//            firstNameEdt.setError("First name field cannot be empty");
//            firstNameEdt.requestFocus();
//            return;
//        }
//        if (!SOBRENOME_PATTERN.matcher(firstName).matches()){
//            firstNameEdt.setError("First name field cannot have number or special characters");
//            firstNameEdt.requestFocus();
//            return;
//        }
//        if(TextUtils.isEmpty(email)){
//            emailRegister.setError("Email field is required");
//            emailRegister.requestFocus();
//            return;
//        }
//        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
//            emailRegister.setError("Please, enter a valid email");
//            emailRegister.requestFocus();
//            return;
//        }
//        if(TextUtils.isEmpty(password)){
//            passwordRegister.setError("Password field is required");
//            passwordRegister.requestFocus();
//            return;
//        }
//        if(TextUtils.isEmpty(confirmPassord)){
//            confirmPasswordRegister.setError("Please confirm Password");
//            confirmPasswordRegister.requestFocus();
//            return;
//        }
//        if (!passwordRegister.getText().toString().equals(confirmPasswordRegister.getText().toString())) {
//            passwordRegister.setError("Password and confirm password should be match");
//            passwordRegister.requestFocus();
//            return;
//        }
//
//        if (mradioGroup.getCheckedRadioButtonId() == -1){
//            Toast.makeText(RegisterActivity.this, "Please choose your sex !", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        firebaseAuth = FirebaseConfiguration.getFirebaseAuth();
//        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//
//                    sendInfoToProfile();
//                    insertUser(modelsContact);
////
////                    if (rbFeminino.isChecked()) {
////                        usuario.setSexo("Feminino");
////                    } else if (rbMasculino.isChecked()) {
////                        usuario.setSexo("Masculino");
////                    }
//
//
//                    ModelsContact user = new ModelsContact(
//                            lastName,
//                            firstName,
//                            phone,
//                            email,
//                            dayOfbirth,
//                            seletedSex
//                    );
//                    FirebaseDatabase.getInstance().getReference("User")
//                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                        }
//                    });
//
//                    finish();
//                    Toast.makeText(RegisterActivity.this, "User registered Successfull", Toast.LENGTH_LONG).show();
//
//                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                } else {
//                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
//                        Toast.makeText(getApplicationContext(), "E-mail already registered",
//                                Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                    }
////
////                    String userId = firebaseAuth.getCurrentUser().getUid();
////                    DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(radioButton.getText().toString());
////                    currentUserDb.setValue(name);
//                }
//            }
//        });
//    }
//
//    private void sendInfoToProfile() {
//        String lastname =  lastNameEdt.getText().toString();
//        String firstname = firstNameEdt.getText().toString();
//
//
//        Intent intent = new Intent(this, RegisterActivity.class);
//        intent.putExtra("LAST_NAME_KEY", lastname);
//        intent.putExtra("FIRST_NAME_KEY", firstname);
//
//        lastNameEdt.setText("");
//        firstNameEdt.setText("");
//
//        this.startActivity(intent);
//    }
//
//    private boolean insertUser(ModelsContact modelsContact) {
//
//        try {
//            databaseReference = FirebaseConfiguration.getFirebase().child("User");
//            String key = databaseReference.push().getKey();
//            modelsContact.getGetKeyUser();
//            databaseReference.child(key).setValue(modelsContact);
//            return true;
//        } catch (Exception e){
//            e.printStackTrace();
//            return false;
//        }
//    }

    public void DataAni(View view) {
        DialogFragment dataPicker = new DataPickerFragmento();
        dataPicker.show(getSupportFragmentManager(), "data picker");
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set( Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDataString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        EditText editText = (EditText)findViewById(R.id.day_of_birth);
        editText.setText(currentDataString);
    }

    public void limpar (View view) {
        TextView lastNameEditText = findViewById(R.id.lastname_register);
        TextView firstNameEditText = findViewById(R.id.firstname_register);
        EditText phoneEditText = findViewById(R.id.phone_register);
        EditText emailEditText = findViewById(R.id.email_register);
        EditText dateOfBirth = findViewById(R.id.day_of_birth);
        EditText passwordEditText = findViewById(R.id.passord_edt);
        EditText confirmPasswordEditText = findViewById(R.id.comfirm_password_edt);
        lastNameEditText.setText("");
        firstNameEditText.setText("");
        phoneEditText.setText("");
        emailEditText.setText("");
        dateOfBirth.setText("");
        passwordEditText.setText("");
        confirmPasswordEditText.setText("");
        lastNameEditText.requestFocus();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthAuthStateListener);
    }
}
