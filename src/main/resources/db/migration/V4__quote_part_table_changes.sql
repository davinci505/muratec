-- QuotePart 테이블 변경 마이그레이션 (V4)
-- 1. 컬럼명 변경
--    product_spec -> part_no_product_spec
--    part_no -> ccs_po_no
--    model -> work_no_serial_no
--    quote_quantity -> order_quantity
--    unit_price_brt -> ccs_po_amount
-- 2. 새 컬럼 추가
--    hmx_order_no (VARCHAR)
--    hmx_order_amount (DECIMAL)
--    status (VARCHAR)
--    delivery_date (VARCHAR)
-- 3. 불필요한 컬럼 삭제 (new_parts_no, machine_name, type, unit_name, description, maker, murata_parts_no, part_quantity)

-- 1. 컬럼명 변경
ALTER TABLE quote_parts RENAME COLUMN product_spec TO part_no_product_spec;
ALTER TABLE quote_parts RENAME COLUMN part_no TO ccs_po_no;
ALTER TABLE quote_parts RENAME COLUMN model TO work_no_serial_no;
ALTER TABLE quote_parts RENAME COLUMN quote_quantity TO order_quantity;
ALTER TABLE quote_parts RENAME COLUMN unit_price_brt TO ccs_po_amount;

-- 2. 새 컬럼 추가
ALTER TABLE quote_parts ADD COLUMN IF NOT EXISTS hmx_order_no VARCHAR(100);
ALTER TABLE quote_parts ADD COLUMN IF NOT EXISTS hmx_order_amount DECIMAL(15,2);
ALTER TABLE quote_parts ADD COLUMN IF NOT EXISTS status VARCHAR(20);
ALTER TABLE quote_parts ADD COLUMN IF NOT EXISTS delivery_date VARCHAR(100);

-- 3. 불필요한 컬럼 삭제
ALTER TABLE quote_parts DROP COLUMN IF EXISTS new_parts_no;
ALTER TABLE quote_parts DROP COLUMN IF EXISTS machine_name;
ALTER TABLE quote_parts DROP COLUMN IF EXISTS type;
ALTER TABLE quote_parts DROP COLUMN IF EXISTS unit_name;
ALTER TABLE quote_parts DROP COLUMN IF EXISTS description;
ALTER TABLE quote_parts DROP COLUMN IF EXISTS maker;
ALTER TABLE quote_parts DROP COLUMN IF EXISTS murata_parts_no;
ALTER TABLE quote_parts DROP COLUMN IF EXISTS part_quantity;