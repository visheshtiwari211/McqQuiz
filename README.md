The application presents a streamlined two-screen quiz experience. Users begin on the MCQ Screen, where questions are fetched from the backend, rendered with Jetpack Compose, and backed by Room for persistence. As users answer or skip questions, the app updates correctness, skipped state, and streak metrics in real time while ensuring state survives app restarts. Once all questions have been addressed, the user is transitioned to the Result Screen, which summarizes overall performance—including total score, longest streak, and skipped questions—and provides an option to restart the quiz by resetting all stored answer states. This flow ensures a responsive, consistent, and offline-capable quiz interaction supported by reactive data updates.

## Technologies Used

* **Jetpack Compose** – Declarative UI for rendering MCQs, animations, and result components.  
* **MVVM Architecture** – Clean separation of UI, business logic, and state management via ViewModel + StateFlow.  
* **Retrofit** – Network layer for fetching quiz questions from the API.  
* **Room Database** – Local persistence of quiz questions and user progress (answered/skipped/correct).  
* **Kotlin Coroutines & Flow** – Asynchronous execution and reactive UI updates with non-blocking data streams.
