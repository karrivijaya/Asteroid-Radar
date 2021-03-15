package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.domain.Asteroid
import timber.log.Timber

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }

        ViewModelProvider(this, MainViewModel.Factory(activity.application)).get(MainViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        val mainFragmentAdapter = MainFragmentAdapter(MainFragmentAdapter.OnClickListener{
            viewModel.displayAsteroidDetails(it)
        })

        binding.asteroidRecycler.adapter = mainFragmentAdapter

        viewModel.updateRecycleViewList.observe(viewLifecycleOwner, Observer<List<Asteroid>> { asteroids ->
            asteroids?.apply {
                mainFragmentAdapter.submitList(asteroids)
                if(this.isNotEmpty()){
                    viewModel.setUpdateData()
                }
            }
        })

        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner, Observer {
            if(null != it){
                findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
               viewModel.displayAsteroidDetailsComplete()
            }
        })

        viewModel.updatePicture.observe(viewLifecycleOwner, Observer{
            binding.picture = it
        })

        binding.refreshButton.setOnClickListener {
            viewModel.refreshData()
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.show_saved_asteroids -> {
                item.setChecked(true)
                viewModel.updateFilter(AsteroidApiFilter.SHOW_SAVED)}
            R.id.show_today_asteroids -> {
                item.setChecked(true)
                viewModel.updateFilter(AsteroidApiFilter.SHOW_TODAY) }
            else -> {
                item.setChecked(true)
                viewModel.updateFilter(AsteroidApiFilter.SHOW_WEEK) }
        }

        return true
    }
}
