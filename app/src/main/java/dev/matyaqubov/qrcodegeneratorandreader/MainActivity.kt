package dev.matyaqubov.qrcodegeneratorandreader

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import com.google.zxing.integration.android.IntentIntegrator
import net.glxn.qrgen.android.QRCode
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var myBitmap :Bitmap
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
        val shareButton=findViewById<Button>(R.id.btn_share)
         resultText=findViewById<TextView>(R.id.tv_result)
        var text=findViewById<EditText>(R.id.et_text).text
        generateButton.setOnClickListener{
            myBitmap =QRCode.from(text.toString()).bitmap()
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

        shareButton.setOnClickListener {
            getImage(this,myBitmap)?.let {
                shareImage(it)
            }
        }


    }

    private fun shareImage(uri: Uri) {
        val intent =Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT,uri)
        intent.type="image/*"
        startActivity(Intent.createChooser(intent,"Share"))
    }

    private fun getImage(context: Context, myBitmap: Bitmap): Uri? {
        val bytes= ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes)
        val path=MediaStore.Images.Media.insertImage(context.contentResolver,myBitmap,"QR Code",null)
        return Uri.parse(path)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==Activity.RESULT_OK){
            val result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
            if (result!=null){
                //toast
                resultText.text=result.contents
            }
        }
    }
}