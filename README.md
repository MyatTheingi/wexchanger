# Wexchanger App

This Currency Conversion App is designed to provide seamless and efficient currency exchange information to users. Built with modern Android development practices, the app follows an architecture focused on scalability, maintainability, and performance.

## Core Technologies:

- **Dependency Injection (Dagger Hilt)**:
  Using Dagger Hilt for efficient and scalable dependency management, ensuring loose coupling and ease of testing.

- **Worker (for Background Tasks)**:
  Using Worker to perform background data synchronization every 30 minutes, ensuring up-to-date currency rates without disrupting the user experience.

- **MVVM (Model-View-ViewModel) and Clean Architecture**:
  Adopting MVVM to separate concerns between UI and business logic, ensuring maintainable and testable code. The app follows Clean Architecture principles for better modularity and easier code management.

- **ROOM (for Data Storage)**:
  Utilizing ROOM for local data storage, ensuring fast and reliable access to currency data even when offline.

- **Jetpack Compose for UI**:
  Using Jetpack Compose for a modern, declarative UI, allowing for dynamic and responsive interfaces with easy binding between UI components and ResultAPIs.

## Features:

- **Currency List Fetching**:
  Data is fetched from a remote source and stored locally using ROOM for offline access.

- **Currency Conversion**:
  Real-time currency conversion with accurate rates fetched periodically.

- **User-Friendly Interface**:
  The app's interface is built with Jetpack Compose, offering a responsive, fluid, and modern design.

## Screenshots:

<div style="display: flex; overflow-x: auto; gap: 16px;">
    <img src="https://github.com/MyatTheingi/wexchanger/blob/main/Screenshot_20250106_084450.png" alt="Screenshot 1" width="300" />
    <img src="https://github.com/MyatTheingi/wexchanger/blob/main/Screenshot_20250106_084545.png" alt="Screenshot 2" width="300" />
    <img src="https://github.com/MyatTheingi/wexchanger/blob/main/Screenshot_20250106_084607.png" alt="Screenshot 3" width="300" />
</div>