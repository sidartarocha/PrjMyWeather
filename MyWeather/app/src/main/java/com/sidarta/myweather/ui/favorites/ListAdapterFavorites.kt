package com.sidarta.myweather.ui.favorites

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sidarta.myweather.R
import com.sidarta.myweather.api.Place
import com.bumptech.glide.Glide
import com.sidarta.myweather.entity.Cities
import com.sidarta.myweather.preferences.Preferences
import com.sidarta.myweather.ui.dao.RoomManager
import kotlinx.android.synthetic.main.card_city3.view.*

class ListAdapterFavorites : RecyclerView.Adapter<ListAdapterFavorites.ListItemViewHolder>() {
    private var list: List<Place>? = null

    class ListItemViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)  {

        private var currentItem: Place? = null

        private val itemNameTextViewF: TextView = itemView.findViewById(R.id.txtCityF)
        private val itemTempTextViewF: TextView = itemView.findViewById(R.id.txtTempF)
        private val itemWeatherTextViewF: TextView = itemView.findViewById(R.id.txtSituacaoF)
        private val itemDetalheTextViewF: TextView = itemView.findViewById(R.id.txtDetalheF)
        private val imgWeatherF: ImageView = itemView.findViewById(R.id.imgTempF)
        private val itemTempGrauTextViewF:TextView = itemView.findViewById(R.id.txtUnitF)



        //Room
        private val db = RoomManager.getInstance(itemView.context)

        fun bind(item: Place){
            currentItem = item
            var wind = itemView.resources.getString(R.string.wind)
            var cloud = itemView.resources.getString(R.string.clouds)
            itemNameTextViewF.text = item.name
            itemTempTextViewF.text = item.main.temp.toInt().toString()
            itemWeatherTextViewF.text = item.weather[0].description
            itemDetalheTextViewF.text = "${wind.toString()}  ${item.wind.speed} | ${cloud.toString()} ${item.main.humidity}% | ${item.main.pressure} hpa"
            itemTempGrauTextViewF.text = Preferences.getTemperature()
            val icon = item.weather.last().icon
            val url = "http://openweathermap.org/img/wn/$icon@4x.png"
            Glide.with(itemView).load(url).into(imgWeatherF)


            val paramFavorite = Cities(item.id.toInt(),item.name)
            var favorite = db?.getCityDao()?.findFavorite(paramFavorite.id)

            if (favorite!=null){
                val drawable = ContextCompat.getDrawable(itemView.context,android.R.drawable.btn_star_big_on)
                itemView.btnFavoriteF.setImageDrawable(drawable)
            }else itemView.btnFavoriteF.setImageDrawable(ContextCompat.getDrawable(itemView.context,android.R.drawable.btn_star_big_off))

            itemView.btnFavoriteF.setOnClickListener {
                favorite = db?.getCityDao()?.findFavorite(paramFavorite.id)
                if (favorite == null) {
                    db?.getCityDao()?.insertCity(paramFavorite)
                    val drawable = ContextCompat.getDrawable(itemView.context,android.R.drawable.btn_star_big_on)
                    itemView.btnFavoriteF.setImageDrawable(drawable)
                    Log.d("SID_LOG", "Registro inserido : ${paramFavorite.id}")
                }else{
                    db?.getCityDao()?.deleteCity(paramFavorite)
                    val drawable = ContextCompat.getDrawable(itemView.context,android.R.drawable.btn_star_big_off)
                    itemView.btnFavoriteF.setImageDrawable(drawable)
                    Log.d("SID_LOG", "Registro removido : ${paramFavorite.id}")
                }
            }
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_city3, parent, false)
        return ListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListItemViewHolder, position: Int) {
        list?.let {
            holder.bind(it[position])
        }
    }
    fun updateData(list: List<Place>?){
        this.list= list
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return list?.size ?:0
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