package com.example.intheclouds.repository

import androidx.lifecycle.LiveData
import com.example.intheclouds.api.RetrofitBuilder
import com.example.intheclouds.model.Pixabay
import com.example.intheclouds.ui.choosecloud.state.ChooseCloudViewState
import com.example.intheclouds.util.ApiSuccessResponse
import com.example.intheclouds.util.DataState
import com.example.intheclouds.util.GenericApiResponse
import timber.log.Timber.d

object ChooseCloudRepository {

    fun getCloudImages(): LiveData<DataState<ChooseCloudViewState>> {

        return object : NetworkBoundResource<Pixabay.Response, ChooseCloudViewState>() {

            var pageNumber = 1
            var clouds = ArrayList<Pixabay.CloudImage>()

            override fun handleApiSuccessResponse(response: ApiSuccessResponse<Pixabay.Response>) {

                clouds.addAll(response.body.cloudImages as ArrayList<Pixabay.CloudImage>)

                response.body.totalHits?.let {
                    when (it > clouds.size) {
                        true -> this.doCall()
                        else -> {
                            d("DEBUG:  HOW MANY CLOUDS????  ${clouds.size}")
                            result.value = DataState.data(
                                data = ChooseCloudViewState(cloudImages = clouds)
                            )
                        }
                    }
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<Pixabay.Response>> {
                return RetrofitBuilder.apiService.getCumulusPhotos(page = pageNumber++)
            }

        }.asLiveData()
    }
}