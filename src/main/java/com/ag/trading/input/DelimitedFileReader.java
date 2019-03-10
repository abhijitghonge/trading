package com.ag.trading.input;

import java.io.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class DelimitedFileReader<T> {


    private String filePath;


    public DelimitedFileReader(String filePath) {
        this.filePath = filePath;
    }


    public Stream<String> read() throws IOException {

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(filePath)))){
            return reader.lines();
        }
    }
}
