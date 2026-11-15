package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.ui.components.NirapodBrandLogo
import com.example.ui.components.NirapodFloatingNavBar
import com.example.ui.components.NirapodLanguageSwitcher
import com.example.ui.theme.AnekBangla
import com.example.ui.theme.BorderLight
import com.example.ui.theme.BlueBorderLight
import com.example.ui.theme.BlueContainerLight
import com.example.ui.theme.BlueDark
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.CanvasLight
import com.example.ui.theme.SurfaceLight
import com.example.ui.theme.SurfaceVariantLight
import com.example.ui.theme.TextMutedLight
import com.example.ui.theme.TextPrimaryLight
import com.example.ui.theme.TextSecondaryLight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.localization.AppStrings
import com.example.ui.localization.Language
import com.example.ui.localization.LocalAppLanguage
import com.example.ui.screens.AdminOperationsScreen
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.CreateListingScreen
import com.example.ui.screens.DisputeScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ListingDetailScreen
import com.example.ui.screens.SellerDashboardScreen
import com.example.ui.screens.TransactionChatScreen
import com.example.ui.screens.TransactionDetailScreen
import com.example.ui.screens.TransactionsListScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.NirapodTheme
import com.example.ui.viewmodel.EscrowViewModel
import com.example.ui.viewmodel.UserRole

class MainActivity : ComponentActivity() {

    private val viewModel: EscrowViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NirapodTheme {
                NirapodApp(viewModel = viewModel)
            }
        }
    }
}

sealed class Screen(val route: String, val icon: ImageVector) {
    object Explore : Screen("explore", Icons.Default.Explore)
    object Transactions : Screen("transactions", Icons.Default.ReceiptLong)
    object SellerHub : Screen("seller_hub", Icons.Default.Storefront)
    object Wallet : Screen("wallet", Icons.Default.AccountBalanceWallet)
    object Admin : Screen("admin_ops", Icons.Default.AdminPanelSettings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NirapodApp(viewModel: EscrowViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val lang by viewModel.currentLanguage.collectAsState()
    val role by viewModel.currentRole.collectAsState()

    var showRoleMenu by remember { mutableStateOf(false) }

    val isTopLevelDestination = currentRoute in listOf(
        Screen.Explore.route,
        Screen.Transactions.route,
        Screen.SellerHub.route,
        Screen.Wallet.route,
        Screen.Admin.route
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
        topBar = {
            if (isTopLevelDestination) {
                TopAppBar(
                    title = {
                        NirapodBrandLogo(size = 32.dp, showText = true, lang = lang)
                    },
                    actions = {
                        // Role Switcher Dropdown (Compact, touch-accessible, responsive)
                        Box {
                            Surface(
                                onClick = { showRoleMenu = true },
                                shape = RoundedCornerShape(14.dp),
                                color = BlueContainerLight.copy(alpha = 0.7f),
                                border = BorderStroke(1.dp, BlueBorderLight),
                                modifier = Modifier
                                    .defaultMinSize(minHeight = 40.dp)
                                    .testTag("role_switcher_chip")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 7.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val roleIcon = when (role) {
                                        UserRole.BUYER -> Icons.Default.Person
                                        UserRole.SELLER -> Icons.Default.Storefront
                                        UserRole.ADMIN -> Icons.Default.AdminPanelSettings
                                    }
                                    Icon(
                                        imageVector = roleIcon,
                                        contentDescription = null,
                                        tint = BluePrimary,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = when (role) {
                                            UserRole.BUYER -> if (lang == Language.BANGLA) "ক্রেতা" else "Buyer"
                                            UserRole.SELLER -> if (lang == Language.BANGLA) "বিক্রেতা" else "Seller"
                                            UserRole.ADMIN -> if (lang == Language.BANGLA) "অ্যাডমিন" else "Admin"
                                        },
                                        fontFamily = AnekBangla,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimaryLight
                                    )
                                    Icon(
                                        Icons.Default.ArrowDropDown,
                                        contentDescription = null,
                                        tint = BluePrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            DropdownMenu(
                                expanded = showRoleMenu,
                                onDismissRequest = { showRoleMenu = false }
                            ) {
                                UserRole.values().forEach { r ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = if (lang == Language.BANGLA) r.labelBn else r.labelEn,
                                                fontFamily = AnekBangla,
                                                fontWeight = if (role == r) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        onClick = {
                                            viewModel.setRole(r)
                                            showRoleMenu = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Dedicated Unclipped Language Toggle Button
                        NirapodLanguageSwitcher(
                            currentLanguage = lang,
                            onToggle = {
                                val next = if (lang == Language.BANGLA) Language.ENGLISH else Language.BANGLA
                                viewModel.setLanguage(next)
                            },
                            modifier = Modifier.testTag("language_toggle_button")
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Explore.route,
            modifier = Modifier.padding(
                top = if (isTopLevelDestination) innerPadding.calculateTopPadding() else 0.dp
            )
        ) {
            composable(Screen.Explore.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onListingClick = { listingId ->
                        navController.navigate("listing/$listingId")
                    },
                    onStartTransactionClick = {
                        val firstItem = viewModel.allListings.value.firstOrNull()
                        if (firstItem != null) {
                            navController.navigate("listing/${firstItem.id}")
                        }
                    },
                    onBecomeSellerClick = {
                        viewModel.setRole(UserRole.SELLER)
                        navController.navigate(Screen.SellerHub.route)
                    }
                )
            }

            composable(Screen.Transactions.route) {
                TransactionsListScreen(
                    viewModel = viewModel,
                    onTransactionClick = { trxId ->
                        viewModel.selectTransaction(trxId)
                        navController.navigate("transaction/$trxId")
                    }
                )
            }

            composable(Screen.SellerHub.route) {
                SellerDashboardScreen(
                    viewModel = viewModel,
                    onTransactionClick = { trxId ->
                        viewModel.selectTransaction(trxId)
                        navController.navigate("transaction/$trxId")
                    },
                    onCreateListingClick = {
                        navController.navigate("create_listing")
                    },
                    onWithdrawClick = {
                        navController.navigate(Screen.Wallet.route)
                    }
                )
            }

            composable(Screen.Wallet.route) {
                WalletScreen(viewModel = viewModel)
            }

            composable(Screen.Admin.route) {
                AdminOperationsScreen(
                    viewModel = viewModel,
                    onOpenDispute = { trxId ->
                        viewModel.selectTransaction(trxId)
                        navController.navigate("dispute/$trxId")
                    }
                )
            }

            composable("listing/{listingId}") { backStackEntry ->
                val listingId = backStackEntry.arguments?.getString("listingId") ?: ""
                ListingDetailScreen(
                    listingId = listingId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onProceedToCheckout = { id ->
                        navController.navigate("checkout/$id")
                    }
                )
            }

            composable("checkout/{listingId}") { backStackEntry ->
                val listingId = backStackEntry.arguments?.getString("listingId") ?: ""
                CheckoutScreen(
                    listingId = listingId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onOrderComplete = { trxId ->
                        viewModel.selectTransaction(trxId)
                        navController.navigate("transaction/$trxId") {
                            popUpTo(Screen.Explore.route)
                        }
                    }
                )
            }

            composable("transaction/{trxId}") { backStackEntry ->
                val trxId = backStackEntry.arguments?.getString("trxId") ?: ""
                viewModel.selectTransaction(trxId)
                TransactionDetailScreen(
                    transactionId = trxId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onOpenChat = { id ->
                        navController.navigate("chat/$id")
                    },
                    onOpenDispute = { id ->
                        navController.navigate("dispute/$id")
                    }
                )
            }

            composable("chat/{trxId}") { backStackEntry ->
                val trxId = backStackEntry.arguments?.getString("trxId") ?: ""
                viewModel.selectTransaction(trxId)
                TransactionChatScreen(
                    transactionId = trxId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable("dispute/{trxId}") { backStackEntry ->
                val trxId = backStackEntry.arguments?.getString("trxId") ?: ""
                viewModel.selectTransaction(trxId)
                DisputeScreen(
                    transactionId = trxId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable("create_listing") {
                CreateListingScreen(
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onListingCreated = { newId ->
                        navController.navigate("listing/$newId") {
                            popUpTo(Screen.SellerHub.route)
                        }
                    }
                )
            }
        }
    }

    if (isTopLevelDestination) {
        NirapodFloatingNavBar(
            currentRoute = currentRoute,
            onNavigate = { route ->
                navController.navigate(route) {
                    popUpTo(Screen.Explore.route) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            lang = lang,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
        )
    }
}
}
