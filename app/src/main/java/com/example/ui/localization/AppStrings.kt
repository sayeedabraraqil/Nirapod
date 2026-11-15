package com.example.ui.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf

enum class Language(val code: String, val label: String) {
    BANGLA("bn", "বাংলা"),
    ENGLISH("en", "English")
}

val LocalAppLanguage = compositionLocalOf { mutableStateOf(Language.BANGLA) }

object AppStrings {

    fun appName(lang: Language) = if (lang == Language.BANGLA) "নিরাপদ" else "Nirapod"
    fun appTagline(lang: Language) = if (lang == Language.BANGLA) 
        "আপনার টাকা, আপনার পণ্য—শতভাগ সুরক্ষিত।" 
        else "Your money. Your product. Fully protected."

    // Navigation
    fun navExplore(lang: Language) = if (lang == Language.BANGLA) "এক্সপ্লোর" else "Explore"
    fun navTransactions(lang: Language) = if (lang == Language.BANGLA) "লেনদেন" else "Transactions"
    fun navSellerHub(lang: Language) = if (lang == Language.BANGLA) "সেলার হাব" else "Seller Hub"
    fun navWallet(lang: Language) = if (lang == Language.BANGLA) "ওয়ালেট" else "Wallet"
    fun navAdmin(lang: Language) = if (lang == Language.BANGLA) "অপারেশন্স" else "Operations"

    // Home
    fun heroTitle(lang: Language) = if (lang == Language.BANGLA) 
        "অনলাইনে কেনাবেচা করুন নিশ্চিন্তে" 
        else "Trade online with zero trust anxiety"
    fun heroSubtitle(lang: Language) = if (lang == Language.BANGLA)
        "লেনদেন সম্পন্ন না হওয়া পর্যন্ত আপনার টাকা নিরাপদে সংরক্ষিত থাকবে।"
        else "Your payment is held securely in escrow until product verification is complete."
    fun ctaStartProtected(lang: Language) = if (lang == Language.BANGLA) "সুরক্ষিত লেনদেন শুরু করুন" else "Start Protected Transaction"
    fun ctaBecomeSeller(lang: Language) = if (lang == Language.BANGLA) "বিক্রেতা হিসেবে যোগ দিন" else "Become a Verified Seller"

    // Stats
    fun statProtectedVol(lang: Language) = if (lang == Language.BANGLA) "৳১.২ কোটি+" else "৳12.4M+"
    fun statProtectedVolLabel(lang: Language) = if (lang == Language.BANGLA) "সুরক্ষিত লেনদেন" else "Protected Volume"
    fun statDisputeRate(lang: Language) = if (lang == Language.BANGLA) "৯৯.৬%" else "99.6%"
    fun statDisputeRateLabel(lang: Language) = if (lang == Language.BANGLA) "সফল সমাধান হার" else "Resolution Rate"
    fun statZeroLoss(lang: Language) = if (lang == Language.BANGLA) "০%" else "0%"
    fun statZeroLossLabel(lang: Language) = if (lang == Language.BANGLA) "গ্রাহকের অর্থ ক্ষতি" else "Fund Loss"

    // How it works
    fun howItWorksTitle(lang: Language) = if (lang == Language.BANGLA) "নিরাপদ যেভাবে কাজ করে" else "How Nirapod Protects You"
    fun step1Title(lang: Language) = if (lang == Language.BANGLA) "১. সুরক্ষিত পেমেন্ট" else "1. Pay into Escrow"
    fun step1Desc(lang: Language) = if (lang == Language.BANGLA) "টাকা সরাসরি বিক্রেতা পায় না, নিরাপদ সুরক্ষায় সাময়িক জমা থাকে।" else "Funds are locked in escrow, not sent to seller."
    fun step2Title(lang: Language) = if (lang == Language.BANGLA) "২. নিশ্চিত ডেলিভারি" else "2. Seller Dispatches"
    fun step2Desc(lang: Language) = if (lang == Language.BANGLA) "পেমেন্ট সুরক্ষিত দেখে বিক্রেতা কুরিয়ার বা ডিজিটাল কি পাঠায়।" else "Seller delivers product knowing payment is guaranteed."
    fun step3Title(lang: Language) = if (lang == Language.BANGLA) "৩. পণ্য যাচাই ও মুক্তি" else "3. Verify & Release"
    fun step3Desc(lang: Language) = if (lang == Language.BANGLA) "পণ্য হাতে পেয়ে সন্তুষ্ট হলে তবেই বিক্রেতার টাকা ছাড় করা হয়।" else "Buyer inspects item. Only after approval is seller paid."

    // Financial & Escrow notices
    fun escrowNotice(lang: Language) = if (lang == Language.BANGLA)
        "টাকা বিক্রেতাকে দেওয়া হয়নি। পণ্য বুঝে পাওয়ার পর আপনি অনুমোদন দিলেই কেবল বিক্রেতা টাকা পাবেন।"
        else "Funds are held securely by Nirapod. The seller has NOT received payment yet."
    fun productPrice(lang: Language) = if (lang == Language.BANGLA) "পণ্যের মূল্য" else "Product Price"
    fun courierDeliveryFee(lang: Language) = if (lang == Language.BANGLA) "কুরিয়ার ডেলিভারি চার্জ" else "Courier Delivery Fee"
    fun escrowProtectionFee(lang: Language) = if (lang == Language.BANGLA) "এসক্রো সুরক্ষা ফি" else "Escrow Protection Fee"
    fun totalPayable(lang: Language) = if (lang == Language.BANGLA) "সর্বমোট প্রদেয়" else "Total Payable"

    // Actions
    fun buyWithProtection(lang: Language) = if (lang == Language.BANGLA) "নিরাপদ সুরক্ষায় কিনুন" else "Buy with Protection"
    fun confirmReceipt(lang: Language) = if (lang == Language.BANGLA) "পণ্য পেয়েছি ও সন্তুষ্ট" else "Confirm Receipt & Release Funds"
    fun reportProblem(lang: Language) = if (lang == Language.BANGLA) "সমস্যা রিপোর্ট / বিতর্ক" else "Report Problem / Dispute"
    fun openChat(lang: Language) = if (lang == Language.BANGLA) "লেনদেন চ্যাট" else "Transaction Chat"
    fun revealKey(lang: Language) = if (lang == Language.BANGLA) "লাইসেন্স কি আনলক করুন" else "Reveal License Key"
    fun viewTracking(lang: Language) = if (lang == Language.BANGLA) "কুরিয়ার ট্র্যাকিং দেখুন" else "View Courier Tracking"

    // Wallet
    fun heldInEscrow(lang: Language) = if (lang == Language.BANGLA) "এসক্রোতে রক্ষিত ফান্ড" else "Held in Escrow"
    fun availableBalance(lang:Language) = if (lang == Language.BANGLA) "উত্তোলনযোগ্য ব্যালেন্স" else "Available Balance"
    fun pendingPayout(lang: Language) = if (lang == Language.BANGLA) "প্রক্রিয়াধীন পেআউট" else "Pending Payout"
    fun totalEarnings(lang: Language) = if (lang == Language.BANGLA) "মোট আয়" else "Total Earnings"
    fun withdrawFunds(lang: Language) = if (lang == Language.BANGLA) "টাকা উত্তোলন করুন" else "Withdraw Funds"

    // Common
    fun searchHint(lang: Language) = if (lang == Language.BANGLA) "পণ্য, সফটওয়্যার বা লাইসেন্স খুঁজুন..." else "Search protected listings, software, licenses..."
    fun verifiedSeller(lang: Language) = if (lang == Language.BANGLA) "যাচাইকৃত বিক্রেতা" else "Verified Seller"
    fun returnPolicy(lang: Language, days: Int) = if (lang == Language.BANGLA) "$days দিনের রিটার্ন ও নিরীক্ষা উইন্ডো" else "$days-Day Inspection Window"
    fun cancel(lang: Language) = if (lang == Language.BANGLA) "বাতিল" else "Cancel"
    fun submit(lang: Language) = if (lang == Language.BANGLA) "সাবমিট করুন" else "Submit"
}
