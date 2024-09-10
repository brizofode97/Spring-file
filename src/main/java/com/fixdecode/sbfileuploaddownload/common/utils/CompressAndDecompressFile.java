package com.fixdecode.sbfileuploaddownload.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class CompressAndDecompressFile {

    public static byte[] compressFile(byte[] data){
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length)){
            byte[] buf = new byte[1024];
            while(deflater.finished()){
                int count = deflater.deflate(buf);
                baos.write(buf, 0, count);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error during the compression", e);
        } finally {
            deflater.end();
        }
    }

    public static byte[] decompressFile(byte[] data) throws Exception{
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            byte[] buffer = new byte[1024];
            while(inflater.finished()){
                int count = inflater.inflate(buffer);
                baos.write(buffer, 0, count);
            }
            return baos.toByteArray();
        } catch (IOException | DataFormatException dfe){
            throw new Exception("Error during decompression", dfe);
        } finally {
            inflater.end();
        }
    }

}
