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
import androidx.hilt.navigation.compose.hiltViewModel
import com.lehtimaeki.askold.R
import com.lehtimaeki.askold.landingscreen.LandingScreenFragment
import com.lehtimaeki.askold.theme.AskoldTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileScreenFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileScreenFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val viewModel: ProfileScreenViewModel = hiltViewModel()
                AskoldTheme {
                    val name by viewModel.userName.collectAsState()
                    ProfileScreen(
                        name = name,
                        onNameChange = { viewModel.setUserName(it) },
                        saveData = { viewModel.getUserName() }
                    )
                }
            }
        }
    }

    @Composable
    fun ProfileScreen(name: String, onNameChange: (String) -> Unit, saveData: () -> Unit) {
        Box(modifier = Modifier.background(Color.White)) {
            Image(
                modifier = Modifier
                    .padding(bottom = 143.dp)
                    .fillMaxWidth(),
                painter = painterResource(R.drawable.bg),
                contentDescription = "background image",
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileScreenComponents()
                BabyNameText(name = name, onNameChange = onNameChange)
            }
            StartButton(
                name = name,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp), saveData = saveData
            )
        }
    }

    @Composable
    fun ProfileScreenComponents() {
        Image(
            modifier = Modifier
                .size(100.dp)
                .padding(top = 30.dp)
                .fillMaxWidth(),
            painter = painterResource(R.drawable.baby),
            contentDescription = "baby_image"
        )
        Text(
            text = "Toddler\n  Tiles",
            color = Color(0xFF5DDAD0),
            style = MaterialTheme.typography.h1,
            modifier = Modifier
                .padding(top = 16.dp, bottom = 30.dp),
        )
        Text(
            text = "What should we call you?",
            color = Color(0xFF666666),
            fontSize = 27.sp,
            fontWeight = FontWeight.SemiBold
        )
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
    fun StartButton(name: String, modifier: Modifier = Modifier, saveData: () -> Unit) {
        Button(
            shape = RoundedCornerShape(20.dp),
            enabled =  name != "",
            modifier = modifier
                .height(64.dp)
                .width(336.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF8674F5)),
            onClick = {
                saveData()
                navigateToLandingScreenActivity(name)
            }
        ) {
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