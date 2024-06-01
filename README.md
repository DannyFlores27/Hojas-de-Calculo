Manual de Usuario
Introducción
Bienvenido al manual de usuario de la aplicación de hojas de cálculo. Este manual proporciona instrucciones detalladas sobre cómo utilizar la aplicación para crear, cargar y manipular hojas de cálculo.

Requisitos del Sistema
Para ejecutar la aplicación de hojas de cálculo, asegúrese de tener instalado lo siguiente:

Java Runtime Environment (JRE) versión 8 o superior.
Instalación
No se requiere instalación para ejecutar la aplicación de hojas de cálculo. Simplemente ejecute el archivo JAR proporcionado.

Funcionalidades Principales
La aplicación de hojas de cálculo proporciona las siguientes funcionalidades principales:

Crear un Nuevo Libro de Cálculo: Permite crear un nuevo libro de cálculo vacío.
Cargar un Libro de Cálculo Existente: Permite cargar un libro de cálculo previamente guardado desde un archivo.
Editar Hojas de Cálculo: Permite insertar, actualizar y eliminar celdas en una hoja de cálculo, así como realizar operaciones matemáticas básicas utilizando fórmulas.
Instrucciones de Uso
Inicio de la Aplicación:

Ejecute la aplicación.
Se mostrará el menú principal con dos opciones: "Nuevo Libro" y "Cargar Libro".
Crear un Nuevo Libro de Cálculo:

Haga clic en el botón "Nuevo Libro".
Se abrirá una nueva ventana con una hoja de cálculo vacía.
Puede editar las celdas de la hoja de cálculo haciendo clic en ellas y escribiendo el contenido deseado.
Cargar un Libro de Cálculo Existente:

Haga clic en el botón "Cargar Libro".
Se abrirá un cuadro de diálogo para seleccionar el archivo del libro de cálculo.
Seleccione el archivo deseado y haga clic en "Abrir".
Se cargará el libro de cálculo existente y se mostrará en una nueva ventana.
Editar Hojas de Cálculo:

Una vez que tenga abierto un libro de cálculo, puede realizar las siguientes acciones:
Haga clic en una celda para seleccionarla.
Escriba un valor directamente en la celda seleccionada.
Para realizar operaciones matemáticas, escriba una fórmula en una celda comenzando con el signo "=".
Por ejemplo: "=SUMA(A1,A2)" sumará los valores de las celdas A1 y A2.
Presione Enter para aplicar la fórmula.
Guardar el Libro de Cálculo:

El libro de cálculo se guarda automáticamente al cerrar la aplicación.
Si desea guardar manualmente, cierre la aplicación y confirme cuando se le solicite.
Solución de Problemas
Si experimenta algún problema con la aplicación, intente lo siguiente:

Verifique que tenga instalado Java Runtime Environment (JRE) versión 8 o superior.
Asegúrese de estar utilizando la última versión de la aplicación.
Si el problema persiste, comuníquese con el soporte técnico.
Contacto

¡Gracias por utilizar la aplicación de hojas de cálculo!





Manual Técnico
Introducción
El manual técnico proporciona información detallada sobre la estructura interna y el funcionamiento de la aplicación de hojas de cálculo.

Arquitectura
La aplicación sigue una arquitectura de tres capas:

Capa de Presentación: Interfaz de usuario construida con Java Swing.
Capa de Lógica de Negocios: Lógica de aplicación que maneja la creación, carga y manipulación de hojas de cálculo.
Capa de Datos: Almacena los datos de las hojas de cálculo, utilizando archivos JSON para la persistencia.
Componentes Principales
Clase HojasDeCalculo: Contiene el método main y el menú principal de la aplicación.
Clase HojaDeCalculo: Representa una hoja de cálculo con una tabla de celdas y métodos para procesar entradas y operaciones matemáticas.
Clase Celda: Representa una celda en la hoja de cálculo con un identificador único y un valor asociado.
Clase MatrizOrtogonal: Estructura de datos que almacena las celdas de la hoja de cálculo en una matriz ortogonal.
Interacción de Componentes
La clase HojasDeCalculo maneja la interfaz de usuario y la lógica de presentación.
La clase HojaDeCalculo maneja la lógica de negocio relacionada con la manipulación de celdas y fórmulas.
La clase Celda representa las celdas individuales de la hoja de cálculo.
La clase MatrizOrtogonal proporciona una estructura de datos eficiente para almacenar las celdas de la hoja de cálculo.
Almacenamiento de Datos
Los datos de las hojas de cálculo se almacenan en archivos JSON utilizando la clase ObjectMapper de Jackson.
La clase LibroDeCalculo se encarga de cargar y guardar los datos de las hojas de cálculo desde y hacia archivos.
Dependencias
La aplicación depende de las siguientes bibliotecas externas:

javax.swing: Para la interfaz de usuario.
jackson-databind: Para el manejo de archivos JSON.
Pruebas
Se han realizado pruebas unitarias para garantizar el funcionamiento correcto de las clases principales y las funcionalidades principales de la aplicación.

Limitaciones Conocidas
La aplicación actualmente tiene un tamaño de hoja de cálculo fijo (20x20 celdas) que puede ser ampliado en futuras versiones.
No se admiten todas las funciones de una hoja de cálculo avanzada como Excel.
Contacto

¡Gracias por contribuir al desarrollo de la aplicación de hojas de cálculo!
