
import java.util.InputMismatchException;
import java.util.Scanner;

public class SoftwareEstimate {

	static Scanner scan = new Scanner(System.in);
	static int actorPoint, useCases, uucp, rMhourEs;
	static double tcf, szUC, tFactor, eFactor, ef, ucp, mHours, adjMHours, tmHours;
	static int[] pERating = new int[8];

	public static void main(String[] args) throws Exception {
		calActorPoints();
	}

	public static void calActorPoints() throws Exception {
		System.out.println("\n**********Weighting Actors for Complexity**********");
		int wFsimple = 1;
		int wFaverage = 2;
		int wFcomplex = 3;

		System.out.print("Quantity of Simple Actor: ");
		int s = scan.nextInt();
		System.out.print("Quantity of Average Actor: ");
		int a = scan.nextInt();
		System.out.print("Quantity of Complex Actor: ");
		int c = scan.nextInt();

		actorPoint = (wFsimple * s) + (wFaverage * a) + (wFcomplex * c);
		System.out.println("==============================================");
		System.out.println("Total Actor Points: " + actorPoint);
		Thread.sleep(2000);
		calUseCases();
	}

	public static void calUseCases() throws Exception {
		System.out.println("\n**********Weighting Use Cases**********");
		int wFsimple = 5;
		int wFaverage = 10;
		int wFcomplex = 15;

		System.out.print("Quantity of Simple Use Case: ");
		int s = scan.nextInt();
		System.out.print("Quantity of Average Use Case: ");
		int a = scan.nextInt();
		System.out.print("Quantity of Complex Use Case: ");
		int c = scan.nextInt();

		useCases = (wFsimple * s) + (wFaverage * a) + (wFcomplex * c);
		System.out.println("==============================================");
		System.out.println("Total Use Cases: " + useCases);

		uucp = actorPoint + useCases;
		System.out.println("Unadjusted Use Case Points: " + uucp);
		Thread.sleep(2000);
		calTFactor();
	}

	public static void calTFactor() throws Exception {
		int[] pTRating = new int[13];
		String description[] = { "Must have a distributed solution", "Must respond to specific performance objectives",
				"Must meet end-user efficiency desires", "Complex internal processing", "Code must be reusable",
				"Must be easy to install", "Must be easy to use", "Must be portable", "Must be easy to change",
				"Must allow concurrent users", "Includes special security features",
				"Must provide direct access for 3rd parties", "Requires special user training facilities" };
		double[] wFactor = { 2, 1, 1, 1, 1, 0.5, 0.5, 2, 1, 1, 1, 1, 1 };
		int x = 0;

		System.out.println("\n**********Weighting Technical Factors**********");
		System.out.println("Please rate from 0 to 5");

		do {
			try {
				for (int i = 0; i < 13; i++) {
					System.out.print("Project Rating of T" + (i + 1) + " (" + description[i] + "): ");
					pTRating[i] = scan.nextInt();
					if ((pTRating[i] > 5) || (pTRating[i] < 0)) {
						throw new Exception("Wrong input");
					}
					x = 1;
				}
			} catch (InputMismatchException e) {
				System.out.println("-------------------------------------");
				System.out.println("Wrong input, please input again.\n");
				Thread.sleep(2000);
				scan.next();
				x = 2;

			} catch (Exception e) {
				System.out.println("-------------------------------------");
				System.out.println("Wrong input, please rate it from 0 to 5.\n");
				Thread.sleep(2000);
				x = 2;
			}
		} while (x == 2);

		for (int i = 0; i < 13; i++) {
			double total = wFactor[i] * pTRating[i];
			tFactor += total;
		}

		tcf = (0.01 * tFactor) + 0.6;
		szUC = uucp * tcf;

		System.out.println("==============================================");
		System.out.println("Total Technical Factors: " + tFactor);
		System.out.println("Size of software (use case) project: " + szUC);
		Thread.sleep(2000);
		calEF();
	}

	public static void calEF() throws Exception {
		double[] wFactor = { 1, 0.5, 1, 0.5, 0, 2, -1, -1 };
		String description[] = { "Familiar with FPT software process", "Application experience",
				"Paradigm experience (OO)", "Lead analyst capability", "Motivation", "Stable Requirements",
				"Part-time workers", "Difficulty of programming language" };
		int x = 0;

		System.out.println("\n**********Weighting Experience Factors**********");
		System.out.println("Please rate from 0 to 5");

		do {
			try {
				for (int i = 0; i < 8; i++) {
					System.out.print("Project Rating of E" + (i + 1) + " (" + description[i] + "): ");
					pERating[i] = scan.nextInt();
					if ((pERating[i] > 5) || (pERating[i] < 0)) {
						throw new Exception("Wrong input");
					}
					x = 1;
				}

			} catch (InputMismatchException e) {
				System.out.println("-------------------------------------");
				System.out.println("Wrong input, please input again.\n");
				Thread.sleep(2000);
				scan.next();
				x = 2;

			} catch (Exception e) {
				System.out.println("-------------------------------------");
				System.out.println("Wrong input, please rate it from 0 to 5.\n");
				Thread.sleep(2000);
				x = 2;
			}
		} while (x == 2);

		for (int i = 0; i < 8; i++) {
			double total = wFactor[i] * pERating[i];
			eFactor += total;
		}

		ef = (-0.03 * eFactor) + 1.4;
		ucp = szUC * ef;
		System.out.println("==============================================");
		System.out.println("Total Experience Factors: " + eFactor);
		System.out.println("Use Case Points: " + ucp);
		Thread.sleep(2000);
		calManHours();
	}

	public static void calManHours() throws Exception {
		int er = 0;
		int numberFR = 0;
		double percentage;

		for (int i = 0; i <= 5; i++) {
			if (pERating[i] <= 2) {
				numberFR++;
			}
		}
		for (int i = 6; i <= 7; i++) {
			if (pERating[i] >= 4) {
				numberFR++;
			}
		}
		if (numberFR <= 2) {
			er = 20;
		} else if ((numberFR == 3) || (numberFR == 4)) {
			er = 28;
		} else {
			System.out.println("-----------------------------------------------");
			System.out.println("Sorry, please restructure the project team\n");
			Thread.sleep(2000);
			calActorPoints();
		}

		mHours = er * ucp;
		System.out.println("==============================================");
		System.out.printf("Total man-hours: %.2f\n", mHours);
		Thread.sleep(2000);
		System.out.print("\nIdentify the assumptions and apply a coefficient as a percentage: ");
		percentage = scan.nextDouble();
		adjMHours = (1.0 + (percentage / 100)) * mHours;
		System.out.println("==============================================");
		System.out.printf("Total adjusted man-hours: %.2f\n", adjMHours);
		calReport();
	}

	public static void calReport() {
		System.out.println("\n**********Weighting Reports for Complexity**********");
		int wFsimple = 12;
		int wFaverage = 20;
		int wFcomplex = 40;

		System.out.print("Quantity of Simple Report: ");
		int s = scan.nextInt();
		System.out.print("Quantity of Average Report: ");
		int a = scan.nextInt();
		System.out.print("Quantity of Complex Report: ");
		int c = scan.nextInt();

		rMhourEs = (wFsimple * s) + (wFaverage * a) + (wFcomplex * c);
		tmHours = adjMHours + rMhourEs;

		System.out.println("==============================================");
		System.out.println("Total Report Man-hour Estimate: " + rMhourEs);
		System.out.printf("Total man-hours: %.2f\n", tmHours);
	}
}
