package mx.com.enterprises.devare.example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast.*
import kotlinx.android.synthetic.main.activity_main.*
import mx.com.enterprises.biometric_authentication.BiometricManager
import mx.com.enterprises.biometric_authentication.builder.BiometricBuilder
import mx.com.enterprises.biometric_authentication.interfaces.IBiometricCallback
import mx.com.enterprises.devare.R

/**
 *
 * @author Jose Angel.
 * @since 1
 */

class MainActivity : AppCompatActivity(), IBiometricCallback, View.OnClickListener {
    private lateinit var mBiometricManager: BiometricManager

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

    private fun startAutenticateFourParameters(){
        mBiometricManager = BiometricBuilder(this)
            .setTitle(getString(R.string.biometric_title))
            .setSubtitle(getString(R.string.biometric_subtitle))
            .setDescription(getString(R.string.biometric_description))
            .setNegativeBtnText(getString(R.string.biometric_negative_button_text))
            .build()

    }

    private fun startAutenticateThreeParameters(){
        mBiometricManager = BiometricBuilder(this)
            .setTitle(getString(R.string.biometric_title))
            .setSubtitle(getString(R.string.biometric_subtitle))
            .setNegativeBtnText(getString(R.string.biometric_negative_button_text))
            .build()
    }

    private fun startAutenticateTwoParameters(){
        mBiometricManager = BiometricBuilder(this)
            .setTitle(getString(R.string.biometric_title))
            .setNegativeBtnText(getString(R.string.biometric_negative_button_text))
            .build()
    }

    private fun startAutenticateTitleRequire(){
        mBiometricManager = BiometricBuilder(this)
            .setNegativeBtnText(getString(R.string.biometric_negative_button_text))
            .build()
    }

    override fun onBiometricAuthenticationInternalError(error: String) = makeText(applicationContext, error, LENGTH_LONG).show()

    override fun onAuthenticationFailed() = makeText(applicationContext, getString(R.string.biometric_failure), LENGTH_LONG).show()

    override fun onAuthenticationCancelled() {
        makeText(applicationContext, getString(R.string.biometric_cancelled), LENGTH_LONG).show()
        mBiometricManager.cancelAuthentication()
    }

    override fun onAuthenticationSuccessful() = makeText(applicationContext, getString(R.string.biometric_success), LENGTH_LONG).show()

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) = makeText(applicationContext, errString, LENGTH_LONG).show()

}
