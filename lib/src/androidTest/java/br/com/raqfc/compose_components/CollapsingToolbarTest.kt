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

package br.com.raqfc.compose_components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.raqfc.compose_components.composables.scaffold.CollapsingToolbarScaffold
import br.com.raqfc.compose_components.composables.scaffold.CollapsingToolbarScaffoldState
import br.com.raqfc.compose_components.composables.scaffold.toolbar.CollapsingToolbarState
import br.com.raqfc.compose_components.composables.scaffold.toolbar.ScrollStrategy
import kotlin.math.abs
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CollapsingToolbarTest {
	@get:Rule
	val rule = createComposeRule()

	@Test
	fun testCollapse() {
		val state = CollapsingToolbarScaffoldState(
			CollapsingToolbarState()
		)

		rule.setContent {
			CollapsingToolbarScaffold(
				modifier = Modifier
					.fillMaxSize(),
				state = state,
				scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
				toolbarModifier = Modifier
					.semantics {
						testTag = "toolbar"
					},
				toolbar = {
					Box(modifier = Modifier
						.fillMaxWidth()
						.height(300.dp))
					Box(modifier = Modifier
						.fillMaxWidth()
						.height(50.dp))
				}
			) {
				LazyColumn(modifier = Modifier
					.fillMaxSize()
					.semantics {
						testTag = "contentList"
					}
				) {
					items(List(100) { "Hello $it" }) {
						Text(text = it)
					}
				}
			}
		}

		assert(state.toolbarState.progress == 1f)

		rule.onNode(hasTestTag("toolbar"))
			.assertHeightIsEqualTo(300.dp)

		rule.onNode(hasTestTag("contentList"))
			.performTouchInput {
				swipeUp()
			}

		rule.onNode(hasTestTag("toolbar"))
			.assertHeightIsEqualTo(50.dp)

		assert(abs(state.toolbarState.progress) < 0.01f)
	}
}
