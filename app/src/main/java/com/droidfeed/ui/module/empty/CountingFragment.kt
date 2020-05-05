package com.droidfeed.ui.module.empty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.droidfeed.R
import com.droidfeed.ui.common.BaseFragment

class CountingFragment: BaseFragment() {
    var mNum = 0

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mNum = if (arguments != null) requireArguments().getInt("num") else 1
    }

    /**
     * The Fragment's UI is just a simple text view showing its
     * instance number.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.hello_world, container, false)
        val tv = v.findViewById<View>(R.id.text)
        (tv as TextView).text = "Fragment #$mNum"
        tv.background = resources.getDrawable(R.drawable.ic_fly, null)
        return v
    }

    companion object {
        /**
         * Create a new instance of CountingFragment, providing "num"
         * as an argument.
         */
        @JvmStatic
        fun newInstance(num: Int): CountingFragment? {
            val f = CountingFragment()

            // Supply num input as an argument.
            val args = Bundle()
            args.putInt("num", num)
            f.arguments = args
            return f
        }
    }
}