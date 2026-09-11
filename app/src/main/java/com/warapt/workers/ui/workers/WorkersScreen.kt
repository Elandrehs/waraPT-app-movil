package com.warapt.workers.ui.workers

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.warapt.workers.data.local.SessionManager

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

    // para mostrar el nombre del usuario en la pantalla de workers
    val sessionManager = remember { SessionManager(context) }
    val username = sessionManager.getUsername() ?: "usuario"

    // Se ejecuta UNA vez, al entrar a la pantalla
    LaunchedEffect(Unit) {
        viewModel.loadWorkers()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bienvenido, $username!!") },
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
        floatingActionButtonPosition = FabPosition.Center,
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
                .imePadding()            // empuja cuando se abre el teclado
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = dniFilter,
                onValueChange = {
                    dniFilter = it
                    viewModel.loadWorkers(it)   // filtra en vivo mientras se escribe
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

            // scroll de lista
            // el último ítem no quede tapado por el botón "+", y un
            // difuminado detrás del FAB para que se vea prolijo al hacer scroll.
            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
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

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(90.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.background.copy(alpha = 0f),
                                    MaterialTheme.colorScheme.background
                                )
                            )
                        )
                )
            }
        }
    }
}