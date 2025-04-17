package com.importgenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Quotes {
  private static final ArrayList<String> quotes = loadQuotes();

  public static String getRandomQuote() {
    int randomIndex = (int) (Math.random() * quotes.size());
    return quotes.get(randomIndex);
  }

  private static ArrayList<String> loadQuotes() {
    ArrayList<String> quotes = new ArrayList<String>();
    try {
      InputStream stream = ImportGenerator.class.getClassLoader().getResourceAsStream("quotes.txt");
      BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
      String line;
      while ((line = reader.readLine()) != null) {
        quotes.add(line);
      }
    } catch (IOException e) {
      System.err.println(e);
    }
    return quotes;
  }
}
