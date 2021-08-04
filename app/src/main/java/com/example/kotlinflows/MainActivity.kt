package com.example.kotlinflows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.kotlinflows.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            mainViewModel.loginUser(
                binding.editTextName.text.toString(),
                binding.editTextPassword.text.toString()
            )
        }

        lifecycleScope.launchWhenStarted {
            mainViewModel.loginUiState
                .collect { uiState ->
                when (uiState) {
                    is MainViewModel.LoginUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        Snackbar.make(binding.root, "Successfully Logged In", Snackbar.LENGTH_LONG)
                            .show()
                    }
                    is MainViewModel.LoginUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is MainViewModel.LoginUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Snackbar.make(binding.root, uiState.message, Snackbar.LENGTH_LONG).show()
                    }
                    is MainViewModel.LoginUiState.Empty->{
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onDestroy() {

        super.onDestroy()
    }
}