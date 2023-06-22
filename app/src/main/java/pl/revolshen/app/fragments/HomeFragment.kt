package pl.revolshen.app.fragments
import NetworkUtils.isInternetAvailable
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import pl.revolshen.app.ChildItem
import pl.revolshen.app.ParentAdapter
import pl.revolshen.app.ParentContent
import pl.revolshen.app.ParentItem
import pl.revolshen.app.R
import pl.revolshen.app.databinding.FragmentHomeBinding
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException

import org.json.JSONObject
import pl.revolshen.app.networking.NoInternetActivity

import java.io.IOException


class HomeFragment : Fragment() {

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
//        prepareData()
//        binding.optionText.text = "Wybierz co chcesz zjeść"
        checkNetworkConnectivity()
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
            prepareData()
        } else {
            showNoInternetMessage()
        }
    }

    private fun showNoInternetMessage() {
        val intent = Intent(requireContext(), NoInternetActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun prepareData() {
        val parentItemList = mutableListOf<ParentItem>()
        val childItems1 = mutableListOf<ChildItem>()

        // Make the HTTP request to fetch the data
        val request = Request.Builder()
            .url("https://groovy-granite-384220-service-zasubdnc3q-lm.a.run.app/pizza")
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
                            val restaurantPizza = jsonArray.getJSONObject(i)
                            val namePizza = restaurantPizza.getString("name")
                            val addressPizza = restaurantPizza.getString("address")
                            val discountPizza = restaurantPizza.getString("discount")
                            val timePizza = restaurantPizza.getString("time")
                            val urlPizza = restaurantPizza.getString("url")

                            // Fetch photo from urlPizza using Picasso
                            requireActivity().runOnUiThread {
                                Picasso.get().load(urlPizza).into(object : com.squareup.picasso.Target {
                                    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                                        if (bitmap != null) {
                                            // Add ChildItem with the fetched photo URL
                                            childItems1.add(
                                                ChildItem(namePizza, urlPizza, addressPizza, discountPizza, timePizza)
                                            )
                                            // Update the RecyclerView adapter here if needed
                                        }
                                    }

                                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                                        // Handle photo fetch failure
                                    }

                                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                                        // Called when the photo fetch is in progress
                                    }
                                })
                            }
                        }

                        // Update the RecyclerView adapter here if needed
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.e("API Error", "Failed to parse JSON response.")
                    }
                }
            }
        })




        val parentContent1 = ParentContent(R.drawable.pizza, "Pizza", childItems1)
        // Rest of your code...



    val childItems2 = mutableListOf<ChildItem>()
        // Make the HTTP request to fetch the characters
        val requestKebab = Request.Builder()
            .url("https://groovy-granite-384220-service-zasubdnc3q-lm.a.run.app/kebab")
            .build()
        val clientKebab = OkHttpClient()

        clientKebab.newCall(requestKebab).enqueue(object : Callback {
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
                        val restaurantKebab = jsonArray.getJSONObject(i)
                        val nameKebab = restaurantKebab.getString("name")
                        val addressKebab = restaurantKebab.getString("address")
                        val discountKebab = restaurantKebab.getString("discount")
                        val timeKebab = restaurantKebab.getString("time")
                        val urlKebab = restaurantKebab.getString("url")

                        // Fetch photo from urlPizza using Picasso
                        requireActivity().runOnUiThread {
                            Picasso.get().load(urlKebab).into(object : com.squareup.picasso.Target {
                                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                                    if (bitmap != null) {
                                        // Add ChildItem with the fetched photo URL
                                        childItems2.add(
                                            ChildItem(nameKebab, urlKebab, addressKebab, discountKebab, timeKebab)
                                        )
                                        // Update the RecyclerView adapter here if needed
                                    }
                                }

                                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                                    // Handle photo fetch failure
                                }

                                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                                    // Called when the photo fetch is in progress
                                }
                            })
                        }
                    }

                        // Update the RecyclerView adapter here if needed
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.e("API Error", "Failed to parse JSON response.")
                    }
                }
            }
        })
        val parentContent2 = ParentContent(R.drawable.kebab, "Kebab", childItems2)

        parentItemList.add(ParentItem(parentContent1, parentContent2))

//        val childItems3 = ArrayList<ChildItem>()
//        // Make the HTTP request to fetch the characters
//        val requestAzjatyckie = Request.Builder()
//            .url("https://groovy-granite-384220-service-zasubdnc3q-lm.a.run.app/azjatyckie")
//            .build()
//        val clientAzjatyckie = OkHttpClient()
//
//        clientAzjatyckie.newCall(requestAzjatyckie).enqueue(object : Callback {
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
//                    // Parse the JSON response
//                    val jsonArray = JSONArray(responseData)
//
//                    for (i in 0 until jsonArray.length()) {
//                        val restaurantAzjatyckie = jsonArray.getJSONObject(i)
//                        val nameAzjatyckie = restaurantAzjatyckie.getString("name")
//                        val addressAzjatyckie = restaurantAzjatyckie.getString("address")
//                        val discountAzjatyckie = restaurantAzjatyckie.getString("discount")
//                        val timeAzjatyckie = restaurantAzjatyckie.getString("time")
//                        val urlAzjatyckie = restaurantAzjatyckie.getString("url")
//
//                        // Fetch photo from urlPizza using Picasso
//                        requireActivity().runOnUiThread {
//                            Picasso.get().load(urlAzjatyckie).into(object : com.squareup.picasso.Target {
//                                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
//                                    if (bitmap != null) {
//                                        // Add ChildItem with the fetched photo URL
//                                        childItems3.add(
//                                            ChildItem(nameAzjatyckie, urlAzjatyckie, addressAzjatyckie, discountAzjatyckie, timeAzjatyckie)
//                                        )
//                                        // Update the RecyclerView adapter here if needed
//                                    }
//                                }
//
//                                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
//                                    // Handle photo fetch failure
//                                }
//
//                                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                                    // Called when the photo fetch is in progress
//                                }
//                            })
//                        }
//                    }
//
//                    // Update the RecyclerView adapter here if needed
//                }
//            }
//        })
//        val parentContent3 = ParentContent(R.drawable.spaghetti, "Azjatyckie", childItems3)
//
//        val childItems4 = ArrayList<ChildItem>()
//        // Make the HTTP request to fetch the characters
//        val requestSushi = Request.Builder()
//            .url("https://groovy-granite-384220-service-zasubdnc3q-lm.a.run.app/sushi")
//            .build()
//        val clientSushi = OkHttpClient()
//
//        clientSushi.newCall(requestSushi).enqueue(object : Callback {
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
//                    // Parse the JSON response
//                    val jsonArray = JSONArray(responseData)
//
//                    for (i in 0 until jsonArray.length()) {
//                        val restaurantSushi = jsonArray.getJSONObject(i)
//                        val nameSushi = restaurantSushi.getString("name")
//                        val addressSushi = restaurantSushi.getString("address")
//                        val discountSushi = restaurantSushi.getString("discount")
//                        val timeSushi = restaurantSushi.getString("time")
//                        val urlSushi = restaurantSushi.getString("url")
//
//                        // Fetch photo from urlPizza using Picasso
//                        requireActivity().runOnUiThread {
//                            Picasso.get().load(urlSushi).into(object : com.squareup.picasso.Target {
//                                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
//                                    if (bitmap != null) {
//                                        // Add ChildItem with the fetched photo URL
//                                        childItems4.add(
//                                            ChildItem(nameSushi, urlSushi, addressSushi, discountSushi, timeSushi)
//                                        )
//                                        // Update the RecyclerView adapter here if needed
//                                    }
//                                }
//
//                                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
//                                    // Handle photo fetch failure
//                                }
//
//                                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                                    // Called when the photo fetch is in progress
//                                }
//                            })
//                        }
//                    }
//                    // Update the RecyclerView adapter here if needed
//                }
//            }
//        })
//        val parentContent4 = ParentContent(R.drawable.spaghetti, "Sushi", childItems4)
//
//        parentItemList.add(ParentItem(parentContent3, parentContent4))
//
//        val childItems5 = ArrayList<ChildItem>()
//        // Make the HTTP request to fetch the characters
//        val requestChinskie = Request.Builder()
//            .url("https://groovy-granite-384220-service-zasubdnc3q-lm.a.run.app/chinskie")
//            .build()
//        val clientChinskie = OkHttpClient()
//
//        clientChinskie.newCall(requestChinskie).enqueue(object : Callback {
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
//                    // Parse the JSON response
//                    val jsonArray = JSONArray(responseData)
//
//                    for (i in 0 until jsonArray.length()) {
//                        val restaurantChinskie = jsonArray.getJSONObject(i)
//                        val nameChinskie = restaurantChinskie.getString("name")
//                        val addressChinskie = restaurantChinskie.getString("address")
//                        val discountChinskie = restaurantChinskie.getString("discount")
//                        val timeChinskie = restaurantChinskie.getString("time")
//                        val urlChinskie = restaurantChinskie.getString("url")
//
//                        // Fetch photo from urlPizza using Picasso
//                        requireActivity().runOnUiThread {
//                            Picasso.get().load(urlChinskie).into(object : com.squareup.picasso.Target {
//                                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
//                                    if (bitmap != null) {
//                                        // Add ChildItem with the fetched photo URL
//                                        childItems5.add(
//                                            ChildItem(nameChinskie, urlChinskie, addressChinskie, discountChinskie, timeChinskie)
//                                        )
//                                        // Update the RecyclerView adapter here if needed
//                                    }
//                                }
//
//                                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
//                                    // Handle photo fetch failure
//                                }
//
//                                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                                    // Called when the photo fetch is in progress
//                                }
//                            })
//                        }
//                    }
//
//                    // Update the RecyclerView adapter here if needed
//                }
//            }
//        })
//        val parentContent5 = ParentContent(R.drawable.spaghetti, "Chinskie", childItems5)
//
//        val childItems6 = ArrayList<ChildItem>()
//        // Make the HTTP request to fetch the characters
//        val requestBurgery = Request.Builder()
//            .url("https://groovy-granite-384220-service-zasubdnc3q-lm.a.run.app/burgery")
//            .build()
//        val clientBurgery = OkHttpClient()
//
//        clientBurgery.newCall(requestBurgery).enqueue(object : Callback {
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
//                    // Parse the JSON response
//                    val jsonArray = JSONArray(responseData)
//
//                    for (i in 0 until jsonArray.length()) {
//                        val restaurantBurgery = jsonArray.getJSONObject(i)
//                        val nameBurgery = restaurantBurgery.getString("name")
//                        val addressBurgery = restaurantBurgery.getString("address")
//                        val discountBurgery = restaurantBurgery.getString("discount")
//                        val timeBurgery = restaurantBurgery.getString("time")
//                        val urlBurgery = restaurantBurgery.getString("url")
//
//                        // Fetch photo from urlPizza using Picasso
//                        requireActivity().runOnUiThread {
//                            Picasso.get().load(urlBurgery).into(object : com.squareup.picasso.Target {
//                                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
//                                    if (bitmap != null) {
//                                        // Add ChildItem with the fetched photo URL
//                                        childItems6.add(
//                                            ChildItem(nameBurgery, urlBurgery, addressBurgery, discountBurgery, timeBurgery)
//                                        )
//                                        // Update the RecyclerView adapter here if needed
//                                    }
//                                }
//
//                                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
//                                    // Handle photo fetch failure
//                                }
//
//                                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                                    // Called when the photo fetch is in progress
//                                }
//                            })
//                        }
//                    }
//
//                    // Update the RecyclerView adapter here if needed
//                }
//            }
//        })
//        val parentContent6 = ParentContent(R.drawable.spaghetti, "Burgery", childItems6)
//        parentItemList.add(ParentItem(parentContent5, parentContent6))
//
//        val childItems7 = ArrayList<ChildItem>()
//        // Make the HTTP request to fetch the characters
//        val requestFastfood = Request.Builder()
//            .url("https://groovy-granite-384220-service-zasubdnc3q-lm.a.run.app/fastfood")
//            .build()
//        val clientFastfood = OkHttpClient()
//
//        clientFastfood.newCall(requestFastfood).enqueue(object : Callback {
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
//                    // Parse the JSON response
//                    val jsonArray = JSONArray(responseData)
//
//                    for (i in 0 until jsonArray.length()) {
//                        val restaurantFastfood = jsonArray.getJSONObject(i)
//                        val nameFastfood = restaurantFastfood.getString("name")
//                        val addressFastfood = restaurantFastfood.getString("address")
//                        val discountFastfood = restaurantFastfood.getString("discount")
//                        val timeFastfood = restaurantFastfood.getString("time")
//                        val urlFastfood = restaurantFastfood.getString("url")
//
//                        // Fetch photo from urlPizza using Picasso
//                        requireActivity().runOnUiThread {
//                            Picasso.get().load(urlFastfood).into(object : com.squareup.picasso.Target {
//                                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
//                                    if (bitmap != null) {
//                                        // Add ChildItem with the fetched photo URL
//                                        childItems7.add(
//                                            ChildItem(nameFastfood, urlFastfood, addressFastfood, discountFastfood, timeFastfood)
//                                        )
//                                        // Update the RecyclerView adapter here if needed
//                                    }
//                                }
//
//                                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
//                                    // Handle photo fetch failure
//                                }
//
//                                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                                    // Called when the photo fetch is in progress
//                                }
//                            })
//                        }
//                    }
//
//                    // Update the RecyclerView adapter here if needed
//                }
//            }
//        })
//        val parentContent7 = ParentContent(R.drawable.spaghetti, "Fast food", childItems7)
//
//        val childItems8 = ArrayList<ChildItem>()
//        // Make the HTTP request to fetch the characters
//        val requestIndyjskie = Request.Builder()
//            .url("https://groovy-granite-384220-service-zasubdnc3q-lm.a.run.app/indyjskie")
//            .build()
//        val clientIndyjskie = OkHttpClient()
//
//        clientIndyjskie.newCall(requestIndyjskie).enqueue(object : Callback {
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
//                    // Parse the JSON response
//                    val jsonArray = JSONArray(responseData)
//
//                    for (i in 0 until jsonArray.length()) {
//                        val restaurantIndyjskie = jsonArray.getJSONObject(i)
//                        val nameIndyjskie = restaurantIndyjskie.getString("name")
//                        val addressIndyjskie = restaurantIndyjskie.getString("address")
//                        val discountIndyjskie = restaurantIndyjskie.getString("discount")
//                        val timeIndyjskie = restaurantIndyjskie.getString("time")
//                        val urlIndyjskie = restaurantIndyjskie.getString("url")
//
//                        // Fetch photo from urlPizza using Picasso
//                        requireActivity().runOnUiThread {
//                            Picasso.get().load(urlIndyjskie).into(object : com.squareup.picasso.Target {
//                                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
//                                    if (bitmap != null) {
//                                        // Add ChildItem with the fetched photo URL
//                                        childItems8.add(
//                                            ChildItem(nameIndyjskie, urlIndyjskie, addressIndyjskie, discountIndyjskie, timeIndyjskie)
//                                        )
//                                        // Update the RecyclerView adapter here if needed
//                                    }
//                                }
//
//                                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
//                                    // Handle photo fetch failure
//                                }
//
//                                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
//                                    // Called when the photo fetch is in progress
//                                }
//                            })
//                        }
//                    }
//
//                    // Update the RecyclerView adapter here if needed
//                }
//            }
//        })
//        val parentContent8 = ParentContent(R.drawable.spaghetti, "Indyjskie", childItems8)
//
//        parentItemList.add(ParentItem(parentContent7, parentContent8))

        val adapter = ParentAdapter(parentItemList)
        binding.mainRecyclerView.adapter = adapter
    }
}