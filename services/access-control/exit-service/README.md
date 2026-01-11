
---

### Exit Service – README.md
```markdown
# Exit Service

## 📌 Description
The Exit Service registers vehicle exits, releases parking spaces, and triggers billing.

## 🏗️ Domain
Access Control

## ⚙️ Main Features
- Register vehicle exit.
- Release parking space.
- Notify Billing Service.

## 🔗 Endpoints
- `POST /exit` → Register vehicle exit.
- `GET /exit/{id}` → Retrieve exit details.

## 🚀 Installation & Run
```bash
git clone <repo>
cd exit-service
mvn clean install
mvn spring-boot:run
