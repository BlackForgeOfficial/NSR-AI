```markdown
# NSR-AI Security Policy

This document outlines the security policies and guidelines for developing and using addons with the NSR-AI plugin. Our primary goal is to ensure the stability, integrity, and security of the server environment and the NSR-AI ecosystem.

## Core Principles

*   **Server Control:** We do not control your server. Your full power and control over the plugin remain unchanged.
*   **Addon Integrity:** Security measures are implemented to protect against malicious, suspicious, or non-compliant addons.
*   **API Key Protection:** The NSR-AI API is designed to prevent the exposure or misuse of API keys and sensitive information.

## Addon Compliance and Bans

Addons or dependent plugins that contain malware, suspicious code, or violate our terms of service will be subject to security measures, including dynamic detaching from the NSR-AI core. Failure to comply with these guidelines will result in a ban.

### Prohibited Actions for Addons:

As per the Commons Clause in our `LICENSE.txt`, and for security reasons, addons are strictly prohibited from:

*   **Creating or redistributing offline AI integrations:** This includes systems for Ollama, LLaMA, local models, or any other method that bypasses our intended AI processing.
*   **Adding API key expansions:** You may NOT implement direct API key expansions for external services (e.g., Gemini, OpenAI, Claude) through your addon. Only the official NSR-AI backend can be used for secure API key handling.
*   **Creating scripted response systems:** Addons may NOT create pre-made or canned AI response systems that mimic or replace the dynamic AI behavior of NSR-AI.
*   **Forking or re-implementing core functionality:** You may NOT fork or re-implement the core functionality of NSR-AI to bypass its monetization, security, or intended functionality.
*   **Storing or revealing API keys:** Addons must NEVER store, log, or reveal API keys or other sensitive information obtained from or used by the NSR-AI plugin.

## Security Updates

*   **Bug Fixes:** Bug fixes require a server restart to take effect.
*   **Dynamic Security Updates:** Security updates are applied dynamically without requiring a server restart. This allows us to instantly reload or patch dependent addons and plugins if a security vulnerability or non-compliant behavior is detected.

## Reporting Security Issues

If you discover a security vulnerability or suspect a non-compliant addon, please report it immediately to [Your Security Contact/Email/Discord] to help us maintain a secure environment for all users.

---
*This policy is subject to change. Please refer to the latest version for up-to-date information.*
```