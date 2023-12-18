# 💳 Co-Badged Cards Example

This example demonstrates how to integrate support for co-badged cards in your Android app.

## Getting Started

To run the example app:

1. Clone the repo:
```sh
git clone https://github.com/primer-io/checkout-examples-android.git
```
2. Open the project in Android Studio 🚀

Select `co-badged-cards` configuration and run the project.

3. Setup the client token (TODO)

## Understanding the integration


To initiate the checkout process, follow these steps:

### 1. Obtain Client Token or Checkout Backend URL

#### Manually Created Client Token

- On the initial screen of your application, there's an option to manually input a client token.
- Paste the client token generated specifically for your integration to start the checkout process.

#### Setting Checkout Backend URL

- Alternatively, you can set the URL of the checkout backend to initiate the checkout.
- The application provides an input field where you can input this URL.

### 2. Client Token Validation

Once the client token is provided:

- The example application will verify the validity of the client token.
- If the token is valid, the example app will proceed to create a card input form for the checkout process.

### Repositories

We have organized our code into two repositories to streamline the integration process:

#### 1. Primer Headless Initialization and Events

- This [repository](co-badged-cards/src/main/java/io/primer/checkout/cobadged/checkout/data/repository/PrimerHeadlessRepository.kt) contains the necessary code for initializing the Primer Headless SDK and managing events.
- Use this repository to set up the base structure and manage Primer Headless events within your application.

Repository Link: [Primer Headless Init and Events](https://github.com/your-primer-headless-init-events-repo)

#### 2. Card Input Functions using `PrimerHeadlessRawDataManager`

- This [repository](co-badged-cards/src/main/java/io/primer/checkout/cobadged/checkout/data/repository/CardInputRepository.kt) focuses specifically on card input functions leveraging the `PrimerHeadlessRawDataManager`.
- It provides functions and utilities for handling card inputs during the checkout process.

Repository Link: [Card Input Functions Repository](https://github.com/your-card-input-functions-repo)
