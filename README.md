# PlanMe

PlanMe es una aplicación móvil para dispositivos Android que permite a usuarios conocer nuevos planes y experiencias en la ciudad, filtrarlos y tener la oportunidad de planearlos mediante un sistema de reservas centralizado. La aplicación ofrece una interfaz elegante, simple y concisa para presentar dichos planes.

## Especificaciones 📋

La aplicación está desarrollada para dispositivos Android con una versión SDK mínima de 24.

## Tech Stack 🛠️

* [Kotlin](https://kotlinlang.org/) - Lenguaje de desarrollo
* [Android Studio](https://developer.android.com/studio) - Herramienta y entorno de desarrollo
* [Firebase](https://firebase.google.com/) - Servicios de autenticación, base de datos, almacenamiento cloud

## Instalación 📦

### Pre-rrequisitos 📚

Este proyecto está construido con tecnologías Firebase y Google Maps Platforms. Para instalar el proyecto, debe:

1. Contar con Android Studio y el plugin de Kotlin instalado.
2. Contar una _API de Google Maps_ y un _google-services.json_ con la información de conexión con Firebase.

### Pasos de instalación 📚

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

### Licencia 📝

Este proyecto está bajo licencia Apache 2.0. Una copia de la licencia es adjuntada. El uso de la marca, el logo y el nombre de la aplicación (PlanMe) bajo cualquier motivo está explícitamente prohibido sin previa autorización.
