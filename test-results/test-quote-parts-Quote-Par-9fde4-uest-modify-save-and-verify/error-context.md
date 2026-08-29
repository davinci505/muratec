# Instructions

- Following Playwright test failed.
- Explain why, be concise, respect Playwright best practices.
- Provide a snippet of code with the fix, if possible.

# Test info

- Name: test-quote-parts.spec.js >> Quote Parts Management >> Create Quote with parts from JobRequest, modify, save, and verify
- Location: test-quote-parts.spec.js:65:3

# Error details

```
Test timeout of 30000ms exceeded.
```

```
Error: locator.textContent: Test timeout of 30000ms exceeded.
Call log:
  - waiting for locator('#jobRequestPartsTable .tabulator-row').first().locator('td:nth-child(2)')

```

# Page snapshot

```yaml
- generic [active] [ref=e1]:
  - banner [ref=e2]:
    - button "메뉴 열기" [ref=e3] [cursor=pointer]:
      - generic [ref=e4]: 
    - heading [level=1] [ref=e5]:
      - link "BARATEC Management System" [ref=e6] [cursor=pointer]:
        - /url: /
    - button "이전 페이지로 가기" [ref=e7] [cursor=pointer]:
      - generic [ref=e8]: 
  - generic [ref=e9]:
    - generic [ref=e10]:
      - img "BARATEC Logo" [ref=e11]
      - text: 
    - list [ref=e12]:
      - listitem [ref=e13]:
        - link " 홈" [ref=e14] [cursor=pointer]:
          - /url: /
          - generic [ref=e15]: 
          - generic [ref=e16]: 홈
      - listitem [ref=e17]:
        - link " 고객의뢰" [ref=e18] [cursor=pointer]:
          - /url: /jobs
          - generic [ref=e19]: 
          - generic [ref=e20]: 고객의뢰
      - listitem [ref=e21]:
        - link " 견적관리" [ref=e22] [cursor=pointer]:
          - /url: /quotes
          - generic [ref=e23]: 
          - generic [ref=e24]: 견적관리
      - listitem [ref=e25]:
        - link " 발주/납품관리" [ref=e26] [cursor=pointer]:
          - /url: /quote-parts
          - generic [ref=e27]: 
          - generic [ref=e28]: 발주/납품관리
      - listitem [ref=e29]:
        - link " 판매부품 실적관리" [ref=e30] [cursor=pointer]:
          - /url: /sales-parts
          - generic [ref=e31]: 
          - generic [ref=e32]: 판매부품 실적관리
      - listitem [ref=e33]:
        - link " 부품등록" [ref=e34] [cursor=pointer]:
          - /url: /parts
          - generic [ref=e35]: 
          - generic [ref=e36]: 부품등록
      - listitem [ref=e37]:
        - link " 리포트" [ref=e38] [cursor=pointer]:
          - /url: "#"
          - generic [ref=e39]: 
          - generic [ref=e40]: 리포트
      - listitem [ref=e41]:
        - link " 설정" [ref=e42] [cursor=pointer]:
          - /url: "#"
          - generic [ref=e43]: 
          - generic [ref=e44]: 설정
  - generic [ref=e49]:
    - heading "새 견적 추가" [level=2] [ref=e51]
    - generic [ref=e53]:
      - generic [ref=e55]:
        - generic [ref=e56]: 고객의뢰 선택
        - combobox "고객의뢰 선택" [ref=e57]:
          - option "선택하세요"
          - option "바라텍 / TEST-JOB-001 / Test Customer / Test User / 2024-01-15" [selected]
          - option "바라텍 / TEST-JOB-001 / Test Customer / Test User / 2024-01-15"
          - option "바라텍 / TEST-JOB-001 / Test Customer / Test User / 2024-01-15"
          - option "바라텍 / TEST-JOB-001 / Test Customer / Test User / 2024-01-15"
          - option "바라텍 / TEST-JOB-001 / Test Customer / Test User / 2024-01-15"
          - option "바라텍 / TEST-JOB-001 / Test Customer / Test User / 2024-01-15"
          - option "바라텍 / TEST-JOB-001 / Test Customer / Test User / 2024-01-15"
          - option "바라텍 / TEST-JOB-001 / Test Customer / Test User / 2024-01-15"
          - option "바라텍 / TEST-JOB-001 / Test Customer / Test User / 2024-01-15"
      - generic [ref=e58]:
        - generic [ref=e59]:
          - generic [ref=e60]: CCS견적날짜
          - textbox "CCS견적날짜" [ref=e61]:
            - /placeholder: YYYYMMDD
            - text: 2026-08-29
        - generic [ref=e62]:
          - generic [ref=e63]: CCS견적번호
          - textbox "CCS견적번호" [ref=e64]
        - generic [ref=e65]:
          - generic [ref=e66]: CCS견적금액 (￥)
          - textbox "CCS견적금액 (￥)" [ref=e67]:
            - /placeholder: 금액 입력 시 ￥ 자동생성
      - generic [ref=e68]:
        - generic [ref=e69]: 내역
        - textbox "내역" [ref=e70]
      - generic [ref=e71]:
        - generic [ref=e72]:
          - generic [ref=e73]: BRT견적번호
          - textbox "BRT견적번호" [ref=e74]
        - generic [ref=e75]:
          - generic [ref=e76]: BRT견적금액 (￦)
          - textbox "BRT견적금액 (￦)" [ref=e77]:
            - /placeholder: 금액 입력 시 ￦ 자동생성
        - generic [ref=e78]:
          - generic [ref=e79]: BRT견적날짜
          - textbox "BRT견적날짜" [ref=e80]:
            - /placeholder: YYYYMMDD
            - text: 2026-08-29
        - generic [ref=e81]:
          - generic [ref=e82]: BRT네고금액
          - textbox "BRT네고금액" [ref=e83]:
            - /placeholder: 텍스트 입력
      - generic [ref=e85]:
        - generic [ref=e86]: 상태
        - combobox "상태" [ref=e87]:
          - option "선택하세요" [selected]
          - option "견적중"
          - option "재견적"
          - option "견적제출"
          - option "발주예정"
      - generic [ref=e89]:
        - generic [ref=e90]:
          - heading "의뢰부품목록" [level=5] [ref=e91]
          - button " 부품 추가" [ref=e92] [cursor=pointer]:
            - generic [ref=e93]: 
            - text: 부품 추가
        - grid [ref=e95]:
          - rowgroup [ref=e96]:
            - rowgroup [ref=e97]:
              - row [ref=e98]:
                - columnheader "No." [ref=e99]
                - columnheader "부품명" [ref=e106]
                - columnheader "품번" [ref=e113]
                - columnheader "스펙" [ref=e120]
                - columnheader "수량" [ref=e127]
                - columnheader "삭제" [ref=e134]
          - rowgroup [ref=e142]:
            - row [ref=e143]:
              - gridcell "1" [ref=e144]
              - gridcell "CSS Part 1" [ref=e146]
              - gridcell "1234" [ref=e148]
              - gridcell "Spec A" [ref=e150]
              - gridcell "2" [ref=e152]
              - gridcell [ref=e154]:
                - button "" [ref=e155] [cursor=pointer]
            - row [ref=e158]:
              - gridcell "2" [ref=e159]
              - gridcell "CSS Part 2" [ref=e161]
              - gridcell "5678" [ref=e163]
              - gridcell "Spec B" [ref=e165]
              - gridcell "1" [ref=e167]
              - gridcell [ref=e169]:
                - button "" [ref=e170] [cursor=pointer]
      - generic [ref=e173]:
        - button "등록" [ref=e174] [cursor=pointer]
        - link "취소" [ref=e175] [cursor=pointer]:
          - /url: /quotes
```

# Test source

```ts
  1   | const { test, expect } = require('@playwright/test');
  2   | 
  3   | test.describe('Quote Parts Management', () => {
  4   |   let jobRequestId;
  5   |   let quoteId;
  6   | 
  7   |   test.beforeAll(async ({ browser }) => {
  8   |     // Create a JobRequest with parts first via API
  9   |     const context = await browser.newContext();
  10  |     const page = await context.newPage();
  11  |     
  12  |     // Create JobRequest with parts via form submission
  13  |     await page.goto('http://localhost:8080/jobs/new');
  14  |     
  15  |     // Fill JobRequest form
  16  |     await page.fill('input[name="jobNo"]', 'TEST-JOB-001');
  17  |     await page.selectOption('select[name="division"]', '바라텍');
  18  |     await page.fill('input[name="requester"]', 'Test User');
  19  |     await page.fill('input[name="requestDate"]', '2024-01-15');
  20  |     await page.fill('input[name="customerName"]', 'Test Customer');
  21  |     await page.fill('input[name="factoryName"]', 'Test Factory');
  22  |     
  23  |     // Submit the form
  24  |     await page.click('button[type="submit"]');
  25  |     
  26  |     // Wait for redirect to jobs list
  27  |     await page.waitForURL('**/jobs');
  28  |     
  29  |     // Find the created job and get its ID
  30  |     const editLink = await page.locator('a[href^="/jobs/edit/"]').first();
  31  |     const href = await editLink.getAttribute('href');
  32  |     jobRequestId = href.split('/').pop();
  33  |     
  34  |     // Now add parts to this job request by editing it
  35  |     await editLink.click();
  36  |     
  37  |     // Wait for edit page to load
  38  |     await page.waitForSelector('#job-parts-table', { timeout: 5000 });
  39  |     
  40  |     // Add parts via the parts table - we need to use the popup
  41  |     // For simplicity, let's add parts via the API directly
  42  |     await page.evaluate(async (jobId) => {
  43  |       const response = await fetch(`http://localhost:8080/jobs/${jobId}`, {
  44  |         method: 'POST',
  45  |         headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
  46  |         body: new URLSearchParams({
  47  |           jobNo: 'TEST-JOB-001',
  48  |           division: '바라텍',
  49  |           requester: 'Test User',
  50  |           requestDate: '2024-01-15',
  51  |           customerName: 'Test Customer',
  52  |           factoryName: 'Test Factory',
  53  |           partsJson: JSON.stringify([
  54  |             { partName: 'CSS Part 1', partNumber: '1234', spec: 'Spec A', quantity: 2, sortOrder: 0 },
  55  |             { partName: 'CSS Part 2', partNumber: '5678', spec: 'Spec B', quantity: 1, sortOrder: 1 }
  56  |           ])
  57  |         })
  58  |       });
  59  |       return response.text();
  60  |     }, jobRequestId);
  61  |     
  62  |     await context.close();
  63  |   });
  64  | 
  65  |   test('Create Quote with parts from JobRequest, modify, save, and verify', async ({ page }) => {
  66  |     // Navigate to new quote page
  67  |     await page.goto('http://localhost:8080/quotes/new');
  68  |     
  69  |     // Select the JobRequest and trigger change event
  70  |     await page.selectOption('select[name="jobRequestId"]', jobRequestId);
  71  |     await page.evaluate(() => {
  72  |       const select = document.querySelector('select[name="jobRequestId"]');
  73  |       select.dispatchEvent(new Event('change', { bubbles: true }));
  74  |     });
  75  |     
  76  |     // Wait for parts to load in Tabulator
  77  |     await page.waitForSelector('#jobRequestPartsTable .tabulator-row', { timeout: 15000 });
  78  |     
  79  |     // Verify initial parts loaded (should be 2 parts from JobRequest)
  80  |     const initialRows = await page.locator('#jobRequestPartsTable .tabulator-row').count();
  81  |     console.log('Initial rows:', initialRows);
  82  |     expect(initialRows).toBe(2);
  83  |     
  84  |     // Verify part data - use DOM directly since Tabulator API might not be ready
> 85  |     const firstPartName = await page.locator('#jobRequestPartsTable .tabulator-row').first().locator('td:nth-child(2)').textContent();
      |                                                                                                                         ^ Error: locator.textContent: Test timeout of 30000ms exceeded.
  86  |     console.log('First part name:', firstPartName);
  87  |     expect(firstPartName).toContain('CSS Part 1');
  88  |     
  89  |     // Delete first part
  90  |     await page.locator('#jobRequestPartsTable .tabulator-row').first().locator('button.btn-delete-part').click();
  91  |     
  92  |     // Verify only 1 part remains
  93  |     await page.waitForTimeout(500);
  94  |     const afterDeleteRows = await page.locator('#jobRequestPartsTable .tabulator-row').count();
  95  |     console.log('After delete rows:', afterDeleteRows);
  96  |     expect(afterDeleteRows).toBe(1);
  97  |     
  98  |     // Add a new part via console
  99  |     await page.evaluate(() => {
  100 |       const table = window.partsTable;
  101 |       if (table) {
  102 |         table.addRow({
  103 |           partName: 'New Added Part',
  104 |           partNumber: '9999',
  105 |           spec: 'New Spec',
  106 |           quantity: 3,
  107 |           sortOrder: table.getDataCount()
  108 |         }, true);
  109 |       }
  110 |     });
  111 |     
  112 |     await page.waitForTimeout(500);
  113 |     const afterAddRows = await page.locator('#jobRequestPartsTable .tabulator-row').count();
  114 |     console.log('After add rows:', afterAddRows);
  115 |     expect(afterAddRows).toBe(2);
  116 |     
  117 |     // Fill required quote fields
  118 |     await page.fill('input[name="ccsQuoteDate"]', '2024-01-20');
  119 |     await page.fill('input[name="ccsQuoteNo"]', 'QUOTE-001');
  120 |     await page.fill('input[name="ccsAmount"]', '100000');
  121 |     await page.selectOption('select[name="status"]', '견적중');
  122 |     
  123 |     // Submit form
  124 |     await page.click('button[type="submit"]');
  125 |     
  126 |     // Wait for redirect to quotes list
  127 |     await page.waitForURL('**/quotes');
  128 |     
  129 |     // Find the created quote and click edit
  130 |     const editLink = await page.locator('a[href^="/quotes/edit/"]').first();
  131 |     const editHref = await editLink.getAttribute('href');
  132 |     quoteId = editHref.split('/').pop();
  133 |     
  134 |     await editLink.click();
  135 |     
  136 |     // Wait for edit page to load
  137 |     await page.waitForSelector('#jobRequestPartsTable .tabulator-row', { timeout: 15000 });
  138 |     
  139 |     // Verify parts match what we saved (1 original + 1 new = 2 parts)
  140 |     const editRows = await page.locator('#jobRequestPartsTable .tabulator-row').count();
  141 |     console.log('Edit rows:', editRows);
  142 |     expect(editRows).toBe(2);
  143 |     
  144 |     // Verify the remaining original part and new part
  145 |     const partNames = await page.evaluate(() => {
  146 |       const table = window.partsTable;
  147 |       if (table) {
  148 |         return table.getRows().map(row => row.getData().partName);
  149 |       }
  150 |       return [];
  151 |     });
  152 |     console.log('Part names:', partNames);
  153 |     expect(partNames).toContain('CSS Part 2');
  154 |     expect(partNames).toContain('New Added Part');
  155 |     expect(partNames).not.toContain('CSS Part 1');
  156 |     
  157 |     console.log('Test passed: Quote parts saved and loaded correctly!');
  158 |   });
  159 | });
  160 | 
```