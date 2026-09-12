# Design Decisions — MediTrack

This document explains the key architectural and design choices made while building MediTrack, and the reasoning behind them.

## 1. Layered Architecture (Presentation → Service → Entity/Util)

`Main.java` never talks directly to `DataStore` or performs validation — it only calls `service` classes. Business rules (validation, ID generation, existence checks) live entirely in `service/`. This means the console UI could be swapped for a REST API or GUI later without touching any business logic, and business rules only need to be changed in one place.

## 2. `MedicalEntity` as a Separate Abstraction Layer Below `Person`

`Doctor` and `Patient` need `id`, `name`, `age`, `contactNumber`, and `address` — all naturally provided by `Person`. However, `Appointment` and `Bill` also need a unique `id` and a `getSummary()` contract, but have none of the "person" attributes (no name, age, etc.). Making them extend `Person` directly would have forced irrelevant fields onto them.

Instead, `MedicalEntity` was introduced as a minimal abstract class holding only `id` and an abstract `getSummary()`. `Person` extends `MedicalEntity` and adds person-specific fields; `Appointment` and `Bill` extend `MedicalEntity` directly, skipping the person-specific layer entirely. This keeps every class holding only the fields that are actually meaningful for what it represents.

## 3. Distinctly-Named Search Methods Instead of Forced Overloading

The brief requested `searchPatient()` overloaded by ID, name, and age. True Java method overloading requires different parameter types or counts — but ID and name are both `String`, meaning `searchPatient(String id)` and a hypothetical `searchPatient(String name)` would have an identical signature, which Java cannot distinguish. True overloading is only possible between the `String` (id) and `int` (age) versions.

Rather than force an awkward partial overload just to satisfy the letter of the requirement, distinctly-named methods (`searchPatient(String id)`, `searchPatientByName(String name)`, `searchPatientByAge(int age)`) were used across both `PatientService` and `DoctorService`. This favors code clarity and readability over blindly matching a pattern that isn't fully achievable in Java for this exact case.

## 4. Interfaces (`Payable`, `Searchable`) vs. Abstract Classes

`MedicalEntity` (abstract class) was used for shared **identity** — things that are fundamentally "the same kind of thing" in the domain. `Payable` and `Searchable` (interfaces) were used for shared **capability** — traits that cut across otherwise unrelated classes. `Bill` is `Payable` but has no reason to extend `Doctor` or `Patient`; `Doctor` and `Patient` are both `Searchable` despite representing very different domain concepts. Java's single-inheritance restriction on classes (but not interfaces) makes interfaces the correct tool whenever a capability needs to be shared across classes that aren't otherwise related.

## 5. Storing Full Objects (Not Just IDs) in `Appointment`

`Appointment` stores actual `Doctor` and `Patient` object references rather than just their ID strings. This was a deliberate trade-off: storing IDs and looking up fresh data via the service layer is generally safer and avoids stale data, but the brief explicitly required demonstrating deep-clone semantics on `Appointment`, which is only meaningful if there are actual nested mutable objects to clone. Storing full objects made the deep-clone requirement genuinely testable rather than trivial.

## 6. Deep Cloning Implementation

`Patient` and `Doctor` both implement `Cloneable` with a simple `super.clone()` — sufficient because all their fields are primitives, `String`, or enums, all of which are either immutable or safely shared by reference. `Appointment.clone()` goes further: after calling `super.clone()` (a shallow copy), it explicitly re-clones its nested `doctor` and `patient` fields, ensuring the cloned `Appointment` shares no mutable object references with the original — a genuine deep copy, not just a shallow one.

## 7. Checked Exceptions for Business-Rule Failures

`InvalidDataException` and `AppointmentNotFoundException` both extend `Exception` (checked), not `RuntimeException`. This was intentional: invalid input data and missing appointment lookups are expected, recoverable situations that calling code (ultimately `Main.java`) should be forced to explicitly handle — e.g., showing an error and returning to the menu, rather than allowing the program to fail unexpectedly if a developer forgets to handle them.

## 8. Immutability of `BillSummary`

`BillSummary` is declared `final` (the class itself, preventing subclassing that could override getters to break the immutability contract), with all fields `final` and no setters. Once a bill summary is generated, its values are permanently fixed — appropriate for something that represents a snapshot handed to a patient, which should never silently change after the fact.