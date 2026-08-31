-- QuotePart 테이블에 매입가/공급가 컬럼 추가 (V5)
ALTER TABLE quote_parts ADD COLUMN IF NOT EXISTS purchase_price DECIMAL(15,2);
ALTER TABLE quote_parts ADD COLUMN IF NOT EXISTS selling_price DECIMAL(15,2);
