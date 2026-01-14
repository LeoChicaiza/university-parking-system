
---

### Notification Service – README.md
```markdown
# Notification Service

## 📌 Description
The Notification Service sends system notifications related to entry, exit, and billing events.

## 🏗️ Domain
Billing, Notification & Reporting

## ⚙️ Main Features
- Send notifications via email/SMS.
- Notify users about billing and parking events.

## 🔗 Endpoints
- `POST /notifications` → Send notification.
- `GET /notifications/{id}` → Retrieve notification status.

## 🚀 Installation & Run
```bash
git clone <repo>
cd notification-service
mvn clean install
mvn spring-boot:run
