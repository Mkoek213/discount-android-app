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
import okhttp3.*

import org.json.JSONObject

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
        val childItems1 = mutableListOf<ChildItem>()

        // Make the HTTP request to fetch the characters
        val request = Request.Builder()
            .url("https://rickandmortyapi.com/api/character/")
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
                    // Parse the JSON response
                    val characters = JSONObject(responseData)
                    val results = characters.getJSONArray("results")

                    for (i in 0 until results.length()) {
                        val character = results.getJSONObject(i)
                        val id = character.getInt("id")
                        val name = character.getString("name")

                        // Add character names to childItems1
                        childItems1.add(ChildItem(name, R.drawable.kebab))
                    }

                    // Update the RecyclerView adapter here if needed
                }
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
