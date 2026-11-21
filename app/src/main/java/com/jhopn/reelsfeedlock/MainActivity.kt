package com.jhopn.reelsfeedlock

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private lateinit var instructionsText: TextView
    private lateinit var enableButton: Button
    private lateinit var optionsSection: View
    private lateinit var blockReelsSwitch: MaterialSwitch
    private lateinit var allowDirectSwitch: MaterialSwitch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
        instructionsText = findViewById(R.id.instructions)
        enableButton = findViewById(R.id.enableButton)
        optionsSection = findViewById(R.id.optionsSection)
        blockReelsSwitch = findViewById(R.id.blockReelsSwitch)
        allowDirectSwitch = findViewById(R.id.allowDirectSwitch)

        blockReelsSwitch.isChecked = Prefs.isBlockReels(this)
        allowDirectSwitch.isChecked = Prefs.isAllowFromDirect(this)
        blockReelsSwitch.setOnCheckedChangeListener { _, checked ->
            Prefs.setBlockReels(this, checked)
            updateUI()
        }
        allowDirectSwitch.setOnCheckedChangeListener { _, checked ->
            Prefs.setAllowFromDirect(this, checked)
        }

        // A Play Store exige um aviso claro, com consentimento, antes de mandar
        // o usuário ativar o serviço de acessibilidade.
        enableButton.setOnClickListener { showDisclosure() }

        updateUI()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun showDisclosure() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.disclosure_title)
            .setMessage(R.string.disclosure_message)
            .setCancelable(false)
            .setPositiveButton(R.string.disclosure_accept) { _, _ ->
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            }
            .setNegativeButton(R.string.disclosure_decline, null)
            .show()
    }

    private fun updateUI() {
        if (isAccessibilityServiceEnabled()) {
            val blocking = Prefs.isBlockReels(this)
            statusText.setText(R.string.status_active)
            instructionsText.setText(
                if (blocking) R.string.instructions_active else R.string.instructions_paused
            )
            enableButton.visibility = View.GONE
            optionsSection.visibility = View.VISIBLE
            allowDirectSwitch.isEnabled = blocking
        } else {
            statusText.setText(R.string.status_permission_required)
            instructionsText.setText(R.string.instructions)
            enableButton.visibility = View.VISIBLE
            optionsSection.visibility = View.GONE
        }
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        val expected = ComponentName(this, FeedLockService::class.java)
        val enabledServices = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        return enabledServices.split(':').any {
            ComponentName.unflattenFromString(it) == expected
        }
    }
}
