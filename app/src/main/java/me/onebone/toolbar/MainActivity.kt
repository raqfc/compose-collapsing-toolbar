/*
 * Copyright (c) 2021 onebone <me@onebone.me>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */

package me.onebone.toolbar

import ActionItem
import ActionMenu
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.onebone.toolbar.ui.theme.CollapsingToolbarTheme

class MainActivity: ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			CollapsingToolbarTheme {
				// A surface container using the 'background' color from the theme
				Surface(color = MaterialTheme.colors.background) {
					MainScreen()
				}
			}
		}
	}
}

@Composable
internal fun MainScreen() {
	val state = rememberCollapsingToolbarScaffoldState()

	val items = listOf(
		ActionItem(R.string.a, Icons.Default.Call, OverflowMode.IF_NECESSARY) {},
		ActionItem(R.string.b, Icons.Default.Send, OverflowMode.IF_NECESSARY) {},
		ActionItem(R.string.c, Icons.Default.Email, OverflowMode.IF_NECESSARY) {},
		ActionItem(R.string.d, Icons.Default.Delete, OverflowMode.IF_NECESSARY) {},
	)

	var actionsRowWidth by remember { mutableStateOf(0) }
	CollapsingToolbarScaffold(
		modifier = Modifier
			.fillMaxSize(),
		state = state,
		scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
		toolbar = {
			val textSize = (18 + (30 - 18) * state.toolbarState.progress).sp
			val titleBottomPadding = (16 * (1 - state.toolbarState.progress)).dp
			val actionsFraction = if(state.toolbarState.progress == 1f) 0.9f else 0.1f

			Text(
				text = "Title",
				modifier = Modifier
					.road(
						ViewConfiguration(Alignment.CenterStart, 120.dp),
						ViewConfiguration(Alignment.BottomStart, 16.dp)
					)
					.padding(
						top = 16.dp,
						end = 16.dp,
						bottom = titleBottomPadding
					),
				color = Color.White,
				fontSize = textSize
			)

			Image(
				modifier = Modifier
					.pin()
					.padding(12.dp),
				painter = painterResource(id = R.drawable.abc_vector_test),
				contentDescription = null
			)
			
			Row(
				modifier = Modifier
					.fillMaxWidth(actionsFraction)
					.animateContentSize()
					.onGloballyPositioned {
						actionsRowWidth = it.size.width
					}
					.pin(Alignment.TopEnd),
				horizontalArrangement = Arrangement.End
			) {
				ActionMenu(
					items = items,
					viewWidth = actionsRowWidth
				)
			}
		}
	) {
		LazyColumn(
			modifier = Modifier
				.fillMaxWidth()
		) {
			items(100) {
				Text(
					text = "Item $it",
					modifier = Modifier.padding(8.dp)
				)
			}
		}

		Box(
			modifier = Modifier
				.fillMaxWidth()
				.alpha(0.5f)
				.height(40.dp)
		)
	}

}
