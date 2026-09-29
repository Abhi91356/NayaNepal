# NayaNepal Security & Compliance Framework 🛡️

## 1. Data Privacy (PII)
NayaNepal adheres to international data protection standards (GDPR-like) and local Nepal regulations.
- **PII Masking**: Sensitive user data (email, phone) is masked in system logs using `SecurityUtils`.
- **Encryption at Rest**: 
    - Passwords: BrCypt with cost factor 12.
    - Identity Documents: AES-256 encrypted before cloud storage.
- **Right to be Forgotten**: Implementation of a 30-day "Soft Delete" period before permanent data erasure.

## 2. Network & API Security
- **HMAC Request Signing**: Critical operations (Wallet, Identity) require an `X-SIGNATURE` header generated using `HMAC-SHA256`.
- **SSL Pinning**: Mandated for all production traffic to prevent MITM attacks.
- **Rate Limiting**:
    - IP-based: 100 req / 15 min.
    - User-based: Throttling for sensitive actions (OTP, Login).

## 3. Authentication (MFA)
- **Biometric Integration**: TEE (Trusted Execution Environment) backed `BiometricPrompt` implementation.
- **JWT Lifecycles**: 15-minute access tokens with 7-day encrypted refresh tokens.

## 4. Compliance Checklist
- [x] OWASP Top 10 mitigation applied.
- [x] Data residency within compliant borders.
- [x] Privacy Policy & Terms of Service reachable in-app.
- [x] Multi-module isolation to prevent data leaks between pillars.
