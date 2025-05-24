package com.healthmate.app

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    private lateinit var ageInput: EditText
    private lateinit var weightInput: EditText
    private lateinit var bloodPressureInput: EditText
    private lateinit var analyzeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ageInput = findViewById(R.id.ageInput)
        weightInput = findViewById(R.id.weightInput)
        bloodPressureInput = findViewById(R.id.bloodPressureInput)
        analyzeButton = findViewById(R.id.analyzeButton)

        analyzeButton.setOnClickListener {
            analyzeBloodPressure()
        }
    }

    private fun analyzeBloodPressure() {
        val age = ageInput.text.toString().toIntOrNull()
        val weight = weightInput.text.toString().toDoubleOrNull()
        val bp = bloodPressureInput.text.toString()

        if (age == null || weight == null || !bp.contains("/")) {
            showResultDialog("Mohon lengkapi semua data dengan format yang benar.")
            return
        }

        val parts = bp.split("/")
        if (parts.size != 2) {
            showResultDialog("Format tekanan darah harus seperti 120/80.")
            return
        }

        val systolic = parts[0].trim().toIntOrNull()
        val diastolic = parts[1].trim().toIntOrNull()

        if (systolic == null || diastolic == null) {
            showResultDialog("Angka tekanan darah tidak valid.")
            return
        }

        val diagnosis = when {
            systolic >= 180 || diastolic >= 120 -> "⚠️ Krisis Hipertensi – segera cari bantuan medis!"
            systolic >= 140 || diastolic >= 90 -> "❗ Hipertensi Stage 2 – konsultasikan ke dokter."
            systolic >= 130 || diastolic >= 80 -> "⚠️ Hipertensi Stage 1 – ubah pola hidup & pantau rutin."
            systolic in 90..120 && diastolic in 60..80 -> "✅ Tekanan darah Anda normal."
            else -> "❓ Tekanan darah Anda di luar kisaran umum – konsultasikan dengan dokter."
        }

        val result = """
            Usia: $age tahun
            Berat Badan: $weight kg
            Tekanan Darah: $systolic/$diastolic mmHg

            📊 Hasil Analisis:
            $diagnosis

            Catatan: Ini bukan diagnosis medis resmi.
        """.trimIndent()

        showResultDialog(result)
    }

    private fun showResultDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Hasil Analisis")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
