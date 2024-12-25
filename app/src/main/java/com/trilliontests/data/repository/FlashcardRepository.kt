package com.trilliontests.data.repository

import com.trilliontests.model.FlashcardSet
import com.trilliontests.model.Flashcard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlashcardRepository @Inject constructor() {
    private val flashcardSets = listOf(
        FlashcardSet(
            id = "vocab_advanced",
            title = "Advanced Vocabulary Practice",
            description = "Improve your vocabulary with challenging English words",
            cardCount = 5,
            cards = listOf(
                Flashcard(
                    id = "1",
                    front = "Ephemeral",
                    back = "Lasting for a very short time; short-lived or temporary"
                ),
                Flashcard(
                    id = "2",
                    front = "Ubiquitous",
                    back = "Present, appearing, or found everywhere"
                ),
                Flashcard(
                    id = "3",
                    front = "Surreptitious",
                    back = "Kept secret, especially because it would not be approved of; secretive or stealthy"
                ),
                Flashcard(
                    id = "4",
                    front = "Perfunctory",
                    back = "Carried out with minimal effort or reflection; done merely as a duty"
                ),
                Flashcard(
                    id = "5",
                    front = "Mellifluous",
                    back = "Sweet or musical; pleasant to hear"
                )
            )
        ),
        FlashcardSet(
            id = "vocab_business",
            title = "Business Terms",
            description = "Essential business and economics vocabulary",
            cardCount = 3,
            cards = listOf(
                Flashcard(
                    id = "1",
                    front = "Paradigm",
                    back = "A typical example or pattern of something; a model"
                ),
                Flashcard(
                    id = "2",
                    front = "Synergy",
                    back = "The interaction of elements that when combined produce a total effect greater than the sum of the individual elements"
                ),
                Flashcard(
                    id = "3",
                    front = "Leverage",
                    back = "Use (something) to maximum advantage"
                )
            )
        ),
        FlashcardSet(
            id = "vocab_science",
            title = "Scientific Terms",
            description = "Common scientific terminology and concepts",
            cardCount = 3,
            cards = listOf(
                Flashcard(
                    id = "1",
                    front = "Entropy",
                    back = "A thermodynamic quantity representing the unavailability of a system's thermal energy for conversion into mechanical work"
                ),
                Flashcard(
                    id = "2",
                    front = "Quantum",
                    back = "The smallest amount of a physical quantity that can exist independently"
                ),
                Flashcard(
                    id = "3",
                    front = "Catalyst",
                    back = "A substance that increases the rate of a chemical reaction without itself undergoing any permanent chemical change"
                )
            )
        )
    )

    fun getFlashcardSets(): Flow<List<FlashcardSet>> = flowOf(flashcardSets)

    fun getFlashcardSet(setId: String): Flow<FlashcardSet?> = 
        flowOf(flashcardSets.find { set -> set.id == setId })
} 