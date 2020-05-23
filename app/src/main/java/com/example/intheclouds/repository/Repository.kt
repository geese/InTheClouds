package com.example.intheclouds.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.intheclouds.api.RetrofitBuilder
import com.example.intheclouds.model.CumulusResponse
import com.example.intheclouds.ui.addeditcloud.state.AddEditViewState
import com.example.intheclouds.util.ApiSuccessResponse
import com.example.intheclouds.util.DataState
import com.example.intheclouds.util.GenericApiResponse


fun main(){
    var response = RetrofitBuilder.apiService.getCumulusPhotos()
    print(response)
}
class Repository {

    fun getCumulusImages(): LiveData<DataState<AddEditViewState>> {
        return object: NetworkBoundResource<CumulusResponse.Response, AddEditViewState>() {

            override fun handleApiSuccessResponse(response: ApiSuccessResponse<CumulusResponse.Response>) {
                TODO("Not yet implemented")
            }

            override fun createCall(): LiveData<GenericApiResponse<CumulusResponse.Response>> {
                TODO("Not yet implemented")
            }
        }.asLiveData()
    }
}