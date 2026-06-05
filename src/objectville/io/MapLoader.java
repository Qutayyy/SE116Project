package objectville.io;

import objectville.cells.*;
import objectville.sim.City;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MapLoader {
    public static City loadFromFile(String path) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) continue;
                lines.add(line);
            }
        }

        if (lines.isEmpty()) {
            throw new IOException("Map file is empty: " + path);
        }

        int rows = lines.size();
        int cols = 0;
        for (String l : lines) cols = Math.max(cols, l.length());

        City city = new City(rows, cols);

        for (int r = 0; r < rows; r++) {
            String l = lines.get(r);
            for (int c = 0; c < cols; c++) {
                char ch;
                if (c < l.length()) {
                    ch = l.charAt(c);
                } else {
                    ch = 'E';
                }
                city.setCellAt(r, c, createCell(ch, r, c));
            }
        }
        return city;
    }

    private static Cell createCell(char ch, int r, int c) {
        switch (ch) {
            case 'H': return new Housing(r, c);
            case 'I': return new Industrial(r, c);
            case 'C': return new Commercial(r, c);
            case 'R': return new Road(r, c);
            case 'P': return new PowerPlant(r, c);
            case 'W': return new WaterStation(r, c);
            case 'T': return new InternetHub(r, c);
            case 'F': return new PoliceStation(r, c);
            case 'D': return new Hospital(r, c);
            case 'S': return new School(r, c);
            case 'E': return new EmptyCell(r, c);
            default:  return new EmptyCell(r, c);
        }
    }
}
