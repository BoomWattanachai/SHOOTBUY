package com.google.firebase.ml.md.kotlin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ml.md.R
import com.google.firebase.ml.md.ShootBuyMainActivity
import com.google.firebase.ml.md.kotlin.EntityModels.UserData.User
import com.google.firebase.ml.md.kotlin.Models.Service.UserData.IfUserExist

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private var TAG = "Google Signin"
    private var RC_SIGN_IN = 1000
    private var googleSignInButton: SignInButton? = null
    private var signOutBtn: Button? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private lateinit var auth: FirebaseAuth

    private var pref: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var buyerUuid: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        signOutBtn = findViewById(R.id.signOutBtn)
        googleSignInButton = findViewById(R.id.sign_in_button)
//        signOutBtn?.setOnClickListener(this)
        googleSignInButton?.setOnClickListener(this)

        pref = getSharedPreferences("SP_USER_DATA", Context.MODE_PRIVATE)
        buyerUuid = pref!!.getString("UUID", "")
        val logout = intent.getStringExtra("Logout")
        if (logout != null) {
            signOut()
        } else if (buyerUuid != "") {
            startActivity(Intent(this, ShootBuyMainActivity::class.java))
            finish()
        }


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()


    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        //updateUI(currentUser)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.sign_in_button -> {
                signIn()
            }
//            R.id.signOutBtn ->{
//                signOut()
//            }
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    private fun signOut() {
        mGoogleSignInClient?.signOut()
        pref = getSharedPreferences("SP_USER_DATA", Context.MODE_PRIVATE)
        pref!!.edit().remove("UUID").apply()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        val user = auth.currentUser
                        Log.d(TAG, "signInWithCredential:success" + user?.email)
                        ifUserExist(user)
//                    updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
//                    updateUI(null)
                    }

                    // ...
                }
    }

    private fun ifUserExist(user: FirebaseUser?) {
        pref = getSharedPreferences("SP_USER_DATA", Context.MODE_PRIVATE)

        editor = pref!!.edit()

        editor!!.putString("UUID", user!!.uid).apply()
        editor!!.commit()


        val insertUserDataScan = IPAddress.ipAddress + "user-data/ifUserExist/"

        var name = user.displayName!!.split(" ").toTypedArray()

        var userData = User()
        userData.uuid = user.uid
        userData.email = user.email
        userData.firstName = name[0]
        userData.lastName = name[1]

        IfUserExist(userData).execute(insertUserDataScan)

//        startActivity(Intent(this, LiveObjectDetectionActivity::class.java))
        startActivity(Intent(this, ShootBuyMainActivity::class.java))
        finish()
    }
}
