package website.copyandpaste.bottombarnavigationwithnavigationdrawer.Helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {

    private Context context;
    private SharedPreferences preferences;
    private String FILE_NAME = "app.preference";
    private int MODE = 0;
    private SharedPreferences.Editor editor;


    private final String USER_EMAIL_LOGIN = "email_usuario_logado";
    private final String PASSWORD_USER_LOGIN = "senha_usuario_logado";

    public Preference(Context parameterContext) {
        context = parameterContext;

        preferences = context.getSharedPreferences(FILE_NAME, MODE);

        //associar o nosso preferencees.edit()
        editor = preferences.edit();
    }


    public void savedPreferenceUser(String email, String password) {


        //salvar dentro do nosso arquivo de preferencias o email e password do usuario

        editor.putString(USER_EMAIL_LOGIN, email);
        editor.putString(PASSWORD_USER_LOGIN, password);
        editor.commit();

    }

    public String getUserEmailLogin() {

        return preferences.getString(USER_EMAIL_LOGIN, null);
    }

    public String getUserpasswordLogin() {

        return preferences.getString(PASSWORD_USER_LOGIN, null);
    }


}
