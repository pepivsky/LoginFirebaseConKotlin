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
import com.pepivsky.loginfirebaseconkotlin.databinding.ActivityResultBinding
import com.pepivsky.loginfirebaseconkotlin.model.Collections

class ResultActivity : AppCompatActivity() {

    lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        successAnimation(binding.imSuccessAnim, R.raw.success)
        Log.i("ResultActivity", "${Collections.counter} ${Collections.total}")

        binding.tvTotal.postDelayed({
            startCounterAnimation()
        }, 1000)

        initUI()
    }

    private fun initUI() {
        //TODO arreglar el bug al volver al home
        binding.btnContinuarR.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startCounterAnimation() {
        val animator = ValueAnimator.ofInt(0, Collections.counter)
        animator.duration = 1000 // 5 seconds
        animator.addUpdateListener { animation ->
            binding.tvTotal.text = "${animation.animatedValue.toString()} / ${Collections.total}"
        }
        animator.start()

        //lanzar animacion de conffeti al terminar la otra animacion
        animator.doOnEnd {
            if (Collections.counter == Collections.total ) { //si todas las respuestas son correctas
                binding.ivAnimationConfetti.setAnimation(R.raw.confetti)
                binding.ivAnimationConfetti.visibility = View.VISIBLE
                binding.ivAnimationConfetti.playAnimation()
            }
        }

    }

    private fun successAnimation(imageView: LottieAnimationView, animation: Int) {
        imageView.setAnimation(animation)
        imageView.playAnimation()
    }
}