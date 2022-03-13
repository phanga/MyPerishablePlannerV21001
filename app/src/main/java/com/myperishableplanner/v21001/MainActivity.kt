package com.myperishableplanner.v21001

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.myperishableplanner.v21001.ui.theme.MyPerishablePlannerTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.myperishableplanner.v21001.dto.Category
import com.myperishableplanner.v21001.dto.Item

class MainActivity : ComponentActivity() {

    private var selectedCategory : Category? = null
    private val viewModel: ItemViewModel by viewModel<ItemViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.fetchItems()
            val items by viewModel.items.observeAsState(initial = emptyList())
            val categories = ArrayList<Category>()
            categories.add(Category(1,"Frozen","Below 45 degree"))
            categories.add(Category(2,"Canned goods","Away from direct Sunlight"))
            MyPerishablePlannerTheme {
                Column {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        color = MaterialTheme.colors.background,
                        modifier = Modifier.fillMaxWidth()

                    ) {
                        ExpirationFacts(items,categories)

                    }

                }
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        MyPerishablePlannerTheme {
            Column {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ExpirationFacts()
                }
            }
        }
    }

    var strSelectedItem = "No item selected"
    var selecteItem = Item(0, "", "")

    @Composable
    fun ButtonBar()
    {
        Row(modifier = Modifier.padding(all = 2.dp)) {
        }

        val context = LocalContext.current
        Button(
            onClick = {
                Toast.makeText(context, "Selected Country $strSelectedItem", Toast.LENGTH_LONG)
                    .show()
            },
            modifier = Modifier.padding(all = Dp(10F)),
            enabled = true,
            border = BorderStroke(width = 1.dp, brush = SolidColor(Color.Blue)),
            shape = MaterialTheme.shapes.medium,
        ) {
            Text(text = "Save", color = Color.White)
        }
    }
    @Composable
    fun ExpirationFacts(items: List<Item> = ArrayList<Item>(), Categories : List<Category> = ArrayList<Category>()) {
        var category by remember { mutableStateOf(value = "") }
        var expirationDate by remember { mutableStateOf(value = "") }
        var storageInstruction by remember { mutableStateOf(value = "") }
        val context = LocalContext.current
        Column {

            TextFieldWithDropdownUsage(itemsIn = items, stringResource(R.string.ItemName))
            CatogerySpinner(categories= Categories)
            //
            OutlinedTextField(
                value = storageInstruction,
                onValueChange = { storageInstruction = it },
                label = { Text(text = stringResource(R.string.StorageInstruction)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = expirationDate,
                onValueChange = { expirationDate = it },
                label = { Text(text = stringResource(R.string.expirationDate)) },
                modifier = Modifier.fillMaxWidth()
            )

            ButtonBar()

        }

    }

    private @Composable
    fun CatogerySpinner(categories: List<Category>) {
        var itemText by remember { mutableStateOf("Select Category")}
        var expanded by remember { mutableStateOf(false)}
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
            Row(Modifier
                .padding(6.dp)
                .clickable {
                    expanded = !expanded
                }
                .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Text(text=itemText, fontSize = 15.sp , modifier=Modifier.padding(end = 6.dp))
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
                DropdownMenu(expanded = expanded, onDismissRequest = {expanded = false}) {
                    categories.forEach{
                            category -> DropdownMenuItem(onClick = {
                        expanded = false
                        itemText = category.toString()
                        selectedCategory = category
                    })
                    {
                        Text (text = category.toString() )
                    }
                    }

                }
            }

        }
    }


    @Composable
    fun TextFieldWithDropdownUsage(itemsIn: List<Item>, label: String = "", take: Int = 10) {

        val dropDownOptions = remember { mutableStateOf(listOf<Item>()) }
        val textFieldValue = remember { mutableStateOf(TextFieldValue()) }
        val dropDownExpanded = remember { mutableStateOf(false) }

        fun onDropdownDismissRequest() {
            dropDownExpanded.value = false
        }

        fun onValueChanged(value: TextFieldValue) {
            strSelectedItem = value.text
            dropDownExpanded.value = true
            textFieldValue.value = value
            dropDownOptions.value = itemsIn.filter {
                it.toString().startsWith(value.text) && it.toString() != value.text
            }.take(10)
        }

        TextFieldWithDropdown(
            modifier = Modifier.fillMaxWidth(),
            value = textFieldValue.value,
            setValue = ::onValueChanged,
            onDismissRequest = ::onDropdownDismissRequest,
            dropDownExpanded = dropDownExpanded.value,
            list = dropDownOptions.value,
            label = label
        )
    }

    @Composable
    fun TextFieldWithDropdown(
        modifier: Modifier = Modifier,
        value: TextFieldValue,
        setValue: (TextFieldValue) -> Unit,
        onDismissRequest: () -> Unit,
        dropDownExpanded: Boolean,
        list: List<Item>,
        label: String = ""
    ) {
        Box(modifier) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (!focusState.isFocused)
                            onDismissRequest()
                    },
                value = value,
                onValueChange = setValue,
                label = { Text(label) },
                colors = TextFieldDefaults.outlinedTextFieldColors()
            )
            DropdownMenu(
                expanded = dropDownExpanded,
                properties = PopupProperties(
                    focusable = false,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                ),
                onDismissRequest = onDismissRequest
            ) {
                list.forEach { text ->
                    DropdownMenuItem(onClick = {
                        setValue(
                            TextFieldValue(
                                text.toString(),
                                TextRange(text.toString().length)
                            )
                        )
                    }) {
                        Text(text = text.toString())
                    }
                }
            }

        }



    }
}


