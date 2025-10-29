package com.android.photogallery

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

private const val TAG = "VisibleFragment"

abstract class VisibleFragment : Fragment() {

    private val onShowNotification = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // If we receive this, we're visible, so cancel the notification
            Log.i(TAG, "canceling notification")
            setResultCode(Activity.RESULT_CANCELED)
        }
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(PollWorker.ACTION_SHOW_NOTIFICATION)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // API 33+ 需要明确指定导出标志
            ContextCompat.registerReceiver(
                requireContext(),
                onShowNotification,
                filter,
                PollWorker.PERM_PRIVATE,
                null,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        } else {
            // 低版本使用传统方法
            @Suppress("DEPRECATION")
            ContextCompat.registerReceiver(
                requireContext(),
                onShowNotification,
                filter,
                PollWorker.PERM_PRIVATE,
                null,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }
    }

    override fun onStop() {
        super.onStop()
        requireContext().unregisterReceiver(onShowNotification)
    }
}