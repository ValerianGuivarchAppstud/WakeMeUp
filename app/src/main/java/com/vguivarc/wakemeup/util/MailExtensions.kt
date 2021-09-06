package com.vguivarc.wakemeup.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import com.vguivarc.wakemeup.R

/**
 * Checks email address against Patterns.EMAIL_ADDRESS pattern
 */
fun String.isValidEmail(): Boolean {
    val pattern = Patterns.EMAIL_ADDRESS
    return pattern.matcher(this.trim()).matches()
}

/**
 * Checks email address against Patterns.EMAIL_ADDRESS pattern
 */
@Suppress("MagicNumber")
fun String.isValidPassword(): Boolean {
    return this.count() > 7
}

/**
 * Checks email address against Patterns.EMAIL_ADDRESS pattern
 */
@Suppress("MagicNumber")
fun String.isValidUsername(): Boolean {

    return this.count() > 3
}

/**
 * Open email app with contact address pre-filled
 */
fun Context.contactUs() {
    val addresses: Array<String> =
        arrayOf(getString(R.string.contact_email_address))
    val subject: String = getString(R.string.contact_email_subject)
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:") // only email apps should handle this
        putExtra(Intent.EXTRA_EMAIL, addresses)
        putExtra(Intent.EXTRA_SUBJECT, subject)
    }

    if (this.packageManager != null && this.packageManager.queryIntentActivities(intent, 0)
        .isNotEmpty()
    ) {
        startActivity(intent)
    } else {
        Toast.makeText(
            this,
            R.string.error_no_email_app,
            Toast.LENGTH_SHORT
        ).show()
    }
}
