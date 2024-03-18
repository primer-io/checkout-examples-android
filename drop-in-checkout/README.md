# 💳 DropIn Checkout Example

This example demonstrates how to integrate support for DropIn Checkout in your Android app.

## Getting Started

To run the example app:

1. Clone the repo:
```sh
git clone https://github.com/primer-io/checkout-examples-android.git
```
2. Open the project in Android Studio 🚀

Select `drop-in-checkout` configuration and run the project.

3. Setup the client token server

Refer to the instructions provided in the [example-backend Readme](https://github.com/primer-io/checkout-example-backend/blob/main/README.md) 
to set up the server for generating the client token needed to initialize the SDK.

----

This project requires a server to communicate with Primer's API. To get started quickly, we encourage you to use the [companion backend](https://github.com/primer-io/checkout-example-backend).

#### Setting Checkout Backend URL

- You can set the URL of the checkout backend to initiate the checkout generated in step 3.
- The application provides an input field where you can input this URL or you can set the `BASE_URL` field defined
  in the [ClientSessionService](src/main/java/io/primer/checkout/cobadged/configuration/data/api/ClientSessionService.kt#L26).
- When the URL is set, you can request new client token in the example app.

#### Manually Created Client Token

- On the initial screen of your application, there's an option to manually input a client token.
- Paste the client token generated specifically for your integration to start the checkout process.

## Understanding the integration

We have followed a very simple Android architectural principles as describe in Android [documentation](https://developer.android.com/topic/architecture).

We have used following stack:

- Hilt for DI
- Retrofit for API calls
- Jetpack Compose + ViewModels on the UI/presentation layer

For easier separation of concerns, application was split into:

### Repositories

We have organized our code into two repositories to streamline the integration process:

#### 1. Primer Headless Initialization and Events

- [PrimerDropInRepository](src/main/java/io/primer/checkout/dropin/checkout/data/repository/PrimerDropInRepository.kt) 
  contains the necessary code for initializing the Primer Universal Checkout SDK and managing checkout lifecycle events.
- Use this repository to set up the base structure and manage Primer Universal Checkout events within your application.


### UI/Presentation

We have organized our code into two ViewModels to streamline the integration process:

#### 1. Primer DropIn Initialization and Events

- [CheckoutConfigurationViewModel](src/main/java/io/primer/checkout/cobadged/configuration/viewmodel/CheckoutConfigurationViewModel.kt)
  focuses on retrieving and validating client token needed for SDK initialization.

