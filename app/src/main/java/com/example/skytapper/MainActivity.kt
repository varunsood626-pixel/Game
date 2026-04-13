package com.example.skytapper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SkyTapperGame()
                }
            }
        }
    }
}

@Composable
fun SkyTapperGame() {
    var score by remember { mutableIntStateOf(0) }
    var misses by remember { mutableIntStateOf(0) }
    var bestScore by remember { mutableIntStateOf(0) }
    var gameRunning by remember { mutableStateOf(true) }

    val laneCount = 3
    var targetLane by remember { mutableIntStateOf(Random.nextInt(laneCount)) }
    val targetY = remember { Animatable(0f) }

    LaunchedEffect(gameRunning, score) {
        if (!gameRunning) return@LaunchedEffect

        targetY.snapTo(0f)
        val speed = (2200 - score * 35).coerceAtLeast(800)
        targetY.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = speed, easing = LinearEasing)
        )

        misses += 1
        if (misses >= 3) {
            gameRunning = false
            bestScore = maxOf(bestScore, score)
        } else {
            targetLane = Random.nextInt(laneCount)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B1026))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sky Tapper",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatCard("Score", score)
            StatCard("Best", bestScore)
            StatCard("Lives", 3 - misses)
        }

        Box(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .weight(1f)
                .border(2.dp, Color(0xFF475569), RoundedCornerShape(18.dp))
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(laneCount) { lane ->
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .padding(6.dp)
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(14.dp))
                            .clickable(enabled = gameRunning) {
                                if (lane == targetLane) {
                                    score += 1
                                    targetLane = Random.nextInt(laneCount)
                                } else {
                                    misses += 1
                                    if (misses >= 3) {
                                        gameRunning = false
                                        bestScore = maxOf(bestScore, score)
                                    }
                                }
                            }
                    )
                }
            }

            if (gameRunning) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                        .align(Alignment.TopStart)
                        .offsetForLane(laneCount, targetLane)
                        .background(Color(0xFF38BDF8), RoundedCornerShape(12.dp))
                        .align(Alignment.TopStart)
                        .verticalProgress(targetY.value)
                )
            } else {
                GameOverCard(score = score, bestScore = bestScore) {
                    score = 0
                    misses = 0
                    gameRunning = true
                    targetLane = Random.nextInt(laneCount)
                }
            }
        }

        Text(
            text = "Tap the lane with the falling comet. 3 misses ends the run.",
            modifier = Modifier.padding(top = 12.dp),
            color = Color(0xFFCBD5E1)
        )
    }
}

private fun Modifier.offsetForLane(lanes: Int, lane: Int): Modifier {
    val laneWidth = 1f / lanes
    return this.fillMaxWidth(laneWidth).padding(start = (laneWidth * lane * 100).dp)
}

private fun Modifier.verticalProgress(progress: Float): Modifier {
    return this.padding(top = (progress * 420).dp)
}

@Composable
private fun StatCard(label: String, value: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color(0xFF1E293B), RoundedCornerShape(10.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(text = label, color = Color(0xFF94A3B8), fontSize = 12.sp)
        Text(text = value.toString(), color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun GameOverCard(score: Int, bestScore: Int, onRestart: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .background(Color(0xCC020617), RoundedCornerShape(16.dp))
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Game Over", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text(text = "Score: $score", color = Color(0xFFCBD5E1), modifier = Modifier.padding(top = 8.dp))
            Text(text = "Best: $bestScore", color = Color(0xFFCBD5E1), modifier = Modifier.padding(top = 4.dp))
            Button(onClick = {
                onRestart()
            }, modifier = Modifier.padding(top = 12.dp)) {
                Text("Play Again")
            }
        }
    }
}
