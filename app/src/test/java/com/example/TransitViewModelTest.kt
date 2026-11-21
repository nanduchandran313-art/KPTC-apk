package com.example

import com.example.model.RouteCategory
import com.example.ui.SheetMode
import com.example.ui.TransitViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TransitViewModelTest {

    @Test
    fun `search query and category filter sample routes accurately`() {
        val vm = TransitViewModel()
        assertEquals(3, vm.uiState.value.filteredRoutes.size)

        vm.onSearchQueryChange("Airport")
        assertEquals(1, vm.uiState.value.filteredRoutes.size)
        assertEquals("13", vm.uiState.value.filteredRoutes.first().number)

        vm.onCategorySelect(RouteCategory.COASTAL)
        assertEquals(0, vm.uiState.value.filteredRoutes.size)

        vm.onResetFilters()
        assertEquals("", vm.uiState.value.searchQuery)
        assertEquals(RouteCategory.ALL, vm.uiState.value.selectedCategory)
        assertEquals(3, vm.uiState.value.filteredRoutes.size)
    }

    @Test
    fun `route stops and timetable sheets open and reverse direction`() {
        val vm = TransitViewModel()
        val route = vm.uiState.value.allRoutes.first()

        vm.openTimetableSheet(route)
        assertEquals(SheetMode.TIMETABLE_PREVIEW, vm.uiState.value.sheetMode)
        assertEquals(route.id, vm.uiState.value.selectedRouteForSheet?.id)
        assertFalse(vm.uiState.value.isDirectionReversed)

        vm.toggleSheetDirection()
        assertTrue(vm.uiState.value.isDirectionReversed)

        vm.dismissSheet()
        assertNull(vm.uiState.value.selectedRouteForSheet)
    }
}
