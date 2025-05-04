package com.nando.service;

import jakarta.enterprise.context.ApplicationScoped;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Base64;

@ApplicationScoped
public class OcrService {

    public String toText(String base64Image) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
            BufferedImage image = ImageIO.read(bis);

            ITesseract instance = new Tesseract();
            instance.setDatapath("/usr/share/tesseract-ocr/4.00/tessdata");
            instance.setLanguage("eng");
            instance.setPageSegMode(7); // Apenas PSM 11
            instance.setTessVariable("user_defined_dpi", "300");
            instance.setTessVariable("tessedit_char_whitelist", "0123456789");

            String result = instance.doOCR(image).replaceAll("\\s+", "");
            System.out.println("Resultado OCR (PSM 11): " + result);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro no OCR", e);
        }
    }




}
