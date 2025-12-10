package estore.istic.fr.Resources;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.view.View;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfUtils {
    public static void generateAndSharePdf(
            Context context,
            View contentToPrint,
            String orderId
    ) {

        PdfDocument document = new PdfDocument();

        // We assume 'contentToPrint' is the LinearLayout INSIDE the ScrollView
        int pageHeight = contentToPrint.getHeight();
        int pageWidth = contentToPrint.getWidth();

        // we add a little margin or use exact view size
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();

        // start the Page
        PdfDocument.Page page = document.startPage(pageInfo);

        // draw the View onto the Page Canvas
        Canvas canvas = page.getCanvas();

        // draw a white background first (in case view is transparent)
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, pageWidth, pageHeight, paint);

        // draw the actual view
        contentToPrint.draw(canvas);

        // finish the Page
        document.finishPage(page);

        // save the File to Cache Directory (No Permissions needed!)
        File receiptsDir = new File(context.getCacheDir(), "receipts");
        if (!receiptsDir.exists()) receiptsDir.mkdirs();

        String fileName = "Receipt_" + orderId + ".pdf";
        File file = new File(receiptsDir, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            fos.close();

            sharePdf(context, file);

        } catch (IOException e) {
            e.printStackTrace();
            Utils.showToast(context,  "Error generating PDF: " + e.getMessage());
        }
    }

    private static void sharePdf(Context context, File file) {
        Uri uri = FileProvider.getUriForFile(
                context,
                context.getPackageName() + ".provider",
                file
        );

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(intent, "Share Receipt"));
    }
}
