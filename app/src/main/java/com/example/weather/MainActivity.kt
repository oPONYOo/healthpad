package com.example.weather

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private var mAdapter: RecyclerViewAdapter? = null
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //레이아웃 매니저 설정
        findViewById<RecyclerView>(R.id.recyclerView).layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        findViewById<RecyclerView>(R.id.recyclerView).setHasFixedSize(true)

        fetchJson()

    }
    fun fetchJson(){
        println("데이터를 가져오는 중..")
        val url = "https://api.odcloud.kr/api/15080622/v1/uddi:f9eb56a3-81f1-4f2a-a8d7-c8d264f0b9d1?page=1&perPage=200&serviceKey=58X%2FSZRd7RKQG2G5WjygMkYJGKzgwMktqCSsEtb09U18Zbw%2F33Q1CzIb6EoYWSqS7i26gvny%2FE81KxU7QoqR4A%3D%3D"

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                println(body)

                //파싱 = 이렇게 가져온 데이터를 모델 오브젝트로 변환해줘야 한다.
                val gson = GsonBuilder().create()
                val parser = JsonParser()

               // val rootObj = parser.parse(body).asJsonObject.getAsJsonArray("data")
                val array: JsonArray = parser.parse(body).asJsonObject.get("data").asJsonArray

                val itemlist = ArrayList<Dto>()
                for (array in array){
                    val books:Dto = gson.fromJson(array, Dto::class.java)
                    itemlist.add(books)
                }

                mAdapter=RecyclerViewAdapter(itemlist)
                //백그라운드에서 돌기 때문에 메인 UI로 접근할 수 있도록 해줘야 함
                runOnUiThread {
                    //어댑터 설정
                    findViewById<RecyclerView>(R.id.recyclerView).adapter = mAdapter
                }

            }
            override fun onFailure(call: Call, e: IOException) {
                println("request 실패")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the options menu from XML
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search_layout, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.menu_action_search).actionView as SearchView
        searchView!!.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView!!.maxWidth = Integer.MAX_VALUE
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                mAdapter!!.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                mAdapter!!.filter.filter(query)
                return false
            }
        })

        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        return if(id== R.id.menu_action_search) {
            true
        } else if(id==android.R.id.home){
            finish()
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        // close search view on back button pressed
        if (!searchView!!.isIconified) {
            searchView!!.isIconified = true
            return
        }
        super.onBackPressed()
    }




}