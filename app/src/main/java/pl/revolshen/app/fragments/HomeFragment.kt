package pl.revolshen.app.fragments
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import pl.revolshen.app.ChildItem
import pl.revolshen.app.ParentAdapter
import pl.revolshen.app.ParentContent
import pl.revolshen.app.ParentItem
import pl.revolshen.app.R
import pl.revolshen.app.databinding.FragmentHomeBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import pl.revolshen.app.networking.Restaurant
import pl.revolshen.app.networking.RestaurantApiService
import pl.revolshen.app.networking.RetrofitClient
import javax.security.auth.callback.Callback


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
        prepareData()
        binding.optionText.text = "Wybierz co chcesz zjeść"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.mainRecyclerView.setHasFixedSize(true)
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun prepareData() {
        val parentItemList = mutableListOf<ParentItem>()
        val apiService = RetrofitClient.createRestaurantApiService()

        val restaurantByFoodTypeCall = apiService.getRestaurantsByFoodType("Pizza")


        val childItems1 = mutableListOf<ChildItem>()

        val restaurantApiService = RetrofitClient.createRestaurantApiService()

        restaurantApiService.getRestaurantsByFoodType("Pizza").enqueue(object : retrofit2.Callback<List<Restaurant>> {
            override fun onResponse(call: retrofit2.Call<List<Restaurant>>, response: retrofit2.Response<List<Restaurant>>) {
                if (response.isSuccessful) {
                    val restaurants = response.body()

                    if (restaurants != null) {
                        // Clear the existing childItems1 list
                        childItems1.clear()

                        // Iterate over the restaurants and add corresponding ChildItems to the childItems1 list
                        restaurants.forEach { restaurant ->
                            childItems1.add(ChildItem(restaurant.name, R.drawable.spaghetti))
                        }

                        // Notify any adapter or UI component about the change in the data
                        // For example, if you're using RecyclerView, you can call notifyDataSetChanged()
                    } else {
                        // Log absence of data
                        Log.d("API Response", "Empty restaurant list")
                    }
                } else {
                    // Log API error
                    Log.d("API Response", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<List<Restaurant>>, t: Throwable) {
                // Log network or request failure
                Log.e("API Response", "Request failed: ${t.message}")
            }
        })


        val parentContent1 = ParentContent(R.drawable.pizza, "Pizza", childItems1)

        val childItems2 = mutableListOf<ChildItem>()
        childItems2.add(ChildItem("Kotlin", R.drawable.kebab))
        childItems2.add(ChildItem("XML", R.drawable.kebab))
        childItems2.add(ChildItem("Java", R.drawable.kebab))
        val parentContent2 = ParentContent(R.drawable.kebab, "Azjatyckie", childItems2)

        parentItemList.add(ParentItem(parentContent1, parentContent2))

        val childItems3 = ArrayList<ChildItem>()
        childItems3.add(ChildItem("JavaScript", R.drawable.spaghetti))
        childItems3.add(ChildItem("HTML", R.drawable.spaghetti))
        childItems3.add(ChildItem("CSS", R.drawable.spaghetti))
        val parentContent3 = ParentContent(R.drawable.spaghetti, "Sushi", childItems3)

        val childItems4 = ArrayList<ChildItem>()
        childItems4.add(ChildItem("Julia", R.drawable.spaghetti))
        childItems4.add(ChildItem("Python", R.drawable.spaghetti))
        childItems4.add(ChildItem("R", R.drawable.spaghetti))
        val parentContent4 = ParentContent(R.drawable.spaghetti, "Kebab", childItems4)

        parentItemList.add(ParentItem(parentContent3, parentContent4))

        val childItems5 = ArrayList<ChildItem>()
        childItems5.add(ChildItem("Java", R.drawable.spaghetti))
        childItems5.add(ChildItem("Python", R.drawable.spaghetti))
        childItems5.add(ChildItem("PHP", R.drawable.spaghetti))
        childItems5.add(ChildItem("JavaScript", R.drawable.spaghetti))
        val parentContent5 = ParentContent(R.drawable.spaghetti, "Burgery", childItems5)

        val childItems6 = ArrayList<ChildItem>()
        childItems6.add(ChildItem("Java", R.drawable.spaghetti))
        childItems6.add(ChildItem("Python", R.drawable.spaghetti))
        childItems6.add(ChildItem("C++", R.drawable.spaghetti))
        childItems6.add(ChildItem("C#", R.drawable.spaghetti))
        val parentContent6 = ParentContent(R.drawable.spaghetti, "Fast food", childItems6)
        parentItemList.add(ParentItem(parentContent5, parentContent6))

        val childItems7 = ArrayList<ChildItem>()
        childItems7.add(ChildItem("Java", R.drawable.spaghetti))
        childItems7.add(ChildItem("Python", R.drawable.spaghetti))
        childItems7.add(ChildItem("C++", R.drawable.spaghetti))
        childItems7.add(ChildItem("C#", R.drawable.spaghetti))
        val parentContent7 = ParentContent(R.drawable.spaghetti, "Indyjskie", childItems7)

        val childItems8 = ArrayList<ChildItem>()
        childItems8.add(ChildItem("Java", R.drawable.spaghetti))
        childItems8.add(ChildItem("Python", R.drawable.spaghetti))
        childItems8.add(ChildItem("C++", R.drawable.spaghetti))
        childItems8.add(ChildItem("C#", R.drawable.spaghetti))
        val parentContent8 = ParentContent(R.drawable.spaghetti, "Bary", childItems8)

        parentItemList.add(ParentItem(parentContent7, parentContent8))

        val adapter = ParentAdapter(parentItemList)
        binding.mainRecyclerView.adapter = adapter
    }
}
