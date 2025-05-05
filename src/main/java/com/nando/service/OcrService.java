package com.nando.service;

import jakarta.enterprise.context.ApplicationScoped;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.textract.TextractClient;
import software.amazon.awssdk.services.textract.model.*;
import software.amazon.awssdk.regions.Region;

import java.util.Base64;

@ApplicationScoped
public class OcrService {

    public String toText(String base64Image) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            SdkBytes sdkBytes = SdkBytes.fromByteArray(imageBytes);

            TextractClient textractClient = TextractClient.builder()
                    .region(Region.US_EAST_1) // ajuste conforme necessário
                    .build();

            Document document = Document.builder()
                    .bytes(sdkBytes)
                    .build();

            DetectDocumentTextRequest request = DetectDocumentTextRequest.builder()
                    .document(document)
                    .build();

            DetectDocumentTextResponse response = textractClient.detectDocumentText(request);

            StringBuilder sb = new StringBuilder();

            for (Block block : response.blocks()) {
                if (block.blockType() == BlockType.LINE && block.text() != null) {
                    sb.append(block.text().replaceAll("\\s+", ""));
                }
            }

            String textoFinal = sb.toString();
            System.out.println("Texto concatenado: " + textoFinal);

            textractClient.close();
            return textoFinal;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro no OCR", e);
        }
    }


}
