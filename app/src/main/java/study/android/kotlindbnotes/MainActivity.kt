package study.android.kotlindbnotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

lateinit var dbHelper: SimpleDBHelper;

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val context = this
        dbHelper = SimpleDBHelper(context)
        // при запуске восстанавливаем базу
        dbHelper.clearAll()
        for (r in TestData.russianCompanies2020) {
            dbHelper.insert(r);
        }
        showList()
        statistics.setOnClickListener {
            startActivity(Intent(this, StatActivity::class.java))
        }

        delete.setOnClickListener {
            dbHelper.deleteSubStringName(toDelete.text.toString());
            showList()

        }
    }

    fun showList() {
        val data = dbHelper.getAll("RESULT DESC")
        tvResult.text = ""
        for (d in data) {
            tvResult.append("${d.name} ${d.result}\n")
        }
    }
}