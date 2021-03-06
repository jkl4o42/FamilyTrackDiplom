package com.jkl.familytrack.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.jkl.familytrackdiplom.R

object DialogUtils {
    data class DialogModel(
        var title: String? = null,
        var message: String? = null,
        var header: String? = null,
        var headerContent: String? = null,
        var memberId: String? = null,
        var memberCount: String? = null,
        @DrawableRes
        var icon: Int = 0,
        var isFamilyInfoPopup: Boolean = false
    )

    interface DialogAlertListener {
        fun onPositiveClick()
        fun onNegativeClick()
    }

    interface DialogInvitationListener {
        fun sendInvitation(memberID: String)
    }

    fun showBaseAlertDialog(
        context: Context, title: String, message: String, positive: String, negative: String,
        listener: DialogAlertListener
    ) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder
            .setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(positive) { dialog, id ->
                listener.onPositiveClick()
                dialog.cancel()
            }
            .setNegativeButton(negative) { dialog, id ->
                listener.onNegativeClick()
                dialog.cancel()
            }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun showAlertDialog(
        context: Context,
        dialogModel: DialogModel,
        listener: DialogAlertListener? = null
    ) {

        val dialog = AlertDialog.Builder(context)
        val layoutInflater = LayoutInflater.from(context)
        val dialogView = layoutInflater.inflate(R.layout.dialog_popup, null)
        val icon = dialogView.findViewById<AppCompatImageView>(R.id.imgPopupIcon)
        val message = dialogView.findViewById<AppCompatTextView>(R.id.txtMessageDialog)
        val header = dialogView.findViewById<AppCompatTextView>(R.id.txtSuccessHeader)
        val headerContent = dialogView.findViewById<AppCompatTextView>(R.id.txtSuccessContent)
        val btnOk = dialogView.findViewById<AppCompatButton>(R.id.btnOkDialog)

        val familyInfoFamilyID = dialogView.findViewById<AppCompatTextView>(R.id.familyId)
        val familyInfoMemberID = dialogView.findViewById<AppCompatTextView>(R.id.memberId)
        val familyInfoMemberCount = dialogView.findViewById<AppCompatTextView>(R.id.memberCount)

        val familyInfoLayout = dialogView.findViewById<ConstraintLayout>(R.id.familyInfoContent)
        val welcomeLayout = dialogView.findViewById<ConstraintLayout>(R.id.welcomeContent)

        if (dialogModel.isFamilyInfoPopup)
            familyInfoLayout.visibility = View.VISIBLE
        else
            welcomeLayout.visibility = View.VISIBLE

        dialog.setView(dialogView)

        icon.setImageDrawable(ContextCompat.getDrawable(context, dialogModel.icon))

        message.text = dialogModel.message
        header.text = dialogModel.header
        headerContent.text = dialogModel.headerContent

        familyInfoFamilyID.text = dialogModel.headerContent
        familyInfoMemberID.text = dialogModel.memberId
        familyInfoMemberCount.text = dialogModel.memberCount

        val alertDialog = dialog.create()
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog.setCanceledOnTouchOutside(false)
        val v = alertDialog.window!!.decorView
        v.setBackgroundResource(android.R.color.transparent)
        alertDialog.show()

        btnOk.setOnClickListener {
            listener?.onPositiveClick()
            alertDialog.cancel()
        }

    }

    fun showInvitationDialog(
        context: Context,
        listener: DialogInvitationListener? = null
    ) {

        val dialog = AlertDialog.Builder(context)
        val layoutInflater = LayoutInflater.from(context)
        val dialogView = layoutInflater.inflate(R.layout.dialog_invite, null)

        val textMemberId = dialogView.findViewById<AppCompatEditText>(R.id.textMemberId)
        val btnSendInvitation = dialogView.findViewById<AppCompatImageView>(R.id.btnSendInvitation)

        dialog.setView(dialogView)

        val alertDialog = dialog.create()
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog.setCanceledOnTouchOutside(false)
        val v = alertDialog.window!!.decorView
        v.setBackgroundResource(android.R.color.transparent)
        alertDialog.show()

        btnSendInvitation.setOnClickListener {
            if (!textMemberId.text.isNullOrEmpty()) {
                listener?.sendInvitation(textMemberId.text.toString())
                alertDialog.cancel()
            }
        }
    }
}