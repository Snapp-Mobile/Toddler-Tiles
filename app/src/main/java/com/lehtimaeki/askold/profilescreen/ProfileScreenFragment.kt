package com.lehtimaeki.askold.profilescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.lehtimaeki.askold.WindowInfo
import com.lehtimaeki.askold.landingscreen.LandingScreenFragment
import com.lehtimaeki.askold.rememberWindowInfo
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
                        saveData = { viewModel.saveUserName() }
                    )
                }
            }
        }
    }

    @Composable
    fun ProfileScreen(name: String, onNameChange: (String) -> Unit, saveData: () -> Unit) {
        val windowInfo = rememberWindowInfo()
        if (windowInfo.screenWidthInfo is WindowInfo.WindowType.Compact) {
            Box(modifier = Modifier.background(Color.White)) {
                Image(
                    modifier = Modifier
                        .padding(bottom = 130.dp)
                        .fillMaxSize(),
                    painter = painterResource(R.drawable.bg),
                    contentDescription = "background image"
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ProfileScreenComponents(imageSize = 100, titleSize = 40, textSize = 27)
                    BabyNameText(
                        name = name,
                        widthSize = 336,
                        heightSize = 64,
                        textSize = 24,
                        roundedSize = 20,
                        onNameChange = onNameChange
                    )
                }
                StartButton(
                    name = name,
                    roundedSize = 20,
                    textSize = 27,
                    widthSize = 336,
                    heightSize = 64,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp), saveData = saveData
                )
            }
        } else {
            Box(modifier = Modifier.background(Color.White)) {
                Image(
                    modifier = Modifier
                        .size(1500.dp)
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
                    ProfileScreenComponents(imageSize = 180, titleSize = 72, textSize = 48)
                    BabyNameText(
                        name = name,
                        widthSize = 600,
                        heightSize = 100,
                        textSize = 40,
                        roundedSize = 40,
                        onNameChange = onNameChange
                    )
                }
                StartButton(
                    name = name,
                    roundedSize = 40,
                    textSize = 48,
                    widthSize = 600,
                    heightSize = 100,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp), saveData = saveData
                )
            }
        }
    }

    @Composable
    fun ProfileScreenComponents(imageSize: Int, textSize: Int, titleSize: Int) {
        Image(
            modifier = Modifier
                .size(imageSize.dp)
                .padding(top = 30.dp)
                .fillMaxWidth(),
            painter = painterResource(R.drawable.baby),
            contentDescription = "baby_image"
        )
        Text(
            text = "Toddler\n  Tiles",
            color = Color(0xFF5DDAD0),
            fontSize = titleSize.sp,
            style = MaterialTheme.typography.h1,
            modifier = Modifier
                .padding(top = 16.dp, bottom = 30.dp),
        )
        Text(
            text = "What should we call you?",
            color = Color(0xFF666666),
            fontSize = textSize.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(bottom = 15.dp),
        )
    }

    @Composable
    fun BabyNameText(
        name: String,
        textSize: Int,
        widthSize: Int,
        heightSize: Int,
        roundedSize: Int,
        onNameChange: (String) -> Unit
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            enabled = true,
            textStyle = TextStyle.Default.copy(fontSize = textSize.sp),
            placeholder = {
                Text(
                    text = "Enter your name",
                    fontSize = textSize.sp,
                    color = Color.LightGray,
                    modifier = Modifier.padding(start = 24.dp)
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White
            ),
            modifier = Modifier
                .size(width = widthSize.dp, height = heightSize.dp)
                .padding(start = 12.dp, end = 12.dp)
                .border(
                    width = 3.dp,
                    color = Color(0xFF5DDAD0),
                    shape = RoundedCornerShape(roundedSize.dp)
                ),
            shape = RoundedCornerShape(roundedSize.dp),
        )
    }

    @Composable
    fun StartButton(
        name: String,
        roundedSize: Int,
        textSize: Int,
        widthSize: Int,
        heightSize: Int,
        modifier: Modifier = Modifier,
        saveData: () -> Unit
    ) {
        Button(
            shape = RoundedCornerShape(roundedSize.dp),
            enabled = name != "",
            modifier = modifier
                .height(heightSize.dp)
                .width(widthSize.dp)
                .padding(start = 12.dp, end = 12.dp),
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
                fontSize = textSize.sp,
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