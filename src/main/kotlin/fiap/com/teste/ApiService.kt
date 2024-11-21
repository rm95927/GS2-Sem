package fiap.com.teste

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class User(val email: String, val senha: String)

data class UserResponse(
    val success: Boolean,
)

data class ConsumoResponse(
    val nome_item: String,
    val consumo_hora: Float,
    val valor: Float
)


interface ApiService {
    @POST("/items/")
    fun registerUser(@Body user: User): Call<User>

    @GET("/check-user/")
    fun checkUser(@Query("email") email: String, @Query("password") password: String): Call<Boolean>

    @GET("/consumo/")
    fun obterConsumo(@Query("nome_item") nomeItem: String): Call<ConsumoResponse>

    @GET("/eletrodomesticos/")
    fun obterEletrodomesticos(): Call<List<String>>

}


