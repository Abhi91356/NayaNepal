package com.example.nayanepal.feature.gov.domain.model

data class Employee(
    val id: String,
    val userId: String,
    val fullName: String,
    val email: String? = null,
    val phone: String,
    val nepaliName: String? = null,
    val profileImageUrl: String? = null,
    val designation: String? = null,
    val department: String? = null,
    val employmentType: String, // "domestic" or "abroad"
    val isVerified: Boolean = false,
    val office: Office? = null,
    val abroadCompany: AbroadCompany? = null
)

data class Office(
    val id: String,
    val officeName: String,
    val officeAddress: String? = null,
    val officePhone: String? = null,
    val district: String? = null,
    val region: String? = null,
    val officeType: String? = null,
    val floorNumber: Int? = null,
    val buildingName: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)

data class AbroadCompany(
    val id: String,
    val companyName: String,
    val companyAddress: String? = null,
    val country: String,
    val companyPhone: String? = null,
    val companyWebsite: String? = null,
    val industry: String? = null,
    val isVerified: Boolean = false,
    val isTrusted: Boolean = false,
    val trustScore: Int = 0,
    val verificationSource: String? = null,
    val verificationDate: Long? = null
)
