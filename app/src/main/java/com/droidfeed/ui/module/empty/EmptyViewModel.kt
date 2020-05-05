package com.droidfeed.ui.module.empty

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import com.droidfeed.R
import com.droidfeed.data.repo.SourceRepo
import com.droidfeed.ui.common.BaseViewModel
import com.droidfeed.util.event.Event
import timber.log.Timber
import javax.inject.Inject

class EmptyViewModel @Inject constructor(
        private val sourceRepo: SourceRepo
) : BaseViewModel() {

    val backgroundImageId = R.drawable.onboard_bg

    val isProgressVisible = MutableLiveData<Boolean>().apply { value = false }
    val isContinueButtonEnabled = MutableLiveData<Boolean>().apply { value = false }
    val isAgreementCBEnabled = MutableLiveData<Boolean>().apply { value = true }
    val jump2Fragment = MutableLiveData<Event<Int>>()


    private fun postLoadingState(isLoading: Boolean) {
        isContinueButtonEnabled.postValue(!isLoading)
        isAgreementCBEnabled.postValue(!isLoading)
        isProgressVisible.postValue(isLoading)
    }


    fun jump(index:Int) {
        jump2Fragment.postValue(Event(index))
    }

    fun onClicked(v: View) {
        Timber.d("onClicked!!! ${v.id}")
    }
}