fun main() {

    GestorBaseDeDatos.crearTablas()


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

    } ?: println("No se pudo iniciar el programa porque la conexión falló.")
}