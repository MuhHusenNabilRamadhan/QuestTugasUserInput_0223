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

    val ungu = colorResource(R.color.biodata_purple)
    val teks = colorResource(R.color.biodata_text)
    val garis = colorResource(R.color.biodata_input_border)
    val latar = colorResource(R.color.biodata_background)
    val nonaktif = colorResource(R.color.biodata_disabled)

    // Ambil teks dari resources sebelum masuk ke callback.
    val pesanEmail = stringResource(R.string.error_email)
    val pesanRequired = stringResource(R.string.error_required)
    val judulHasil = stringResource(R.string.result_title)

    val labelAndroid = stringResource(R.string.skill_android)
    val labelUiux = stringResource(R.string.skill_uiux)
    val labelWeb = stringResource(R.string.skill_web)

    val hasilNama = stringResource(
        R.string.result_name,
        namaDepan,
        namaBelakang
    )
    val hasilEmail = stringResource(
        R.string.result_email,
        email
    )
    val hasilTanggal = stringResource(
        R.string.result_birth_date,
        tanggalTampil
    )
    val hasilPengalaman = stringResource(
        R.string.result_experience,
        pengalaman
    )

    val daftarSkill = listOfNotNull(
        if (skillAndroid) labelAndroid else null,
        if (skillUiux) labelUiux else null,
        if (skillWeb) labelWeb else null
    ).joinToString()

    val hasilSkill = stringResource(
        R.string.result_skills,
        daftarSkill
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(latar)
    ) {
        val tinggiGambar = maxHeight *
                stringResource(R.string.header_height_fraction).toFloat()

        // Gambar header bagian atas.
        Image(
            painter = painterResource(R.drawable.header_biodata),
            contentDescription = stringResource(R.string.biodata_title),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(tinggiGambar)
                .align(Alignment.TopCenter)
        )

        // Kartu putih mengambang dan menimpa gambar.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(
                    top = tinggiGambar -
                            dimensionResource(R.dimen.card_overlap)
                )
                .clip(
                    RoundedCornerShape(
                        topStart = dimensionResource(
                            R.dimen.card_top_radius
                        ),
                        topEnd = dimensionResource(
                            R.dimen.card_top_radius
                        )
                    )
                )
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(dimensionResource(R.dimen.card_padding)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.biodata_title),
                color = ungu,
                fontSize = dimensionResource(
                    R.dimen.title_text_size
                ).value.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                Modifier.height(
                    dimensionResource(R.dimen.title_spacing)
                )
            )

            Text(
                text = stringResource(R.string.biodata_subtitle),
                color = colorResource(R.color.biodata_hint),
                fontSize = dimensionResource(
                    R.dimen.body_text_size
                ).value.sp
            )

            Spacer(
                Modifier.height(
                    dimensionResource(R.dimen.section_spacing)
                )
            )

            // Nama depan dan belakang.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.input_spacing)
                )
            ) {
                InputBiodata(
                    value = namaDepan,
                    onValueChange = { namaDepan = it },
                    label = stringResource(R.string.first_name),
                    modifier = Modifier.weight(1f)
                )

                InputBiodata(
                    value = namaBelakang,
                    onValueChange = { namaBelakang = it },
                    label = stringResource(R.string.last_name),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(
                Modifier.height(
                    dimensionResource(R.dimen.input_spacing)
                )
            )

            // Email memenuhi lebar kartu.
            InputBiodata(
                value = email,
                onValueChange = { email = it },
                label = stringResource(R.string.email),
                keyboard = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                Modifier.height(
                    dimensionResource(R.dimen.input_spacing)
                )
            )

            // Tanggal lahir dan experience dalam satu baris.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.input_spacing)
                )
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = tanggalTampil,
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        placeholder = {
                            Text(stringResource(R.string.birth_date))
                        },
                        trailingIcon = {
                            Text("▦", color = teks)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(
                            dimensionResource(R.dimen.input_corner)
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ungu,
                            unfocusedBorderColor = garis
                        )
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { pilihTanggal = true }
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = pengalaman,
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        placeholder = {
                            Text(stringResource(R.string.experience))
                        },
                        trailingIcon = {
                            Text("▼", color = teks)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(
                            dimensionResource(R.dimen.input_corner)
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ungu,
                            unfocusedBorderColor = garis
                        )
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { bukaDropdown = true }
                    )

                    DropdownMenu(
                        expanded = bukaDropdown,
                        onDismissRequest = {
                            bukaDropdown = false
                        }
                    ) {
                        pilihanPengalaman.forEach { pilihan ->
                            DropdownMenuItem(
                                text = { Text(pilihan) },
                                onClick = {
                                    pengalaman = pilihan
                                    bukaDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(
                Modifier.height(
                    dimensionResource(R.dimen.section_spacing)
                )
            )

            Text(
                text = stringResource(R.string.select_skill),
                modifier = Modifier.fillMaxWidth(),
                color = teks,
                fontSize = dimensionResource(
                    R.dimen.label_text_size
                ).value.sp,
                fontWeight = FontWeight.Bold
            )

            // Pilihan skill sejajar.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ItemSkill(
                    label = labelAndroid,
                    checked = skillAndroid,
                    onCheckedChange = { skillAndroid = it }
                )

                ItemSkill(
                    label = labelUiux,
                    checked = skillUiux,
                    onCheckedChange = { skillUiux = it }
                )

                ItemSkill(
                    label = labelWeb,
                    checked = skillWeb,
                    onCheckedChange = { skillWeb = it }
                )
            }

            Spacer(
                Modifier.height(
                    dimensionResource(R.dimen.section_spacing)
                )
            )

}
