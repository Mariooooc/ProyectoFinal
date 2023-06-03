import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
public class Cliente {
    // Datos de configuración de la base de datos
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gimnasio";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);
        int opcion = 0;

        // Mostrar menú de opciones
        System.out.println("1. VER DATOS");
        System.out.println("2. INGRESAR DATOS");
        System.out.println("3. ACTUALIZAR DATOS");
        System.out.println("4. ELIMINAR DATOS");
        System.out.println("Ingrese número de opción: ");
        opcion = read.nextInt();


        try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            switch (opcion) {
                case 1:
                    verDatos(connection);
                    break;
                case 2:
                    ingresarDatos(connection);
                    break;
                case 3:
                    actualizarDatos(connection);
                    break;
                case 4:
                    eliminarDatos(connection);
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static void verDatos(Connection connection) {
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM clientes")) {

            // Recorrer los resultados de la consulta y mostrar los datos
            while (rs.next()) {
                System.out.println(rs.getShort(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getDate(4));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener los datos: " + e.getMessage());
        }
    }

    private static void ingresarDatos(Connection connection) {
        try (Scanner registro = new Scanner(System.in)) {
            String nombres, apellidos, fechaString;

            int id = obtenerNuevoID(connection);

            // Solicitar información al usuario
            System.out.println("Ingrese nombres de la persona: ");
            nombres = registro.nextLine();
            System.out.println("Ingrese apellidos de la persona: ");
            apellidos = registro.nextLine();
            System.out.println("Ingrese fecha de inscripción de la persona (YYYY-MM-DD): ");
            fechaString = registro.nextLine();

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaDate = format.parse(fechaString);
            java.sql.Date sqlDate = new java.sql.Date(fechaDate.getTime());

            // Preparar y ejecutar la consulta SQL para insertar los datos
            String sql = "INSERT INTO clientes VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.setString(2, nombres);
                ps.setString(3, apellidos);
                ps.setDate(4, sqlDate);

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Registro agregado con éxito.");
                } else {
                    System.out.println("No se pudo agregar el registro.");
                }
            } catch (SQLException e) {
                System.out.println("Error al ingresar los datos: " + e.getMessage());
            }
        } catch (ParseException e) {
            System.out.println("Fecha ingresada en un formato incorrecto. Se esperaba YYYY-MM-DD.");
        }
    }

    private static void actualizarDatos(Connection connection) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ingrese el ID del cliente a actualizar: ");
            int id = scanner.nextInt();

            // Consultar los datos del cliente a actualizar
            String sql = "SELECT * FROM clientes WHERE id_cliente = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int idIgual=id;
                        System.out.println("Cliente encontrado:");
                        System.out.println(rs.getShort(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getDate(4));
                        scanner.nextLine();

                        // Solicitar la nueva información al usuario
                        System.out.println("Ingrese los nuevos nombres de la persona: ");
                        String nuevosNombres = scanner.nextLine();
                        System.out.println("Ingrese los nuevos apellidos de la persona: ");
                        String nuevosApellidos = scanner.nextLine();
                        System.out.println("Ingrese la nueva fecha de inscripción de la persona (YYYY-MM-DD): ");
                        String nuevaFechaString = scanner.nextLine();

                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date nuevaFechaDate = format.parse(nuevaFechaString);
                        java.sql.Date nuevaSqlDate = new java.sql.Date(nuevaFechaDate.getTime());

                        // Preparar y ejecutar la consulta SQL para actualizar los datos
                        String updateSql = "UPDATE clientes SET nombres_cliente = ?, apellidos_cliente = ?, fecha_de_pago = ? WHERE id_cliente = ?";
                        try (PreparedStatement updatePs = connection.prepareStatement(updateSql)) {
                            updatePs.setString(1, nuevosNombres);
                            updatePs.setString(2, nuevosApellidos);
                            updatePs.setDate(3, nuevaSqlDate);
                            updatePs.setInt(4, id);

                            int rowsAffected = updatePs.executeUpdate();
                            if (rowsAffected > 0) {
                                System.out.println("Registro actualizado con éxito.");
                            } else {
                                System.out.println("No se pudo actualizar el registro.");
                            }
                        }
                    } else  {
                        System.out.println("ERROR");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error al actualizar los datos: " + e.getMessage());
            }
        } catch (ParseException e) {
            System.out.println("Fecha ingresada en un formato incorrecto. Se esperaba YYYY-MM-DD.");
        }
    }

    private static void eliminarDatos(Connection connection) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ingrese el ID del cliente a eliminar: ");
            int id = scanner.nextInt();

            // Consultar los datos del cliente a eliminar
            String sql = "SELECT * FROM clientes WHERE id_cliente = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        System.out.println("Cliente encontrado:");
                        System.out.println(rs.getShort(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getDate(4));

                        // Preparar y ejecutar la consulta SQL para eliminar el registro
                        String deleteSql = "DELETE FROM clientes WHERE id_cliente = ?";
                        try (PreparedStatement deletePs = connection.prepareStatement(deleteSql)) {
                            deletePs.setInt(1, id);

                            int rowsAffected = deletePs.executeUpdate();
                            if (rowsAffected > 0) {
                                System.out.println("Registro eliminado con éxito.");
                            } else {
                                System.out.println("No se pudo eliminar el registro.");
                            }
                        }
                    } else {
                        System.out.println("No se encontró ningún cliente con el ID especificado.");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error al eliminar los datos" + e.getMessage());
            }
        }
    }

    private static int obtenerNuevoID(Connection connection) {
        int id = 0;
        String sql = "SELECT MAX(id_cliente) FROM clientes";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            if (rs.next()) {
                id = rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el nuevo ID: " + e.getMessage());
        }
        return id;
    }
    String eliminarDatos;
    String verDatos;

    void conectar() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
