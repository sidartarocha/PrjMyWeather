package com.sidarta.myweather.ui.favorites

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sidarta.myweather.api.QueryResult
import com.sidarta.myweather.api.RetrofitInitializer
import com.sidarta.myweather.common.Constants
import com.sidarta.myweather.databinding.FragmentFavoritesBinding
import com.sidarta.myweather.ui.dao.RoomManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FavoritesFragment : Fragment() {

    private lateinit var favoritesViewModel: FavoritesViewModel
    private var _binding: FragmentFavoritesBinding? = null

        private val binding get() = _binding!!
        private val adapterFavorites: ListAdapterFavorites by lazy {
        ListAdapterFavorites()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favoritesViewModel =
            ViewModelProvider(this).get(FavoritesViewModel::class.java)

        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (!isInternetAvailable(requireContext())){
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
        }else{
            setupRecyclerview()
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = RoomManager.getInstance(view.context)
        var list = db?.getCityDao()?.allFavoriteCities()
        var aux:String = ""
        if (!list.isNullOrEmpty()) {
            for (i in 0..list.size - 1) {
                Log.d("SID_LOG", "${list[i].id}")
                if (!aux.isNullOrBlank()) {
                    aux = "$aux,${list[i].id}"
                } else {
                    aux = "${list[i].id}"
                }
            }
        }
        val call = RetrofitInitializer.getWeatherService().findByFavorite(aux, Constants.API_KEY)
        call.enqueue(object :Callback<QueryResult> {
            override fun onResponse(call: Call<QueryResult>, response: Response<QueryResult>) {
                response?.body()?.let {
                    val notes: QueryResult = it
                    adapterFavorites.updateData(notes.list)
                    Log.d("SID_LOG1", notes.toString())
                }
            }
            override fun onFailure(call: Call<QueryResult>, t: Throwable) {
                t.message?.let { Log.d("SID_LOG", it) }
            }
        })


    }


    fun isInternetAvailable(context: Context): Boolean {
        var result = false

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        cm.getNetworkCapabilities(cm.activeNetwork)?.run {
            result = when {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }

        return result
    }

    private fun setupRecyclerview() {

        binding.recyclerViewFavorites.adapter = adapterFavorites
        val layoutManager = LinearLayoutManager(context)
        binding.recyclerViewFavorites.layoutManager = layoutManager
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}