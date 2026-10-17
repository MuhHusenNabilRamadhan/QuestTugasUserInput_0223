package com.example.prak5

import android.util.Patterns as AndroidPatterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiodataScreen(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val snackbarHost = remember { SnackbarHostState() }

    var namaDepan by remember { mutableStateOf("") }
    var namaBelakang by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var tanggalLahir by remember { mutableStateOf<Long?>(null) }
    var pengalaman by remember { mutableStateOf("") }

    var pilihTanggal by remember { mutableStateOf(false) }
    var bukaDropdown by remember { mutableStateOf(false) }

    var skillAndroid by remember { mutableStateOf(false) }
    var skillUiux by remember { mutableStateOf(false) }
    var skillWeb by remember { mutableStateOf(false) }

    val pilihanPengalaman =
        stringArrayResource(R.array.experience_options)

    val tanggalTampil = tanggalLahir?.let { millis ->
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date(millis))
    }.orEmpty()

    val emailValid = email.isNotBlank() &&
            AndroidPatterns.EMAIL_ADDRESS
                .matcher(email.trim())
                .matches()

    val formValid = namaDepan.isNotBlank() &&
            namaBelakang.isNotBlank() &&
            emailValid &&
            tanggalLahir != null &&
            pengalaman.isNotBlank() &&
            (skillAndroid || skillUiux || skillWeb)

    
}
