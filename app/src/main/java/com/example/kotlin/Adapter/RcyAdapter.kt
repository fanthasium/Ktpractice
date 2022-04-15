package com.example.kotlin.Adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.R
import com.example.kotlin.data.AccountData
import com.example.kotlin.data.UserData
import com.example.kotlin.databinding.ItemMainBinding
import com.example.kotlin.fragment.MainFragment

class RcyAdapter(
    private var accountDataArrayList: ArrayList<AccountData>?,
    private var arrayList: ArrayList<UserData>?,
    private var context: MainFragment
) : RecyclerView.Adapter<RcyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       val inflater = LayoutInflater.from(parent.context)
        val mBinding: ItemMainBinding = DataBindingUtil.inflate(inflater, R.layout.item_main,parent,false)

            return ViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userData = arrayList?.get(position)
        holder.mBinding.userData = userData // DataBinding 으로 값 바로 set해줌
        holder.mBinding.cardView
    }

    override fun getItemCount(): Int {

        // 코틀린에서는 삼항연산자를 if로 대체한다 if 문이 아니라 if 식으로 사용되기 때문에
        return if (arrayList != null) arrayList!!.size else 0
    }

     class  ViewHolder(val mBinding : ItemMainBinding) : RecyclerView.ViewHolder(mBinding.root) {

        fun viewHolder(item : UserData) {
            with(mBinding){
                userData = item
                executePendingBindings()
            }
        }
    }
}