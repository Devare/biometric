package mx.com.enterprises.biometric_authentication

import android.annotation.TargetApi
import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import mx.com.enterprises.biometric_authentication.builder.BiometricBuilder
import mx.com.enterprises.biometric_authentication.interfaces.IBiometricCallback
import mx.com.enterprises.biometric_authentication.utils.BiometricUtils
import mx.com.enterprises.biometric_authentication.v23.BiometricManagerV23
import mx.com.enterprises.biometric_authentication.v28.BiometricCallbackV28

/**
 *
 * @author Jose Angel.
 * @since 1
 */

open class BiometricManager(biometricBuilder: BiometricBuilder) : BiometricManagerV23() {


    protected var mCancellationSignal = CancellationSignal()

    init {
        this.context = biometricBuilder.context
        this.title = biometricBuilder.title
        this.subtitle = biometricBuilder.subtitle
        this.description = biometricBuilder.description
        this.negativeButtonText = biometricBuilder.negativeBtnText
    }

    fun authenticate(biometricCallback: IBiometricCallback) {

        if (negativeButtonText == null) {
            biometricCallback.onBiometricAuthenticationInternalError(context!!.getString(R.string.text_info_internal_error_btn_negative))
            return
        }


        if (!BiometricUtils.isSdkVersionSupported) {
            //biometricCallback.onSdkVersionNotSupported()
            return
        }

        if (!BiometricUtils.isPermissionGranted(context!!)) {
            // biometricCallback.onBiometricAuthenticationPermissionNotGranted()
            return
        }

        if (!BiometricUtils.isHardwareSupported(context!!)) {
            //biometricCallback.onBiometricAuthenticationNotSupported()
            return
        }

        if (!BiometricUtils.isFingerprintAvailable(context!!)) {
            // biometricCallback.onBiometricAuthenticationNotAvailable()
            return
        }

        displayBiometricDialog(biometricCallback)
    }

    private fun displayBiometricDialog(biometricCallback: IBiometricCallback) {
        //Se inabilita displayBiometricPrompt for falla de seguridad con el faceId en ciertos dispositivos que lo soportan
        /*  if (BiometricUtils.isBiometricPromptEnabled)  displayBiometricPrompt(biometricCallback) else  displayBiometricPromptV23(biometricCallback)*/
        displayBiometricPromptV23(biometricCallback)
    }

    @TargetApi(Build.VERSION_CODES.P) //SOPORTE PARA ANDROID P=28
    private fun displayBiometricPrompt(biometricCallback: IBiometricCallback) {
        val executor = context!!.mainExecutor
        val biometricPrompt = BiometricPrompt.Builder(context).setTitle(title.toString())

        if (!subtitle.isNullOrEmpty()) {
            biometricPrompt.setSubtitle(subtitle!!)
        }
        if (!description.isNullOrEmpty()) {
            biometricPrompt.setDescription(description!!)
        }

        biometricPrompt.run {

            if (!subtitle.isNullOrEmpty()) {
                biometricPrompt.setSubtitle(subtitle!!)
            }
            if (!description.isNullOrEmpty()) {
                biometricPrompt.setDescription(description!!)
            }

            setNegativeButton(
                negativeButtonText!!,
                executor,
                DialogInterface.OnClickListener { dialog, which -> biometricCallback.onAuthenticationCancelled() })
                .build()
                .authenticate(mCancellationSignal, executor, BiometricCallbackV28(biometricCallback))
        }
    }

    fun cancelAuthentication() {
        if (BiometricUtils.isBiometricPromptEnabled) {
            if (!mCancellationSignal.isCanceled)
                mCancellationSignal.cancel()
        } else {
            if (!mCancellationSignalV23.isCanceled)
                mCancellationSignalV23.cancel()
        }
    }
}
