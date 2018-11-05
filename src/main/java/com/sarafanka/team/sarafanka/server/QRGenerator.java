package com.sarafanka.team.sarafanka.server;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;


import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;

public class QRGenerator {

    private static final String QR_CODE_IMAGE_PATH = "./MyQRCode.png";

    public static String generateQRCodeImage(String text, int width, int height, String filePath)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        File file =  new File(filePath);
        MatrixToImageWriter.writeToFile(bitMatrix, "PNG", file);
        return filePath;
    }
}
