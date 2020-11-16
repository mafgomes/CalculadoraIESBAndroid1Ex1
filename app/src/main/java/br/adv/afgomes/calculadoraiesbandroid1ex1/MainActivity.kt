/*
 * Calculadora
 * Pós-graduação IESB em Desenvolvimento de Apps para plataformas móveis
 * Android 1 - Exercício 1
 * Aluno: Marcelo Amarante Ferreira Gomes (2086332021)
 */
package br.adv.afgomes.calculadoraiesbandroid1ex1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Math.pow
import java.lang.Math.abs
import java.lang.Math.sin
import java.lang.Math.cos
import java.lang.Math.tan
import java.lang.Math.asin
import java.lang.Math.acos
import java.lang.Math.atan
import java.lang.Math.sqrt
import java.lang.Math.log10
import java.lang.Math.log
import kotlin.math.*

class MainActivity : AppCompatActivity() {
    val constPi: Double = 3.14159265358979
    val constEuler: Double = 2.718281828459
    var previousNumber: Double = 0.0
    var currentNumber: Double = 0.0
    var currentFrac: Double = 1.0
    var currentOp: String = ""
    var currentDisplayString: String = "0"
    var typingNumber: Boolean = true
    val tabBtDispatch = hashMapOf<String, () -> Unit>()
    val tabOpDispatch = hashMapOf<String, (Double, Double) -> Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Digit buttons click listeners
        tvButton0.setOnClickListener() { btDigitPressed(0) }
        tvButton1.setOnClickListener() { btDigitPressed(1) }
        tvButton2.setOnClickListener() { btDigitPressed(2) }
        tvButton3.setOnClickListener() { btDigitPressed(3) }
        tvButton4.setOnClickListener() { btDigitPressed(4) }
        tvButton5.setOnClickListener() { btDigitPressed(5) }
        tvButton6.setOnClickListener() { btDigitPressed(6) }
        tvButton7.setOnClickListener() { btDigitPressed(7) }
        tvButton8.setOnClickListener() { btDigitPressed(8) }
        tvButton9.setOnClickListener() { btDigitPressed(9) }

        // Other (non-digit) buttons click listeners
        tvButtonBack.setOnClickListener() { texto ->
            dispatchButtonPress((texto as TextView).text.toString())
        }
        tvButtonDecimalPoint.setOnClickListener() { texto ->
            dispatchButtonPress((texto as TextView).text.toString())
        }
        tvButtonPiNumber.setOnClickListener() { texto ->
            dispatchButtonPress((texto as TextView).text.toString())
        }
        tvButtonEulerNumber.setOnClickListener() { texto ->
            dispatchButtonPress((texto as TextView).text.toString())
        }
        tvButtonAdd.setOnClickListener() { texto ->
            dispatchButtonPress((texto as TextView).text.toString())
        }
        tvButtonSubtract.setOnClickListener() { texto ->
            dispatchButtonPress((texto as TextView).text.toString())
        }
        tvButtonMultiply.setOnClickListener() { texto ->
            dispatchButtonPress((texto as TextView).text.toString())
        }
        tvButtonDivide.setOnClickListener() { texto ->
            dispatchButtonPress((texto as TextView).text.toString())
        }
        tvButtonEqualsTo.setOnClickListener() { texto ->
            dispatchButtonPress((texto as TextView).text.toString())
        }
        tvButtonClear.setOnClickListener() { texto ->
            dispatchButtonPress((texto as TextView).text.toString())
        }
        tvButtonModulus.setOnClickListener() { texto ->
            dispatchButtonPress((texto as TextView).text.toString())
        }
        tvButtonSignChg.setOnClickListener() { texto ->
            dispatchButtonPress((texto as TextView).text.toString())
        }
        tvButtonSqrt.setOnClickListener() { texto ->
            dispatchButtonPress((texto as TextView).text.toString())
        }
        tvButtonPower.setOnClickListener() { texto ->
            dispatchButtonPress((texto as TextView).text.toString())
        }
        tvButtonSquare.setOnClickListener() { texto ->
            dispatchButtonPress((texto as TextView).text.toString())
        }
        tvButtonSin.setOnClickListener() { texto ->
            dispatchButtonPress((texto as TextView).text.toString())
        }
        tvButtonCos.setOnClickListener() { texto ->
            dispatchButtonPress((texto as TextView).text.toString())
        }
        tvButtonTan.setOnClickListener() { texto ->
            dispatchButtonPress((texto as TextView).text.toString())
        }
        tvButtonInverse.setOnClickListener() { texto ->
            dispatchButtonPress((texto as TextView).text.toString())
        }
        tvButtonLog.setOnClickListener() { texto ->
            dispatchButtonPress((texto as TextView).text.toString())
        }
        tvButtonAsin.setOnClickListener() { texto ->
            dispatchButtonPress((texto as TextView).text.toString())
        }
        tvButtonAcos.setOnClickListener() { texto ->
            dispatchButtonPress((texto as TextView).text.toString())
        }
        tvButtonAtan.setOnClickListener() { texto ->
            dispatchButtonPress((texto as TextView).text.toString())
        }
        tvButtonFactorial.setOnClickListener() { texto ->
            dispatchButtonPress((texto as TextView).text.toString())
        }
        tvButtonLogNeperian.setOnClickListener() { texto ->
            dispatchButtonPress((texto as TextView).text.toString())
        }

        // Buttons dispatch table for non-digit buttons
        tabBtDispatch[getString(R.string.buttonBack)]           = ::btBackPressed
        tabBtDispatch[getString(R.string.buttonEqualsTo)]       = ::btEqualsPressed
        tabBtDispatch[getString(R.string.buttonAdd)]            = ::btAddPressed
        tabBtDispatch[getString(R.string.buttonSubtract)]       = ::btSubtractPressed
        tabBtDispatch[getString(R.string.buttonMultiply)]       = ::btMultiplyPressed
        tabBtDispatch[getString(R.string.buttonDivide)]         = ::btDividePressed
        tabBtDispatch[getString(R.string.buttonClear)]          = ::btClearPressed
        tabBtDispatch[getString(R.string.buttonDecimalPoint)]   = ::btDecimalPointPressed
        tabBtDispatch[getString(R.string.buttonPi)]             = ::btPiPressed
        tabBtDispatch[getString(R.string.buttonEulerNumber)]    = ::btEulerPressed
        tabBtDispatch[getString(R.string.buttonModulus)]        = ::btModulusPressed
        tabBtDispatch[getString(R.string.buttonSignChg)]        = ::btSignChgPressed
        tabBtDispatch[getString(R.string.buttonSqrt)]           = ::btSqrtPressed
        tabBtDispatch[getString(R.string.buttonPower)]          = ::btPowerPressed
        tabBtDispatch[getString(R.string.buttonSquare)]         = ::btSquarePressed
        tabBtDispatch[getString(R.string.buttonSin)]            = ::btSinPressed
        tabBtDispatch[getString(R.string.buttonCos)]            = ::btCosPressed
        tabBtDispatch[getString(R.string.buttonTan)]            = ::btTanPressed
        tabBtDispatch[getString(R.string.buttonInverse)]        = ::btInversePressed
        tabBtDispatch[getString(R.string.buttonLog)]            = ::btLogPressed
        tabBtDispatch[getString(R.string.buttonAsin)]           = ::btAsinPressed
        tabBtDispatch[getString(R.string.buttonAcos)]           = ::btAcosPressed
        tabBtDispatch[getString(R.string.buttonAtan)]           = ::btAtanPressed
        tabBtDispatch[getString(R.string.buttonFactorial)]      = ::btFactorialPressed
        tabBtDispatch[getString(R.string.buttonLn)]             = ::btLogNeperianPressed

        // Operations dispatch table
        // used to perform the mathematical operation associated with the button
        tabOpDispatch[""] = ::opNone
        tabOpDispatch[getString(R.string.buttonAdd)]        = ::opAdd
        tabOpDispatch[getString(R.string.buttonSubtract)]   = ::opSubtract
        tabOpDispatch[getString(R.string.buttonMultiply)]   = ::opMultiply
        tabOpDispatch[getString(R.string.buttonDivide)]     = ::opDivide
        tabOpDispatch[getString(R.string.buttonPower)]      = ::opPower
    }

    // Dispatcher function for non-digit buttons
    fun dispatchButtonPress(which: String) {
        Log.d("Digito", "Botão pressionado: $which")
        Log.d("Digito", "Prev Num:  $previousNumber")
        Log.d("Digito", "Curr Num:  $currentNumber")
        Log.d("Digito", "Curr Frac: $currentFrac")
        Log.d("Digito", "Curr Op:   '$currentOp'")
        Log.d("Digito", "Curr Str:  '$currentDisplayString'")

        tabBtDispatch[which]?.let { it() }
        updateCalculatorDisplay()
    }

    // Generic function for digit buttons (no need to dispatch)
    private fun btDigitPressed(digito: Int) {
        Log.d("Digito", "Valor do botão: $digito")

        if (currentFrac - 1.0 > -0.01) {
            // Esse "if" é necessário!!!
            // Ele evita que se "continue" a digitar sobre um resultado de operação anterior
            if (typingNumber) {
                currentNumber *= 10.0
                currentNumber += digito.toDouble()
            } else {
                currentNumber = digito.toDouble()
            }
            currentFrac = 1.0
        } else {
            currentNumber += currentFrac * digito.toDouble()
            currentFrac /= 10.0
        }
        typingNumber = true
        updateCalculatorDisplay()
    }

    // Handler functions for non-digit, non-math-function buttons
    fun btBackPressed() {
        if (typingNumber) {
            if (currentFrac - 1.0 > -0.1) {
                currentFrac = 1.0
                currentNumber = truncate(currentNumber / 10.0)
            } else {
                currentFrac *= 10.0
                currentNumber = 10.0 * truncate(currentNumber / (10 * currentFrac)) * currentFrac
            }
            if (currentFrac - 0.1 > -0.01) {
                currentFrac = 1.0
            }
        } else {
            currentNumber = previousNumber
        }
    }

    fun btDecimalPointPressed() {
        currentFrac = 0.1
        if (!typingNumber) {
            currentNumber = 0.0
        }
        typingNumber = true
    }

    fun btClearPressed() {
        currentNumber = 0.0
        previousNumber = 0.0
        currentFrac = 1.0
        typingNumber = true
    }

    fun btPiPressed() {
        currentNumber = constPi
        // previousNumber = 0.0
        currentFrac = 1.0
        typingNumber = false
    }

    fun btEulerPressed() {
        currentNumber = constEuler
        // previousNumber = 0.0
        currentFrac = 1.0
        typingNumber = false
    }

    fun btEqualsPressed() {
        currentNumber = tabOpDispatch[currentOp]?.invoke(previousNumber, currentNumber) ?: 0.0
        previousNumber = 0.0
        currentFrac = 1.0
        currentOp = ""
        typingNumber = false
    }

    // Handler functions for the Math operation buttons
    fun btAddPressed() {
        Log.d("Digito", "Apertado o botão de SOMA!")
        previousNumber = currentNumber
        currentNumber = 0.0
        currentFrac = 1.0
        currentOp = getString(R.string.buttonAdd)
        typingNumber = true
    }

    fun btSubtractPressed() {
        Log.d("Digito", "Apertado o botão de SUBTRAÇÃO!")
        previousNumber = currentNumber
        currentNumber = 0.0
        currentFrac = 1.0
        currentOp = getString(R.string.buttonSubtract)
        typingNumber = true
    }

    fun btMultiplyPressed() {
        Log.d("Digito", "Apertado o botão de MULTIPLICAÇÃO!")
        previousNumber = currentNumber
        currentNumber = 0.0
        currentFrac = 1.0
        currentOp = getString(R.string.buttonMultiply)
        typingNumber = true
    }

    fun btDividePressed() {
        Log.d("Digito", "Apertado o botão de DIVISÃO!")
        previousNumber = currentNumber
        currentNumber = 0.0
        currentFrac = 1.0
        currentOp = getString(R.string.buttonDivide)
        typingNumber = true
    }

    fun btModulusPressed() {
        Log.d("Digito", "Apertado o botão de MÓDULO!")
        currentNumber = abs(currentNumber)
        previousNumber = 0.0
        currentFrac = 1.0
        currentOp = ""
        typingNumber = false
    }

    fun btSignChgPressed() {
        Log.d("Digito", "Apertado o botão de MUDAR SINAL!")
        currentNumber = -currentNumber
        previousNumber = 0.0
        currentFrac = 1.0
        currentOp = ""
        typingNumber = false
    }

    fun btSqrtPressed() {
        Log.d("Digito", "Apertado o botão de RAIZ QUADRADA!")
        currentNumber = sqrt(currentNumber)
        previousNumber = currentNumber
        currentFrac = 1.0
        currentOp = ""
        typingNumber = false
    }

    fun btPowerPressed() {
        Log.d("Digito", "Apertado o botão de POTENCIAÇÃO!")
        previousNumber = currentNumber
        currentNumber = 0.0
        currentFrac = 1.0
        currentOp = getString(R.string.buttonPower)
        typingNumber = true
    }

    fun btSquarePressed() {
        Log.d("Digito", "Apertado o botão de ELEVAR AO QUADRADO!")
        currentNumber = pow(currentNumber, 2.0)
        previousNumber = currentNumber
        currentFrac = 1.0
        currentOp = ""
        typingNumber = false
    }

    fun btSinPressed() {
        Log.d("Digito", "Apertado o botão de SENO!")
        currentNumber = sin(currentNumber)
        previousNumber = currentNumber
        currentFrac = 1.0
        currentOp = ""
        typingNumber = false
    }

    fun btCosPressed() {
        Log.d("Digito", "Apertado o botão de COSSENO!")
        currentNumber = cos(currentNumber)
        previousNumber = currentNumber
        currentFrac = 1.0
        currentOp = ""
        typingNumber = false
    }

    fun btTanPressed() {
        Log.d("Digito", "Apertado o botão de TANGENTE!")
        currentNumber = tan(currentNumber)
        previousNumber = currentNumber
        currentFrac = 1.0
        currentOp = ""
        typingNumber = false
    }

    fun btInversePressed() {
        Log.d("Digito", "Apertado o botão de INVERTER!")
        currentNumber = 1.0 / currentNumber
        previousNumber = currentNumber
        currentFrac = 1.0
        currentOp = ""
        typingNumber = false
    }

    fun btLogPressed() {
        Log.d("Digito", "Apertado o botão de LOG10!")
        currentNumber = log10(currentNumber)
        previousNumber = currentNumber
        currentFrac = 1.0
        currentOp = ""
        typingNumber = false
    }

    fun btAsinPressed() {
        Log.d("Digito", "Apertado o botão de ARCO SENO!")
        currentNumber = asin(currentNumber)
        previousNumber = currentNumber
        currentFrac = 1.0
        currentOp = ""
        typingNumber = false
    }

    fun btAcosPressed() {
        Log.d("Digito", "Apertado o botão de ARCO COSSENO!")
        currentNumber = acos(currentNumber)
        previousNumber = currentNumber
        currentFrac = 1.0
        currentOp = ""
        typingNumber = false
    }

    fun btAtanPressed() {
        Log.d("Digito", "Apertado o botão de ARCO TANGENTE!")
        currentNumber = atan(currentNumber)
        previousNumber = currentNumber
        currentFrac = 1.0
        currentOp = ""
        typingNumber = false
    }

    fun btFactorialPressed() {
        Log.d("Digito", "Apertado o botão de FATORIAL!")
        currentNumber = factorial(currentNumber)
        previousNumber = currentNumber
        currentFrac = 1.0
        currentOp = ""
        typingNumber = false
    }

    fun btLogNeperianPressed() {
        Log.d("Digito", "Apertado o botão de LOG NEPERIANO!")
        currentNumber = log(currentNumber)
        previousNumber = currentNumber
        currentFrac = 1.0
        currentOp = ""
        typingNumber = false
    }

    // Mathematical operation functions
    fun opNone(a: Double, b: Double) = currentNumber
    fun opAdd(a: Double, b: Double) = a + b
    fun opSubtract(a: Double, b: Double) = a - b
    fun opMultiply(a: Double, b: Double) = a * b
    fun opDivide(a: Double, b: Double): Double {
        if (b != 0.0) {
            return a / b
        } else {
            return 0.0
        }
    }

    fun opPower(a: Double, b: Double) = pow(a, b)

    fun factorial(x: Double): Double {
        if (x <= 0)
            return 1.0
        else {
            return x * factorial(x - 1.0)
        }
    }

    // Function to update the calculator's display
    fun updateCalculatorDisplay() {
        Log.d("Digito", "--------------------------")
        Log.d("Digito", "Prev Num:  $previousNumber")
        Log.d("Digito", "Curr Num:  $currentNumber")
        Log.d("Digito", "Curr Frac: $currentFrac")
        Log.d("Digito", "Curr Op:   '$currentOp'")
        Log.d("Digito", "Curr Str:  '$currentDisplayString'")
        Log.d("Digito", "==========================")

        if (currentNumber == truncate(currentNumber)) {
            currentDisplayString = currentNumber.toInt().toString()
        } else {
            currentDisplayString = currentNumber.toString()
        }
        etCalcDisplay.setText(currentDisplayString)
    }
}