import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Plateau {

	Bushi[][] plateau = new Bushi[10][10];
	Joueur[] joueurs = new Joueur[2];
	int joueurCourant; // permet de connaitre le joueur qui
						// est entrain de jouer.(0,1);

	public Plateau() {
		// super();
		this.joueurs = new Joueur[2];
		this.plateau = new Bushi[10][10];
		joueurCourant = 0;
		this.initPlateau();
		joueurs[0] = new Joueur();
		joueurs[1] = new Joueur();
	}

	public void bloquerCases() { // bloque les cases sur les c�t�s du plateau

		int i;

		for (i = 0; i < 4; i++) {
			this.plateau[i][0].etat = -1;
			this.plateau[i][9].etat = -1;
		}

		for (i = 6; i < 10; i++) {
			this.plateau[i][0].etat = -1;
			this.plateau[i][9].etat = -1;
		}

	}

	/**
	 * Affiche en vert les déplacements possibles
	 * 
	 * @param deplacements
	 *
	 *            public void afficheDeplacement(ArrayList<Bushi> deplacements)
	 *            {
	 * 
	 *            for (int i = 0; i < 10; i++) { for (int j = 0; j < 10; j++) {
	 *            if (deplacements.contains(this.plateau[i][j])) {
	 *            System.out.print("1|");
	 * 
	 *            } else { System.out.print(this.plateau[i][j] + "|"); }
	 * 
	 *            } System.out.print("\n"); }
	 * 
	 *            }
	 * 
	 *            /** Affichage utilisé pour le debug, on ne peut stocker des
	 *            informations concernant la couleur dans une String
	 */
	public String toString() {

		String s = "";

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				switch (this.plateau[i][j].etat) {
				case 0:
					s += " ";
					break;
				case 1:
					s += "s";
					break;
				case 2:
					s += "l";
					break;
				case 3:
					s += "d";
					break;
				case -1:
					s += "X";
					break;
				case -2:
					s += "p";
					break;

				}
				s += "|";
				// s += this.plateau[i][j] + "|";

			}
			s += "\n";
		}

		return s;

	}

	/**
	 * Initialise toutes les cases du plateau � un Bushi(0,0,0,0)
	 */
	public void initPlateau() {

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				this.plateau[j][i] = new Bushi(i, j, 0, 0);
			}
		}

	}

	/**
	 * Initialise le plateau et place les bushis en suivant les instruction du
	 * fichier fileName
	 * 
	 * @param fileName
	 *            le nom du fichier � charger
	 */

	public void nouvellePartie(String fileName) {

		// On bloque les cases non accessibles
		this.initPlateau();
		this.bloquerCases();
		Scanner fichier = new Scanner("Init Scanner");

		try {

			fichier = new Scanner(new File(fileName)).useDelimiter("[\n]");

			while (fichier.hasNext()) {// tant que le fichier n'est pas
										// termine

				String ligne = fichier.next();// contient
												// la
												// ligne
				// courante de
				// fichier fileName
				/*
				 * Chaque ligne du fichier d�crit un bushi comme suit :
				 * type,abs,ord,joueur,jouable
				 * 
				 */

				String[] strArray = ligne.split(" ");
				// System.out.println("str length : " +
				// strArray.length);
				int[] intArray = new int[strArray.length - 1];

				for (int i = 0; i < strArray.length - 1; i++) {
					intArray[i] = Integer.parseInt(strArray[i]);
					// System.out.println(intArray[i]);
				}

				// On g�n�re la liste des Bushis de chaque joueur
				switch (intArray[0]) {
				case 1:
					joueurs[intArray[3] - 1].bushiJoueur.add(new Singe(intArray[1], intArray[2]));
					break;
				case 2:
					joueurs[intArray[3] - 1].bushiJoueur.add(new Lion(intArray[1], intArray[2]));
					break;
				case 3:
					joueurs[intArray[3] - 1].bushiJoueur.add(new Dragon(intArray[1], intArray[2]));
					break;
				case -2:
					joueurs[intArray[3] - 1].bushiJoueur.add(new Portail(intArray[1], intArray[2]));

				default:
					System.out.println("merde");
					break;
				}

			}

		}

		catch (FileNotFoundException e) {
			System.out.println(e);
		}

		/* On place les bushis de chaque joueur sur le plateau */
		for (Bushi b : joueurs[0].bushiJoueur) {
			plateau[b.ord][b.abs] = b;
			// System.out.println("coucou");
		}
		for (Bushi b : joueurs[1].bushiJoueur) {
			plateau[b.ord][b.abs] = b;
		}

		fichier.close();

	}

	public int YOLOLOLDeplacement() {
		Affichage.affichePlateau(this);

		ArrayList<Bushi> jouable = new ArrayList<Bushi>();
		Scanner sc = new Scanner(System.in);
		Bushi choixBushiDeplace;// Contient le bushi que l'on souhaite deplace
		Bushi choixDestination;// contient la destination choise pour le bushi
								// deplacee
		int choix = 0;

		// Presente la liste des bushis jouable du joueur

		for (Bushi b : joueurs[joueurCourant].bushiJoueur) {
			if (b.jouable == 0) {
				jouable.add(b);
			}
		}
		for (Bushi b : jouable) {
			choix = jouable.indexOf(b) + 1;
			System.out.println(choix + ":" + b);
		}
		// Le joueur choisi le bushi qu'il souhaite déplacer
		// try {
		choix = sc.nextInt() - 1;

		choixBushiDeplace = jouable.get(choix);

		// Le joueur choisi la destination du Bushi qu'il souhaite d�plac�
		choixDestination = choixBushiDeplace.choisirDeplacement(this);

		// Verifie si le bushi deplace saute
		// int saute = choixBushiDeplace.aSaute(choixDestination, this);
		// System.out.println(saute);

		// Effectuer le deplacement
		// jouable.get(choix).effectuerDeplacement(jouable.get(choix).choisirDeplacement(this),
		// this);
		/*
		 * } catch (InputMismatchException e) { System.out.println(
		 * "Saisie incorrecte"); } catch (IndexOutOfBoundsException e) {
		 * System.out.println("Vous n'avez pas saisie un des nombres proposés");
		 * sc.close(); return this.YOLOLOLDeplacement();
		 * 
		 * }
		 * 
		 * catch (Exception e) { System.out.println(e); }
		 */
		sc.close();
		return 0;
	}

	public static void main(String args[]) {

		Plateau p1 = new Plateau();
		p1.nouvellePartie("standard.txt");
		Affichage.affichePlateau(p1);
		// System.out.println(p1.plateau[1][0]);
		// System.out.println(p1);
		p1.YOLOLOLDeplacement();
		// p1.YOLOLOLDeplacement();
		// p1.afficheDeplacement(p1.plateau[2][1].listerDeplacement(p1));
		// Affichage.affichePlateau(p1, p1.plateau[1][2],
		// p1.plateau[1][2].listerDeplacement(p1));
		// System.out.println(p1.plateau[1][1]);
		// System.out.println(p1.plateau[1][1].etat);

	}

}
