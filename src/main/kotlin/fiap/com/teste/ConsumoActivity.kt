package fiap.com.teste

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import fiap.com.teste.databinding.ActivityConsumoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsumoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsumoBinding
    private lateinit var spinnerEletrodomesticos: Spinner
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsumoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        spinnerEletrodomesticos = binding.spinnerEletrodomesticos
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mutableListOf())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEletrodomesticos.adapter = adapter


        carregarEletrodomesticos()

        binding.btnAtualizarConsumo.setOnClickListener {
            val itemSelecionado = spinnerEletrodomesticos.selectedItem as? String
            if (!itemSelecionado.isNullOrEmpty()) {
                obterConsumo(itemSelecionado)
            } else {
                Toast.makeText(this, "Selecione um eletrodoméstico", Toast.LENGTH_SHORT).show()
            }
        }

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
    }

    private fun carregarEletrodomesticos() {
        RetrofitClient.instance.obterEletrodomesticos()
            .enqueue(object : Callback<List<String>> {
                override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                    if (response.isSuccessful) {
                        val eletrodomesticos = response.body() ?: emptyList()
                        adapter.clear()
                        adapter.addAll(eletrodomesticos)
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this@ConsumoActivity, "Erro ao carregar eletrodomésticos", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<String>>, t: Throwable) {
                    Toast.makeText(this@ConsumoActivity, "Erro na comunicação: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun obterConsumo(nomeItem: String) {
        RetrofitClient.instance.obterConsumo(nomeItem)
            .enqueue(object : Callback<ConsumoResponse> {
                override fun onResponse(call: Call<ConsumoResponse>, response: Response<ConsumoResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val consumo = response.body()


                        val consumoPorHora = consumo?.consumo_hora?.toDouble() ?: 0.0
                        val valorPorHora = consumo?.valor?.toDouble() ?: 0.0
                        val horasNoMes = 30
                        val consumoMensal = consumoPorHora * horasNoMes
                        val valorMensal = valorPorHora * horasNoMes

                        // Atualizar os valores na interface
                        binding.tvConsumoValor.text = """
                        Consumo de ${consumo?.nome_item}: 
                        ${String.format("%.2f", consumoPorHora)} kWh/h
                        Valor por hora: R$ ${String.format("%.2f", valorPorHora)}
                        Total mensal: R$ ${String.format("%.2f", valorMensal)}
                    """.trimIndent()
                    } else {
                        Toast.makeText(this@ConsumoActivity, "Erro ao obter consumo", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ConsumoResponse>, t: Throwable) {
                    Toast.makeText(this@ConsumoActivity, "Erro na comunicação: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }

}


