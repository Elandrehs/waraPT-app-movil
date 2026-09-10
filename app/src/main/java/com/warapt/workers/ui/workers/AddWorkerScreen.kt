package com.warapt.workers.ui.workers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AddWorkerScreen(
    viewModel: AddWorkerViewModel = viewModel(),
    onWorkerAdded: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage
    val success by viewModel.success

    // Cuando el backend confirma que se creó, regresamos a la lista
    LaunchedEffect(success) {
        if (success) onWorkerAdded()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .padding(24.dp)
    ) {
        Text("Agregar Trabajador", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = firstName, onValueChange = { firstName = it },
            label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = lastName, onValueChange = { lastName = it },
            label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = dni, onValueChange = { dni = it },
            label = { Text("DNI") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = age, onValueChange = { age = it },
            label = { Text("Edad") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        (validationError ?: errorMessage)?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                val ageInt = age.toIntOrNull()
                validationError = when {
                    firstName.isBlank() || lastName.isBlank() || dni.isBlank() -> "Completa todos los campos"
                    ageInt == null -> "La edad debe ser un número"
                    else -> null
                }
                if (validationError == null) {
                    viewModel.addWorker(firstName, lastName, dni, ageInt!!)
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp))
            } else {
                Text("Guardar")
            }
        }
    }
}