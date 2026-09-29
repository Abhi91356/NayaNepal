# NayaNepal Backend Architecture 🛡️

## 1. Overview
NayaNepal transitioned from a monolithic backend to a secure, scalable, and cloud-native **Microservices Architecture**.

## 2. Components

### 🛡️ API Gateway (Entry Point)
- **Technology**: Express.js with `express-rate-limit`.
- **Responsibilities**:
  - Request routing to microservices.
  - Rate limiting (100 requests / 15 min per IP).
  - Centralized JWT validation.
  - Unified error response formatting.

### ⚙️ Core Microservices
Each service is independently deployable via Docker:
- **Auth Service**: User identity and session management.
- **Wallet Service**: Ledger, balances, and payment processing.
- **Marketplace Service**: Catalog and order management.
- **Learning Service**: Educational modules and progress.

### 🗄️ Data Layer
- **PostgreSQL**: Primary transactional database.
- **Redis**: High-performance caching for sessions and popular items.
- **RabbitMQ**: Message bus for asynchronous event-driven communication (e.g., triggering a notification after a payment).

## 3. System Interactions

### Authentication Flow
1. Client sends credentials to `api-gateway`.
2. Gateway routes to `auth-service`.
3. Gateway attaches JWT to subsequent requests.

### Event-Driven Communication
- When `wallet-service` completes a transaction, it publishes a `wallet.transaction.completed` event.
- `notification-service` (subscriber) picks up the event and sends a push notification to the user via FCM.

## 4. Error Handling
Standardized JSON format for all services:
```json
{
  "success": false,
  "error": {
    "code": "ERROR_CODE",
    "message": "Human readable message",
    "details": {}
  },
  "timestamp": "ISO_TIMESTAMP",
  "requestId": "unique_request_id"
}
```
