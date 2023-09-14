package pl.mikolaj.app.fragments
import NetworkUtils.isInternetAvailable
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import pl.mikolaj.app.ChildItem
import pl.mikolaj.app.ParentAdapter
import pl.mikolaj.app.ParentContent
import pl.mikolaj.app.ParentItem
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException

import pl.mikolaj.app.LanguageChangeListener
import pl.mikolaj.app.SearchAdapter
import pl.mikolaj.app.SearchItem
import pl.mikolaj.app.networking.NoInternetActivity
import pl.revolshen.app.R
import pl.revolshen.app.databinding.FragmentHomeBinding

import java.io.IOException
import java.util.Locale


class HomeFragment : Fragment(), LanguageChangeListener {
    private val childItemsAll = mutableListOf<SearchItem>()
    private val filteredChildItems = mutableListOf<SearchItem>()
    private lateinit var searchAdapter: SearchAdapter
    private var apiRequestCount = 0
    private var currentLanguage: String? = null



    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        checkNetworkConnectivity()


        val sharedPreferences = requireContext().getSharedPreferences("MODE", Context.MODE_PRIVATE)
        val settingsFragment = SettingsFragment()
        settingsFragment.setLanguageChangeListener(this)
        currentLanguage = sharedPreferences.getString("language", "English")
        fetchDataFromAPI(currentLanguage)



        binding.refreshButton.setOnClickListener {
            // Increment the API request count
            apiRequestCount++
            childItemsAll.clear()
            // Call the API request function
            fetchDataFromAPI(currentLanguage)
            Toast.makeText(requireContext(), "Odświeżono", Toast.LENGTH_SHORT)
                .show()
        }


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterChildItems(newText, searchAdapter)
                return true
            }
        })
    }
    override fun onLanguageChanged(language: String) {
        fetchDataFromAPI(currentLanguage)
        binding.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.mainRecyclerView.setHasFixedSize(true)
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
    private fun checkNetworkConnectivity() {
        val isInternetAvailable = isInternetAvailable(requireContext())
        if (isInternetAvailable) {
            fetchDataFromAPI(currentLanguage)
        } else {
            showNoInternetMessage()
        }
    }

    private fun showNoInternetMessage() {
        val intent = Intent(requireContext(), NoInternetActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }


    fun fetchDataFromAPI(language: String?) {
        // ...

        val url = if (language == "Polski") {
            "https://groovy-granite-384220-service-zasubdnc3q-lm.a.run.app/polish"
        } else {
            "https://groovy-granite-384220-service-zasubdnc3q-lm.a.run.app/english"
        }

        // Make the HTTP request to fetch the data
        val parentItemList = mutableListOf<ParentItem>()
        val childItemsPizza = mutableListOf<ChildItem>()
        val childItemsKebab = mutableListOf<ChildItem>()
        val childItemsAzjatyckie = mutableListOf<ChildItem>()
        val childItemsSushi = mutableListOf<ChildItem>()
        val childItemsEuropejskie = mutableListOf<ChildItem>()
        val childItemsBurgery = mutableListOf<ChildItem>()
        val childItemsFastfood = mutableListOf<ChildItem>()
        val childItemsIndyjskie = mutableListOf<ChildItem>()

        val request = Request.Builder()
            .url(url)
            .build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle request failure
                e.printStackTrace()
                Log.e("API Connection", "Failed to connect to the API.")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()

                if (response.isSuccessful && responseData != null) {
                    try {
                        // Parse the JSON response as a JSONArray
                        val jsonArray = JSONArray(responseData)

                        for (i in 0 until jsonArray.length()) {
                            val restaurant = jsonArray.getJSONObject(i)
                            val name = restaurant.getString("name")
                            val address = restaurant.getString("address")
                            val discount = restaurant.getString("discount")
                            val time = restaurant.getString("time")
                            val url = restaurant.getString("url")
                            val foodtype = restaurant.getString("foodtype")
                            val language = restaurant.getString("language")


                            // Fetch photo from url using Picasso
                            requireActivity().runOnUiThread {
                                Picasso.get().load(url)
                                    .into(object : com.squareup.picasso.Target {
                                        override fun onBitmapLoaded(
                                            bitmap: Bitmap?,
                                            from: Picasso.LoadedFrom?
                                        ) {
                                            if (bitmap != null) {
                                                // Add ChildItem with the fetched photo URL
                                                val childItem = ChildItem(
                                                    name,
                                                    url,
                                                    address,
                                                    discount,
                                                    time,
                                                    language
                                                )
                                                val searchItem = SearchItem(
                                                    name,
                                                    url,
                                                    address,
                                                    discount,
                                                    time,
                                                    language
                                                )
                                                if (foodtype == "pizza") {
                                                    childItemsPizza.add(childItem)
                                                } else if (foodtype == "kebab") {
                                                    childItemsKebab.add(childItem)
                                                } else if (foodtype == "azjatyckie") {
                                                    childItemsAzjatyckie.add(childItem)
                                                } else if (foodtype == "sushi") {
                                                    childItemsSushi.add(childItem)
                                                } else if (foodtype == "europejskie") {
                                                    childItemsEuropejskie.add(childItem)
                                                } else if (foodtype == "burgery") {
                                                    childItemsBurgery.add(childItem)
                                                } else if (foodtype == "fastfood") {
                                                    childItemsFastfood.add(childItem)
                                                } else if (foodtype == "indyjskie") {
                                                    childItemsIndyjskie.add(childItem)
                                                }
                                                childItemsAll.add(searchItem)

                                                // Update the RecyclerView adapter here if needed
                                            }
                                        }

                                        override fun onBitmapFailed(
                                            e: Exception?,
                                            errorDrawable: Drawable?
                                        ) {
                                            // Handle photo fetch failure
                                        }

                                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                                            // Called when the photo fetch is in progress
                                        }
                                    })
                            }
                        }
                        requireActivity().runOnUiThread {
                            searchAdapter.setItems(childItemsAll)
                        }
                        // Update the RecyclerView adapter here if needed
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.e("API Error", "Failed to parse JSON response.")
                    }
                }
            }
        })
        val parentContentPizza = ParentContent(R.drawable.pizza, "Pizza", childItemsPizza)
        val parentContentKebab = ParentContent(R.drawable.kebab, "Kebab", childItemsKebab)
        val parentContentAzjatyckie = ParentContent(R.drawable.kebab, "Azjatyckie", childItemsAzjatyckie)
        val parentContentSushi = ParentContent(R.drawable.kebab, "Sushi", childItemsSushi)
        val parentContentEuropejskie = ParentContent(R.drawable.kebab, "Europejskie", childItemsEuropejskie)
        val parentContentBurgery = ParentContent(R.drawable.kebab, "Burgery", childItemsBurgery)
        val parentContentFastfood = ParentContent(R.drawable.kebab, "Fast food", childItemsFastfood)
        val parentContentIndyjskie = ParentContent(R.drawable.kebab, "Indyjskie", childItemsIndyjskie)


        parentItemList.add(ParentItem(parentContentPizza, parentContentKebab))
        parentItemList.add(ParentItem(parentContentAzjatyckie, parentContentSushi))
        parentItemList.add(ParentItem(parentContentEuropejskie, parentContentBurgery))
        parentItemList.add(ParentItem(parentContentFastfood, parentContentIndyjskie))
        val adapter = ParentAdapter(parentItemList)
        binding.mainRecyclerView.adapter = adapter
        searchAdapter = SearchAdapter(filteredChildItems)
        binding.searchRecyclerView.adapter = searchAdapter
    }






    private fun filterChildItems(query: String, searchAdapter: SearchAdapter) {
        val filteredItems = mutableListOf<SearchItem>()
        if (query.isNotEmpty()) {
            val lowerCaseQuery = query.lowercase(Locale.getDefault())
            for (searchItem in childItemsAll) {
                val itemName = searchItem.name.lowercase(Locale.getDefault())
                val itemLanguage = mapLanguage(searchItem.language).lowercase(Locale.getDefault())
                if (itemName.contains(lowerCaseQuery) && itemLanguage == currentLanguage?.lowercase(Locale.getDefault())) {
                    filteredItems.add(searchItem)
                }
            }
        }
        filteredChildItems.clear()
        filteredChildItems.addAll(filteredItems)
        if (filteredItems.isNotEmpty()) {
            binding.mainRecyclerView.visibility = View.GONE
            binding.searchRecyclerView.visibility = View.VISIBLE
            binding.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            // Update the searchAdapter with filtered items
            searchAdapter.setItems(filteredItems)
        } else {
            binding.mainRecyclerView.visibility = View.VISIBLE
            binding.searchRecyclerView.visibility = View.GONE
        }
    }



    private fun mapLanguage(language: String): String {
        return when (language.lowercase(Locale.getDefault())) {
            "polish" -> "Polski"
            "english" -> "english"
            else -> language
        }
    }

}


















//    private fun prepareData() {
//        val parentItemList = mutableListOf<ParentItem>()
//        val childItems1 = mutableListOf<ChildItem>()
//
//        val client = OkHttpClient()
//
//        // Make the HTTP request to fetch the data
//        val request = Request.Builder()
//            .url("https://groovy-granite-384220-service-zasubdnc3q-lm.a.run.app")
//            .build()
//
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                // Handle request failure
//                e.printStackTrace()
//                Log.e("API Connection", "Failed to connect to the API.")
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                val responseData = response.body?.string()
//
//                if (response.isSuccessful && responseData != null) {
//                    try {
//                        // Parse the JSON response as a JSONArray
//                        val jsonArray = JSONArray(responseData)
//
//                        for (i in 0 until jsonArray.length()) {
//                            val restaurant = jsonArray.getJSONObject(i)
//                            val name = restaurant.getString("name")
//                            val address = restaurant.getString("address")
//                            val discount = restaurant.getString("discount")
//                            val time = restaurant.getString("time")
//                            val url = restaurant.getString("url")
//
//                            // Fetch photo from url using Picasso
//                            requireActivity().runOnUiThread {
//                                Picasso.get().load(url)
//                                    .into(object : com.squareup.picasso.Target {
//                                        override fun onBitmapLoaded(
//                                            bitmap: Bitmap?,
//                                            from: Picasso.LoadedFrom?
//                                        ) {
//                                            if (bitmap != null) {
//                                                // Add ChildItem with the fetched photo URL
//                                                childItemsAll.add(
//                                                    ChildItem(
//                                                        name,
//                                                        url,
//                                                        address,
//                                                        discount,
//                                                        time
//                                                    )
//                                                )
//
//                                                // Update the RecyclerView adapter here if needed
//                                            }
//                                        }
//
//                                        override fun onBitmapFailed(
//                                            e: Exception?,
//                                            errorDrawable: Drawable?
//                                        ) {
//                                            // Handle photo fetch failure
//                                        }
//
//                                        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                                            // Called when the photo fetch is in progress
//                                        }
//                                    })
//                            }
//                        }
//
//                        // Update the RecyclerView adapter here if needed
//                    } catch (e: JSONException) {
//                        e.printStackTrace()
//                        Log.e("API Error", "Failed to parse JSON response.")
//                    }
//                }
//            }
//        })
//
//// Make the HTTP request to fetch the data
//
//            val requestPizza = Request.Builder()
//                .url("https://groovy-granite-384220-service-zasubdnc3q-lm.a.run.app/pizza")
//                .build()
//            val clientPizza = OkHttpClient()
//
//            clientPizza.newCall(requestPizza).enqueue(object : Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    // Handle request failure
//                    e.printStackTrace()
//                    Log.e("API Connection", "Failed to connect to the API.")
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    val responseData = response.body?.string()
//
//                    if (response.isSuccessful && responseData != null) {
//                        try {
//                            // Parse the JSON response as a JSONArray
//                            val jsonArray = JSONArray(responseData)
//
//                            for (i in 0 until jsonArray.length()) {
//                                val restaurantPizza = jsonArray.getJSONObject(i)
//                                val name = restaurantPizza.getString("name")
//                                val addressPizza = restaurantPizza.getString("address")
//                                val discountPizza = restaurantPizza.getString("discount")
//                                val timePizza = restaurantPizza.getString("time")
//                                val urlPizza = restaurantPizza.getString("url")
//
//                                // Fetch photo from urlPizza using Picasso
//                                requireActivity().runOnUiThread {
//                                    Picasso.get().load(urlPizza)
//                                        .into(object : com.squareup.picasso.Target {
//                                            override fun onBitmapLoaded(
//                                                bitmap: Bitmap?,
//                                                from: Picasso.LoadedFrom?
//                                            ) {
//                                                if (bitmap != null) {
//                                                    // Add ChildItem with the fetched photo URL
//                                                    childItems1.add(
//                                                        ChildItem(
//                                                            name,
//                                                            urlPizza,
//                                                            addressPizza,
//                                                            discountPizza,
//                                                            timePizza
//                                                        )
//                                                    )
//
//
//
//                                                    // Update the RecyclerView adapter here if needed
//                                                }
//                                            }
//
//                                            override fun onBitmapFailed(
//                                                e: Exception?,
//                                                errorDrawable: Drawable?
//                                            ) {
//                                                // Handle photo fetch failure
//                                            }
//
//                                            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                                                // Called when the photo fetch is in progress
//                                            }
//                                        })
//                                }
//                            }
//
//                            // Update the RecyclerView adapter here if needed
//                        } catch (e: JSONException) {
//                            e.printStackTrace()
//                            Log.e("API Error", "Failed to parse JSON response.")
//                        }
//                    }
//                }
//            })
//
//
//
//
//
//        val parentContent1 = ParentContent(R.drawable.pizza, "Pizza", childItems1)
//        // Rest of your code...
//
//
//
//    val childItems2 = mutableListOf<ChildItem>()
//        // Make the HTTP request to fetch the characters
//
//            val requestKebab = Request.Builder()
//                .url("https://groovy-granite-384220-service-zasubdnc3q-lm.a.run.app/kebab")
//                .build()
//            val clientKebab = OkHttpClient()
//
//            clientKebab.newCall(requestKebab).enqueue(object : Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    // Handle request failure
//                    e.printStackTrace()
//                    Log.e("API Connection", "Failed to connect to the API.")
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    val responseData = response.body?.string()
//
//                    if (response.isSuccessful && responseData != null) {
//                        try {
//                            // Parse the JSON response as a JSONArray
//                            val jsonArray = JSONArray(responseData)
//
//                            for (i in 0 until jsonArray.length()) {
//                                val restaurantKebab = jsonArray.getJSONObject(i)
//                                val name = restaurantKebab.getString("name")
//                                val addressKebab = restaurantKebab.getString("address")
//                                val discountKebab = restaurantKebab.getString("discount")
//                                val timeKebab = restaurantKebab.getString("time")
//                                val urlKebab = restaurantKebab.getString("url")
//
//                                // Fetch photo from urlPizza using Picasso
//                                requireActivity().runOnUiThread {
//                                    Picasso.get().load(urlKebab)
//                                        .into(object : com.squareup.picasso.Target {
//                                            override fun onBitmapLoaded(
//                                                bitmap: Bitmap?,
//                                                from: Picasso.LoadedFrom?
//                                            ) {
//                                                if (bitmap != null) {
//                                                    // Add ChildItem with the fetched photo URL
//                                                    childItems2.add(
//                                                        ChildItem(
//                                                            name,
//                                                            urlKebab,
//                                                            addressKebab,
//                                                            discountKebab,
//                                                            timeKebab
//                                                        )
//                                                    )
//
//                                                    // Update the RecyclerView adapter here if needed
//                                                }
//                                            }
//
//                                            override fun onBitmapFailed(
//                                                e: Exception?,
//                                                errorDrawable: Drawable?
//                                            ) {
//                                                // Handle photo fetch failure
//                                            }
//
//                                            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                                                // Called when the photo fetch is in progress
//                                            }
//                                        })
//                                }
//                            }
//
//                            // Update the RecyclerView adapter here if needed
//                        } catch (e: JSONException) {
//                            e.printStackTrace()
//                            Log.e("API Error", "Failed to parse JSON response.")
//                        }
//                    }
//                }
//            })
//
//        val parentContent2 = ParentContent(R.drawable.kebab, "Kebab", childItems2)
//
//        parentItemList.add(ParentItem(parentContent1, parentContent2))
//        val childItems3 = ArrayList<ChildItem>()
//        // Make the HTTP request to fetch the characters
//
//            val requestAzjatyckie = Request.Builder()
//                .url("https://groovy-granite-384220-service-zasubdnc3q-lm.a.run.app/azjatyckie")
//                .build()
//            val clientAzjatyckie = OkHttpClient()
//            clientAzjatyckie.newCall(requestAzjatyckie).enqueue(object : Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    // Handle request failure
//                    e.printStackTrace()
//                    Log.e("API Connection", "Failed to connect to the API.")
//                }
//                override fun onResponse(call: Call, response: Response) {
//                    val responseData = response.body?.string()
//                    if (response.isSuccessful && responseData != null) {
//                        try {
//                            // Parse the JSON response as a JSONArray
//                            val jsonArray = JSONArray(responseData)
//                            for (i in 0 until jsonArray.length()) {
//                                val restaurantAzjatyckie = jsonArray.getJSONObject(i)
//                                val name = restaurantAzjatyckie.getString("name")
//                                val addressAzjatyckie = restaurantAzjatyckie.getString("address")
//                                val discountAzjatyckie = restaurantAzjatyckie.getString("discount")
//                                val timeAzjatyckie = restaurantAzjatyckie.getString("time")
//                                val urlAzjatyckie = restaurantAzjatyckie.getString("url")
//                                // Fetch photo from urlPizza using Picasso
//                                requireActivity().runOnUiThread {
//                                    Picasso.get().load(urlAzjatyckie)
//                                        .into(object : com.squareup.picasso.Target {
//                                            override fun onBitmapLoaded(
//                                                bitmap: Bitmap?,
//                                                from: Picasso.LoadedFrom?
//                                            ) {
//                                                if (bitmap != null) {
//                                                    // Add ChildItem with the fetched photo URL
//                                                    childItems3.add(
//                                                        ChildItem(
//                                                            name,
//                                                            urlAzjatyckie,
//                                                            addressAzjatyckie,
//                                                            discountAzjatyckie,
//                                                            timeAzjatyckie
//                                                        )
//                                                    )
//
//                                                    // Update the RecyclerView adapter here if needed
//                                                }
//                                            }
//                                            override fun onBitmapFailed(
//                                                e: Exception?,
//                                                errorDrawable: Drawable?
//                                            ) {
//                                                // Handle photo fetch failure
//                                            }
//                                            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                                                // Called when the photo fetch is in progress
//                                            }
//                                        })
//                                }
//                            }
//                            // Update the RecyclerView adapter here if needed
//                        } catch (e: JSONException) {
//                            e.printStackTrace()
//                            Log.e("API Error", "Failed to parse JSON response.")
//                        }
//                    }
//                }
//            })
//
//        val parentContent3 = ParentContent(R.drawable.spaghetti, "Azjatyckie", childItems3)
//        val childItems4 = ArrayList<ChildItem>()
//        // Make the HTTP request to fetch the characters
//
//            val requestSushi = Request.Builder()
//                .url("https://groovy-granite-384220-service-zasubdnc3q-lm.a.run.app/sushi")
//                .build()
//            val clientSushi = OkHttpClient()
//            clientSushi.newCall(requestSushi).enqueue(object : Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    // Handle request failure
//                    e.printStackTrace()
//                    Log.e("API Connection", "Failed to connect to the API.")
//                }
//                override fun onResponse(call: Call, response: Response) {
//                    val responseData = response.body?.string()
//                    if (response.isSuccessful && responseData != null) {
//                        try {
//                            // Parse the JSON response as a JSONArray
//                            val jsonArray = JSONArray(responseData)
//                            for (i in 0 until jsonArray.length()) {
//                                val restaurantSushi = jsonArray.getJSONObject(i)
//                                val name = restaurantSushi.getString("name")
//                                val addressSushi = restaurantSushi.getString("address")
//                                val discountSushi = restaurantSushi.getString("discount")
//                                val timeSushi = restaurantSushi.getString("time")
//                                val urlSushi = restaurantSushi.getString("url")
//                                // Fetch photo from urlPizza using Picasso
//                                requireActivity().runOnUiThread {
//                                    Picasso.get().load(urlSushi)
//                                        .into(object : com.squareup.picasso.Target {
//                                            override fun onBitmapLoaded(
//                                                bitmap: Bitmap?,
//                                                from: Picasso.LoadedFrom?
//                                            ) {
//                                                if (bitmap != null) {
//                                                    // Add ChildItem with the fetched photo URL
//                                                    childItems4.add(
//                                                        ChildItem(
//                                                            name,
//                                                            urlSushi,
//                                                            addressSushi,
//                                                            discountSushi,
//                                                            timeSushi
//                                                        )
//                                                    )
//
//                                                    // Update the RecyclerView adapter here if needed
//                                                }
//                                            }
//                                            override fun onBitmapFailed(
//                                                e: Exception?,
//                                                errorDrawable: Drawable?
//                                            ) {
//                                                // Handle photo fetch failure
//                                            }
//                                            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                                                // Called when the photo fetch is in progress
//                                            }
//                                        })
//                                }
//                            }
//                            // Update the RecyclerView adapter here if needed
//                        } catch (e: JSONException) {
//                            e.printStackTrace()
//                            Log.e("API Error", "Failed to parse JSON response.")
//                        }
//                    }
//                }
//            })
//
//        val parentContent4 = ParentContent(R.drawable.spaghetti, "Sushi", childItems4)
//        parentItemList.add(ParentItem(parentContent3, parentContent4))
//        val childItems5 = ArrayList<ChildItem>()
//        // Make the HTTP request to fetch the characters
//
//            val requestChinskie = Request.Builder()
//                .url("https://groovy-granite-384220-service-zasubdnc3q-lm.a.run.app/chinskie")
//                .build()
//            val clientChinskie = OkHttpClient()
//            clientChinskie.newCall(requestChinskie).enqueue(object : Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    // Handle request failure
//                    e.printStackTrace()
//                    Log.e("API Connection", "Failed to connect to the API.")
//                }
//                override fun onResponse(call: Call, response: Response) {
//                    val responseData = response.body?.string()
//                    if (response.isSuccessful && responseData != null) {
//                        try {
//                            // Parse the JSON response as a JSONArray
//                            val jsonArray = JSONArray(responseData)
//                            for (i in 0 until jsonArray.length()) {
//                                val restaurantChinskie = jsonArray.getJSONObject(i)
//                                val name = restaurantChinskie.getString("name")
//                                val addressChinskie = restaurantChinskie.getString("address")
//                                val discountChinskie = restaurantChinskie.getString("discount")
//                                val timeChinskie = restaurantChinskie.getString("time")
//                                val urlChinskie = restaurantChinskie.getString("url")
//                                // Fetch photo from urlPizza using Picasso
//                                requireActivity().runOnUiThread {
//                                    Picasso.get().load(urlChinskie)
//                                        .into(object : com.squareup.picasso.Target {
//                                            override fun onBitmapLoaded(
//                                                bitmap: Bitmap?,
//                                                from: Picasso.LoadedFrom?
//                                            ) {
//                                                if (bitmap != null) {
//                                                    // Add ChildItem with the fetched photo URL
//                                                    childItems5.add(
//                                                        ChildItem(
//                                                            name,
//                                                            urlChinskie,
//                                                            addressChinskie,
//                                                            discountChinskie,
//                                                            timeChinskie
//                                                        )
//                                                    )
//
//                                                    // Update the RecyclerView adapter here if needed
//                                                }
//                                            }
//                                            override fun onBitmapFailed(
//                                                e: Exception?,
//                                                errorDrawable: Drawable?
//                                            ) {
//                                                // Handle photo fetch failure
//                                            }
//                                            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                                                // Called when the photo fetch is in progress
//                                            }
//                                        })
//                                }
//                            }
//                            // Update the RecyclerView adapter here if needed
//                        } catch (e: JSONException) {
//                            e.printStackTrace()
//                            Log.e("API Error", "Failed to parse JSON response.")
//                        }
//                    }
//                }
//            })
//
//        val parentContent5 = ParentContent(R.drawable.spaghetti, "Chinskie", childItems5)
//        val childItems6 = ArrayList<ChildItem>()
//        // Make the HTTP request to fetch the characters
//
//            val requestBurgery = Request.Builder()
//                .url("https://groovy-granite-384220-service-zasubdnc3q-lm.a.run.app/burgery")
//                .build()
//            val clientBurgery = OkHttpClient()
//            clientBurgery.newCall(requestBurgery).enqueue(object : Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    // Handle request failure
//                    e.printStackTrace()
//                    Log.e("API Connection", "Failed to connect to the API.")
//                }
//                override fun onResponse(call: Call, response: Response) {
//                    val responseData = response.body?.string()
//                    if (response.isSuccessful && responseData != null) {
//                        try {
//                            // Parse the JSON response as a JSONArray
//                            val jsonArray = JSONArray(responseData)
//                            for (i in 0 until jsonArray.length()) {
//                                val restaurantBurgery = jsonArray.getJSONObject(i)
//                                val name = restaurantBurgery.getString("name")
//                                val addressBurgery = restaurantBurgery.getString("address")
//                                val discountBurgery = restaurantBurgery.getString("discount")
//                                val timeBurgery = restaurantBurgery.getString("time")
//                                val urlBurgery = restaurantBurgery.getString("url")
//                                // Fetch photo from urlPizza using Picasso
//                                requireActivity().runOnUiThread {
//                                    Picasso.get().load(urlBurgery)
//                                        .into(object : com.squareup.picasso.Target {
//                                            override fun onBitmapLoaded(
//                                                bitmap: Bitmap?,
//                                                from: Picasso.LoadedFrom?
//                                            ) {
//                                                if (bitmap != null) {
//                                                    // Add ChildItem with the fetched photo URL
//                                                    childItems6.add(
//                                                        ChildItem(
//                                                            name,
//                                                            urlBurgery,
//                                                            addressBurgery,
//                                                            discountBurgery,
//                                                            timeBurgery
//                                                        )
//                                                    )
//
//                                                    // Update the RecyclerView adapter here if needed
//                                                }
//                                            }
//                                            override fun onBitmapFailed(
//                                                e: Exception?,
//                                                errorDrawable: Drawable?
//                                            ) {
//                                                // Handle photo fetch failure
//                                            }
//                                            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                                                // Called when the photo fetch is in progress
//                                            }
//                                        })
//                                }
//                            }
//                            // Update the RecyclerView adapter here if needed
//                        } catch (e: JSONException) {
//                            e.printStackTrace()
//                            Log.e("API Error", "Failed to parse JSON response.")
//                        }
//                    }
//                }
//            })
//
//        val parentContent6 = ParentContent(R.drawable.spaghetti, "Burgery", childItems6)
//        parentItemList.add(ParentItem(parentContent5, parentContent6))
//        val childItems7 = ArrayList<ChildItem>()
//        // Make the HTTP request to fetch the characters
//
//            val requestFastfood = Request.Builder()
//                .url("https://groovy-granite-384220-service-zasubdnc3q-lm.a.run.app/fastfood")
//                .build()
//            val clientFastfood = OkHttpClient()
//            clientFastfood.newCall(requestFastfood).enqueue(object : Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    // Handle request failure
//                    e.printStackTrace()
//                    Log.e("API Connection", "Failed to connect to the API.")
//                }
//                override fun onResponse(call: Call, response: Response) {
//                    val responseData = response.body?.string()
//                    if (response.isSuccessful && responseData != null) {
//                        try {
//                            // Parse the JSON response as a JSONArray
//                            val jsonArray = JSONArray(responseData)
//                            for (i in 0 until jsonArray.length()) {
//                                val restaurantFastfood = jsonArray.getJSONObject(i)
//                                val name = restaurantFastfood.getString("name")
//                                val addressFastfood = restaurantFastfood.getString("address")
//                                val discountFastfood = restaurantFastfood.getString("discount")
//                                val timeFastfood = restaurantFastfood.getString("time")
//                                val urlFastfood = restaurantFastfood.getString("url")
//                                // Fetch photo from urlPizza using Picasso
//                                requireActivity().runOnUiThread {
//                                    Picasso.get().load(urlFastfood)
//                                        .into(object : com.squareup.picasso.Target {
//                                            override fun onBitmapLoaded(
//                                                bitmap: Bitmap?,
//                                                from: Picasso.LoadedFrom?
//                                            ) {
//                                                if (bitmap != null) {
//                                                    // Add ChildItem with the fetched photo URL
//                                                    childItems7.add(
//                                                        ChildItem(
//                                                            name,
//                                                            urlFastfood,
//                                                            addressFastfood,
//                                                            discountFastfood,
//                                                            timeFastfood
//                                                        )
//                                                    )
//
//                                                    // Update the RecyclerView adapter here if needed
//                                                }
//                                            }
//                                            override fun onBitmapFailed(
//                                                e: Exception?,
//                                                errorDrawable: Drawable?
//                                            ) {
//                                                // Handle photo fetch failure
//                                            }
//                                            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                                                // Called when the photo fetch is in progress
//                                            }
//                                        })
//                                }
//                            }
//                            // Update the RecyclerView adapter here if needed
//                        } catch (e: JSONException) {
//                            e.printStackTrace()
//                            Log.e("API Error", "Failed to parse JSON response.")
//                        }
//                    }
//                }
//            })
//
//        val parentContent7 = ParentContent(R.drawable.spaghetti, "Fast food", childItems7)
//        val childItems8 = ArrayList<ChildItem>()
//        // Make the HTTP request to fetch the characters
//
//            val requestIndyjskie = Request.Builder()
//                .url("https://groovy-granite-384220-service-zasubdnc3q-lm.a.run.app/indyjskie")
//                .build()
//            val clientIndyjskie = OkHttpClient()
//            clientIndyjskie.newCall(requestIndyjskie).enqueue(object : Callback {
//                override fun onFailure(call: Call, e: IOException) {
//                    // Handle request failure
//                    e.printStackTrace()
//                    Log.e("API Connection", "Failed to connect to the API.")
//                }
//                override fun onResponse(call: Call, response: Response) {
//                    val responseData = response.body?.string()
//                    if (response.isSuccessful && responseData != null) {
//                        try {
//                            // Parse the JSON response as a JSONArray
//                            val jsonArray = JSONArray(responseData)
//                            for (i in 0 until jsonArray.length()) {
//                                val restaurantIndyjskie = jsonArray.getJSONObject(i)
//                                val name = restaurantIndyjskie.getString("name")
//                                val addressIndyjskie = restaurantIndyjskie.getString("address")
//                                val discountIndyjskie = restaurantIndyjskie.getString("discount")
//                                val timeIndyjskie = restaurantIndyjskie.getString("time")
//                                val urlIndyjskie = restaurantIndyjskie.getString("url")
//                                // Fetch photo from urlPizza using Picasso
//                                requireActivity().runOnUiThread {
//                                    Picasso.get().load(urlIndyjskie)
//                                        .into(object : com.squareup.picasso.Target {
//                                            override fun onBitmapLoaded(
//                                                bitmap: Bitmap?,
//                                                from: Picasso.LoadedFrom?
//                                            ) {
//                                                if (bitmap != null) {
//                                                    // Add ChildItem with the fetched photo URL
//                                                    childItems8.add(
//                                                        ChildItem(
//                                                            name,
//                                                            urlIndyjskie,
//                                                            addressIndyjskie,
//                                                            discountIndyjskie,
//                                                            timeIndyjskie
//                                                        )
//                                                    )
//
//                                                    // Update the RecyclerView adapter here if needed
//                                                }
//                                            }
//                                            override fun onBitmapFailed(
//                                                e: Exception?,
//                                                errorDrawable: Drawable?
//                                            ) {
//                                                // Handle photo fetch failure
//                                            }
//                                            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                                                // Called when the photo fetch is in progress
//                                            }
//                                        })
//                                }
//                            }
//                            // Update the RecyclerView adapter here if needed
//                        } catch (e: JSONException) {
//                            e.printStackTrace()
//                            Log.e("API Error", "Failed to parse JSON response.")
//                        }
//                    }
//                }
//            })
//
//        val parentContent8 = ParentContent(R.drawable.spaghetti, "Indyjskie", childItems8)
//        parentItemList.add(ParentItem(parentContent7, parentContent8))
//        val adapter = ParentAdapter(parentItemList)
//        binding.mainRecyclerView.adapter = adapter
//        searchAdapter = ChildAdapter(filteredChildItems)
//        binding.searchRecyclerView.adapter = searchAdapter
//    }