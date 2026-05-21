import java.sql.Connection
import java.sql.SQLException

class TiendaDAO(private val conexion: Connection) {

    fun insertarUsuario(usuario: Usuario): Boolean {
        val sql = "INSERT INTO Usuario (nombre, email) VALUES (?, ?)"
        return try {
            conexion.prepareStatement(sql).use { ps ->
                ps.setString(1, usuario.nombre)
                ps.setString(2, usuario.email)
                val filasAfectadas = ps.executeUpdate()
                filasAfectadas > 0
            }
        } catch (e: SQLException) {
            println("Error de SQL al insertar usuario (${usuario.nombre}): ${e.message}")
            false
        }
    }

    fun insertarProducto(producto: Producto): Boolean {
        val sql = "INSERT INTO Producto (nombre, precio, stock) VALUES (?, ?, ?)"
        return try {
            conexion.prepareStatement(sql).use { ps ->
                ps.setString(1, producto.nombre)
                ps.setDouble(2, producto.precio)
                ps.setInt(3, producto.stock)
                val filasAfectadas = ps.executeUpdate()
                filasAfectadas > 0
            }
        } catch (e: SQLException) {
            println("Error de SQL al insertar producto (${producto.nombre}): ${e.message}")
            false
        }
    }
}