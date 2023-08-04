package data_structures.ai_models

// This is a packaging file, packaging between views and sending to server.



import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ChatDataViewModel : ViewModel() {
    val botMessages = mutableStateOf<Map<String, List<BotMessage>>>(emptyMap())

    fun updateBotMessages(newBotMessages: Map<String, List<BotMessage>>) {
        botMessages.value = newBotMessages
    }
}

data class BotMessage(
    var message: String,
    var flag: Boolean
)
