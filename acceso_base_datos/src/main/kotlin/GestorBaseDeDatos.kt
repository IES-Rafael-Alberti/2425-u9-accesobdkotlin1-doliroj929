import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.nio.file.Path

object GestorBaseDeDatos {

    private val rutaBase: Path = Path.of("datos", "tienda_db").toAbsolutePath().normalize()
    private val url = "jdbc:h2:file:$rutaBase;AUTO_SERVER=TRUE"
    private const val USUARIO = "sa"
    private const val CONTRASENA = ""

    fun conectar(): Connection? {
        return try {
            val directorio = rutaBase.parent.toFile()
            if (!directorio.exists()) {
                directorio.mkdirs()
            }
            DriverManager.getConnection(url, USUARIO, CONTRASENA)
        } catch (e: SQLException) {
            println("ERROR SQL al conectar: ${e.message}")
            null
        }
    }

    fun crearTablas() {
        val sqlUsuario = """
            CREATE TABLE IF NOT EXISTS Usuario (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nombre VARCHAR(255) NOT NULL,
                email VARCHAR(255) UNIQUE
            );
        """.trimIndent()

        val sqlProducto = """
            CREATE TABLE IF NOT EXISTS Producto (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nombre VARCHAR(255) NOT NULL,
                precio DECIMAL NOT NULL,
                stock INT NOT NULL
            );
        """.trimIndent()

        val sqlPedido = """
            CREATE TABLE IF NOT EXISTS Pedido (
                id INT AUTO_INCREMENT PRIMARY KEY,
                precioTotal DECIMAL NOT NULL,
                idUsuario INT,
                FOREIGN KEY (idUsuario) REFERENCES Usuario(id)
            );
        """.trimIndent()

        val sqlLineaPedido = """
            CREATE TABLE IF NOT EXISTS LineaPedido (
                id INT AUTO_INCREMENT PRIMARY KEY,
                cantidad INT NOT NULL,
                precio DECIMAL NOT NULL,
                idPedido INT,
                idProducto INT,
                FOREIGN KEY (idPedido) REFERENCES Pedido(id),
                FOREIGN KEY (idProducto) REFERENCES Producto(id)
            );
        """.trimIndent()

        conectar()?.use { conexion ->
            try {
                conexion.createStatement().use { stmt ->
                    stmt.execute(sqlUsuario)
                    stmt.execute(sqlProducto)
                    stmt.execute(sqlPedido)
                    stmt.execute(sqlLineaPedido)
                    println("Tablas creadas o verificadas con éxito.")
                }
            } catch (e: SQLException) {
                println("Error al crear las tablas: ${e.message}")
            }
        }
    }
}