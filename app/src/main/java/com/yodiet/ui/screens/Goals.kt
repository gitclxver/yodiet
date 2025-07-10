package com.yodiet.ui.screens

package com.yodiet.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yodiet.data.db.model.Goal
import com.yodiet.ui.vmodels.GoalVM

@Composable
fun GoalsScreen(goalVM: GoalVM = viewModel()) {
    var showAddEditDialog by remember { mutableStateOf(false) }
    var editingGoal by remember { mutableStateOf<Goal?>(null) }
    var showDeleteDialog by remember { mutableStateOf<Goal?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Goals",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Color.Black,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 32.dp, bottom = 16.dp)
            )
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(goalVM.goals) { goal ->
                    GoalCard(
                        goal = goal,
                        onEdit = { editingGoal = goal; showAddEditDialog = true },
                        onDelete = { showDeleteDialog = goal }
                    )
                }
            }
        }
        FloatingActionButton(
            onClick = { editingGoal = null; showAddEditDialog = true },
            containerColor = Color(0xFF1976D2),
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Goal")
        }
    }

    if (showAddEditDialog) {
        AddEditGoalDialog(
            initialGoal = editingGoal,
            onDismiss = { showAddEditDialog = false },
            onSave = { name, type, desc, current, target ->
                if (editingGoal == null) {
                    goalVM.addGoal(name, type, desc, current, target)
                } else {
                    goalVM.updateGoal(editingGoal!!.id, name, type, desc, current, target)
                }
                showAddEditDialog = false
            }
        )
    }
    if (showDeleteDialog != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Delete Goal") },
            text = { Text("Are you sure you want to delete this goal?") },
            confirmButton = {
                TextButton(onClick = {
                    goalVM.deleteGoal(showDeleteDialog!!.id)
                    showDeleteDialog = null
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun GoalCard(goal: Goal, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.Transparent),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1976D2)),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(goal.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(goal.description, color = Color.White, fontSize = 14.sp, maxLines = 1)
                Text(
                    text = "${goal.currentValue} / ${goal.targetValue} | ${goal.type}",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(end = 8.dp)) {
                CircularProgressIndicator(
                    progress = goal.progress,
                    modifier = Modifier.size(48.dp),
                    strokeWidth = 6.dp,
                    color = Color.White
                )
                Text(
                    text = "${(goal.progress * 100).toInt()}%",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
            }
        }
    }
}

@Composable
fun AddEditGoalDialog(
    initialGoal: Goal?,
    onDismiss: () -> Unit,
    onSave: (String, String, String, Float, Float) -> Unit
) {
    var name by remember { mutableStateOf(initialGoal?.name ?: "") }
    var type by remember { mutableStateOf(initialGoal?.type ?: "") }
    var desc by remember { mutableStateOf(initialGoal?.description ?: "") }
    var current by remember { mutableStateOf(initialGoal?.currentValue?.toString() ?: "0") }
    var target by remember { mutableStateOf(initialGoal?.targetValue?.toString() ?: "0") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialGoal == null) "Add Goal" else "Edit Goal") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Goal Name") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = type,
                    onValueChange = { type = it },
                    label = { Text("Goal Type") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Description") },
                    singleLine = false
                )
                OutlinedTextField(
                    value = current,
                    onValueChange = { current = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text("Current Value") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = target,
                    onValueChange = { target = it.filter { c -> c.isDigit() || c == '.' } },
                    label = { Text("Target Value") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSave(
                        name,
                        type,
                        desc,
                        current.toFloatOrNull() ?: 0f,
                        target.toFloatOrNull() ?: 0f
                    )
                },
                enabled = name.isNotBlank() && type.isNotBlank() && target.toFloatOrNull() != null
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}


