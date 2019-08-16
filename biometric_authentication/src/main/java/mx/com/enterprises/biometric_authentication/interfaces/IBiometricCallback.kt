package mx.com.enterprises.biometric_authentication.interfaces

/**
 *
 * @author Jose Angel.
 * @since 1
 */

interface IBiometricCallback {

    fun onAuthenticationSuccessful()
    fun onAuthenticationFailed()
    fun onAuthenticationError(errorCode: Int, errString: CharSequence)
    fun onAuthenticationCancelled()
    fun onBiometricAuthenticationInternalError(error: String)

    //fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence)
    //fun onSdkVersionNotSupported()
    //fun onBiometricAuthenticationNotSupported()
    //fun onBiometricAuthenticationNotAvailable()
    //fun onBiometricAuthenticationPermissionNotGranted()

}
