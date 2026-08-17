-- Quote 테이블 변경 마이그레이션 (V3)
-- 1. hmx_order_no, hmx_order_date, ccs_po_no, ccs_po_date 컬럼 삭제
-- 2. brt_negotiated_amount 컬럼 타입 변경 (DECIMAL -> VARCHAR)

-- 1. 컬럼 삭제
ALTER TABLE quotes DROP COLUMN IF EXISTS hmx_order_no;
ALTER TABLE quotes DROP COLUMN IF EXISTS hmx_order_date;
ALTER TABLE quotes DROP COLUMN IF EXISTS ccs_po_no;
ALTER TABLE quotes DROP COLUMN IF EXISTS ccs_po_date;

-- 2. brt_negotiated_amount 타입 변경 (DECIMAL -> VARCHAR)
ALTER TABLE quotes ALTER COLUMN brt_negotiated_amount TYPE VARCHAR(500);