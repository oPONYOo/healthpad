package com.example.weather

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(var bookList: ArrayList<Dto>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(), Filterable {
    lateinit var linstener: AdapterView.OnItemClickListener

    //검색기능을 위해서 별도의 List를 하나 더 만듬듬
   private var searchList: ArrayList<Dto>? = null
    //초기화 구문 init
    init {
        this.searchList = bookList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycleritem_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchList!!.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(searchList!![position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            view.setOnClickListener{
               Toast.makeText(it.context, "", Toast.LENGTH_SHORT).show()

//                //새 액티비티를 연다
//                val intent = Intent(view.context, MainActivity::class.java)
//                view.context.startActivity(intent)
            }
        }
        fun bindItems(data : Dto){
            //이미지 표시
            //데이터 표시
            itemView.findViewById<AppCompatImageView>(R.id.imgView).setBackgroundResource(R.drawable.ic_launcher_background)
            itemView.findViewById<AppCompatTextView>(R.id.nameTxtView).text = data.name
            itemView.findViewById<AppCompatTextView>(R.id.addressTxtView).text = data.address
            itemView.findViewById<AppCompatTextView>(R.id.phonenumberTxtView).text = data.phoneNumber


        }
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    searchList = bookList
                } else {
                    val filteredList = ArrayList<Dto>()
                    //이부분에서 원하는 데이터를 검색할 수 있음
                    for (row in bookList) {
                        if (row.name.toLowerCase().contains(charString.toLowerCase()) || row.address.toLowerCase().contains(charString.toLowerCase())
                            ) {
                            Log.e("charString", "$charString")
                                Log.e("in row", "$row")
                            filteredList.add(row)
                            Log.e("filterList1", filteredList.toString())
                        }
                    }
                    Log.e("filterList", filteredList.toString())
                    searchList = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = searchList
                return filterResults
            }
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                Log.e("value", "${filterResults.values}")
                if(filterResults.values!=null) {
                    searchList = filterResults.values as ArrayList<Dto>
                    Log.e("searhList", "${searchList}")
                    notifyDataSetChanged()
                }
            }
        }
    }
}