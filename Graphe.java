package Metrique;
/*****************************************************/

import java.util.*;
import java.io.*;

/**
 * Classe Graphe permettant de manipuler des graphes. Représentation par listes
 * de successeurs
 **/
class Graphe {
	/** ensemble de Node (ou NodeSet) **/
	public TreeSet S;

	/** constructeur **/
	Graphe() {
		this.S = new TreeSet();
	}

	/** accès à l'ensemble de Node **/
	public TreeSet getS() {
		return this.S;
	}

	/** ajout d'un Node dans le graphe **/
	public boolean addNode(Node N) {
		return this.S.add(N);
	}

	/** test de l'existence d'un Node dans le graphe **/
	public boolean containsNode(int i) {
		return this.S.contains(new Node(i));
	}

	/** suppression d'un Node dans le graphe **/
	public boolean removeNode(Node N) {
		if (!this.S.remove(N))
			return false;
		for (Iterator I = N.succ().iterator(); I.hasNext();) {
			Arc A = (Arc) I.next();
			Node N2 = A.to();
			N2.removePred(A);
		}
		for (Iterator I = N.pred().iterator(); I.hasNext();) {
			Arc A = (Arc) I.next();
			Node N2 = A.from();
			N2.removeSucc(A);
		}
		return true;
	}

	/** accès à un Node du graphe **/
	public Node getNode(int i) {
		if (this.containsNode(i))
			for (Iterator I = this.S.iterator(); I.hasNext();) {
				Node N = (Node) I.next();
				if (N.id() == i)
					return N;
			}
		return null;
	}

	/** test de l'existence d'un Arc dans le graphe **/
	public boolean containsArc(int i, int j) {
		if (this.containsNode(i) && this.containsNode(j)) {
			Node from = this.getNode(i);
			Node to = this.getNode(j);
			return from.containsSucc(new Arc(from, to));
		}
		return false;
	}

	/** accès à un Arc du graphe **/
	public Arc getArc(int i, int j) {
		if (this.containsArc(i, j)) {
			Node from = this.getNode(i);
			for (Iterator I = from.succ().iterator(); I.hasNext();) {
				Arc A = (Arc) I.next();
				if (A.from().id() == i && A.to().id() == j)
					return A;
			}
		}
		return null;
	}

	/** ajout d'un Arc dans le graphe **/
	public boolean addArc(Arc A) {
		int i = A.from().id();
		int j = A.to().id();
		if (this.containsNode(i) && this.containsNode(j) && !this.containsArc(i, j)) {
			A.from().addSucc(A);
			A.to().addPred(A);
			return true;
		}
		return false;
	}

	/** suppression d'un Arc dans le graphe **/
	public boolean removeArc(Arc A) {
		int i = A.from().id();
		int j = A.to().id();
		if (this.containsNode(i) && this.containsNode(j) && this.containsArc(i, j)) {
			A.from().removeSucc(A);
			A.to().removePred(A);
			return true;
		}
		return false;
	}

	public int nbNodes() {
		return this.S.size();
	}

	public int nbArcs() {
		int nb = 0;
		for (Iterator I = S.iterator(); I.hasNext();) {

			Node N = (Node) I.next();
			nb += N.succ().size();
		}
		return nb;
	}

	/* colorier tous les noeuds et arcs d'une meme couleur */
	public void setColor(String color) {
		for (Iterator I = S.iterator(); I.hasNext();) {
			Node N = (Node) I.next();
			N.setColor(color);
			for (Iterator J = N.succ().iterator(); J.hasNext();) {
				Arc A = (Arc) J.next();
				A.setColor(color);
			}
		}
	}

	/** methode d'affichage **/
	public String toString() {
		String graph = "";
		String nodes = "Nodes = {";
		String arcs = "Arcs = {";
		graph += "G --> " + this.nbNodes() + "Nodes \n";
		graph += "       " + this.nbArcs() + "Arcs \n ";
		for (Iterator I = S.iterator(); I.hasNext();) {
			Node N = (Node) I.next();
			nodes += " " + N;
			TreeSet succ = N.succ();
			// Parcours de l'ensemble des successeurs
			for (Iterator J = succ.iterator(); J.hasNext();) {
				Arc A = (Arc) J.next();
				arcs += " " + A;
			}

		}
		return graph + nodes + "}\n" + arcs + "}\n";

	}

	/** methode d'affichage grammaire dot **/
	public static void toDot(Graphe G, String filename) throws IOException {
		FileOutputStream fich = new FileOutputStream(filename);
		DataOutputStream out = new DataOutputStream(fich);

		out.writeBytes("digraph G {\n");
		String nodes = "";
		String arcs = "";
		// parcours de l'ensemble de Node
		for (Iterator I = G.S.iterator(); I.hasNext();) {
			Node N = (Node) I.next();
			nodes += " " + N.toDot();
			TreeSet succ = N.succ();
			// parcours de l'ensemble des successeurs de N
			for (Iterator J = succ.iterator(); J.hasNext();) {
				Arc A = (Arc) J.next();

				String arc = A.toString();
				String S = "";
				String label = "";
				if (A.label().length() != 0)
					label = "label=" + A.label() + ",";
				String color = "color=" + A.color();
				S += arc + " [" + label + color + "]\n";
				arcs = arcs + " " + S;
			}
		}
		out.writeBytes(nodes);
		out.writeBytes(arcs);
		out.writeBytes("}");
		out.close();
	}

	public static Graphe divGraph1(int nb, boolean visu) {
		Graphe G = new Graphe();
		for (int i = 2; i <= nb; i++) {
			Node N = new Node(i);
			if (i % 2 == 0 && visu) {
				N.setColor("red");
			}
			G.addNode(N);
		}
		// ajout des arcs
		for (int i = 2; i <= nb; i++)

			for (int j = 2; j <= nb; j++)

				if (i % j == 0) {
					Node N1 = G.getNode(i);
					Node N2 = G.getNode(j);
					Arc A = new Arc(N2, N1);
					if (visu) {
						int div = i / j;
						String label = div + "";
						A.setLabel(label);
					}
					G.addArc(A);
				}

		return G;
	}

	// ---------
	/* parcours en largeur */
	public void parcoursLargeur(boolean visu) {
		// couleur noire (inexplorée) pour tous les noeuds
		for (Iterator I = S.iterator(); I.hasNext();) {
			Node N = (Node) I.next();
			N.setColor("black");
		}
		// parcours à partir d'une source inexplorée
		for (Iterator I = this.S.iterator(); I.hasNext();) {
			Node N = (Node) I.next();
			if (N.color().equals("black"))
				parcoursLargeur(N, visu);
		}
		// remet les sommets en noir
		for (Iterator I = S.iterator(); I.hasNext();) {
			Node N = (Node) I.next();
			N.setColor("black");
		}
	}

	/* parcours en largeur à partir d'une source */
	private void parcoursLargeur(Node S, boolean visu) {
		S.setColor("blue");
		ArrayList F = new ArrayList();
		F.add(S);
		while (!F.isEmpty()) {
			Node N = (Node) F.get(0);
			for (Iterator I = N.succ().iterator(); I.hasNext();) {
				Arc A = (Arc) I.next();
				Node N2 = (Node) A.to();
				if (N2.color().equals("black")) {
					if (!F.contains(N2)) {
						F.add(N2);
						if (visu)
							A.setColor("blue");
					}
				}
			}
			F.remove(N);
			N.setColor("blue");
		}
	}

	/* parcours en profondeur */
	public void parcoursProfondeur(boolean visu) {
		// couleur noire (inexplorée) pour tous les noeuds
		for (Iterator I = S.iterator(); I.hasNext();) {
			Node N = (Node) I.next();
			N.setColor("black");
		}
		// parcours à partir d'une source inexplorée
		for (Iterator I = this.S.iterator(); I.hasNext();) {
			Node N = (Node) I.next();
			if (N.color().equals("black"))
				parcoursProfondeur(N, visu);
		}
		// remettre les sommets en noir
		/*
		 * for (Iterator I=S.iterator(); I.hasNext(); ) { Node N = (Node)
		 * I.next(); N.setColor("black"); }
		 */
	}

	/* parcours en profondeur récursif à partir d'une source */
	private void parcoursProfondeur(Node S, boolean visu) {
		S.setColor("red");
		for (Iterator I = S.succ().iterator(); I.hasNext();) {
			Arc A = (Arc) I.next();
			// A.setNode("black");
			Node N = (Node) A.to();
			if (!N.color().equals("red")) {
				if (visu)
					A.setColor("green");
				parcoursProfondeur(N, visu);
			}
		}
	}

	/**********************************************************/
	/** méthode statique de génération d'un graphe aléatoire **/
	public static Graphe randomGraphe(int nb) {
		Graphe G = new Graphe();
		// ensemble S de Node
		int i = nb;
		while (i > 0) {
			int id = (int) Math.rint(10 * nb * Math.random());
			if (G.addNode(new Node(id)))
				i--;
		}
		// ensemble A d'Arcs
		for (Iterator I = G.getS().iterator(); I.hasNext();) {
			Node N1 = (Node) I.next();
			for (Iterator J = G.getS().iterator(); J.hasNext();) {
				Node N2 = (Node) J.next();
				int choice = (int) Math.rint(10 * Math.random());
				/*
				 * choice compris entre 0 et 10. si choice < 5: on n'ajoute pas
				 * l'arc (N1,N2) si choice > 5: on ajoute l'arc
				 */
				if (choice > 5 && N1 != N2)
					G.addArc(new Arc(N1, N2));
			}
		}
		return G;
	}
	
	

	/** méthode statique de génération d'un DAG aléatoire **/
	public static Graphe randomDAG(int nb) {
		Graphe G = new Graphe();
		// ensemble S de Node
		int i = nb;
		while (i > 0) {
			int id = (int) Math.rint(10 * nb * Math.random());
			if (G.addNode(new Node(id, "n" + id))==true)
				i--;
		}
		// ensemble A d'Arcs
		for (Iterator I = G.getS().iterator(); I.hasNext();) {
			Node N1 = (Node) I.next();
			for (Iterator J = G.getS().iterator(); J.hasNext();) {
				Node N2 = (Node) J.next();
				if (N2.id() > N1.id()) {
					int choice = (int) Math.rint(10 * Math.random());
					if (choice > 5){
						Arc A = new Arc(N1, N2);
						int il = A.from().id();
						int jl = A.to().id();
						if (G.containsNode(il))
							if (G.containsNode(jl))
								if( !G.containsArc(il, jl)) {
									A.from().addSucc(A);
									A.to().addPred(A);
								}
						
					}
				}
			}
		}
		return G;
	}

	/************************/
	/** calcul des sources **/
	public TreeSet sources(boolean visu) {
		TreeSet sources = new TreeSet();
		for (Iterator I = S.iterator(); I.hasNext();) {
			Node N = (Node) I.next();
			if (N.pred().size() == 0) {

				sources.add(N);
				if (visu)
					N.setShape("box");
			}
		}
		return sources;
	}

	/** calcul d'un tri topologique **/
	public ArrayList triTopologique(boolean visu) {
		TreeSet sources = this.sources(visu);
		ArrayList choisis = new ArrayList();
		while (sources.size() != 0) {
			// choix d'un Node dans sources
			int nb = (int) (Math.random() * sources.size());
			if (nb == 0)
				nb++;
			Node Nx = null;
			for (Iterator I = sources.iterator(); I.hasNext();) {
				if (nb == 1)
					Nx = (Node) I.next();
				else
					I.next();
				nb--;
			}
			sources.remove(Nx);
			choisis.add(Nx);
			// maj des sources par parcours des succ de x
			for (Iterator I = Nx.succ().iterator(); I.hasNext();) {
				Arc Ax = (Arc) I.next();
				Node Ny = Ax.to();
				boolean ajouty = true;
				for (Iterator J = Ny.pred().iterator(); J.hasNext();) {
					Arc Ay = (Arc) J.next();
					Node Nz = Ay.from();
					
					if (!choisis.contains(Nz))
						ajouty = false;

				}
				if (ajouty) {
					sources.add(Ny);
					if (visu)
						Ax.setColor("red");
				}
			}
		}
		return choisis;
	}

	// test de l'existence d'un cycle
	public boolean cycles(boolean visu) {
		ArrayList T = this.triTopologique(false);
		if (T.size() != this.nbNodes()) {
			if (visu) {
				for (Iterator I = this.getS().iterator(); I.hasNext();) {
					Node N = (Node) I.next();
					if (!T.contains(N))
						N.setColor("blue");
				}
			}
			return true;
		} else
			return false;
	}

}// fin de Graph
