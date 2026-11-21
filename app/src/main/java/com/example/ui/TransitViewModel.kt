package com.example.ui

import androidx.lifecycle.ViewModel
import com.example.data.SampleTransitRepository
import com.example.model.BusRoute
import com.example.model.RouteCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class SheetMode {
    ROUTE_STOPS,
    TIMETABLE_PREVIEW
}

data class TransitUiState(
    val allRoutes: List<BusRoute> = emptyList(),
    val filteredRoutes: List<BusRoute> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: RouteCategory = RouteCategory.ALL,
    val selectedRouteForSheet: BusRoute? = null,
    val sheetMode: SheetMode = SheetMode.ROUTE_STOPS,
    val isDirectionReversed: Boolean = false,
    val selectedTrackRouteId: String = "route_13"
)

class TransitViewModel(
    private val repository: SampleTransitRepository = SampleTransitRepository()
) : ViewModel() {

    private val initialRoutes = repository.getSampleRoutes()

    private val _uiState = MutableStateFlow(
        TransitUiState(
            allRoutes = initialRoutes,
            filteredRoutes = initialRoutes,
            selectedTrackRouteId = initialRoutes.firstOrNull()?.id ?: ""
        )
    )
    val uiState: StateFlow<TransitUiState> = _uiState.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _uiState.update { current ->
            current.copy(
                searchQuery = query,
                filteredRoutes = filterRoutes(
                    routes = current.allRoutes,
                    query = query,
                    category = current.selectedCategory
                )
            )
        }
    }

    fun onCategorySelect(category: RouteCategory) {
        _uiState.update { current ->
            current.copy(
                selectedCategory = category,
                filteredRoutes = filterRoutes(
                    routes = current.allRoutes,
                    query = current.searchQuery,
                    category = category
                )
            )
        }
    }

    fun onResetFilters() {
        _uiState.update { current ->
            current.copy(
                searchQuery = "",
                selectedCategory = RouteCategory.ALL,
                filteredRoutes = current.allRoutes
            )
        }
    }

    fun openRouteStopsSheet(route: BusRoute) {
        _uiState.update {
            it.copy(
                selectedRouteForSheet = route,
                sheetMode = SheetMode.ROUTE_STOPS,
                isDirectionReversed = false
            )
        }
    }

    fun openTimetableSheet(route: BusRoute) {
        _uiState.update {
            it.copy(
                selectedRouteForSheet = route,
                sheetMode = SheetMode.TIMETABLE_PREVIEW,
                isDirectionReversed = false
            )
        }
    }

    fun toggleSheetDirection() {
        _uiState.update { it.copy(isDirectionReversed = !it.isDirectionReversed) }
    }

    fun dismissSheet() {
        _uiState.update { it.copy(selectedRouteForSheet = null) }
    }

    fun onTrackRouteSelect(routeId: String) {
        _uiState.update { it.copy(selectedTrackRouteId = routeId) }
    }

    private fun filterRoutes(
        routes: List<BusRoute>,
        query: String,
        category: RouteCategory
    ): List<BusRoute> {
        val trimmed = query.trim()
        return routes.filter { route ->
            val matchesCategory = category == RouteCategory.ALL || route.category == category
            val matchesQuery = trimmed.isEmpty() ||
                "${route.number} ${route.name} ${route.from} ${route.to} ${route.illustrativeStops.joinToString(" ")}"
                    .contains(trimmed, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }
}
