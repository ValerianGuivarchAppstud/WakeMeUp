package com.vguivarc.wakemeup.transport.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.base.BaseActivity
import kotlinx.android.synthetic.main.fragment_auth_email.*

/**
 * Page to login or sign in with email
 */
class AuthEmailFragment : Fragment() {
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as BaseActivity).setSupportActionBar(loginEmailToolbar)
        (activity as BaseActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tabLayout = view.findViewById(R.id.loginEmailTabs)
        initializeTabs(view.findViewById(R.id.loginEmailViewPager))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(
            this, // LifecycleOwner
            callback
        )
    }

    private fun initializeTabs(viewpager: ViewPager2) {
        val ctx = context ?: return
        viewpager.adapter =
            AuthEmailAdapter(childFragmentManager, lifecycle)

        TabLayoutMediator(
            tabLayout,
            viewpager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                tab.text =
                    if (position == 0)
                        getString(R.string.login_email_tab_signin)
                    else
                        getString(R.string.login_email_tab_signup)
            }
        ).attach()

        val uncoloredDrawable =
            ResourcesCompat.getDrawable(ctx.resources, R.drawable.tab_indicator_circle_3, null)
        tabLayout.setSelectedTabIndicator(uncoloredDrawable)
        tabLayout.setSelectedTabIndicatorColor(
            ContextCompat.getColor(ctx, R.color.colorPrimary)
        )
    }
}
