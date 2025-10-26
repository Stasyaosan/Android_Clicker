package com.example.clicker.ui.home

import android.media.MediaPlayer
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
import kotlin.concurrent.timer

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var mediaPlayer: MediaPlayer

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var counter = 0

    private var clickCountLen = 10
    private var nextImage = false
    private val random = Random

    private lateinit var soundManager: SoundManager

    private lateinit var newImage: ImageView
    private lateinit var imageView: ImageView
    private lateinit var newPlayer: MediaPlayer
    private lateinit var mainCont: ConstraintLayout
    private lateinit var textView: TextView
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
            if (!nextImage){
                soundManager.playSound(R.raw.rat_squeak)

                counter++
                mov()

                if (counter >= clickCountLen) {
                    soundManager.playSound(R.raw.cat_meow)
                    showNewImage()

                    imageView.postDelayed({

                        if (level == 1) {
                            level = 2
                            clickCountLen *= 2
                            imageView.setImageResource(R.mipmap.pig)
                            nextImage = false
                            mov()
                        } else if (level == 2) {
                            level = 3
                        }
                    }, 2000)


                }
            }


        }



        return root
    }

    private fun mov() {
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
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}