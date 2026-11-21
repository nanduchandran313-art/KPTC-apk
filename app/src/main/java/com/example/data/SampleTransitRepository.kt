package com.example.data

import com.example.model.BusRoute
import com.example.model.RouteCategory
import com.example.model.TimetableWindow

class SampleTransitRepository {
    fun getSampleRoutes(): List<BusRoute> = listOf(
        BusRoute(
            id = "route_13",
            number = "13",
            name = "Airport route",
            from = "Kuwait City",
            to = "Kuwait International Airport",
            frequency = "Approx. every 30 min (sample)",
            category = RouteCategory.AIRPORT,
            illustrativeStops = listOf(
                "Mirqab Bus Terminal",
                "Kuwait City Financial District",
                "Airport Road (Route 55)",
                "Farwaniya Interchange",
                "Kuwait International Airport"
            ),
            illustrativeWindows = listOf(
                TimetableWindow("Morning", "05:30 – 11:00", "Sample ~30 min interval"),
                TimetableWindow("Midday", "11:00 – 17:00", "Sample ~30 min interval"),
                TimetableWindow("Evening", "17:00 – 23:00", "Sample ~30–40 min interval")
            )
        ),
        BusRoute(
            id = "route_15",
            number = "15",
            name = "City route",
            from = "Mirqab",
            to = "Hawally",
            frequency = "Approx. every 20 min (sample)",
            category = RouteCategory.CITY,
            illustrativeStops = listOf(
                "Mirqab Central",
                "Sharq Waterfront",
                "Dasman District",
                "Shaab Park",
                "Hawally Tunis Street"
            ),
            illustrativeWindows = listOf(
                TimetableWindow("Morning", "06:00 – 11:30", "Sample ~20 min interval"),
                TimetableWindow("Midday", "11:30 – 16:30", "Sample ~25 min interval"),
                TimetableWindow("Evening", "16:30 – 22:30", "Sample ~20 min interval")
            )
        ),
        BusRoute(
            id = "route_coastal",
            number = "BUS",
            name = "Coastal route",
            from = "Salmiya",
            to = "Kuwait City",
            frequency = "Schedule to be confirmed",
            category = RouteCategory.COASTAL,
            illustrativeStops = listOf(
                "Salmiya Marina",
                "Gulf Road Corniche",
                "Marina Crescent",
                "Kuwait Towers Promenade",
                "Kuwait City Terminal"
            ),
            illustrativeWindows = listOf(
                TimetableWindow("Daytime", "Pending official feed", "Schedule to be confirmed"),
                TimetableWindow("Evening", "Pending official feed", "Schedule to be confirmed")
            )
        )
    )
}
