package com.example.mycalculator.presentation

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mycalculator.R
import com.example.mycalculator.data.preferences.PreferenceProvider
import com.example.mycalculator.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var preferenceProvider: PreferenceProvider
    private lateinit var hashColors: HashMap<String, Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        Log.d("TAG", "onCreate: ")
        setContentView(binding.root)

        //Initializations
        hashColors = hashMapOf(
            RED to getColor(R.color.red),
            PURPLE to getColor(R.color.purple),
            BLUE to getColor(R.color.blue),
            GREEN to getColor(R.color.green),
            ORANGE to getColor(R.color.orange),
            BLACK to getColor(R.color.black),
        )
        preferenceProvider = PreferenceProvider(this)

        var selectedColor = preferenceProvider.getSelectedColor()
        if (selectedColor == null) selectedColor = GREEN
        changeTheme(selectedColor)

        //ViewModel
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    fun onSelectColorClicked(view: View) {
        val cardView = view as MaterialCardView
        val tag = cardView.tag.toString()
        changeTheme(tag)
    }

    fun onButtonClick(view: View) {
        val tag = view.tag.toString()
        val text = (view as Button).text.toString()

        viewModel.onClickButton(tag, text)
        if (tag == SPECIAL_OPERATOR_BUTTON)
            Toast.makeText(this@MainActivity, "$text is not implemented yet!", Toast.LENGTH_SHORT)
                .show()

    }

    private fun changeTheme(color: String) {
        preferenceProvider.saveSelectedColor(color)
        markSelectedTheme(color)

        if (color == BLACK) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            return
        }

        //Applying Theme
        val operatorBtns = getViewsByTag(binding.linearLayout, OPERATOR_BUTTON)
        operatorBtns.addAll(getViewsByTag(binding.linearLayout, CLEAR_BUTTON))
        operatorBtns.addAll(getViewsByTag(binding.linearLayout, ALL_CLEAR_BUTTON))
        val backSpaceBtns = getViewsByTag(binding.linearLayout, BACK_BUTTON)
        val equalBtns = getViewsByTag(binding.linearLayout, EQUAL_BUTTON)

        if (!isPortrait()) {
            operatorBtns.addAll(getViewsByTag(binding.linearLayout3!!, OPERATOR_BUTTON))
            operatorBtns.addAll(getViewsByTag(binding.linearLayout3!!, CLEAR_BUTTON))
            operatorBtns.addAll(getViewsByTag(binding.linearLayout3!!, ALL_CLEAR_BUTTON))
            equalBtns.addAll(getViewsByTag(binding.linearLayout3!!, EQUAL_BUTTON))
        }

        val colorCode = hashColors[color]!!
        operatorBtns.forEach {
            (it as Button).setTextColor(colorCode)
        }

        backSpaceBtns.forEach {
            (it as MaterialButton).iconTint = ColorStateList.valueOf(colorCode)
        }

        equalBtns.forEach {
            (it as MaterialButton).backgroundTintList = ColorStateList.valueOf(colorCode)
        }

        if (!theme.equals(R.style.OrangeTheme)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun markSelectedTheme(color: String) {
        //Mark Selected Theme
        val allViews = binding.colorPickerLayout.let { getViewsByTag(it, CHECK_MARK) }
        allViews.forEach { it.visibility = View.INVISIBLE }
        val circle = binding.colorPickerLayout.findViewWithTag<MaterialCardView>(color)
        val checkMark = circle?.findViewWithTag<ImageView>(CHECK_MARK)
        checkMark?.visibility = View.VISIBLE
    }

    private fun getViewsByTag(root: ViewGroup, tag: String): ArrayList<View> {
        val views = ArrayList<View>()
        val childCount = root.childCount
        for (i in 0 until childCount) {
            val child = root.getChildAt(i)
            if (child is ViewGroup) {
                views.addAll(getViewsByTag(child, tag))
            }
            val tagObj = child.tag
            if (tagObj != null && tagObj == tag) {
                views.add(child)
            }
        }
        return views
    }

    private fun isPortrait(): Boolean {
        val orientation = this.resources.configuration.orientation
        return orientation == Configuration.ORIENTATION_PORTRAIT
    }

    companion object {
        const val DIGIT_BUTTON = "digit_btn"
        const val OPERATOR_BUTTON = "operator_btn"
        const val SPECIAL_OPERATOR_BUTTON = "special_operator_btn"
        const val BACK_BUTTON = "back_btn"
        const val EQUAL_BUTTON = "equal_btn"
        const val CLEAR_BUTTON = "clear_btn"
        const val ALL_CLEAR_BUTTON = "all_clear_btn"

        const val RED = "red"
        const val PURPLE = "purple"
        const val BLUE = "blue"
        const val GREEN = "green"
        const val ORANGE = "orange"
        const val BLACK = "black"

        const val CHECK_MARK = "check_mark"
    }
}