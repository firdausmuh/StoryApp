package com.dicoding.storyapp.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivitySignupBinding
import com.dicoding.storyapp.utils.ViewModelFactory

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var factory: ViewModelFactory
    private val registerViewModel: SignupViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupView()
        setupViewModel()
        playAnimation()
        setupAction()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivRegister, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(500)
        val nameEdit = ObjectAnimator.ofFloat(binding.tlName, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(500)
        val emailEdit = ObjectAnimator.ofFloat(binding.tlEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(500)
        val passwordEdit =
            ObjectAnimator.ofFloat(binding.tlPassword, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                name,
                nameEdit,
                email,
                emailEdit,
                password,
                passwordEdit,
                register
            )
            startDelay = 500
        }.start()
    }

    private fun setupAction() {
        binding.apply {
            btnRegister.setOnClickListener {
                if (edtName.length() == 0 && edtEmail.length() == 0 && edtPassword.length() == 0) {
                    edtName.error = getString(R.string.required_field)
                    edtEmail.error = getString(R.string.required_field)
                    edtPassword.setError(getString(R.string.required_field), null)
                } else if (edtName.length() != 0 && edtEmail.length() != 0 && edtPassword.length() != 0) {
                    showLoading()
                    postText()
                    showToast()
                    moveActivity()
                }
            }
        }
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)
    }

    private fun setupView() {
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = getString(R.string.title_signup)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun showLoading() {
        registerViewModel.isLoading.observe(this@SignupActivity) {
            binding.pbRegister.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun showToast() {
        registerViewModel.toastText.observe(this@SignupActivity) {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(
                    this@SignupActivity, toastText, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun moveActivity() {
        registerViewModel.registerResponse.observe(this@SignupActivity) { response ->
            if (!response.error) {
                startActivity(Intent(this@SignupActivity, SignupActivity::class.java))
                finish()
            }
        }
    }

    private fun postText() {
        binding.apply {
            registerViewModel.postRegister(
                edtName.text.toString(),
                edtEmail.text.toString(),
                edtPassword.text.toString()
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}