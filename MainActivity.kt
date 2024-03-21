package com.example.tipcalculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    private val people = arrayOf(1, 2, 3, 4, 5, 6)
    private var currentTipPercentage = 15 // Default tip percentage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupTipCalculation()
    }

    private fun ComponentActivity.setupTipCalculation() {
        val spinner: Spinner = findViewById(R.id.tipPercentageSpinner)
        val seekBar: SeekBar = findViewById(R.id.seekBar)
        val baseAmountEditText: EditText = findViewById(R.id.base_amount)
        val tipPercentageTextView: TextView = findViewById(R.id.tipPercentageTextView)

        // Initialize SeekBar and Spinner
        seekBar.max = 30
        seekBar.progress = currentTipPercentage
        tipPercentageTextView.text = getString(R.string.tip_percentage, currentTipPercentage)

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, people)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        // Listeners for SeekBar and Spinner
        setupSeekBarListener(seekBar, tipPercentageTextView)
        setupSpinnerListener(spinner)

        // TextWatcher for the EditText
        baseAmountEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculateAndDisplayTotal()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        calculateAndDisplayTotal()
    }

    private fun setupSeekBarListener(seekBar: SeekBar, tipPercentageTextView: TextView) {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                currentTipPercentage = progress
                tipPercentageTextView.text = getString(R.string.tip_percentage, currentTipPercentage)
                calculateAndDisplayTotal()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun setupSpinnerListener(spinner: Spinner) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                calculateAndDisplayTotal()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                calculateAndDisplayTotal()
            }
        }
    }

    private fun calculateAndDisplayTotal() {
        val baseAmountEditText: EditText = findViewById(R.id.base_amount)
        val tipTextView: TextView = findViewById(R.id.tip)
        val totalTextView: TextView = findViewById(R.id.total)
        val spinner: Spinner = findViewById(R.id.tipPercentageSpinner)

        val baseAmount = baseAmountEditText.text.toString().toDoubleOrNull() ?: 0.0
        val tipAmount = baseAmount * (currentTipPercentage / 100.0)
        val totalAmount = baseAmount + tipAmount
        val numberOfPeople = spinner.selectedItem?.toString()?.toIntOrNull() ?: 1

        tipTextView.text = getString(R.string.tip_amount, tipAmount)
        totalTextView.text = getString(R.string.total_amount, totalAmount / numberOfPeople)
    }
}
