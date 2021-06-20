package com.linminphyo.meditation.swipe

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun CardSwipeScreen() {
    val squareSize = 100.dp
    val coroutineScope = rememberCoroutineScope()
    val offsetXAnimateVersion = remember { Animatable(0f) }
    val offsetYAnimateVersion = remember { Animatable(0f) }
    Box(modifier = Modifier.fillMaxSize()) {
        Card(
            backgroundColor = Color.White,
            modifier = Modifier
                .offset {
                    IntOffset(
                        offsetXAnimateVersion.value.roundToInt(),
                        offsetYAnimateVersion.value.roundToInt()
                    )
                }
                .size(squareSize)
                .align(Alignment.Center)
                .pointerInput("drag") {
                    detectDragGestures(onDragEnd = {
                        coroutineScope.async {
                            offsetXAnimateVersion.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(
                                    durationMillis = 300,
                                    delayMillis = 0,
                                    easing = FastOutLinearInEasing
                                )
                            )
                        }
                        coroutineScope.async {
                            offsetYAnimateVersion.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(
                                    durationMillis = 300,
                                    delayMillis = 0,
                                    easing = FastOutLinearInEasing
                                )
                            )
                        }
                    }) { change, dragAmount ->
                        change.consumeAllChanges()
                        coroutineScope.launch {
                            offsetXAnimateVersion.snapTo(offsetXAnimateVersion.value + dragAmount.x)
                            offsetYAnimateVersion.snapTo(offsetYAnimateVersion.value + dragAmount.y)
                        }
                    }
                }
        ) {

        }
    }
}

@ExperimentalMaterialApi
@Composable
fun CardSwipe2() {

    val squareSize = 300.dp

    val verticalSwipeableState = rememberSwipeableState(1)
    val horizontalSwipeableState = rememberSwipeableState(1)

    val rotationAngle =
        animateFloatAsState(targetValue = horizontalSwipeableState.offset.value / 50)
    val verticalOffsetOnHorizontalDrag = animateDpAsState(
        targetValue = (horizontalSwipeableState.offset.value.absoluteValue.dp)
            .coerceAtMost(10.dp)
    )
    val overlayColor = animateColorAsState(
        calculateColorOverlayFor(
            xSwipedState = horizontalSwipeableState,
            ySwipedState = verticalSwipeableState
        ), animationSpec = spring()
    )

    val textAlpha = animateFloatAsState(
        calculateScaleForText(
            xSwipedState = horizontalSwipeableState,
            ySwipedState = verticalSwipeableState
        ), animationSpec = spring()
    )

    val actionText = getActionText(
        xSwipedState = horizontalSwipeableState,
        ySwipedState = verticalSwipeableState
    )

    val actionIcon = getActionIcon(
        xSwipedState = horizontalSwipeableState,
        ySwipedState = verticalSwipeableState
    )


    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val verticalAnchors =
        mapOf(-sizePx * 2 to 0, 0f to 1, sizePx * 2 to 2) // Maps anchor points (in px) to states
    val horizontalAnchors =
        mapOf(-sizePx * 2 to 0, 0f to 1, sizePx * 2 to 2) // Maps anchor points (in px) to states
//    val offsetY = remember { androidx.compose.animation.core.Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Card(
            shape = RoundedCornerShape(16.dp),
            backgroundColor = Color.White,
            modifier = Modifier
                .offset {
                    IntOffset(
                        horizontalSwipeableState.offset.value.roundToInt(),
                        verticalSwipeableState.offset.value.roundToInt(),
                    )
                }
                .offset(y = verticalOffsetOnHorizontalDrag.value)
                .rotate(rotationAngle.value)
                .size(squareSize)
                .aspectRatio(0.69f, false)
                .align(Alignment.Center)
                .swipeable(
                    state = verticalSwipeableState,
                    anchors = verticalAnchors,
                    thresholds = { _, _ -> FractionalThreshold(0.5f) },
                    orientation = Orientation.Vertical
                )
                .swipeable(
                    state = horizontalSwipeableState,
                    anchors = horizontalAnchors,
                    thresholds = { _, _ -> FractionalThreshold(0.5f) },
                    orientation = Orientation.Horizontal
                )
        ) {
            Box {
                Image(
                    painter = rememberCoilPainter(request = "https://m.media-amazon.com/images/M/MV5BNTkwOTE1ZDYtODQ3Yy00YTYwLTg0YWQtYmVkNmFjNGZlYmRiXkEyXkFqcGdeQXVyNTc4MjczMTM@._V1_.jpg"),
                    contentDescription = "Photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .background(overlayColor.value)
                        .fillMaxSize()
                )

                Column(
                    modifier = Modifier
                        .alpha(textAlpha.value)
                        .padding(16.dp)
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                ) {
                    actionIcon?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = "Delete Icon",
                            modifier = Modifier
                                .size(48.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }

                    Text(
                        actionText,
                        style = MaterialTheme.typography.h4,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

        Button(onClick = {
            coroutineScope.launch {
                verticalSwipeableState.animateTo(1)
                horizontalSwipeableState.animateTo(1)
            }
        }, modifier = Modifier.align(Alignment.BottomCenter)) {
            Text(text = "Go to center")
        }
    }

}

@ExperimentalMaterialApi
@Composable
private fun calculateColorOverlayFor(
    xSwipedState: SwipeableState<Int>,
    ySwipedState: SwipeableState<Int>
): Color {
    return when (getSwipedDirection(xSwipedState = xSwipedState, ySwipedState = ySwipedState)) {
        SwipeDirection.Left -> {
            val alpha = if (xSwipedState.progress.fraction == 1f) {
                0f
            } else {
                xSwipedState.progress.fraction.coerceAtMost(0.5f)
            }
            Color.Blue.copy(alpha = alpha)
        }

        SwipeDirection.Right -> {
            val alpha = if (xSwipedState.progress.fraction == 1f) {
                0f
            } else {
                xSwipedState.progress.fraction.coerceAtMost(0.5f)
            }
            Color.Green.copy(alpha = alpha)
        }

        SwipeDirection.Top -> {
            val alpha = if (ySwipedState.progress.fraction == 1f) {
                0f
            } else {
                ySwipedState.progress.fraction.coerceAtMost(0.5f)
            }
            Color.Yellow.copy(alpha = alpha)
        }

        SwipeDirection.Bottom -> {
            val alpha = if (ySwipedState.progress.fraction == 1f) {
                0f
            } else {
                ySwipedState.progress.fraction.coerceAtMost(0.5f)
            }
            Color.Red.copy(alpha = alpha)
        }
        else -> Color.Transparent
    }

}


@ExperimentalMaterialApi
@Composable
private fun calculateScaleForText(
    xSwipedState: SwipeableState<Int>,
    ySwipedState: SwipeableState<Int>
): Float {
    return when (getSwipedDirection(xSwipedState, ySwipedState)) {
        SwipeDirection.Left -> {
            if (xSwipedState.progress.fraction == 1f) {
                0f
            } else {
                (xSwipedState.progress.fraction * 10).coerceAtMost(1f)
            }
        }

        SwipeDirection.Right -> {
            if (xSwipedState.progress.fraction == 1f) {
                0f
            } else {
                (xSwipedState.progress.fraction * 10).coerceAtMost(1f)
            }

        }

        SwipeDirection.Top -> {
            if (ySwipedState.progress.fraction == 1f) {
                0f
            } else {
                (ySwipedState.progress.fraction * 10).coerceAtMost(1f)
            }
        }

        SwipeDirection.Bottom -> {
            if (ySwipedState.progress.fraction == 1f) {
                0f
            } else {
                (ySwipedState.progress.fraction * 10).coerceAtMost(1f)
            }
        }
        else -> 0f
    }
}


@ExperimentalMaterialApi
@Composable
fun getActionText(
    xSwipedState: SwipeableState<Int>,
    ySwipedState: SwipeableState<Int>
): String {
    return when (getSwipedDirection(xSwipedState, ySwipedState)) {
        SwipeDirection.Left -> "May be Later"
        SwipeDirection.Right -> "Watchlist"
        SwipeDirection.Top -> "Seen"
        SwipeDirection.Bottom -> "Never"
        else -> ""
    }
}


@ExperimentalMaterialApi
@Composable
private fun getActionIcon(
    xSwipedState: SwipeableState<Int>,
    ySwipedState: SwipeableState<Int>
): ImageVector? {
    return when (getSwipedDirection(xSwipedState, ySwipedState)) {
        SwipeDirection.Left -> Icons.Outlined.List
        SwipeDirection.Right -> Icons.Outlined.MailOutline
        SwipeDirection.Top -> Icons.Outlined.Send
        SwipeDirection.Bottom -> Icons.Default.Delete
        else -> null
    }
}


@ExperimentalMaterialApi
@Composable
private fun getSwipedDirection(
    xSwipedState: SwipeableState<Int>,
    ySwipedState: SwipeableState<Int>
): SwipeDirection {
    return when {
        xSwipedState.direction < 0f -> SwipeDirection.Left
        xSwipedState.direction > 0f -> SwipeDirection.Right
        ySwipedState.direction < 0f -> SwipeDirection.Top
        ySwipedState.direction > 0f -> SwipeDirection.Bottom
        else -> SwipeDirection.None
    }
}

private enum class SwipeDirection {
    Left, Top, Right, Bottom, None
}

