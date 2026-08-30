package com.example.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.di.AppContainer
import com.example.presentation.auth.AuthViewModel
import com.example.presentation.auth.LoginScreen
import com.example.presentation.auth.RegisterScreen
import com.example.presentation.auth.ResetPasswordScreen  // 👈 NUEVA IMPORTACIÓN
import com.example.presentation.detail.DetailScreen
import com.example.presentation.detail.DetailViewModel
import com.example.presentation.explore.ExploreScreen
import com.example.presentation.explore.ExploreViewModel
import com.example.presentation.favorites.FavoritesScreen
import com.example.presentation.favorites.FavoritesViewModel
import com.example.presentation.home.HomeScreen
import com.example.presentation.home.HomeViewModel
import com.example.presentation.map.MapScreen
import com.example.presentation.myroute.MyRouteScreen
import com.example.presentation.myroute.MyRouteViewModel
import com.example.presentation.profile.ProfileScreen
import com.example.presentation.profile.ProfileViewModel
import com.example.presentation.splash.SplashScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val container = remember { AppContainer.getInstance(context) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavRoutes = listOf(
        Screen.Home.route,
        Screen.Explore.route,
        Screen.Favorites.route,
        Screen.MyRoute.route,
        Screen.Profile.route
    )

    val showBottomBar = currentRoute in bottomNavRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            // Splash Screen
            composable(Screen.Splash.route) {
                SplashScreen(
                    encryptedPreferences = container.encryptedPreferences,
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            // Auth: Login
            composable(Screen.Login.route) {
                val authViewModel = remember { AuthViewModel(container.userRepository) }
                LoginScreen(
                    authViewModel = authViewModel,
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    },
                    onNavigateToResetPassword = {  // 👈 NUEVO CALLBACK
                        navController.navigate("reset")
                    }
                )
            }

            // Auth: Register
            composable(Screen.Register.route) {
                val authViewModel = remember { AuthViewModel(container.userRepository) }
                RegisterScreen(
                    authViewModel = authViewModel,
                    onRegisterSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            // Auth: Reset Password (NUEVO BLOQUE)
            composable("reset") {
                val authViewModel = remember { AuthViewModel(container.userRepository) }
                ResetPasswordScreen(
                    authViewModel = authViewModel,
                    onResetSent = {
                        // Después de enviar el correo, volvemos al login
                        navController.popBackStack()
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            // Home
            composable(Screen.Home.route) {
                val homeViewModel = remember { HomeViewModel(container.destinationRepository, container.userRepository) }
                HomeScreen(
                    homeViewModel = homeViewModel,
                    onDestinationClick = { destinationId ->
                        navController.navigate(Screen.Detail.createRoute(destinationId))
                    },
                    onNavigateToMyRoute = {
                        navController.navigate(Screen.MyRoute.route)
                    }
                )
            }

            // Explore
            composable(Screen.Explore.route) {
                val exploreViewModel = remember { ExploreViewModel(container.destinationRepository, container.userRepository) }
                ExploreScreen(
                    exploreViewModel = exploreViewModel,
                    onDestinationClick = { destinationId ->
                        navController.navigate(Screen.Detail.createRoute(destinationId))
                    }
                )
            }

            // Favorites
            composable(Screen.Favorites.route) {
                val favoritesViewModel = remember { FavoritesViewModel(container.userRepository) }
                FavoritesScreen(
                    favoritesViewModel = favoritesViewModel,
                    onDestinationClick = { destinationId ->
                        navController.navigate(Screen.Detail.createRoute(destinationId))
                    }
                )
            }

            // My Route
            composable(Screen.MyRoute.route) {
                val myRouteViewModel = remember {
                    MyRouteViewModel(
                        container.destinationRepository,
                        container.itineraryRepository,
                        container.generateItineraryUseCase
                    )
                }
                MyRouteScreen(
                    myRouteViewModel = myRouteViewModel
                )
            }

            // Profile
            composable(Screen.Profile.route) {
                val profileViewModel = remember { ProfileViewModel(container.userRepository) }
                ProfileScreen(
                    profileViewModel = profileViewModel,
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            // Detail
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("destinationId") { type = NavType.StringType })
            ) { backStackEntry ->
                val destinationId = backStackEntry.arguments?.getString("destinationId") ?: "machu_picchu"
                val detailViewModel = remember {
                    DetailViewModel(
                        container.destinationRepository,
                        container.userRepository,
                        container.weatherRepository
                    )
                }

                DetailScreen(
                    destinationId = destinationId,
                    detailViewModel = detailViewModel,
                    onBackClick = { navController.popBackStack() },
                    onOpenMap = { destId ->
                        navController.navigate(Screen.Map.createRoute(destId))
                    },
                    onAddToMyRoute = { _ ->
                        navController.navigate(Screen.MyRoute.route)
                    }
                )
            }

            // Map Screen
            composable(
                route = Screen.Map.route,
                arguments = listOf(navArgument("destinationId") { type = NavType.StringType })
            ) { backStackEntry ->
                val destinationId = backStackEntry.arguments?.getString("destinationId") ?: "machu_picchu"
                MapScreen(
                    destinationId = destinationId,
                    destinationRepository = container.destinationRepository,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}