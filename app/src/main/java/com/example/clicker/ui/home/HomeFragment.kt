package com.example.clicker.ui.home

import android.media.MediaPlayer
import android.graphics.Matrix
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.clicker.R
import com.example.clicker.databinding.FragmentHomeBinding
import kotlin.random.Random
import com.example.clicker.SoundManager
import java.util.Timer
import kotlin.concurrent.timer
import java.lang.Runnable

class HomeFragment : Fragment() {

    private var closeHandler: android.os.Handler? = null
    private var closeRunnable: Runnable? = null
    private var _binding: FragmentHomeBinding? = null
    private lateinit var mediaPlayer: MediaPlayer

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var counter = 0

    private var clickCountLen = 5.0
    private var nextImage = false
    private val random = Random

    private lateinit var soundManager: SoundManager

    private lateinit var newImage: ImageView
    private lateinit var imageView: ImageView
    private lateinit var newPlayer: MediaPlayer
    private lateinit var mainCont: ConstraintLayout
    private lateinit var textView: TextView

    private var catTimer: Timer? = null
    private var x1 = 0
    private var y1 = 0

    private var level = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        soundManager = SoundManager(requireContext())

        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        textView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            //textView.text = it

        }

        imageView = binding.clickImage
        mainCont = binding.mainCont
        newPlayer = MediaPlayer.create(requireContext(), R.raw.cat_meow)
        newImage = ImageView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setImageResource(R.mipmap.cat_paw)
            visibility = View.GONE
        }
        mainCont.addView(newImage)


        imageView.setOnClickListener {
            if (!nextImage) {
                if (level == 3) {
                    soundManager.playSound(requireContext(), R.raw.cat)
                } else {
                    soundManager.playSound(requireContext(), R.raw.rat_squeak)
                }


                counter++
                mov()
                if (counter >= clickCountLen) {
                    if (level == 3) {
                        showFinal()
                    } else {
                        showNewImage()
                    }


                    imageView.postDelayed({

                        if (level == 1) {
                            level = 2
                            clickCountLen *= 2
                            imageView.setImageResource(R.mipmap.pig)
                            nextImage = false
                            mov()
                        } else if (level == 2) {
                            level = 3
                            nextImage = false
                            clickCountLen *= 1.5
                            imageView.setImageResource(R.mipmap.cat)
                            startCatMove()
                        }
                    }, 2000)


                }
            }


        }



        return root
    }

    private fun mov() {
        if (!nextImage) {
            textView.text = counter.toString()
            val maxX = mainCont.width - imageView.width
            val maxY = mainCont.height - imageView.height
            if (maxX > 0 && maxY > 0) {
                val randomX = random.nextInt(maxX)
                val randomY = random.nextInt(maxY)
                x1 = randomX
                y1 = randomY
                imageView.animate()
                    .x(randomX.toFloat())
                    .y(randomY.toFloat())
                    .setDuration(300).start()

            }
        }
    }

    private fun showNewImage() {
        nextImage = true
        newImage.x = 0.toFloat()
        newImage.visibility = View.VISIBLE

        val width = imageView.width

        imageView.animate()
            .x(-width.toFloat())
            .y(y1.toFloat())
            .setDuration(2000).start()

        newImage.animate()
            .x(-1500.toFloat())
            .y(y1.toFloat())
            .setDuration(2000).start()

        soundManager.playSound(requireContext(), R.raw.cat_meow)
    }

    private fun startCatMove() {
        catTimer = timer(period = 400) {
            activity?.runOnUiThread {
                mov()
            }
        }
    }

    private fun showFinal() {
        catTimer?.cancel()
        nextImage = true
        imageView.layoutParams.width = resources.displayMetrics.widthPixels
        imageView.layoutParams.height = resources.displayMetrics.heightPixels

        imageView.scaleType = ImageView.ScaleType.MATRIX
        val matrix = Matrix()
        matrix.setScale(1.5f, 1.5f)

        val screenWidth = resources.displayMetrics.widthPixels
        val translateX = screenWidth * 1.4f
        matrix.postTranslate(-translateX, 0f)
        imageView.imageMatrix = matrix

        imageView.requestLayout()
        val centerX = 0.toFloat()
        val centerY = 0.toFloat()
        imageView.x = centerX
        imageView.y = centerY
        imageView.setImageResource(R.mipmap.cat_final)
        imageView.animate().cancel()
        soundManager.playSound(requireContext(), R.raw.cat_final)
        startAutoCloseTimer(500)
    }

    private fun startAutoCloseTimer(delayMillis: Long){
        closeHandler?.removeCallbacksAndMessages(null)
        closeHandler = android.os.Handler(android.os.Looper.getMainLooper())
        closeRunnable = Runnable{
            activity?.finishAffinity()
        }
        closeHandler?.postDelayed(closeRunnable!!, delayMillis)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}