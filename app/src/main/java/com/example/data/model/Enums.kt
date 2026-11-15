package com.example.data.model

enum class TransactionState(val displayNameEn: String, val displayNameBn: String) {
    ORDER_CREATED("Order Created", "অর্ডার তৈরি হয়েছে"),
    PAYMENT_PENDING("Payment Pending", "পেমেন্ট অপেক্ষমান"),
    PAYMENT_VERIFIED("Payment Verified", "পেমেন্ট যাচাইকৃত"),
    FUNDS_HELD("Funds Secured in Escrow", "এসক্রোতে ফান্ড সুরক্ষিত"),
    SELLER_NOTIFIED("Seller Notified", "বিক্রেতাকে অবহিত করা হয়েছে"),
    SELLER_ACCEPTED("Seller Accepted", "বিক্রেতা অর্ডার গ্রহণ করেছেন"),
    PROCESSING("Processing / Packaging", "প্রস্তুত করা হচ্ছে"),
    SHIPPED("Shipped via Courier", "কুরিয়ারে পাঠানো হয়েছে"),
    OUT_FOR_DELIVERY("Out for Delivery", "ডেলিভারির জন্য বের হয়েছে"),
    DELIVERED("Delivered", "ডেলিভারি সম্পন্ন"),
    BUYER_CONFIRMATION_PENDING("Inspection Window Active", "যাচাইকরণ সময় সক্রিয়"),
    COMPLETED("Completed & Funds Released", "সম্পন্ন ও ফান্ড রিলিজ হয়েছে"),
    SELLER_PAYOUT_PENDING("Seller Payout Pending", "উত্তোলন অপেক্ষমান"),
    SELLER_PAID("Seller Paid Out", "বিক্রেতাকে অর্থ প্রদান সম্পন্ন"),
    DISPUTED("Dispute Opened", "বিতর্ক খোলা হয়েছে"),
    UNDER_REVIEW("Dispute Under Review", "বিতর্ক পর্যালোচনায় রয়েছে"),
    REFUND_PENDING("Refund Processing", "রিফান্ড প্রক্রিয়াকরণ"),
    REFUNDED("Refunded to Buyer", "ক্রেতাকে রিফান্ড সম্পন্ন"),
    CANCELLED("Cancelled", "বাতিল করা হয়েছে"),
    DELIVERY_FAILED("Delivery Refused / Failed", "ডেলিভারি ব্যর্থ / প্রত্যাখ্যাত")
}

enum class ProductType(val labelEn: String, val labelBn: String) {
    PHYSICAL("Physical Product", "ভৌত পণ্য"),
    DIGITAL_KEY("Digital License / Key", "ডিজিটাল লাইসেন্স / কি"),
    DIGITAL_FILE("Digital Asset / File", "ডিজিটাল ফাইল / অ্যাসেট"),
    SERVICE("Service / Milestone", "সেবা / মাইলস্টোন")
}

enum class CourierProvider(val providerName: String, val defaultCharge: Double) {
    STEADFAST("Steadfast Courier", 70.0),
    PATHAO("Pathao Courier", 80.0),
    REDX("RedX Delivery", 75.0),
    ECOURIER("eCourier BD", 85.0),
    PAPERFLY("Paperfly Express", 70.0),
    DIGITAL_INSTANT("Instant Digital Delivery", 0.0)
}

enum class PaymentProvider(val providerName: String, val iconResName: String) {
    BKASH("bKash PGW", "ic_bkash"),
    NAGAD("Nagad", "ic_nagad"),
    ROCKET("DBBL Rocket", "ic_rocket"),
    UPAY("Upay", "ic_upay"),
    BANK_TRANSFER("City / BRAC / DBBL Bank", "ic_bank"),
    CARD_GATEWAY("Visa / Mastercard (SSLCommerz)", "ic_card")
}

enum class DisputeReason(val titleEn: String, val titleBn: String) {
    NOT_RECEIVED("Product Not Received", "পণ্য পাওয়া যায়নি"),
    WRONG_PRODUCT("Wrong Product Received", "ভুল পণ্য পাঠানো হয়েছে"),
    DEFECTIVE("Product Defective / Not Working", "পণ্যটি ত্রুটিপূর্ণ বা কাজ করছে না"),
    DAMAGED("Damaged in Shipping", "শিপিংয়ের সময় ক্ষতিগ্রস্ত"),
    DIGITAL_KEY_INVALID("Digital Key / License Invalid", "ডিজিটাল কি বা কোড অকার্যকর"),
    SELLER_UNRESPONSIVE("Seller Failed to Ship / Unresponsive", "বিক্রেতা যোগাযোগ করছেন না"),
    DELIVERY_REFUSED_BY_BUYER("Buyer Refused Parcel at Doorstep", "ক্রেতা পণ্য গ্রহণে অস্বীকৃতি জানিয়েছেন"),
    OTHER("Other Legitimate Reason", "অন্যান্য কারণ")
}

enum class DisputeStatus(val titleEn: String, val titleBn: String) {
    OPEN("Dispute Open", "বিতর্ক উন্মুক্ত"),
    SELLER_RESPONSE_REQUIRED("Seller Evidence Required", "বিক্রেতার প্রমাণ প্রয়োজন"),
    BUYER_RESPONSE_REQUIRED("Buyer Clarification Needed", "ক্রেতার ব্যাখ্যা প্রয়োজন"),
    UNDER_REVIEW("Platform Review in Progress", "প্ল্যাটফর্ম রিভিউ চলছে"),
    RESOLUTION_PENDING("Resolution Pending Approval", "সিদ্ধান্ত অনুমোদনাধীন"),
    RESOLVED_BUYER_REFUND("Resolved: Full Refund to Buyer", "সমাধান: ক্রেতাকে সম্পূর্ণ রিফান্ড"),
    RESOLVED_SELLER_RELEASE("Resolved: Payment Released to Seller", "সমাধান: বিক্রেতাকে ফান্ড রিলিজ"),
    RESOLVED_SPLIT("Resolved: Split / Delivery Charge Deducted", "সমাধান: ডেলিভারি খরচ সমন্বয়পূর্বক সমাধান")
}

enum class RiskLevel(val title: String) {
    LOW("LOW RISK"),
    MEDIUM("MEDIUM RISK"),
    HIGH("HIGH RISK"),
    CRITICAL("CRITICAL FRAUD ALERT")
}
