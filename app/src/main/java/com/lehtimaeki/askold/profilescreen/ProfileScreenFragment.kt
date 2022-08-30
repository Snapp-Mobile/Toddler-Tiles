package com.lehtimaeki.askold.profilescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lehtimaeki.askold.R
import com.lehtimaeki.askold.landingscreen.LandingScreenFragment
import com.lehtimaeki.askold.theme.MyApplicationTheme


class ProfileScreenFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileScreenFragment()
    }

    private val viewModel: ProfileScreenViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MyApplicationTheme {
                    val name by viewModel.name.collectAsState()
                    ProfileScreen(name)
                }
            }
        }
    }

    @Composable
    fun ProfileScreen(name: String) {
        Box(modifier = Modifier.background(Color.White)) {
            Image(
                modifier = Modifier
                    .padding(bottom = 143.dp)
                    .fillMaxWidth(),
                painter = painterResource(R.drawable.bg),
                contentDescription = "background_image",
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(top = 30.dp)
                        .fillMaxWidth(),
                    painter = painterResource(R.drawable.baby),
                    contentDescription = "baby_image"
                )
                Text(
                    "Toddler\n  Tiles",
                    color = Color(0xFF5DDAD0),
                    style = MaterialTheme.typography.h1,
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 30.dp),
                )
                Text(
                    "What should we call you?",
                    color = Color(0xFF666666),
                    fontSize = 27.sp,
                    fontWeight = FontWeight.SemiBold
                )
                BabyNameText(name = name, onNameChange = { viewModel.setName(it)})
            }
            StartButton(
                name, modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
            )
        }
    }

    @Composable
    fun BabyNameText(name: String, onNameChange: (String) -> Unit) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            enabled = true,
            textStyle = TextStyle.Default.copy(fontSize = 24.sp),
            placeholder = {
                Text(
                    text = "Enter your name",
                    fontSize = 24.sp,
                    color = Color.LightGray,
                    modifier = Modifier.padding(start = 24.dp)
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color(0xFF5DDAD0),
                unfocusedBorderColor = Color(0xFF5DDAD0),
                backgroundColor = Color.White
            ),
            modifier = Modifier.size(width = 336.dp, height = 64.dp),
            shape = RoundedCornerShape(20.dp),
        )
    }

    @Composable
    fun StartButton(name: String, modifier: Modifier = Modifier) {
        Button(
            shape = RoundedCornerShape(20.dp),
            enabled =  name != "",
            modifier = modifier
                .height(64.dp)
                .width(336.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF8674F5)),
            onClick = {
                viewModel.saveData()
                navigateToLandingScreenActivity(name)
            }) {
            Text(
                text = "Let's start",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 27.sp,
            )
        }
    }

    private fun navigateToLandingScreenActivity(name: String) {

        activity?.supportFragmentManager?.beginTransaction()
            ?.add(R.id.container, LandingScreenFragment.newInstance(name))
            ?.addToBackStack(null)
            ?.commit()
    }
}