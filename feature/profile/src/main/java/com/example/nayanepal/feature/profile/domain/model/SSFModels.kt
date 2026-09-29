package com.example.nayanepal.feature.profile.domain.model

data class SSFRecord(
    val contributionId: String,
    val employerName: String,
    val officeLocation: String,
    val month: String,
    val amount: Double,
    val contributionDate: Long,
    val status: String // "Verified", "Pending"
)

data class EmploymentStatus(
    val workplace: String,
    val officeLocation: String,
    val salary: Double,
    val position: String,
    val startDate: Long,
    val isVerified: Boolean,
    val isAbroad: Boolean,
    val abroadDetails: AbroadCompanyDetails? = null
)

data class AbroadCompanyDetails(
    val companyName: String,
    val country: String,
    val trustScore: Int, // 0-100
    val verificationStatus: String // "Verified", "Trusted", "Warning"
)
