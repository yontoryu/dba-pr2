package pr2mapAgent;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        String filename = "/home/badi/Documents/Desarollo_basado_en_Agentes/Pr2-maps/mapWithVerticalWall.txt";
        Map map = new Map(filename);
        map.printMap();
    }
}
