package com.myperishableplanner.v21001

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager.PERMISSION_GRANTED

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage

import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.myperishableplanner.v21001.dto.Category
import com.myperishableplanner.v21001.dto.Item
import com.myperishableplanner.v21001.dto.ItemDetail
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : ComponentActivity() {

    private var uri: Uri? = null
    private var currentImagePath: String = ""
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var selectedItem: Item ? = null
    private var selectedCategory : Category? = null
    private val viewModel: ItemViewModel by viewModel<ItemViewModel>()
    private var strUri by mutableStateOf("")
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

    var inItemName = "No item selected"
    var selecteItem = Item(0, "", "")

    @Composable
    fun SaveButton(inCategory : String, inDescription : String, inExpirationDate : String)
    {
        Row(modifier = Modifier.padding(all = 2.dp)) {
        }

        val context = LocalContext.current
        Button(
            onClick = {
                var itemDetail = ItemDetail().apply{
                    itemId =  selectedItem?.let { it.id }?:0
                    itemName = inItemName
                    description = inDescription
                    expirationDate = inExpirationDate
                    category = inCategory

                }
                viewModel.saveItemDetail(itemDetail)
                Toast.makeText(context, "Selected Product $inItemName", Toast.LENGTH_LONG)
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
    fun AuthenticationButton()
    {
        Row(modifier = Modifier.padding(all = 2.dp)) {
        }

        val context = LocalContext.current
        Button(
            onClick = {
                SignIn ()

            },
            modifier = Modifier.padding(all = Dp(10F)),
            enabled = true,
            border = BorderStroke(width = 1.dp, brush = SolidColor(Color.Blue)),
            shape = MaterialTheme.shapes.medium,
        ) {
            Text(text = "Logon", color = Color.White)
        }
    }
    @Composable
    fun PhotoButton() {


        Button(
            onClick = {
                TakePhoto()

            },
            modifier = Modifier.padding(all = Dp(10F)),
            enabled = true,
            border = BorderStroke(width = 1.dp, brush = SolidColor(Color.Blue)),
            shape = MaterialTheme.shapes.medium,
        ) {
            Text(text = "Photo", color = Color.White)
        }
        AsyncImage(model = strUri, contentDescrption = "Item Image")
    }

    private fun TakePhoto() {
        if (hasCameraPermission() == PERMISSION_GRANTED && hasExternalStoragePermission() == PERMISSION_GRANTED) {
        invokeCamera()
        } else {
            requestMultiplePermissionsLauncher.launch(arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ))
        }
    }
    private val requestMultiplePermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) {
        resultsMap ->
        var permissionGranted = false
        resultsMap.forEach {
            if (it.value == true) {
                permissionGranted = it.value
            } else {
                permissionGranted = false
                return@forEach
            }
        }
        if (permissionGranted) {
            invokeCamera()
        } else {
            Toast.makeText(this, getString(R.string.cameraPermissionDenied), Toast.LENGTH_LONG).show()
        }
    }
    private fun invokeCamera() {
        val file = createImageFile()
        try {


            uri = FileProvider.getUriForFile(
                this,
                "com.myperishableplanner.v21001.fileprovider",
                file
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
            var foo = e.message
        }
        getCameraImage.launch(uri)
    }

    private fun createImageFile() : File {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "Item_${timestamp}",
            ".jpg",
            imageDirectory
        ).apply{
            currentImagePath = absolutePath
        }
    }
    private val getCameraImage = registerForActivityResult(ActivityResultContracts.TakePicture()){
        success ->
        if (success) {
            Log.i(TAG, "Image Location : $uri")
            strUri = uri.toString()
        } else {
            Log.e(TAG, "Image not saved. $uri")
        }
    }
    fun hasCameraPermission() = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
    fun hasExternalStoragePermission() = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private fun SignIn() {
        val providers = arrayListOf(
           AuthUI.IdpConfig.EmailBuilder().build()
        )
        val signinIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()

        signInLauncher.launch(signinIntent)
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract())
    {

        res -> this.signInResult(res)
    }

    private fun signInResult (result : FirebaseAuthUIAuthenticationResult)
    {
       val response = result.idpResponse
       if (result.resultCode== RESULT_OK) {
         firebaseUser =  FirebaseAuth.getInstance().currentUser
       }
        else
       {
           Log.e("MainActivity.kt","Error logging in" + response?.error?.errorCode)
       }
    }


    @Composable
    fun ExpirationFacts(items: List<Item> = ArrayList<Item>(), Categories : List<Category> = ArrayList<Category>()) {
        var inCategory by remember { mutableStateOf(value = "") }
        var inDescription by remember { mutableStateOf(value = "") }
        var inExpirationDate by remember { mutableStateOf(value = "") }
        val context = LocalContext.current
        Column {

            TextFieldWithDropdownUsage(itemsIn = items, stringResource(R.string.ItemName))
                // CatogerySpinner(categories= Categories)
            //
            OutlinedTextField(
                value = inDescription,
                onValueChange = { inDescription = it },
                label = { Text(text = stringResource(R.string.description)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = inCategory,
                onValueChange = { inCategory = it },
                label = { Text(text = stringResource(R.string.category)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = inExpirationDate,
                onValueChange = { inExpirationDate = it },
                label = { Text(text = stringResource(R.string.expirationDate)) },
                modifier = Modifier.fillMaxWidth()
            )

            SaveButton(inCategory,inDescription,inExpirationDate)
            AuthenticationButton()

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
            inItemName = value.text
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
            label = "Item"
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
                list.forEach { item ->
                    DropdownMenuItem(onClick = {
                        setValue(
                            TextFieldValue(
                                item.toString(),
                                TextRange(item.toString().length)
                            )
                        )
                        selectedItem = item
                    }) {
                        Text(text = item.toString())
                    }
                }
            }

        }



    }
}


