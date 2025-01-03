# BuyBuy E-Commerce Application

BuyBuy is a project aimed at simulating the operation of e-commerce applications. This project includes a set of features that develop the basic functions of e-commerce platforms and improve user experience. The app is equipped with modern Android features such as RecyclerView working with different ViewTypes, Deep Links, Push Notifications, Hilt, Multimodule Architecture, Shimmer, Lottie, Glide, and Custom Views, and follows the MVVM architecture.

## Features
- **RecyclerView with Different ViewTypes**: Dynamically displaying data on the main screen using RecyclerView with different layouts for each type.
- **Deep Links**: Allows users to be directed to specific pages via external links to the app.
- **Push Notifications**: Firebase Cloud Messaging (FCM) is used to send notifications to users. This enables real-time communication with users through updates and notifications.
- **Work Manager**: Messages sent via FCM are also automatically sent at specific intervals, and if the message is a coupon message, a random coupon is defined, and a notification with a deep link is sent.
- **Hilt Integration**: Efficient dependency management within the app using Hilt.
- **Multimodule Architecture**: The app is partially modularized, and each module can be managed independently.
- **Shimmer Animation**: Shimmer animation is shown to improve the user experience when data loads slowly.
- **Lottie Animations**: Lottie library is used for animated content, providing visual interaction.
- **Glide Integration**: Glide library is used for efficient and fast image loading.
- **Custom Views**: The custom coupon view was created to allow users to access coupon information in an easy and aesthetic way, enhancing the user experience.
- **Dark Mode Support**: The app automatically works in dark mode when the system settings are set to dark mode.
- **Turkish Language Support**: The app supports the Turkish language and can be viewed in Turkish based on the user's device language settings.

## Application Demo

The video below demonstrates the usage and functionality of the BuyBuy e-commerce application:

[![BuyBuy E-Commerce Application - Demo Video](https://img.youtube.com/vi/4OF06yXryG8/0.jpg)](https://youtu.be/4OF06yXryG8)

You can visually explore the app's features, user experience, and functionality.

## Installation Instructions

Follow these steps to run the project on your local machine:

### 1. Clone the Project

First, clone the project to your local machine:


git clone https://github.com/serhatkutlu/buybuy.git

### 2. Add Google Services JSON File
To use Google services, obtain the `google-services.json` file and add it to the `app` folder.

### 3. Open the Project in Android Studio
Open Android Studio and use the `File > Open` menu to open the project. Once the project is opened, the necessary dependencies will be automatically downloaded.

### 4. Update Dependencies
To update the dependencies used in the project, click the "Sync Now" button in the top right corner of Android Studio.

### 5. Run the Project
To run the project on your local device:

- Connect your device or launch an emulator.
- Click the "Run" button in Android Studio to start the app.

### 6. Configuration and Running
When the project runs, you will see a RecyclerView on the main screen displaying products, each with a different ViewType.

## Technologies and Libraries Used
- **Hilt**: Dependency injection.
- **Firebase Cloud Messaging (FCM)**: Push notifications.
- **Glide**: Image loading.
- **Lottie**: Animations.
- **Shimmer**: Transparent animations.
- **Deep Links**: In-app redirection.
- **Room Database**: Database management.
- **Retrofit**: API requests.

## Contributing
If you'd like to contribute to the project, please follow these steps:

1. Fork this repo.
2. Create a new branch (`git checkout -b feature-xyz`).
3. Commit your changes (`git commit -am 'Add new feature'`).
4. Push your branch (`git push origin feature-xyz`).
5. Open a pull request.
