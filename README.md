# Ray Tracer Engine - Java Project

A robust and modular 3D rendering engine based on the **Ray Tracing** algorithm, developed as an academic project. This engine emphasizes high-quality software design, advanced rendering techniques, and performance optimization.

**Developed by:** Amitay Ben-Ami & Elad Elya

---

## 🚀 Key Features

### 1. Modular Geometry Engine
* Supports a wide variety of geometric primitives: **Spheres, Planes, Triangles, Polygons, Cylinders, and Tubes**.
* Built with a clean hierarchy using the `Intersectable` and `Geometry` interfaces for easy extensibility.

### 2. Advanced Lighting & Materials
* **Light Sources:** Implementation of Ambient, Directional, Point, and Spot lights.
* **Complex Materials:** Support for Diffuse and Specular components, as well as reflection and transparency (refraction) to create realistic textures (e.g., glass, mirrors).

### 3. High-End Rendering Techniques
* **Anti-Aliasing:** Smoothing edges for a polished, professional look.
* **Soft Shadows:** Implementation of blurry, realistic shadows using a distributed ray-tracing approach (via the `Blackboard` sampling mechanism).
* **Depth of Field (DoF):** Simulating real camera lens focus by blurring objects based on their distance from the focal plane.

### 4. Performance & Software Quality
* **Multi-threading:** Optimized rendering speed using a `PixelManager` to distribute the workload across multiple CPU cores, significantly reducing render time.
* **Test-Driven Development (TDD):** The project includes an extensive suite of **JUnit** tests covering all geometric calculations, lighting effects, and final scene renders to ensure maximum accuracy.
* **Clean Architecture:** Strict adherence to OOP principles and design patterns (Builder, Factory, Composite).

---

## 🖼️ Visual Showcase

### Soft Shadows & Reflections
The following render demonstrates our implementation of soft shadows, material reflections, and light interactions:

![Soft Shadows Result](https://github.com/amitaybenami/ISE5784_9458_1468/blob/master/soft%20shadows.jfif?raw=true)

---

## 🛠️ Tech Stack
* **Language:** Java
* **IDE:** IntelliJ IDEA
* **Testing:** JUnit 5
