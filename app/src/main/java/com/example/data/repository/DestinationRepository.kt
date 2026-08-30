package com.example.data.repository

import com.example.data.model.Destination
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DestinationRepository {

    private val initialDestinations = listOf(
        Destination(
            id = "machu_picchu",
            name = "Machu Picchu",
            region = "Cusco",
            category = "Cultura",
            rating = 4.9,
            description = "Iconica ciudadela inca asentada en las alturas de las montañas de los Andes. Una de las Siete Maravillas del Mundo Moderno y Patrimonio de la Humanidad por la UNESCO.",
            history = "Construida a mediados del siglo XV durante el mandato del inca Pachacútec, sirvió como santuario religioso y residencia real de la élite incaica.",
            activities = listOf("Recorrido arqueológico", "Trekking Huayna Picchu", "Fotografía paisajística", "Viaje en tren Panorámico"),
            imageUrl = "https://images.unsplash.com/photo-1526392060635-9d6019884377",
            latitude = -13.1631,
            longitude = -72.5450,
            isFeatured = true,
            isPopular = true
        ),
        Destination(
            id = "cusco_centro",
            name = "Cusco Histórico",
            region = "Cusco",
            category = "Cultura",
            rating = 4.8,
            description = "La antigua capital del Imperio Inca (Tawantinsuyu), famosa por su arquitectura colonial española levantada sobre cimientos incaicos intactos.",
            history = "Considerada el 'Ombligo del Mundo' por la civilización inca, fue reconvertida tras la conquista en una vibrante metrópoli colonial con iglesias barrocas y templos ancestrales.",
            activities = listOf("Visita al Qorikancha", "Paseo por el barrio San Blas", "Recorrido Sacsayhuamán", "Gastronomía andina"),
            imageUrl = "https://images.unsplash.com/photo-1589802829985-817e51171b92",
            latitude = -13.5319,
            longitude = -71.9675,
            isFeatured = true,
            isPopular = true
        ),
        Destination(
            id = "huacachina",
            name = "Oasis de Huacachina",
            region = "Ica",
            category = "Aventura",
            rating = 4.7,
            description = "Un oasis natural impresionante en medio del desierto costero peruano, rodeado de imponentes dunas de arena dorada y palmeras.",
            history = "La leyenda cuenta que la laguna nació de las lágrimas de una doncella inca que lloraba la pérdida de su amado guerrero.",
            activities = listOf("Sandboard en dunas", "Paseo en tubulares (Buggies)", "Paseo nocturno en el oasis", "Cata de Pisco en bodegas"),
            imageUrl = "https://images.unsplash.com/photo-1509316975850-ff9c5deb0cd9",
            latitude = -14.0875,
            longitude = -75.7626,
            isFeatured = true,
            isPopular = true
        ),
        Destination(
            id = "paracas",
            name = "Reserva Nacional de Paracas",
            region = "Ica",
            category = "Naturaleza",
            rating = 4.7,
            description = "Santuario marino costero famoso por las Islas Ballestas, acantilados rojizos sobre el Pacífico y vasta fauna silvestre.",
            history = "Cuna de la milenaria cultura Paracas (700 a.C. - 200 d.C.), célebre por sus mantos textiles y conocimientos en trepanaciones craneanas.",
            activities = listOf("Tour en bote a Islas Ballestas", "Avistamiento de lobos marinos", "Playa Roja", "Circuito de bicicletas"),
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e",
            latitude = -13.8378,
            longitude = -76.2525,
            isFeatured = false,
            isPopular = true
        ),
        Destination(
            id = "huaraz",
            name = "Laguna 69 - Huaraz",
            region = "Ancash",
            category = "Aventura",
            rating = 4.9,
            description = "Espectacular laguna turquesa a 4,600 m.s.n.m. al pie del imponente nevado Chacraraju en la Cordillera Blanca.",
            history = "Forma parte del Parque Nacional Huascarán, reserva mundial de la biosfera con más de 600 glaciares e incontables picos de más de 6,000 metros.",
            activities = listOf("Trekking de altura", "Fotografía de montaña", "Camping en Llanganuco", "Andinismo"),
            imageUrl = "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b",
            latitude = -9.0718,
            longitude = -77.6186,
            isFeatured = true,
            isPopular = true
        ),
        Destination(
            id = "arequipa",
            name = "Arequipa y Cañón del Colca",
            region = "Arequipa",
            category = "Cultura",
            rating = 4.8,
            description = "La 'Ciudad Blanca' construida con piedra volcánica sillar, custodiada por el Misti y hogar del Cañón del Colca, uno de los más profundos del mundo.",
            history = "Fundada en 1540 en un fértil valle volcánico, su Centro Histórico exhibe una síntesis única de técnicas de construcción hispánicas e indígenas.",
            activities = listOf("Avistamiento de cóndores", "Monasterio de Santa Catalina", "Ruta del Sillar", "Baños termales La Calera"),
            imageUrl = "https://images.unsplash.com/photo-1544735716-392fe2489ffa",
            latitude = -16.4090,
            longitude = -71.5375,
            isFeatured = false,
            isPopular = true
        ),
        Destination(
            id = "iquitos",
            name = "Selva de Iquitos",
            region = "Loreto",
            category = "Selva",
            rating = 4.8,
            description = "La ciudad más grande del mundo inaccesible por carretera, puerta de entrada al majestuoso Río Amazonas y la selva tropical profunda.",
            history = "Tuvo su gran época de esplendor durante la Fiebre del Caucho a finales del siglo XIX, conservando mansiones azulejadas como la Casa de Fierro de Eiffel.",
            activities = listOf("Navegación Río Amazonas", "Reserva Pacaya Samiria", "Avistamiento de delfines rosados", "Visita a comunidades nativas"),
            imageUrl = "https://images.unsplash.com/photo-1516026672322-bc52d61a55d5",
            latitude = -3.7437,
            longitude = -73.2516,
            isFeatured = true,
            isPopular = true
        ),
        Destination(
            id = "puno",
            name = "Lago Titicaca y Uros",
            region = "Puno",
            category = "Cultura",
            rating = 4.6,
            description = "El lago navegable más alto del mundo (3,812 m.s.n.m.), célebre por sus ancestrales islas flotantes de totora habitadas por la comunidad Uros.",
            history = "Según la mitología andina, de sus frías aguas emergieron Manco Cápac y Mama Ocllo para fundar el legendario Imperio de los Incas.",
            activities = listOf("Navegación en islas flotantes", "Turismo vivencial en Taquile", "Sillustani", "Danzas de la Candelaria"),
            imageUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23",
            latitude = -15.8402,
            longitude = -70.0219,
            isFeatured = false,
            isPopular = false
        ),
        Destination(
            id = "mancora",
            name = "Playas de Máncora",
            region = "Piura",
            category = "Playa",
            rating = 4.7,
            description = "Paraíso tropical en el norte peruano con cálidas olas todo el año, ambiente veraniego constante y gastronomía marina de primer nivel.",
            history = "De antiguo pueblo de pescadores a punto de encuentro global para surfistas y amantes del clima cálido del Pacífico ecuatorial.",
            activities = listOf("Surf y Kitesurf", "Nado con tortugas en El Ñuro", "Avistamiento de ballenas jorobadas", "Degustación de ceviche fresco"),
            imageUrl = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e",
            latitude = -4.1078,
            longitude = -81.0475,
            isFeatured = false,
            isPopular = true
        ),
        Destination(
            id = "trujillo",
            name = "Chan Chan y Trujillo",
            region = "La Libertad",
            category = "Cultura",
            rating = 4.6,
            description = "La 'Ciudad de la Eterna Primavera' rodeada por Chan Chan, la ciudad de barro más grande de América precolombina.",
            history = "Capital de la sofisticada civilización Chimú (siglos X al XV), destacan sus relieves tallados en barro, plazas ceremoniales y las Huacas del Sol y de la Luna.",
            activities = listOf("Recorrido Chan Chan", "Huaca de la Luna", "Caballitos de totora en Huanchaco", "Espectáculo de Marinera Norteña"),
            imageUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23",
            latitude = -8.1118,
            longitude = -79.0287,
            isFeatured = false,
            isPopular = false
        )
    )

    private val _destinationsFlow = MutableStateFlow(initialDestinations)
    val destinationsFlow: Flow<List<Destination>> = _destinationsFlow.asStateFlow()

    fun getAllDestinations(): List<Destination> = _destinationsFlow.value

    fun getDestinationById(id: String): Destination? {
        return _destinationsFlow.value.find { it.id == id }
    }

    fun getFeaturedDestinations(): List<Destination> {
        return _destinationsFlow.value.filter { it.isFeatured }
    }

    fun getPopularDestinations(): List<Destination> {
        return _destinationsFlow.value.filter { it.isPopular }
    }

    fun searchDestinations(query: String, category: String = "Todos"): List<Destination> {
        return _destinationsFlow.value.filter { destination ->
            val matchesQuery = query.isBlank() ||
                    destination.name.contains(query, ignoreCase = true) ||
                    destination.region.contains(query, ignoreCase = true)

            val matchesCategory = category.equals("Todos", ignoreCase = true) ||
                    destination.category.equals(category, ignoreCase = true)

            matchesQuery && matchesCategory
        }
    }
}
