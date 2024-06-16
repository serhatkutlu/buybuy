package com.example.buybuy.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.buybuy.MainActivity
import com.example.buybuy.databinding.FragmentLoginBinding
import com.example.buybuy.util.viewBinding

class LoginFragment:Fragment() {
    private val binding by viewBinding(FragmentLoginBinding::bind)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity
        if (activity is MainActivity) {
            activity.openDrawerClick()
        }


    }

}