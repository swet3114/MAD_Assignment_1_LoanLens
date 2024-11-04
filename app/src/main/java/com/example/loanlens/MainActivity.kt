package com.example.loanlens

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var predictButton: Button
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setting edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize input fields and spinners
        val ageInput: EditText = findViewById(R.id.person_age)
        val incomeInput: EditText = findViewById(R.id.person_income)
        val empLengthInput: EditText = findViewById(R.id.person_emp_length)
        val loanAmountInput: EditText = findViewById(R.id.loan_amnt)
        val interestRateInput: EditText = findViewById(R.id.loan_int_rate)
        val repaymentIncomeInput: EditText = findViewById(R.id.loan_repayment_income)
        val creditHistoryLengthInput: EditText = findViewById(R.id.credit_history_length)

        val spinnerHomeOwnershipStatus: Spinner = findViewById(R.id.spinner_home_ownership_status)
        val spinnerCreditGrade: Spinner = findViewById(R.id.spinner_credit_grade)
        val spinnerDefaultHistory: Spinner = findViewById(R.id.spinner_default_history)
        val spinnerLoanPurpose: Spinner = findViewById(R.id.spinner_loan_purpose)

        setupSpinner(spinnerHomeOwnershipStatus, listOf("Rent", "Own", "Mortgage", "Other"))
        setupSpinner(spinnerCreditGrade, listOf("A", "B", "C", "D", "E", "F", "G"))
        setupSpinner(spinnerDefaultHistory, listOf("Yes", "No"))
        setupSpinner(spinnerLoanPurpose, listOf("Education", "Home Improvement", "Medical", "Personal", "Venture", "Debt Consolidation"))

        predictButton = findViewById(R.id.predict_button)
        predictButton.setOnClickListener {
            val age = ageInput.text.toString()
            val income = incomeInput.text.toString()
            val empLength = empLengthInput.text.toString()
            val loanAmount = loanAmountInput.text.toString()
            val interestRate = interestRateInput.text.toString()
            val repaymentIncome = repaymentIncomeInput.text.toString()
            val creditHistoryLength = creditHistoryLengthInput.text.toString()

            val homeOwnershipStatus = spinnerHomeOwnershipStatus.selectedItem.toString()
            val creditGrade = spinnerCreditGrade.selectedItem.toString()
            val defaultHistory = spinnerDefaultHistory.selectedItem.toString()
            val loanPurpose = spinnerLoanPurpose.selectedItem.toString()

            if (age.isNotEmpty() && income.isNotEmpty() && empLength.isNotEmpty() &&
                loanAmount.isNotEmpty() && interestRate.isNotEmpty() && repaymentIncome.isNotEmpty() &&
                creditHistoryLength.isNotEmpty()
            ) {
                makePredictionRequest(age, income, empLength, loanAmount, interestRate, repaymentIncome, creditHistoryLength, homeOwnershipStatus, creditGrade, defaultHistory, loanPurpose)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSpinner(spinner: Spinner, items: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun makePredictionRequest(
        age: String, income: String, empLength: String, loanAmount: String,
        interestRate: String, repaymentIncome: String, creditHistoryLength: String,
        homeOwnershipStatus: String, creditGrade: String, defaultHistory: String, loanPurpose: String
    ) {
        val url = "http://127.0.0.1:5000"
        val json = JSONObject()
        json.put("age", age)
        json.put("income", income)
        json.put("emp_length", empLength)
        json.put("loan_amnt", loanAmount)
        json.put("int_rate", interestRate)
        json.put("loan_repayment_income", repaymentIncome)
        json.put("credit_history_length", creditHistoryLength)
        json.put("home_ownership", homeOwnershipStatus)
        json.put("credit_grade", creditGrade)
        json.put("default_history", defaultHistory)
        json.put("loan_purpose", loanPurpose)

        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString())
        val request = Request.Builder().url(url).post(body).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Failed to get response", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body()?.string()
                val predictionResult = JSONObject(responseData).getString("prediction_result")

                val intent = Intent(this@MainActivity, ResultActivity::class.java)
                intent.putExtra("PREDICTION_RESULT", predictionResult)
                startActivity(intent)
            }
        })
    }
}
