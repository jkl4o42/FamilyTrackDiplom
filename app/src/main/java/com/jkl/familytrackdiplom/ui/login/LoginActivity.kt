package com.jkl.familytrackdiplom.ui.login

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.jkl.familytrackdiplom.R
import com.jkl.familytrack.data.remote.model.family.Family
import com.jkl.familytrack.data.remote.model.family.Member
import com.jkl.familytrack.data.remote.model.location.MemberLocation
import com.jkl.familytrack.utils.IDGenerator
import com.jkl.familytrackdiplom.ui.base.BaseActivity
import com.jkl.familytrack.ui.login.ILoginNavigator
import com.jkl.familytrack.ui.login.LoginViewModel
import com.jkl.familytrack.ui.main.MainActivity
import com.jkl.familytrack.utils.AppConstants.FAMILIES
import com.jkl.familytrack.utils.AppConstants.FAMILY_ID
import com.jkl.familytrack.utils.AppConstants.FAMILY_MEMBERS
import com.jkl.familytrack.utils.AppConstants.LOCATION
import com.jkl.familytrack.utils.AppConstants.MEMBER_ID
import com.jkl.familytrack.utils.CommonUtils
import com.jkl.familytrack.utils.DialogUtils
import com.jkl.familytrackdiplom.utils.PrefUtils
import com.jkl.familytrack.utils.extensions.launchActivity
import com.jkl.familytrackdiplom.utils.service.LocationMonitoringService
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity(), ILoginNavigator {
    private val viewModel by viewModel<LoginViewModel>()

    private val db = FirebaseFirestore.getInstance()
    private var retrievedfamilyData:Family?=null
    private var retrievedMemberData:Member?=null
    private var newFamilyId = IDGenerator.GetBase62(6)
    private var newMemberId = IDGenerator.GetBase62(6)

    override val layoutId: Int
        get() = R.layout.activity_login

    override fun initNavigator() {
        viewModel.setNavigator(this)
    }

    companion object {
        var mServiceIntent = Intent()
        var mSensorService: LocationMonitoringService? = null
    }

    override fun initUI() {
        checkPermission()
    }

    private fun startBackgroundService() {
        mSensorService = LocationMonitoringService()
        mServiceIntent = Intent(this, mSensorService!!::class.java)

        if (!CommonUtils.isMyServiceRunning(mSensorService!!::class.java, this)) {
            startService(mServiceIntent)
        }
    }

    override fun initListener() {
        btnCreateFamily.setOnClickListener {
            checkPermission()
            familyExistRequest()
        }

        btnJoin.setOnClickListener {
            checkPermission()
            retrieveFamily()
        }

        btnGetMemberId.setOnClickListener {
            val member = Member(newMemberId,"Mehmet","")
            val docReferenceForMember = db.collection(FAMILY_MEMBERS)
                .document(MEMBER_ID+member.member_id)
            viewModel.writeOnFamily(member,docReferenceForMember)
            showFamilyCreationPopup(resources.getString(R.string.memberCreationSuccess),member.member_id)

            val docReferenceForMemberLocation = db.collection(FAMILY_MEMBERS)
                .document(MEMBER_ID+member.member_id)
                .collection(LOCATION)
                .document(MEMBER_ID+member.member_id)
            val initialLocation = MemberLocation(0.0,0.0)

            viewModel.writeOnFamily(initialLocation,docReferenceForMemberLocation)
        }
    }

    private fun retrieveFamily() {
        if (!textMemberId.text.isNullOrEmpty()) {
            val memberID = textMemberId.text
            val documentReference = db.collection(FAMILY_MEMBERS).document(MEMBER_ID + memberID)
            viewModel.retrieveFamily(documentReference)
        }
    }

    private fun retrieveMembers(){
        val collectionReference = db.collection(FAMILY_MEMBERS)
        viewModel.retrieveFamilyMembers(collectionReference,retrievedMemberData!!.family_id)
    }

    private fun familyExistRequest() {
        val documentReference = db.collection(FAMILIES).document(FAMILY_ID+newFamilyId)
        viewModel.isDocumentExist(documentReference)
    }

    private fun createFamily() {
        val family = Family(newFamilyId, 1)
        val docReferenceForFamily = db.collection(FAMILIES)
            .document(FAMILY_ID+family.family_id)

        viewModel.writeOnFamily(family, docReferenceForFamily)
        createMember(family)
    }

    private fun createMember(family: Family)
    {
        val member = Member(newMemberId,"",family.family_id)
        val docReferenceForMember = db.collection(FAMILY_MEMBERS)
            .document(MEMBER_ID+member.member_id)
        viewModel.writeOnFamily(member,docReferenceForMember)
        showFamilyCreationPopup(resources.getString(R.string.familyCreationSuccess),member.member_id)
    }

    override fun familyExist(memberData:Member) {
        hideLoading()
        retrievedMemberData = memberData
        retrieveMembers()
    }

    override fun familyNotExist() {
        hideLoading()
        Toast.makeText(this,"Can't find any member with given id",Toast.LENGTH_LONG).show()
    }

    override fun membersRetrieved(members: ArrayList<Member>) {
        hideLoading()

        retrievedMemberData = members.find {
            it.member_id == textMemberId.text.toString()
        }
        retrievedfamilyData = Family(retrievedMemberData!!.family_id,members.size)

        val docReferenceForFamily = db.collection(FAMILIES)
            .document(FAMILY_ID+retrievedfamilyData!!.family_id)
        viewModel.writeOnFamily(retrievedfamilyData!!,docReferenceForFamily)

        PrefUtils.createFamily(Gson().toJson(retrievedfamilyData),Gson().toJson(retrievedMemberData),Gson().toJson(members))
        launchActivity<MainActivity> {  }
        finish()
    }

    override fun documentExist() {
        hideLoading()
        newFamilyId = IDGenerator.GetBase62(6)
        newMemberId = IDGenerator.GetBase62(6)
        familyExistRequest() // consume new id and resend the request
    }

    private fun showFamilyCreationPopup(message:String,memberID:String) {

        val model = DialogUtils.DialogModel(
            "",
            message,
            resources.getString(R.string.memberIDtext),
            memberID,
            "",
            "",
            R.drawable.img_family_one
        )
        DialogUtils.showAlertDialog(this,
            model,
            object:DialogUtils.DialogAlertListener{
                override fun onPositiveClick() {
                    textMemberId.setText(memberID)
                }

                override fun onNegativeClick() {

                }
            })
    }

    private fun checkPermission()
    {
        Dexter.withActivity(this).withPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).withListener(object : MultiplePermissionsListener {
            @SuppressLint("MissingPermission")
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                if (report.areAllPermissionsGranted()) {
                    startBackgroundService()
                    if(PrefUtils.isLoggedFamily())
                    {
                        launchActivity<MainActivity> {  }
                        finish()
                    }
                } else {
                }
            }
            override fun onPermissionRationaleShouldBeShown(
                permissions: List<PermissionRequest>,
                token: PermissionToken
            ) {
            }
        }).check()
    }

    override fun documentNotExist() {
        hideLoading()
        createFamily()
    }

    override fun writeOnFamilySuccess() {
        hideLoading()
    }

    override fun writeOnFamilyFailure() {
        hideLoading()
    }

    override fun membersNotRetrieved() {
    }
}
