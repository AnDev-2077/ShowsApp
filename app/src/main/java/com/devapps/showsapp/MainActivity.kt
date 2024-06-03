package com.devapps.showsapp

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.devapps.showsapp.adapters.ShowAdapter
import com.devapps.showsapp.databinding.ActivityMainBinding
import com.devapps.showsapp.services.ShowService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ShowAdapter
    private val ShowsResponse = mutableListOf<TvShowItem>()
    private val favoriteShows = mutableListOf<TvShowItem>() // Nueva lista para almacenar los shows favoritos
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        showFavorites()
        configSwipe()
    }

    private fun configSwipe() {
        binding.srlMain.setOnRefreshListener {
            getShows()
            binding.srlMain.isRefreshing = false
        }
    }

    private fun initRecyclerView(){
        adapter = ShowAdapter(ShowsResponse, favoriteShows, // Pasar favoriteShows al constructor del adaptador
            onShowClick = { show ->
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("Id", show.id)
                startActivity(intent)
            },
            onStarClick = { show ->
                db.collection("favorites").document(show.id.toString()).get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            db.collection("favorites").document(show.id.toString()).delete()
                        } else {
                            db.collection("favorites").document(show.id.toString()).set(show)
                        }
                    }
            }
        )
        binding.rvShows.layoutManager = LinearLayoutManager(this)
        binding.rvShows.adapter = adapter
    }


    private fun getRetrofit():Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.episodate.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getShows(){
        if (isOnline()) {
            CoroutineScope(Dispatchers.IO).launch {
                val call = getRetrofit().create(ShowService::class.java).getShows()
                val shows = call.body()
                runOnUiThread{
                    if (call.isSuccessful){
                        val showsResponse = shows?.tv_shows ?: emptyList()
                        ShowsResponse.clear()
                        ShowsResponse.addAll(showsResponse)
                        adapter.notifyDataSetChanged()
                    }else{
                        showError()
                    }
                }
            }
        } else {
            ShowsResponse.clear()
            ShowsResponse.addAll(favoriteShows)
            adapter.notifyDataSetChanged()
            Toast.makeText(this,"No hay conexión a Internet, mostrando solo favoritos", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showError() {
        Toast.makeText(this,"Ha ocurrido un error", Toast.LENGTH_SHORT).show()
    }

    private fun isOnline(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun showFavorites() {
        db.collection("favorites")
            .get(Source.CACHE)
            .addOnSuccessListener { documents ->
                val favorites = documents.mapNotNull { it.toObject(TvShowItem::class.java) }
                favoriteShows.clear()
                favoriteShows.addAll(favorites)
                adapter.notifyDataSetChanged()
                getShows()
            }
            .addOnFailureListener { exception ->
                showError()
            }
    }
}