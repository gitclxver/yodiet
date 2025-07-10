package com.yodiet.ui.screens

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yodiet.ui.components.TopNav

@Composable
fun HomeScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
    ) {

        TopNav(navController = navController)

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Home",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        HomeCard(
            title = "Weight",
            value = "80",
            details = "KG    25%    23.9\n      Fat      BMI",
            progress = 0.3f
        )
        HomeCard(
            title = "Nutrition",
            value = "1200",
            details = "600g   6000\nkcal    Carb    Water",
            progress = 0.65f
        )
        HomeCard(
            title = "Fitness",
            value = "800",
            details = "8.5\nkcal    KM",
            progress = 0.8f
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Chart Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(horizontal = 24.dp)
                .background(Color(0xFF1976D2), shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("Chart goes here", color = Color.White)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun HomeCard(title: String, value: String, details: String, progress: Float) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 24.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1976D2))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .height(80.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = details,
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
            Box(contentAlignment = Alignment.Center) {

                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(48.dp),
                    strokeWidth = 6.dp,
                    color = Color.White
                )
                Text(
                    text = "${(progress * 100).toInt()}%",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ProfileScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Profile Screen Content")
        }
    }
}
