
---

### Parking Space Service – README.md
```markdown
# Parking Space Service

## 📌 Description
The Parking Space Service manages individual parking spaces and their availability.

## 🏗️ Domain
Parking Management

## ⚙️ Main Features
- Assign available spaces.
- Update occupancy status.
- Track real-time availability.

## 🔗 Endpoints
- `POST /parking-spaces` → Register parking space.
- `GET /parking-spaces/{id}` → Retrieve space details.
- `PUT /parking-spaces/{id}` → Update space status.

## 🚀 Installation & Run
```bash
git clone <repo>
cd parking-space-service
mvn clean install
mvn spring-boot:run
