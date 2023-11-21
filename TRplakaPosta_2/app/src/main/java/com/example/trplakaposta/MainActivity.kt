package com.example.trplakaposta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.trplakaposta.data.DataSource
import com.example.trplakaposta.model.Province
import com.example.trplakaposta.ui.theme.TRplakaPostaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TRplakaPostaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProvincesApp()
                }
            }
        }
    }
}

@Composable
fun ProvincesApp() {
    // Use remember and mutableStateOf to create a state for the search query
    var searchQuery by remember { mutableStateOf("") }
    // Pass the search query to ProvincesList
    ProvincesList(provincesList = DataSource().loadProvinces(),
        searchQuery = searchQuery,
        onSearchQueryChange = { newQuery -> searchQuery = newQuery })
}

@Composable
fun ProvincesList(
    provincesList: List<Province>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(modifier) {
        // Add a TextField for search
        SearchBar(searchQuery = searchQuery, onSearchQueryChange = onSearchQueryChange)
        // Use LazyColum to display the filtered list
        LazyColumn(modifier = Modifier.weight(1f)) {
            val filteredList = if(searchQuery.isNotEmpty()) {
                provincesList.filter { province ->
                    //LocalContext.current.getString(province.provinceName).contains(searchQuery, ignoreCase = true)
                    context.getString(province.provinceName).contains(searchQuery, ignoreCase = true)
                }
            }else{
                provincesList
            }
            items(filteredList) {province ->
                ProvinceCard(province = province, modifier = Modifier.padding(8.dp))
            }
        }
    }
    /*
    LazyColumn(modifier = modifier) {
        items(provincesList) {province ->
            ProvinceCard(province = province, modifier = Modifier.padding(.8.dp))
        }
    }*/
}

@Composable
fun ProvinceCard(province: Province, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column {
            Text(
                text = LocalContext.current.getString(province.provinceName),
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(searchQuery: String, onSearchQueryChange: (String) -> Unit) {
    // Use TextField for search input
    TextField(
        value = searchQuery,
        onValueChange = { newQuery -> onSearchQueryChange(newQuery) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        label = { Text(text = "Listede ara:") }
    )
}
