package com.example.intheclouds.repository

import androidx.lifecycle.LiveData
import com.example.intheclouds.api.RetrofitBuilder
import com.example.intheclouds.model.Pixabay
import com.example.intheclouds.ui.choosecloud.state.ChooseCloudViewState
import com.example.intheclouds.util.*

object ChooseCloudRepository {

    fun getCloudImages(): LiveData<DataState<ChooseCloudViewState>> {

        return object : NetworkBoundResource<Pixabay.Response, ChooseCloudViewState>() {
            override fun handleApiSuccessResponse(response: ApiSuccessResponse<Pixabay.Response>) {
                result.value = DataState.data(
                    data = ChooseCloudViewState(
                        cloudImages = response.body.cloudImages as ArrayList<Pixabay.CloudImage>
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<Pixabay.Response>> {
                return RetrofitBuilder.apiService.getCumulusPhotos()
            }
        }.asLiveData()
    }
}