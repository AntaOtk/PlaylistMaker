package com.example.playlistmaker.libraby.ui.adapter
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.example.playlistmaker.libraby.ui.fragments.PlayListFragment
import com.example.playlistmaker.libraby.ui.fragments.TracksFragment

class LibraryViewPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle)
: FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 ->  TracksFragment.newInstance()
            else -> PlayListFragment.newInstance()
        }
    }
}
