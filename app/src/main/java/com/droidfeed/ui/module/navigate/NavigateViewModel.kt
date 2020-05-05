package com.droidfeed.ui.module.navigate

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.droidfeed.R
import com.droidfeed.data.repo.SourceRepo
import com.droidfeed.ui.common.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class NavigateViewModel @Inject constructor(
        private val sourceRepo: SourceRepo
) : BaseViewModel() {
    val TAG = NavigateViewModel::class.java.simpleName

    val backgroundImageId = R.drawable.onboard_bg

    val isProgressVisible = MutableLiveData<Boolean>().apply { value = false }
    val isContinueButtonEnabled = MutableLiveData<Boolean>().apply { value = false }
    val isAgreementCBEnabled = MutableLiveData<Boolean>().apply { value = true }


    private fun postLoadingState(isLoading: Boolean) {
        isContinueButtonEnabled.postValue(!isLoading)
        isAgreementCBEnabled.postValue(!isLoading)
        isProgressVisible.postValue(isLoading)
    }

    fun onClicked(v: View) {
        Timber.d("onClicked!!! ${v.id}")
    }
}