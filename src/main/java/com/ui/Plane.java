package com.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Plane extends JPanel {
	private static final long serialVersionUID = -7142024150936780562L;

	private MainFrame parent;
	private int WIDTH = 800;
	private int HEIGHT = 400;
	private CirclePoint addedPoint;

	public Plane() {

	}

	public Plane(JFrame parent) {
		this.parent = (MainFrame) parent;
		makeGUI();
	}

	private void makeGUI() {
		setLayout(null);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(Color.LIGHT_GRAY);
	}

	public void addPoint(int xPos, int yPos, int pointClass) {
		setAddedPoint(new CirclePoint(xPos, yPos, pointClass, true));
		this.add(addedPoint);
		parent.getGui().getOutputTextArea().append("* Test Point: " + xPos + ":" + yPos + ":" + pointClass + "\n");
		parent.revalidate();
		parent.repaint();
	}

	public void start(int rangeX, int rangeY, int dataCount) {
		for (int i = 0; i < dataCount; i++) {
			int xPos = (int) (Math.random() * (this.getPreferredSize().getWidth() - CirclePoint.WIDTH));
			int yPos = (int) (Math.random() * (this.getPreferredSize().getHeight() - CirclePoint.HEIGHT));
			int pointClass;
			if (xPos < rangeX && yPos < rangeY) {
				pointClass = 1;
			} else {
				pointClass = 2;
			}

			parent.getCurrentClass()[i] = pointClass;
			for (int j = 0; j < 2; j++) {
				if (j > 0) {
					parent.getDataSet()[i][j] = yPos;
				} else {
					parent.getDataSet()[i][j] = xPos;
				}

			}

			this.add(new CirclePoint(xPos, yPos, pointClass));

			parent.revalidate();
			parent.repaint();
		}
	}

	public CirclePoint getAddedPoint() {
		return addedPoint;
	}

	public void setAddedPoint(CirclePoint addedPoint) {
		this.addedPoint = addedPoint;
	}

	public void reset() {
		this.removeAll();
		parent.revalidate();
		parent.repaint();
	}

	public int getWIDTH() {
		return WIDTH;
	}

	public void setWIDTH(int wIDTH) {
		WIDTH = wIDTH;
	}

	public int getHEIGHT() {
		return HEIGHT;
	}

	public void setHEIGHT(int hEIGHT) {
		HEIGHT = hEIGHT;
	}

}
