package com.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class CirclePoint extends JPanel {

	private static final long serialVersionUID = 2132127208789180612L;

	public static int WIDTH = 25;
	public static int HEIGHT = 25;
	public int groupID;
	public int xPos, yPos;
	public int pointClass;

	private int R = WIDTH / 2;
	private boolean isAnalysis = false;

	public CirclePoint(int xPos, int yPos, int pClass, boolean isAnalysis) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.pointClass = pClass;
		this.isAnalysis = isAnalysis;

		setLocation(new Point(xPos, yPos));
		this.setSize(new Dimension(WIDTH, HEIGHT));
	}

	public CirclePoint(int xPos, int yPos, int pClass) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.pointClass = pClass;

		setLayout(null);
		setLocation(new Point(xPos, yPos));
		this.setSize(new Dimension(WIDTH, HEIGHT));
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D graphics2d = (Graphics2D) g;
		drawCenteredCircle(graphics2d, xPos, yPos, R);
	}

	public void drawCenteredCircle(Graphics2D g, int x, int y, int r) {

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (isAnalysis) {
			g.setColor(Color.GREEN);
		} else if (pointClass == 1) {
			g.setColor(Color.BLUE);
		} else if (pointClass == 2) {
			g.setColor(Color.RED);
		}
		g.fillOval(0, 0, r, r);
	}
}
