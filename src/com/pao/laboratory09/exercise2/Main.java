package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        if (!scanner.hasNextInt()) {
            return;
        }

        int n = scanner.nextInt();

        File file = new File(OUTPUT_FILE);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
            for (int i = 0; i < n; i++) {
                int id = scanner.nextInt();
                double suma = scanner.nextDouble();
                String data = scanner.next();
                TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

                ByteBuffer buffer = ByteBuffer.allocate(RECORD_SIZE);
                buffer.order(ByteOrder.LITTLE_ENDIAN);

                buffer.putInt(id);
                buffer.putDouble(suma);

                byte[] dataBytes = data.getBytes();
                for (int j = 0; j < 10; j++) {
                    if (j < dataBytes.length) {
                        buffer.put(dataBytes[j]);
                    } else {
                        buffer.put((byte) ' ');
                    }
                }

                byte tipByte = (byte) (tip == TipTranzactie.CREDIT ? 0 : 1);
                buffer.put(tipByte);

                buffer.put((byte) 0);

                buffer.putLong(0L);

                dos.write(buffer.array());
            }
        }

        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            while (scanner.hasNext()) {
                String command = scanner.next();

                if ("READ".equals(command)) {
                    int idx = scanner.nextInt();
                    printRecord(raf, idx);

                } else if ("UPDATE".equals(command)) {
                    int idx = scanner.nextInt();
                    String statusStr = scanner.next();

                    byte statusByte = 0;
                    if ("PROCESSED".equals(statusStr)) statusByte = 1;
                    else if ("REJECTED".equals(statusStr)) statusByte = 2;

                    raf.seek((long) idx * RECORD_SIZE + 23);
                    raf.write(statusByte);

                    System.out.println("Updated [" + idx + "]: " + statusStr);

                } else if ("PRINT_ALL".equals(command)) {
                    for (int i = 0; i < n; i++) {
                        printRecord(raf, i);
                    }
                }
            }
        }
        scanner.close();
    }

    private static void printRecord(RandomAccessFile raf, int idx) throws IOException {
        raf.seek((long) idx * RECORD_SIZE);

        byte[] recordBytes = new byte[RECORD_SIZE];
        raf.readFully(recordBytes);

        ByteBuffer buffer = ByteBuffer.wrap(recordBytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        int id = buffer.getInt();
        double suma = buffer.getDouble();

        byte[] dataBytes = new byte[10];
        buffer.get(dataBytes);
        String data = new String(dataBytes).trim();

        byte tipByte = buffer.get();
        String tipStr = (tipByte == 0) ? "CREDIT" : "DEBIT";

        byte statusByte = buffer.get();
        String statusStr = "PENDING";
        if (statusByte == 1) statusStr = "PROCESSED";
        else if (statusByte == 2) statusStr = "REJECTED";

        System.out.printf(Locale.US, "[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s%n",
                idx, id, data, tipStr, suma, statusStr);
    }
}
