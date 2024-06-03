package com.devapps.showsapp

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.devapps.showsapp.databinding.ActivityDetailBinding
import com.devapps.showsapp.services.DetailService
import com.google.firebase.Firebase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val DetailsResponse = mutableListOf<ShowDetailItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDetails()
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.episodate.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private fun getDetails(){
        CoroutineScope(Dispatchers.IO).launch {
            val showId = intent.getIntExtra("Id",-1)
            val call = getRetrofit().create(DetailService::class.java).getShowDetails(showId.toString())
            runOnUiThread{
                if (call.isSuccessful){
                    val details = call.body()?.tvShow
                    binding.tvShowName.text = details?.name
                    binding.tvShowNetwork.text = details?.network
                    binding.tvShowGender.text = details?.genres.toString()
                    binding.tvShowStatus.text = details?.status
                    binding.tvShowDescription.text = details?.description
                    Picasso.get().load(details?.image_thumbnail_path).into(binding.ivShow)
                    val seasons = details?.episodes?.let { getSeasons(it) }
                    seasons?.forEach { season ->
                        val button = createSeasonButton(season)
                        binding.seasonsContainer.addView(button)
                    }

                }
            }
        }
    }



    private fun getSeasons(episodes: List<Episode>): List<Int> {
        return episodes.map { it.season }.distinct()
    }
    private fun createSeasonButton(seasonNumber: Int): Button {
        val button = Button(this)
        button.text = "Temporada $seasonNumber"
        button.setBackgroundColor(Color.parseColor("#674FA3"))
        button.setTextColor(Color.WHITE)
        button.setOnClickListener {

        }
        return button
    }
}