package fiap.com.teste

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import fiap.com.teste.databinding.ActivityDashboardBinding


class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private val TAG = "Firestore"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = FirebaseFirestore.getInstance()

        val consumo = db.collection("consumo")

        var consumovalue = 0

        consumo.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val querySnapshot = task.result
                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    for (document in querySnapshot.documents) {
                        Log.d(TAG, "${document.id} => ${document.data}")
                        val valueitem = document.data?.get("valorconsumo").toString().toInt()
                        consumovalue += valueitem

                    }
                } else {
                    Log.d(TAG, "No documents found in collection.")
                }
            } else {
                Log.w(TAG, "Error getting documents.", task.exception)
            }

            binding.rewardValue.text = consumovalue.toString()
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
}



