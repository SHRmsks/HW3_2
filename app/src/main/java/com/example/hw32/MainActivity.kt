
package com.example.hw32
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hw32.ui.theme.HW32Theme
import org.xmlpull.v1.XmlPullParser

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var dataset = mutableListOf<Card>()
            val parser = LocalContext.current.resources.getXml(R.xml.flashcards)
            var type = parser.eventType
            var currentQuestion: String? = null
            var currentAnswer: String? = null
            while (type != XmlPullParser.END_DOCUMENT) {
                if (type == XmlPullParser.START_TAG && parser.name == "question") {
                    currentQuestion = parser.nextText().trim()
                }
                if (type == XmlPullParser.START_TAG && parser.name == "answer") {
                    currentAnswer = parser.nextText().trim()
                }
                if (type == XmlPullParser.END_TAG && parser.name == "flashcard") {
                    dataset.add(Card(question = currentQuestion!!, answer = currentAnswer!!))
                }
                type = parser.next()
            }

            HW32Theme {
                flashCardLoader(dataset)
            }
        }
    }
}

data class Card(
    val question: String,
    val answer: String,
)

@Composable
fun flashcard(card: Card) {
    val flipped = remember { mutableStateOf(false) }
    var init by remember { mutableStateOf(true) }
    var text by remember { mutableStateOf(card.question) }
    Row(
        modifier =
            Modifier.width(200.dp).height(300.dp).clickable {
                flipped.value = !flipped.value
                init = false
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        LaunchedEffect(init, flipped.value) {
            text =
                if (!init && flipped.value) {
                    card.answer
                } else {
                    card.question
                }
        }
        Text(
            text = text,
            color = Color(0xFF3a0aa3),
            fontFamily = FontFamily(Font(R.font.hanzipen)),
            fontWeight = FontWeight.W300,
        )
    }
}

@Composable
fun flashCardLoader(cards: List<Card>) {
    Box(modifier = Modifier.fillMaxSize(1f), contentAlignment = Alignment.Center) {
        LazyRow(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f),
            contentPadding = PaddingValues(3.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            userScrollEnabled = true,
        ) {
            items(cards) { card ->
                flashcard(card = card)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun test() {
    var dataset = mutableListOf<Card>()
    val parser = LocalContext.current.resources.getXml(R.xml.flashcards)
    var type = parser.eventType
    var currentQuestion: String? = null
    var currentAnswer: String? = null
    while (type != XmlPullParser.END_DOCUMENT) {
        if (type == XmlPullParser.START_TAG && parser.name == "question") {
            currentQuestion = parser.nextText().trim()
        }
        if (type == XmlPullParser.START_TAG && parser.name == "answer") {
            currentAnswer = parser.nextText().trim()
        }
        if (type == XmlPullParser.END_TAG && parser.name == "flashcard") {
            dataset.add(Card(question = currentQuestion!!, answer = currentAnswer!!))
        }
        type = parser.next()
    }

    HW32Theme {
        flashCardLoader(dataset)
    }
}
