package com.yodiet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.yodiet.ui.components.TopNav
import com.yodiet.ui.vmodels.GoalViewModel
import com.yodiet.ui.vmodels.HealthVM
import com.yodiet.ui.vmodels.MealViewModel
import com.yodiet.nav.Routes

@Composable
fun HomeScreen(
    navController: NavController,
    healthVM: HealthVM = hiltViewModel(),
    mealVM: MealViewModel = hiltViewModel(),
    goalVM: GoalViewModel = hiltViewModel()
) {
    val healthData by healthVM.healthData.collectAsStateWithLifecycle(initialValue = com.yodiet.ui.vmodels.HealthUiState())
    val meals by mealVM.allMeals.collectAsStateWithLifecycle(initialValue = emptyList())
    val goals by goalVM.allGoals.collectAsStateWithLifecycle(initialValue = emptyList())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            TopNav(navController = navController)
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            SectionCard(
                title = "Health Goals",
                emptyText = "No health goals yet",
                onAddClick = { navController.navigate(Routes.GoalsScreen) },
                onCardClick = { navController.navigate(Routes.GoalsScreen) } // Added click handler
            ) {
                if (healthData.dailyGoals.isEmpty()) {
                    Text("No goals added", color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        healthData.dailyGoals.forEach { goal ->
                            HealthGoalCard(
                                goal = goal,
                                onProgressUpdate = { newValue ->
                                    healthVM.updateGoalProgress(goal.id, newValue)
                                }
                            )
                        }
                    }
                }
            }
        }

        item {
            SectionCard(
                title = "Meals",
                emptyText = "No meals logged",
                onAddClick = { navController.navigate(Routes.DietScreen) },
                onCardClick = { navController.navigate(Routes.DietScreen) } // Added click handler
            ) {
                if (meals.isEmpty()) {
                    Text("No meals today", color = MaterialTheme.colorScheme.onSurfaceVariant)
                } else {
                    Column {
                        meals.take(2).forEach { meal ->
                            Text("- ${meal.title}: ${meal.kcal} kcal",
                                color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }

        item {
            SectionCard(
                title = "Goals",
                emptyText = "No goals yet",
                onAddClick = { navController.navigate(Routes.GoalsScreen) },
                onCardClick = { navController.navigate(Routes.GoalsScreen) } // Added click handler
            ) {
                if (goals.isEmpty()) {
                    Text(
                        text = "No goals set yet",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        goals.take(2).forEach { goal ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = goal.name,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    if (goal.description.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = goal.description,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionCard(
    title: String,
    emptyText: String,
    onAddClick: () -> Unit,
    onCardClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onCardClick() }, // Make entire card clickable
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(
                    onClick = {
                        onAddClick()
                    },
                    modifier = Modifier.noRippleClickable { onAddClick() }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.primary)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            content()
        }
    }
}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick
    )
}
