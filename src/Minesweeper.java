import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;

public class Minesweeper extends JFrame {
	private static Random randy = new Random();
	private static Scanner reader = new Scanner(System.in);
	private static JButton[][] buttonBoard;
	private static int[][] bombBoard, bombCounter;
	private static boolean[][] pressed;
	private static int size, x, y, counter = 0, hsTime, sweepTime = 1000,
			bombs;
	private static long time1, time2;
	private static boolean win = false;
	private static JPanel bg;

	public Minesweeper() {
		buttonBoard = new JButton[size][size];
		bombBoard = new int[size][size];
		bombCounter = new int[size][size];
		pressed = new boolean[size][size];

		BorderLayout layout = new BorderLayout();
		Container mainWindow = getContentPane();
		mainWindow.setLayout(layout);

		bg = new JPanel();
		mainWindow.add(bg, BorderLayout.CENTER);
		bg.setLayout(new GridLayout(size, size));

		insertBombs(bombs);
		initiateArrays();
		fillBombCounter();

		setTitle("Minesweeper");
		setResizable(true);
		setSize(500 + (size - 10) * 30, 420 + (size - 10) * 30);
		setVisible(true);
	}

	public static class ButtonCheck implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String value = e.getActionCommand();
			int x = Integer.parseInt("" + value.charAt(0)), y = Integer
					.parseInt("" + value.charAt(1));
			Font font2 = new Font("Courier New", Font.BOLD, 250 / size - 3);
			if (bombBoard[x][y] == 0) {
				setColor(x, y);
				if (bombCounter[x][y] != 0)
					buttonBoard[x][y].setText("" + bombCounter[x][y]);
				else
					buttonBoard[x][y].setBackground(Color.gray);
				buttonBoard[x][y].setFont(font2);

				if (pressed[x][y] == false)
					counter++;
				pressed[x][y] = true;
				if (counter == Math.pow(size, 2) - bombs) {
					time2 = System.currentTimeMillis();
					sweepTime = (int) ((time2 - time1) / 1000);
					JOptionPane.showMessageDialog(null, "You Win!  "
							+ sweepTime + " seconds.", "Game Over",
							JOptionPane.INFORMATION_MESSAGE);
					win = true;
					endGame();
				}
			} else {
				JOptionPane.showMessageDialog(null, "BOOOOOOOOOOOOOOOM!!!",
						"Game Over", JOptionPane.INFORMATION_MESSAGE);
				endGame();
			}
		}
	}

	public static void endGame() {
		for (int y = 0; y < size; y++)
			for (int x = 0; x < size; x++) {
				if (bombBoard[x][y] == 0) {
					if (bombCounter[x][y] != 0)
						buttonBoard[x][y].setText("" + bombCounter[x][y]);
					else
						buttonBoard[x][y].setBackground(Color.gray);
				} else
					buttonBoard[x][y].setText("B");
			}

		for (int y = 0; y < size; y++)
			for (int x = 0; x < size; x++)
				buttonBoard[x][y].setEnabled(false);
	}

	public static void fillBombCounter() {
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				for (int xCheck = x - 1; xCheck <= x + 1; xCheck++) {
					if ((xCheck == x - 1 && x != 0)
							|| (xCheck == x + 1 && x != size - 1)
							|| (xCheck == x)) {
						if (y != 0)
							if (bombBoard[xCheck][y - 1] == 1)
								bombCounter[x][y]++;
						if (bombBoard[xCheck][y] == 1)
							bombCounter[x][y]++;
						if (y != size - 1)
							if (bombBoard[xCheck][y + 1] == 1)
								bombCounter[x][y]++;
					}
				}
				if (bombBoard[x][y] == 1)
					bombCounter[x][y]--;
			}
		}
	}

	public static void insertBombs(int bombs) {
		for (int x = 0; x < bombs; x++) {
			int xBomb, yBomb;
			do {
				xBomb = randy.nextInt(size);
				yBomb = randy.nextInt(size);
			} while (bombBoard[xBomb][yBomb] == 1);
			bombBoard[xBomb][yBomb] = 1;
		}
	}

	public static void initiateArrays() {
		for (int y = 0; y < size; y++)
			for (int x = 0; x < size; x++) {
				if (bombBoard[x][y] != 1)
					bombBoard[x][y] = 0;
				buttonBoard[x][y] = new JButton("");
				buttonBoard[x][y].setBackground(new Color(220, 220, 220));
				buttonBoard[x][y].addActionListener(new ButtonCheck());
				buttonBoard[x][y].setActionCommand("" + x + y);
				bg.add(buttonBoard[x][y]);
				bombCounter[x][y] = 0;
				pressed[x][y] = false;
			}
	}

	public static void setColor(int x, int y) {
		buttonBoard[x][y].setForeground(Color.black);
		switch (bombCounter[x][y]) {
		case 1:
			buttonBoard[x][y].setForeground(Color.blue);
			break;
		case 2:
			buttonBoard[x][y].setForeground(Color.green);
			break;
		case 3:
			buttonBoard[x][y].setForeground(Color.red);
			break;
		case 4:
			buttonBoard[x][y].setForeground(new Color(0, 0, 128));
			break;
		case 5:
			buttonBoard[x][y].setForeground(Color.green);
			break;
		case 6:
			buttonBoard[x][y].setForeground(new Color(0, 250, 154));
			break;
		case 7:
			buttonBoard[x][y].setForeground(Color.cyan);
			break;
		case 8:
			buttonBoard[x][y].setForeground(Color.blue);
			break;
		}
	}

	public static void main(String[] args) throws IOException {
		boolean valid = true;
		String inputStr;
		do {
			inputStr = JOptionPane.showInputDialog("Board Size? (2-20)", "10");
			if (inputStr == null)
				return;
			size = Integer.parseInt(inputStr);
			if (size < 2 || size > 20)
				JOptionPane.showMessageDialog(null, "Error: Size must be > 0");
		} while (size < 0);

		do {
			inputStr = JOptionPane.showInputDialog("Bombs?", "10");
			if (inputStr == null)
				return;
			bombs = Integer.parseInt(inputStr);
			if (bombs < 0 || bombs > Math.pow(size, 2))
				JOptionPane
						.showMessageDialog(null,
								"Error: Bombs must be > 0 or less than amount of spaces");
		} while (bombs < 0 || bombs > Math.pow(size, 2));

		time1 = System.currentTimeMillis();
		Minesweeper app = new Minesweeper();
	}
}