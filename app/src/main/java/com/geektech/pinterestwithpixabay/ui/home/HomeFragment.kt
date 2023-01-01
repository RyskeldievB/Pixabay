package com.geektech.pinterestwithpixabay.ui.home

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.geektech.pinterestwithpixabay.R
import com.geektech.pinterestwithpixabay.RetrofitServices
import com.geektech.pinterestwithpixabay.databinding.FragmentHomeBinding
import com.geektech.pinterestwithpixabay.model.HitsModel
import com.geektech.pinterestwithpixabay.utils.hideKeyboard
import com.geektech.pinterestwithpixabay.model.PixaModel
import com.geektech.pinterestwithpixabay.ui.home.adapter.HomeAdapter
import com.geektech.pinterestwithpixabay.utils.getCurrentPosition
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var page = 1
    private var startPosition = 0
    private var adapter = HomeAdapter()
    private val hitsList = arrayListOf<HitsModel>()
    val model = PixaModel(hitsList)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClicker()
    }


    private fun initClicker() {
        clickSearch()
        search()
        binding.rvFeed.adapter = adapter
    }

    //change width of etSearch
    private fun clickSearch() {
        var isNotAnimated = true
        with(binding) {
            etSearch.addTextChangedListener {
                val params = etSearch.layoutParams as ConstraintLayout.LayoutParams
                if (etSearch.text.isNotEmpty()) {
                    params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
                    params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                    etSearch.layoutParams = ConstraintLayout.LayoutParams(
                        params
                    )
                 if (isNotAnimated){
                     isNotAnimated = false
                     val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.et_scale_expand)
                     etSearch.startAnimation(anim)
                 }
                } else {
                    params.width = ConstraintLayout.LayoutParams.WRAP_CONTENT
                    params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                    etSearch.layoutParams = ConstraintLayout.LayoutParams(
                        params
                    )
                    if (etSearch.text.isEmpty()){
                        isNotAnimated = true
                        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.et_scale_narrow)
                        etSearch.startAnimation(anim)
                    }
                }
            }
        }
    }

    //hide keyboard and load data on click enter
    private fun search() {
        binding.etSearch.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                page = 1
                startPosition = 0
                //update recycler if user have done new request
                if (model.hits.isNotEmpty()) {
                    hitsList.clear()
                    adapter = HomeAdapter()
                    binding.rvFeed.adapter = adapter
                }
                hideKeyboard()
                load(page)
                return@OnKeyListener true
            }
            false
        })
    }

    private fun load(page: Int) {
        RetrofitServices().api.searchImage(
            keyWord = binding.etSearch.text.toString(),
            page = page
        )
            .enqueue(object : Callback<PixaModel> {
                override fun onResponse(
                    call: Call<PixaModel>,
                    response: Response<PixaModel>
                ) {
                    response.body()?.let { hitsList.addAll(it.hits) }
                    adapter.addData(model)
                    loadMore()
                    binding.ivWaiter.visibility = View.GONE
//                    Toast.makeText(requireContext(), "PAGE ${page}", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<PixaModel>, t: Throwable) {
                    Log.e("aga", "onFailure: ${t.message}")
                }
            })
    }

    private fun loadMore() {
        page++
        binding.rvFeed.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val position = binding.rvFeed.getCurrentPosition()
                if (position >= (startPosition + 6)) {
                    startPosition += 6
                    load(page)
                }
            }
        })
    }

}
