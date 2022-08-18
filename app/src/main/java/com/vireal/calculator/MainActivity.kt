package com.vireal.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private var resultItem: TextView? = null
    private var isDotUsed: Boolean = false
    private var isLastElementNumber = false
    private var isLastElementDot = false
    private var previousAction: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultItem = findViewById<TextView>(R.id.result)
    }

    fun onClickOnDigit(view: View){
        val digit = view as Button
        if(digit.text != "0" && resultItem?.text.toString() == "0"){
            resultItem?.text = digit.text
        } else {
            resultItem?.append(digit.text)
            isLastElementNumber = true
        }
    }

    fun onClear (view: View){
        resultItem?.text = ""
        isDotUsed = false
        isLastElementNumber = false
    }

    fun onDecimalPoint(view: View){
        if(isDotUsed === true){
        } else if(isLastElementNumber === false){
            resultItem?.append("0.")
        } else {
            resultItem?.append(".")
        }
        isLastElementNumber = false
        isDotUsed = true
        isLastElementDot = true
    }

    fun onAction(view: View){
        val action = view as Button
        if (isLastElementNumber) {
            println(isLastElementNumber)
            if (previousAction != null) {
                calculateAction(resultItem?.text.toString(), action)
                previousAction = action.text as String
            } else {
                resultItem?.append(action.text)
                previousAction = action.text as String?
            }
            isLastElementNumber = false
        } else {
            resultItem?.text = resultItem?.text?.dropLast(1)
            resultItem?.append(action.text)
            previousAction = action.text as String?
        }
    }

    private fun calculateAction(statementText:String, action: Button){
        var actionText = action.text as String
        var firstNumberMultiplier: Int = 1
        var changedStatementText = statementText
        if(statementText[0] == '-') {
            // Multiplier solve problem when first number is negative and action is subtract
            firstNumberMultiplier = -1
            changedStatementText = statementText.drop(1)
        }
        var (a,b) = changedStatementText.split(previousAction!!)
        var firstNumber = a.toFloat() * firstNumberMultiplier
        var secondNumber = b.toFloat()
        var actionTextToAdd = actionText
        when (previousAction) {
            "/" -> {
                if(secondNumber === 0.toFloat()) resultItem?.text = "0" else {
                    resultItem?.text = (firstNumber / secondNumber).toString()
                }
            }
            "*" -> resultItem?.text = (firstNumber * secondNumber).toString()
            "+" -> resultItem?.text = (firstNumber + secondNumber).toString()
            "-" -> resultItem?.text = (firstNumber - secondNumber).toString()
            else -> {
                calculateAction(statementText, action)
            }
        }
        isDotUsed = false
        if(actionText != "="){
            resultItem?.append(actionTextToAdd)
        }
    }

    fun onTapOnEqual(view: View){
        val action = view as Button
        if(resultItem?.text.toString() != "" && previousAction != null &&
            resultItem?.text.toString().split(previousAction!!).size > 1){
            calculateAction(resultItem?.text.toString(), action)
            isLastElementNumber = true
            previousAction = null
            isDotUsed = false
        }
    }
}