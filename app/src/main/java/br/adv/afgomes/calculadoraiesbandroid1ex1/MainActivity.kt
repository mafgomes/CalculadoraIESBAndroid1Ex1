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
    val constPi                 : Double = 3.14159265358979
    val constEuler              : Double = 2.718281828459
    var previousNumber          : Double = 0.0
    var currentOp               : String = ""
    var typingNumber            : Boolean = true
    val tabBtDispatch           = hashMapOf<String, () -> Unit>()
    val tabUnOpDispatch         = hashMapOf<String, (Double) -> Double>()
    val tabBinOpDispatch        = hashMapOf<String, (Double, Double) -> Double>()
    lateinit var display        : CalculatorDisplay
    val buttonToDisplay         = hashMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val listenerMappingTable    = hashMapOf<TextView, (String) -> Unit>()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = CalculatorDisplay(this)
        display.zeroOut()

        // Table to map the buttons into their respective listeners
        // Here are the ones that need unique listeners:
        listenerMappingTable[tvButtonBack]          = ::btBackPressed
        listenerMappingTable[tvButtonClear]         = ::btClearPressed
        listenerMappingTable[tvButtonDecimalPoint]  = ::btDecimalPointPressed
        listenerMappingTable[tvButtonPiNumber]      = ::btPiPressed
        listenerMappingTable[tvButtonEulerNumber]   = ::btEulerPressed
        listenerMappingTable[tvButtonEqualsTo]      = ::btEqualsPressed
        // The listener for all digit buttons is the same
        listenerMappingTable[tvButton0]             = ::btDigitPressed
        listenerMappingTable[tvButton1]             = ::btDigitPressed
        listenerMappingTable[tvButton2]             = ::btDigitPressed
        listenerMappingTable[tvButton3]             = ::btDigitPressed
        listenerMappingTable[tvButton4]             = ::btDigitPressed
        listenerMappingTable[tvButton5]             = ::btDigitPressed
        listenerMappingTable[tvButton6]             = ::btDigitPressed
        listenerMappingTable[tvButton7]             = ::btDigitPressed
        listenerMappingTable[tvButton8]             = ::btDigitPressed
        listenerMappingTable[tvButton9]             = ::btDigitPressed
        // Listener for all inary (two-operand) operations
        listenerMappingTable[tvButtonAdd]           = ::btBinaryOpPressed
        listenerMappingTable[tvButtonSubtract]      = ::btBinaryOpPressed
        listenerMappingTable[tvButtonMultiply]      = ::btBinaryOpPressed
        listenerMappingTable[tvButtonDivide]        = ::btBinaryOpPressed
        listenerMappingTable[tvButtonPower]         = ::btBinaryOpPressed
        // Listener for all unary (single-operand) operations
        listenerMappingTable[tvButtonModulus]       = ::btUnaryOpPressed
        listenerMappingTable[tvButtonSignChg]       = ::btUnaryOpPressed
        listenerMappingTable[tvButtonSqrt]          = ::btUnaryOpPressed
        listenerMappingTable[tvButtonSquare]        = ::btUnaryOpPressed
        listenerMappingTable[tvButtonSin]           = ::btUnaryOpPressed
        listenerMappingTable[tvButtonCos]           = ::btUnaryOpPressed
        listenerMappingTable[tvButtonTan]           = ::btUnaryOpPressed
        listenerMappingTable[tvButtonInverse]       = ::btUnaryOpPressed
        listenerMappingTable[tvButtonLog]           = ::btUnaryOpPressed
        listenerMappingTable[tvButtonAsin]          = ::btUnaryOpPressed
        listenerMappingTable[tvButtonAcos]          = ::btUnaryOpPressed
        listenerMappingTable[tvButtonAtan]          = ::btUnaryOpPressed
        listenerMappingTable[tvButtonFactorial]     = ::btUnaryOpPressed
        listenerMappingTable[tvButtonLogNeperian]   = ::btUnaryOpPressed

        // Here is where we actually associate the listeners, according to the table above
        for( tv : TextView in listenerMappingTable.keys ) {
            tv.setOnClickListener() {
                listenerMappingTable[tv]?.invoke((it as TextView).text.toString())
            }
        }

        // Binary Operations dispatch table
        // used to perform the mathematical operation associated with the
        // buttons that perform operations on two operands (binary operations)
        tabBinOpDispatch[""]                                    = ::binOpNone
        tabBinOpDispatch[getString(R.string.buttonAdd)]         = ::opAdd
        tabBinOpDispatch[getString(R.string.buttonSubtract)]    = ::opSubtract
        tabBinOpDispatch[getString(R.string.buttonMultiply)]    = ::opMultiply
        tabBinOpDispatch[getString(R.string.buttonDivide)]      = ::opDivide
        tabBinOpDispatch[getString(R.string.buttonPower)]       = ::pow

        // Unary Operations dispatch table
        // used to perform the mathematical operation associated with the
        // buttons that perform operations on a single operand (unary operations)
        tabUnOpDispatch[""]                                     = ::unOpNone
        tabUnOpDispatch[getString(R.string.buttonModulus)]      = ::opModulus
        tabUnOpDispatch[getString(R.string.buttonSquare)]       = ::opSquare
        tabUnOpDispatch[getString(R.string.buttonInverse)]      = ::opInverse
        tabUnOpDispatch[getString(R.string.buttonSignChg)]      = ::opSignChg
        tabUnOpDispatch[getString(R.string.buttonLog)]          = ::opLog10
        tabUnOpDispatch[getString(R.string.buttonLn)]           = ::opLogNeperian
        tabUnOpDispatch[getString(R.string.buttonFactorial)]    = ::opFactorial
        tabUnOpDispatch[getString(R.string.buttonSqrt)]         = ::sqrt
        tabUnOpDispatch[getString(R.string.buttonSin)]          = ::sin
        tabUnOpDispatch[getString(R.string.buttonCos)]          = ::cos
        tabUnOpDispatch[getString(R.string.buttonTan)]          = ::tan
        tabUnOpDispatch[getString(R.string.buttonAsin)]         = ::asin
        tabUnOpDispatch[getString(R.string.buttonAcos)]         = ::acos
        tabUnOpDispatch[getString(R.string.buttonAtan)]         = ::atan

        // button name to display string mapping table
        buttonToDisplay[""]                                     = ""
        buttonToDisplay[getString(R.string.buttonSignChg)]      = getString(R.string.opSignChange)
        buttonToDisplay[getString(R.string.buttonAdd)]          = getString(R.string.opAdd)
        buttonToDisplay[getString(R.string.buttonSubtract)]     = getString(R.string.opSubtract)
        buttonToDisplay[getString(R.string.buttonMultiply)]     = getString(R.string.opMultiply)
        buttonToDisplay[getString(R.string.buttonDivide)]       = getString(R.string.opDivide)
        buttonToDisplay[getString(R.string.buttonPower)]        = getString(R.string.opPower)
        buttonToDisplay[getString(R.string.buttonModulus)]      = getString(R.string.opModulus)
        buttonToDisplay[getString(R.string.buttonSqrt)]         = getString(R.string.opSqrt)
        buttonToDisplay[getString(R.string.buttonSquare)]       = getString(R.string.opSquare)
        buttonToDisplay[getString(R.string.buttonSin)]          = getString(R.string.opSin)
        buttonToDisplay[getString(R.string.buttonCos)]          = getString(R.string.opCos)
        buttonToDisplay[getString(R.string.buttonTan)]          = getString(R.string.opTan)
        buttonToDisplay[getString(R.string.buttonInverse)]      = getString(R.string.opInverse)
        buttonToDisplay[getString(R.string.buttonLog)]          = getString(R.string.opLog)
        buttonToDisplay[getString(R.string.buttonAsin)]         = getString(R.string.opAsin)
        buttonToDisplay[getString(R.string.buttonAcos)]         = getString(R.string.opAcos)
        buttonToDisplay[getString(R.string.buttonAtan)]         = getString(R.string.opAtan)
        buttonToDisplay[getString(R.string.buttonFactorial)]    = getString(R.string.opFactorial)
        buttonToDisplay[getString(R.string.buttonLn)]           = getString(R.string.opLn)
    }

    // Generic listener function for all digit buttons
    private fun btDigitPressed(digito: String) {
        Log.d("Digito", "Valor do botão: $digito")

        if( ! typingNumber ) {
            display.clearCurrentNumber()
        }
        typingNumber = true
        display.appendDigit(digito)
    }

    // Listener functions for non-digit buttons, non-math operation buttons
    fun btDecimalPointPressed(param : String) {
        if (!typingNumber) {
            display.clearCurrentNumber()
        }
        typingNumber = true
        display.update()
    }

    fun btBackPressed(param : String) {
        if( typingNumber ) {
            display.deleteLastDigit()
        } else {
            display.clearCurrentNumber()
        }
        display.update()
    }

    fun btClearPressed(param : String) {
        previousNumber = 0.0
        typingNumber = true
        display.zeroOut()
        display.update()
    }

    fun btPiPressed(param : String) {
        display.setCurrentValue(constPi)
        display.update()
        typingNumber = false
    }

    fun btEulerPressed(param : String) {
        display.setCurrentValue(constEuler)
        display.update()
        typingNumber = false
    }

    fun btEqualsPressed(param : String) {
        if( ! currentOp.isNullOrEmpty() ) {
            val current : Double = display.getCurrentValue()

            display.setCurrentValue(
                    tabBinOpDispatch[currentOp]?.invoke(previousNumber, current) ?: 0.0)
            display.newLine(display.formatValue(previousNumber)
                    + buttonToDisplay[currentOp]
                    + display.formatValue(current)
                    + " $param "
                    + display.formatValue(display.getCurrentValue())            )
            previousNumber = 0.0
            currentOp = ""
        }
        typingNumber = false
        display.update()
    }

    // Gemeric Listener function for all binary Math operation buttons
    fun btBinaryOpPressed(param : String) {
        Log.d("Digito", "Apertado o botão '$param'")
        previousNumber = display.getCurrentValue()

        // Let's hope to yield more meaningful results
        // by operating on zero, instead of NaN
        if(previousNumber.isNaN()) {
            previousNumber = 0.0
            display.clearCurrentNumber()
            display.update()
        }
        currentOp = param
        typingNumber = false
    }

    // Gemeric Listener function for all unary Math operation buttons
    fun btUnaryOpPressed(param : String) {
        var current : Double = display.getCurrentValue()

        // Let's hope to yield more meaningful results
        // by operating on zero, instead of NaN
        if(current.isNaN()) {
            current = 0.0
        }

        Log.d("Digito", "Apertado o botão '$param'")

        display.setCurrentValue(
                tabUnOpDispatch[param]?.invoke(current) ?: 0.0)
        display.newLine(buttonToDisplay[param] + display.formatValue(current))
        previousNumber = display.getCurrentValue()
        display.update()
        currentOp = ""
        typingNumber = false
    }

    // Mathematical binary operation functions
    fun binOpNone(a: Double, b: Double) = display.getCurrentValue() // Unknown operation
    fun opAdd(a: Double, b: Double) = a + b
    fun opSubtract(a: Double, b: Double) = a - b
    fun opMultiply(a: Double, b: Double) = a * b
    fun opDivide(a: Double, b: Double) = a / b

    // Mathematical unary operation functions
    fun unOpNone(a: Double) = display.getCurrentValue() // Unknown operation
    fun opModulus(a: Double) = if( a < 0.0 ) -a else a
    fun opSquare(a: Double) = a * a
    fun opInverse(a: Double) = 1.0 / a
    fun opSignChg(a: Double) = if( a != 0.0 ) -a else a

    // For some reason, instead of returning NaN, log functions abort the app,
    // so here we explicitly test for invalid arguments before operating on them
    fun opLog10(a: Double) : Double = if( a <= 0 ) Double.NaN else log10(a)
    fun opLogNeperian(a: Double) : Double = if( a <= 0 ) Double.NaN else ln(a)

    // Recursive function to calculate the factorial of an given number
    // Even though the argument is a Double, it's expected to be an integer number,
    // otherwise, it will return NaN as its result
    fun opFactorial(x: Double): Double {
        // If non-integer or negative, return NaN
        if( (x != floor(x)) or (x < 0.0) ) {
            return Double.NaN
        }

        // If equals to zero, return 1
        if (x == 0.0)
            return 1.0
        // Otherwise, call itself recursively
        else {
            return x * opFactorial(x - 1.0)
        }
    }
}