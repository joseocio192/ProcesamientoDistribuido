package datosParaLimpiarYinsertar;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CSVToSQL_TicketsD {

    public static void main(String[] args) {
        String csvFile = "datosParaLimpiarYinsertar\\TicketDL.csv";
        String outputFile = "datosParaLimpiarYinsertar\\TicketDOutput.sql";
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
                String ticket = values[0]; // FOLIO como VARCHAR
                int idProducto = Integer.parseInt(values[1]);
                int unidades = Integer.parseInt(values[2]);
                int precio = (int) Double.parseDouble(values[3]); // Convertir a entero para la tabla
                // No incluimos TOTAL en la tabla TICKETSD

                // Crear la sentencia SQL INSERT
                String sql = String.format(
                    "INSERT INTO TICKETSD (TICKET, IDPRODUCTO, UNIDADES, PRECIO) VALUES ('%s', %d, %d, %d);%n",
                    ticket, idProducto, unidades, precio
                );

                // Escribir la sentencia SQL en el archivo
                fw.write(sql);
            }

            System.out.println("Archivo SQL generado: " + outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
