package com.bookmart.bookmartpk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Strings;
import com.google.firebase.firestore.DocumentSnapshot;
import com.shockwave.pdfium.PdfDocument;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;

public class PdfView extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener, OnPageErrorListener, OnErrorListener, OnRenderListener {
    PDFView pdfView;
    private ProgressBar progressBar;
    private static final String TAG = PdfView.class.getSimpleName();
    String url = "";
    public static String SAMPLE_FILE;
    Integer pageNumber = 0;
    String pdfFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        String str1 = getIntent().getStringExtra("BOOK_NAME");
        setTitle(str1);

        pdfView = findViewById(R.id.pdfView);
        String str = getIntent().getStringExtra("PDF");
        SAMPLE_FILE = str;
        LOADURL loadurl = new LOADURL(PdfView.this);
        loadurl.execute(SAMPLE_FILE);

    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }

    @Override
    public void onPageError(int page, Throwable t) {

    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {

    }

    class LOADURL extends AsyncTask<String, Void, InputStream> {
        private ProgressDialog progressDialog;

        public LOADURL(PdfView loadPdf) {

            progressDialog = new ProgressDialog(loadPdf);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Fetching ebook from server...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

        }

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)
                        url.openConnection();
                if (urlConnection.getResponseCode() == 200) {

                    inputStream = new
                            BufferedInputStream(urlConnection.getInputStream());
                }
            } catch (IOException e) {

                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfFileName = Book_RecyclerView.bookname;
            pdfView.fromStream(inputStream).load();
//                    .defaultPage(pageNumber)
//                    .onPageChange(PdfView.this)
//                    .enableAnnotationRendering(true)
//                    .onLoad(PdfView.this)
//                    .scrollHandle(new DefaultScrollHandle(PdfView.this))
//                    .spacing(10) // in dp
//                    .onPageError(PdfView.this)
//                    .onError(PdfView.this)
//                    .onPageError(PdfView.this)
//                    .enableAnnotationRendering(false)
//                    .enableAntialiasing(true)
//                    .onRender(PdfView.this)

            progressDialog.dismiss();
        }
    }
}
