package br.com.raqfc.compose_components.presentation.composables.listing

import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import br.com.raqfc.compose_components.R
import br.com.raqfc.compose_components.theme.AppTheme
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun CircleImageView(
    srcUrl: String?,
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    @DrawableRes placeholderImageRes: Int = R.drawable.ic_person_placeholder,
    contentDescription: String = stringResource(id = R.string.content_description_avatar_image)
) {
    CircleImageView(
        modifier = modifier,
        checked = checked
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(srcUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(placeholderImageRes),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
fun CircleImageView(
    @DrawableRes imageRes: Int?,
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    @DrawableRes placeholderImageRes: Int = R.drawable.ic_person_placeholder,
    contentDescription: String = stringResource(id = R.string.content_description_avatar_image)
) {
    CircleImageView(
        modifier = modifier,
        checked = checked
    ) {
        Image(
            painter = painterResource(id = imageRes ?: placeholderImageRes),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
private fun CircleImageView(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .size(AppTheme.dimensions.defaultCircleAvatarSize)
            .then(modifier)
    ) {
        content()

        if (checked) {
            Box(//todo selection color
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.4f))
                    .fillMaxSize()
            )
            AnimatedCheck(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(AppTheme.dimensions.padding3)
            )
        }
    }
}

@Composable
private fun AnimatedCheck(
    modifier: Modifier = Modifier,
    @RawRes lottieAnimationFile: Int = R.raw.animated_check_lottie
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottieAnimationFile))
    LottieAnimation(
        modifier = modifier,
        composition = composition,
        speed = 3.5f
    )
}