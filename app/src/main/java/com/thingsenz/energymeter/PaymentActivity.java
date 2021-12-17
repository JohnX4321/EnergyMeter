package com.thingsenz.energymeter;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment;
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener;
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class PaymentActivity extends AppCompatActivity  {

    Button paymentBtn;
    EasyUpiPayment easyUpiPayment;
    long tid;
    String amt,energy,power,total,tax;
    private String path;
    private File dir,file;
    private PdfPCell cell;

    BaseColor custColor= WebColors.getRGBColor("#9E9E9E");
    BaseColor custColor1=WebColors.getRGBColor("#757575");

    @Override
    protected void onCreate(Bundle saved){

        super.onCreate(saved);
        setContentView(R.layout.activity_payment);


        Intent i=getIntent();
        amt=i.getStringExtra("amount");
        energy=i.getStringExtra("energy");
        power=i.getStringExtra("power");
        tax=Double.toString(0.25*Double.parseDouble(amt));
        total=Double.toString(Double.parseDouble(amt)+Double.parseDouble(tax));

        path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/EnergyMeter";
        dir=new File(path);
        if (!dir.exists())
            dir.mkdirs();


        ((TextView)findViewById(R.id.wattText)).setText(power+" W");
        ((TextView)findViewById(R.id.billText)).setText("\u20B9"+amt);
        ((TextView)findViewById(R.id.taxText)).setText("\u20B9"+tax);
        ((TextView)findViewById(R.id.totalText)).setText("\u20B9"+total);


        PaymentStatusListener paymentStatusListener=new PaymentStatusListener() {
            @Override
            public void onTransactionCompleted(TransactionDetails transactionDetails) {
                // Transaction Completed
                Log.d("TransactionDetails", transactionDetails.toString());

            }

            @Override
            public void onTransactionSuccess() {
                // Payment Success
                generatePDF();
                Toast.makeText(PaymentActivity.this, "Success", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onTransactionSubmitted() {
                // Payment Pending
                generatePDF();
                Toast.makeText(PaymentActivity.this, "Pending | Submitted", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onTransactionFailed() {
                // Payment Failed
                generatePDF();
                Toast.makeText(PaymentActivity.this, "Failed", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onTransactionCancelled() {
                // Payment Cancelled by User
                generatePDF();
                Toast.makeText(PaymentActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();

            }

        };


        paymentBtn=findViewById(R.id.paymentBtn);
        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaymentDialog paymentDialog=new PaymentDialog(PaymentActivity.this);
                paymentDialog.show();
                Random random=new Random();
                tid=random.nextLong();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        paymentDialog.dismiss();
                        easyUpiPayment=new EasyUpiPayment.Builder()
                                .with(PaymentActivity.this)
                                .setPayeeVpa("thingsenz@oksbi")
                                .setPayeeName("ThingSenz")
                                .setTransactionId(Long.toString(tid))
                                .setTransactionRefId(Long.toString(tid))
                                .setDescription("Electricity Bill")
                                .setAmount(total)
                                .build();
                        easyUpiPayment.setPaymentStatusListener(paymentStatusListener);
                        easyUpiPayment.startPayment();
                    }
                },2000);
            }
        });


    }


    private void generatePDF() {

        Document document=new Document();
        try {

            Log.e("PDFCreator","Path"+path);
            SimpleDateFormat sdf=new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
            file = new File(dir,"Bill_"+sdf.format(Calendar.getInstance().getTime())+".pdf");
            FileOutputStream fOut=new FileOutputStream(file);
            PdfWriter writer=PdfWriter.getInstance(document, fOut);
            document.open();

            PdfPTable pt=new PdfPTable(3);
            pt.setWidthPercentage(100);
            float[] f1=new float[]{20,45,35};
            pt.setWidths(f1);

            cell=new PdfPCell();
            cell.setBorder(Rectangle.NO_BORDER);

            Drawable myImage=this.getResources().getDrawable(R.drawable.thingsenzlogo);
            Bitmap bitmap=((BitmapDrawable)myImage).getBitmap();
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
            byte[] bitmapData=stream.toByteArray();
            try {

                Image bgImage=Image.getInstance(bitmapData);
                bgImage.setAbsolutePosition(330f,642f);
                cell.addElement(bgImage);
                pt.addCell(cell);
                cell=new PdfPCell();
                cell.setBorder(Rectangle.NO_BORDER);
                cell.addElement(new Paragraph("ThingSenz Energy Services"));

                cell.addElement(new Paragraph(""));
                cell.addElement(new Paragraph(""));
                pt.addCell(cell);
                cell=new PdfPCell(new Paragraph(""));
                cell.setBorder(Rectangle.NO_BORDER);
                pt.addCell(cell);

                PdfPTable pdfPTable=new PdfPTable(1);
                pdfPTable.setWidthPercentage(100);
                cell=new PdfPCell();
                cell.setColspan(1);
                cell.addElement(pt);
                pdfPTable.addCell(cell);
                PdfPTable table=new PdfPTable(3);
                float[]  colWidth=new float[]{40,40,40};
                table.setWidths(colWidth);

                cell=new PdfPCell();

                cell.setBackgroundColor(custColor);
                cell.setColspan(3);
                cell.addElement(pdfPTable);
                table.addCell(cell);
                cell=new PdfPCell(new Phrase(""));
                cell.setColspan(3);
                table.addCell(cell);
                cell=new PdfPCell();
                cell.setColspan(3);

                cell.setBackgroundColor(custColor1);
                cell=new PdfPCell(new Phrase("Energy"));

                cell.setBackgroundColor(custColor1);
                table.addCell(cell);

                cell=new PdfPCell(new Phrase("Power"));
                cell.setBackgroundColor(custColor1);
                table.addCell(cell);

                cell=new PdfPCell(new Phrase("Total Amount"));
                cell.setBackgroundColor(custColor1);
                table.addCell(cell);

                cell=new PdfPCell();
                cell.setColspan(3);
                table.addCell(String.valueOf(energy));
                table.addCell(String.valueOf(power));
                table.addCell(String.valueOf(total));

                document.add(table);
                Toast.makeText(getApplicationContext(),"Created Bill PDF",Toast.LENGTH_LONG).show();

            } catch (DocumentException| IOException e) {
                e.printStackTrace();
            } finally {
                document.close();
            }



        } catch (Exception e){
            e.printStackTrace();
        }

    }




}
