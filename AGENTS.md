# Spring Boot CRUD Project Instructions

This is a Spring Boot + Thymeleaf CRUD application for user management.

## Project Structure
- Maven-based Spring Boot 3.x application
- H2 in-memory database
- Thymeleaf for server-side rendering
- Bootstrap for UI styling

## Technology Stack
- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- Thymeleaf
- H2 Database
- Bootstrap 5

## Frontend & Data Grid Guidelines

### Tabulator v5 Integration Rules
- **Library Standard**: Always use **Tabulator v5+** for data tables and grid components. Do not use commercial or non-MIT grid libraries.
- **Event Handling**: Use Tabulator v5 event syntax (e.g., `table.on("rowClick", ...)` instead of v4 callbacks).
- **Styling**: Integrate Tabulator with Bootstrap 5 themes or clean minimal CSS.

### Data Binding Standards
- **Server-Rendered Initial Data**: Bind Spring Model attributes using Thymeleaf inline JS syntax: `/*[[${users}]]*/ []`.
- **Asynchronous / Large Data**: Use Tabulator AJAX features (`ajaxURL: "/api/..."`) connected to Spring REST Controllers.
- **CRUD & Editing**:
  - Enable inline cell editing where appropriate using Tabulator native editors.
  - Implement standard actions (Edit/Delete) using custom column formatters or event listeners.
  
## Test Rules
- 테스트한 다음에는 반드시 어플리케이션 종료할 것.
- 테스트시, 인코딩은 UTF8로 할 것.