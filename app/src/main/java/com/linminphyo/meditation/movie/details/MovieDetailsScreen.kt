package com.linminphyo.meditation.movie.details

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.google.accompanist.coil.rememberCoilPainter
import com.linminphyo.meditation.components.RatingBar

@Composable
fun MovieDetailsScreen(movieDetailsViewModel: VMInterface) {
    val movieDetailsViewState : MovieDetailsViewState by movieDetailsViewModel.movieDetailsFlow.collectAsState()

    when(val mvState = movieDetailsViewState) {
        is MovieDetailsViewState.Content -> {
            MovieScreenContent(mvState.data)
        }
        MovieDetailsViewState.Loading -> {

        }
    }

}

@Composable
private fun MovieScreenContent(movieInfoUIModel: MovieDetailsUIModel) {
    val scrollState = rememberScrollState()
    val coverAlpha = ( 1 - (scrollState.value.toFloat() / 1000)).coerceAtLeast(0.5f)
    val bottomRoundedThreshold = 2 - coverAlpha

    val defaultHeaderTopOffset = 300.dp
    val headerYOffset = ( defaultHeaderTopOffset - LocalDensity.current.run { scrollState.value.toDp() }).coerceAtLeast(100.dp)
    Box {
        MovieSummary(movieInfoUIModel, Modifier.verticalScroll(scrollState).padding(top = 160.dp))
        MovieCover(movieInfoUIModel.movieCoverUrl, bottomRoundedThreshold, modifier = Modifier.alpha(coverAlpha))
        MovieInfoHeader(movieInfoUIModel, bottomRoundedThreshold, modifier = Modifier.offset(y = headerYOffset))
    }
}

@Composable
fun MovieSummary(movieInfoUIModel: MovieDetailsUIModel, modifier: Modifier) {
    Column(
        modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(300.dp))
        Spacer(modifier = Modifier.height(16.dp))
        CastList(movieInfoUIModel.castProfileUrls)
        MovieInfo(movieInfoUIModel)
    }
}

@Preview
@Composable
private fun HeaderPreview() {
    // MovieDetailsScreen(viewModels())
}

/**
 * @param roundedRadius [ 1 to 100]
 */
@Immutable
class BottomRoundedShape(val viewSizeMultiplier: Float = 1f) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            this.addArc(
                oval = Rect(
                    left = - size.width * viewSizeMultiplier,
                    top = -size.height * viewSizeMultiplier / .8f ,
                    size.width + size.width * viewSizeMultiplier,
                    size.height
                ), 0f, 180f
            )
            close()
        }
        return Outline.Generic(path)
    }

}
//
//private val BottomRoundedShape = GenericShape { size, _ ->
//    addPath(roundedBottomPath(size))
//}

fun roundedBottomPath(size: Size): Path {
    return Path().apply {
        val centerPoint = Offset(x = 0f, y = size.width / 2)
        this.addArc(
            oval = Rect(
                left = -size.width / 2,
                top = -size.height,
                size.width * 1.5f,
                size.height
            ), 0f, 180f
        )
        close()
    }
}


@Composable
private fun MovieCover(movieCoverUrl: String, bottomRoundedThreshold: Float, modifier: Modifier) {
    Box(modifier = Modifier
        .background(MaterialTheme.colors.surface, shape = BottomRoundedShape(bottomRoundedThreshold))
        .height(300.dp * bottomRoundedThreshold)
        .shadow(20.dp, BottomRoundedShape(bottomRoundedThreshold), clip = true)) {
        Image(
            painter = rememberCoilPainter(movieCoverUrl),
            contentDescription = "Movie Cover",
            contentScale = ContentScale.Crop,
            modifier = modifier.height(300.dp * bottomRoundedThreshold),
        )
    }
}

@Composable
private fun MovieInfoHeader(
    movieInfoUIModel: MovieDetailsUIModel,
    bottomRoundedThreshold: Float,
    modifier: Modifier
) {
    Column(modifier) {
        Text(
            text = movieInfoUIModel.movieName,
            style = MaterialTheme.typography.h4,
            fontSize = 40.sp / bottomRoundedThreshold,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = movieInfoUIModel.totalRunTimeMin.toString(),
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp)
        )
        RatingBar(
            movieInfoUIModel.rating, modifier = Modifier
                .width(100.dp)
                .height(20.dp)
                .align(Alignment.CenterHorizontally)
        )

        FloatingActionButton(onClick = { /*TODO*/ }, modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 8.dp, bottom = 16.dp)) {
            Image(
                imageVector = Icons.Outlined.PlayArrow,
                colorFilter = ColorFilter.tint(Color.Blue),
                contentDescription = "Play",
            )
        }

    }
}

@Composable
private fun CastList(castProfileUrls: List<String>) {
    LazyRow {
        item { Spacer(modifier = Modifier.width(16.dp)) }
        items(castProfileUrls + castProfileUrls + castProfileUrls + castProfileUrls) {
            CastItem(castProfileUrl = it)
        }
    }
}

@Composable
private fun CastItem(castProfileUrl: String) {
    Image(
        painter = rememberCoilPainter(castProfileUrl),
        contentDescription = "Movie Cover",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .padding(4.dp)
            .size(52.dp)
            .border(2.dp, Color.Blue, CircleShape)
            .padding(4.dp)
            .clip(CircleShape)
    )
}

@Composable
private fun MovieInfo(movieInfoUIModel: MovieDetailsUIModel) {
    Column {
        Text(
            text = movieInfoUIModel.summary.repeat(10),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )
    }
}