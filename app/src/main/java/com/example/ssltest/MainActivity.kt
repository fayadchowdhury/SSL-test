package com.example.ssltest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCAdditionalInitializer
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCCustomerInfoInitializer
import com.sslwireless.sslcommerzlibrary.model.initializer.SSLCommerzInitialization
import com.sslwireless.sslcommerzlibrary.model.response.SSLCTransactionInfoModel
import com.sslwireless.sslcommerzlibrary.model.util.SSLCCurrencyType
import com.sslwireless.sslcommerzlibrary.model.util.SSLCSdkType
import com.sslwireless.sslcommerzlibrary.view.singleton.IntegrateSSLCommerz
import com.sslwireless.sslcommerzlibrary.viewmodel.listener.SSLCTransactionResponseListener

class MainActivity : AppCompatActivity(), SSLCTransactionResponseListener {

    lateinit var setUpButton: Button

    private var sslCommerzInitialization: SSLCommerzInitialization? = null
    private var customerInfoInitializer: SSLCCustomerInfoInitializer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpButton = findViewById(R.id.setUpButton)

        setUpButton.setOnClickListener {
            sslSetUp("11")
        }
    }
    private fun sslSetUp(amount:String) {

        val currentTimestamp = System.currentTimeMillis()

        //ssl_commarz
        sslCommerzInitialization = SSLCommerzInitialization(
            "storeID",
            "storePSWD",
            amount.toDouble(), SSLCCurrencyType.BDT,
            "UID-timestamp",
            "",
            SSLCSdkType.TESTBOX //needs to be changed to LIVE for production
        )

        customerInfoInitializer = SSLCCustomerInfoInitializer(
            "Name",
            "e.mail@email.com",
            "Address",
            "City",
            "PostCode",
            "Country",
            "PhoneNp.",
        )

        IntegrateSSLCommerz
            .getInstance(this)
            .addSSLCommerzInitialization(sslCommerzInitialization)
            .addCustomerInfoInitializer(customerInfoInitializer)
            .buildApiCall(this)
    }

    override fun transactionSuccess(p0: SSLCTransactionInfoModel?) {
        if (p0 != null) {
            Log.d("DebugSSL", p0.toString())
            Toast.makeText(this, "Id:${p0.tranId} \nAmount: ${p0.amount} \nPayment Status:${p0.apiConnect}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun transactionFail(p0: String?) {
        Toast.makeText(this, p0, Toast.LENGTH_SHORT).show()
    }

    override fun merchantValidationError(p0: String?) {
        Toast.makeText(this, p0, Toast.LENGTH_SHORT).show()
    }

}