# Sistema de Alquiler de Vehículos

Aplicación de consola en Java que gestiona el registro de vehículos, clientes y alquileres, con persistencia en SQL Server via JDBC.

---

## Estructura del proyecto

```
src/
├── Main.java
├── db/
│   └── ConexionBD.java
├── dto/
│   ├── Persona.java
│   ├── Cliente.java
│   ├── Producto.java
│   ├── Vehiculo.java
│   ├── Alquiler.java
│   └── DetalleAlquiler.java
├── interfaces/
│   ├── IBuscable.java
│   └── IRegistrable.java
├── dao/
│   ├── ClienteDAO.java
│   ├── VehiculoDAO.java
│   └── AlquilerDAO.java
└── view/
    ├── Utilidades.java
    ├── MenuClientes.java
    ├── MenuVehiculos.java
    └── MenuAlquiler.java
```

---

## Modelo de datos (DTOs)

Las clases del paquete `dto` representan las entidades del dominio. Se aplicó herencia para reflejar relaciones reales entre conceptos:

- `Persona` es una clase abstracta con los atributos comunes de cualquier persona (nombres, apellidos, teléfono, email) y el método abstracto `mostrarInfo()`. `Cliente` la extiende agregando cédula, licencia, dirección y fecha de registro.

- `Producto` es otra clase abstracta con nombre, precio y stock, más el método abstracto `mostrarDetalle()` y el método concreto `validarStock(int cantidad)`. `Vehiculo` la extiende con los atributos propios del vehículo (marca, modelo, año, placa, etc.) e implementa `calcularCostoAlquiler(int dias)`.

- `Alquiler` agrupa un `Cliente`, una fecha de devolución y una lista de `DetalleAlquiler`. El método `agregarDetalle(Vehiculo, int dias)` crea el detalle y recalcula el total automáticamente sumando los subtotales de cada línea.

- `DetalleAlquiler` representa una línea del alquiler: un vehículo, la cantidad de días y el subtotal calculado como `precioDia * cantidadDias`.

---

## Conexión a la base de datos

`ConexionBD` implementa el patrón Singleton: tiene un constructor privado y un método estático `getConexion()` que retorna siempre la misma instancia de `Connection`. Si la conexión es nula o está cerrada, la crea; de lo contrario, la reutiliza. Esto evita abrir múltiples conexiones durante la ejecución.

---

## Capa DAO

El patrón DAO (Data Access Object) separa completamente la lógica de acceso a base de datos del resto de la aplicación. Cada DAO recibe la conexión desde `ConexionBD` en su constructor y es el único responsable de ejecutar SQL para su entidad.

Las interfaces `IBuscable` e `IRegistrable` definen el contrato que deben cumplir los DAOs:

- `IRegistrable` exige implementar `registrar(Object obj)` y `listarTodos()`.
- `IBuscable` exige implementar `buscarPorId(int id)` y `buscarPorNombre(String nombre)`.

### ClienteDAO

Implementa `IBuscable` e `IRegistrable`. Además de los métodos de la interfaz, expone `buscarPorCedula(String cedula)` que es el método principal usado en el flujo de alquiler para validar que el cliente exista. Todos los `ResultSet` se procesan a través de un método privado `mapearCliente(ResultSet rs)` que construye el objeto `Cliente` a partir de las columnas, manteniendo el código de cada método limpio.

### VehiculoDAO

Igual que `ClienteDAO`, usa un `mapearVehiculo(ResultSet rs)` privado. Adicionalmente expone `listarConStock()` que retorna una `List<Vehiculo>` con solo los vehículos disponibles, y `listarPorTipo(String tipoNombre)` que filtra por el nombre del tipo usando `LIKE`. Tiene también `descontarStock(Connection conTran, int idVehiculo, int cantidad)` que recibe la conexión activa de la transacción externa en lugar de abrir la propia, para poder participar del mismo `BEGIN TRAN` que maneja `AlquilerDAO`.

### AlquilerDAO

Es el DAO más complejo porque su operación de registro involucra múltiples tablas y debe ser atómica. El método `registrar(Object obj)` funciona así:

1. Desactiva el autocommit con `con.setAutoCommit(false)`.
2. Inserta el registro en la tabla `alquiler` y recupera el ID generado con `Statement.RETURN_GENERATED_KEYS`.
3. Por cada `DetalleAlquiler` en el alquiler, inserta una fila en `detalle_alquiler` y llama a `vehiculoDAO.descontarStock(con, ...)` pasando la misma conexión, para que el descuento de stock ocurra dentro de la misma transacción.
4. Si algún descuento de stock falla (el UPDATE no afecta filas porque el stock llegó a 0), hace `con.rollback()` y retorna `false`, cancelando todo.
5. Si todo fue bien, hace `con.commit()`.

De esta manera, nunca queda un alquiler registrado sin su correspondiente descuento de stock, ni un stock descontado sin alquiler.

`listarPorCliente(int idCliente)` usa un JOIN entre `alquiler`, `detalle_alquiler` y `vehiculo`, y agrupa visualmente los detalles por alquiler en consola, detectando cambio de `id_alquiler` en el loop del `ResultSet`.

---

## Capa View

### Utilidades

Clase de métodos estáticos de utilidad para la lectura por consola:

- `leerInt(Scanner sc)` — lee una línea e intenta parsearla como entero. Si falla, muestra un mensaje y vuelve a pedir. Nunca lanza una excepción al llamador.
- `leerDouble(Scanner sc)` — igual para valores decimales.
- `leerCampo(Scanner sc, String etiqueta)` — imprime la etiqueta, lee el input y si viene vacío informa que el campo es obligatorio y vuelve a pedirlo. Se usa para todos los campos que no pueden quedar en blanco (cédula, nombres, apellidos, licencia).

### Menús

Cada clase de menú (`MenuVehiculos`, `MenuClientes`, `MenuAlquiler`) recibe por constructor el `Scanner` y los DAOs que necesita. Esto permite que `Main` instancie los DAOs una sola vez y los pase a los menús, evitando instancias duplicadas.

El método público de cada clase es `mostrarMenu()`, que contiene el loop `do-while` con la impresión del submenú y el `switch` que llama a los métodos privados de acción. Todos los métodos de acción son privados porque son detalles internos del menú: nadie fuera de la clase necesita llamarlos directamente.

- `MenuVehiculos` delega en `VehiculoDAO` para registrar, buscar y listar vehículos.
- `MenuClientes` delega en `ClienteDAO`. En `registrarCliente` usa `leerCampo` para cédula, nombres, apellidos y licencia; deja opcionales teléfono, email y dirección.
- `MenuAlquiler` es el más complejo: coordina `ClienteDAO` para validar al cliente, `VehiculoDAO` para mostrar disponibles y `AlquilerDAO` para confirmar. La cédula también se lee con `leerCampo` para no permitir búsquedas vacías.

### Main

`Main` es exclusivamente el punto de entrada. Instancia los tres DAOs y los tres menús, e implementa el loop del menú principal delegando en cada menú según la opción elegida. No contiene lógica de negocio ni de I/O más allá del menú principal.