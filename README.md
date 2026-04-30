# MyQuizApplication

A beginner-friendly Java quiz mini project built with:
- Core Java
- JDBC
- MySQL
- Console UI
- Swing UI

## Package
- `com.miniproject.quizapp`

## Features
- Login system with `ADMIN` and `USER` roles
- Admin can create quizzes and manage questions
- User can attempt quizzes and view scores
- Console-based version
- Swing-based version
- Layered structure: model, dao, service, ui, config
- Basic validations
- Randomized question order for quiz attempts
- Simple timer in console mode
- Launch mode can be chosen from `Main` interactively

## Project Structure
- `src/main/java/com/miniproject/quizapp/config` - DB configuration and connection
- `src/main/java/com/miniproject/quizapp/model` - Entity classes
- `src/main/java/com/miniproject/quizapp/dao` - JDBC database access
- `src/main/java/com/miniproject/quizapp/service` - Business logic
- `src/main/java/com/miniproject/quizapp/ui` - Console and Swing applications
- `src/main/resources/application.properties` - DB credentials
- `src/main/resources/schema.sql` - Database schema and seed users

## Database Setup
1. Start MySQL.
2. Create the database and tables by running `src/main/resources/schema.sql`.
3. Update `src/main/resources/application.properties` with your local MySQL username and password.

Default seeded users:
- Admin: `admin / admin123`
- User: `alice / alice123`
- User: `bob / bob123`

## Build
```powershell
mvn compile
```

## Run With Interactive Mode Selection
If you run `Main` without arguments, it will ask whether you want console mode or Swing mode.

```powershell
mvn exec:java -Dexec.mainClass=com.miniproject.quizapp.Main
```

## Run Console Version Directly
```powershell
mvn exec:java -Dexec.mainClass=com.miniproject.quizapp.Main -Dexec.args=console
```

## Run Swing Version Directly
```powershell
mvn exec:java -Dexec.mainClass=com.miniproject.quizapp.Main -Dexec.args=swing
```

## Notes
- Passwords are stored as plain text for simplicity because this is a beginner mini project.
- The console version includes a basic time limit per quiz.
- The Swing UI is intentionally simple and functional.
