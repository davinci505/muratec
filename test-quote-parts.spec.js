const { test, expect } = require('@playwright/test');

test.describe('Quote Parts Management', () => {
  let jobRequestId;
  let quoteId;

  test.beforeAll(async ({ browser }) => {
    // Create a JobRequest with parts first via API
    const context = await browser.newContext();
    const page = await context.newPage();
    
    // Create JobRequest with parts via form submission
    await page.goto('http://localhost:8080/jobs/new');
    
    // Fill JobRequest form
    await page.fill('input[name="jobNo"]', 'TEST-JOB-001');
    await page.selectOption('select[name="division"]', '바라텍');
    await page.fill('input[name="requester"]', 'Test User');
    await page.fill('input[name="requestDate"]', '2024-01-15');
    await page.fill('input[name="customerName"]', 'Test Customer');
    await page.fill('input[name="factoryName"]', 'Test Factory');
    
    // Submit the form
    await page.click('button[type="submit"]');
    
    // Wait for redirect to jobs list
    await page.waitForURL('**/jobs');
    
    // Find the created job and get its ID
    const editLink = await page.locator('a[href^="/jobs/edit/"]').first();
    const href = await editLink.getAttribute('href');
    jobRequestId = href.split('/').pop();
    
    // Now add parts to this job request by editing it
    await editLink.click();
    
    // Wait for edit page to load
    await page.waitForSelector('#job-parts-table', { timeout: 5000 });
    
    // Add parts via the parts table - we need to use the popup
    // For simplicity, let's add parts via the API directly
    await page.evaluate(async (jobId) => {
      const response = await fetch(`http://localhost:8080/jobs/${jobId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({
          jobNo: 'TEST-JOB-001',
          division: '바라텍',
          requester: 'Test User',
          requestDate: '2024-01-15',
          customerName: 'Test Customer',
          factoryName: 'Test Factory',
          partsJson: JSON.stringify([
            { partName: 'CSS Part 1', partNumber: '1234', spec: 'Spec A', quantity: 2, sortOrder: 0 },
            { partName: 'CSS Part 2', partNumber: '5678', spec: 'Spec B', quantity: 1, sortOrder: 1 }
          ])
        })
      });
      return response.text();
    }, jobRequestId);
    
    await context.close();
  });

  test('Create Quote with parts from JobRequest, modify, save, and verify', async ({ page }) => {
    // Navigate to new quote page
    await page.goto('http://localhost:8080/quotes/new');
    
    // Select the JobRequest and trigger change event
    await page.selectOption('select[name="jobRequestId"]', jobRequestId);
    await page.evaluate(() => {
      const select = document.querySelector('select[name="jobRequestId"]');
      select.dispatchEvent(new Event('change', { bubbles: true }));
    });
    
    // Wait for parts to load in Tabulator
    await page.waitForSelector('#jobRequestPartsTable .tabulator-row', { timeout: 15000 });
    
    // Verify initial parts loaded (should be 2 parts from JobRequest)
    const initialRows = await page.locator('#jobRequestPartsTable .tabulator-row').count();
    console.log('Initial rows:', initialRows);
    expect(initialRows).toBe(2);
    
    // Verify part data - use DOM directly since Tabulator API might not be ready
    const firstPartName = await page.locator('#jobRequestPartsTable .tabulator-row').first().locator('td:nth-child(2)').textContent();
    console.log('First part name:', firstPartName);
    expect(firstPartName).toContain('CSS Part 1');
    
    // Delete first part
    await page.locator('#jobRequestPartsTable .tabulator-row').first().locator('button.btn-delete-part').click();
    
    // Verify only 1 part remains
    await page.waitForTimeout(500);
    const afterDeleteRows = await page.locator('#jobRequestPartsTable .tabulator-row').count();
    console.log('After delete rows:', afterDeleteRows);
    expect(afterDeleteRows).toBe(1);
    
    // Add a new part via console
    await page.evaluate(() => {
      const table = window.partsTable;
      if (table) {
        table.addRow({
          partName: 'New Added Part',
          partNumber: '9999',
          spec: 'New Spec',
          quantity: 3,
          sortOrder: table.getDataCount()
        }, true);
      }
    });
    
    await page.waitForTimeout(500);
    const afterAddRows = await page.locator('#jobRequestPartsTable .tabulator-row').count();
    console.log('After add rows:', afterAddRows);
    expect(afterAddRows).toBe(2);
    
    // Fill required quote fields
    await page.fill('input[name="ccsQuoteDate"]', '2024-01-20');
    await page.fill('input[name="ccsQuoteNo"]', 'QUOTE-001');
    await page.fill('input[name="ccsAmount"]', '100000');
    await page.selectOption('select[name="status"]', '견적중');
    
    // Submit form
    await page.click('button[type="submit"]');
    
    // Wait for redirect to quotes list
    await page.waitForURL('**/quotes');
    
    // Find the created quote and click edit
    const editLink = await page.locator('a[href^="/quotes/edit/"]').first();
    const editHref = await editLink.getAttribute('href');
    quoteId = editHref.split('/').pop();
    
    await editLink.click();
    
    // Wait for edit page to load
    await page.waitForSelector('#jobRequestPartsTable .tabulator-row', { timeout: 15000 });
    
    // Verify parts match what we saved (1 original + 1 new = 2 parts)
    const editRows = await page.locator('#jobRequestPartsTable .tabulator-row').count();
    console.log('Edit rows:', editRows);
    expect(editRows).toBe(2);
    
    // Verify the remaining original part and new part
    const partNames = await page.evaluate(() => {
      const table = window.partsTable;
      if (table) {
        return table.getRows().map(row => row.getData().partName);
      }
      return [];
    });
    console.log('Part names:', partNames);
    expect(partNames).toContain('CSS Part 2');
    expect(partNames).toContain('New Added Part');
    expect(partNames).not.toContain('CSS Part 1');
    
    console.log('Test passed: Quote parts saved and loaded correctly!');
  });
});
