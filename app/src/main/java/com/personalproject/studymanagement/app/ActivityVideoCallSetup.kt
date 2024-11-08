package com.personalproject.studymanagement.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.personalproject.studymanagement.R
import com.personalproject.studymanagement.common.CommonVar
import com.personalproject.studymanagement.databinding.LayoutVideoCallBinding
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallFragment
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig

class ActivityVideoCallSetup : AppCompatActivity() {
    lateinit var binding: LayoutVideoCallBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LayoutVideoCallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLoginVideoCall.setOnClickListener{
            addCallFragment()
        }

    }

    private fun addCallFragment() {
        try {
            val appID: Long = CommonVar.INT_APP_ID
            val appSign: String = CommonVar.INT_APP_SIGN_IN
            val callID: String = ""
            val userID: String = ""
            val userName: String = binding.tvInputUserName.text.toString()
                try {
                    println("input username is : $userName")
                    val config = ZegoUIKitPrebuiltCallInvitationConfig()
                    ZegoUIKitPrebuiltCallService.init(application,appID,appSign,userName,userName,config)
                    val intent=Intent(this@ActivityVideoCallSetup,ActivityVideoCall::class.java).apply {
                        putExtra("username",userName)
                    }
                    intent. putExtra("username",userName)
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }
}