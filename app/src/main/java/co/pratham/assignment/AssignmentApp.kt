package co.pratham.assignment

import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.koin.core.context.startKoin
import org.koin.dsl.module

class AssignmentApp : Application() {
    val appModule = module {
        single<GoogleSignInOptions> {
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        }

        single<GoogleSignInClient> {
            GoogleSignIn.getClient(applicationContext, get())
        }

        single { FirebaseAuth.getInstance() }
        single { Firebase.database }
    }


    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(appModule)
        }
    }
}