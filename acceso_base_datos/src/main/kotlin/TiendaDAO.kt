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

    fun insertarPedido(pedido: Pedido): Boolean {
        val sql = "INSERT INTO Pedido (precioTotal, idUsuario) VALUES (?, ?)"
        return try {
            conexion.prepareStatement(sql).use { ps ->
                ps.setDouble(1, pedido.precioTotal)
                ps.setInt(2, pedido.idUsuario)
                val filasAfectadas = ps.executeUpdate()
                filasAfectadas > 0
            }
        } catch (e: SQLException) {
            println("Error de SQL al insertar pedido: ${e.message}")
            false
        }
    }

    fun insertarLineaPedido(linea: LineaPedido): Boolean {
        val sql = "INSERT INTO LineaPedido (cantidad, precio, idPedido, idProducto) VALUES (?, ?, ?, ?)"
        return try {
            conexion.prepareStatement(sql).use { ps ->
                ps.setInt(1, linea.cantidad)
                ps.setDouble(2, linea.precio)
                ps.setInt(3, linea.idPedido)
                ps.setInt(4, linea.idProducto)
                val filasAfectadas = ps.executeUpdate()
                filasAfectadas > 0
            }
        } catch (e: SQLException) {
            println("Error de SQL al insertar línea de pedido: ${e.message}")
            false
        }
    }

    fun obtenerLineasDelPedido(idPedido: Int) {
        val sql = "SELECT * FROM LineaPedido WHERE idPedido = ?"
        try {
            conexion.prepareStatement(sql).use { ps ->
                ps.setInt(1, idPedido)
                val rs = ps.executeQuery()
                println("--- Líneas del Pedido ID: $idPedido ---")
                while (rs.next()) {
                    println("Línea ID: ${rs.getInt("id")} | Cantidad: ${rs.getInt("cantidad")} | Precio: ${rs.getDouble("precio")} | Producto ID: ${rs.getInt("idProducto")}")
                }
            }
        } catch (e: SQLException) {
            println("Error de SQL al consultar las líneas de pedido: ${e.message}")
        }
    }

    fun obtenerGastoTotalUsuario(nombreUsuario: String) {
        val sql = """
            SELECT SUM(p.precioTotal) AS total 
            FROM Pedido p 
            JOIN Usuario u ON p.idUsuario = u.id 
            WHERE u.nombre = ?
        """.trimIndent()
        try {
            conexion.prepareStatement(sql).use { ps ->
                ps.setString(1, nombreUsuario)
                val rs = ps.executeQuery()
                if (rs.next()) {
                    val total = rs.getDouble("total")
                    println("Suma total de los pedidos de '$nombreUsuario': $total €")
                }
            }
        } catch (e: SQLException) {
            println("Error de SQL al calcular el total del usuario: ${e.message}")
        }
    }

    fun obtenerUsuariosPorProducto(nombreProducto: String) {
        val sql = """
            SELECT DISTINCT u.nombre 
            FROM Usuario u
            JOIN Pedido pe ON u.id = pe.idUsuario
            JOIN LineaPedido lp ON pe.id = lp.idPedido
            JOIN Producto pr ON lp.idProducto = pr.id
            WHERE pr.nombre = ?
        """.trimIndent()
        try {
            conexion.prepareStatement(sql).use { ps ->
                ps.setString(1, nombreProducto)
                val rs = ps.executeQuery()
                println("--- Usuarios que compraron '$nombreProducto' ---")
                var tieneResultados = false
                while (rs.next()) {
                    tieneResultados = true
                    println("- ${rs.getString("nombre")}")
                }
                if (!tieneResultados) {
                    println("Nadie ha comprado este producto aún.")
                }
            }
        } catch (e: SQLException) {
            println("Error de SQL al consultar usuarios por producto: ${e.message}")
        }
    }
}