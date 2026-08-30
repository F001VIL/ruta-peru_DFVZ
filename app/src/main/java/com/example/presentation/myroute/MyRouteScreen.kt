package com.example.presentation.myroute

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Itinerary
import com.example.ui.theme.BackgroundLight
import com.example.ui.theme.IncaGold
import com.example.ui.theme.MidnightBlue
import com.example.ui.theme.MidnightGradient
import com.example.ui.theme.SurfaceBorder
import com.example.ui.theme.SurfaceVariant
import com.example.ui.theme.SurfaceWhite
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun MyRouteScreen(
    myRouteViewModel: MyRouteViewModel
) {
    val currentStep by myRouteViewModel.currentStep.collectAsState()
    val selectedDestinationName by myRouteViewModel.selectedDestinationName.collectAsState()
    val daysCount by myRouteViewModel.daysCount.collectAsState()
    val selectedBudget by myRouteViewModel.selectedBudget.collectAsState()
    val selectedInterests by myRouteViewModel.selectedInterests.collectAsState()
    val generatedItinerary by myRouteViewModel.generatedItinerary.collectAsState()

    Scaffold(
        containerColor = BackgroundLight,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                    .background(MidnightGradient)
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column {
                    Text(
                        text = "Mi Ruta - Planificador Inteligente",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = IncaGold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Itinerarios personalizados por reglas de descubrimiento",
                        fontSize = 12.sp,
                        color = SurfaceWhite.copy(alpha = 0.8f)
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (generatedItinerary == null) {
                // Step Progress Indicator
                StepProgressHeader(currentStep = currentStep)

                Spacer(modifier = Modifier.height(20.dp))

                // Card Container for Steps
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        when (currentStep) {
                            1 -> Step1DestinationSelection(
                                selectedName = selectedDestinationName,
                                destinations = myRouteViewModel.allDestinations.map { it.name },
                                onSelectName = { myRouteViewModel.setSelectedDestination(it) }
                            )
                            2 -> Step2DaysAndBudget(
                                daysCount = daysCount,
                                selectedBudget = selectedBudget,
                                budgetOptions = myRouteViewModel.budgetOptions,
                                onDaysChange = { myRouteViewModel.setDaysCount(it) },
                                onBudgetSelect = { myRouteViewModel.setBudget(it) }
                            )
                            3 -> Step3InterestsSelection(
                                availableInterests = myRouteViewModel.availableInterests,
                                selectedInterests = selectedInterests,
                                onToggleInterest = { myRouteViewModel.toggleInterest(it) }
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Navigation Actions
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (currentStep > 1) {
                                OutlinedButton(
                                    onClick = { myRouteViewModel.previousStep() },
                                    modifier = Modifier.testTag("btn_step_prev"),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Anterior")
                                }
                            } else {
                                Spacer(modifier = Modifier.width(1.dp))
                            }

                            if (currentStep < 3) {
                                Button(
                                    onClick = { myRouteViewModel.nextStep() },
                                    modifier = Modifier.testTag("btn_step_next"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MidnightBlue)
                                ) {
                                    Text("Siguiente")
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                            } else {
                                Button(
                                    onClick = { myRouteViewModel.generateItinerary() },
                                    modifier = Modifier.testTag("btn_generate_route"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = IncaGold,
                                        contentColor = MidnightBlue
                                    )
                                ) {
                                    Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("GENERAR RUTA", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            } else {
                // Generated Itinerary Display View
                GeneratedItineraryResultView(
                    itinerary = generatedItinerary!!,
                    onReset = { myRouteViewModel.resetPlanner() }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun StepProgressHeader(currentStep: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StepCircle(stepNumber = 1, title = "Destino", isActive = currentStep >= 1, isCurrent = currentStep == 1)
        Box(modifier = Modifier.width(40.dp).height(2.dp).background(if (currentStep >= 2) IncaGold else TextSecondary.copy(alpha = 0.3f)))
        StepCircle(stepNumber = 2, title = "Días y Presupuesto", isActive = currentStep >= 2, isCurrent = currentStep == 2)
        Box(modifier = Modifier.width(40.dp).height(2.dp).background(if (currentStep >= 3) IncaGold else TextSecondary.copy(alpha = 0.3f)))
        StepCircle(stepNumber = 3, title = "Intereses", isActive = currentStep >= 3, isCurrent = currentStep == 3)
    }
}

@Composable
fun StepCircle(stepNumber: Int, title: String, isActive: Boolean, isCurrent: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isCurrent -> IncaGold
                        isActive -> MidnightBlue
                        else -> SurfaceVariant
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isActive && !isCurrent) {
                Icon(Icons.Default.Check, contentDescription = null, tint = SurfaceWhite, modifier = Modifier.size(18.dp))
            } else {
                Text(
                    text = "$stepNumber",
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrent) MidnightBlue else TextSecondary,
                    fontSize = 14.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
            color = if (isActive) MidnightBlue else TextSecondary
        )
    }
}

@Composable
fun Step1DestinationSelection(
    selectedName: String,
    destinations: List<String>,
    onSelectName: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(
            text = "Paso 1: Selecciona tu Destino",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = MidnightBlue
        )
        Text(
            text = "Selecciona uno de los 10 destinos turísticos clave del Perú",
            fontSize = 12.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(20.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedName,
                onValueChange = {},
                readOnly = true,
                label = { Text("Destino Seleccionado") },
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(Icons.Default.ExpandMore, contentDescription = "Desplegar")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
                    .testTag("dropdown_destination"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = IncaGold,
                    unfocusedBorderColor = TextSecondary.copy(alpha = 0.5f)
                )
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(0.85f)
            ) {
                destinations.forEach { destName ->
                    DropdownMenuItem(
                        text = { Text(destName, fontWeight = if (destName == selectedName) FontWeight.Bold else FontWeight.Normal) },
                        onClick = {
                            onSelectName(destName)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Step2DaysAndBudget(
    daysCount: Int,
    selectedBudget: String,
    budgetOptions: List<String>,
    onDaysChange: (Int) -> Unit,
    onBudgetSelect: (String) -> Unit
) {
    Column {
        Text(
            text = "Paso 2: Duración y Presupuesto",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = MidnightBlue
        )
        Text(
            text = "Define cuántos días durará tu viaje y tu presupuesto aproximado",
            fontSize = 12.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Número de Días: $daysCount días",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = MidnightBlue
        )

        Slider(
            value = daysCount.toFloat(),
            onValueChange = { onDaysChange(it.toInt()) },
            valueRange = 1f..7f,
            steps = 5,
            colors = SliderDefaults.colors(
                thumbColor = IncaGold,
                activeTrackColor = MidnightBlue
            ),
            modifier = Modifier.testTag("slider_days")
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Presupuesto Aproximado:",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = MidnightBlue
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            budgetOptions.forEach { option ->
                val isSelected = selectedBudget == option
                FilterChip(
                    selected = isSelected,
                    onClick = { onBudgetSelect(option) },
                    label = { Text(option, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = IncaGold,
                        selectedLabelColor = MidnightBlue,
                        containerColor = SurfaceVariant
                    ),
                    modifier = Modifier.weight(1f).testTag("chip_budget_$option")
                )
            }
        }
    }
}

@Composable
fun Step3InterestsSelection(
    availableInterests: List<String>,
    selectedInterests: Set<String>,
    onToggleInterest: (String) -> Unit
) {
    Column {
        Text(
            text = "Paso 3: Selecciona tus Intereses",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = MidnightBlue
        )
        Text(
            text = "Elegiremos las mejores actividades según tus gustos de viaje",
            fontSize = 12.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                availableInterests.take(3).forEach { interest ->
                    val isSelected = selectedInterests.contains(interest)
                    FilterChip(
                        selected = isSelected,
                        onClick = { onToggleInterest(interest) },
                        label = { Text(interest, fontSize = 13.sp) },
                        leadingIcon = {
                            if (isSelected) Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = IncaGold,
                            selectedLabelColor = MidnightBlue,
                            containerColor = SurfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("chip_interest_$interest")
                    )
                }
            }

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                availableInterests.drop(3).forEach { interest ->
                    val isSelected = selectedInterests.contains(interest)
                    FilterChip(
                        selected = isSelected,
                        onClick = { onToggleInterest(interest) },
                        label = { Text(interest, fontSize = 13.sp) },
                        leadingIcon = {
                            if (isSelected) Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = IncaGold,
                            selectedLabelColor = MidnightBlue,
                            containerColor = SurfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("chip_interest_$interest")
                    )
                }
            }
        }
    }
}

@Composable
fun GeneratedItineraryResultView(
    itinerary: Itinerary,
    onReset: () -> Unit
) {
    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("itinerary_result_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MidnightBlue),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(IncaGold, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "ITINERARIO PERSONALIZADO",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MidnightBlue
                        )
                    }

                    IconButton(onClick = onReset, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reiniciar", tint = IncaGold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = itinerary.destinationName,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    color = SurfaceWhite
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = IncaGold, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${itinerary.daysCount} Días", fontSize = 13.sp, color = SurfaceWhite.copy(alpha = 0.9f))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "•  Presupuesto: ${itinerary.budget}", fontSize = 13.sp, color = SurfaceWhite.copy(alpha = 0.9f))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Plan Detallado Día por Día",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = MidnightBlue
        )

        Spacer(modifier = Modifier.height(12.dp))

        itinerary.days.forEach { dayPlan ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(IncaGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "D${dayPlan.dayNumber}",
                                fontWeight = FontWeight.Bold,
                                color = MidnightBlue,
                                fontSize = 13.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = dayPlan.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif,
                            color = MidnightBlue
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    dayPlan.activities.forEach { act ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = IncaGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = act, fontSize = 13.sp, color = TextPrimary)
                        }
                    }
                }
            }
        }
    }
}
