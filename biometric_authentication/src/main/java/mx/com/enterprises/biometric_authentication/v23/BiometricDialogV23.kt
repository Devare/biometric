package mx.com.enterprises.biometric_authentication.v23

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.view_bottom_sheet.*
import mx.com.enterprises.biometric_authentication.R

import mx.com.enterprises.biometric_authentication.interfaces.IBiometricCallback

/**
 *
 * @author Jose Angel.
 * @since 1
 */

class BiometricDialogV23 : BottomSheetDialog, View.OnClickListener {


    private lateinit var biometricCallback: IBiometricCallback

    constructor(context: Context) : super(context, R.style.BottomSheetDialogTheme) {
        setDialogView()
    }

    constructor(context: Context, biometricCallback: IBiometricCallback) : super(
        context,
        R.style.BottomSheetDialogTheme
    ) {
        this.biometricCallback = biometricCallback
        setDialogView()
    }

    constructor(context: Context, theme: Int) : super(context, theme) {}

    protected constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener
    ) : super(context, cancelable, cancelListener) {
    }

    @SuppressLint("InflateParams")
    private fun setDialogView() {
        val bottomSheetView = layoutInflater.inflate(R.layout.view_bottom_sheet, null)
        setContentView(bottomSheetView)
        btn_cancel!!.setOnClickListener(this)
        updateLogo()
    }

    //Set the title to display (Required)
    fun setTitle(title: String?) {
        val titleIsCandidateShow = !title.isNullOrEmpty()
        item_title.text= if(titleIsCandidateShow) title else getApplicationName()
    }

    fun setSubtitle(subtitle: String?) {

        val subtitleIsCandidateShow = !subtitle.isNullOrEmpty()
        item_subtitle.visibility = if (subtitleIsCandidateShow) View.VISIBLE else View.GONE

        if (subtitleIsCandidateShow && item_subtitle.visibility == View.VISIBLE) {
            item_subtitle!!.text = subtitle
        }
    }

    fun setDescription(description: String?) {

        val descriptionIsCandidateShow = !description.isNullOrEmpty()
        item_description.visibility = if (descriptionIsCandidateShow) View.VISIBLE else View.GONE

        if (descriptionIsCandidateShow && item_description.visibility == View.VISIBLE) {
            item_description!!.text = description
        }
    }

    //Set the text for the negative button(Required).
    fun setButtonText(negativeButtonText: String) {
        btn_cancel!!.text = if (negativeButtonText.isEmpty()) context.getString(R.string.biometric_negative_button_text) else negativeButtonText
    }

    fun updateStatus(status: String?, statusColor:Int) {
        val statusIsCandidateShow = !status.isNullOrEmpty()
        item_status!!.text = status

        item_status.visibility = if (statusIsCandidateShow) View.VISIBLE else View.GONE

        if (statusIsCandidateShow && item_status.visibility == View.VISIBLE) {
            item_status!!.text = status
            item_status.setTextColor(ContextCompat.getColor(context, statusColor))
        }
    }

    private fun updateLogo() = try {
        val drawable = context.packageManager.getApplicationIcon(context.packageName)
        img_logo!!.setImageDrawable(drawable)

    } catch (e: Exception) {
        e.printStackTrace()
    }

    override fun onClick(view: View) {
        dismiss()
        biometricCallback.onAuthenticationCancelled()
    }

    private fun getApplicationName(): String = this.context.applicationInfo.loadLabel(context.packageManager).toString()

}
