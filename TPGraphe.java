package Metrique;
import java.io.*; 
import java.util.*;
import java.io.*;

public class TPGraphe {
	public static void main (String arg[]) throws IOException { 
		//int nb = Integer.parseInt(arg[0]);
		int nb=32;
		// graphe des diviseurs
		Graphe G1 = Graphe.randomDAG(10);  
		System.out.println(G1.toString());
		Graphe.toDot(G1,"prof.dot");
		G1.parcoursProfondeur(true);
		Graphe.toDot(G1,"profDiv.dot");
		G1.setColor("black");
		ArrayList L = G1.triTopologique(true); 
		System.out.println(L);
		Graphe.toDot(G1,"profDiv.dot");


	}
}