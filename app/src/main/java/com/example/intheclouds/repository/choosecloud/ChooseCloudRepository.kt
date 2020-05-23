package com.example.intheclouds.repository.choosecloud

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.intheclouds.api.MitchRetrofitBuilder
import com.example.intheclouds.model.Cumulus
import com.example.intheclouds.ui.choosecloud.state.ChooseCloudViewState
import com.example.intheclouds.util.ApiEmptyResponse
import com.example.intheclouds.util.ApiErrorResponse
import com.example.intheclouds.util.ApiSuccessResponse

object ChooseCloudRepository {

    fun getCloudImages(): LiveData<ChooseCloudViewState> {

        // TEMPORARY !! just to get the ball rolling

        return Transformations
            .switchMap(MitchRetrofitBuilder.apiService.getCumulusPhotos()) { apiResponse ->
                object: LiveData<ChooseCloudViewState>(){
                    override fun onActive() {
                        super.onActive()
                        when(apiResponse) {

                            is ApiSuccessResponse -> {
                                value = ChooseCloudViewState(
                                    cloudImages = apiResponse.body.cloudImages as ArrayList<Cumulus.CloudImage>
                                )
                            }

                            is ApiErrorResponse -> {
                                value = ChooseCloudViewState()  // handle error?
                            }

                            is ApiEmptyResponse -> {
                                value = ChooseCloudViewState() // handle empty/error?
                            }

                        }
                    }
                }
            }
    }
}