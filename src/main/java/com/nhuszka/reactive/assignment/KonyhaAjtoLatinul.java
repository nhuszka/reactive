package com.nhuszka.reactive.assignment;

import com.nhuszka.reactive.util.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class KonyhaAjtoLatinul {
    private static List<String> ekson = List.of(
            "júúújj",
            "huhh",
            "mozgás, te nyamvadt anyaszomorító",
            "hmm?",
            "uff",
            "el ne bőgd magad",
            "hasba rúgtalak"
    );

    public static void main(String[] args) {
        Vector<String> tanc = new Vector<>();

        System.out.println("Akkor ROPJUK!!");

        Flux.interval(Duration.ofSeconds(1))
                .parallel(8)
                .runOn(Schedulers.immediate())
                .map(hehehe -> ekson.get(new Random().nextInt(ekson.size())))
                .doOnNext(mivan -> tanc.add(mivan))
                .subscribe(System.out::println);

        Util.waitFor(5);

        boolean isAgressziv = tanc.contains("hasba rúgtalak");
        if (isAgressziv) {
            System.out.println("Nekem tetszik!!!");
        } else {
            System.out.println("Max 3 pont");
        }
    }
}


