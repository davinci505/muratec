package com.example.usercrud;

import java.awt.geom.Point2D;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.contentstream.PDFGraphicsStreamEngine;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

public class PdfTableExtractor {

    // =========================================================
    // 설정
    // =========================================================

    private static final float LEFT = 75f;
    private static final float RIGHT = 815f;

    private static final float TOP = 775f;
    private static final float BOTTOM = 505f;

    // 좌표 오차 허용범위
    private static final float TOLERANCE = 2.0f;


    // =========================================================
    // Line
    // =========================================================

    static class Line {

        float x1;
        float y1;
        float x2;
        float y2;

        Line(float x1, float y1, float x2, float y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        boolean isHorizontal() {
            return Math.abs(y1 - y2) < TOLERANCE;
        }

        boolean isVertical() {
            return Math.abs(x1 - x2) < TOLERANCE;
        }

        float getX() {
            return (x1 + x2) / 2f;
        }

        float getY() {
            return (y1 + y2) / 2f;
        }

        float getMinX() {
            return Math.min(x1, x2);
        }

        float getMaxX() {
            return Math.max(x1, x2);
        }

        float getMinY() {
            return Math.min(y1, y2);
        }

        float getMaxY() {
            return Math.max(y1, y2);
        }
    }


    // =========================================================
    // PDF Text
    // =========================================================

    static class PdfText {

        String text;

        float x;
        float y;

        PdfText(String text, float x, float y) {
            this.text = text;
            this.x = x;
            this.y = y;
        }
    }


    // =========================================================
    // Cell
    // =========================================================

    static class Cell {

        float left;
        float right;
        float top;
        float bottom;

        List<PdfText> texts = new ArrayList<>();

        Cell(
                float left,
                float right,
                float top,
                float bottom) {

            this.left = left;
            this.right = right;
            this.top = top;
            this.bottom = bottom;
        }

        boolean contains(float x, float y) {

            return x >= left - TOLERANCE
                    && x <= right + TOLERANCE
                    && y <= top + TOLERANCE
                    && y >= bottom - TOLERANCE;
        }

        String getText() {

            texts.sort(
                    Comparator.comparingDouble(t -> t.x)
            );

            StringBuilder sb = new StringBuilder();

            for (PdfText text : texts) {
                sb.append(text.text);
            }

            return sb.toString().trim();
        }
    }


    // =========================================================
    // PDF Row
    // =========================================================

    static class PdfRow {

        private final List<String> cells;

        PdfRow(List<String> cells) {
            this.cells = cells;
        }

        String get(int index) {

            if (index < 0 || index >= cells.size()) {
                return "";
            }

            String value = cells.get(index);

            return value == null
                    ? ""
                    : value.trim();
        }

        int size() {
            return cells.size();
        }

        @Override
        public String toString() {
            return cells.toString();
        }
    }


    // =========================================================
    // PartItem
    // =========================================================

    static class PartItem {

        private int no;

        private String partName;

        private String spec;

        private String partNo;

        private int qty;

        private String unit;

        private String unitCurrency;

        private BigDecimal unitPrice;

        private String amountCurrency;

        private BigDecimal amount;

        @Override
        public String toString() {

            return "PartItem{" +
                    "no=" + no +
                    ", partName='" + partName + '\'' +
                    ", spec='" + spec + '\'' +
                    ", partNo='" + partNo + '\'' +
                    ", qty=" + qty +
                    ", unit='" + unit + '\'' +
                    ", unitCurrency='" + unitCurrency + '\'' +
                    ", unitPrice=" + unitPrice +
                    ", amountCurrency='" + amountCurrency + '\'' +
                    ", amount=" + amount +
                    '}';
        }

        public void setNo(int no) {
            this.no = no;
        }

        public void setPartName(String partName) {
            this.partName = partName;
        }

        public void setSpec(String spec) {
            this.spec = spec;
        }

        public void setPartNo(String partNo) {
            this.partNo = partNo;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public void setUnitCurrency(String unitCurrency) {
            this.unitCurrency = unitCurrency;
        }

        public void setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
        }

        public void setAmountCurrency(String amountCurrency) {
            this.amountCurrency = amountCurrency;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }


    // =========================================================
    // 1. Graphics Line Extractor
    // =========================================================

    static class LineExtractor extends PDFGraphicsStreamEngine {

        private final List<Line> lines = new ArrayList<>();

        private float currentX;
        private float currentY;

        LineExtractor(PDPage page) {
            super(page);
        }

        @Override
        public void moveTo(float x, float y) {
            currentX = x;
            currentY = y;
        }

        @Override
        public void lineTo(float x, float y) {

            lines.add(
                    new Line(
                            currentX,
                            currentY,
                            x,
                            y
                    )
            );

            currentX = x;
            currentY = y;
        }

        @Override
        public void curveTo(
                float x1,
                float y1,
                float x2,
                float y2,
                float x3,
                float y3) {

            currentX = x3;
            currentY = y3;
        }

        @Override
        public Point2D getCurrentPoint() {

            return new Point2D.Float(
                    currentX,
                    currentY
            );
        }

        @Override
        public void closePath() {
        }

        @Override
        public void endPath() {
        }

        @Override
        public void strokePath() {
        }

        @Override
        public void fillPath(int windingRule) {
        }

        @Override
        public void fillAndStrokePath(int windingRule) {
        }

        @Override
        public void shadingFill(COSName shadingName) {
        }

        @Override
        public void drawImage(PDImage pdImage) {
        }

        @Override
        public void clip(int windingRule) {
        }

        @Override
        public void appendRectangle(
                Point2D p0,
                Point2D p1,
                Point2D p2,
                Point2D p3) {

            addLine(
                    p0.getX(),
                    p0.getY(),
                    p1.getX(),
                    p1.getY()
            );

            addLine(
                    p1.getX(),
                    p1.getY(),
                    p2.getX(),
                    p2.getY()
            );

            addLine(
                    p2.getX(),
                    p2.getY(),
                    p3.getX(),
                    p3.getY()
            );

            addLine(
                    p3.getX(),
                    p3.getY(),
                    p0.getX(),
                    p0.getY()
            );
        }

        private void addLine(
                double x1,
                double y1,
                double x2,
                double y2) {

            Line line = new Line(
                    (float) x1,
                    (float) y1,
                    (float) x2,
                    (float) y2
            );

            /*
             * 테이블 영역과 어느 정도 겹치는 선만 수집
             */
            if (line.isHorizontal()) {

                if (line.getY() >= BOTTOM - 5
                        && line.getY() <= TOP + 5
                        && line.getMaxX() >= LEFT
                        && line.getMinX() <= RIGHT) {

                    lines.add(line);
                }

            } else if (line.isVertical()) {

                if (line.getX() >= LEFT - 5
                        && line.getX() <= RIGHT + 5
                        && line.getMaxY() >= BOTTOM
                        && line.getMinY() <= TOP) {

                    lines.add(line);
                }
            }
        }

        List<Line> getLines() {
            return lines;
        }
    }


    // =========================================================
    // 2. Text Extractor
    // =========================================================

    static class TextExtractor extends PDFTextStripper {

        private final List<PdfText> texts = new ArrayList<>();

        TextExtractor() throws Exception {
            super();
        }

        @Override
        protected void writeString(
                String text,
                List<TextPosition> positions)
                throws java.io.IOException {

            for (TextPosition position : positions) {

                String value = position.getUnicode();

                if (value == null || value.trim().isEmpty()) {
                    continue;
                }

                float x = position.getXDirAdj();

                /*
                 * PDFTextStripper의 Y는 위에서 아래 방향.
                 *
                 * 테이블 좌표는 PDF의 아래에서 위 방향이므로
                 * 나중에 pageHeight - y 방식으로 변환한다.
                 */
                float y = position.getYDirAdj();

                texts.add(
                        new PdfText(
                                value,
                                x,
                                y
                        )
                );
            }

            super.writeString(text, positions);
        }

        List<PdfText> getTexts() {
            return texts;
        }
    }


    // =========================================================
    // 3. 좌표 정규화
    // =========================================================

    private static List<Float> uniqueSorted(
            List<Float> values) {

        List<Float> result = new ArrayList<>();

        values.sort(Float::compare);

        for (Float value : values) {

            if (result.isEmpty()) {

                result.add(value);

            } else {

                float last =
                        result.get(result.size() - 1);

                if (Math.abs(last - value) > TOLERANCE) {

                    result.add(value);

                }
            }
        }

        return result;
    }


    // =========================================================
    // 4. Cell 추출
    // =========================================================

    private static List<Cell> makeCells(
            List<Line> lines) {

        List<Float> xs = new ArrayList<>();
        List<Float> ys = new ArrayList<>();

        List<Line> horizontal = new ArrayList<>();
        List<Line> vertical = new ArrayList<>();

        for (Line line : lines) {

            if (line.isHorizontal()) {

                horizontal.add(line);
                ys.add(line.getY());

            } else if (line.isVertical()) {

                vertical.add(line);
                xs.add(line.getX());
            }
        }

        xs = uniqueSorted(xs);
        ys = uniqueSorted(ys);

        List<Cell> cells = new ArrayList<>();

        /*
         * PDF 좌표 Y는 아래 → 위
         *
         * 높은 Y가 위쪽.
         */
        ys.sort(Float::compare);

        for (int yi = 0; yi < ys.size() - 1; yi++) {

            float bottom = ys.get(yi);
            float top = ys.get(yi + 1);

            if (top - bottom < 3) {
                continue;
            }

            for (int xi = 0; xi < xs.size() - 1; xi++) {

                float left = xs.get(xi);
                float right = xs.get(xi + 1);

                if (right - left < 3) {
                    continue;
                }

                boolean topLine =
                        hasHorizontalLine(
                                horizontal,
                                left,
                                right,
                                top
                        );

                boolean bottomLine =
                        hasHorizontalLine(
                                horizontal,
                                left,
                                right,
                                bottom
                        );

                boolean leftLine =
                        hasVerticalLine(
                                vertical,
                                left,
                                bottom,
                                top
                        );

                boolean rightLine =
                        hasVerticalLine(
                                vertical,
                                right,
                                bottom,
                                top
                        );

                if (topLine
                        && bottomLine
                        && leftLine
                        && rightLine) {

                    cells.add(
                            new Cell(
                                    left,
                                    right,
                                    top,
                                    bottom
                            )
                    );
                }
            }
        }

        return cells;
    }


    private static boolean hasHorizontalLine(
            List<Line> lines,
            float left,
            float right,
            float y) {

        for (Line line : lines) {

            if (Math.abs(line.getY() - y)
                    > TOLERANCE) {
                continue;
            }

            if (line.getMinX()
                    <= left + TOLERANCE
                    && line.getMaxX()
                    >= right - TOLERANCE) {

                return true;
            }
        }

        return false;
    }


    private static boolean hasVerticalLine(
            List<Line> lines,
            float x,
            float bottom,
            float top) {

        for (Line line : lines) {

            if (Math.abs(line.getX() - x)
                    > TOLERANCE) {
                continue;
            }

            if (line.getMinY()
                    <= bottom + TOLERANCE
                    && line.getMaxY()
                    >= top - TOLERANCE) {

                return true;
            }
        }

        return false;
    }


    // =========================================================
    // 5. Cell → Row
    // =========================================================

    private static List<PdfRow> makeRows(
            List<Cell> cells) {

        List<Cell> sorted = new ArrayList<>(cells);

        /*
         * 위쪽 ROW부터
         */
        sorted.sort(
                Comparator
                        .comparingDouble((Cell c) -> -c.top)
                        .thenComparingDouble(c -> c.left)
        );

        List<List<Cell>> rowCells =
                new ArrayList<>();

        for (Cell cell : sorted) {

            List<Cell> targetRow = null;

            float centerY =
                    (cell.top + cell.bottom) / 2f;

            for (List<Cell> row : rowCells) {

                Cell first = row.get(0);

                float firstCenterY =
                        (first.top + first.bottom) / 2f;

                if (Math.abs(
                        firstCenterY - centerY)
                        < 5f) {

                    targetRow = row;
                    break;
                }
            }

            if (targetRow == null) {

                targetRow = new ArrayList<>();

                rowCells.add(targetRow);
            }

            targetRow.add(cell);
        }

        /*
         * 각 ROW 내부를 X 순으로 정렬
         */
        for (List<Cell> row : rowCells) {

            row.sort(
                    Comparator.comparingDouble(
                            c -> c.left
                    )
            );
        }

        /*
         * Cell → String 배열
         */
        List<PdfRow> result =
                new ArrayList<>();

        for (List<Cell> row : rowCells) {

            List<String> values =
                    new ArrayList<>();

            for (Cell cell : row) {

                values.add(
                        cell.getText()
                );
            }

            /*
             * 뒤쪽 빈 컬럼 제거
             */
            while (!values.isEmpty()
                    && values.get(
                            values.size() - 1)
                    .isEmpty()) {

                values.remove(
                        values.size() - 1
                );
            }

            result.add(
                    new PdfRow(values)
            );
        }

        return result;
    }


    // =========================================================
    // 6. PDF Row → PartItem
    // =========================================================

    private static List<PartItem> makePartItems(
            List<PdfRow> rows) {

        List<PartItem> result =
                new ArrayList<>();

        for (int i = 0;
             i < rows.size();
             i++) {

            PdfRow row = rows.get(i);

            /*
             * 첫 번째 컬럼이 숫자인 ROW만
             * 실제 품목 ROW
             */
            String noText = row.get(0);

            if (!noText.matches("\\d+")) {
                continue;
            }

            PartItem item = new PartItem();

            item.setNo(
                    Integer.parseInt(noText)
            );

            /*
             * ROW 04
             *
             * [1]
             * [P.C. Board]
             * [4]
             * [JPY]
             * [97,160.00]
             * [JPY]
             * [388,640.00]
             */
            item.setPartName(
                    row.get(1)
            );

            item.setQty(
                    parseInt(row.get(2))
            );

            item.setUnitCurrency(
                    row.get(3)
            );

            item.setUnitPrice(
                    parseDecimal(row.get(4))
            );

            item.setAmountCurrency(
                    row.get(5)
            );

            item.setAmount(
                    parseDecimal(row.get(6))
            );


            /*
             * 다음 ROW
             *
             * []
             * [MVL-SED-IF1-204 / HM1G0020540]
             * [pcs]
             */
            if (i + 1 < rows.size()) {

                PdfRow detail =
                        rows.get(i + 1);

                if (detail.get(0).isEmpty()) {

                    /*
                     * 단위
                     */
                    item.setUnit(
                            detail.get(2)
                    );

                    /*
                     * 스펙 / 품번
                     */
                    parseSpecAndPartNo(
                            item,
                            detail.get(1)
                    );
                }
            }

            result.add(item);
        }

        return result;
    }


    // =========================================================
    // 7. 스펙 / 품번 분리
    // =========================================================

    private static void parseSpecAndPartNo(
            PartItem item,
            String value) {

        if (value == null) {
            return;
        }

        value = value.trim();

        if (value.isEmpty()) {
            return;
        }

        int slash =
                value.indexOf('/');

        if (slash < 0) {

            item.setSpec(value);

            return;
        }

        String spec =
                value.substring(
                        0,
                        slash
                ).trim();

        String partNo =
                value.substring(
                        slash + 1
                ).trim();

        /*
         * 예:
         *
         * MVL-SED-IF1-204 / HM1G0020540
         *
         * spec    = MVL-SED-IF1-204
         * partNo  = HM1G0020540
         */

        if (!spec.isEmpty()) {
            item.setSpec(spec);
        }

        if (!partNo.isEmpty()) {
            item.setPartNo(partNo);
        }
    }


    // =========================================================
    // 8. 숫자 변환
    // =========================================================

    private static int parseInt(
            String value) {

        if (value == null
                || value.trim().isEmpty()) {

            return 0;
        }

        try {

            return Integer.parseInt(
                    value
                            .replace(",", "")
                            .trim()
            );

        } catch (NumberFormatException e) {

            return 0;
        }
    }


    private static BigDecimal parseDecimal(
            String value) {

        if (value == null
                || value.trim().isEmpty()) {

            return null;
        }

        try {

            return new BigDecimal(
                    value
                            .replace(",", "")
                            .trim()
            );

        } catch (NumberFormatException e) {

            return null;
        }
    }


    // =========================================================
    // 9. 실제 PDF 실행
    // =========================================================

    public static void main(String[] args)
            throws Exception {

        File file =
                new File("sample.pdf");

        System.out.println(
                "PDF : "
                        + file.getAbsolutePath()
        );

        if (!file.exists()) {

            throw new IllegalArgumentException(
                    "PDF 파일이 없습니다 : "
                            + file.getAbsolutePath()
            );
        }


        try (PDDocument document =
                     Loader.loadPDF(file)) {

            PDPage page =
                    document.getPage(0);


            // -------------------------------------------------
            // 1. 선 추출
            // -------------------------------------------------

            LineExtractor lineExtractor =
                    new LineExtractor(page);

            lineExtractor.processPage(page);

            List<Line> lines =
                    lineExtractor.getLines();


            // -------------------------------------------------
            // 2. Text 추출
            // -------------------------------------------------

            TextExtractor textExtractor =
                    new TextExtractor();

            textExtractor.setStartPage(1);
            textExtractor.setEndPage(1);

            textExtractor.getText(document);

            List<PdfText> texts =
                    textExtractor.getTexts();


            // -------------------------------------------------
            // 3. Cell 생성
            // -------------------------------------------------

            List<Cell> cells =
                    makeCells(lines);


            /*
             * Text를 Cell에 넣는다.
             */
            float pageHeight =
                    page.getMediaBox().getHeight();

            for (PdfText text : texts) {

                /*
                 * PDFTextStripper:
                 * Y = 위에서 아래
                 *
                 * PDF 좌표:
                 * Y = 아래에서 위
                 */
                float pdfY =
                        pageHeight - text.y;

                for (Cell cell : cells) {

                    if (cell.contains(
                            text.x,
                            pdfY)) {

                        cell.texts.add(text);

                        break;
                    }
                }
            }


            // -------------------------------------------------
            // 4. Cell → PdfRow
            // -------------------------------------------------

            List<PdfRow> rows =
                    makeRows(cells);


            System.out.println();
            System.out.println(
                    "========== PDF ROW =========="
            );

            int rowNo = 1;

            for (PdfRow row : rows) {

                System.out.printf(
                        "ROW %02d : %s%n",
                        rowNo++,
                        row
                );
            }


            // -------------------------------------------------
            // 5. PdfRow → PartItem
            // -------------------------------------------------

            List<PartItem> items =
                    makePartItems(rows);


            // -------------------------------------------------
            // 6. 결과 출력
            // -------------------------------------------------

            System.out.println();
            System.out.println(
                    "========== PART ITEM =========="
            );

            for (PartItem item : items) {

                System.out.println(item);
            }
        }
    }
}