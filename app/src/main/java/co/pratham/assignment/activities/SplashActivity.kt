package co.pratham.assignment.activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import co.pratham.assignment.R
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {

    private val auth: FirebaseAuth by inject()

    private lateinit var countDown: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        countDown = object : CountDownTimer(500, 250) {
            override fun onFinish() {
                val currentUser = auth.currentUser

                // If there is no user open the Create Account Screen
                if (currentUser == null) {
                    val intent = Intent(this@SplashActivity, CreateAccountActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this@SplashActivity,TodoListActivity::class.java)
                    startActivity(intent)
                    finish()
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