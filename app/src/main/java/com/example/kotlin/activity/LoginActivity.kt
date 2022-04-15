package com.example.kotlin.activity

import android.os.Bundle

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.kotlin.R
import com.example.kotlin.databinding.ActivityLoginBinding
import com.example.kotlin.fragment.IdFragment
import com.example.kotlin.fragment.MainFragment
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var mBinding : ActivityLoginBinding
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        mBinding.login = this@LoginActivity


        // onClick 메서드랑 속도차이 and 메모리차이는?..
        mBinding.btnLogin.setOnClickListener {

            val loginEmail = mBinding.editEmail.text.toString().trim()
            val loginPwd = mBinding.editPwd.text.toString().trim()

            if (loginEmail.isNotEmpty() && loginPwd.isNotEmpty()){
                mAuth.signInWithEmailAndPassword(loginEmail, loginPwd)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = mAuth.currentUser
                            startToast("로그인에 성공하였습니다")
                            frag(0)
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.
                            startToast(task.exception.toString())
                        }
                    }
            }
        }

        mBinding.btnRegiter.setOnClickListener {
            frag(1)
        }

    }

    // ViewModel을 이용한 Data 전달


    //프레그먼트 이동
    private fun frag(fragmentNum: Int) {
        val fragment = supportFragmentManager.beginTransaction()

        when (fragmentNum) {
            0 -> {
                fragment.replace(R.id.loginActivity, MainFragment()).commit()
            }
            1 -> {
                fragment.replace(R.id.loginActivity, IdFragment()).commit()
            }

        }

    }



     // 코드 재활용
     fun setFragment(fragment: Fragment) {
     val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.loginActivity,fragment)
        transaction.commit()
    }

    fun startToast (msg : String){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
  }


