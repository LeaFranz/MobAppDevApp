package at.mobappdev.flytta.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import at.mobappdev.flytta.MainActivity
import at.mobappdev.flytta.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var progressBar:ProgressBar? = null
    private lateinit var userName:String
    private lateinit var email:String
    private lateinit var password:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()

        alreadyAccountTextView.setOnClickListener {
            val intent = Intent("at.mobappdev.flytta.login.LoginActivity")
            startActivity(intent)
        }

        registerButton.setOnClickListener {
            closeKeyboard()
            createAccount()
        }
    }

    /**
     * Checks for logged in user and skips registration screen
     */
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun closeKeyboard() {
        val inputManager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            currentFocus?.windowToken,
            InputMethodManager.SHOW_FORCED
        )
    }

    /**
     * Firebase Authentication
     * registration
     */
    private fun createAccount() {
        progressBar = findViewById(R.id.progressBarRegister)
        progressBar?.visibility = View.VISIBLE

        userName = usernameRegisterEditText.text.toString()
        email = emailRegisterEditText.text.toString()
        password = passwordRegisterEditText.text.toString()

        if(hasInputValidationError()){
            progressBar?.visibility = View.INVISIBLE
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("register activity", "createUserWithEmail:success")
                    saveUserToDB(userName, email)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
                progressBar?.visibility = View.INVISIBLE
            }
    }

    /**
     * Checks for invalid or missing input
     * returns TRUE for invalid input
     */
    private fun hasInputValidationError():Boolean{
        var validationError = false

        if(userName.isEmpty()){
            usernameRegisterEditText.error = "Please enter text for a username."
            validationError = true
        }
        if (email.isEmpty()) {
            emailRegisterEditText.error = "Please enter text for an email address."
            validationError = true
        }
        if (password.length < 6) {
            passwordRegisterEditText.error = "Your password must have at least 6 characters."
            validationError = true
        }

       return validationError
    }

    /**
     * Firebase Firestore
     * stores user in db
     * collection: users, document: userId
     */
    private fun saveUserToDB(userName: String, email: String) {
        val db = Firebase.firestore
        val user = hashMapOf(
            "id" to auth.uid,
            "username" to userName,
            "email" to email
        )

        val userId = auth.uid

        if (userId != null) {
            db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener {
                    Log.i("Register Activity", "User added to DB")
                }
                .addOnFailureListener { e ->
                    Log.i("Register Activity", "Error adding user", e)
                }
        }
    }
}
