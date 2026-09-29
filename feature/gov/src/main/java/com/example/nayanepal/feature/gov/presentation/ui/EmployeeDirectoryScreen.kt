package com.example.nayanepal.feature.gov.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.nayanepal.core.designsystem.theme.NayaNepalTheme
import com.example.nayanepal.feature.gov.domain.model.Employee
import com.example.nayanepal.feature.gov.presentation.viewmodel.EmployeeViewModel
import com.example.nayanepal.core.designsystem.R as DesignR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeDirectoryScreen(viewModel: EmployeeViewModel = hiltViewModel()) {
    val employees by viewModel.employees.collectAsState()
    val selectedEmployee by viewModel.selectedEmployee.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    
    NayaNepalTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("कर्मचारी विवरण (Employee Directory)", style = MaterialTheme.typography.titleLarge) },
                    actions = {
                        IconButton(onClick = { viewModel.showAddEmployeeDialog() }) {
                            Icon(
                                painter = painterResource(id = DesignR.drawable.ic_add_person),
                                contentDescription = "Add Employee"
                            )
                        }
                        IconButton(onClick = { viewModel.refreshData() }) {
                            Icon(
                                painter = painterResource(id = DesignR.drawable.ic_refresh),
                                contentDescription = "Refresh"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFDC143C),
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            },
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    // Search Bar
                    SearchBar(searchQuery, viewModel = viewModel)
                    
                    Spacer(modifier = Modifier.padding(vertical = 8.dp))
                    
                    // Employee List
                    if (employees.isEmpty()) {
                        EmptyStateCard()
                    } else {
                        LazyColumn {
                            items(employees) { employee ->
                                EmployeeListItem(
                                    employee = employee,
                                    onClick = { viewModel.selectEmployee(employee) }
                                )
                            }
                        }
                    }
                }
            }
        )
        
        // Employee Detail Dialog
        if (selectedEmployee != null) {
            EmployeeDetailDialog(
                employee = selectedEmployee!!,
                viewModel = viewModel,
                onDismiss = { viewModel.clearSelectedEmployee() }
            )
        }
    }
}

@Composable
fun SearchBar(
    searchQuery: String,
    viewModel: EmployeeViewModel
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = { viewModel.updateSearchQuery(it) },
        label = { Text("खोज्नुहोस् (Search by name/office)") },
        leadingIcon = {
            Icon(
                painter = painterResource(id = DesignR.drawable.ic_search),
                contentDescription = "Search"
            )
        },
        modifier = Modifier
            .fillMaxWidth(),
        singleLine = true
    )
}

@Composable
fun EmployeeListItem(
    employee: Employee,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (employee.employmentType == "abroad") 
                Color(0xFF003893).copy(alpha = 0.05f) 
            else 
                Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Avatar
                if (employee.profileImageUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(employee.profileImageUrl),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                } else {
                    Icon(
                        painter = painterResource(id = DesignR.drawable.ic_person_avatar),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        tint = Color.Gray
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column {
                    Text(
                        text = employee.fullName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = employee.designation ?: "No designation",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Employment Type Indicator
                    Text(
                        text = if (employee.employmentType == "abroad") 
                            "🌍 विदेशमा कार्यरत (Abroad)" 
                        else 
                            "🏢 नेपालमा कार्यरत (Domestic)",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (employee.employmentType == "abroad") 
                            Color(0xFF003893) 
                        else 
                            Color(0xFF00CC00)
                    )
                }
            }
            
            // Trust Badge (for abroad employees)
            if (employee.employmentType == "abroad") {
                val company = employee.abroadCompany
                if (company != null) {
                    TrustBadge(
                        isVerified = company.isVerified,
                        isTrusted = company.isTrusted,
                        trustScore = company.trustScore
                    )
                }
            }
        }
    }
}

@Composable
fun TrustBadge(
    isVerified: Boolean,
    isTrusted: Boolean,
    trustScore: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(start = 8.dp)
            .background(
                if (isVerified && isTrusted) Color(0xFF00CC00)
                else if (isVerified) Color(0xFF003893)
                else Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Icon(
            painter = painterResource(
                id = if (isVerified && isTrusted) DesignR.drawable.ic_star_trusted else DesignR.drawable.ic_shield_check
            ),
            contentDescription = "Trust Status",
            modifier = Modifier.size(20.dp),
            tint = Color.White
        )
        
        Text(
            text = "$trustScore%",
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun EmployeeDetailDialog(
    employee: Employee,
    viewModel: EmployeeViewModel,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = employee.fullName,
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFFDC143C)
            )
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                // Basic Info
                DetailRow(label = "नेपाली नाम (Nepali Name)", value = employee.nepaliName ?: "Not provided")
                DetailRow(label = "पद (Designation)", value = employee.designation ?: "Not provided")
                DetailRow(label = "विभाग (Department)", value = employee.department ?: "Not provided")
                DetailRow(label = "मोबाइल (Phone)", value = employee.phone)
                DetailRow(label = "ईमेल (Email)", value = employee.email ?: "Not provided")
                
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))
                
                if (employee.employmentType == "domestic") {
                    Text(
                        text = "🏢 कार्यालय विवरण (Office)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    employee.office?.let { office ->
                        DetailRow(label = "Office Name", value = office.officeName)
                        DetailRow(label = "Address", value = office.officeAddress ?: "Not provided")
                        DetailRow(label = "District", value = office.district ?: "Not provided")
                        DetailRow(label = "Region", value = office.region ?: "Not provided")
                    } ?: Text("No office information available", color = Color.Gray)
                } else {
                    Text(
                        text = "🌍 वैदेशिक कम्पनी (Abroad Company)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    employee.abroadCompany?.let { company ->
                        DetailRow(label = "Company Name", value = company.companyName)
                        DetailRow(label = "Country", value = company.country)
                        DetailRow(label = "Industry", value = company.industry ?: "Not provided")
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        TrustStatusRow("Verification:", if (company.isVerified) "✅ Verified" else "❌ Unverified")
                        TrustStatusRow("Trust Status:", if (company.isTrusted) "⭐ Trusted" else "⚠️ Not Trusted")
                        TrustStatusRow("Trust Score:", "${company.trustScore}/100")
                    } ?: Text("No company information available", color = Color.Gray)
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider()
                
                Text(
                    text = "📊 तलब विवरण (Salary)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                
                Button(
                    onClick = { viewModel.navigateToSalarySection(employee) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC143C))
                ) {
                    Text("तलब विवरण हेर्नुहोस् (View Salary Details)", color = Color.White)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("बन्द गर्नुहोस् (Close)")
            }
        }
    )
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        Text(text = value, style = MaterialTheme.typography.bodySmall, color = Color.Gray, modifier = Modifier.weight(1f))
    }
}

@Composable
fun TrustStatusRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun EmptyStateCard() {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(32.dp)
        ) {
            Icon(
                painter = painterResource(id = DesignR.drawable.ic_no_employees),
                contentDescription = "No employees",
                modifier = Modifier.size(80.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "कोही कर्मचारी फेला परेन", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
            Text(text = "No employees found", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
    }
}
