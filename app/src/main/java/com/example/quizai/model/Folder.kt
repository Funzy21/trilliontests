package com.example.quizai.model
import android.provider.DocumentsContract.Document


data class Folder(
    val id: String,
    val name: String,
    val documents: List<Document> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
) 