package com.example.usercrud;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfReader {

    public static void main(String[] args) throws Exception {

        // 프로젝트 실행 위치에 있는 sample.pdf
        File file = new File("sample.pdf");

        System.out.println("PDF 경로 : " + file.getAbsolutePath());
        System.out.println("파일 존재 : " + file.exists());

        if (!file.exists()) {
            throw new IllegalArgumentException(
                    "PDF 파일이 없습니다: " + file.getAbsolutePath());
        }

        try (PDDocument document = Loader.loadPDF(file)) {

            System.out.println("페이지 수 : "
                    + document.getNumberOfPages());

            PDFTextStripper stripper = new PDFTextStripper();

            // PDF에서 텍스트 추출
            String text = stripper.getText(document);

            System.out.println();
            System.out.println("========== PDF 원본 텍스트 ==========");
            System.out.println(text);
            System.out.println("====================================");
            
            // 키 : 값 형태로 파싱
            Map<String, String> data = parse(text);

            System.out.println();
            System.out.println("========== 파싱 결과 ==========");

            for (Map.Entry<String, String> entry : data.entrySet()) {
                System.out.println(
                        "KEY=[" + entry.getKey() + "] "
                        + "VALUE=[" + entry.getValue() + "]"
                );
            }

            System.out.println("==============================");
        }
    }

    /**
     * PDF에서 추출한 텍스트를
     *
     * 고객명 : 홍길동
     * 생년월일 : 1990-01-01
     * 금액 : 1,000,000
     *
     * 형태로 파싱
     */
    private static Map<String, String> parse(String text) {

        Map<String, String> result = new LinkedHashMap<>();

        String[] lines = text.split("\\R");

        for (String line : lines) {

            line = line.trim();

            if (line.isEmpty()) {
                continue;
            }

            // ":" 기준으로 최대 2개로 분리
            String[] parts = line.split(":", 2);

            if (parts.length != 2) {
                continue;
            }

            String key = parts[0].trim();
            String value = parts[1].trim();

            if (!key.isEmpty() && !value.isEmpty()) {
                result.put(key, value);
            }
        }

        return result;
    }
}