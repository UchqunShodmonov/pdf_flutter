package com.erluxman.pdf_flutter;

import android.content.Context;
import android.view.View;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.platform.PlatformView;
import android.graphics.Color;

public class PdfViewer implements PlatformView, MethodCallHandler {
    private PDFView pdfView;
    private String filePath;
    private String backgroundColor;
    private double minZoom;
    private double midZoom;
    private double maxZoom;

    PdfViewer(Context context, BinaryMessenger messenger, int id, Map<String, Object> args) {
        MethodChannel methodChannel = new MethodChannel(messenger, "pdf_viewer_plugin_" + id);
        methodChannel.setMethodCallHandler(this);

        pdfView = new PDFView(context, null);

        if (!args.containsKey("filePath")) {
            return;
        }

        filePath = (String) args.get("filePath");
        minZoom = (double) args.get("minZoom");
        midZoom = (double) args.get("midZoom");
        maxZoom = (double) args.get("maxZoom");
        backgroundColor = (String) args.get("backgroundColor");
        loadPdfView();
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals("getPdfViewer")) {
            result.success(null);
        } else {
            result.notImplemented();
        }
    }

    private void loadPdfView() {
        if (backgroundColor != null && !backgroundColor.isEmpty()) {
            pdfView.setBackgroundColor(Color.parseColor(backgroundColor));
        } else {
            pdfView.setBackgroundColor(Color.parseColor("#f1f5f9"));

        }
        pdfView.setMinZoom((float) minZoom);
        pdfView.setMidZoom((float) midZoom);
        pdfView.setMaxZoom((float) maxZoom);
        pdfView.fromFile(new File(filePath))
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .pageSnap(false) // snap pages to screen boundaries
                .defaultPage(0)
                .spacing(16)
                .load();
    }

    @Override
    public View getView() {
        return pdfView;
    }

    @Override
    public void dispose() {
    }
}
