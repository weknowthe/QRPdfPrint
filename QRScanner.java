/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qrscanner;
import java.io.*;
import javax.print.*;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.awt.Color;
import java.awt.print.PrinterJob;
import java.io.File;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import java.awt.print.PrinterJob;
import javax.print.PrintService;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.print.* ;

import javax.imageio.ImageIO;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRScanner {

	/**
	 * @param args
	 * @throws WriterException
	 * @throws IOException
	 */
	public static void main(String[] args) throws Exception,WriterException, IOException,DocumentException {
            
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
            String formattedDate = sdf.format(date);
            System.out.println(formattedDate);
            
		String qrCodeText = " 2484  | "+formattedDate;
		//String filePath = "F:\\QR.jpeg";
		int size = 225;
		String fileType = "png";
		///File qrFile = new File(filePath);
		BufferedImage QRimage =createQRImage( qrCodeText, size, fileType);
                imageToPDF(QRimage,qrCodeText);
		System.out.println("DONE 2");           
            printPDF();
           
	}

	private static BufferedImage createQRImage( String qrCodeText, int size,
			String fileType) throws WriterException, IOException {
		// Create the ByteMatrix for the QR-Code that encodes the given String
		Hashtable hintMap = new Hashtable();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText,
				BarcodeFormat.QR_CODE, size, size, hintMap);
		// Make the BufferedImage that are to hold the QRCode
		int matrixWidth = byteMatrix.getWidth();
		BufferedImage image = new BufferedImage(matrixWidth, matrixWidth,
				BufferedImage.TYPE_INT_RGB);
		image.createGraphics();

		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, matrixWidth, matrixWidth);
		// Paint and save the image using the ByteMatrix
		graphics.setColor(Color.BLACK);

		for (int i = 0; i < matrixWidth; i++) {
			for (int j = 0; j < matrixWidth; j++) {
				if (byteMatrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}
		//ImageIO.write(image, fileType, qrFile);
                return image;
	}
        
        
        private static void imageToPDF(BufferedImage qrimg,String qrtext) throws 
                DocumentException, MalformedURLException, IOException {
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(qrimg, "png", baos);
        Image iTextImage = Image.getInstance(baos.toByteArray());          
          //  iTextImage.setAbsolutePosition(50f, 10f);
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("QRscannedpdf.pdf"));
        document.open();
         document.add(new Paragraph("Parking Ticket:- "));
        document.add(new Paragraph(qrtext));
        document.add(iTextImage);
        document.add(new Paragraph("--** Thank you for visiting us **--"));
        document.close();
        
        //document.
        System.out.println("Done 1"); 
        
  }
        
        
//private static Logger log = Logger.getLogger(QRScanner.class);

    public static void printPDF() throws Exception {       
         //printPDF("ZJ-58",pdfPath);
        
        Path currentRelativePath = Paths.get("");
        String pdfPath = currentRelativePath.toAbsolutePath().toString()+"\\QRscannedpdf.pdf";

        System.out.println("Current relative path is: " + pdfPath);

        PDDocument document = PDDocument.load(new File(pdfPath));

        PrintService myPrintService = findPrintService("ZJ-58");

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPageable(new PDFPageable(document));
        job.setPrintService(myPrintService);
        job.print();

    }       

    private static PrintService findPrintService(String printerName) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printService : printServices) {
            if (printService.getName().trim().equals(printerName)) {
                return printService;
            }
        }
        return null;
    }
        
        

}