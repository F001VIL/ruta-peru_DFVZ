package com.example.domain.usecase

import com.example.data.model.Itinerary
import com.example.data.model.ItineraryDay

class GenerateItineraryUseCase {

    fun execute(
        destinationName: String,
        daysCount: Int,
        budget: String,
        interests: List<String>
    ): Itinerary {
        val daysList = mutableListOf<ItineraryDay>()

        for (day in 1..daysCount) {
            val titleAndActivities = generateDayPlan(destinationName, day, budget, interests)
            daysList.add(
                ItineraryDay(
                    dayNumber = day,
                    title = titleAndActivities.first,
                    activities = titleAndActivities.second
                )
            )
        }

        return Itinerary(
            id = "itin_${destinationName.lowercase().replace(" ", "_")}_${System.currentTimeMillis()}",
            destinationName = destinationName,
            daysCount = daysCount,
            budget = budget,
            interests = interests,
            days = daysList
        )
    }

    private fun generateDayPlan(
        destination: String,
        day: Int,
        budget: String,
        interests: List<String>
    ): Pair<String, List<String>> {
        val isCulture = interests.contains("Cultura")
        val isAdventure = interests.contains("Aventura")
        val isNature = interests.contains("Naturaleza")
        val isFoodie = interests.contains("Gastronomía")
        val isPhoto = interests.contains("Fotografía")

        return when {
            destination.contains("Cusco", ignoreCase = true) || destination.contains("Machu Picchu", ignoreCase = true) -> {
                when (day) {
                    1 -> Pair(
                        "Bienvenida e Aclimatación en la Capital Inca",
                        listOf(
                            "Camino por el Centro Histórico y Plaza de Armas",
                            "Templo Inca Qorikancha (Santo Domingo)",
                            if (isFoodie) "Almuerzo de bienvenida: Lomo Saltado o Pacha Manca" else "Paseo por la Calle Hatun Rumiyoc (Piedra de 12 ángulos)",
                            "Mirador de San Cristóbal al atardecer"
                        )
                    )
                    2 -> Pair(
                        "Valle Sagrado de los Incas",
                        listOf(
                            "Complejo Arqueológico de Pisac y Mercado Artesanal",
                            "Almuerzo buffet campestre en Urubamba",
                            "Fortaleza Inca de Ollantaytambo",
                            "Tren Expedition / Vistadome hacia Aguas Calientes"
                        )
                    )
                    3 -> Pair(
                        "Santuario Histórico de Machu Picchu",
                        listOf(
                            "Ascenso temprano en bus a la Ciudadela Inca",
                            "Visita guiada oficial por los circuitos principales",
                            if (isAdventure) "Caminata al Huayna Picchu o Montaña Machu Picchu" else "Sesión de fotografía en el Guardián",
                            "Retorno en tren y descanso en Cusco"
                        )
                    )
                    4 -> Pair(
                        "Maras, Moray y Chinchero",
                        listOf(
                            "Salineras de Maras con sus miles de pozas de sal",
                            "Andenes circulares concéntricos de Moray",
                            "Taller de tejido ancestral en Chinchero"
                        )
                    )
                    5 -> Pair(
                        if (isAdventure) "Montaña de 7 Colores (Vinicunca)" else "Laguna Humantay",
                        listOf(
                            "Salida de madrugada en transporte turístico",
                            if (isAdventure) "Trekking a 5,200 msnm con vista al nevado Ausangate" else "Caminata a la Laguna Turquesa Humantay",
                            "Almuerzo buffet en Mollepata",
                            "Noche libre en el barrio bohemio de San Blas"
                        )
                    )
                    6 -> Pair(
                        "Complejos Arqueológicos Cercanos",
                        listOf(
                            "Fortaleza de Sacsayhuamán y sus bloques megalíticos",
                            "Q'enqo, Puka Pukara y Tambomachay",
                            "Cena show con danzas folclóricas cusqueñas"
                        )
                    )
                    else -> Pair(
                        "Ruta del Sur y Compras Artesanales",
                        listOf(
                            "Templo de Andahuaylillas (La Capilla Sixtina de América)",
                            "Mercado Central de San Pedro",
                            "Despedida y traslado al aeropuerto Velasco Astete"
                        )
                    )
                }
            }

            destination.contains("Huacachina", ignoreCase = true) || destination.contains("Ica", ignoreCase = true) -> {
                when (day) {
                    1 -> Pair(
                        "Llegada al Oasis y Aventura en Dunas",
                        listOf(
                            "Paseo relajante alrededor de la Laguna Huacachina",
                            "Paseo lleno de adrenalina en carritos tubulares (Buggies)",
                            "Práctica de Sandboard al atardecer sobre dunas gigantes",
                            "Cena con vista al oasis iluminado"
                        )
                    )
                    2 -> Pair(
                        "Ruta del Pisco y Vino Iqueño",
                        listOf(
                            "Visita a la Bodega Artesanal El Catador",
                            "Cata guiada de Piscos Quebranta y Vinos Tacama",
                            "Almuerzo típico de Carapulcra con Sopa Seca",
                            "Visita al pueblo de Cachiche (Tierra de Brujas)"
                        )
                    )
                    else -> Pair(
                        "Cañón de los Perdidos",
                        listOf(
                            "Excursión 4x4 hacia el misterioso Cañón de los Perdidos",
                            "Fotografía panorámica de formaciones rocosas únicas",
                            "Retorno y descanso"
                        )
                    )
                }
            }

            destination.contains("Paracas", ignoreCase = true) -> {
                when (day) {
                    1 -> Pair(
                        "Islas Ballestas y Marina",
                        listOf(
                            "Tour en deslizador a las Islas Ballestas",
                            "Observación del misterioso Candelabro esculpido en arena",
                            "Avistamiento de lobos marinos y pingüinos de Humboldt",
                            "Almuerzo marino: Ceviche de Pescado y Causa de Cangrejo"
                        )
                    )
                    else -> Pair(
                        "Reserva Nacional de Paracas",
                        listOf(
                            "Paseo por la llamativa Playa Roja",
                            "Mirador de la Catedral y Bahía de Lagunillas",
                            "Visita al Centro de Interpretación de Paracas"
                        )
                    )
                }
            }

            destination.contains("Huaraz", ignoreCase = true) -> {
                when (day) {
                    1 -> Pair(
                        "Aclimatación en la Cordillera Blanca",
                        listOf(
                            "Paseo por la Plaza de Armas de Huaraz",
                            "Baños Termales de Monterrey",
                            "Prueba de la gastronomía ancashina: Picante de Cuy"
                        )
                    )
                    2 -> Pair(
                        "Laguna Llanganuco y Campo Santo de Yungay",
                        listOf(
                            "Visita al memorial del terremoto de Yungay",
                            "Paseo en bote por la Laguna Chinancoa",
                            "Fotografía del majestuoso Nevado Huascarán"
                        )
                    )
                    3 -> Pair(
                        "Trekking a la Laguna 69",
                        listOf(
                            "Partida de madrugada hacia Cebollapampa",
                            "Caminata de 3 horas rodeado de saltos de agua y glaciares",
                            "Contemplación del color turquesa intenso de Laguna 69"
                        )
                    )
                    else -> Pair(
                        "Monumento Arqueológico Chavín de Huántar",
                        listOf(
                            "Recorrido por las galerías subterráneas y el Lanzón Monolítico",
                            "Museo Nacional de Chavín y Cabezas Clavas",
                            "Retorno a Huaraz"
                        )
                    )
                }
            }

            destination.contains("Arequipa", ignoreCase = true) -> {
                when (day) {
                    1 -> Pair(
                        "Ciudad Blanca de Sillar",
                        listOf(
                            "Monasterio de Santa Catalina (Ciudad dentro de la ciudad)",
                            "Plaza de Armas y Catedral de Arequipa",
                            "Museo Santuarios Andinos (Momia Juanita)",
                            "Cena en Picantería Arequipeña: Rocoto Relleno"
                        )
                    )
                    2 -> Pair(
                        "Rumbo al Cañón del Colca (Chivay)",
                        listOf(
                            "Paso por la Reserva de Pampa Cañahuas y sus vicuñas",
                            "Mirador de los Andes a 4,910 msnm",
                            "Relajantes Baños Termales de La Calera en Chivay"
                        )
                    )
                    3 -> Pair(
                        "Cruz del Cóndor y Vuelo Majestuoso",
                        listOf(
                            "Mirador Cruz del Cóndor para ver el ave andina en pleno vuelo",
                            "Vista panorámica del cañón (3,400m de profundidad)",
                            "Retorno a la ciudad de Arequipa"
                        )
                    )
                    else -> Pair(
                        "Ruta del Sillar en Añashuayco",
                        listOf(
                            "Visita a las canteras donde se extrae la piedra volcánica sillar",
                            "Demostración en vivo de labrado en piedra por artesanos",
                            "Mirador Yanahuara con vista al Volcán Misti"
                        )
                    )
                }
            }

            destination.contains("Iquitos", ignoreCase = true) -> {
                when (day) {
                    1 -> Pair(
                        "Navegación Río Amazonas y Lodge",
                        listOf(
                            "Embarque en el Puerto de Nanay rumbo a la selva",
                            "Encuentro del Río Amazonas con el Río Itaya",
                            "Bienvenida en Lodge ecológico con jugos de frutas exóticas",
                            "Caminata nocturna para avistamiento de insectos y tarántulas"
                        )
                    )
                    2 -> Pair(
                        "Reserva y Delfines Rosados",
                        listOf(
                            "Búsqueda de delfines rosados y grises en el río",
                            "Visita al mariposario Pilpintuwai",
                            "Pesca artesanal de pirañas en la laguna",
                            "Demostración de danza nativa con comunidades indígenas"
                        )
                    )
                    else -> Pair(
                        "Ciudad de Iquitos y Barrio Belén",
                        listOf(
                            "Barrio Flotante de Belén (La Venecia Amazónica)",
                            "Paseo por el Pasaje Paquito y sus remedios naturales",
                            "Casa de Fierro diseñada por Gustave Eiffel"
                        )
                    )
                }
            }

            else -> {
                when (day) {
                    1 -> Pair(
                        "Exploración Histórica e Introducción",
                        listOf(
                            "Paseo por la Plaza Principal y centro histórico",
                            "Degustación de platillos emblemáticos locales",
                            "Visita al museo regional principal"
                        )
                    )
                    2 -> Pair(
                        "Aventura en la Naturaleza",
                        listOf(
                            "Excursión hacia miradores naturales y mirador panorámico",
                            "Trekking guiado con fotografía de paisaje",
                            "Atardecer en punto turístico destacado"
                        )
                    )
                    else -> Pair(
                        "Cultura Local y Compras",
                        listOf(
                            "Recorrido por mercados artesanales tradicionales",
                            "Visita a talleres de artesanos locales",
                            "Cena de despedida con música en vivo"
                        )
                    )
                }
            }
        }
    }
}
