# PlanMe

PlanMe es una aplicaci贸n m贸vil para dispositivos Android que permite a usuarios conocer nuevos planes y experiencias en la ciudad, filtrarlos y tener la oportunidad de planearlos mediante un sistema de reservas centralizado. La aplicaci贸n ofrece una interfaz elegante, simple y concisa para presentar dichos planes.

## App Preview

<img src="https://raw.githubusercontent.com/amoralesc/planme/main/screenshots/app_preview.gif" width="300">

<div style="float: left">
  <img src="https://raw.githubusercontent.com/amoralesc/planme/main/screenshots/screenshot_1.jpg" width="200">
  <img src="https://raw.githubusercontent.com/amoralesc/planme/main/screenshots/screenshot_2.jpg" width="200">
  <img src="https://raw.githubusercontent.com/amoralesc/planme/main/screenshots/screenshot_3.jpg" width="200">
</div>

## Especificaciones 馃搵

La aplicaci贸n est谩 desarrollada para dispositivos Android con una versi贸n SDK m铆nima de 24.

## Tech Stack 馃洜锔?

* [Kotlin](https://kotlinlang.org/) - Lenguaje de desarrollo
* [Android Studio](https://developer.android.com/studio) - Herramienta y entorno de desarrollo
* [Firebase](https://firebase.google.com/) - Servicios de autenticaci贸n, base de datos, almacenamiento cloud

## Instalaci贸n 馃摝

### Pre-rrequisitos 馃摎

Este proyecto est谩 construido con tecnolog铆as Firebase y Google Maps Platforms. Para instalar el proyecto, debe:

1. Contar con Android Studio y el plugin de Kotlin instalado.
2. Contar una _API de Google Maps_ y un _google-services.json_ con la informaci贸n de conexi贸n con Firebase.

### Pasos de instalaci贸n 馃摎

1. Cree un nuevo [proyecto en Google Cloud](https://cloud.google.com/resource-manager/docs/creating-managing-projects).

2. Habilite las APIs de Google Maps Platform y tome nota de la clave API.

3. Cree un nuevo [proyecto de Firebase enlazado al de GCP](https://firebase.google.com/firebase-and-gcp).

4. Clone el repositorio y entre al proyecto de Android Studio.

```
git clone https://github.com/amoralesc/UWheels.git
```

5. Coloque su Google Maps API en el archivo _local.properties_ bajo el nombre ```MAPS_API_KEY```.

```
MAPS_API_KEY=[your_api_key]
```

6. Conecte su proyecto de Android Studio a su [proyecto de Firebase](https://firebase.google.com/docs/android/setup). Se puede utilizar el asistente de Firebase de Android Studio ubicado en Tools > Firebase.

### Licencia 馃摑

Este proyecto est谩 bajo licencia Apache 2.0. Una copia de la licencia es adjuntada. El uso de la marca, el logo y el nombre de la aplicaci贸n (PlanMe) bajo cualquier motivo est谩 expl铆citamente prohibido sin previa autorizaci贸n.
