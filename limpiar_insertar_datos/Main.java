package limpiar_insertar_datos;

import bulk_cleaning.BulkInsertUtility;

public class Main {
    public static void main(String[] args) {
        String inputFileD = "limpiar_insertar_datos\\TicketD.CVS";
        String outputFileD = "limpiar_insertar_datos\\TicketDL.CSV";
        String inputFileH = "limpiar_insertar_datos\\TicketH.CVS";
        String outputFileH = "limpiar_insertar_datos\\TicketHL.CSV";

        // DataCleaning dataCleaning = new DataCleaning();
        // dataCleaning.cleanDataD(inputFileD, outputFileD);
        // dataCleaning.cleanDataH(inputFileH, outputFileH);

        String tableD = "dbo.TICKETSD";
        String tableH = "dbo.TICKETSH";
        ConexionDB conn = new ConexionDB("ONCE", "DBTICKETS2", "sa", "123456789");
        conn.getConexion();
        // BulkInsertUtility.bulkInsert(ConexionDB.conexion,
        // "C:\\Users\\brend\\OneDrive\\Desktop\\TicketHL.csv", tableH);
        BulkInsertUtility.bulkInsert(ConexionDB.conexion,
                "C:\\Users\\brend\\OneDrive\\Desktop\\TicketDL.csv", tableD);
    }
}
