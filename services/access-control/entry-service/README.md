
---

## 🚦 Access Control Domain

### Entry Service – README.md
```markdown
# Entry Service

## 📌 Description
The Entry Service validates vehicle entry and assigns available parking spaces.

## 🏗️ Domain
Access Control

## ⚙️ Main Features
- Validate authorized vehicles.
- Assign parking spaces.
- Register entry events.

## 🔗 Endpoints
- `POST /entry` → Register vehicle entry.
- `GET /entry/{id}` → Retrieve entry details.

## 🚀 Installation & Run
```bash
git clone <repo>
cd entry-service
mvn clean install
mvn spring-boot:run
