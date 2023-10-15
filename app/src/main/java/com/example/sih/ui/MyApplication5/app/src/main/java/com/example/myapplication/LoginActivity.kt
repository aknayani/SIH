package com.example.myapplication

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class LoginActivity : ComponentActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001 // Define your request code
    private lateinit var startForResult: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
            )
            {
                MaterialTheme {
                    Login()
                }
            }
        }
        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Sign-in was successful, and user data is available in the Intent
                val data: Intent? = result.data

                // Now you can extract user information, typically Google account details
                val account = GoogleSignIn.getSignedInAccountFromIntent(data)
                val displayName = account?.result?.displayName
                val email = account?.result?.email

                // You can use this information for further processing
                // For example, you can display the user's name or email in your app
                // Check if the email matches an authorized user in your app's database
                if (email == "dgpatel828@gmail.com") {
                    // The user is authorized; grant access or perform further actions
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                } else {
                    // The user is not authorized; display a message or take appropriate action
                    Toast.makeText(this, "Unauthorized user", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Sign-in was not successful; handle the error or perform any other actions
                // For example, you can display a message to the user indicating the failure
                Toast.makeText(this, "Sign-in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logged(username: String, password: String) {

        if (username == "aknayani" && password == "123") {
            Toast.makeText(this, "logged", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Wrong Credential", Toast.LENGTH_SHORT).show()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Login() {
        var passwordVisibility by remember { mutableStateOf(false) }
        val username = remember { mutableStateOf(TextFieldValue()) }
        val password = remember { mutableStateOf(TextFieldValue()) }
//        val focusRequester = remember { FocusRequester() }

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally, // Added horizontal alignment
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo), // Specify the image resource
                contentDescription = null, // Provide a content description
                modifier = Modifier
                    .size(200.dp) // Adjust the size as needed
                    .padding(bottom = 16.dp) // Adjust the padding as needed
            )
            Text(
                text = "Log in",
                fontSize = 40.sp,
                color = Color.Blue,
                fontWeight = FontWeight.Bold,
            )

            OutlinedTextField(
                value = username.value,
                onValueChange = {
                    username.value = it
                },
                label = {
                    Text(text = "Username")
                },
                placeholder = {
                    Text(text = "Enter username")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,


                )

            OutlinedTextField(
                value = password.value,
                onValueChange = {
                    password.value = it
                },
                label = {
                    Text(text = "Password")
                },
                placeholder = {
                    Text(text = "Enter password")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation()
            )



            OutlinedButton(
                onClick = {
                    passwordVisibility = !passwordVisibility
                },
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
            ) {
                Text(text = if (passwordVisibility) "Hide Password" else "Show Password")
            }
            OutlinedButton(
                onClick = {
                    logged(username.value.text, password.value.text)
                },
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
            ) {
                Text(text = "Login")
            }
            OutlinedButton(
                onClick = {
                    val signInIntent = googleSignInClient.signInIntent
                    startForResult.launch(signInIntent)
                },
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
            ) {
                Text(text = "Sign in with Google")
            }
        }
    }
}