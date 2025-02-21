package com.example.jeudushifumi


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jeudushifumi.Events.ShakeEvent
import com.example.jeudushifumi.ViewModel.GameViewModel
import com.example.jeudushifumi.model.GameResult
import com.example.jeudushifumi.utils.getChoiceImage


import com.example.jeudushifumi.ui.theme.JEUDUSHIFUMITheme

class MainActivity : ComponentActivity() {
    private lateinit var shakeEvent: ShakeEvent
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        setContent {
            JEUDUSHIFUMITheme {
                AppNavigation()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (::shakeEvent.isInitialized) {
            sensorManager.registerListener(shakeEvent, accelerometer, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(shakeEvent)
    }

}

@Composable
fun HomeScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.main_background),
            contentDescription = "Background dragon ball",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Es-tu pret à faire un ShiFouMi dans l'univers de Dragon Ball?",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                color = Color.White,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { navController.navigate("playScreen") } ,
                        modifier = Modifier
                            .height(70.dp)
                            .width(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF6200EA), Color(0xFFBB86FC))
                                )
                            ),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 4.dp
                )
            ) {
                Text(text = "Jouer contre la machine")
            }
        }
    }
}
@Composable
fun PlayScreen(viewModel: GameViewModel, navController: NavController) {
    ShakeListener(viewModel)

    val gameState = viewModel.gameState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.play_background),
            contentDescription = "Background PlayScreen",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Button(onClick = { navController.navigate("home") } ,

                modifier = Modifier
                    .height(50.dp)
                    .width(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            ){
                Text(
                    text = "retour menu",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = when (gameState.value.result) {
                    GameResult.WIN -> "Tu as gagné !"
                    GameResult.LOSE -> "Tu as perdu !"
                    GameResult.DRAW -> "Égalité !"
                    else -> "Secoue pour jouer"
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White
            )

            Text(
                text = "Score Joueur : ${gameState.value.playerScore} | Score Ordinateur : ${gameState.value.computerScore}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Yellow,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Joueur",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    if (gameState.value.playerChoice == null) {
                        Image(
                            painter = painterResource(id = R.drawable.freezer),
                            contentDescription = "Trunks",
                            modifier = Modifier.size(200.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = getChoiceImage(gameState.value.playerChoice!!)),
                            contentDescription = "Choix de l'ordinateur",
                            modifier = Modifier.size(200.dp)
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Machine",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    if (gameState.value.computerChoice == null) {
                        Image(
                            painter = painterResource(id = R.drawable.trunks),
                            contentDescription = "Trunks",
                            modifier = Modifier.size(200.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = getChoiceImage(gameState.value.computerChoice!!)),
                            contentDescription = "Choix de l'ordinateur",
                            modifier = Modifier.size(200.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}




@Composable
fun AppNavigation() {
    val viewModel: GameViewModel = viewModel()

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("playScreen") {
            PlayScreen(viewModel, navController)
        }
    }
}

@Composable
fun ShakeListener(viewModel: GameViewModel) {
    val sensorManager = LocalContext.current.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    val shakeEvent = ShakeEvent {
        viewModel.onShakeDetected()
    }

    DisposableEffect(Unit) {
        sensorManager.registerListener(shakeEvent, accelerometer, SensorManager.SENSOR_DELAY_UI)
        onDispose {
            sensorManager.unregisterListener(shakeEvent)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JEUDUSHIFUMITheme {

    }
}