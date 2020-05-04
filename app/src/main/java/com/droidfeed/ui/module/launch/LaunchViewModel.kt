package com.droidfeed.ui.module.launch

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.droidfeed.R
import com.droidfeed.data.repo.SharedPrefsRepo
import com.droidfeed.data.repo.SourceRepo
import com.droidfeed.ui.common.BaseViewModel
import com.droidfeed.util.logd
import javax.inject.Inject

class LaunchViewModel @Inject constructor(
        var sharedPrefs: SharedPrefsRepo
) : BaseViewModel() {
    val TAG = LaunchViewModel::class.java.simpleName

    val backgroundImageId = R.drawable.onboard_bg

    val isProgressVisible = MutableLiveData<Boolean>().apply { value = false }
    val isContinueButtonEnabled = MutableLiveData<Boolean>().apply { value = false }
    val isAgreementCBEnabled = MutableLiveData<Boolean>().apply { value = true }
    val isUserTermsAccepted = MutableLiveData<Boolean>().apply {
        value = sharedPrefs.hasAcceptedTerms()
    }

    private fun postLoadingState(isLoading: Boolean) {
        isContinueButtonEnabled.postValue(!isLoading)
        isAgreementCBEnabled.postValue(!isLoading)
        isProgressVisible.postValue(isLoading)
    }

    fun onClicked(v: View) {
        logd(TAG, "onClicked!!! ${v.id}")
    }
}