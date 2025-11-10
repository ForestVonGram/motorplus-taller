# Páginas del Proyecto MotorPlus

## ✅ Páginas Creadas

### 1. **Login** (`/login`)
- Página de autenticación con diseño moderno
- Formulario con usuario y contraseña
- Redirige a `/dashboard` al iniciar sesión

### 2. **Dashboard** (`/dashboard`)
- Vista principal con estadísticas clave:
  - Total de vehículos
  - Órdenes activas
  - Mecánicos disponibles
  - Facturación del mes
- Tarjetas con hover effect

### 3. **Vehículos** (`/vehiculos`)
- Tabla estilo captura de pantalla con:
  - Logo de marca del vehículo
  - Información del vehículo (marca + modelo)
  - Cliente asociado
  - Matrícula
  - Cantidad de órdenes
  - Fecha última orden
  - Botón "En taller"
- Botón "+ Nuevo Vehículo"

### 4. **Clientes** (`/clientes`)
- Lista de clientes con:
  - Nombre completo
  - Email
  - Teléfono
  - Cantidad de vehículos (badge)
  - Fecha de registro
- Botón "Ver Detalle"

### 5. **Órdenes de Trabajo** (`/ordenes`)
- Lista de órdenes con:
  - Número de orden
  - Vehículo
  - Cliente
  - Mecánicos asignados
  - Estado (badge con colores)
  - Fecha
- Estados: Pendiente, En Proceso, Completada, Facturada

### 6-10. **Páginas Placeholder**
Páginas básicas creadas (mensaje "en construcción"):
- Mecánicos (`/mecanicos`)
- Facturas (`/facturas`)
- Repuestos (`/repuestos`)
- Proveedores (`/proveedores`)
- Servicios (`/servicios`)
- Reportes (`/reportes`)

## 🎨 Componentes Reutilizables

### `DataTable`
Componente de tabla configurable usado en todas las páginas de listado:
- Props:
  - `columns`: definición de columnas (key, header, render)
  - `data`: array de datos
  - `onAction`: callback para el botón de acción
  - `actionLabel`: texto del botón (ej: "En taller", "Ver Detalle")
- Estilos consistentes con la captura
- Hover effects
- Badges de colores

### `Layout`
Layout principal con:
- Sidebar de navegación con iconos
- Indicador visual de página activa
- Diseño responsive

## 🎯 Próximos Pasos

### Para completar las páginas CRUD:

1. **Mecánicos**
   - Crear `MecanicosList.tsx` similar a `ClientesList.tsx`
   - Campos: nombre, especialidad, experiencia, estado

2. **Repuestos**
   - Crear `RepuestosList.tsx`
   - Campos: código, nombre, proveedor, stock, precio

3. **Proveedores**
   - Crear `ProveedoresList.tsx`
   - Campos: nombre, RUT/NIF, contacto, repuestos suministrados

4. **Servicios**
   - Crear `ServiciosList.tsx`
   - Campos: código, nombre, descripción, precio base

5. **Facturas**
   - Crear `FacturasList.tsx`
   - Campos: número, orden, cliente, monto, estado pago, fecha

### Para las páginas de Detalle/Edición:

Crear componentes de formulario para:
- `VehiculoForm.tsx` - formulario crear/editar vehículo
- `ClienteForm.tsx` - formulario crear/editar cliente
- `OrdenForm.tsx` - formulario complejo para órdenes (con selección de servicios, mecánicos, repuestos)
- Etc.

### Para Reportes:

1. **Reportes Simples**
   - Historial de vehículo
   - Listado de facturas por cliente

2. **Reportes Intermedios**
   - Rendimiento de mecánicos
   - Inventario de repuestos por proveedor

3. **Reportes Complejos**
   - Análisis de rentabilidad
   - Trazabilidad completa de órdenes
   - Supervisión de mecánicos

4. **Exportación PDF**
   - Instalar librería como `jspdf` o `react-pdf`
   - Agregar botón "Exportar a PDF" en cada reporte

## 🚀 Para Correr el Proyecto

```bash
# Instalar dependencias
cd frontend
npm ci

# Modo desarrollo
npm run dev

# Build para producción
npm run build

# Preview de build
npm run preview
```

## 📝 Notas Técnicas

- **React Router** configurado para todas las rutas
- **TypeScript** con tipos definidos para cada entidad
- **CSS Modules** approach con archivos `.css` por componente
- Los logos de marcas se cargan desde CDN (WorldVectorLogo)
- Datos mock en cada página - reemplazar con llamadas API reales

## 🎨 Paleta de Colores

- **Azul primario**: #007bff
- **Gris claro**: #f8f9fa
- **Gris medio**: #6c757d
- **Texto oscuro**: #212529
- **Badges**: success (#2b8a3e), warning (#f59f00), danger (#c92a2a), primary (#1971c2)

## 📐 Estructura de Archivos

```
src/
├── components/
│   ├── DataTable.tsx        # Tabla reutilizable
│   ├── DataTable.css
│   ├── Layout.tsx           # Layout con sidebar
│   └── Layout.css
├── pages/
│   ├── login/
│   │   ├── Login.tsx
│   │   └── Login.css
│   ├── dashboard/
│   │   ├── Dashboard.tsx
│   │   └── Dashboard.css
│   ├── vehiculos/
│   │   ├── VehiculosList.tsx
│   │   └── VehiculosList.css
│   ├── clientes/
│   │   └── ClientesList.tsx
│   ├── ordenes/
│   │   └── OrdenesList.tsx
│   └── ... (otras páginas)
├── App.tsx                  # Router principal
├── index.css               # Estilos globales
└── main.tsx                # Entry point
```
