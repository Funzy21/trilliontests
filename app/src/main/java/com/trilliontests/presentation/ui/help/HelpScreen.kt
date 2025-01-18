package com.trilliontests.presentation.ui.help

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import android.content.Intent
import android.net.Uri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpScreen() {
    val context = LocalContext.current
    val faqs = remember {
        listOf(
            FAQ(
                "What is TrillionTests?",
                "TrillionTests is an educational app that helps you study more effectively using AI-generated quizzes and flashcards."
            ),
            FAQ(
                "How do I create flashcards?",
                "You can create flashcards by uploading study material and letting our AI generate them automatically, or create them manually."
            ),
            FAQ(
                "Is there a limit to how many quizzes I can take?",
                "No, you can take as many quizzes as you want to reinforce your learning."
            ),
            // Add more FAQs as needed
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Help Center",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "Frequently Asked Questions",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(faqs) { faq ->
                FAQItem(faq)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:support@trilliontests.com")
                    putExtra(Intent.EXTRA_SUBJECT, "TrillionTests Support")
                }
                context.startActivity(Intent.createChooser(intent, "Send email"))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Default.Email,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Contact Support")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FAQItem(faq: FAQ) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = faq.question,
                style = MaterialTheme.typography.titleMedium
            )
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = faq.answer,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

data class FAQ(
    val question: String,
    val answer: String
) 