package main.java.service.AEF;

public class Automat {

	Integer[][] auto;
	boolean[] init;
	boolean[] fin;
	Integer[][] epsilon;
	int root;

	public String toString() {
		String str = "";
		System.out.println("Auto : ");

		for (int i = 0; i < auto.length; i++) {
			for (int j = 0; j < auto[0].length; j++) {
				if (auto[i][j] != null)
					System.out.print(i + " => " + j + " : " + auto[i][j] + " , ");
			}
			System.out.println();
		}

		System.out.println("Epsilon : ");

		for (int i = 0; i < epsilon.length; i++) {
			System.out.print(i + " => " + 0 + " : [" + epsilon[i][0] + " , " + epsilon[i][1] + "] , ");
			System.out.println();
		}

		System.out.println("Etat initial : ");
		for (int i = 0; i < init.length; i++) {
			System.out.print(i + " => " + init[i] + " , ");

		}
		System.out.println();
		System.out.println("Etat Final : ");
		for (int i = 0; i < fin.length; i++) {
			System.out.print(i + " => " + fin[i] + " , ");
		}

		return str;
	}

	public Automat(int root, Automat au1, Automat au2) {
		this.root = root;
		if (au1 != null && au2 != null) {

			int row1 = au1.auto.length;
			int row2 = au2.auto.length;

			if (root == RegEx.CONCAT) {

				// auto

				this.auto = new Integer[row1 + row2][256];
				for (int i = 0; i < row1; i++) {
					for (int j = 0; j < 255; j++) {
						if (au1.auto[i][j] != null) {
							this.auto[i][j] = au1.auto[i][j];
						} else {
							this.auto[i][j] = null;
						}
					}
				}
				for (int i = 0; i < row2; i++) {
					for (int j = 0; j < 255; j++) {
						if (au2.auto[i][j] != null) {
							this.auto[i + row1][j] = au2.auto[i][j] + row1;
						} else {
							this.auto[i + row1][j] = null;
						}

					}
				}

				// Init

				init = new boolean[row1 + row2];

				for (int i = 0; i < row1; i++) {
					this.init[i] = au1.init[i];
				}
				for (int i = 0; i < row2; i++) {
					this.init[i + row1] = false;
				}

				// Fin

				fin = new boolean[row1 + row2];

				for (int i = 0; i < row1; i++) {
					this.fin[i] = false;
				}
				for (int i = 0; i < row2; i++) {
					this.fin[i + row1] = au2.fin[i];
				}

				// Epsilon

				epsilon = new Integer[row1 + row2][2];
				for (int i = 0; i < row1 - 1; i++) {
					if (au1.epsilon[i][0] != null && au1.epsilon[i][1] != null) {
						this.epsilon[i][0] = au1.epsilon[i][0];
						this.epsilon[i][1] = au1.epsilon[i][1];
					} else if (au1.epsilon[i][0] != null) {
						this.epsilon[i][0] = au1.epsilon[i][0];
						this.epsilon[i][1] = null;
					} else if (au1.epsilon[i][1] != null) {
						this.epsilon[i][0] = null;
						this.epsilon[i][1] = au1.epsilon[i][1];
					} else {
						this.epsilon[i][0] = null;
						this.epsilon[i][1] = null;
					}

				}
				this.epsilon[row1 - 1][0] = row1;
				for (int i = 0; i < row2; i++) {

					if (au2.epsilon[i][0] != null && au2.epsilon[i][1] != null) {
						this.epsilon[i + row1][0] = au2.epsilon[i][0] + row1;
						this.epsilon[i + row1][1] = au2.epsilon[i][1] + row1;
					} else if (au2.epsilon[i][0] != null) {
						this.epsilon[i + row1][0] = au2.epsilon[i][0] + row1;
						this.epsilon[i + row1][1] = null;
					} else if (au2.epsilon[i][1] != null) {
						this.epsilon[i + row1][0] = null;
						this.epsilon[i + row1][1] = au2.epsilon[i][1] + row1;
					} else {
						this.epsilon[i + row1][0] = null;
						this.epsilon[i + row1][1] = null;
					}
				}

			}
			if (root == RegEx.ALTERN) {
				// auto
				this.auto = new Integer[row1 + row2 + 2][256];
				for (int i = 0; i < 255; i++) {
					this.auto[0][i] = null;
				}
				for (int i = 0; i < row1; i++) {
					for (int j = 0; j < 255; j++) {
						if (au1.auto[i][j] != null) {
							this.auto[i + 1][j] = au1.auto[i][j] + 1;
						} else {
							this.auto[i + 1][j] = null;
						}
					}

				}

				for (int i = 0; i < row2; i++) {
					for (int j = 0; j < 255; j++) {
						if (au2.auto[i][j] != null) {
							this.auto[i + row1 + 1][j] = au2.auto[i][j] + row1 + 1;
						} else {
							this.auto[i + row1 + 1][j] = null;
						}
					}

				}

				for (int i = 0; i < 255; i++) {
					this.auto[row1 + row2 + 1][i] = null;
				}

				// init

				init = new boolean[row1 + row2 + 2];

				this.init[0] = true;
				for (int i = 1; i < row1 + row2 + 2; i++) {
					this.init[i] = false;
				}

				// fin

				fin = new boolean[row1 + row2 + 2];
				this.fin[row1 + row2 + 1] = true;
				for (int i = 0; i < row1 + row2 + 1; i++) {
					this.fin[i] = false;
				}

				// Epsilon

				epsilon = new Integer[row1 + row2 + 2][2];

				epsilon[0][0] = 1;
				epsilon[0][1] = row1 + 1;

				for (int i = 0; i < row1; i++) {
					if (au1.epsilon[i][0] != null && au1.epsilon[i][1] != null) {
						this.epsilon[i + 1][0] = au1.epsilon[i][0] + 1;
						this.epsilon[i + 1][1] = au1.epsilon[i][1] + 1;
					} else if (au1.epsilon[i][0] != null) {
						this.epsilon[i + 1][0] = au1.epsilon[i][0] + 1;
						this.epsilon[i + 1][1] = null;
					} else if (au1.epsilon[i][1] != null) {
						this.epsilon[i + 1][0] = null;
						this.epsilon[i + 1][1] = au1.epsilon[i][1] + 1;
					} else {
						this.epsilon[i + 1][0] = null;
						this.epsilon[i + 1][1] = null;
					}

				}

				for (int i = 0; i < row2; i++) {
					if (au2.epsilon[i][0] != null && au2.epsilon[i][1] != null) {
						this.epsilon[i + row1 + 1][0] = au2.epsilon[i][0] + row1 + 1;
						this.epsilon[i + row1 + 1][1] = au2.epsilon[i][1] + row1 + 1;
					} else if (au2.epsilon[i][0] != null) {
						this.epsilon[i + row1 + 1][0] = au2.epsilon[i][0] + row1 + 1;
						this.epsilon[i + row1 + 1][1] = null;
					} else if (au2.epsilon[i][1] != null) {
						this.epsilon[i + row1 + 1][0] = null;
						this.epsilon[i + row1 + 1][1] = au2.epsilon[i][1] + row1 + 1;
					} else {
						this.epsilon[i + row1 + 1][0] = null;
						this.epsilon[i + row1 + 1][1] = null;
					}

				}

				epsilon[row1][0] = row1 + row2 + 1;
				epsilon[row1][1] = null;
				epsilon[row1 + row2][0] = row1 + row2 + 1;
				epsilon[row1 + row2][1] = null;

				epsilon[row1 + row2 + 1][0] = null;
				epsilon[row1 + row2 + 1][1] = null;
			}

		} else if (au1 != null) {
			int row1 = au1.auto.length;

			if (root == RegEx.ETOILE) {

				// auto
				this.auto = new Integer[row1 + 2][256];

				for (int i = 0; i < 255; i++) {
					this.auto[0][i] = null;
				}
				for (int i = 0; i < row1; i++) {
					for (int j = 0; j < 255; j++) {
						if (au1.auto[i][j] != null) {
							this.auto[i + 1][j] = au1.auto[i][j] + 1;
						} else {
							this.auto[i + 1][j] = null;
						}

					}
				}
				for (int i = 0; i < 255; i++) {
					this.auto[row1 + 1][i] = null;
				}

				// init

				init = new boolean[row1 + 2];

				this.init[0] = true;
				for (int i = 1; i < row1 + 2; i++) {
					this.init[i] = false;
				}

				// fin

				fin = new boolean[row1 + 2];
				this.fin[row1 + 1] = true;
				for (int i = 0; i < row1 + 1; i++) {
					this.fin[i] = false;
				}

				// Epsilon
				epsilon = new Integer[row1 + 2][2];
				epsilon[0][0] = 1;
				epsilon[0][1] = row1 + 1;

				for (int i = 0; i < row1 - 1; i++) {
					if (au1.epsilon[i][0] != null && au1.epsilon[i][1] != null) {
						epsilon[i + 1][0] = au1.epsilon[i][0] + 1;
						epsilon[i + 1][1] = au1.epsilon[i][1] + 1;
					} else if (au1.epsilon[i][0] != null) {
						epsilon[i + 1][0] = au1.epsilon[i][0] + 1;
						epsilon[i + 1][1] = null;
					} else if (au1.epsilon[i][1] != null) {
						epsilon[i + 1][0] = null;
						epsilon[i + 1][1] = au1.epsilon[i][1] + 1;
					} else {
						epsilon[i + 1][0] = null;
						epsilon[i + 1][1] = null;
					}
				}

				epsilon[row1][0] = row1 + 1;
				epsilon[row1][1] = 1;

				epsilon[row1 + 1][0] = null;
				epsilon[row1 + 1][1] = null;

			}

		} else if (au2 != null) {

			int row1 = au2.auto.length;

			if (root == RegEx.ETOILE) {
				// auto
				this.auto = new Integer[row1 + 2][256];

				for (int i = 0; i < 255; i++) {
					this.auto[0][i] = null;
				}
				for (int i = 0; i < row1; i++) {
					for (int j = 0; j < 255; j++) {
						if (au2.auto[i][j] != null) {
							this.auto[i + 1][j] = au2.auto[i][j] + 1;
						} else {
							this.auto[i + 1][j] = null;
						}

					}
				}
				for (int i = 0; i < 255; i++) {
					this.auto[row1 + 1][i] = null;
				}

				// init

				init = new boolean[row1 + 2];

				this.init[0] = true;
				for (int i = 1; i < row1 + 2; i++) {
					this.init[i] = false;
				}

				// fin

				fin = new boolean[row1 + 2];
				this.fin[row1 + 1] = true;
				for (int i = 0; i < row1 + 1; i++) {
					this.fin[i] = false;
				}

				// Epsilon
				epsilon = new Integer[row1 + 2][2];
				epsilon[0][0] = 1;
				epsilon[0][1] = row1 + 1;

				for (int i = 0; i < row1 - 1; i++) {
					if (au2.epsilon[i][0] != null && au2.epsilon[i][1] != null) {
						epsilon[i + 1][0] = au2.epsilon[i][0] + 1;
						epsilon[i + 1][1] = au2.epsilon[i][1] + 1;
					} else if (au2.epsilon[i][0] != null) {
						epsilon[i + 1][0] = au2.epsilon[i][0] + 1;
						epsilon[i + 1][1] = null;
					} else if (au2.epsilon[i][1] != null) {
						epsilon[i + 1][0] = null;
						epsilon[i + 1][1] = au2.epsilon[i][1] + 1;
					} else {
						epsilon[i + 1][0] = null;
						epsilon[i + 1][1] = null;
					}
				}

				epsilon[row1][0] = row1 + 1;
				epsilon[row1][1] = 1;

				epsilon[row1 + 1][0] = null;
				epsilon[row1 + 1][1] = null;
			}

		} else if (root == RegEx.DOT) {

			// auto

			this.auto = new Integer[2][256];

			for (int i = 0; i < 256; i++) {
				this.auto[0][i] = 1;
				this.auto[1][i] = null;
			}

			// init
			init = new boolean[2];
			init[0] = true;
			init[1] = false;

			// fin
			fin = new boolean[2];
			fin[0] = false;
			fin[1] = true;

			// Epsilon
			epsilon = new Integer[2][2];
			epsilon[0][0] = null;
			epsilon[0][1] = null;
			epsilon[1][0] = null;
			epsilon[1][1] = null;

		} else {

			// auto

			this.auto = new Integer[2][256];

			if (root < 256) {
				this.auto[0][root] = 1;

				for (int i = 0; i < 255; i++) {
					if (i == root)
						continue;
					this.auto[1][i] = null;
				}
			}

			// init
			init = new boolean[2];
			init[0] = true;
			init[1] = false;

			// fin
			fin = new boolean[2];
			fin[0] = false;
			fin[1] = true;

			// Epsilon
			epsilon = new Integer[2][2];
			epsilon[0][0] = null;
			epsilon[0][1] = null;
			epsilon[1][0] = null;
			epsilon[1][1] = null;

		}

	}

}
