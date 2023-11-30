/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package reports;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.swing.JFileChooser;
import javax.swing.JTable;

/**
 *
 * @author rafae
 */
public class GeradorRelatorio {
    // EXEMPLO USANDO A CLASSE:
    //GeradorRelatorio rel = new GeradorRelatorio();
    //rel.Gerar(this, "categorias", "Listagem", jTable1);
    public void Gerar(Component telaPai, String nomeArquivo, String titulo, JTable tabela) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showSaveDialog(telaPai);
        if (chooser.getSelectedFile() != null) {
            String pasta = chooser.getSelectedFile().getPath();
            String caminhoArquivo = pasta +"\\"+ nomeArquivo + ".pdf";
            try {
                PdfWriter writer = new PdfWriter(caminhoArquivo);
                PdfDocument pdf = new PdfDocument(writer);
                pdf.setTagged();
                pdf.setDefaultPageSize(PageSize.A4);
                Document doc = new Document(pdf);

                //gerar cabecalho
                //doc = ListagemCategoria.gerarCabecalhoTemporario(doc);
                doc.setMargins(110, 36, 55, 36);
                Header header = new Header(titulo);
                Footer footer = new Footer();
                pdf.addEventHandler(PdfDocumentEvent.START_PAGE, header);
                pdf.addEventHandler(PdfDocumentEvent.END_PAGE, footer);

                //gerar tabela
                Table tbl = new Table(tabela.getColumnCount()).useAllAvailableWidth();

                for (int i = 0; i < tabela.getColumnCount(); i++) {
                    tbl.addCell(tabela.getColumnName(i));
                }

                for (int i = 0; i < tabela.getRowCount(); i++) {
                    for (int c = 0; c < tabela.getColumnCount(); c++) {
                        tbl.addCell(tabela.getValueAt(i, c).toString());
                    }
                }
                doc.add(tbl);
                footer.writeTotal(pdf);
                doc.close();

                File file = new File(caminhoArquivo);
                Desktop.getDesktop().open(file);
            } catch (FileNotFoundException ex) {

            } catch (Exception ex) {
            }
        }
    }

    public class Header implements IEventHandler {

        private String titulo;

        public Header(String titulo) {
            this.titulo = titulo;
        }

        @Override
        public void handleEvent(Event event) {
            try {
                PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
                PdfDocument pdf = docEvent.getDocument();
                PdfPage page = docEvent.getPage();
                Rectangle pageSize = page.getPageSize();
                Canvas canvas = new Canvas(new PdfCanvas(page), pageSize);
                canvas.setFontSize(18);
                URL urlToImage = this.getClass().getResource("/img/logo.jpg");
                ImageData data = ImageDataFactory.create(urlToImage.getPath());
                Image img = new Image(data);
                img.setWidth(90);
                canvas.add(img);
                canvas.showTextAligned(titulo,
                        pageSize.getWidth() / 2,
                        pageSize.getTop() - 60, TextAlignment.CENTER);
                canvas.setUnderline();
                canvas.close();
            } catch (MalformedURLException ex) {

            }

        }

    }

    public class Footer implements IEventHandler {

        protected PdfFormXObject placeholder;
        protected float side = 20;
        protected float x = 550;
        protected float y = 25;
        protected float space = 4.5f;
        protected float descent = 3;

        public Footer() {
            placeholder = new PdfFormXObject(new Rectangle(0, 0, side, side));
        }

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdf = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            int pageNumber = pdf.getPageNumber(page);
            Rectangle pageSize = page.getPageSize();
            // Creates drawing canvas
            PdfCanvas pdfCanvas = new PdfCanvas(page);
            Canvas canvas = new Canvas(pdfCanvas, pageSize);
            Paragraph p = new Paragraph()
                    .add(String.valueOf(pageNumber))
                    .add(" de ");
            canvas.showTextAligned(p, x, y, TextAlignment.RIGHT);
            canvas.close();
            // Create placeholder object to write number of pages
            pdfCanvas.addXObjectAt(placeholder, x + space, y - descent);
            pdfCanvas.release();
        }

        public void writeTotal(PdfDocument pdf) {
            Canvas canvas = new Canvas(placeholder, pdf);
            canvas.showTextAligned(String.valueOf(pdf.getNumberOfPages()),
                    0, descent, TextAlignment.LEFT);
            canvas.close();
        }

    }
}
