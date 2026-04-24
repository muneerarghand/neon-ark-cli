# Neon Ark CLI

Java CLI application that simulates a front-end system for Neon Ark.

## Overview

* No database
* No backend
* No file writing
* All actions simulate API behavior

## Features

* Add New Warden (validated + simulated request)
* View Wardens (read-only CSV)
* Other actions are simulated

## Run

```bash
javac -d out src/com/neonark/*.java
java -cp out com.neonark.Main
```
