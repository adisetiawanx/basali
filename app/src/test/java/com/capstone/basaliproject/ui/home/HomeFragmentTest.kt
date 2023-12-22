package com.capstone.basaliproject.ui.home

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.capstone.basaliproject.data.api.response.Photo
import com.capstone.basaliproject.data.api.response.ProfileData
import com.capstone.basaliproject.data.api.response.ProfileResponse
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeFragmentTest {

    @Mock
    lateinit var profileResponse: ProfileResponse

    @Mock
    lateinit var profileData: ProfileData

    @Mock
    lateinit var photo: Photo

    @InjectMocks
    lateinit var homeFragment: HomeFragment

    private val testContext = mock(Context::class.java)

    @Before
    fun setup() {
        val fragmentManager = mock(FragmentManager::class.java)
        `when`(homeFragment.isAdded).thenReturn(true)
        `when`(homeFragment.childFragmentManager).thenReturn(fragmentManager)

        val application = mock(Application::class.java)
        `when`(application.applicationContext).thenReturn(testContext)

        val activity = mock(AppCompatActivity::class.java)
        `when`(homeFragment.activity).thenReturn(activity)
        `when`(homeFragment.context).thenReturn(testContext)
        `when`(activity.application).thenReturn(application)

        `when`(homeFragment.requireContext()).thenReturn(testContext)
    }

    @Test
    fun testShowProfileImage() {

        `when`(profileResponse.data).thenReturn(profileData)
        `when`(profileData.photo).thenReturn(photo)
        `when`(photo.url).thenReturn("https://storage.googleapis.com/basali-bucket/scan/1702896128087_angka3.png")

        homeFragment.showProfileImage(profileResponse)

        verify(Glide.with(homeFragment.requireContext())).load("https://storage.googleapis.com/basali-bucket/scan/1702896128087_angka3.png").into(homeFragment.binding.ivProfile)
    }
}