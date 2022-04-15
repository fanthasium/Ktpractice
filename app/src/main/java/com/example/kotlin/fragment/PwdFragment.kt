package com.example.kotlin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.kotlin.activity.LoginActivity
import com.example.kotlin.data.UserData
import com.example.kotlin.databinding.FragmentPwdBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


class PwdFragment : Fragment() {
    //파이어베읏 계정 연결
    private lateinit var mBinding: FragmentPwdBinding
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()  //Auth값에 ? 줘버리면 어떻게되지
    private var mUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    //데이터베이스 리얼타임 레퍼런스
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = database.reference
    // email KEY값 받아옴
    private lateinit var email: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentPwdBinding.inflate(inflater, container, false)

    //버튼 클릭 & 로그인
        mBinding.btnActivityInfo.setOnClickListener {

            setFragmentResultListener("requestKey") { _, bundle ->
                email = bundle.getString("bundleKey").toString()
            }

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN)
            val mkDate = simpleDateFormat.format(Date())

            val goto = activity as LoginActivity
            val pwd = mBinding.pwEditText.text.toString()
            val pwdCheck = mBinding.pwCheckEditText.text.toString()
            val pwdRge = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,19}"

            if (pwd == pwdCheck && Pattern.matches(pwdRge, pwd)) {
                mAuth.createUserWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            startToast("회원가입을 성공하셨습니다")
                            writeNewUser(email, mkDate)
                            goto.setFragment(InfoFragment())

                        } else {
                            startToast("가입을 실패했습니다.")
                        }
                    }
            } else {
                if (pwd !== pwdCheck) {
                    startToast("비밀번호가 일치하지 않습니다")
                } else if (!Pattern.matches(pwdRge, pwd)){
                    startToast("잘못된 표현 : 8자 이상 19자 이하의 올바른 정규식으로 입력해주세요")
                }
            }
        }
        return mBinding.root
    }

    // 파이어베이스 데이터 쓰기
    private fun writeNewUser(email : String, mkDate : String){
        val userData = UserData(email, mkDate)
        val uid : String = mUser!!.uid
        databaseReference.child("가입정보").child(uid).setValue(userData)
    }


    private fun startToast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }


}

