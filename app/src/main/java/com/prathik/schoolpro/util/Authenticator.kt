package com.prathik.schoolpro.util

import com.prathik.schoolpro.util.authenticatorUtils.Base32String
import com.prathik.schoolpro.util.authenticatorUtils.totp.TOTP
import org.apache.commons.codec.binary.Hex
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.security.SecureRandom
import com.google.zxing.WriterException
import android.R
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter



class Authenticator {

    fun generateSecretKey(): String {
        val random = SecureRandom()
        val bytes = ByteArray(20)
        random.nextBytes(bytes)
        return Base32String.encode(bytes)
    }

    fun getTOTPCode(secretKey: String): String {

        val bytes = Base32String.decode(secretKey)
        val hexKey = Hex.encodeHexString(bytes)
        return TOTP.getOTP(hexKey)
    }

    fun getGoogleAuthenticatorBarCode(secretKey: String, account: String, issuer: String): String {
        try {
            return ("otpauth://totp/"
                    + URLEncoder.encode("$issuer:$account", "UTF-8").replace("+", "%20")
                    + "?secret=" + URLEncoder.encode(secretKey, "UTF-8").replace("+", "%20")
                    + "&issuer=" + URLEncoder.encode(issuer, "UTF-8").replace("+", "%20"))
        } catch (e: UnsupportedEncodingException) {
            throw IllegalStateException(e)
        }

    }


    fun generateQRBitmap(content: String, context: Context): Bitmap? {
        var bitmap: Bitmap? = null
        val width = 256
        val height = 256

        val writer = QRCodeWriter()
        try {
            val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height)//256, 256
            /* int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();*/
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    //bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);//guest_pass_background_color
                    bitmap!!.setPixel(
                        x,
                        y,
                        if (bitMatrix.get(
                                x,
                                y
                            )
                        ) Color.BLACK else context.getResources().getColor(R.color.background_light)
                    )//Color.WHITE
                }
            }
        } catch (e: WriterException) {
            e.printStackTrace()
        }

        return bitmap
    }
}