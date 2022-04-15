package com.example.kotlin.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.Adapter.RcyAdapter
import com.example.kotlin.data.AccountData
import com.example.kotlin.data.UserData
import com.example.kotlin.databinding.FragmentMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainFragment : Fragment() {

    private var list: ArrayList<UserData>? = null
    private var adapter: RcyAdapter? = null
    private var accountDataArrayList: ArrayList<AccountData>? = null

    private lateinit var mBinding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentMainBinding.inflate(inflater, container, false)


        val recyclerView: RecyclerView = mBinding.rcyView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = recyclerView.layoutManager

        //firebase database 연동
        list = java.util.ArrayList()
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.reference

        //파이어베이스 가입정보 읽어오기
        databaseReference.child("가입정보")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //파이어베이스 데이터 가져오는 곳
                    list!!.clear()
                    for (dataSnapShot in dataSnapshot.children)  //여러 값을 불러와 하나씩
                    {
                        val userData = dataSnapShot.getValue(UserData::class.java)
                        list!!.add(userData!!)
                    }
                    adapter!!.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    //가져오던 중 에러나면
                    Log.e("mainActivity", databaseError.toException().toString())
                }
            })

        //파이어베이스 계정정보 읽어오기
        accountDataArrayList = java.util.ArrayList()
        databaseReference.child("account info").addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                accountDataArrayList!!.clear()
                for (ds in dataSnapshot.children) {
                    val accountData = ds.getValue(AccountData::class.java)
                    accountDataArrayList!!.add(accountData!!)
                }
                adapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("account Activity", "loadPost:onCancelled", databaseError.toException())
            }
        })

        recyclerView.adapter = adapter
        adapter = RcyAdapter(accountDataArrayList!!, list!!, this)
        recyclerView.adapter = adapter

        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = mLayoutManager
        return mBinding.root      //왜 바인딩 리턴을 root로 리턴하는거냥
    }

}