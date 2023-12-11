package com.capstone.basaliproject.ui.scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.capstone.basaliproject.databinding.FragmentScanBinding
import com.capstone.basaliproject.ui.scan.scanner.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class ScanFragment : Fragment() {
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            com.capstone.basaliproject.R.string.tab_scan,
            com.capstone.basaliproject.R.string.tab_history
        )
    }

    private var _binding: FragmentScanBinding? = null
    private var menuItem: MenuItem? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentScanBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val toolbar = binding.toolbarScan
        val mTitle =
            toolbar.findViewById<View>(com.capstone.basaliproject.R.id.toolbar_title) as TextView
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(toolbar)
        mTitle.setText("Aksara")
        activity.getSupportActionBar()?.setDisplayShowTitleEnabled(false);

        // Setup ViewPager2 with Tabs
        val viewPager: ViewPager2 = binding.viewPager
        val tabs: TabLayout = binding.tabs
        val sectionsPagerAdapter = SectionsPagerAdapter(requireActivity())
        viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()




        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                tab.view.background = ContextCompat.getDrawable(
                    requireContext(),
                    com.capstone.basaliproject.R.drawable.tab_selected_background
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.view?.background = null
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    tab.view.background = ContextCompat.getDrawable(
                        requireContext(),
                        com.capstone.basaliproject.R.drawable.tab_selected_background
                    )
                }
            }
        })
        val tabAtIndex0: TabLayout.Tab? = tabs.getTabAt(0)
        if (tabAtIndex0 != null) {
            tabAtIndex0.select()
        }
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(com.capstone.basaliproject.R.menu.custom_tab_scan_menu, menu)
        menuItem = menu.findItem(com.capstone.basaliproject.R.menu.custom_tab_scan_menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}