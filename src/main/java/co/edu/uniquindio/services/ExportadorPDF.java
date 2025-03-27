package co.edu.uniquindio.services;

import co.edu.uniquindio.models.Transaccion;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.util.List;

public class ExportadorPDF {

    public static void exportarHistorial(List<Transaccion> historial, String nombreArchivo) {
        try {
            File archivo = new File(nombreArchivo);
            PdfWriter writer = new PdfWriter(archivo);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document documento = new Document(pdfDoc);

            documento.add(new Paragraph("Historial de Transacciones").setBold().setFontSize(18));

            float[] anchos = {120F, 120F, 120F, 120F};
            Table tabla = new Table(anchos);

            tabla.addCell("Fecha");
            tabla.addCell("Tipo");
            tabla.addCell("Monto");
            tabla.addCell("Cuenta destino");

            for (Transaccion t : historial) {
                tabla.addCell(t.getFecha());
                tabla.addCell(t.getTipo());
                tabla.addCell("$" + t.getMonto());
                tabla.addCell(t.getCuentaDestino() != null ? t.getCuentaDestino() : "-");
            }

            documento.add(tabla);
            documento.close();

            System.out.println("PDF generado: " + archivo.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}