# android-github-top-stars

 Android project created to apply concepts of Clean Architecture and SOLID with MVVM architecture and using some jetpack components as: Room, Retrofit, Paging 3.0, Dagger and Flow. 

 - The tests (unit and instrumented) were writen using libraries as Mokito, Mockk and Espresso.

The main feature performs a query to the GitHub repositories API to seach and show the Kotlin repositories witch have more stars, besides it's creates a local cache in database to avoid unnecessary request overloads.
