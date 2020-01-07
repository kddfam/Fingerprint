package com.kdd.fingerprint

import android.content.DialogInterface
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.CancellationSignal
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var executor: Executor
    private lateinit var mButton : Button
    lateinit var biometricPrompt: BiometricPrompt
    lateinit var mToast: Toast

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mButton = findViewById(R.id.authButton)
        executor = Executors.newSingleThreadExecutor()

        biometricPrompt = BiometricPrompt.Builder(this)
            .setDescription("Please verify yourself")
            .setTitle("Authentication Required")
            .setNegativeButton("CANCEL", executor,
                DialogInterface.OnClickListener { dialog, _ -> dialog?.dismiss() })
            .build()

        val activity : MainActivity = this
        mButton.setOnClickListener {
            biometricPrompt.authenticate(android.os.CancellationSignal(), executor, @RequiresApi(Build.VERSION_CODES.P)
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    activity.runOnUiThread {
                        Runnable { mToast = Toast.makeText(applicationContext, "Authentication Successful", Toast.LENGTH_LONG)
                                    mToast.show()}
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    activity.finish()
                }
            })
        }
    }
}
