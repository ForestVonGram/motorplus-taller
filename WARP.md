# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Overview

This repo is a simple monorepo with:
- backend: Java (Gradle) skeleton with DAO/Service/Controller layers and a JDBC `ConnectionManager`.
- frontend: React + TypeScript app bootstrapped with Vite.

## Commands

FrontEnd (Vite + React)
- Install deps: `cd frontend && npm ci` (or `npm install`)
- Dev server: `npm run dev`
- Build: `npm run build`
- Preview built app: `npm run preview`
- Lint: `npm run lint`
- Tests: no test runner is configured in `frontend/package.json`.

BackEnd (Gradle Java)
- Build (compile + tests): `cd backend && ./gradlew build`
- Clean: `./gradlew clean`
- Run all tests (JUnit 5): `./gradlew test`
- Run a single test: `./gradlew test --tests 'full.package.ClassName'` or `./gradlew test --tests 'full.package.ClassName.testMethod'`

Notes
- There is no configured application plugin or runnable Main; building produces a JAR but running it is not currently wired up.

## Configuration

Database configuration for the backend is read from a classpath resource at `backend/src/main/resources/application.properties` using `util.ConnectionManager`.
- Supported keys: `db.url`, `db.user`, `db.password`.
- Defaults in code (used if the properties file is missing or a key is absent):
  - `db.url = jdbc:postgresql://localhost:5432/motorplus`
  - `db.user = forest`
  - `db.password = ` (empty)

## High-level architecture

BackEnd (Java)
- util/ConnectionManager: Centralized JDBC connection factory. Loads DB settings from `application.properties` with in-code fallbacks.
- model/*: POJOs for domain entities (e.g., `Cliente`, `Vehiculo`, etc.).
- dao/*: Data-access classes intended to encapsulate SQL per entity; `BaseDAO` is currently a placeholder.
- service/*: Business-logic layer classes intended to orchestrate DAOs; currently mostly placeholders.
- controller/*: Entry-point layer for application/use-case orchestration; currently placeholders (no web framework is integrated).
- org/example/Main.java: IntelliJ template stub; not wired as an executable entry point.

FrontEnd (Vite React)
- `src/main.tsx` bootstraps the React app; `src/App.tsx` is the root component.
- Tooling: Vite config in `frontend/vite.config.ts`; TypeScript config in `frontend/tsconfig.json`.
- ESLint is available via `npm run lint`; no explicit ESLint config file is present in the repo.

## What to expect / gaps
- Backend classes are mostly scaffolds; persistence and business logic need implementation.
- No API/server framework is included; “controller” classes are plain Java classes.
- No automated tests exist yet in either project (Gradle is set up for JUnit 5).
