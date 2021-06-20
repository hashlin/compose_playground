package com.linminphyo.meditation.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.properties.PropertyDelegateProvider
import kotlin.reflect.KProperty

@Composable
fun CategoryRow() {
    val (selected, onSelect) = remember { mutableStateOf(0) }
    val categories = listOf("All", "My", "Anxious", "Sleep", "Walk")

    LazyRow {
        itemsIndexed(categories) { index, item ->
            Category(item, index == selected, onClick = { onSelect(index) })
        }
    }
}

@Composable
fun TabSampe() {

}

@Composable
fun Category(name: String, selected: Boolean, onClick: () -> Unit) {
    val bgColor: Color by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colors.primary else Color(0xFF586894)
    )
    val boxSize by animateDpAsState(targetValue = if (selected) 140.dp else 65.dp)

    val topOffset by animateDpAsState(targetValue = if (selected) 0.dp else 0.dp)

    val coroutineScope = rememberCoroutineScope()
    val offsetY  = remember { androidx.compose.animation.core.Animatable(0f) }

    val cornerRadius by animateDpAsState(targetValue = if (selected) 70.dp else 15.dp)

    Column(modifier = Modifier
        .padding(8.dp)
        .width(boxSize)
        .graphicsLayer {
            this.rotationY = offsetY.value / 10
        }
        .clickable { onClick.invoke() }
        .offset {
            IntOffset(0, offsetY.value.roundToInt())
        }
        .draggable(
            state = rememberDraggableState { delta ->
                coroutineScope.launch {
                    offsetY.snapTo(offsetY.value + delta)
                }
            },
            enabled = true,
            onDragStarted = {

            },
            onDragStopped = {
                coroutineScope.launch {
                    offsetY.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(
                            durationMillis = 600,
                            delayMillis = 0,
                            easing = FastOutLinearInEasing
                        )
                    )
                }
            },
            orientation = Orientation.Vertical
        )
    ) {
        Box(
            modifier = Modifier
                .size(boxSize)
                .rotate(offsetY.value)
                .clip(RoundedCornerShape(cornerRadius))
                .background(bgColor)
                .graphicsLayer {
                    this.shadowElevation = offsetY.value / 10
                }
        ) {
            Icon(
                imageVector = Icons.Outlined.Home,
                tint = Color.White.copy(alpha = 0.5f),
                contentDescription = "Icon",
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.Center)
            )
        }
        Text(
            text = name,
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Medium),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}

@Preview(device = Devices.NEXUS_5, showSystemUi = true)
@Preview(device = Devices.NEXUS_6P, showSystemUi = true)
@Composable
private fun CategoryPreview() {
    CategoryRow()
}