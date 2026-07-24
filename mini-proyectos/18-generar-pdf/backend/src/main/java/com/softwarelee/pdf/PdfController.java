package com.softwarelee.pdf;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reportes")
public class PdfController {

    @GetMapping("/ejemplo/pdf")
    public void generarPdf(HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=reporte_" + LocalDate.now() + ".pdf");

        Document document = new Document(PageSize.LETTER);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        document.add(new Paragraph("Reporte de Ejemplo", titleFont));
        document.add(new Paragraph("Fecha: " + LocalDate.now()));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.addCell("Producto");
        table.addCell("Precio");
        table.addCell("Stock");

        table.addCell("Laptop HP");
        table.addCell("$18,999.00");
        table.addCell("25");

        table.addCell("Monitor Dell");
        table.addCell("$12,499.00");
        table.addCell("15");

        table.addCell("Mouse Logitech");
        table.addCell("$1,899.00");
        table.addCell("50");

        document.add(table);
        document.close();
    }
}
