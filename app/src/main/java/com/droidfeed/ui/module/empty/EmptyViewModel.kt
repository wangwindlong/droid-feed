package com.droidfeed.ui.module.empty

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.droidfeed.R
import com.droidfeed.data.repo.SourceRepo
import com.droidfeed.ui.common.BaseViewModel
import com.droidfeed.util.logd
import javax.inject.Inject

class EmptyViewModel @Inject constructor(
        private val sourceRepo: SourceRepo
) : BaseViewModel() {
    val TAG = EmptyViewModel::class.java.simpleName

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
        logd(TAG, "onClicked!!! ${v.id}")
    }
}