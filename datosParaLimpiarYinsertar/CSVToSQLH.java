package datosParaLimpiarYinsertar;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CSVToSQLH {

    private static final SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yy");
    private static final SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        String csvFile = "TicketHL.CVS";
        String outputFile = "TicketHLOutput.CVS";
        String line;
        String csvSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile));
             FileWriter fw = new FileWriter(outputFile)) {

            // Saltar la primera línea (encabezados)
            br.readLine();

            while ((line = br.readLine()) != null) {
                // Usa el delimitador para separar los valores
                String[] values = line.split(csvSplitBy);

                // Asumiendo que el formato es correcto y tiene todos los campos
                int folio = Integer.parseInt(values[0]);
                String fechaStr = values[1];
                int idEstado = Integer.parseInt(values[2]);
                int idCiudad = Integer.parseInt(values[3]);
                int idTienda = Integer.parseInt(values[4]);
                int idEmpleado = Integer.parseInt(values[5]);

                // Convertir la fecha al formato SQL
                String fechaSQL = convertDateToSQLFormat(fechaStr);

                // Crear la sentencia SQL INSERT
                String sql = String.format(
                    "INSERT INTO TICKETSH (FOLIO, FECHA, IDESTADO, IDCIUDAD, IDTIENDA, IDEMPLEADO) VALUES (%d, '%s', %d, %d, %d, %d);%n",
                    folio, fechaSQL, idEstado, idCiudad, idTienda, idEmpleado
                );

                // Escribir la sentencia SQL en el archivo
                fw.write(sql);
            }

            System.out.println("Archivo SQL generado: " + outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para convertir la fecha al formato SQL
    private static String convertDateToSQLFormat(String fechaStr) {
        try {
            // Convertir la fecha del formato DD/MM/YY a Date
            Date date = inputDateFormat.parse(fechaStr);
            // Convertir la fecha de Date a formato SQL YYYY-MM-DD
            return outputDateFormat.format(date);
        } catch (ParseException e) {
            // Manejar fechas inválidas
            System.err.println("Fecha inválida: " + fechaStr);
            return "1900-01-01"; // Valor por defecto en caso de error
        }
    }
}