package datosParaLimpiarYinsertar;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DateCleaningH {

    public static void main(String[] args) {
        String inputFile = "TicketH.CVS"; // Ruta del archivo CSV de entrada
        String outputFile = "TicketHL.CVS"; // Ruta del archivo CSV de salida
        String line;
        String csvDelimiter = ",";

        // Mapa para reemplazar los nombres de los meses por números
        Map<String, String> meses = new HashMap<>();
        meses.put("ene", "1");
        meses.put("feb", "2");
        meses.put("mar", "3");
        meses.put("abr", "4");
        meses.put("may", "5");
        meses.put("jun", "6");
        meses.put("jul", "7");
        meses.put("ago", "8");
        meses.put("sep", "9");
        meses.put("oct", "10");
        meses.put("nov", "11");
        meses.put("dic", "12");

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {

            // Leer y escribir la cabecera
            if ((line = br.readLine()) != null) {
                bw.write(line);
                bw.newLine();
            }

            // Leer el archivo línea por línea y procesar
            while ((line = br.readLine()) != null) {
                // Separar los campos por delimitador (coma)
                String[] datos = line.split(csvDelimiter);

                // Limpiar el FOLIO eliminando ceros iniciales
                String folioLimpio = String.valueOf(Integer.parseInt(datos[0]));

               // Limpiar la fecha reemplazando el nombre del mes por su número
               String[] fechaPartes = datos[1].split("/");
               String mesNumero = meses.get(fechaPartes[1].toLowerCase());
               String fechaLimpia = fechaPartes[0] + "/" + mesNumero + "/" + fechaPartes[2];


                // Construir la línea limpia
                String lineaLimpia = folioLimpio + csvDelimiter + fechaLimpia + csvDelimiter + datos[2] +
                                     csvDelimiter + datos[3] + csvDelimiter + datos[4] + csvDelimiter + datos[5];

                // Escribir la línea limpia en el archivo de salida
                bw.write(lineaLimpia);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
