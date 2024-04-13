package cat.dam.ivan.menuLayoutCompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import cat.dam.ivan.menuLayoutCompose.ui.theme.Views2Theme

/**
 * Main activity of the application.
 * This is the entry point of your application.
 */
class MainActivity : ComponentActivity() {
    /**
     * Called when the activity is starting.
     * This is where most initialization should go.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Create a NavController to manage app navigation
            val navController = rememberNavController()
            // Create a state variable to hold the current route
            val currentRoute = remember { mutableStateOf("Layout 1") }
            // Create a NavGraph for the NavController
            val navGraph = remember(navController) {
                navController.createGraph(startDestination = currentRoute.value) {
                    // Add your layouts here if necessary
                    composable("Layout 1") { Layout1() }
                    composable("Layout 2") { Layout2() }
                    composable("Layout 3") { Layout3() }
                    composable("Layout 4") { Layout4() }
                }
            }
            Views2Theme {
                Scaffold(
                    // Set the top bar to be a MenuButton
                    topBar = { MenuButton(navController, navGraph, currentRoute) }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        // Set the NavHost to manage the navigation within the Box
                        NavHost(navController, navGraph)
                    }
                }
            }
        }
    }
}

/**
 * MenuButton is a composable function that creates a horizontal scrollable row of buttons.
 * Each button represents a route in the navigation graph.
 */
@Composable
fun MenuButton(
    navController: NavHostController,
    navGraph: NavGraph,
    currentRoute: MutableState<String>
) {
    LazyRow(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .padding(4.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.secondary),
        userScrollEnabled = true,
    )
    {
        item {
            Spacer(modifier = Modifier.width(4.dp))
        }

        for (i in 0 until navGraph.nodes.size()) {
            val route = navGraph.nodes.valueAt(i).route.toString()

            item {
                // Create a button for each route in the navigation graph
                StylizedButton(navController, route, currentRoute)
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}

/**
 * StylizedButton is a composable function that creates a button with a specific style.
 * When the button is clicked, it navigates to the corresponding route and updates the current route state.
 */
@Composable
fun StylizedButton(
    navController: NavHostController,
    route: String,
    currentRoute: MutableState<String>
) {
    val haptic = LocalHapticFeedback.current

    Button(
        onClick = {
            // Remove the current layout from the back stack
            navController.popBackStack()
            // Navigate to the selected layout
            navController.navigate(route)
            // Perform haptic feedback(vibration)
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            // Update the current route
            currentRoute.value = route
        },
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 15.dp,
        ),
        enabled = currentRoute.value != route,
    ) {
        Text(
            text = route,
            fontSize = 15.sp
        )
    }
}