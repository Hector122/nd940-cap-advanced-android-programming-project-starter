package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {
    
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
                             ): View? {
        
        //complete: Add ViewModel values and create ViewModel
        val args = VoterInfoFragmentArgs.fromBundle(requireArguments())
        val viewModelFactory = VoterInfoViewModelFactory(requireActivity().application, args)
        val viewModel =
            ViewModelProvider(this, viewModelFactory).get(VoterInfoViewModel::class.java)
        
        //complete: Add binding values
        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        
        //TODO: Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */
        
        //completed: Handle loading of URLs
        viewModel.votingLocationsUrl.observe(viewLifecycleOwner, Observer {
            it?.let {
                loadUrl(it)
                viewModel.onVotingLocationNavigated()
            }
        })
        
        viewModel.ballotInformationUrl.observe(viewLifecycleOwner, Observer {
            it?.let {
                loadUrl(it)
                viewModel.onBallotInformationNavigated()
            }
        })
        
        //complete: Handle save button UI state
        //complete: cont'd(Continue) Handle save button clicks
        viewModel.isElectionSaved.observe(viewLifecycleOwner, Observer { isSaved ->
            binding.saveElectionButton.text =
                if (isSaved) getString(R.string.unfollow_election)
                else getString(R.string.follow_election)
        })
        
        return binding.root
    }
    
    //completed: Create method to load URL intents
    private fun loadUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    }
    
}