package com.b305.buddy.extension

import com.b305.buddy.fragment.MapFragment
import com.b305.buddy.model.FriendLocation
import com.b305.buddy.util.LocationProvider
import com.b305.buddy.util.SharedManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


fun MapFragment.getMyLocation(): LatLng {
    val locationProvider = LocationProvider(requireActivity())
    val currentLat = locationProvider.getLocationLatitude()!!
    val currentLng = locationProvider.getLocationLongitude()!!
    return LatLng(currentLat, currentLng)
}

fun MapFragment.moveCameraToCurrentLocation(googleMap: GoogleMap, friendLocationList: MutableList<FriendLocation>) {
    val myLocation = getMyLocation()
    setMarker(myLocation, googleMap, friendLocationList)
    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 14f))
}

fun setMarker(myLocation: LatLng, googleMap: GoogleMap, friendLocationList: MutableList<FriendLocation>) {
    googleMap.let {
        it.clear()
        val markerOption = MarkerOptions()
        markerOption.position(myLocation)
        it.addMarker(markerOption)

        for (friendLocation in friendLocationList) {
            val friendLatLng = LatLng(friendLocation.lat!!, friendLocation.lng!!)
            val friendMarkerOption = MarkerOptions()
            friendMarkerOption.position(friendLatLng)
            it.addMarker(friendMarkerOption)
        }
    }
}

fun logout(sharedManager: SharedManager) {
    sharedManager.removeCurrentToken()
    sharedManager.removeCurrentUser()
}

fun MapFragment.renewFriendList(
    friendLocationList: MutableList<FriendLocation>,
    friendLocation: FriendLocation
): MutableList<FriendLocation> {
    if (friendLocation in friendLocationList) {
        friendLocationList.remove(friendLocation)
        friendLocationList.add(friendLocation)
        return friendLocationList
    }
    friendLocationList.add(friendLocation)
    return friendLocationList
}
