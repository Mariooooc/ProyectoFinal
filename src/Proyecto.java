import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


public class Proyecto {
    public static void main(String[] args) throws ParseException, ClassNotFoundException, SQLException {
        Scanner read = new Scanner(System.in);
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/gimnasio";
        String username = "root";
        String password = "";
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection connection = null;
        int opcion;

        System.out.println("1. VER DATOS");
        System.out.println("2. INGRESAR DATOS");
        System.out.println("3. ELIMINAR DATOS");
        System.out.println("Ingrese número de opción: ");
        opcion = read.nextInt();

        if (opcion == 1) {
            try {

                Class.forName(driver);

                connection = DriverManager.getConnection(url, username, password);

                Statement statement = connection.createStatement();

                rs = statement.executeQuery("select * from clientes ");

                while (rs.next()) {
                    System.out.println(rs.getShort(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getDate(4));
                }

                connection.close();

            } catch (Exception e) {
                System.out.println(e);
            }
        } else if (opcion == 2) {
            try {

                //Scanner que va a leer los datos que se quieren agregar a la BD
                Scanner registro = new Scanner(System.in);

                //Concetar la BD con el programa
                Class.forName(driver);
                connection = DriverManager.getConnection(url, username, password);
                ps = connection.prepareStatement("INSERT INTO clientes VALUES (?,?,?,?)");

                //Se ingresan los datos
                String nombres, apellidos, fechaString;
                sql s = new sql();
                int id = s.id_incrementable();
                System.out.println("Ingrese nombres de la persona: ");
                nombres = registro.nextLine();
                System.out.println("Ingrese apellidos de la persona: ");
                apellidos = registro.nextLine();
                System.out.println("Ingrese fecha de inscripción de la persona: ");
                fechaString = registro.next();

                //Convierte la fecha que se recibe en String a Date
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date fechaDate = (Date) format.parse(fechaString);

                //Se aplica el método convertDate
                java.sql.Date sql_date = convertDate(fechaDate);


                //Se envían los datos a la BD
                ps.setInt(1, id);
                ps.setString(2, nombres);
                ps.setString(3, apellidos);
                ps.setDate(4, sql_date);

                //Se registran los datos en la BD
                ps.executeUpdate();
                System.out.println("Se agregó con éxito");

                //En caso de un error el catch se encarga de informar
            } catch (Exception e) {
                System.out.println(e);
            }


        } else if (opcion == 3) {
            try {
                try {

                    Class.forName(driver);

                    connection = DriverManager.getConnection(url, username, password);

                    Statement statement = connection.createStatement();

                    rs = statement.executeQuery("select * from clientes ");

                    while (rs.next()) {
                        System.out.println(rs.getShort(1) + " " + rs.getString(2) + " " + rs.getString(3) + " " + rs.getDate(4));
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                try {
                    Scanner idEliminar = new Scanner(System.in);

                    Class.forName(driver);
                    connection = DriverManager.getConnection(url, username, password);
                    ps = connection.prepareStatement("DELETE FROM clientes WHERE id_cliente = ?");

                    short id_cliente = 0;
                    System.out.println("Ingrese ID del cliente que desea eliminar: ");
                    id_cliente = idEliminar.nextShort();

                    ps.setShort(1, id_cliente);
                    ps.executeUpdate();

                    System.out.println("Se eliminó con éxito.");
                } catch (Exception e) {
                    System.out.println(e);
                }
            }catch (Exception e){
            System.out.println(e);}
        }
    }

        //Método que convierte Date en sql_Date
        private static java.sql.Date convertDate(java.util.Date date) {
        java.sql.Date myDate = new java.sql.Date(date.getTime());
        return myDate;
    }
}