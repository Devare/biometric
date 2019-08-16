package mx.com.enterprises.biometric_authentication.utils

import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.NoSuchPaddingException

object ExceptionUtils {

     fun showStackTrace(exc: Exception){
        when(exc){
            is KeyStoreException,
            is NoSuchAlgorithmException,
            is NoSuchProviderException,
            is InvalidAlgorithmParameterException,
            is CertificateException,
            is IOException ->{
                exc.printStackTrace()
            }
            else -> throw exc
        }
    }

    fun showRuntimeException(exc: Exception): Nothing {
        when(exc){
            is NoSuchPaddingException,
            is KeyStoreException,
            is CertificateException,
            is UnrecoverableKeyException,
            is RuntimeException,
            is NoSuchAlgorithmException,
            is InvalidKeyException ->{
                throw RuntimeException("Failed to init Cipher", exc)
            }
            else -> throw exc
        }
    }
}