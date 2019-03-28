package com.example.library;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.MultiFormatOneDReader;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRGenerator extends AppCompatActivity {
    ImageView i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgenerator);
        i = (ImageView) findViewById(R.id.imageViewQr);


        final String text = getIntent().getStringExtra("Name");

        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

                if(text!=null && !text.isEmpty())
                {
                    try{
                        MultiFormatWriter multiFormatWriter= new MultiFormatWriter();
                        BitMatrix bitMatrix= multiFormatWriter.encode(text,BarcodeFormat.QR_CODE,460,460);
                        BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
                        Bitmap bitmap=barcodeEncoder.createBitmap(bitMatrix);
                        i.setImageBitmap(bitmap);
                    }
                    catch(WriterException e)
                    {e.printStackTrace();}

                }

            }

    }





