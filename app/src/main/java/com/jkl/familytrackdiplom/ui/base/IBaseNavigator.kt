package com.jkl.familytrack.ui.base

interface IBaseNavigator {

    fun showLoading()

    fun hideLoading()

    fun onError(errorMessage:String)

}