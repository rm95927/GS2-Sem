package fiap.com.teste

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import fiap.com.teste.R.layout.activity_main


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)

        // Botão Entrar
        val btnEntrar: Button = findViewById(R.id.btnEntrar)
        btnEntrar.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Botão Registrar
        val btnRegistrar: Button = findViewById(R.id.btnRegistrar)
        btnRegistrar.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


    }
}


