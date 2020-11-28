/*
 * Calculadora
 * Pós-graduação IESB em Desenvolvimento de Apps para plataformas móveis
 * Android 1 - Exercício 1
 * Aluno: Marcelo Amarante Ferreira Gomes (2086332021)
 *
 * Definição da classe MainActivity, que trata os eventos da
 * tela (activity) principal (única) da calculadora
 */
package br.adv.afgomes.calculadoraiesbandroid1ex1

import android.util.Log
import android.util.Range
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.truncate

class CalculatorDisplay(contexto : MainActivity): Any() {
    private val maxLines        : Int           = 4
    private var numberStr       : String        = contexto.getString(R.string.strDisplayZero)
    private var lines                           = arrayOf(numberStr)
    private var decimalPoint    : Char          = contexto.getString(R.string.strDecimalPoint)[0]
    private var thousandsSep    : Char          = contexto.getString(R.string.strThousandsSep)[0]
    private var minusSign       : Char          = contexto.getString(R.string.strMinusSign)[0]
    private var ctx             : MainActivity  = contexto
    private var nextDigitFractional : Boolean   = false

    // Method to update the calculator's display
    fun update() {
        var displayText : String = ""

        Log.d("Digito", "--------------------------")
        Log.d("Digito", "Prev Num:  ${ctx.previousNumber}")
        Log.d("Digito", "Curr Num:  " + getCurrentValue())
        Log.d("Digito", "numberStr: '${numberStr}'")
        Log.d("Digito", "Curr Op:   '${ctx.currentOp}'")
        Log.d("Digito", "==========================")

        if( lines.isNullOrEmpty() or lines[0].isEmpty() ) {
            displayText = numberStr
        } else {
            lines[0] = numberStr
            for(ind: Int in lines.size - 1 downTo 0) {
                displayText += lines[ind] + "\n"
            }
        }
        ctx.etCalcDisplay.setText(displayText)
    }

    fun getCurrentValue(): Double {
        var valStr : String = numberStr.replace(thousandsSep.toString(), "")

        valStr = valStr.replace(decimalPoint.toString(), ".")

        return valStr.toDouble()
    }

    fun formatValue(v: Double) : String {
        var formatted : String = v.toString().replace("-", minusSign.toString())
        formatted = formatted.replace(".", decimalPoint.toString())
        while( formatted[formatted.length - 1] == '0' ) {
            formatted = formatted.dropLast(1)
        }
        return appendThousandsSep(formatted)
    }

    fun setCurrentValue(v: Double) {
        numberStr = formatValue(v)
    }

    fun deleteLastDigit() {
        var oldIntPart      : String = ""
        var oldFracPart     : String = ""
        var inIntegerPart   : Boolean = true

        for(ch in numberStr.toCharArray()) {
            if( ch.isDigit()) {
                if (inIntegerPart) {
                    oldIntPart += ch
                } else {
                    oldFracPart += ch
                }
            } else if( ch == decimalPoint ) {
                inIntegerPart = false
            } else if( ch == minusSign ) {
                oldIntPart += ch
            }
        }
        if( oldFracPart.isEmpty() ) {
            if( ! oldIntPart.isEmpty() ) {
                oldIntPart = oldIntPart.substring(0,oldIntPart.length - 1)
            }
        } else {
            oldFracPart = oldFracPart.substring(0, oldFracPart.length - 1)
        }
        if( oldIntPart.isEmpty() ) {
            oldIntPart = "0"
        }
        if( (oldIntPart == minusSign.toString()) and oldFracPart.isEmpty() ) {
            oldIntPart = "0"
        }
        numberStr = appendThousandsSep(oldIntPart) + decimalPoint + oldFracPart
    }

    private fun appendThousandsSep(paramStr : String) : String {
        var signStr     = ""
        var newNumStr   = ""
        var j : Int     = -1
        var oldNumStr   = paramStr
        var trailerPart = ""

        if( oldNumStr.length < 4 ) {
            return oldNumStr
        }

        if( oldNumStr[0] == minusSign ) {
            signStr = minusSign.toString()
            oldNumStr = oldNumStr.drop(1)
        }

        var k : Int = oldNumStr.indexOf(decimalPoint)
        if( k < 0 ) {
            k = oldNumStr.length
            trailerPart = decimalPoint.toString()
        } else {
            trailerPart = oldNumStr.drop(k)
        }
        for( i in k - 1 downTo 0 ) {
            j++
            newNumStr += oldNumStr[i]
            if ((j % 3 == 2) and (i != 0)) {
                newNumStr += thousandsSep
            }
        }
        return signStr + newNumStr.reversed() + trailerPart
    }

    fun appendDigit(digit : String) {
        localAppendDigit(digit, nextDigitFractional)
    }

    fun appendDigit(digit : String, toFractionalPart: Boolean) {
        localAppendDigit(digit, toFractionalPart)
    }

    fun localAppendDigit(digit : String, toFractionalPart: Boolean) {
        var oldIntPart:     String
        var oldFracPart:    String
        val decPtLocation:  Int

        numberStr   = numberStr.replace(thousandsSep.toString(), "")
        decPtLocation = numberStr.indexOf(decimalPoint)

        // Determine the old Integer and fractional parts of the number
        if( decPtLocation == -1 ) {
            oldIntPart = numberStr
            oldFracPart = ""
        } else if( decPtLocation > 0 ) {
            oldIntPart = numberStr.substring(0,decPtLocation)
            if( decPtLocation < numberStr.length - 1 ) {
                oldFracPart = numberStr.substring(decPtLocation + 1, numberStr.length)
            } else {
                oldFracPart = ""
            }
        } else { // decPtLocation == 0
            oldIntPart = ""
            if( numberStr.length > 1 ) {
                oldFracPart = numberStr.substring(1, numberStr.length)
            } else {
                oldFracPart = ""
            }
        }

        // Now, format the new number using the old parts
        if( toFractionalPart ) {
            oldFracPart += digit
        } else {
            if( oldIntPart == "0" ) {
                oldIntPart = digit
            } else {
                oldIntPart += digit
            }
        }
        numberStr = appendThousandsSep(oldIntPart + decimalPoint + oldFracPart)
        update()
    }

    fun clearCurrentNumber() {
        numberStr = 0.toString() + decimalPoint
    }

    fun newLine(line : String) {
        lines[0] = line
        if( lines.size < maxLines ) {
            lines = lines.plusElement("")
        }
        for( i : Int in lines.size - 1 downTo 1 ) {
            lines[i] = lines[i - 1]
        }
        lines[0] = numberStr
    }

    fun zeroOut() {
        clearCurrentNumber()
        lines = arrayOf(numberStr)
    }

    fun changeSignal() {
        if( numberStr[0] == minusSign ) {
            numberStr = numberStr.drop(1)
        } else {
            numberStr = minusSign + numberStr
        }
    }
}