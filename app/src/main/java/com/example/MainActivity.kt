package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.model.TransitRouteKey
import com.example.ui.TransitViewModel
import com.example.ui.components.RouteDetailModalSheet
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.RoutesScreen
import com.example.ui.screens.TimingsScreen
import com.example.ui.screens.TrackScreen
import com.example.ui.theme.KptcKuwaitTheme
import com.example.ui.theme.KptcNavyDeep

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(KptcNavyDeep.toArgb())
        )
        setContent {
            KptcKuwaitTheme {
                KptcKuwaitApp()
            }
        }
    }
}

@Composable
fun KptcKuwaitApp(
    viewModel: TransitViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val navigateToTab: (TransitRouteKey) -> Unit = { tab ->
        navController.navigate(tab.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useRail = maxWidth >= 600.dp

        if (useRail) {
            Row(modifier = Modifier.fillMaxSize()) {
                KptcNavigationRail(
                    currentRoute = currentDestination?.route,
                    onSelectTab = navigateToTab
                )
                KptcNavHost(
                    navController = navController,
                    viewModel = viewModel,
                    onNavigateTab = navigateToTab,
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.background,
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                bottomBar = {
                    KptcBottomBar(
                        isSelected = { route ->
                            currentDestination?.hierarchy?.any { it.route == route } == true
                        },
                        onSelectTab = navigateToTab
                    )
                }
            ) { innerPadding ->
                KptcNavHost(
                    navController = navController,
                    viewModel = viewModel,
                    onNavigateTab = navigateToTab,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }

    val activeRoute = uiState.selectedRouteForSheet
    if (activeRoute != null) {
        RouteDetailModalSheet(
            route = activeRoute,
            sheetMode = uiState.sheetMode,
            isReversed = uiState.isDirectionReversed,
            onToggleDirection = viewModel::toggleSheetDirection,
            onDismiss = viewModel::dismissSheet
        )
    }
}

@Composable
private fun KptcNavHost(
    navController: NavHostController,
    viewModel: TransitViewModel,
    onNavigateTab: (TransitRouteKey) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = TransitRouteKey.HOME.route,
        modifier = modifier.fillMaxSize()
    ) {
        composable(TransitRouteKey.HOME.route) {
            HomeScreen(
                sampleRoutes = uiState.allRoutes,
                onNavigateRoutes = { onNavigateTab(TransitRouteKey.ROUTES) },
                onNavigateTimings = { onNavigateTab(TransitRouteKey.TIMINGS) },
                onNavigateTrack = { onNavigateTab(TransitRouteKey.TRACK) },
                onSelectRoute = viewModel::openRouteStopsSheet
            )
        }
        composable(TransitRouteKey.ROUTES.route) {
            RoutesScreen(
                routes = uiState.filteredRoutes,
                query = uiState.searchQuery,
                selectedCategory = uiState.selectedCategory,
                onQueryChange = viewModel::onSearchQueryChange,
                onCategorySelect = viewModel::onCategorySelect,
                onResetFilters = viewModel::onResetFilters,
                onSelectRoute = viewModel::openRouteStopsSheet
            )
        }
        composable(TransitRouteKey.TIMINGS.route) {
            TimingsScreen(
                routes = uiState.allRoutes,
                onViewTimetable = viewModel::openTimetableSheet
            )
        }
        composable(TransitRouteKey.TRACK.route) {
            TrackScreen(
                routes = uiState.allRoutes,
                selectedRouteId = uiState.selectedTrackRouteId,
                onSelectRouteId = viewModel::onTrackRouteSelect
            )
        }
    }
}

@Composable
private fun KptcBottomBar(
    isSelected: (String) -> Boolean,
    onSelectTab: (TransitRouteKey) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp
    ) {
        TransitRouteKey.entries.forEach { tab ->
            val selected = isSelected(tab.route)
            val label = stringResource(tab.labelRes)
            NavigationBarItem(
                selected = selected,
                onClick = { onSelectTab(tab) },
                icon = {
                    Icon(
                        imageVector = tabIcon(tab, selected),
                        contentDescription = label
                    )
                },
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("nav_tab_${tab.route}")
            )
        }
    }
}

@Composable
private fun KptcNavigationRail(
    currentRoute: String?,
    onSelectTab: (TransitRouteKey) -> Unit
) {
    NavigationRail(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxHeight()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Spacer(modifier = Modifier.weight(1f))
        TransitRouteKey.entries.forEach { tab ->
            val selected = currentRoute == tab.route
            val label = stringResource(tab.labelRes)
            NavigationRailItem(
                selected = selected,
                onClick = { onSelectTab(tab) },
                icon = {
                    Icon(
                        imageVector = tabIcon(tab, selected),
                        contentDescription = label
                    )
                },
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium
                    )
                },
                colors = NavigationRailItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("nav_tab_${tab.route}")
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

private fun tabIcon(tab: TransitRouteKey, selected: Boolean) = when (tab) {
    TransitRouteKey.HOME -> if (selected) Icons.Filled.Home else Icons.Outlined.Home
    TransitRouteKey.ROUTES -> if (selected) Icons.Filled.DirectionsBus else Icons.Outlined.DirectionsBus
    TransitRouteKey.TIMINGS -> if (selected) Icons.Filled.Schedule else Icons.Outlined.Schedule
    TransitRouteKey.TRACK -> if (selected) Icons.Filled.Map else Icons.Outlined.Map
}
