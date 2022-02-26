package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.models.Election

class ElectionsFragment : Fragment() {
    
    //completed: Declare ViewModel
    private lateinit var viewModel: ElectionsViewModel
    
    private lateinit var upComingElectionsAdapter: ElectionListAdapter
    private lateinit var savedElectionsAdapter: ElectionListAdapter
    
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
                             ): View? {
        
        //complete: Add ViewModel values and create ViewModel
        val viewModelFactory = ElectionsViewModelFactory(requireActivity().application)
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(ElectionsViewModel::class.java)
        
        //complete: Add binding values
        val binding = FragmentElectionBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        
        
        //TODO: Link elections to voter info
        
        
        //TODO: Initiate recycler adapters
        //Upcoming adapter
        upComingElectionsAdapter = ElectionListAdapter(ElectionListener { election ->
            viewModel.onElectionSelected(election)
        })
        binding.recyclerViewUpcomingElections.adapter = upComingElectionsAdapter
        
        //Saved Election
        savedElectionsAdapter = ElectionListAdapter(ElectionListener { election ->
            viewModel.onElectionSelected(election)
        })
        binding.recyclerViewSavedElections.adapter = savedElectionsAdapter
        
        //TODO: Populate recycler adapters
        viewModel.upcomingElection.observe(viewLifecycleOwner, Observer {
            it.let {
                upComingElectionsAdapter.submitList(it)
            }
        })
        
        viewModel.savedElection.observe(viewLifecycleOwner, Observer {
            it.let {
                savedElectionsAdapter.submitList(it)
            }
        })


//        viewModel.navigateToVoterInfo.observe(viewLifecycleOwner, Observer {
//            election -> findNavController().navigate()
//        })
        
        return binding.root
    }
    
    //TODO: Refresh adapters when fragment loads
//    private fun setupRecyclerViewAdapters() {
//        upComingElectionsAdapter = ElectionListAdapter(ElectionListener { election ->
//            viewModel.onElectionSelected(election)
//        })
//        binding.upcomingElectionsRecyclerView.adapter = upComingElectionsAdapterd
//
//        savedElectionsAdapter = ElectionListAdapter(ElectionListener { election ->
//            viewModel.onElectionSelected(election)
//        })
//        binding.savedElectionsRecyclerView.adapter = savedElectionsAdapter
//    }
    
}