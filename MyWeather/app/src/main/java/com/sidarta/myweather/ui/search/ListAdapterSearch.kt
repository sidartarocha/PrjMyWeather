package com.sidarta.myweather.ui.search

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sidarta.myweather.R
import com.sidarta.myweather.api.Place
import com.bumptech.glide.Glide
import com.sidarta.myweather.entity.Cities
import com.sidarta.myweather.preferences.Preferences
import com.sidarta.myweather.ui.dao.RoomManager
import kotlinx.android.synthetic.main.card_city2.view.*

class ListAdapterSearch (private val onClick: (Place) -> Unit)  :
    ListAdapter<Place, ListAdapterSearch.ListItemViewHolder>(ItemDiffCallback) {

     class ListItemViewHolder(itemView: View, val onClick: (Place) -> Unit) :
        RecyclerView.ViewHolder(itemView)  {

        private var currentItem: Place? = null

        private val itemNameTextView: TextView = itemView.findViewById(R.id.txtCity)
        private val itemTempTextView: TextView = itemView.findViewById(R.id.txtTemp)
        private val itemWeatherTextView: TextView = itemView.findViewById(R.id.txtSituacao)
        private val itemDetalheTextView: TextView = itemView.findViewById(R.id.txtDetalhe)
        private val imgWeather: ImageView = itemView.findViewById(R.id.imgTemp)
        private val itemTempGrauTextView:TextView = itemView.findViewById(R.id.txtUnit)


        //Room
        private val db = RoomManager.getInstance(itemView.context)

        fun bind(item: Place){
            currentItem = item
            var wind = itemView.resources.getString(R.string.wind)
            var cloud = itemView.resources.getString(R.string.clouds)

            itemNameTextView.text = item.name
            itemTempTextView.text = item.main.temp.toInt().toString()
            itemWeatherTextView.text = item.weather[0].description
            itemDetalheTextView.text = "${wind.toString()}  ${item.wind.speed} | ${cloud.toString()} ${item.main.humidity}% | ${item.main.pressure} hpa"
            itemTempGrauTextView.text = Preferences.getTemperature()
            val icon = item.weather.last().icon
            val url = "http://openweathermap.org/img/wn/$icon@4x.png"
            Glide.with(itemView).load(url).into(imgWeather)


            val paramFavorite = Cities(item.id.toInt(),item.name)
            var favorite = db?.getCityDao()?.findFavorite(paramFavorite.id)

            if (favorite!=null){
                val drawable = ContextCompat.getDrawable(itemView.context,android.R.drawable.btn_star_big_on)
                itemView.btnFavorite.setImageDrawable(drawable)
            }else itemView.btnFavorite.setImageDrawable(ContextCompat.getDrawable(itemView.context,android.R.drawable.btn_star_big_off))

            itemView.btnFavorite.setOnClickListener {
                favorite = db?.getCityDao()?.findFavorite(paramFavorite.id)
                if (favorite == null) {
                    db?.getCityDao()?.insertCity(paramFavorite)
                    val drawable = ContextCompat.getDrawable(itemView.context,android.R.drawable.btn_star_big_on)
                    itemView.btnFavorite.setImageDrawable(drawable)
                    Log.d("SID_LOG", "Registro inserido : ${paramFavorite.id}")
                }else{
                    db?.getCityDao()?.deleteCity(paramFavorite)
                    val drawable = ContextCompat.getDrawable(itemView.context,android.R.drawable.btn_star_big_off)
                    itemView.btnFavorite.setImageDrawable(drawable)
                    Log.d("SID_LOG", "Registro removido : ${paramFavorite.id}")
                }
            }
        }

        init {
            itemView.btnFavorite.setOnClickListener {
                currentItem?.let {
                    onClick(it)
                    Log.d("SID_LOG", it.name)
                }
            }
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_city2, parent, false)
        return ListItemViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


}

object ItemDiffCallback : DiffUtil.ItemCallback<Place>() {
    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem.id.equals(newItem.id)
    }

    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem.equals(newItem)
    }
}