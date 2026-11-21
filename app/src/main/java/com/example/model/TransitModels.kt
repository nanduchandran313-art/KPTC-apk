package com.example.model

import androidx.annotation.StringRes
import com.example.R

enum class TransitRouteKey(val route: String, @StringRes val labelRes: Int) {
    HOME("home", R.string.nav_home),
    ROUTES("routes", R.string.nav_routes),
    TIMINGS("timings", R.string.nav_timings),
    TRACK("track", R.string.nav_track)
}

enum class RouteCategory(@StringRes val labelRes: Int) {
    ALL(R.string.routes_filter_all),
    AIRPORT(R.string.routes_filter_airport),
    CITY(R.string.routes_filter_city),
    COASTAL(R.string.routes_filter_coastal)
}

data class TimetableWindow(
    val period: String,
    val hours: String,
    val headwayNote: String
)

data class BusRoute(
    val id: String,
    val number: String,
    val name: String,
    val from: String,
    val to: String,
    val frequency: String,
    val category: RouteCategory,
    val illustrativeStops: List<String>,
    val illustrativeWindows: List<TimetableWindow>
)
