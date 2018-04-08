package mydraw;
import java.awt.*; 
import java.io.*; 
import java.awt.image.*; 

/** Import and export Windows bitmap (*.bmp) images
*/
public class MyBMPFile extends Component { 

     /**
      Read a Windows bitmap file (*.bmp) 
      @param filename the image's file name
      @return the image read 
      */ 
     static public Image read(String filename) throws IOException
     {
        MyBMPFile reader = new MyBMPFile();
        
        return reader.loadBitmap(filename);
     }

     /**
      Write the given image as a Windows bitmap file (*.bmp) 
      @param filename the image's file name
      @param image the image to be written 
      */ 
     static public void write(String filename, Image image) throws IOException
     {
        MyBMPFile reader = new MyBMPFile();
        
        reader.saveBitmap(filename, image, image.getWidth(reader), image.getHeight(reader));
     }

     //--- Private constants 
     private final static int BITMAPFILEHEADER_SIZE = 14; 
     private final static int BITMAPINFOHEADER_SIZE = 40; 

     //--- Private variable declaration 

     //--- Bitmap file header 
     private byte bitmapFileHeader [] = new byte [14]; 
     private byte bfType [] = {(byte)'B', (byte)'M'}; 
     private int bfSize = 0; 
     private int bfReserved1 = 0; 
     private int bfReserved2 = 0; 
     private int bfOffBits = BITMAPFILEHEADER_SIZE + BITMAPINFOHEADER_SIZE; 

     //--- Bitmap info header 
     private byte bitmapInfoHeader [] = new byte [40]; 
     private int biSize = BITMAPINFOHEADER_SIZE; 
     private int biWidth = 0; 
     private int biHeight = 0; 
     private int biPlanes = 1; 
     private int biBitCount = 24; 
     private int biCompression = 0; 
     private int biSizeImage = 0x030000; 
     private int biXPelsPerMeter = 0x0; 
     private int biYPelsPerMeter = 0x0; 
     private int biClrUsed = 0; 
     private int biClrImportant = 0; 
     
     //--- Bitmap raw data 
     private int bitmap []; 

     //--- File section 
     //private FileOutputStream fo; // unbuffered
     private BufferedOutputStream fo; 

     //--- Default constructor --- CHANGED TO PUBLIC !!!!!
     public MyBMPFile() { 

     } 

     private void 
     saveBitmap (String parFilename, Image parImage, int parWidth, int parHeight) throws IOException
     { 
       fo = new BufferedOutputStream (new FileOutputStream(parFilename)); 
       save (parImage, parWidth, parHeight); 
       fo.close (); 
     } 


     private void save (Image parImage, int parWidth, int parHeight) throws IOException
     { 
        convertImage (parImage, parWidth, parHeight); 
        writeBitmapFileHeader (); 
        writeBitmapInfoHeader (); 
        writeBitmap (); 
     } 

     private void convertImage (Image parImage, int parWidth, int parHeight) throws IOException
     { 
        int pad; 
        bitmap = new int [parWidth * parHeight]; 

        PixelGrabber pg = new PixelGrabber (parImage, 0, 0, parWidth, parHeight, 
                                            bitmap, 0, parWidth); 

        try { 
           pg.grabPixels (); 
        } 
        catch (InterruptedException e) { 
           throw new IOException("BMPFile.write(): Interrupted"); 
        } 

        pad = (4 - ((parWidth * 3) % 4)) * parHeight; 
        biSizeImage = ((parWidth * parHeight) * 3) + pad; 
        bfSize = biSizeImage + BITMAPFILEHEADER_SIZE + BITMAPINFOHEADER_SIZE; 
        biWidth = parWidth; 
        biHeight = parHeight; 
     } 

     private void writeBitmap () throws IOException
     { 
         int size; 
         int value; 
         int j; 
         int i; 
         int rowCount; 
         int rowIndex; 
         int lastRowIndex; 
         int pad; 
         int padCount; 
         byte rgb [] = new byte [3]; 


         size = (biWidth * biHeight) - 1; 
         pad = 4 - ((biWidth * 3) % 4); 
         if (pad == 4) 
            pad = 0;
         rowCount = 1; 
         padCount = 0; 
         rowIndex = size - biWidth; 
         lastRowIndex = rowIndex; 

         for (j = 0; j <= size; j++) { 
            value = bitmap [rowIndex]; 
            rgb [0] = (byte) (value & 0xFF); 
            rgb [1] = (byte) ((value >> 8) & 0xFF); 
            rgb [2] = (byte) ((value >> 16) & 0xFF); 
            fo.write (rgb); 
            if (rowCount == biWidth) { 
               padCount += pad; 
               for (i = 1; i <= pad; i++) { 
                  fo.write (0x00); 
               } 
               rowCount = 1; 
               rowIndex = lastRowIndex - biWidth; 
               lastRowIndex = rowIndex; 
            } 
            else 
               rowCount++; 
            rowIndex++; 
         } 

         //--- Update the size of the file 
         bfSize += padCount - pad; 
         biSizeImage += padCount - pad; 
      } 

     private void writeBitmapFileHeader () throws IOException
     { 
         fo.write (bfType); 
         fo.write (intToLSBLong (bfSize)); 
         fo.write (intToLSBShort (bfReserved1)); 
         fo.write (intToLSBShort (bfReserved2)); 
         fo.write (intToLSBLong (bfOffBits)); 
     } 

     private void writeBitmapInfoHeader () throws IOException
     { 
         fo.write (intToLSBLong (biSize)); 
         fo.write (intToLSBLong (biWidth)); 
         fo.write (intToLSBLong (biHeight)); 
         fo.write (intToLSBShort (biPlanes)); 
         fo.write (intToLSBShort (biBitCount)); 
         fo.write (intToLSBLong (biCompression)); 
         fo.write (intToLSBLong (biSizeImage)); 
         fo.write (intToLSBLong (biXPelsPerMeter)); 
         fo.write (intToLSBLong (biYPelsPerMeter)); 
         fo.write (intToLSBLong (biClrUsed)); 
         fo.write (intToLSBLong (biClrImportant)); 
     } 


     private byte [] intToLSBShort (int parValue) { 

        byte retValue [] = new byte [2]; 

        retValue [0] = (byte) (parValue & 0x00FF); 
        retValue [1] = (byte) ((parValue >> 8) & 0x00FF); 

        return (retValue); 

     } 

     private byte [] intToLSBLong (int parValue) { 

        byte retValue [] = new byte [4]; 

        retValue [0] = (byte) (parValue & 0x00FF); 
        retValue [1] = (byte) ((parValue >> 8) & 0x000000FF); 
        retValue [2] = (byte) ((parValue >> 16) & 0x000000FF); 
        retValue [3] = (byte) ((parValue >> 24) & 0x000000FF); 

        return (retValue); 

     } 
     

     private int intFromLSBShort (byte raw[], int index) { 

        return (((int)raw[index+1]&0xff)<<8) 
               | (int)raw[index]&0xff; 
     } 

     private int intFromLSBLong (byte raw[], int index) { 

        return (((int)raw[index+3]&0xff)<<24) 
               | (((int)raw[index+2]&0xff)<<16) 
               | (((int)raw[index+1]&0xff)<<8) 
               | (int)raw[index]&0xff; 

     } 
     
    private byte[] 
    readPixelData(FileInputStream fs, String filename, 
                 int compression, int width, int height, int size) 
       throws IOException
    {
        byte result[] = new byte[width*height];
        
        if(compression == 0)
        {
            fs.read (result, 0, width*height); 
        }
        else if(compression == 1)
        {
            for (int i=0; i < width*height; i++)
                result[i]=0;
            byte raw[] = new byte[size];
            fs.read (raw, 0, size); 
            int i = 0;
            int x = 0;
            for(int y=0; y<height;)
            {
                int count = ((int)raw[i++]&0xff);
                
                if(count != 0) // run-length mode
                {
                    byte value = raw[i++];
                    for(int k=0; k<count; ++k, ++x)
                        result[x+width*y] = value;
                }
                else // escape mode
                {
                    count = ((int)raw[i++]&0xff);
                    if(count == 1)
                        break;
                    else if(count == 0) // end of line
                    {
                        x = 0;
                        ++y;
                    }
                    else if(count == 2) // delta mode
                    {
                        x += ((int)raw[i++]&0xff);
                        y += ((int)raw[i++]&0xff);
                    }
                    else // absolute mode
                    {
                        for(int k=0; k<count; ++k, ++x)
                            result[x+width*y] = raw[i++];
                        // read pad byte
                        if((count & 1) == 1)
                            ++i;
                    }
                }
            }
        }
        else
        {
            throw new IOException("BMPFile.read('"+filename+"'): Unsupported compression.");
        }
        
        return result;
    }
     
    private Image loadBitmap (String sfile) throws IOException
    { 
        Image image; 

        FileInputStream fs=new FileInputStream(sfile); 
        int bflen=14; // 14 byte BITMAPFILEHEADER 
        byte bf[]=new byte[bflen]; 
        fs.read(bf,0,bflen); 
        int bilen=40; // 40-byte BITMAPINFOHEADER 
        byte bi[]=new byte[bilen]; 
        fs.read(bi,0,bilen); 

        // Interperet data. 
        int nsize = intFromLSBLong(bf, 2); 
        if((char)bf[0] != 'B' || (char)bf[1] != 'M')
            throw new IOException("BMPFile.read('"+sfile+"'): File isn't a BMP image.");
            
        int nbisize = intFromLSBLong(bi,0);
        int nwidth = intFromLSBLong(bi,4);
        int nheight = intFromLSBLong(bi,8);
        int nplanes = intFromLSBShort(bi,12);
        int nbitcount = intFromLSBShort(bi,14);
        int ncompression = intFromLSBLong(bi,16);
        int nsizeimage = intFromLSBLong(bi,20);
        int nxpm = intFromLSBLong(bi,24); 
        int nypm = intFromLSBLong(bi,28);
        int nclrused = intFromLSBLong(bi,32);
        int nclrimp = intFromLSBLong(bi,36);

        if (nbitcount==24) 
        { 
             // No Palatte data for 24-bit format but scan lines are 
             // padded out to even 4-byte boundaries. 
             int bytes_per_line = ((nwidth*nbitcount+31)/32)*4;
             int npad = bytes_per_line - 3*nwidth;
             int ndata[] = new int [nheight * nwidth]; 
             byte brgb[] = new byte [( nwidth + npad) * 3 * nheight]; 
             fs.read (brgb, 0, (nwidth + npad) * 3 * nheight); 
             int nindex = 0; 
             for (int j = 0; j < nheight; j++) 
             { 
                 for (int i = 0; i < nwidth; i++) 
                 { 
                      ndata [nwidth * (nheight - j - 1) + i] = 
                          (255&0xff)<<24 
                          | (((int)brgb[nindex+2]&0xff)<<16) 
                          | (((int)brgb[nindex+1]&0xff)<<8) 
                          | (int)brgb[nindex]&0xff; 
                      nindex += 3; 
                 } 
                 nindex += npad; 
             } 

             image = createImage( new MemoryImageSource (nwidth, nheight, 
                     ndata, 0, nwidth)); 
        } 
        else if (nbitcount == 8) 
        { 
             // Have to determine the number of colors, the clrsused 
             // parameter is dominant if it is greater than zero. If 
             // zero, calculate colors based on bitsperpixel. 
             int nNumColors = 0; 
             if (nclrused > 0) 
             { 
                 nNumColors = nclrused; 
             } 
             else 
             { 
                 nNumColors = (1&0xff)<<nbitcount; 
             } 

             // Some bitmaps do not have the sizeimage field calculated 
             // Ferret out these cases and fix 'em. 
             if (nsizeimage == 0) 
             { 
                 nsizeimage = ((((nwidth*nbitcount)+31) & ~31 ) >> 3); 
                 nsizeimage *= nheight; 
             } 

             // Read the palatte colors. 
             int npalette[] = new int [nNumColors]; 
             byte bpalette[] = new byte [nNumColors*4]; 
             fs.read (bpalette, 0, nNumColors*4); 
             int nindex8 = 0; 
             for (int n = 0; n < nNumColors; n++) 
             { 
                 npalette[n] = (255&0xff)<<24 
                               | (((int)bpalette[nindex8+2]&0xff)<<16) 
                               | (((int)bpalette[nindex8+1]&0xff)<<8) 
                               | (int)bpalette[nindex8]&0xff; 
                 nindex8 += 4; 
             } 

             // Read the image data (actually indices into the palette) 
             // Scan lines are still padded out to even 4-byte 
             // boundaries. 

             int bytes_per_line = ((nwidth*nbitcount+31)/32)*4;
             int npad8 = bytes_per_line - nwidth;
             int ndata8[] = new int [nwidth*nheight]; 
             byte bdata[] = readPixelData(fs, sfile, ncompression, bytes_per_line, nheight, nsizeimage); 
             nindex8 = 0; 
             for (int j8 = 0; j8 < nheight; j8++) 
             { 
                 for (int i8 = 0; i8 < nwidth; i8++) 
                 { 
                      ndata8 [nwidth*(nheight-j8-1)+i8] = 
                          npalette [((int)bdata[nindex8]&0xff)]; 
                      nindex8++; 
                 } 
                 nindex8 += npad8; 
             } 

             image = createImage 
                 ( new MemoryImageSource (nwidth, nheight, 
                     ndata8, 0, nwidth)); 
        } 
        else 
        { 
            throw new IOException("BMPFile.read('"+sfile+"'): Unsupported pixel depth.");
        } 

        fs.close(); 
        return image; 
    } 
} 
