package fiap.com.teste

import fiap.com.teste.R.raw.certificate
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.security.KeyStore
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object RetrofitClient {
    private const val BASE_URL = "https://10.0.2.2:8000/" // Substitua pelo IP público, se necessário

    // Adicionando suporte para certificados SSL personalizados
    private fun getSecureClient(): OkHttpClient {
        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null, null)

        // Carrega o certificado do servidor a partir da pasta raw
        val certInputStream: InputStream = MyApplication.context.resources.openRawResource(
            certificate
        ) // Certificado
        val certificateFactory = java.security.cert.CertificateFactory.getInstance("X.509")
        val certificate = certificateFactory.generateCertificate(certInputStream)
        keyStore.setCertificateEntry("server", certificate)

        trustManagerFactory.init(keyStore)

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustManagerFactory.trustManagers, null)
        val sslSocketFactory = sslContext.socketFactory

        val trustManager = trustManagerFactory.trustManagers[0] as X509TrustManager

        return OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustManager)
            .hostnameVerifier { _, _ -> true } // Ignora verificação de hostname (para desenvolvimento)
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()
    }

    // Configurando o Retrofit com o cliente personalizado
    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getSecureClient()) // Usa o cliente seguro
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
