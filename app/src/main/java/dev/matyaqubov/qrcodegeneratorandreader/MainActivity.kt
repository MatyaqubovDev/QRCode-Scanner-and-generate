package dev.matyaqubov.qrcodegeneratorandreader

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.zxing.integration.android.IntentIntegrator
import net.glxn.qrgen.android.QRCode

class MainActivity : AppCompatActivity() {
    private lateinit var resultText:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {
        val qr_image=findViewById<ImageView>(R.id.img_qrcode)
        val generateButton=findViewById<Button>(R.id.btn_generate)
        val scanButton=findViewById<Button>(R.id.btn_scan)
         resultText=findViewById<TextView>(R.id.tv_result)
        var text=findViewById<EditText>(R.id.et_text).text
        generateButton.setOnClickListener{
            val myBitmap =QRCode.from(text.toString()).bitmap()
            qr_image.setImageBitmap(myBitmap)
        }

        scanButton.setOnClickListener{
            val scanner=IntentIntegrator(this)
            scanner.apply {
                setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                setBeepEnabled(false)
                setOrientationLocked(false)
                initiateScan()
            }


        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==Activity.RESULT_OK){
            val result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
            if (result!=null){
                resultText.text=result.contents
            }
        }
    }
}