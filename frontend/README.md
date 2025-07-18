# InnovaTube Frontend - React + Vite

Un frontend moderno y elegante para la plataforma InnovaTube, construido con React, Vite y Tailwind CSS, con conexión SSL/TLS al backend Spring Boot.

## 🚀 Características

### ✅ Funcionalidades Implementadas

#### **🔐 Autenticación de Usuarios**
- ✅ Registro de usuarios con validación completa
- ✅ Inicio de sesión con username/email
- ✅ reCAPTCHA integrado para seguridad
- ✅ Validación de disponibilidad de username y email
- ✅ Sesiones persistentes con localStorage
- ✅ Cerrar sesión

#### **🎥 Gestión de Videos**
- ✅ Búsqueda de videos con **estrategia de fallback**
- ✅ Listado de videos más recientes
- ✅ Videos trending y populares
- ✅ Paginación de resultados
- ✅ Visualización de estadísticas de videos
- ✅ Integración con YouTube para reproducción

#### **❤️ Sistema de Favoritos**
- ✅ Marcar/desmarcar videos como favoritos
- ✅ Listado de favoritos del usuario
- ✅ Búsqueda en favoritos
- ✅ Eliminación de favoritos
- ✅ Favoritos trending

#### **🔄 Estrategia de Fallback**
- ✅ Búsqueda primaria en base de datos InnovaTube
- ✅ Fallback automático a YouTube API v3
- ✅ Transformación de datos entre fuentes
- ✅ Manejo de errores robusto

#### **🎨 UX/UI Profesional**
- ✅ Diseño responsive para todos los dispositivos
- ✅ Interfaz elegante y moderna con Tailwind CSS
- ✅ Navegación intuitiva similar a YouTube
- ✅ Animaciones suaves y transiciones
- ✅ Tema oscuro profesional
- ✅ Iconografía consistente con Lucide React

## 🛠️ Tecnologías Utilizadas

- **React 18** - Biblioteca de interfaz de usuario
- **Vite** - Herramienta de construcción rápida
- **React Router DOM** - Enrutamiento de la aplicación
- **React Query** - Gestión de estado del servidor
- **Axios** - Cliente HTTP para API
- **React Hook Form** - Gestión de formularios
- **React Hot Toast** - Notificaciones
- **Tailwind CSS** - Framework de CSS utilitario
- **Lucide React** - Iconografía moderna

## 🛠️ Instalación

### Prerrequisitos
- Node.js 16+ y npm
- Backend de InnovaTube ejecutándose en `https://localhost:8443`
- Conexión a internet para reCAPTCHA y YouTube API

### Pasos de Instalación

1. **Clonar o descargar el proyecto**
   ```bash
   cd frontend-react/
   ```

2. **Instalar dependencias**
   ```bash
   npm install
   ```

3. **Configurar variables de entorno (opcional)**
   
   Crea un archivo `.env` en la raíz del proyecto:
   ```env
   VITE_API_BASE_URL=https://localhost:8443/api/v1
   VITE_YOUTUBE_API_KEY=AIzaSyCSUq2lpG9MsVCnqKPwHt4A-Z4awgaphj0
   ```

4. **Ejecutar en modo desarrollo**
   ```bash
   npm run dev
   ```

5. **Acceder a la aplicación**
   
   Abre tu navegador y ve a:
   - `http://localhost:3000`

## 🚀 Despliegue

### Construir para producción
```bash
npm run build
```

### Servir la aplicación construida
```bash
npm run preview
```

### Integración con Spring Boot

Para integrar el frontend construido con tu backend Spring Boot:

1. **Construir el proyecto**
   ```bash
   npm run build
   ```

2. **Copiar la carpeta `dist`**
   - Copia todo el contenido de la carpeta `dist/`
   - Pégalo en `src/main/resources/static/` de tu proyecto Spring Boot

3. **Configurar Spring Boot**
   
   Asegúrate de que tu `application.properties` tenga:
   ```properties
   spring.web.resources.static-locations=classpath:/static/
   spring.mvc.static-path-pattern=/**
   ```

4. **Configurar rutas SPA**
   
   En tu `WebConfig.java` o similar, agrega:
   ```java
   @Override
   public void addViewControllers(ViewControllerRegistry registry) {
       registry.addViewController("/").setViewName("forward:/index.html");
       registry.addViewController("/{x:[\\w\\-]+}").setViewName("forward:/index.html");
       registry.addViewController("/{x:^(?!api$).*$}/**").setViewName("forward:/index.html");
   }
   ```

## 🔧 Configuración

### Variables de Configuración

En `src/lib/api.js`:
```javascript
const API_BASE_URL = 'https://localhost:8443/api/v1'; // URL de tu backend con SSL
const YOUTUBE_API_KEY = 'AIzaSyCSUq2lpG9MsVCnqKPwHt4A-Z4awgaphj0'; // Tu clave de YouTube API
```

### Personalización

#### **Colores del Tema**
Los colores principales están definidos en `tailwind.config.js`:
- Primario: `#ff6b6b` (rojo coral)
- Secundario: `#4ecdc4` (turquesa)
- Fondo: `#0f0f0f` (negro)
- Superficie: `#1a1a1a` (gris oscuro)

#### **Logo y Branding**
- Cambia el logo en `src/components/Header.jsx`
- Modifica el favicon en `public/`
- Personaliza el nombre "InnovaTube"

## 📱 Responsive Design

La aplicación está optimizada para:
- ✅ Desktop (1200px+)
- ✅ Tablet (768px - 1199px)
- ✅ Mobile (320px - 767px)
- ✅ Orientación portrait y landscape

## 🔌 Endpoints Utilizados

### Autenticación
- `POST /api/v1/auth/login` - Iniciar sesión
- `POST /api/v1/auth/logout` - Cerrar sesión
- `POST /api/v1/users/create` - Registrar usuario
- `GET /api/v1/users/availability/username` - Verificar username
- `GET /api/v1/users/availability/email` - Verificar email

### Videos
- `GET /api/v1/videos/latest` - Videos recientes
- `GET /api/v1/videos/trending` - Videos trending
- `GET /api/v1/videos/popular` - Videos populares
- `GET /api/v1/videos/search/title` - Búsqueda por título
- `GET /api/v1/videos/search/paginated` - Búsqueda paginada
- `GET /api/v1/videos/{id}/statistics` - Estadísticas

### Favoritos
- `GET /api/v1/favorites/user-videos/{userId}` - Favoritos del usuario
- `GET /api/v1/favorites/user-videos/paginated/{userId}` - Favoritos paginados
- `POST /api/v1/favorites/user-videos/add` - Agregar favorito
- `DELETE /api/v1/favorites/user-videos/remove` - Quitar favorito
- `GET /api/v1/favorites/user-videos/{userId}/check-favorited/{videoId}` - Verificar favorito

## 🎯 Estrategia de Fallback

### Implementación
1. **Búsqueda Primaria**: Se busca en la base de datos de InnovaTube
2. **Fallback Automático**: Si no hay resultados, se busca en YouTube API
3. **Transformación de Datos**: Los resultados de YouTube se adaptan al formato interno
4. **Manejo de Errores**: Se manejan errores de ambas fuentes

### Flujo de Búsqueda
```
Usuario busca → Base de datos InnovaTube → ¿Hay resultados? → Sí: Mostrar
                                                              No: YouTube API → Mostrar
```

## 🐛 Solución de Problemas

### Problemas Comunes

1. **No se conecta al backend**
   - Verifica que el backend esté ejecutándose en `https://localhost:8443`
   - Revisa la consola del navegador para errores CORS
   - Asegúrate de que el certificado SSL sea válido

2. **reCAPTCHA no funciona**
   - Verifica tu conexión a internet
   - En desarrollo, usa la clave de prueba incluida

3. **YouTube API no funciona**
   - Verifica que tengas una API key válida
   - Revisa los límites de cuota de tu API key

4. **Videos no se cargan**
   - Verifica la conexión a internet
   - Revisa la consola para errores específicos

### Debugging

Abre las herramientas de desarrollador (F12) y revisa:
- **Console**: Errores de JavaScript
- **Network**: Llamadas a la API
- **Application**: Almacenamiento local

## 🚀 Scripts Disponibles

```bash
# Desarrollo
npm run dev          # Iniciar servidor de desarrollo
npm run build        # Construir para producción
npm run preview      # Previsualizar build de producción
npm run lint         # Ejecutar ESLint

# Instalación
npm install          # Instalar dependencias
```

## 📄 Licencia

Este proyecto es parte de InnovaTube y está diseñado para uso educativo y comercial.

## 🤝 Contribución

Para contribuir al proyecto:
1. Fork el repositorio
2. Crea una rama para tu feature
3. Commit tus cambios
4. Push a la rama
5. Abre un Pull Request

## 📞 Soporte

Para soporte técnico o preguntas:
- Revisa la documentación del backend
- Consulta los logs del navegador
- Verifica la conectividad de red

---

**InnovaTube Frontend React** - Una experiencia de usuario moderna y elegante para la plataforma de videos. 