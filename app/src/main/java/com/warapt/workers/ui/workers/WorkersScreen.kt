package com.warapt.workers.ui.workers

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.warapt.workers.data.local.SessionManager
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkersScreen(
    viewModel: WorkersViewModel = viewModel(),
    onAddWorkerClick: () -> Unit,
    onLogout: () -> Unit
) {
    var dniFilter by remember { mutableStateOf("") }
    val workers by viewModel.workers
    val isLoading by viewModel.isLoading
    val errorMessage by viewModel.errorMessage
    val context = LocalContext.current

    // Se ejecuta UNA vez, al entrar a la pantalla (equivalente a ngOnInit)
    LaunchedEffect(Unit) {
        viewModel.loadWorkers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trabajadores") },
                actions = {
                    IconButton(onClick = {
                        SessionManager(context).clearSession()
                        onLogout()   // nuevo parámetro que navega a Login
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddWorkerClick) {
                Icon(Icons.Default.Add, contentDescription = "Agregar trabajador")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text("Trabajadores", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = dniFilter,
                onValueChange = {
                    dniFilter = it
                    viewModel.loadWorkers(it)   // filtra en vivo mientras escribes
                },
                label = { Text("Filtrar por DNI") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            LazyColumn {
                items(workers) { worker ->
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("${worker.firstName} ${worker.lastName}", style = MaterialTheme.typography.titleMedium)
                            Text("DNI: ${worker.dni}  •  Edad: ${worker.age}")
                        }
                    }
                }
            }
        }
    }
}