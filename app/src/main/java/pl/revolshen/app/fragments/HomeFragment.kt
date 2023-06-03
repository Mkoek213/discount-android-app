package pl.revolshen.app.fragments

import android.os.Bundle
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
import pl.revolshen.app.databinding.FragmentMainBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        prepareData()
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

        // First item
        val childItems1 = mutableListOf<ChildItem>()
        childItems1.add(ChildItem("C", R.drawable.c))
        childItems1.add(ChildItem("C++", R.drawable.cplusplus))
        childItems1.add(ChildItem("Java", R.drawable.java))
        childItems1.add(ChildItem("C#", R.drawable.csharp))
        val parentContent1 = ParentContent(R.drawable.console, "Game Development", childItems1)

        val childItems2 = mutableListOf<ChildItem>()
        childItems2.add(ChildItem("Kotlin", R.drawable.kotlin))
        childItems2.add(ChildItem("XML", R.drawable.xml))
        childItems2.add(ChildItem("Java", R.drawable.java))
        val parentContent2 = ParentContent(R.drawable.android, "Android Development", childItems2)
        parentItemList.add(ParentItem(parentContent1, parentContent2))

        val childItems3 = ArrayList<ChildItem>()
        childItems3.add(ChildItem("JavaScript", R.drawable.javascript))
        childItems3.add(ChildItem("HTML", R.drawable.html))
        childItems3.add(ChildItem("CSS", R.drawable.css))
        val parentContent3 = ParentContent(R.drawable.front_end, "Front End Web", childItems3)

        val childItems4 = ArrayList<ChildItem>()
        childItems4.add(ChildItem("Julia", R.drawable.julia))
        childItems4.add(ChildItem("Python", R.drawable.python))
        childItems4.add(ChildItem("R", R.drawable.r))

        val parentContent4 = ParentContent(R.drawable.ai, "Artificial Intelligence", childItems4)
        parentItemList.add(ParentItem(parentContent3, parentContent4))

        val childItems5 = ArrayList<ChildItem>()
        childItems5.add(ChildItem("Java", R.drawable.java))
        childItems5.add(ChildItem("Python", R.drawable.python))
        childItems5.add(ChildItem("PHP", R.drawable.php))
        childItems5.add(ChildItem("JavaScript", R.drawable.javascript))

        val parentContent5 = ParentContent(R.drawable.backend, "Back End Web", childItems5)
        val parentContent6 = ParentContent(R.drawable.console, "Game", childItems1)
        parentItemList.add(ParentItem(parentContent5, parentContent6))

        val parentContent7 = ParentContent(R.drawable.front_end, "Front End Web", childItems3)
        val parentContent8 = ParentContent(R.drawable.ai, "Artificial Intelligence", childItems4)

        parentItemList.add(ParentItem(parentContent7, parentContent8))

        val adapter = ParentAdapter(parentItemList)
        binding.mainRecyclerView.adapter = adapter
    }