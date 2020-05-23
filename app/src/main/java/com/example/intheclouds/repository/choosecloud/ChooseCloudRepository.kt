package com.example.intheclouds.repository.choosecloud

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.intheclouds.api.MitchRetrofitBuilder
import com.example.intheclouds.model.Cumulus
import com.example.intheclouds.ui.choosecloud.state.ChooseCloudViewState
import com.example.intheclouds.util.ApiEmptyResponse
import com.example.intheclouds.util.ApiErrorResponse
import com.example.intheclouds.util.ApiSuccessResponse
import com.example.intheclouds.util.DataState

object ChooseCloudRepository {

    fun getCloudImages(): LiveData<DataState<ChooseCloudViewState>> {

        // TEMPORARY !! just to get the ball rolling

        return Transformations
            .switchMap(MitchRetrofitBuilder.apiService.getCumulusPhotos()) { apiResponse ->
                object: LiveData<DataState<ChooseCloudViewState>>(){
                    override fun onActive() {
                        super.onActive()
                        when(apiResponse) {

                            is ApiSuccessResponse -> {
                                value = DataState.data(
                                    message = null,
                                    data = ChooseCloudViewState(
                                        cloudImages = apiResponse.body.cloudImages as ArrayList<Cumulus.CloudImage>
                                    )
                                )
                            }

                            is ApiErrorResponse -> {
                                value = DataState.error(
                                    message = apiResponse.errorMessage
                                )
                            }

                            is ApiEmptyResponse -> {
                                value = DataState.error(
                                    message = "HTTP 204. Returned NOTHING!!"
                                )
                            }
                        }
                    }
                }
            }
    }
}