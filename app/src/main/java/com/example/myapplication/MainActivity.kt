package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val rates = mapOf(
        "USD" to 1.0,
        "EUR" to 0.92,
        "GBP" to 0.81,
        "JPY" to 150.0,
        "AUD" to 1.46,
        "CAD" to 1.34,
        "CHF" to 0.92,
        "CNY" to 6.91,
        "KRW" to 1360.0,
        "VND" to 23600.0
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                    CurrencyConverter(rates)
                }
            }
        }
    }
}

@Composable
fun CurrencyConverter(rates: Map<String, Double>) {
    var fromCurrency by remember { mutableStateOf("USD") }
    var toCurrency by remember { mutableStateOf("EUR") }
    var fromValue by remember { mutableStateOf("") }
    var toValue by remember { mutableStateOf("") }

    val currencyList = rates.keys.toList()
    var expandedFrom by remember { mutableStateOf(false) }
    var expandedTo by remember { mutableStateOf(false) }

    fun convertFrom() {
        val from = fromValue.toDoubleOrNull() ?: 0.0
        val result = from * (rates[toCurrency]!! / rates[fromCurrency]!!)
        toValue = if (result % 1.0 == 0.0) result.toInt().toString() else result.toString()
    }

    fun convertTo() {
        val to = toValue.toDoubleOrNull() ?: 0.0
        val result = to * (rates[fromCurrency]!! / rates[toCurrency]!!)
        fromValue = if (result % 1.0 == 0.0) result.toInt().toString() else result.toString()
    }

    Column(modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {

        // From currency
        Box {
            TextField(
                value = fromCurrency,
                onValueChange = {},
                readOnly = true,
                label = { Text("From currency") },
                modifier = Modifier.fillMaxWidth().clickable { expandedFrom = true }
            )
            DropdownMenu(
                expanded = expandedFrom,
                onDismissRequest = { expandedFrom = false }
            ) {
                currencyList.forEach { currency ->
                    DropdownMenuItem(
                        text = { Text(currency) },
                        onClick = {
                            fromCurrency = currency
                            expandedFrom = false
                            convertFrom()
                        }
                    )
                }
            }
        }

        TextField(
            value = fromValue,
            onValueChange = { fromValue = it; convertFrom() },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        // To currency
        Box {
            TextField(
                value = toCurrency,
                onValueChange = {},
                readOnly = true,
                label = { Text("To currency") },
                modifier = Modifier.fillMaxWidth().clickable { expandedTo = true }
            )
            DropdownMenu(
                expanded = expandedTo,
                onDismissRequest = { expandedTo = false }
            ) {
                currencyList.forEach { currency ->
                    DropdownMenuItem(
                        text = { Text(currency) },
                        onClick = {
                            toCurrency = currency
                            expandedTo = false
                            convertFrom()
                        }
                    )
                }
            }
        }

        TextField(
            value = toValue,
            onValueChange = { toValue = it; convertTo() },
            label = { Text("Converted amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
