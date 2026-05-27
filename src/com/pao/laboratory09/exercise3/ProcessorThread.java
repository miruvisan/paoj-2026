package com.pao.laboratory09.exercise3;

import com.pao.laboratory09.exercise1.Tranzactie;
import java.util.Locale;

public class ProcessorThread implements Runnable {
    private final CoadaTranzactii banda;
    public volatile boolean activ = true;

    public ProcessorThread(CoadaTranzactii banda) {
        this.banda = banda;
    }

    @Override
    public void run() {
        try {
            while (activ || !banda.isEmpty()) {
                try {
                    Tranzactie t = null;

                    synchronized (banda) {
                        if (!banda.isEmpty()) {
                            t = banda.extrage();
                        } else if (!activ) {
                            break;
                        } else {
                            banda.wait(100);
                            continue;
                        }
                    }

                    if (t != null) {
                        System.out.printf(Locale.US, "[Processor] Factura #%d - %.2f RON | %s%n",
                                t.getId(), t.getSuma(), t.getData());
                        Thread.sleep(80);
                    }
                } catch (InterruptedException e) {
                    if (!activ && banda.isEmpty()) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}