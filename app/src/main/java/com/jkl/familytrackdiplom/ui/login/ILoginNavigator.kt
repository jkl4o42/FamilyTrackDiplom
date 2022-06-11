package com.jkl.familytrack.ui.login

import com.jkl.familytrack.data.remote.model.family.Family
import com.jkl.familytrack.data.remote.model.family.Member
import com.jkl.familytrack.ui.base.IBaseNavigator

interface ILoginNavigator: IBaseNavigator {
    fun writeOnFamilySuccess()
    fun writeOnFamilyFailure()
    fun documentNotExist()
    fun documentExist()
    fun familyExist(memberData:Member)
    fun familyNotExist()
    fun membersRetrieved(members:ArrayList<Member>)
    fun membersNotRetrieved()
}