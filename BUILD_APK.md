# Build the Cravely APK without Android Studio

This project includes a GitHub Actions workflow that builds the APK on a GitHub-hosted Ubuntu runner.

## Fastest route

1. Create a new **GitHub repository** (for example `cravely-android`).
2. Upload all files from this project to the repository.
3. Make sure the branch is called `main` (or change the workflow if you use another branch).
4. Open the repository's **Actions** tab.
5. Select **Build Cravely APK**.
6. Click **Run workflow**.
7. Wait for the build to finish.
8. Open the completed workflow run.
9. Under **Artifacts**, download **cravely-debug-apk**.
10. Unzip it and install `app-debug.apk` on your Android phone.

The workflow uses JDK 17 and Gradle 8.9 and builds the existing Android app with Android Gradle Plugin 8.7.3.

## Important

This is a **debug APK** intended for testing the prototype. It is not a Play Store release build and is not signed with a personal release key.

The current V0.1 app is a local visual prototype. Its recommendation data is sample data embedded in the app; Zomato/Swiggy MCP and a real local database are not connected yet.
