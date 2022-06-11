package com.jkl.familytrack.ui.main

import com.jkl.familytrack.data.remote.model.family.Member
import com.jkl.familytrack.data.remote.model.location.MemberLocation
import com.jkl.familytrack.ui.base.IBaseNavigator

interface IMainNavigator: IBaseNavigator {
    fun setUserLocationSuccess()
    fun setUserLocationFailure()
    fun listenLocationsSuccess(data:ArrayList<MemberLocation>)
    fun listenLocationFailures()
    fun memberExist()
    fun memberNotExist()
    fun memberMoveSuccess(items:ArrayList<Any>)
    fun memberMoveFailure()
    fun writeOnFamilySuccess()
    fun writeOnFamilyFailure()
    fun membersRetrieved(members:ArrayList<Member>)
    fun membersNotRetrieved()

}