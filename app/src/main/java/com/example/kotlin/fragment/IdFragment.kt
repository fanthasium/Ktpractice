package com.example.kotlin.fragment

import android.content.Intent
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.example.kotlin.R

import com.example.kotlin.activity.LoginActivity
import com.example.kotlin.databinding.FragmentIdBinding

import java.util.regex.Pattern


class IdFragment : Fragment() {

    private lateinit var mBinding: FragmentIdBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentIdBinding.inflate(inflater, container, false)

        mBinding.btnActivityPwd.setOnClickListener{

            val email = mBinding.idEditText.text.toString().trim()
            val emailArg = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$"
            val goto = activity as LoginActivity   //java에선 class 참조 방법?

                if (email.isNotBlank() && Pattern.matches(emailArg, email)) // isNotEmpty()는 비어있는 charSequence를 true로 반환시켜 notBlnck로 사용 단 " a" 형태를 true해버려 trim이 필요함
                {
                    setFragmentResult("requestKey", bundleOf("bundleKey" to email))
                    goto.setFragment(PwdFragment())

                    startToast("사용자 아이디가 생성됐습니다")
                } else{
                    startToast("잘못된 표현: 올바른 구글메일의 정규식으로 입력해주세요")
                }

        }
        return mBinding.root
    }

    //재활용이 필요한 메서드
    private fun startToast(msg : String){
        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show()
    }

}
