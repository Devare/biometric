package mx.com.enterprises.biometric_authentication.builder

import android.content.Context
import mx.com.enterprises.biometric_authentication.BiometricManager

/**
 *
 * @author Jose Angel.
 * @since 1
 */

class BiometricBuilder(var context: Context) {

    var title: String? = null
    var subtitle: String? = null
    var description: String? = null
    var negativeBtnText: String? = null

    fun setTitle(title: String): BiometricBuilder {
        this.title = title
        return this
    }

    fun setSubtitle(subtitle: String): BiometricBuilder {
        this.subtitle = subtitle
        return this
    }

    fun setDescription(description: String): BiometricBuilder {
        this.description = description
        return this
    }


    fun setNegativeBtnText(negativeBtnText: String): BiometricBuilder {
        this.negativeBtnText = negativeBtnText
        return this
    }

    fun build(): BiometricManager {
        return BiometricManager(this)
    }
}