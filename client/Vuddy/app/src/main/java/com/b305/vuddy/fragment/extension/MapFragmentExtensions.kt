package com.b305.vuddy.fragment.extension

import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.b305.vuddy.R
import com.b305.vuddy.fragment.MapFragment
import com.b305.vuddy.model.MapFeedResponse
import com.b305.vuddy.util.ANIMATE_CAMERA
import com.b305.vuddy.util.BASIC_IMG_URL
import com.b305.vuddy.util.DIALOG_MODE
import com.b305.vuddy.util.FRIEND_FEED_MODE
import com.b305.vuddy.util.GOTOHOME_IMG_URL
import com.b305.vuddy.util.GOTOWORK_IMG_URL
import com.b305.vuddy.util.HOME_IMG_URL
import com.b305.vuddy.util.MOVE_CAMERA
import com.b305.vuddy.util.NOT_MOVE_CAMERA
import com.b305.vuddy.util.OFFICE_IMG_URL
import com.b305.vuddy.util.RetrofitAPI
import com.b305.vuddy.util.SLEEP_IMG_URL
import com.b305.vuddy.util.ZOOM_LEVEL
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


fun MapFragment.onItemClicked(nickname: String) {
    Log.d("MapFragment", "****FriendFeedMode")
    friendListBottomSheetFragment.dismiss()
    markerMode = FRIEND_FEED_MODE
    binding.fabMapMode.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.unselected)
    binding.fabFeedMode.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.unselected)
    binding.fabFriendFeedMode.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.selected)

    val call = RetrofitAPI.mapFeedService
    call.getOneFriendFeed(nickname).enqueue(object : Callback<MapFeedResponse> {
        override fun onResponse(call: Call<MapFeedResponse>, response: Response<MapFeedResponse>) {
            if (response.isSuccessful) {
                val result = response.body()?.mapFeedList
                result?.forEach { mapFeed ->
                    val feedId = "FEED:${mapFeed.feedId}"
                    val imgUrl = mapFeed.imgUrl
                    val location = mapFeed.location
                    val (latitudeStr, longitudeStr) = location.split(",")
                    val latitude: Double = latitudeStr.trim().toDouble()
                    val longitude: Double = longitudeStr.trim().toDouble()
                    val latLng = LatLng(latitude, longitude)
                    Log.d("MapFragment", "****FriendFeedMode result : $feedId, $imgUrl, $latLng")


                    val statusImgUrl = BASIC_IMG_URL
                    val marKerOptions = makeMarkerOptions(feedId, latLng, imgUrl, statusImgUrl)
                    markerOptionsMap[feedId] = marKerOptions

                    val existingMarker = markersMap[feedId]
                    if (existingMarker != null) {
                        animateMarkerTo(existingMarker, marKerOptions.position)
                    } else {
                        val newMarker = mMap.addMarker(marKerOptions)!!
                        markersMap[feedId] = newMarker
                    }
                }

                if (result != null) {
                    markersMap.filterKeys { it != currentNickname && !result.any { feed -> "FEED:${feed.feedId}" == it } }
                        .forEach { (nickname, marker) ->
                            marker.remove()
                            markersMap.remove(nickname)
                        }
                }
                if (result != null) {
                    markerOptionsMap.filterKeys { it != currentNickname && !result.any { feed -> "FEED:${feed.feedId}" == it } }
                        .forEach { (nickname, _) ->
                            markerOptionsMap.remove(nickname)
                        }
                }

                this@onItemClicked.refreshMap(ANIMATE_CAMERA)
            } else {
                val errorMessage = JSONObject(response.errorBody()?.string()!!)
                Log.d("MapFragment", "****FriendFeedMode errorMessage : $errorMessage")
                Toast.makeText(context, errorMessage.getString("message"), Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<MapFeedResponse>, t: Throwable) {
            Log.d("MapFragment", "****FriendFeedMode onFailure : ${t.message}")
            Toast.makeText(context, "피드 불러오기 실패", Toast.LENGTH_SHORT).show()
        }

    })
}

fun MapFragment.makeMarkerOptions(nickname: String, latLng: LatLng, profileImgUrl: String, statusImgUrl: String): MarkerOptions =
    runBlocking {
        val profileBitmap = makeProfileBitmap(profileImgUrl)
        val statusBitmap = makeStatusBitmap(statusImgUrl)

        val resultBitmap = Bitmap.createBitmap(statusBitmap.width, statusBitmap.height, statusBitmap.config)
        val canvas = Canvas(resultBitmap)
        canvas.drawBitmap(statusBitmap, 0f, 0f, null)
        val profileTop = (resultBitmap.height - profileBitmap.height + 30f) / 2f
        val profileLeft = (resultBitmap.width - profileBitmap.width) / 2f
        canvas.drawBitmap(profileBitmap, profileLeft, profileTop, null)
        val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(resultBitmap)
        markerBitmapMap[nickname] = resultBitmap
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
            .icon(bitmapDescriptor)
        return@runBlocking markerOptions
    }

private suspend fun MapFragment.makeProfileBitmap(imgUrl: String): Bitmap = withContext(Dispatchers.IO) {
    return@withContext Glide.with(requireContext())
        .asBitmap()
        .load(imgUrl)
        .circleCrop()
        .override(150, 150)
        .submit()
        .get()
}

private val statusUrlMap = mapOf(
    "normal" to BASIC_IMG_URL,
    "home" to HOME_IMG_URL,
    "office" to OFFICE_IMG_URL,
    "gotowork" to GOTOWORK_IMG_URL,
    "gotohome" to GOTOHOME_IMG_URL,
    "sleep" to SLEEP_IMG_URL
)

private suspend fun MapFragment.makeStatusBitmap(imgStatus: String): Bitmap = withContext(Dispatchers.IO) {
    val statusUrl = statusUrlMap[imgStatus] ?: BASIC_IMG_URL

    return@withContext Glide.with(requireContext())
        .asBitmap()
        .load(statusUrl)
        .override(220, 220)
        .submit()
        .get()
}

fun MapFragment.refreshMap(cameraMode: Int) {
    if (markerMode == DIALOG_MODE) {
        return
    }
    activity?.runOnUiThread {
        markerOptionsMap.forEach { (nickname, markerOptions) ->
            val marker = markersMap[nickname]
            if (marker != null) {
                animateMarkerTo(marker, markerOptions.position)
            } else {
                val newMarker = mMap.addMarker(markerOptions)!!
                markersMap[nickname] = newMarker
            }
        }
        val latLng = markersMap[sharedManager.getCurrentUser().nickname]!!.position
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL)
        when (cameraMode) {
            MOVE_CAMERA -> {
                mMap.moveCamera(cameraUpdate)
            }

            ANIMATE_CAMERA -> {
                mMap.animateCamera(cameraUpdate)
            }

            NOT_MOVE_CAMERA -> {
                // do nothing
            }
        }
    }
    return
}

fun MapFragment.animateMarkerTo(marker: Marker, targetPosition: LatLng) {
    activity?.runOnUiThread {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = 1000
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.addUpdateListener { animation ->
            val fraction = animation.animatedFraction
            val lat = marker.position.latitude + (targetPosition.latitude - marker.position.latitude) * fraction
            val lng = marker.position.longitude + (targetPosition.longitude - marker.position.longitude) * fraction
            marker.position = LatLng(lat, lng)
        }
        valueAnimator.start()
    }
}
