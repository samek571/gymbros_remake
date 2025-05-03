# Gym Tracker – User Documentation

## Requirements
- Linux OS (might not work on windows in the full spectre)
- Java 21+ (JDK)
- Maven
- Terminal access

---

## How to Compile and Run

1. Obtain (Clone or download) the project.
2. Open a terminal in the project root.
3. Compile and run:

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.samuel.gymtracker.App"
```

> If Maven Exec Plugin isnt configured:
```bash
cd target/classes
java com.samuel.gymtracker.App
```

4. Data is stored in:
   - `src/main/resources/custom_exercises.json`
   - `src/main/resources/custom_templates.json`
   - `src/main/resources/workout_history.json`
   - `src/main/resources/muscle_progress.json`

---

## Main Menu Options

```
=== Gym Tracker ===
[Enter] Start workout
1. View workout history
2. Settings
3. Exit
```

As it might suggest:
- `[Enter]` – Starts a workout session
- `1` – Insight into workout history
- `2` – Opens settings menu
- `3` – Quits

---

## Settings Menu

```
=== Settings ===
1. List exercises
2. Add custom exercise
3. Delete custom exercise
4. List exercises by muscle group
5. View muscle progress
6. Create custom template
7. Back to main menu
```

---

## Custom Templates

In the template sandbox (Settings > Create custom template):
- Named routines can be created, exercises must exist in the catalog thats a condition
- Listing or deleting templates

---

## Workout Session
- Start a workout
- load a routine template.
- Log exercises:

```
[Commands] log / finish / abort
```

For each entry, input:
- Name
- Reps
- Weight
- Seconds (optional, can be skipped by pressing `Enter`)

Finish to compute XP and log history.

---

## Muscle Progress

Tracks data throught history:
- Total reps
- Total time
- Total logged entries

Can be fount in Settings > Muscle Progress

---

## Dev Notes

- Javadoc output in `/docs/`
- Source packages under `com.samuel.gymtracker.*`
- Tests located in `src/test/java/...`
- Compatible with Java 21 and Maven

## Dev informal notes at the end
- I hate this fucking language. 
- This is a remake of current Liftoff (former Gymbros) gym tracking application, I am was forced to use java for this project.
- Luckily I can implement things that og app lacks, unfortunately its really fucking ugly language and I hate OOP therefore I wont implement much of that.
- After finishing templates (last imlpementation in favor to grading) I realized the treshold for passing is 40kB including javadocs. I thought for a long time that command `sh -du` is giving size in B so i divided by8 and kept going... Only problem is the command is garbage and doesnt say shit, accidently made 2.5time more than it was necesarry in this fucking ugly language.
- I changed specialization thanks to this java course I took at uni. I am doing theoretical informatics now. Unfortunately it is mandatory to take some sort of OOP lang so I had to finish what I started, otherwise I would be a quitter.