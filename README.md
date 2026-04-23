# Neon Ark CLI

Java CLI app that simulates a front-end system.

No database, no backend, no file writing.

## Features
- Add New Warden (validated + simulated request)
- View Wardens (read-only CSV)
- Other actions are simulated

## Rules
- Data is read-only
- No saving
- All actions simulate API behavior

## Run
javac -d out src/com/neonark/*.java
java -cp out com.neonark.Main