package mx.com.enterprises.devare.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import mx.com.enterprises.biometric_authentication.BiometricManager
import mx.com.enterprises.biometric_authentication.builder.BiometricBuilder
import mx.com.enterprises.biometric_authentication.interfaces.IBiometricCallback
import mx.com.enterprises.devare.R

class MainActivity : AppCompatActivity(), IBiometricCallback, View.OnClickListener {
    lateinit var mBiometricManager: BiometricManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startSetOnclickListener()
    }

    private fun startSetOnclickListener() {
        btn_authenticate_four_params.setOnClickListener(this)
        btn_authenticate_three_params.setOnClickListener(this)
        btn_authenticate_two_params.setOnClickListener(this)
        btn_authenticate_title_require.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        when(v!!.id){
            btn_authenticate_four_params.id ->{
                startAutenticateFourParameters()
            }
            btn_authenticate_three_params.id->{
                startAutenticateThreeParameters()
            }
            btn_authenticate_two_params.id->{
                startAutenticateTwoParameters()
            }
            btn_authenticate_title_require.id->{
                startAutenticateTitleRequire()
            }

        }

        mBiometricManager.authenticate(this)

    }

    fun startAutenticateFourParameters(){
        mBiometricManager = BiometricBuilder(this)
            .setTitle(getString(R.string.biometric_title))
            .setSubtitle(getString(R.string.biometric_subtitle))
            .setDescription(getString(R.string.biometric_description))
            .setNegativeBtnText(getString(R.string.biometric_negative_button_text))
            .build()

    }

    fun startAutenticateThreeParameters(){
        mBiometricManager = BiometricBuilder(this)
            .setTitle(getString(R.string.biometric_title))
            .setSubtitle(getString(R.string.biometric_subtitle))
            .setNegativeBtnText(getString(R.string.biometric_negative_button_text))
            .build()
    }

    fun startAutenticateTwoParameters(){
        mBiometricManager = BiometricBuilder(this)
            .setTitle(getString(R.string.biometric_title))
            .setNegativeBtnText(getString(R.string.biometric_negative_button_text))
            .build()
    }

    fun startAutenticateTitleRequire(){
        mBiometricManager = BiometricBuilder(this)
            .setNegativeBtnText(getString(R.string.biometric_negative_button_text))
            .build()
    }

    override fun onBiometricAuthenticationInternalError(error: String) {
        Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
    }

    override fun onAuthenticationFailed() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_failure), Toast.LENGTH_LONG).show();
    }

    override fun onAuthenticationCancelled() {
        Toast.makeText(applicationContext, getString(R.string.biometric_cancelled), Toast.LENGTH_LONG).show()
        mBiometricManager.cancelAuthentication()
    }

    override fun onAuthenticationSuccessful() {
        Toast.makeText(applicationContext, getString(R.string.biometric_success), Toast.LENGTH_LONG).show()
    }


    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        Toast.makeText(getApplicationContext(), errString, Toast.LENGTH_LONG).show();
    }

    //OTHERS
/*
    override fun onSdkVersionNotSupported() {
        Toast.makeText(applicationContext, getString(R.string.biometric_error_sdk_not_supported), Toast.LENGTH_LONG)
            .show()
    }

    override fun onBiometricAuthenticationNotSupported() {
        Toast.makeText(
            applicationContext,
            getString(R.string.biometric_error_hardware_not_supported),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onBiometricAuthenticationNotAvailable() {
        Toast.makeText(
            applicationContext,
            getString(R.string.biometric_error_fingerprint_not_available),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onBiometricAuthenticationPermissionNotGranted() {
        Toast.makeText(
            applicationContext,
            getString(R.string.biometric_error_permission_not_granted),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence) {
        Toast.makeText(getApplicationContext(), helpString, Toast.LENGTH_LONG).show();
    }*/
}
