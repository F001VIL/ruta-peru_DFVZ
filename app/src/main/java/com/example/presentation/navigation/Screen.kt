package com.example.presentation.navigation

sealed class Screen(val route: String, val title: String = "") {
    object Splash : Screen("splash", "Splash")
    object Login : Screen("login", "Iniciar Sesión")
    object Register : Screen("register", "Registrarse")
    object Home : Screen("home", "Inicio")
    object Explore : Screen("explore", "Explorar")
    object Favorites : Screen("favorites", "Favoritos")
    object MyRoute : Screen("my_route", "Mi Ruta")
    object Profile : Screen("profile", "Perfil")
    object Detail : Screen("detail/{destinationId}", "Detalle del Destino") {
        fun createRoute(destinationId: String) = "detail/$destinationId"
    }
    object Map : Screen("map/{destinationId}", "Mapa Interactivo") {
        fun createRoute(destinationId: String) = "map/$destinationId"
    }
}
