#!/bin/bash
sed -i 's/import com.example.features.documents.DocumentsScreen/import com.example.features.files.FilesScreen/' app/src/main/java/com/example/features/main/MainScreen.kt
sed -i 's/DocumentsScreen()/FilesScreen()/' app/src/main/java/com/example/features/main/MainScreen.kt
sed -i 's/SettingsScreen()/SettingsScreen(isDarkTheme = isDarkTheme, onThemeToggle = onThemeToggle)/' app/src/main/java/com/example/features/main/MainScreen.kt
