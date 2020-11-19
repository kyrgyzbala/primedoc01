package kg.kyrgyzcoder.primedoc01.data

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptorImpl(context: Context) : ConnectivityInterceptor {
    private val appContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {

        return chain.proceed(chain.request())
    }
}