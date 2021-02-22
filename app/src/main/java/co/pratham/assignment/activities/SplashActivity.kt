package co.pratham.assignment

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var countDown: CountDownTimer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        countDown = object : CountDownTimer(2000, 1000) {
            override fun onFinish() {
                auth = FirebaseAuth.getInstance()
                val currentUser = auth.currentUser

                // If there is no user open the Create Account Screen
                if (currentUser == null) {
                    val intent = Intent(this@SplashActivity, CreateAccountActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {

                }
            }

            override fun onTick(millisUntilFinished: Long) = Unit
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDown.cancel()
    }
}