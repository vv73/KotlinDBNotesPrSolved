package study.android.kotlindbnotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val context = this
        val dbHelper = SimpleDBHelper(context)
        btnInsert.setOnClickListener {
            if (editTextName.text.toString().isNotEmpty() &&
                editTextResult.text.toString().isNotEmpty()
            ) {
                val result =
                    Result(editTextName.text.toString(), editTextResult.text.toString().toInt())
                dbHelper.insert(result)
                clearFields()
            } else {
                Toast.makeText(context, "Please Fill All Data's", Toast.LENGTH_SHORT).show()
            }
        }
        btnRead.setOnClickListener {
            val data = dbHelper.getAll("RESULT DESC")
            tvResult.text = ""
            for (d in data) {
                tvResult.append("${d.name} ${d.result}\n")
            }
        }
    }

    private fun clearFields() {
        editTextName.text.clear()
        editTextResult.text.clear()
    }
}