package com.example.kotlin.fragment

import android.R
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface.OnMultiChoiceClickListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.kotlin.activity.LoginActivity
import com.example.kotlin.data.AccountData
import com.example.kotlin.databinding.FragmentInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class InfoFragment : Fragment() {

    private lateinit var mBinding: FragmentInfoBinding

    //데이터베이스 리얼타임 레퍼런스
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = database.reference

    //파이어베이스 계정연결
    private var mUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
           transaction.addToBackStack(null).commit()
        mBinding = FragmentInfoBinding.inflate(inflater, container, false)
        mBinding.info = this
        mBinding.btnActivityMain.setOnClickListener {
            val goto = activity as LoginActivity

            val name = mBinding.nameEditText.text.toString()
            val gender = getGender(view).toString()
            val birth = mBinding.birthdayTxtview.text.toString()
            val phoneNum = mBinding.phoneNumEditText.text.toString()
            val hobby = mBinding.hobbyTxtview.text.toString()

            accountData(name, gender, birth, phoneNum, hobby)
            goto.setFragment(MainFragment())
        }

        return mBinding.root
    }

    // 파이어베이스 데이터 쓰기
    private fun accountData(
        name: String,
        gender: String,
        birth: String,
        phoneNum: String,
        hobby: String
    ) {
        val accountData = AccountData(name, gender, birth, phoneNum, hobby)
        val uid = mUser!!.uid
        databaseReference.child("계정정보").child(uid).setValue(accountData)
    }

    // gender 라디오그룹
    private fun getGender(view: View?): String? {
        val male: RadioButton = mBinding.rgbtnMale
        val female: RadioButton = mBinding.rgbtnFemale
        var gender: String? = null
        if (male.isChecked) {
            gender = male.text.toString()
        } else if (female.isChecked) {
            gender = female.text.toString()
        }
        return gender
    }

    // birth 다이얼로그
    fun birthDialog(view: View) {
        val c = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(), R.style.Theme_Holo_Light_Dialog_MinWidth,
            { view, year, monthOfYear, dayOfMonth ->
                try {
                    val birth: TextView = mBinding.birthdayTxtview
                    birth.text = String.format("%d - %d - %d", year, monthOfYear + 1, dayOfMonth)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            c[Calendar.YEAR],
            c[Calendar.MONTH],
            c[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.datePicker.calendarViewShown = false
        datePickerDialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        datePickerDialog.show()
    }

    // hobby 다이얼로그
    fun hobbyDialog(view: View) {
        val selectItem = ArrayList<String>()
        val builder = AlertDialog.Builder(requireContext())

        //value에서 배열이 안 가져와져서 대체
        val items = arrayOf<String>("음악감상", "독서", "트레이닝", "자전거 타기", "Tv시청", "우표 수집", "U-tube")

        builder.setMultiChoiceItems(items, null,
            OnMultiChoiceClickListener { dialogInterface, which, isChecked ->

                if (isChecked) {
                    selectItem.add(items[which])
                } else if (selectItem.contains(items[which])) {
                    selectItem.remove(items[which])
                }
            })

        builder.apply {
            setPositiveButton(R.string.ok) { dialogInterface, which ->
                val checkBox: TextView = mBinding.hobbyTxtview
                var selection = ""
                for (item in selectItem) {
                    selection = "$selection$item , "
                    checkBox.text = selection
                }
            }
            setNegativeButton(R.string.cancel) { _, _ ->
            }
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

}



