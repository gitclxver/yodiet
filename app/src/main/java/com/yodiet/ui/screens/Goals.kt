package com.yodiet.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yodiet.data.db.model.Goal
import com.yodiet.ui.components.TopNav
import com.yodiet.ui.vmodels.GoalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    navController: NavController,
    viewModel: GoalViewModel = hiltViewModel()
) {
    val goals by viewModel.allGoals.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    var showAddGoalDialog by remember { mutableStateOf(false) }
    var showEditGoalDialog by remember { mutableStateOf<Goal?>(null) }

    Scaffold(
        topBar = { TopNav(navController = navController) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddGoalDialog = true },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add new goal",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        if (goals.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No goals set yet.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(goals) { goal ->
                    GoalItem(
                        goal = goal,
                        onEditClick = { showEditGoalDialog = it },
                        onDeleteClick = { viewModel.deleteGoal(it.id) }
                    )
                }
            }
        }
    }

    if (showAddGoalDialog) {
        AddGoalDialog(
            onConfirm = { newGoal ->
                viewModel.addGoal(newGoal)
                showAddGoalDialog = false
            },
            onCancel = { showAddGoalDialog = false },
            currentUserId = currentUser?.id ?: 0L
        )
    }

    showEditGoalDialog?.let { goalToEdit ->
        EditGoalDialog(
            goal = goalToEdit,
            onConfirm = { updatedGoal ->
                viewModel.updateGoal(updatedGoal)
                showEditGoalDialog = null
            },
            onCancel = { showEditGoalDialog = null }
        )
    }
}

@Composable
fun GoalItem(
    goal: Goal,
    onEditClick: (Goal) -> Unit,
    onDeleteClick: (Goal) -> Unit
) {
    val animatedProgress by animateFloatAsState(
        targetValue = goal.progress,
        animationSpec = tween(durationMillis = 1000), label = "progressAnimation"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3F51B5))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = goal.name,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = goal.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${goal.currentValue.toInt()} / ${goal.targetValue.toInt()} ${goal.unit}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(horizontalAlignment = Alignment.End) {
                Row {
                    IconButton(onClick = { onEditClick(goal) }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit goal",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { onDeleteClick(goal) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete goal",
                            tint = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(60.dp)) {
                    Canvas(modifier = Modifier.size(60.dp)) {
                        drawCircle(
                            color = Color.White.copy(alpha = 0.3f),
                            radius = size.minDimension / 2,
                            style = Stroke(width = 8.dp.toPx())
                        )
                        drawArc(
                            color = Color.White,
                            startAngle = -90f,
                            sweepAngle = 360 * animatedProgress,
                            useCenter = false,
                            style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                    Text(
                        text = "${(animatedProgress * 100).toInt()}%",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGoalDialog(
    onConfirm: (Goal) -> Unit,
    onCancel: () -> Unit,
    currentUserId: Long
) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var targetValue by remember { mutableStateOf("") }
    var currentValue by remember { mutableStateOf("0") }
    var unit by remember { mutableStateOf("") }
    var formError by remember { mutableStateOf<String?>(null) }

    val isFormValid = name.isNotBlank() &&
            description.isNotBlank() &&
            targetValue.toFloatOrNull() != null &&
            currentValue.toFloatOrNull() != null &&
            unit.isNotBlank()

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Add New Goal") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                formError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Goal Name*") },
                    isError = name.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Goal Type") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description*") },
                    isError = description.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = currentValue,
                        onValueChange = {
                            if (it.matches(Regex("^\\d*\\.?\\d*\$"))) currentValue = it
                        },
                        label = { Text("Current*") },
                        isError = currentValue.toFloatOrNull() == null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = targetValue,
                        onValueChange = {
                            if (it.matches(Regex("^\\d*\\.?\\d*\$"))) targetValue = it
                        },
                        label = { Text("Target*") },
                        isError = targetValue.toFloatOrNull() == null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = unit,
                    onValueChange = { unit = it },
                    label = { Text("Unit*") },
                    isError = unit.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isFormValid) {
                        onConfirm(
                            Goal(
                                name = name,
                                type = type,
                                description = description,
                                targetValue = targetValue.toFloat(),
                                currentValue = currentValue.toFloat(),
                                unit = unit,
                                userId = currentUserId
                            )
                        )
                    } else {
                        formError = "Please fill all required fields (*)"
                    }
                },
                enabled = isFormValid
            ) {
                Text("Add Goal")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGoalDialog(
    goal: Goal,
    onConfirm: (Goal) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf(goal.name) }
    var type by remember { mutableStateOf(goal.type) }
    var description by remember { mutableStateOf(goal.description) }
    var targetValue by remember { mutableStateOf(goal.targetValue.toString()) }
    var currentValue by remember { mutableStateOf(goal.currentValue.toString()) }
    var unit by remember { mutableStateOf(goal.unit) }
    var formError by remember { mutableStateOf<String?>(null) }

    val isFormValid = name.isNotBlank() &&
            description.isNotBlank() &&
            targetValue.toFloatOrNull() != null &&
            currentValue.toFloatOrNull() != null &&
            unit.isNotBlank()

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Edit Goal") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                formError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Goal Name*") },
                    isError = name.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Goal Type") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description*") },
                    isError = description.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = currentValue,
                        onValueChange = {
                            if (it.matches(Regex("^\\d*\\.?\\d*\$"))) currentValue = it
                        },
                        label = { Text("Current*") },
                        isError = currentValue.toFloatOrNull() == null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = targetValue,
                        onValueChange = {
                            if (it.matches(Regex("^\\d*\\.?\\d*\$"))) targetValue = it
                        },
                        label = { Text("Target*") },
                        isError = targetValue.toFloatOrNull() == null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = unit,
                    onValueChange = { unit = it },
                    label = { Text("Unit*") },
                    isError = unit.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isFormValid) {
                        onConfirm(
                            goal.copy(
                                name = name,
                                type = type,
                                description = description,
                                targetValue = targetValue.toFloat(),
                                currentValue = currentValue.toFloat(),
                                unit = unit
                            )
                        )
                    } else {
                        formError = "Please fill all required fields (*)"
                    }
                },
                enabled = isFormValid
            ) {
                Text("Save Changes")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
        }
    )
}
