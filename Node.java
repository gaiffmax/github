package Metrique;
/*****************************************************/
import java.util.*;
/** Classe pour manipuler un noeud d'un graphe **/
public class Node implements Comparable {
    /** identifiant du Node **/
    private int id;
    /** listes de successeurs et de prédecesseurs **/
    private TreeSet succ;
    private TreeSet pred;
    /** attributs dot **/
    private String label;
    private String color;
    private String shape;
    /** permet de compter le nb d'appels à compareTo **/
    public static int nbCompareTo = 0;

    /** constructeur **/
    Node (int i) {
	this.id = i;
	this.succ = new TreeSet();
	this.pred = new TreeSet();
	this.label=Integer.toString(i);
	this.color="black";
	this.shape="ellipse";
    }
    /** constructeur **/
    Node (int i, String label) {
	this.id = i;
	this.succ = new TreeSet();
	this.pred = new TreeSet();
	this.label=label;
	this.color="black";
	this.shape="ellipse";
    }
    /** accès à l'identifiant **/
    public int id (){
	return this.id;
    }
    /** accès à l'ensemble des successeurs **/
    public TreeSet succ (){
	return this.succ;
    }
    /** accès à l'ensemble des predecesseurs **/
    public TreeSet pred (){
	return this.pred;
    }
    /** méthodes d'accès à un attribut **/
    public String color (){
	return this.color;
    }
    /** méthodes d'accès à un attribut **/
    public void setColor (String c){
	this.color = c; // possibilité de tester la validité de c
    }
    /** méthodes d'accès à un attribut **/
    public String shape (){
	return this.shape;
    }
    /** méthodes d'accès à un attribut **/
    public void setShape (String s){
	this.shape = s; // possibilité de tester la validité de c
    }
    /** méthodes d'accès à un attribut **/
    public String label (){
	return this.label;
    }
    /** méthodes d'accès à un attribut **/
    public void setLabel (String l){
	this.label = l;
    }
    /** ajout d'un successeur **/
    public boolean addSucc (Arc A ) {
	return this.succ.add(A);
    }
    /** test de l'existence d'un successeur **/
    public boolean containsSucc (Arc A) { 
	return this.succ.contains(A);
    }
    /** suppression d'un successeur **/
    public boolean removeSucc (Arc A) {
	return this.succ.remove(A);
    }
    /** ajout d'un predecesseur **/
    public boolean addPred (Arc A ) {
	return this.pred.add(A);
    }
    /** test de l'existence d'un predecesseur **/
    public boolean containsPred (Arc A) { 
	return this.pred.contains(A);
    }
    /** suppression d'un predecesseur **/
    public boolean removePred (Arc A) {
	return this.pred.remove(A);
    }

    /** methode de comparaison necessaire pour implementer Comparable
	permet de rechercher un Node dans un TreeSet **/
    public int compareTo (Object O)  {  
	// nbCompareTo++;
    	Node N = (Node) O ; // C'est Node qui est entre
	if (this.id > N.id())
	    return 1;
	if (this.id < N.id())
	    return -1;
	return 0;    
    }
    /** methode d'affichage **/
    public String toString () { 
	return  this.id+" ";
    }
    /** methode d'affichage grammaire dot  **/ 
    public String toDot () { 
	return  this.id+" [label="+this.label+",color="+this.color+",shape="+this.shape+"]\n";
    }
    
    public void toDot2(){
    }
    
}// fin de Node
