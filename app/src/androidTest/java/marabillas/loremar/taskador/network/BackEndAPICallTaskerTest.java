package marabillas.loremar.taskador.network;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import marabillas.loremar.taskador.network.resuilthandlers.SignupTest;

@RunWith(AndroidJUnit4.class)
@LargeTest

public class BackEndAPICallTaskerTest {
    @Test
    public void signup() {
        final String username = "test3";
        final String password = "password";
        SignupTest signupTest = new SignupTest();

        BackEndAPICallTasker.getInstance().signup(signupTest, username, password);

        signupTest.waitForResults();
    }
}