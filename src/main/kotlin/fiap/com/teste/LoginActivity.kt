package fiap.com.teste

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import fiap.com.teste.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }

        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Após autenticação no Firebase, verifica na API
                            verifyUserInOracle(email, password)
                        } else {
                            Toast.makeText(
                                this,
                                "Erro no login: ${task.exception?.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun verifyUserInOracle(email: String, password: String) {
        val user = User(email, password)

        // Faz a chamada à API
        RetrofitClient.instance.checkUser(email, password)
            .enqueue(object : Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    Log.d("LoginActivity", "Response: ${response.body()}")  // Verifique o conteúdo
                    if (response.isSuccessful && response.body() == true) {

                        val intent = Intent(this@LoginActivity, ConsumoActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        val message = "Usuário não encontrado."
                        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Falha na comunicação: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

    }


}
