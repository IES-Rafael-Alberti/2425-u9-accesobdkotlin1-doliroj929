fun main() {

    /*ejercicio 1*/

    println("Iniciando programa de base de datos...")
    GestorBaseDeDatos.crearTablas()

    GestorBaseDeDatos.conectar()?.use { conexion ->
        try {
            if (conexion.isValid(2)) {
                println("------------------------------------------")
                println("¡ÉXITO! Conectado a H2 en modo fichero.")
                println("------------------------------------------")
            }
        } catch (e: java.sql.SQLException) {
            println("Error al verificar la conexión inicial: ${e.message}")
        }
    }


    /*ejercicio 2*/

    GestorBaseDeDatos.conectar()?.use { conexion ->
        val tiendaDao = TiendaDAO(conexion)

        println("\n--- PROCESANDO USUARIOS ---")
        val usuariosAInsertar = listOf(
            Usuario(nombre = "Facundo Pérez", email = "facuper@mail.com"),
            Usuario(nombre = "Ataulfo Rodríguez", email = "ataurod@mail.com"),
            Usuario(nombre = "Cornelio Ramírez", email = "Cornram@mail.com")
        )

        for (usuario in usuariosAInsertar) {
            val exito = tiendaDao.insertarUsuario(usuario)
            if (exito) {
                println("-> Guardado: ${usuario.nombre}")
            }
        }

        println("\n--- PROCESANDO PRODUCTOS ---")
        val productosAInsertar = listOf(
            Producto(nombre = "Ventilador", precio = 10.0, stock = 2),
            Producto(nombre = "Abanico", precio = 150.0, stock = 47),
            Producto(nombre = "Estufa", precio = 24.99, stock = 1)
        )

        for (producto in productosAInsertar) {
            val exito = tiendaDao.insertarProducto(producto)
            if (exito) {
                println("-> Guardado: ${producto.nombre}")
            }
        }

        println("\n--- PROCESANDO PEDIDOS ---")
        val pedidosAInsertar = listOf(
            Pedido(precioTotal = 160.0, idUsuario = 2),
            Pedido(precioTotal = 20.0, idUsuario = 1),
            Pedido(precioTotal = 150.0, idUsuario = 2)
        )

        for (pedido in pedidosAInsertar) {
            tiendaDao.insertarPedido(pedido)
        }

        println("\n--- PROCESANDO LÍNEAS DE PEDIDO ---")
        val lineasAInsertar = listOf(
            LineaPedido(cantidad = 1, precio = 10.0, idPedido = 1, idProducto = 1),
            LineaPedido(cantidad = 1, precio = 150.0, idPedido = 1, idProducto = 2),
            LineaPedido(cantidad = 2, precio = 20.0, idPedido = 2, idProducto = 1),
            LineaPedido(cantidad = 1, precio = 150.0, idPedido = 3, idProducto = 2)
        )

        for (linea in lineasAInsertar) {
            tiendaDao.insertarLineaPedido(linea)
        }


        /*ejercicio 3*/

        println("\n--- CONSULTA 1 ---")
        tiendaDao.obtenerLineasDelPedido(1)

        println("\n--- CONSULTA 2 ---")
        tiendaDao.obtenerGastoTotalUsuario("Ataulfo Rodríguez")

        println("\n--- CONSULTA 3 ---")
        tiendaDao.obtenerUsuariosPorProducto("Abanico")

    } ?: println("No se pudo iniciar el programa porque la conexión falló.")
}