package com.pepivsky.loginfirebaseconkotlin.view

import android.animation.ValueAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.animation.doOnEnd
import com.airbnb.lottie.LottieAnimationView
import com.pepivsky.loginfirebaseconkotlin.R
import com.pepivsky.loginfirebaseconkotlin.model.Collections

class ResultActivity : AppCompatActivity() {
    lateinit var imageSuccessAnim: LottieAnimationView
    lateinit var tvTotal: TextView
    lateinit var ivAnimationConffeti: LottieAnimationView
    lateinit var btnContinuar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        imageSuccessAnim = findViewById(R.id.imSuccessAnim)
        ivAnimationConffeti = findViewById(R.id.ivAnimationConfetti)
        btnContinuar = findViewById(R.id.btnContinuarR)
        tvTotal = findViewById(R.id.tvTotal)
        successAnimation(imageSuccessAnim, R.raw.success)

        Log.i("ResultActivity", "${Collections.counter} ${Collections.total}")

        tvTotal.postDelayed({
            startCounterAnimation(tvTotal)
        }, 1000)

        initUI()


        /*tvTotal.postDelayed({
            for (i in 0..Collections.counter) {
                tvTotal.text = "$i / ${Collections.total}"
            }
        }, 1000)*/
    }

    private fun initUI() {
        //TODO arreglar el bug al volver al home
        btnContinuar.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    fun startCounterAnimation(textView: TextView) {
        val animator = ValueAnimator.ofInt(0, Collections.counter)
        animator.duration = 1000 // 5 seconds
        animator.addUpdateListener { animation ->
            tvTotal.text = "${animation.animatedValue.toString()} / ${Collections.total}"
        }
        animator.start()

        //lanzar animacion de conffeti al terminar la otra animacion
        animator.doOnEnd {
            if (Collections.counter == Collections.total ) { //si todas las respuestas son correctas
                ivAnimationConffeti.setAnimation(R.raw.confetti)
                ivAnimationConffeti.visibility = View.VISIBLE
                ivAnimationConffeti.playAnimation()
            }
        }

    }

    private fun successAnimation(imageView: LottieAnimationView, animation: Int) {
        imageView.setAnimation(animation)
        imageView.playAnimation()
    }
}