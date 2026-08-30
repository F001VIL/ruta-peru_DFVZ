package com.example.presentation.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.IncaGold
import com.example.ui.theme.MidnightBlue
import com.example.ui.theme.SurfaceVariant
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun DetailScreen(
    destinationId: String,
    detailViewModel: DetailViewModel,
    onBackClick: () -> Unit,
    onOpenMap: (String) -> Unit,
    onAddToMyRoute: (String) -> Unit
) {
    LaunchedEffect(destinationId) {
        detailViewModel.loadDestination(destinationId)
    }

    val destination by detailViewModel.destination.collectAsState()
    val weatherState by detailViewModel.weatherState.collectAsState()
    val isLoadingWeather by detailViewModel.isLoadingWeather.collectAsState()
    val favoriteIds by detailViewModel.favoriteIds.collectAsState()
    val uriHandler = LocalUriHandler.current

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Descripción", "Historia", "Actividades", "Vuelos y Tours")

    if (destination == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = IncaGold)
        }
        return
    }

    val dest = destination!!
    val isFavorite = favoriteIds.contains(dest.id)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(rememberScrollState())
    ) {
        // Hero Image Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            val imageRes = when (dest.id) {
                "machu_picchu" -> R.drawable.img_machu_picchu
                "huacachina" -> R.drawable.img_huacachina
                else -> null
            }

            if (imageRes != null) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = dest.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                AsyncImage(
                    model = dest.imageUrl,
                    contentDescription = dest.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Dark gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.5f), Color.Transparent, Color.Black.copy(alpha = 0.8f))
                        )
                    )
            )

            // Top Actions Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .size(40.dp)
                        .testTag("back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = SurfaceWhite
                    )
                }

                IconButton(
                    onClick = { detailViewModel.toggleFavorite() },
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .size(40.dp)
                        .testTag("favorite_toggle_button")
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (isFavorite) MaterialTheme.colorScheme.error else SurfaceWhite
                    )
                }
            }

            // Bottom Title Overlay
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(IncaGold, RoundedCornerShape(6.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = dest.category,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MidnightBlue
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = IncaGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${dest.rating} / 5.0",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = SurfaceWhite
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = dest.name,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    color = SurfaceWhite
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = IncaGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Región ${dest.region}, Perú",
                        fontSize = 14.sp,
                        color = SurfaceWhite.copy(alpha = 0.9f)
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {

            // Weather Card (Real-time OpenWeatherMap Integration)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("weather_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MidnightBlue),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Thermostat,
                                contentDescription = null,
                                tint = IncaGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Clima en Tiempo Real",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = IncaGold,
                                fontFamily = FontFamily.Serif
                            )
                        }

                        if (isLoadingWeather) {
                            CircularProgressIndicator(color = IncaGold, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val temp = weatherState?.main?.temp ?: 18.5
                    val tempMin = weatherState?.main?.tempMin ?: 9.0
                    val tempMax = weatherState?.main?.tempMax ?: 22.0
                    val humidity = weatherState?.main?.humidity ?: 60
                    val description = weatherState?.weatherList?.firstOrNull()?.description ?: "Soleado despejado"

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (temp > 22) Icons.Default.WbSunny else Icons.Default.Cloud,
                                contentDescription = null,
                                tint = IncaGold,
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "${temp.toInt()}°C",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SurfaceWhite
                                )
                                Text(
                                    text = description.replaceFirstChar { it.uppercase() },
                                    fontSize = 12.sp,
                                    color = SurfaceWhite.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.WaterDrop, contentDescription = null, tint = IncaGold, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Humedad: $humidity%", fontSize = 12.sp, color = SurfaceWhite)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Mín: ${tempMin.toInt()}°C | Máx: ${tempMax.toInt()}°C",
                                fontSize = 12.sp,
                                color = SurfaceWhite.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quick Booking Banner Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("quick_booking_banner"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Air,
                            contentDescription = null,
                            tint = MidnightBlue,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Reserva de Vuelos y Tours",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            fontFamily = FontFamily.Serif,
                            color = MidnightBlue
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Encuentra boletos de avión y tours guiados completos en las plataformas oficial de viaje.",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                uriHandler.openUri("https://www.google.com/travel/flights?q=vuelos+a+${dest.region}")
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MidnightBlue)
                        ) {
                            Icon(Icons.Default.Air, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Comprar Vuelos", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                val queryName = dest.name.replace(" ", "+")
                                uriHandler.openUri("https://www.getyourguide.com/s?q=$queryName")
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MidnightBlue, contentColor = SurfaceWhite)
                        ) {
                            Icon(Icons.Default.ConfirmationNumber, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Tour Completo", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tab Row Sections
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = SurfaceWhite,
                contentColor = MidnightBlue,
                edgePadding = 8.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = IncaGold
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .testTag("detail_tab_row")
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTabIndex == index) MidnightBlue else TextSecondary,
                                fontSize = 13.sp
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tab Content Box
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    when (selectedTabIndex) {
                        0 -> {
                            Text(
                                text = "Acerca de este destino",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Serif,
                                color = MidnightBlue
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = dest.description,
                                fontSize = 14.sp,
                                color = TextPrimary,
                                lineHeight = 22.sp
                            )
                        }
                        1 -> {
                            Text(
                                text = "Legado Histórico y Ancestral",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Serif,
                                color = MidnightBlue
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = dest.history,
                                fontSize = 14.sp,
                                color = TextPrimary,
                                lineHeight = 22.sp
                            )
                        }
                        2 -> {
                            Text(
                                text = "Actividades Recomendadas",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                fontFamily = FontFamily.Serif,
                                color = MidnightBlue
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            dest.activities.forEach { activity ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = IncaGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = activity,
                                        fontSize = 14.sp,
                                        color = TextPrimary
                                    )
                                }
                            }
                        }
                        3 -> {
                            BookingPlatformsSection(
                                destinationName = dest.name,
                                region = dest.region
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { onOpenMap(dest.id) },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("btn_view_map"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MidnightBlue)
                ) {
                    Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "VER EN MAPA", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { onAddToMyRoute(dest.id) },
                    modifier = Modifier
                        .weight(1.2f)
                        .height(50.dp)
                        .testTag("btn_add_my_route"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = IncaGold,
                        contentColor = MidnightBlue
                    )
                ) {
                    Icon(Icons.Default.Route, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "AGREGAR A MI RUTA", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

data class BookingPlatform(
    val name: String,
    val subtitle: String,
    val tag: String,
    val url: String
)

@Composable
fun BookingPlatformsSection(
    destinationName: String,
    region: String
) {
    val uriHandler = LocalUriHandler.current

    val flightPlatforms = remember(region) {
        listOf(
            BookingPlatform(
                name = "LATAM Airlines",
                subtitle = "Aerolínea líder con vuelos a $region",
                tag = "Oficial",
                url = "https://www.latamairlines.com/pe/es"
            ),
            BookingPlatform(
                name = "SKY Airline",
                subtitle = "Vuelos directos económicos en Perú",
                tag = "Low Cost",
                url = "https://www.skyairline.com/peru"
            ),
            BookingPlatform(
                name = "Despegar Perú",
                subtitle = "Paquetes completos Vuelo + Hotel",
                tag = "Paquetes",
                url = "https://www.despegar.com.pe/vuelos/"
            ),
            BookingPlatform(
                name = "Google Flights",
                subtitle = "Comparador de precios de todas las aerolíneas",
                tag = "Comparador",
                url = "https://www.google.com/travel/flights?q=vuelos+a+$region"
            )
        )
    }

    val tourPlatforms = remember(destinationName) {
        val queryName = destinationName.replace(" ", "+")
        listOf(
            BookingPlatform(
                name = "Civitatis Perú",
                subtitle = "Tours completos guiados en español en $destinationName",
                tag = "Recomendado",
                url = "https://www.civitatis.com/es/peru/"
            ),
            BookingPlatform(
                name = "GetYourGuide",
                subtitle = "Entradas, excursiones y paquetes turísticos",
                tag = "Top Ventas",
                url = "https://www.getyourguide.com/s?q=$queryName"
            ),
            BookingPlatform(
                name = "Viator (TripAdvisor)",
                subtitle = "Experiencias y tours verificados por viajeros",
                tag = "Verificado",
                url = "https://www.viator.com/search/$queryName"
            ),
            BookingPlatform(
                name = "Booking.com Tours",
                subtitle = "Hospedaje y atracciones en la zona",
                tag = "Alojamiento",
                url = "https://www.booking.com/searchresults.html?ss=$queryName"
            )
        )
    }

    Column {
        Text(
            text = "Reserva de Vuelos",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            fontFamily = FontFamily.Serif,
            color = MidnightBlue
        )
        Text(
            text = "Compra boletos de avión en agencias y aerolíneas reconocidas para llegar a $region",
            fontSize = 12.sp,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(10.dp))

        flightPlatforms.forEach { platform ->
            PlatformItemCard(platform = platform, onOpenUrl = { uriHandler.openUri(platform.url) })
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Tours Completos y Paquetes",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            fontFamily = FontFamily.Serif,
            color = MidnightBlue
        )
        Text(
            text = "Paquetes turísticos, excursiones y entradas garantizadas para $destinationName",
            fontSize = 12.sp,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(10.dp))

        tourPlatforms.forEach { platform ->
            PlatformItemCard(platform = platform, onOpenUrl = { uriHandler.openUri(platform.url) })
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun PlatformItemCard(
    platform: BookingPlatform,
    onOpenUrl: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenUrl() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundLight),
        border = BorderStroke(1.dp, MidnightBlue.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = platform.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MidnightBlue
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(IncaGold.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = platform.tag,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MidnightBlue
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = platform.subtitle,
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.OpenInNew,
                contentDescription = "Ir a página de reserva",
                tint = MidnightBlue,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

