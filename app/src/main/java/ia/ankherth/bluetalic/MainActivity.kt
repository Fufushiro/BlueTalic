package ia.ankherth.bluetalic

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextInput: EditText
    private lateinit var textViewOutput: TextView
    private lateinit var buttonCopy: Button
    private lateinit var clipboardManager: ClipboardManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar componentes
        editTextInput = findViewById(R.id.editTextInput)
        textViewOutput = findViewById(R.id.textViewOutput)
        buttonCopy = findViewById(R.id.buttonCopy)
        clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        // Configurar TextWatcher para conversión en tiempo real
        editTextInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val inputText = s?.toString() ?: ""
                val italicText = toItalic(inputText)
                textViewOutput.text = italicText
            }
        })

        // Configurar botón de copiar
        buttonCopy.setOnClickListener {
            val italicText = textViewOutput.text.toString()
            if (italicText.isNotEmpty()) {
                val clip = ClipData.newPlainText("Italic Text", italicText)
                clipboardManager.setPrimaryClip(clip)
                Toast.makeText(this, "Texto copiado al portapapeles", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No hay texto para copiar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Convierte texto ASCII a texto itálico Unicode usando Mathematical Italic.
     * 
     * Rangos Unicode:
     * - A-Z (U+0041-U+005A) → U+1D434 – U+1D44D
     * - a-z (U+0061-U+007A) → U+1D44E – U+1D467
     * 
     * Los números, espacios y símbolos se mantienen sin modificar.
     * 
     * @param text Texto ASCII de entrada
     * @return Texto convertido a itálico Unicode
     */
    fun toItalic(text: String): String {
        val result = StringBuilder(text.length)
        
        for (char in text) {
            val codePoint = char.code
            
            when {
                // Letras mayúsculas A-Z (U+0041-U+005A) → U+1D434-U+1D44D
                codePoint in 0x0041..0x005A -> {
                    val italicCodePoint = codePoint - 0x0041 + 0x1D434
                    result.append(Character.toChars(italicCodePoint))
                }
                // Letras minúsculas a-z (U+0061-U+007A) → U+1D44E-U+1D467
                codePoint in 0x0061..0x007A -> {
                    val italicCodePoint = codePoint - 0x0061 + 0x1D44E
                    result.append(Character.toChars(italicCodePoint))
                }
                // Mantener todos los demás caracteres (números, espacios, símbolos)
                else -> {
                    result.append(char)
                }
            }
        }
        
        return result.toString()
    }
}

