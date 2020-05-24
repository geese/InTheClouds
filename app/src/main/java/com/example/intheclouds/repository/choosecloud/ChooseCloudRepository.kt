package com.example.intheclouds.repository.choosecloud

import androidx.lifecycle.LiveData
import com.example.intheclouds.api.MitchRetrofitBuilder
import com.example.intheclouds.model.Cumulus
import com.example.intheclouds.repository.NetworkBoundResource
import com.example.intheclouds.ui.choosecloud.state.ChooseCloudViewState
import com.example.intheclouds.util.*

object ChooseCloudRepository {

    fun getCloudImages(): LiveData<DataState<ChooseCloudViewState>> {

        return object : NetworkBoundResource<Cumulus.Response, ChooseCloudViewState>() {
            override fun handleApiSuccessResponse(response: ApiSuccessResponse<Cumulus.Response>) {
                result.value = DataState.data(
                    data = ChooseCloudViewState(
                        cloudImages = response.body.cloudImages as ArrayList<Cumulus.CloudImage>
                    )
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<Cumulus.Response>> {
                return MitchRetrofitBuilder.apiService.getCumulusPhotos()
            }
        }.asLiveData()
    }
}