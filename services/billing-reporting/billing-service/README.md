
---

## 💵 Billing, Notification & Reporting Domain

### Billing Service – README.md
```markdown
# Billing Service

## 📌 Description
The Billing Service calculates parking duration and generates charges.

## 🏗️ Domain
Billing, Notification & Reporting

## ⚙️ Main Features
- Calculate parking fees.
- Generate invoices.
- Provide billing records.

## 🔗 Endpoints
- `POST /billing` → Generate bill.
- `GET /billing/{id}` → Retrieve bill details.

## 🚀 Installation & Run
```bash
git clone <repo>
cd billing-service
mvn clean install
mvn spring-boot:run
