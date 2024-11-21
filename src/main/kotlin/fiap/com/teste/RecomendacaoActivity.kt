package fiap.com.teste

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import fiap.com.teste.databinding.ActivityRecomendacaoBinding

class RecomendacaoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecomendacaoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecomendacaoBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // Exibindo recomendações de como economizar energia
        val recomendacoes = """
        Recomendações Importantes:
        
            1. Desligue aparelhos quando não estiverem em uso.
            2. Use lâmpadas LED, elas consomem menos energia.
            3. Desconecte os dispositivos eletrônicos da tomada quando não os estiver usando.
            4. Utilize a geladeira de forma eficiente, evitando abrir a porta desnecessariamente.
            5. Prefira o uso de ventiladores em vez de ar-condicionado.
            6. Aproveite a luz natural durante o dia, evitando ligar as lâmpadas.
            7. Faça uso de aparelhos com selo de eficiência energética.
            8. Evite deixar os eletrodomésticos em standby, pois eles continuam consumindo energia.
            
        Veja Também:
        6 dicas de como economizar energia em casa - Enel Brasil: https://www.youtube.com/watch?v=rtQXnIIY85A
        O que mais consome energia em casa e como economizar - BBC News Brasil: https://www.youtube.com/watch?v=p0Pcj2XLSPs    
            
    Em caso de dúvidas, entre em contato com o nosso suporte:
        """

        binding.tvRecomendacoes.text = recomendacoes



        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_consumo -> {
                    // Navega para a ConsumoActivity (Home)
                    val intent = Intent(this, ConsumoActivity::class.java)
                    startActivity(intent)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_dashboard -> {
                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_recomendacao -> {
                    // Navega para a SettingsActivity
                    val intent = Intent(this, RecomendacaoActivity::class.java)
                    startActivity(intent)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
    }

        val btnCallSupport: Button = findViewById(R.id.btnCallSupport)
        btnCallSupport.setOnClickListener {
            // Número do suporte
            val phoneNumber = "11 3385-8010" // Substitua pelo número de suporte real
            // Intent para abrir o discador
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNumber")
            startActivity(intent)
        }
    }
}
