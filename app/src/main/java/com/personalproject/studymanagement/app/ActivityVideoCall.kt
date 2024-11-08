package com.personalproject.studymanagement.app

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.permissionx.guolindev.PermissionX
import com.personalproject.studymanagement.R
import com.personalproject.studymanagement.databinding.ActivityVideoCallBinding
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import java.util.Collections

class ActivityVideoCall : AppCompatActivity() {
    lateinit var binding:ActivityVideoCallBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityVideoCallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        try {
            PermissionX.init(this)
                .permissions(android.Manifest.permission.SYSTEM_ALERT_WINDOW)
                .onExplainRequestReason { scope, deniedList ->
                    val message = "We need your consent for the following permissions in order to use the offline call function properly"
                    scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny")
                }.request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        // Handle permission granted case
                    } else {
                        // Handle denied case
                    }
                }

            val username=intent?.extras?.getString("username")
            println("Username id : $username")
            binding.tvCurrentUser.text=  "Hello ${username} \n Lets have start a call"
            binding.etvTargetUserName.addTextChangedListener{
                val targetUser=binding.etvTargetUserName.text.toString()
                setupVideoCall(targetUser)
                setUpAudioCall(targetUser)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }


    }
    fun setupVideoCall(username:String){
        try {
            binding.btnSendAudioCall.setIsVideoCall(true)
            binding.btnSendAudioCall.resourceID="zego_uikit_call"
            binding.btnSendAudioCall.setInvitees(Collections.singletonList(ZegoUIKitUser(username,username)))


        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    fun setUpAudioCall(username:String){
        try {
            binding.btnSendVideoCall.setIsVideoCall(false)
            binding.btnSendVideoCall.resourceID="zego_uikit_call"
            binding.btnSendVideoCall.setInvitees(Collections.singletonList(ZegoUIKitUser(username,username)))

        }catch (e:Exception){
            e.printStackTrace()
        }

    }
}