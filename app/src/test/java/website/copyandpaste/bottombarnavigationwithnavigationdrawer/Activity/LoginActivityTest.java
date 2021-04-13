package website.copyandpaste.bottombarnavigationwithnavigationdrawer.Activity;

import org.junit.Test;

import static com.facebook.internal.CallbackManagerImpl.RequestCodeOffset.Login;
import static org.junit.Assert.*;

public class LoginActivityTest {
    // emailValidator_CorrectEmailSimple_ReturnsTrue()

    @Test
    public void login() {
        assertThat(Login.isValidEmail("name@email.com")).isTrue();
    }

    @Test
    public void abrirRegister() {
    }
}