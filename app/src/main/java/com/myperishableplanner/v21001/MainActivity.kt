package com.myperishableplanner.v21001

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myperishableplanner.v21001.ui.theme.MyPerishablePlannerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyPerishablePlannerTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background , modifier = Modifier.fillMaxWidth()) {
                    ExpirationFacts()
                }
            }
        }
    }
}

@Composable
fun ExpirationFacts () {
    var itemName by remember { mutableStateOf(value = "")}
    var category by remember { mutableStateOf(value = "")}
    var expirationDate by remember { mutableStateOf(value = "")}
    var description by remember { mutableStateOf(value = "")}
    val context = LocalContext.current

    Column {
        OutlinedTextField(
            value = itemName,
            onValueChange = { itemName = it },
            label = { Text(text = stringResource(R.string.itemName)) },
            modifier = Modifier.fillMaxWidth().padding(8.dp)

        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(text = stringResource(R.string.description)) },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text(text = stringResource(R.string.category)) },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )


        OutlinedTextField(
            value = expirationDate,
            onValueChange = { expirationDate = it },
            label = { Text(text = stringResource(R.string.expirationDate)) },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        Button(
            onClick = {
                Toast.makeText(context, "$itemName $description $category $expirationDate", Toast.LENGTH_LONG)
                    .show()
            },content = {Text(text = "save")}, modifier = Modifier.padding(8.dp))

    }


}

@Preview(name="Light mode")
@Preview(uiMode=Configuration.UI_MODE_NIGHT_YES,showBackground = true, name = "Dark Mode")
@Composable
fun DefaultPreview() {
    MyPerishablePlannerTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background , modifier = Modifier.fillMaxWidth()) {
            ExpirationFacts()
        }
    }
}