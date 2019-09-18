package metrique;
/*****************************************************/

import java.util.*;
import java.io.*;
import java.util.Random;

/**
 * Classe Graphe permettant de manipuler des graphes. Représentation par listes
 * de successeurs
 **/
class Graphe {
	/** ensemble de Node (ou NodeSet) **/
	private TreeSet s;
	private static Random random = new Random();
	
	public static final String BLACK = "black";
	public static final String BLUE = "blue";
	public static final String RED = "red";
	public static final String GREEN = "green";

	/** constructeur **/
	Graphe() {
		this.s = new TreeSet();
	}

	/** accès à l'ensemble de Node **/
	public SortedSet getS() {
		return this.s;
	}

	/** ajout d'un Node dans le graphe **/
	public boolean addNode(Node N) {
		return this.s.add(N);
	}

	/** test de l'existence d'un Node dans le graphe **/
	public boolean containsNode(int i) {
		return this.s.contains(new Node(i));
	}

	/** suppression d'un Node dans le graphe **/
	public boolean removeNode(Node N) {
		if (!this.s.remove(N))
			return false;
		for (Iterator I = N.succ().iterator(); I.hasNext();) {
			Arc A = (Arc) I.next();
			Node n2 = A.to();
			n2.removePred(A);
		}
		for (Iterator I = N.pred().iterator(); I.hasNext();) {
			Arc A = (Arc) I.next();
			Node n2 = A.from();
			n2.removeSucc(A);
		}
		return true;
	}

	/** accès à un Node du graphe **/
	public Node getNode(int i) {
		if (this.containsNode(i))
			for (Iterator I = this.s.iterator(); I.hasNext();) {
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
		return this.s.size();
	}

	public int nbArcs() {
		int nb = 0;
		for (Iterator I = s.iterator(); I.hasNext();) {

			Node N = (Node) I.next();
			nb += N.succ().size();
		}
		return nb;
	}

	/* colorier tous les noeuds et arcs d'une meme couleur */
	public void setColor(String color) {
		for (Iterator I = s.iterator(); I.hasNext();) {
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
		for (Iterator I = s.iterator(); I.hasNext();) {
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
		try (DataOutputStream out = new DataOutputStream(fich)){
			out.writeBytes("digraph G {\n");
			StringBuilder string
			String nodes = "";
			String arcs = "";
			// parcours de l'ensemble de Node
			for (Iterator I = G.s.iterator(); I.hasNext();) {
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
					StringBuilder stringbuilder = new StringBuilder("arcs");
					stringbuilder.append(" ");
					stringbuilder.append(S);
				}
			}
			out.writeBytes(nodes);
			out.writeBytes(arcs);
			out.writeBytes("}");
		} catch (IOException e) {
			//...
		}
	}

	public static Graphe divGraph1(int nb, boolean visu) {
		Graphe G = new Graphe();
		for (int i = 2; i <= nb; i++) {
			Node N = new Node(i);
			if (i % 2 == 0 && visu) {
				N.setColor(RED);
			}
			G.addNode(N);
		}
		// ajout des arcs
		for (int i = 2; i <= nb; i++)

			for (int j = 2; j <= nb; j++)

				if (i % j == 0) {
					Node n1 = G.getNode(i);
					Node n2 = G.getNode(j);
					Arc A = new Arc(n2, n1);
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
		for (Iterator I = s.iterator(); I.hasNext();) {
			Node N = (Node) I.next();
			N.setColor(BLACK);
		}
		// parcours à partir d'une source inexplorée
		for (Iterator I = this.s.iterator(); I.hasNext();) {
			Node N = (Node) I.next();
			if (N.color().equals(BLACK))
				parcoursLargeur(N, visu);
		}
		// remet les sommets en noir
		for (Iterator I = s.iterator(); I.hasNext();) {
			Node N = (Node) I.next();
			N.setColor(BLACK);
		}
	}

	/* parcours en largeur à partir d'une source */
	private void parcoursLargeur(Node S, boolean visu) {
		S.setColor(BLUE);
		ArrayList F = new ArrayList();
		F.add(S);
		while (!F.isEmpty()) {
			Node n = (Node) F.get(0);
			for (Iterator I = n.succ().iterator(); I.hasNext();) {
				Arc a = (Arc) I.next();
				Node n2 = a.to();
				if (n2.color().equals(BLACK)) {
					if (!F.contains(n2)) {
						F.add(n2);
						if (visu)
							a.setColor(BLUE);
					}
				}
			}
			F.remove(n);
			n.setColor(BLUE);
		}
	}

	/* parcours en profondeur */
	public void parcoursProfondeur(boolean visu) {
		// couleur noire (inexplorée) pour tous les noeuds
		for (Iterator i = s.iterator(); i.hasNext();) {
			Node n = (Node) i.next();
			n.setColor(BLACK);
		}
		// parcours à partir d'une source inexplorée
		for (Iterator I = this.s.iterator(); I.hasNext();) {
			Node n = (Node) I.next();
			if (n.color().equals(BLACK))
				parcoursProfondeur(n, visu);
		}
	}

	/* parcours en profondeur récursif à partir d'une source */
	private void parcoursProfondeur(Node S, boolean visu) {
		S.setColor(RED);
		for (Iterator I = S.succ().iterator(); I.hasNext();) {
			Arc A = (Arc) I.next();
			Node N = A.to();
			if (!N.color().equals(RED)) {
				if (visu)
					A.setColor(GREEN);
				parcoursProfondeur(N, visu);
			}
		}
	}

	/**********************************************************/
	/** méthode statique de génération d'un graphe aléatoire **/
	public static Graphe randomGraphe(int nb) {
		Graphe g = new Graphe();
		// ensemble S de Node
		int i = nb;
		while (i > 0) {
			int id = 10 * nb * random.nextInt();
			if (g.addNode(new Node(id)))
				i--;
		}
		// ensemble A d'Arcs
		for (Iterator I = g.getS().iterator(); I.hasNext();) {
			Node n1 = (Node) I.next();
			for (Iterator J = g.getS().iterator(); J.hasNext();) {
				Node n2 = (Node) J.next();
				int choice = 10 * random.nextInt();
				/*
				 * choice compris entre 0 et 10. si choice < 5: on n'ajoute pas
				 * l'arc (n1,n2) si choice > 5: on ajoute l'arc
				 */
				if (choice > 5 && n1 != n2)
					g.addArc(new Arc(n1, n2));
			}
		}
		return g;
	}
	
	

	/** méthode statique de génération d'un DAG aléatoire **/
	public static Graphe randomDAG(int nb) {
		Graphe G = new Graphe();
		// ensemble S de Node
		int i = nb;
		while (i > 0) {
			int id = 10 * nb * random.nextInt();
			if (G.addNode(new Node(id, "n" + id)))
				i--;
		}
		// ensemble A d'Arcs
		for (Iterator I = G.getS().iterator(); I.hasNext();) {
			Node n1 = (Node) I.next();
			for (Iterator J = G.getS().iterator(); J.hasNext();) {
				Node n2 = (Node) J.next();
				if (n2.id() > n1.id()) {
					int choice = 10 * random.nextInt();
					if (choice > 5){
						Arc A = new Arc(n1, n2);
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
	public SortedSet sources(boolean visu) {
		SortedSet sources = new TreeSet();
		for (Iterator I = s.iterator(); I.hasNext();) {
			Node N = (Node) I.next();
			if (N.pred().isEmpty()) {

				sources.add(N);
				if (visu)
					N.setShape("box");
			}
		}
		return sources;
	}

	/** calcul d'un tri topologique **/
	public List triTopologique(boolean visu) {
		
		SortedSet sources = this.sources(visu);
		List choisis = new ArrayList();
		while (!sources.isEmpty()) {
			// choix d'un Node dans sources
			int nb = (random.nextInt() * sources.size());
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
						Ax.setColor(RED);
				}
			}
		}
		return choisis;
	}

	// test de l'existence d'un cycle
	public boolean cycles(boolean visu) {
		List T = this.triTopologique(false);
		if (T.size() != this.nbNodes()) {
			if (visu) {
				for (Iterator I = this.getS().iterator(); I.hasNext();) {
					Node N = (Node) I.next();
					if (!T.contains(N))
						N.setColor(BLUE);
				}
			}
			return true;
		} else
			return false;
	}

}// fin de Graph
