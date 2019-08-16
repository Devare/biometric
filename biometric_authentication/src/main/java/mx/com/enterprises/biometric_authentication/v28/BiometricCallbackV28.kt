package mx.com.enterprises.biometric_authentication.v28

import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.annotation.RequiresApi
import mx.com.enterprises.biometric_authentication.interfaces.IBiometricCallback

/**
 *
 * @author Jose Angel.
 * @since 1
 */

@RequiresApi(api = Build.VERSION_CODES.P)
class BiometricCallbackV28(private val biometricCallback: IBiometricCallback) : BiometricPrompt.AuthenticationCallback() {


    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        super.onAuthenticationSucceeded(result)
        biometricCallback.onAuthenticationSuccessful()
    }


    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence) {
        super.onAuthenticationHelp(helpCode, helpString)
        //biometricCallback.onAuthenticationHelp(helpCode, helpString)
    }


    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        super.onAuthenticationError(errorCode, errString)
        biometricCallback.onAuthenticationError(errorCode, errString)
    }


    override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
        biometricCallback.onAuthenticationFailed()
    }
}
