package com.ui;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import com.ai.LDA;
import com.utils.Utils;

import net.miginfocom.swing.MigLayout;

public class MainFrame extends JFrame implements Runnable {

	private static final long serialVersionUID = 4622747430398971888L;

	private Plane plane;
	private GUI gui;
	private double[][] dataSet;
	private int[] currentClass;

	public MainFrame() {
		Thread thread = new Thread(this, "MainFrame");
		thread.start();
	}

	private void addDefaultListeners() {
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				System.exit(0);
			}

		});
	}

	@Override
	public void run() {
		addDefaultListeners();
		makeGUI();
		Utils.setJFrameDefaults(this);
	}

	private void makeGUI() {
		this.setTitle("Class Prediction with Linear Discriminant Analysis");
		this.setLayout(new MigLayout("fillx, insets 5, top, left"));
		setPreferredSize(new Dimension(800, 600));

		plane = new Plane(this);
		setPlane(plane);
		add(plane, "cell 0 0");

		gui = new GUI(this);
		add(gui, "cell 0 1");

		showInformation();
	}

	private void showInformation() {
		getGui().getOutputTextArea().append("....::::INFORMATION::::....\n");
		getGui().getOutputTextArea()
				.append("* Class 1: A point with blue color and has smaller coordinates than Limit X and Y.\n");
		getGui().getOutputTextArea()
				.append("* Class 2: A point with red color and has bigger or equal coordinates than Limit X and Y.\n");
		getGui().getOutputTextArea().append("* Training set is equal to the number of points.\n");
		getGui().getOutputTextArea()
				.append("* Test Point: A point with green color and has coordinates for Test X and Y.\n");
		getGui().getOutputTextArea().append(
				"* Analysis tries to estimate the class of the green(test) point. If it is located in blue area it must be class 1 other than that class 2.\n");
	}

	public void addPoint(int xPos, int yPos, int pointClass) {
		getPlane().addPoint(xPos, yPos, pointClass);
	}

	public Plane getPlane() {
		return plane;
	}

	public void setPlane(Plane uzay) {
		this.plane = uzay;
	}

	public GUI getGui() {
		return gui;
	}

	public void setGui(GUI gui) {
		this.gui = gui;
	}

	public void start(int rangeX, int rangeY, int dataCount) {
		setCurrentClass(new int[dataCount]);
		setDataSet(new double[dataCount][2]);
		getPlane().start(rangeX, rangeY, dataCount);
	}

	public int[] getCurrentClass() {
		return currentClass;
	}

	public void setCurrentClass(int[] currentClass) {
		this.currentClass = currentClass;
	}

	public double[][] getDataSet() {
		return dataSet;
	}

	public void setDataSet(double[][] dataSet) {
		this.dataSet = dataSet;
	}

	public void analysis() {
		LDA test = new LDA(getDataSet(), getCurrentClass(), true);
		double[] testData = { getPlane().getAddedPoint().xPos, getPlane().getAddedPoint().yPos };

		getGui().getOutputTextArea().append("....::::DISCRIMINANT FUNCTION::::....\n");

		double[] values = test.getDiscriminantFunctionValues(testData);
		for (int i = 0; i < values.length; i++) {
			getGui().getOutputTextArea().append("Discriminant function " + (i + 1) + ": " + values[i] + "\n");
		}

		getGui().getOutputTextArea().append("....::::MAHALANOBIS DISTANCE::::....\n");

		double[] valuesM = test.getMahalanobisDistance(testData);
		for (int i = 0; i < valuesM.length; i++) {
			getGui().getOutputTextArea().append("D " + (i + 1) + ": " + valuesM[i] + "\n");
		}

		getGui().getOutputTextArea().append("....::::PREDICTION RESULT::::....\n");

		int result = test.predict(testData);
		int resultM = test.predictM(testData);
		getGui().getOutputTextArea().append("* Discriminant function predicted class: " + result + "\n");
		getGui().getOutputTextArea().append("* Mahalanobis distance predicted class: " + resultM + "\n");
	}

	public void reset() {
		resetData();
		getGui().reset();
		getPlane().reset();
	}

	private void resetData() {
		setDataSet(null);
		setCurrentClass(null);
	}
}
