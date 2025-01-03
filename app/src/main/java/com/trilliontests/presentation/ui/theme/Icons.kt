package com.trilliontests.presentation.ui.theme

import android.media.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import com.trilliontests.R

object AppIcons {
    @Composable
    fun subject(): ImageVector = ImageVector.vectorResource(id = R.drawable.subject_icon)
    
    @Composable
    fun progress(): ImageVector = ImageVector.vectorResource(id = R.drawable.progress_icon)
    
    @Composable
    fun community(): ImageVector = ImageVector.vectorResource(id = R.drawable.community_icon)

    @Composable
    fun profile(): ImageVector = ImageVector.vectorResource(id = R.drawable.user_icon)

    @Composable
    fun home(): ImageVector = ImageVector.vectorResource(id = R.drawable.home_icon)
} 