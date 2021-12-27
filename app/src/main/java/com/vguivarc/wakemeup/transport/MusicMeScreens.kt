/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vguivarc.wakemeup.transport

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.vguivarc.wakemeup.R

enum class MusicMeScreens(
    val route: String,
    @StringRes val resourceId: Int = 0,
    val icon: ImageVector,
) {
    AlarmListScreen(
        "AlarmList", R.string.reveils
      ,  icon = Icons.Filled.Close,
    ),
    MusicListScreen(
        "MusicList", R.string.musique, icon = Icons.Filled.Done,
    ),
    ContactListScreen(
        "ContactList", R.string.contacts, icon = Icons.Filled.Person,
    ),
    HistoryScreen(
        "History", R.string.historique
        ,  icon = Icons.Filled.PlayArrow,
    ),
    SettingsScreen(
        "Settings", R.string.historique
        ,  icon = Icons.Filled.PlayArrow,
    ),
    ;

    companion object {
        fun fromRoute(route: String?): MusicMeScreens =
            when (route?.substringBefore("/")) {
                AlarmListScreen.route -> AlarmListScreen
                MusicListScreen.route -> MusicListScreen
                ContactListScreen.route -> MusicListScreen
                HistoryScreen.route -> HistoryScreen
                SettingsScreen.route -> SettingsScreen
                null -> AlarmListScreen
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}
