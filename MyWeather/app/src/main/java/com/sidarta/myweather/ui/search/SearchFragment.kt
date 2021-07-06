package com.sidarta.myweather.ui.search

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.sidarta.myweather.R
import com.sidarta.myweather.api.Place
import com.sidarta.myweather.api.QueryResult
import com.sidarta.myweather.api.RetrofitInitializer
import com.sidarta.myweather.common.Constants
import com.sidarta.myweather.databinding.FragmentSearchBinding
import kotlinx.android.synthetic.main.fragment_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel
    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        searchViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val itemAdapter = ListAdapterSearch { item -> adapterOnClick(item) }
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.adapter = itemAdapter


        binding.txtSearch.setEndIconOnClickListener {
            val textaux = txtSearch.editText?.text.toString()
            if(context != null) {
                if (!isInternetAvailable(requireContext())){
                    Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
                }else{
                    loadData(itemAdapter, textaux)
                }

            }
        }


        return root
    }

    private fun adapterOnClick(item: Place) {

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



    fun loadData(itemAdapterSearch: ListAdapterSearch, textaux: String) {

        val call = RetrofitInitializer.getWeatherService().queryWeather(textaux, Constants.API_KEY)
        Log.d("SID_LOG", "loadData")

        call.enqueue(object :Callback<QueryResult> {
            override fun onResponse(call: Call<QueryResult>, response: Response<QueryResult>) {
                response?.body()?.let {
                    val notes: QueryResult = it
                    itemAdapterSearch.submitList(notes.list)
                    Log.d("SID_LOG", notes.toString())
                }
            }
            override fun onFailure(call: Call<QueryResult>, t: Throwable) {
                t.message?.let { Log.d("SID_LOG", it) }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}






