package com.example.presentation.map

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.DestinationRepository
import com.example.ui.theme.IncaGold
import com.example.ui.theme.MidnightBlue
import com.example.ui.theme.MidnightGradient
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun MapScreen(
    destinationId: String,
    destinationRepository: DestinationRepository,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val destination = destinationRepository.getDestinationById(destinationId)
        ?: destinationRepository.getDestinationById("machu_picchu")!!

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                    .background(MidnightGradient)
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .background(SurfaceWhite.copy(alpha = 0.15f), CircleShape)
                            .size(36.dp)
                            .testTag("map_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                            tint = SurfaceWhite
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Mapa Interactivo RUTA PERÚ",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = IncaGold
                        )
                        Text(
                            text = destination.name,
                            fontSize = 12.sp,
                            color = SurfaceWhite.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MidnightBlue)
        ) {
            // Topological Vector Map Background Canvas
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("interactive_canvas_map")
            ) {
                val gridStep = 60.dp.toPx()
                val width = size.width
                val height = size.height

                // Grid Lines
                var x = 0f
                while (x < width) {
                    drawLine(
                        color = IncaGold.copy(alpha = 0.08f),
                        start = Offset(x, 0f),
                        end = Offset(x, height),
                        strokeWidth = 1f
                    )
                    x += gridStep
                }

                var y = 0f
                while (y < height) {
                    drawLine(
                        color = IncaGold.copy(alpha = 0.08f),
                        start = Offset(0f, y),
                        end = Offset(width, y),
                        strokeWidth = 1f
                    )
                    y += gridStep
                }

                // Radar Concentric Circles around center
                val centerX = width / 2f
                val centerY = height / 2.3f

                for (r in listOf(80.dp.toPx(), 160.dp.toPx(), 240.dp.toPx())) {
                    drawCircle(
                        color = IncaGold.copy(alpha = 0.12f),
                        radius = r,
                        center = Offset(centerX, centerY),
                        style = Stroke(width = 1.5f)
                    )
                }

                // Contour waves
                val path = Path().apply {
                    moveTo(0f, centerY + 100f)
                    quadraticTo(centerX - 100f, centerY - 200f, width, centerY + 50f)
                }
                drawPath(
                    path = path,
                    color = IncaGold.copy(alpha = 0.15f),
                    style = Stroke(width = 2f)
                )

                // Pulse Pin Center
                drawCircle(
                    color = IncaGold.copy(alpha = 0.25f),
                    radius = 36.dp.toPx(),
                    center = Offset(centerX, centerY)
                )
                drawCircle(
                    color = IncaGold,
                    radius = 12.dp.toPx(),
                    center = Offset(centerX, centerY)
                )
                drawCircle(
                    color = MidnightBlue,
                    radius = 6.dp.toPx(),
                    center = Offset(centerX, centerY)
                )
            }

            // Pin Label Badge
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 120.dp)
                    .background(SurfaceWhite, RoundedCornerShape(12.dp))
                    .border(1.dp, IncaGold, RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MidnightBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = destination.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MidnightBlue,
                        fontFamily = FontFamily.Serif
                    )
                }
            }

            // Floating Bottom Info Card overlay
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .testTag("map_info_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(IncaGold.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "COORDENADAS SATELITALES",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MidnightBlue
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = IncaGold, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(text = "${destination.rating}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = destination.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif,
                        color = MidnightBlue
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CompassCalibration, contentDescription = null, tint = MidnightBlue, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Lat: ${destination.latitude} | Lon: ${destination.longitude}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val uri = Uri.parse("geo:${destination.latitude},${destination.longitude}?q=${Uri.encode(destination.name)}")
                                val mapIntent = Intent(Intent.ACTION_VIEW, uri)
                                context.startActivity(mapIntent)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MidnightBlue)
                        ) {
                            Icon(Icons.Default.Explore, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "ABRIR GPS", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = onBackClick,
                            modifier = Modifier
                                .weight(1.2f)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MidnightBlue,
                                contentColor = IncaGold
                            )
                        ) {
                            Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "VOLVER AL DETALLE", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}
