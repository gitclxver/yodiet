//package com.yodiet.ui.screens
//
//import android.graphics.drawable.Drawable
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.annotation.DrawableRes
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.interaction.collectIsPressedAsState
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.graphicsLayer
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.yodiet.data.db.model.Meal
//
//
//@Composable
//@OptIn(ExperimentalMaterial3Api::class)
//fun DietScreen() {
//    var showAddDialog by remember { mutableStateOf(false) }
//    var showEditDialog by remember { mutableStateOf(false) }
//    var selectedMealIndex by remember { mutableIntStateOf(-1) }
//    val image = "Null"
//    val mealList = remember {
//        mutableStateListOf(
//            Meal(0,"Breakfast", image, 300, 25, 200, 100),
//            Meal(1,"Lunch", image, 700, 10, 800, 300)
//        )
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Meal Plan") },
//                navigationIcon = {
//                    Icon(Icons.Default.Person, contentDescription = "Profile", modifier = Modifier.padding(12.dp))
//                },
//                actions = {
//                    Icon(Icons.Default.Menu, contentDescription = "Menu", modifier = Modifier.padding(12.dp))
//                },
//                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
//            )
//        },
//        floatingActionButton = {
//            FloatingActionButton(onClick = { showAddDialog = true }) {
//                Icon(Icons.Default.Add, contentDescription = "Add")
//            }
//        },
//        bottomBar = {
//            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
//                TextButton(onClick = {}) {
//                    Text("", color = Color.Gray)
//                }
//            }
//        }
//    ) { innerPadding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding)
//                .background(
//                    Brush.verticalGradient(colors = listOf(Color.White, Color(0xFFFFF3E0)))
//                )
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text("Diet", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(8.dp))
//
//            mealList.forEachIndexed { index, meal ->
//                Spacer(modifier = Modifier.height(8.dp))
//                MealCard(
//                    title = meal.title,
//                    imageRes = meal.imageRes,
//                    kcal = meal.kcal,
//                    fat = meal.fat,
//                    carbs = meal.carbs,
//                    protein = meal.protein,
//                    onEdit = {
//                        selectedMealIndex = index
//                        showEditDialog = true
//                    },
//                    onDelete = { mealList.removeAt(index) }
//                )
//            }
//        }
//    }
//
//    if (showAddDialog) {
//        AddOrEditMealDialog(
//            initialMeal = null,
//            onConfirm = {
//                mealList.add(it)
//                showAddDialog = false
//            },
//            onCancel = { showAddDialog = false }
//        )
//    }
//
//    if (showEditDialog && selectedMealIndex in mealList.indices) {
//        AddOrEditMealDialog(
//            initialMeal = mealList[selectedMealIndex],
//            onConfirm = {
//                mealList[selectedMealIndex] = it
//                showEditDialog = false
//            },
//            onCancel = { showEditDialog = false }
//        )
//    }
//}
//
//@Composable
//fun MealCard(
//    title: String,
//    @DrawableRes imageRes: Int,
//    kcal: Int,
//    fat: Int,
//    carbs: Int,
//    protein: Int,
//    onEdit: () -> Unit,
//    onDelete: () -> Unit
//) {
//    val interactionSource = remember { MutableInteractionSource() }
//    val isPressed by interactionSource.collectIsPressedAsState()
//
//
//    val scale by animateFloatAsState(
//        targetValue = if (isPressed) 0.97f else 1f,
//        label = "CardScale"
//    )
//
//    Card(
//        modifier = Modifier
//            .graphicsLayer {
//                scaleX = scale
//                scaleY = scale
//            }
//            .fillMaxWidth()
//            .wrapContentHeight()
//            .clickable(interactionSource = interactionSource, indication = null) { },
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(containerColor = Color(0xFF27649C)),
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Image(
//                painter = painterResource(id = imageRes),
//                contentDescription = title,
//                modifier = Modifier
//                    .size(120.dp)
//                    .clip(CircleShape),
//                contentScale = ContentScale.Crop
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 4.dp),
//                horizontalArrangement = Arrangement.End
//            ) {
//                IconButton(onClick = onEdit) {
//                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White)
//                }
//                IconButton(onClick = onDelete) {
//                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
//                }
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                NutritionInfo("$kcal", "k/cal")
//                NutritionInfo("${fat}g", "Fat")
//                NutritionInfo("${carbs}g", "Carbs")
//                NutritionInfo("${protein}g", "Protein")
//            }
//        }
//    }
//}
//
//
//
//@Composable
//fun NutritionInfo(value: String, label: String) {
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Text(value, color = Color.White, fontWeight = FontWeight.Bold)
//        Text(label, color = Color.White, fontSize = 12.sp)
//    }
//}
//
//@Composable
//fun AddOrEditMealDialog(
//    initialMeal: Meal?,
//    onConfirm: (Meal) -> Unit,
//    onCancel: () -> Unit
//) {
//    var title by remember { mutableStateOf(initialMeal?.title ?: "") }
//    var kcal by remember { mutableStateOf(initialMeal?.kcal?.toString() ?: "") }
//    var fat by remember { mutableStateOf(initialMeal?.fat?.toString() ?: "") }
//    var carbs by remember { mutableStateOf(initialMeal?.carbs?.toString() ?: "") }
//    var protein by remember { mutableStateOf(initialMeal?.protein?.toString() ?: "") }
//
//    AlertDialog(
//        onDismissRequest = onCancel,
//        title = { Text(if (initialMeal == null) "Add New Meal" else "Edit Meal") },
//        text = {
//            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Meal Title") })
//                OutlinedTextField(value = kcal, onValueChange = { kcal = it }, label = { Text("kcal") })
//                OutlinedTextField(value = fat, onValueChange = { fat = it }, label = { Text("Fat (g)") })
//                OutlinedTextField(value = carbs, onValueChange = { carbs = it }, label = { Text("Carbs (g)") })
//                OutlinedTextField(value = protein, onValueChange = { protein = it }, label = { Text("Protein (g)") })
//            }
//        },
//        confirmButton = {
//            Button(onClick = {
//                onConfirm(
//                    Meal(
//                        title = title,
//                        imageRes = initialMeal?.imageRes ?: R.drawable.breakfast,
//                        kcal = kcal.toIntOrNull() ?: 0,
//                        fat = fat.toIntOrNull() ?: 0,
//                        carbs = carbs.toIntOrNull() ?: 0,
//                        protein = protein.toIntOrNull() ?: 0
//                    )
//                )
//            }) {
//                Text("Save")
//            }
//        },
//        dismissButton = {
//            TextButton(onClick = onCancel) {
//                Text("Cancel")
//            }
//        }
//    )
//}