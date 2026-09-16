# Cravely — Food Surprise AI (V0.1)

A local-first Android prototype for the food intelligence product we designed.

## Included screens
- Home / action hub
- Order for Me
- Find My Next Favourite
- What Should I Try Next?
- Best Discounts for Me
- Food spending insights
- Food profile / Food DNA

## Build an APK without Android Studio

This repository includes a GitHub Actions workflow that builds a real debug APK on a GitHub-hosted Android build environment.

See **BUILD_APK.md** for the exact steps. In short: upload this project to GitHub → open **Actions** → run **Build Cravely APK** → download the `cravely-debug-apk` artifact.

## Local-first design
V0.1 intentionally has no cloud backend. The prototype is structured so local data can later be replaced/augmented by Zomato/Swiggy MCP and an LLM adapter.

## Current prototype limitation
The UI currently uses sample food/order data embedded in the app. It does not yet persist real order history locally or connect to Zomato/Swiggy MCP.


## V0.2 rebuild

This revision replaces the rough static interaction model with animated screen transitions, functional bottom navigation, press feedback, food imagery, working Home cards, detail navigation, back navigation, and animated Insights/Profile content. The UI remains a local prototype with sample data.

Animation direction: short FastOutSlowIn transitions, subtle scale/alpha movement, staggered recommendation reveals, animated chart bars, and touch feedback.
