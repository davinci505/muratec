-- MarginRate PK를 id(Long)에서 name(String)으로 변경하는 마이그레이션
-- 주의: 기존 데이터가 있다면 백업 후 실행하세요

-- 1. 새 테이블 생성 (name을 PK로)
CREATE TABLE margin_rates_new (
    name VARCHAR(100) NOT NULL PRIMARY KEY,
    category VARCHAR(30),
    yen_exchange_rate DECIMAL(15,4),
    margin_rate DECIMAL(7,2) DEFAULT 0.65,
    transport_clearance_rate DECIMAL(7,2)
);

-- 2. 기존 데이터 복사 (id는 버리고 name만 사용)
INSERT INTO margin_rates_new (name, category, yen_exchange_rate, margin_rate, transport_clearance_rate)
SELECT name, category, yen_exchange_rate, margin_rate, transport_clearance_rate
FROM margin_rates;

-- 3. quote_parts 테이블의 FK 컬럼들을 VARCHAR로 변경
-- 기존 FK 제약조건 삭제
ALTER TABLE quote_parts DROP CONSTRAINT IF EXISTS fk_quote_parts_margin_rate;
ALTER TABLE quote_parts DROP CONSTRAINT IF EXISTS fk_quote_parts_brt_margin_rate;
ALTER TABLE quote_parts DROP CONSTRAINT IF EXISTS fk_quote_parts_hmx_margin_rate;
ALTER TABLE quote_parts DROP CONSTRAINT IF EXISTS fk_quote_parts_expense_rate;
ALTER TABLE quote_parts DROP CONSTRAINT IF EXISTS fk_quote_parts_brt_expense_rate;
ALTER TABLE quote_parts DROP CONSTRAINT IF EXISTS fk_quote_parts_hmx_expense_rate;

-- 4. 컬럼 타입 변경 (BIGINT -> VARCHAR)
ALTER TABLE quote_parts ALTER COLUMN margin_rate_id TYPE VARCHAR(100);
ALTER TABLE quote_parts ALTER COLUMN brt_margin_rate_id TYPE VARCHAR(100);
ALTER TABLE quote_parts ALTER COLUMN hmx_margin_rate_id TYPE VARCHAR(100);
ALTER TABLE quote_parts ALTER COLUMN expense_rate_id TYPE VARCHAR(100);
ALTER TABLE quote_parts ALTER COLUMN brt_expense_rate_id TYPE VARCHAR(100);
ALTER TABLE quote_parts ALTER COLUMN hmx_expense_rate_id TYPE VARCHAR(100);

-- 5. 기존 margin_rates 테이블 삭제
DROP TABLE margin_rates;

-- 6. 새 테이블 이름 변경
ALTER TABLE margin_rates_new RENAME TO margin_rates;

-- 7. 새 FK 제약조건 추가
ALTER TABLE quote_parts ADD CONSTRAINT fk_quote_parts_margin_rate 
    FOREIGN KEY (margin_rate_id) REFERENCES margin_rates(name);
ALTER TABLE quote_parts ADD CONSTRAINT fk_quote_parts_brt_margin_rate 
    FOREIGN KEY (brt_margin_rate_id) REFERENCES margin_rates(name);
ALTER TABLE quote_parts ADD CONSTRAINT fk_quote_parts_hmx_margin_rate 
    FOREIGN KEY (hmx_margin_rate_id) REFERENCES margin_rates(name);
ALTER TABLE quote_parts ADD CONSTRAINT fk_quote_parts_expense_rate 
    FOREIGN KEY (expense_rate_id) REFERENCES margin_rates(name);
ALTER TABLE quote_parts ADD CONSTRAINT fk_quote_parts_brt_expense_rate 
    FOREIGN KEY (brt_expense_rate_id) REFERENCES margin_rates(name);
ALTER TABLE quote_parts ADD CONSTRAINT fk_quote_parts_hmx_expense_rate 
    FOREIGN KEY (hmx_expense_rate_id) REFERENCES margin_rates(name);