package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.ChatMessageEntity
import com.example.ui.components.WarmDecorativeBackground
import com.example.ui.components.formatTimestamp
import com.example.ui.localization.Language
import com.example.ui.theme.AnekBangla
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CanvasLight
import com.example.ui.theme.BlueBorderLight
import com.example.ui.theme.BlueContainerLight
import com.example.ui.theme.BlueDark
import com.example.ui.theme.BlueDeep
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.BlueSoft
import com.example.ui.theme.TextMutedLight
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import com.example.ui.viewmodel.EscrowViewModel
import com.example.ui.viewmodel.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionChatScreen(
    transactionId: String,
    viewModel: EscrowViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lang by viewModel.currentLanguage.collectAsState()
    val role by viewModel.currentRole.collectAsState()
    val messages by viewModel.selectedChatMessages.collectAsState()
    val tx by viewModel.selectedTransaction.collectAsState()

    var inputMessage by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Order #$transactionId",
                                fontFamily = FontFamily.Monospace,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimaryLight
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = BluePrimary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Text(
                            text = if (lang == Language.BANGLA)
                                "সুরক্ষিত লেনদেন চ্যাট ও সিস্টেম অডিট রেকর্ড"
                                else "Protected Transaction Chat & Audit Records",
                            fontFamily = AnekBangla,
                            fontSize = 11.5.sp,
                            color = TextMutedLight
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimaryLight
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                border = BorderStroke(1.dp, BlueBorderLight),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputMessage,
                        onValueChange = { inputMessage = it },
                        placeholder = {
                            Text(
                                text = if (lang == Language.BANGLA)
                                    "লেনদেন সংক্রান্ত বার্তা লিখুন..."
                                    else "Type transaction-related message...",
                                fontFamily = AnekBangla,
                                fontSize = 13.sp,
                                color = TextMutedLight
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_field"),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BluePrimary,
                            unfocusedBorderColor = BlueBorderLight
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (inputMessage.isNotBlank()) {
                                viewModel.sendChatMessage(transactionId, inputMessage.trim())
                                inputMessage = ""
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(BluePrimary)
                            .testTag("chat_send_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        WarmDecorativeBackground(modifier = Modifier.padding(innerPadding)) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Pinned Escrow Disclaimer
                Surface(
                    color = BlueContainerLight,
                    border = BorderStroke(1.dp, BlueBorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = BluePrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (lang == Language.BANGLA)
                                "সকল বার্তা ও প্রমাণাদি প্ল্যাটফর্ম আরবিট্রেশন ও গ্রাহক সুরক্ষায় সংরক্ষিত থাকে।"
                                else "All conversation & media records are permanently logged for escrow arbitration.",
                            fontFamily = AnekBangla,
                            fontSize = 11.5.sp,
                            color = BlueDark,
                            lineHeight = 16.sp
                        )
                    }
                }

                // Message List
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(messages, key = { it.id }) { msg ->
                        if (msg.isSystem) {
                            SystemMessageBubble(msg = msg)
                        } else {
                            val isCurrentUser = (role == UserRole.BUYER && msg.senderId.startsWith("buyer")) ||
                                    (role == UserRole.SELLER && msg.senderId.startsWith("seller")) ||
                                    (role == UserRole.ADMIN && msg.senderId == "ADMIN")
                            UserMessageBubble(msg = msg, isCurrentUser = isCurrentUser)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SystemMessageBubble(msg: ChatMessageEntity) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color.White,
            border = BorderStroke(1.dp, BlueBorderLight)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = msg.senderName,
                    fontFamily = AnekBangla,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = BluePrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = msg.messageText,
                    fontFamily = AnekBangla,
                    fontSize = 12.5.sp,
                    color = TextPrimaryLight,
                    lineHeight = 17.sp
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = formatTimestamp(msg.timestamp),
                    fontFamily = AnekBangla,
                    fontSize = 10.sp,
                    color = TextMutedLight
                )
            }
        }
    }
}

@Composable
private fun UserMessageBubble(msg: ChatMessageEntity, isCurrentUser: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
    ) {
        Text(
            text = msg.senderName,
            fontFamily = AnekBangla,
            fontSize = 11.5.sp,
            color = TextMutedLight,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )

        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isCurrentUser) 16.dp else 3.dp,
                bottomEnd = if (isCurrentUser) 3.dp else 16.dp
            ),
            color = if (isCurrentUser) BluePrimary else Color.White,
            border = if (isCurrentUser) null else BorderStroke(1.2.dp, BlueBorderLight),
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp)) {
                Text(
                    text = msg.messageText,
                    fontFamily = AnekBangla,
                    fontSize = 13.5.sp,
                    color = if (isCurrentUser) Color.White else TextPrimaryLight,
                    lineHeight = 19.sp
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = formatTimestamp(msg.timestamp),
                    fontFamily = AnekBangla,
                    fontSize = 9.5.sp,
                    color = if (isCurrentUser) Color.White.copy(alpha = 0.8f) else TextMutedLight,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
