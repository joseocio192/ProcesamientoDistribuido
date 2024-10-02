package bulk_cleaning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import conexion.ErrorHandler;

import java.text.SimpleDateFormat;
import java.text.ParseException;

public class DataCleaning {
    private static final SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd/MM/yy");
    private static final SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public String cleanDataD(String inputFile, String outputFile) {
        String line;
        String csvDelimiter = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
                BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {

            bw.write("TICKET,IDPRODUCTO,UNIDADES,PRECIO");
            bw.newLine();

            while ((line = br.readLine()) != null) {
                String[] datos = line.split(csvDelimiter);
                if (datos[0].equalsIgnoreCase("FOLIO")) {
                    continue;
                }

                String folio = datos[0];
                short producto = Short.parseShort(datos[1]);
                short unidades = Short.parseShort(datos[2]);

                float precioLimpio = Float.parseFloat(datos[3].replace(" ", ""));
                // float totalLimpio = Float.parseFloat(datos[4].replace("$", "").replace(" ",
                // ""));
                // Excluir total

                String lineaLimpia = folio + csvDelimiter + producto + csvDelimiter + unidades +
                        csvDelimiter + precioLimpio;
                bw.write(lineaLimpia);
                bw.newLine();
            }
        } catch (IOException e) {
            ErrorHandler.showNotification(e.getLocalizedMessage());
        }
        return outputFile;
    }

    public String cleanDataH(String inputFile, String outputFile) {
        String line;
        String csvDelimiter = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
                BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {

            if ((line = br.readLine()) != null) {
                bw.write("TICKET,FECHA,IDESTADO,IDCIUDAD,IDTIENDA,IDEMPLEADO");
                bw.newLine();
            }

            while ((line = br.readLine()) != null) {
                String[] datos = line.split(csvDelimiter);

                String folioLimpio = datos[0];

                String[] fechaPartes = datos[1].split("/");
                String mesNumero = convertMonthToNumber(fechaPartes[1]);
                String fechaLimpia = fechaPartes[0] + "/" + mesNumero + "/" + fechaPartes[2];
                fechaLimpia = convertDateToSQLFormat(fechaLimpia);

                String lineaLimpia = folioLimpio + csvDelimiter + fechaLimpia + csvDelimiter + datos[2] +
                        csvDelimiter + datos[3] + csvDelimiter + datos[4] + csvDelimiter + datos[5];
                bw.write(lineaLimpia);
                bw.newLine();
            }
        } catch (IOException e) {
            ErrorHandler.showNotification(e.getLocalizedMessage());
        }
        return outputFile;
    }

    private String convertMonthToNumber(String monthAbbreviation) {
        switch (monthAbbreviation) {
            case "ene":
                return "1";
            case "feb":
                return "2";
            case "mar":
                return "3";
            case "abr":
                return "4";
            case "may":
                return "5";
            case "jun":
                return "6";
            case "jul":
                return "7";
            case "ago":
                return "8";
            case "sep":
                return "9";
            case "oct":
                return "10";
            case "nov":
                return "11";
            case "dic":
                return "12";
            default:
                return monthAbbreviation; // In case of an unexpected month format
        }
    }

    private static String convertDateToSQLFormat(String fechaStr) {
        try {
            // DD/MM/YY a Date
            Date date = inputDateFormat.parse(fechaStr);
            // Date a YYYY-MM-DD
            return outputDateFormat.format(date);
        } catch (ParseException e) {
            System.err.println("Fecha inválida: " + fechaStr);
            return "1900-01-01";
        }
    }

}