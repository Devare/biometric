package mx.com.enterprises.biometric_authentication.v23

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Handler
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.CancellationSignal
import mx.com.enterprises.biometric_authentication.R
import mx.com.enterprises.biometric_authentication.interfaces.IBiometricCallback
import mx.com.enterprises.biometric_authentication.utils.ExceptionUtils.showRuntimeException
import mx.com.enterprises.biometric_authentication.utils.ExceptionUtils.showStackTrace
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 *
 * @author Jose Angel.
 * @since 1
 */

@Suppress("DEPRECATION")
@TargetApi(Build.VERSION_CODES.M)
open class BiometricManagerV23 {

    private var cipher: Cipher? = null
    private var keyStore: KeyStore? = null
    private var keyGenerator: KeyGenerator? = null
    private var cryptoObject: FingerprintManagerCompat.CryptoObject? = null


    protected var context: Context? = null

    protected var title: String? = null
    protected var subtitle: String? = null
    protected var description: String? = null
    protected var negativeButtonText: String? = null
    private var biometricDialogV23: BiometricDialogV23? = null
    protected var mCancellationSignalV23 = CancellationSignal()

    companion object {
        private val KEY_NAME = UUID.randomUUID().toString()
        val DELAY = 520
    }


    fun displayBiometricPromptV23(biometricCallback: IBiometricCallback) {
        generateKey()

        if (initCipher()) {

            cryptoObject = FingerprintManagerCompat.CryptoObject(cipher!!)
            val fingerprintManagerCompat = FingerprintManagerCompat.from(context!!)

            fingerprintManagerCompat.authenticate(
                cryptoObject, 0, mCancellationSignalV23,
                object : FingerprintManagerCompat.AuthenticationCallback() {
                    override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
                        super.onAuthenticationError(errMsgId, errString)

                        if (errMsgId != 5) {
                            updateStatus(errString.toString(), R.color.dialog_status_error)
                            if (errMsgId == 7) {
                                dismissDialog()
                            }
                        }

                        biometricCallback.onAuthenticationError(errMsgId, errString!!)
                    }

                    override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence?) {
                        super.onAuthenticationHelp(helpMsgId, helpString)
                        updateStatus(helpString.toString(), R.color.dialog_status_error)
                    }

                    override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
                        super.onAuthenticationSucceeded(result)
                        updateStatus(
                            context!!.getString(R.string.biometric_status_success),
                            R.color.dialog_status_sucess
                        )
                        Handler().postDelayed({
                            dismissDialog()
                            biometricCallback.onAuthenticationSuccessful()
                        }, DELAY.toLong())

                    }


                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        updateStatus(context!!.getString(R.string.biometric_failed), R.color.dialog_status_error)
                        biometricCallback.onAuthenticationFailed()
                    }
                },
                null
            )
            displayBiometricDialog(biometricCallback)
        }
    }


    private fun displayBiometricDialog(biometricCallback: IBiometricCallback) {
        biometricDialogV23 = BiometricDialogV23(context!!, biometricCallback)
        biometricDialogV23!!.setTitle(title)
        biometricDialogV23!!.setSubtitle(subtitle)
        biometricDialogV23!!.setDescription(description)
        biometricDialogV23!!.setButtonText(negativeButtonText!!)
        biometricDialogV23!!.show()
    }


    private fun dismissDialog() {
        if (biometricDialogV23 != null) {
            biometricDialogV23!!.dismiss()
        }
    }

    private fun updateStatus(status: String, statusColor: Int) {
        if (biometricDialogV23 != null) {
            biometricDialogV23!!.updateStatus(status, statusColor)
        }
    }

    private fun generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore!!.load(null)

            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            keyGenerator!!.init(
                KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build()
            )

            keyGenerator!!.generateKey()

        } catch (exc: Exception ) {
            showStackTrace(exc)
        }
    }


    private fun initCipher(): Boolean {
        try {
            cipher = Cipher.getInstance(
                KeyProperties.KEY_ALGORITHM_AES + "/"
                        + KeyProperties.BLOCK_MODE_CBC + "/"
                        + KeyProperties.ENCRYPTION_PADDING_PKCS7
            )

        } catch (e: NoSuchAlgorithmException) {
           showRuntimeException(e)
        }

        try {
            keyStore!!.load(null)
            val key = keyStore!!.getKey(KEY_NAME, null) as SecretKey
            cipher!!.init(Cipher.ENCRYPT_MODE, key)
            return true

        } catch (e: KeyPermanentlyInvalidatedException) {
            return false
        } catch (exc: Exception) {
          showRuntimeException(exc)
        }
    }
}
