package datosParaLimpiarYinsertar;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataCleaningD {

    public static void main(String[] args) {
        String inputFile = "TicketD.CVS"; // Ruta del archivo CSV de entrada
        String outputFile = "TicketDL.csv"; // Ruta del archivo CSV de salida
        String line;
        String csvDelimiter = ",";

        // Listas para almacenar los datos limpios
        List<String> folio = new ArrayList<>();
        List<Integer> producto = new ArrayList<>();
        List<Integer> unidades = new ArrayList<>();
        List<Double> precioLimpio = new ArrayList<>();
        List<Double> totalLimpio = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            // Leer el archivo línea por línea
            while ((line = br.readLine()) != null) {
                // Separar los campos por delimitador (coma)
                String[] datos = line.split(csvDelimiter);

                // Saltar la cabecera si está presente
                if (datos[0].equalsIgnoreCase("FOLIO")) {
                    continue;
                }

                // Eliminar ceros iniciales en FOLIO convirtiéndolo a entero y luego de nuevo a String
                String folioSinCeros = String.valueOf(Integer.parseInt(datos[0]));

                // Almacenar los datos en las listas
                folio.add(folioSinCeros);
                producto.add(Integer.parseInt(datos[1]));
                unidades.add(Integer.parseInt(datos[2]));

                // Limpiar y convertir los datos numéricos
                double precioNum = Double.parseDouble(datos[3].replace(" ", ""));
                double totalNum = Double.parseDouble(datos[4].replace("$", "").replace(" ", ""));
                
                precioLimpio.add(precioNum);
                totalLimpio.add(totalNum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Escribir los datos limpios en el archivo de salida
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            // Escribir la cabecera en el archivo de salida
            bw.write("FOLIO,PRODUCTO,UNIDADES,PRECIO,TOTAL");
            bw.newLine();

            // Escribir los datos limpios en el archivo de salida
            for (int i = 0; i < precioLimpio.size(); i++) {
                String lineaLimpia = folio.get(i) + csvDelimiter + producto.get(i) + csvDelimiter + unidades.get(i) +
                                     csvDelimiter + precioLimpio.get(i) + csvDelimiter + totalLimpio.get(i);
                bw.write(lineaLimpia);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Datos limpiados y guardados en " + outputFile);
    }
}
