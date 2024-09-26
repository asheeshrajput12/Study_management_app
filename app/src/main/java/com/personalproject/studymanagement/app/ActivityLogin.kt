package com.personalproject.studymanagement.app

import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.personalproject.studymanagement.R
import com.personalproject.studymanagement.common.CommonFunctions.READ_CALL_LOG_PERMISSION_REQUEST
import com.personalproject.studymanagement.common.CommonFunctions.getCallLogsAndSave
import com.personalproject.studymanagement.common.CommonFunctions.saveCallLogsToFile
import com.personalproject.studymanagement.common.CustomSearchableListDialog
import com.personalproject.studymanagement.common.ImageUtils
import com.personalproject.studymanagement.databinding.ActivityLoginBinding
import timber.log.Timber


class ActivityLogin: AppCompatActivity() , CustomSearchableListDialog.OnItemSelectedListener{
    lateinit var binding:ActivityLoginBinding

    // Initialize variables
    var btSignIn: SignInButton? = null
    var googleSignInClient: GoogleSignInClient? = null
    var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree()) // Or your preferred Timber tree
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Assign variable
        btSignIn = findViewById(R.id.bt_sign_in)

        // Initialize sign in options the client-id is copied form google-services.json file
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("80846934638-a1i4mdjqbmajqiv2hpi1ldq1s01352h4.apps.googleusercontent.com")
            .requestEmail()
            .build()

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        btSignIn!!.setOnClickListener { // Initialize sign in intent
            val intent: Intent = googleSignInClient!!.signInIntent
            // Start activity for result
            startActivityForResult(intent, 100)
        }

        // Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        // Initialize firebase user
        val firebaseUser: FirebaseUser? = firebaseAuth!!.currentUser
        // Check condition
        if (firebaseUser != null) {
            // When user already sign in redirect to profile activity
            startActivity(
                Intent(
                    this,
                    ActivityMain::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }

    override fun onItemSelected(item: String) {
        try {
            Log.d("TAG", "onItemSelected: $item")

        }catch (e:Exception){
            e.printStackTrace()
        }
    }
   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    val username = credential.id
                    val password = credential.password
                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate
                            // with your backend.
                            Log.d(TAG, "Got ID token.")
                        }

                        password != null -> {
                            // Got a saved username and password. Use them to authenticate
                            // with your backend.
                            Log.d(TAG, "Got password.")
                        }

                        else -> {
                            // Shouldn't happen.
                            Log.d(TAG, "No ID token or password!")
                        }
                    }
                } catch (e: ApiException) {
                    e.printStackTrace()
                    // ...
                }
            }

        }
    }*/

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        if (requestCode == READ_CALL_LOG_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with saving call logs
                getCallLogsAndSave(this@ActivityLogin)
            } else {
                // Permission denied, handle accordingly (e.g., show a message to the user)
                Log.w("CallLogs", "READ_CALL_LOG permission denied")
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Check condition
        if (requestCode == 100) {
            // When request code is equal to 100 initialize task
            val signInAccountTask: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)

            // check condition
            try {
                if (signInAccountTask.isSuccessful) {
                    // When google sign in successful initialize string
                    val s = "Google sign in successful"
                    // Display Toast
                    displayToast(s)
                    // Initialize sign in account
                    try {
                        // Initialize sign in account
                        val googleSignInAccount = signInAccountTask.getResult(ApiException::class.java)
                        // Check condition
                        if (googleSignInAccount != null) {
                            // When sign in account is not equal to null initialize auth credential
                            val authCredential: AuthCredential = GoogleAuthProvider.getCredential(
                                googleSignInAccount.idToken, null
                            )
                            // Check credential
                            firebaseAuth!!.signInWithCredential(authCredential)
                                .addOnCompleteListener(this) { task ->
                                    // Check condition
                                    if (task.isSuccessful) {
                                        // When task is successful redirect to profile activity
                                        startActivity(
                                            Intent(
                                                this,
                                                ActivityMain::class.java
                                            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        )
                                        // Display Toast
                                        displayToast("Firebase authentication successful")
                                    } else {
                                        // When task is unsuccessful display Toast
                                        displayToast(
                                            "Authentication Failed :" + task.exception!!.message
                                        )
                                    }
                                }
                        }
                    } catch (e: ApiException) {
                        e.printStackTrace()
                    }
                }else{
                        val exception = signInAccountTask.exception
                        if (exception is ApiException) {
                            exception.printStackTrace()
                        }
                }

            }catch (e:Exception){
                e.printStackTrace()
            }

        }
    }

    private fun displayToast(s: String) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
    }
}
