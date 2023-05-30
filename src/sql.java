import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class sql {
    public int id_incrementable(){
        int id = 1;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Conexion db = new Conexion();
        try{
            ps = db.getConnection().prepareStatement("SELECT MAX(id_cliente) FROM clientes");
            rs = ps.executeQuery();
            while (rs.next()){
                id = rs.getInt(1) + 1;
            }
        }catch(Exception e){
        System.out.println(e);}
        finally{
            try{
                ps.close();
                rs.close();
                db.desconectar();
            }catch (Exception e){
            System.out.println(e);}
        }
        return id;
    }
}
