package com.example.kotlinflows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.kotlinflows.databinding.ActivityMainBinding
import com.example.kotlinflows.model.User
import com.example.kotlinflows.utils.DataStoreManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    lateinit var dataStoreManager: DataStoreManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataStoreManager = DataStoreManager(context = this)
        binding.button.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (name.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    dataStoreManager.saveUser(
                        User(userName = name, password = password)
                    )
                }
                mainViewModel.loginUser(
                    name,
                    password
                )
            }
        }

        lifecycleScope.launchWhenStarted {
            mainViewModel.loginUiState
                .collect { uiState ->
                    when (uiState) {
                        is MainViewModel.LoginUiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            Snackbar.make(
                                binding.root,
                                "Successfully Logged In",
                                Snackbar.LENGTH_LONG
                            )
                                .show()
                            getDataFromDataStore()

                        }
                        is MainViewModel.LoginUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is MainViewModel.LoginUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Snackbar.make(binding.root, uiState.message, Snackbar.LENGTH_LONG)
                                .show()
                        }
                        is MainViewModel.LoginUiState.Empty -> {
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
        }
    }

    private fun getDataFromDataStore() {
        val TAG = "check"
        lifecycleScope.launchWhenStarted {
            dataStoreManager.getUser().collect {
                Log.i(TAG, "getDataFromDataStore: ${it.userName}  --- ${it.password}")
            }
        }

    }

    override fun onDestroy() {

        super.onDestroy()
    }
}