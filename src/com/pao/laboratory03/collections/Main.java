package com.pao.laboratory03.collections;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== PARTEA A: HashMap — frecventa cuvintelor ===");
        
        String[] words = {"java", "python", "java", "c++", "python", "java", "rust", "c++", "go"};
        
        Map<String, Integer> wordFrequency = new HashMap<>();
        for (String word : words) {
            wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
        }
        
        System.out.println("Frecventa: " + wordFrequency);
        
        System.out.println("Contine 'rust'? " + wordFrequency.containsKey("rust"));
        
        System.out.println("Chei: " + wordFrequency.keySet());
        System.out.println("Valori: " + wordFrequency.values());
        
        for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        System.out.println("\n=== PARTEA B: TreeMap - sortare automata ===");
        
        TreeMap<String, Integer> sortedWords = new TreeMap<>(wordFrequency);
        
        System.out.println("Sortat: " + sortedWords);
        
        System.out.println("Prima cheie: " + sortedWords.firstKey());
        System.out.println("Ultima cheie: " + sortedWords.lastKey());
        System.out.println("\n=== PARTEA C: Map cu obiecte ===");
        
        Map<String, List<String>> materii = new HashMap<>();
        materii.put("PAOJ", new ArrayList<>(Arrays.asList("Ana", "Mihai", "Ion")));
        materii.put("BD", new ArrayList<>(Arrays.asList("Ana", "Elena")));
        
        System.out.println("Studenti la PAOJ: " + materii.get("PAOJ"));
        
        materii.get("BD").add("George");
        System.out.println("Studenti la BD (actualizat): " + materii.get("BD"));
    }
}