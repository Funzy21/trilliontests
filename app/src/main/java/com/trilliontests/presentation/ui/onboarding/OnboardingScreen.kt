package com.trilliontests.presentation.ui.onboarding

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

data class UserType(
    val title: String,
    val description: String
)

data class StudyPurpose(
    val title: String,
    val description: String
)

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit
) {
    var currentStep by remember { mutableStateOf(0) }
    var selectedUserType by remember { mutableStateOf<String?>(null) }
    var selectedPurposes by remember { mutableStateOf(setOf<String>()) }

    val userTypes = remember {
        listOf(
            UserType("Student", "Currently enrolled in an educational institution"),
            UserType("Teacher", "Teaching or instructing students"),
            UserType("Parent", "Supporting a student's education")
        )
    }

    val studyPurposes = remember {
        listOf(
            StudyPurpose("Exam Preparation", "Preparing for upcoming school or university exams"),
            StudyPurpose("Licensure", "Studying for professional certification or licensure"),
            StudyPurpose("General Review", "General knowledge enhancement and review")
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Progress indicator
        LinearProgressIndicator(
            progress = when (currentStep) {
                0 -> 0.5f
                else -> 1f
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        )

        // Content
        when (currentStep) {
            0 -> UserTypeSelection(
                userTypes = userTypes,
                selectedUserType = selectedUserType,
                onUserTypeSelected = { selectedUserType = it }
            )
            1 -> StudyPurposeSelection(
                purposes = studyPurposes,
                selectedPurposes = selectedPurposes,
                onPurposeSelected = { purpose ->
                    selectedPurposes = if (selectedPurposes.contains(purpose)) {
                        selectedPurposes - purpose
                    } else {
                        selectedPurposes + purpose
                    }
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Navigation buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onComplete,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Skip")
            }

            Button(
                onClick = {
                    if (currentStep < 1) currentStep++ else onComplete()
                },
                enabled = when (currentStep) {
                    0 -> selectedUserType != null
                    1 -> selectedPurposes.isNotEmpty()
                    else -> true
                }
            ) {
                Text(if (currentStep < 1) "Next" else "Get Started")
            }
        }
    }
}

@Composable
private fun UserTypeSelection(
    userTypes: List<UserType>,
    selectedUserType: String?,
    onUserTypeSelected: (String) -> Unit
) {
    Column(modifier = Modifier.selectableGroup()) {
        Text(
            text = "I am a...",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        userTypes.forEach { userType ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .selectable(
                        selected = selectedUserType == userType.title,
                        onClick = { onUserTypeSelected(userType.title) },
                        role = Role.RadioButton
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedUserType == userType.title) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = userType.title,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = userType.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun StudyPurposeSelection(
    purposes: List<StudyPurpose>,
    selectedPurposes: Set<String>,
    onPurposeSelected: (String) -> Unit
) {
    Column {
        Text(
            text = "I'm using this app for...",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "Select all that apply",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        purposes.forEach { purpose ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .selectable(
                        selected = selectedPurposes.contains(purpose.title),
                        onClick = { onPurposeSelected(purpose.title) },
                        role = Role.Checkbox
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedPurposes.contains(purpose.title)) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = purpose.title,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = purpose.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
} 